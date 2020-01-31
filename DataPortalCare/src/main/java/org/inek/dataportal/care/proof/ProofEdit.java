package org.inek.dataportal.care.proof;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.Extension;
import org.inek.dataportal.care.facades.BaseDataFacade;
import org.inek.dataportal.care.facades.DeptFacade;
import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.care.proof.entity.ProofDocument;
import org.inek.dataportal.care.proof.entity.ProofExceptionFact;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.inek.dataportal.care.proof.util.ProofAggregator;
import org.inek.dataportal.care.proof.util.ProofChecker;
import org.inek.dataportal.care.proof.util.ProofHelper;
import org.inek.dataportal.care.proof.util.ProofImporter;
import org.inek.dataportal.care.utils.BaseDataManager;
import org.inek.dataportal.care.utils.CalculatorPpug;
import org.inek.dataportal.care.utils.CareSignatureCreater;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.ReportController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.MailTemplateHelper;
import org.inek.dataportal.common.helper.TransferFileCreator;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.utils.DateUtils;
import org.inek.dataportal.common.utils.FromToDate;
import org.primefaces.component.api.UIColumn;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.inek.dataportal.common.enums.TransferFileType.PPUGV;

@Named
@ViewScoped
public class ProofEdit implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("ProofEdit");
    public static final String DATA_INCOMPLETE = "Daten unvollständig";
    @Inject
    private SessionController _sessionController;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private ProofFacade _proofFacade;
    @Inject
    private BaseDataFacade _baseDataFacade;
    @Inject
    private ConfigFacade _configFacade;
    @Inject
    private Mailer _mailer;
    @Inject
    private ReportController _reportController;
    @Inject
    private DeptFacade _deptFacade;

    private ProofRegulationBaseInformation _proofBaseInformation;
    private ProofRegulationBaseInformation _oldProofRegulationBaseInformation;
    private boolean _isReadOnly;
    private boolean _isExceptionFactsChangeMode = false;
    private String _uploadMessage;
    private List<ProofExceptionFact> _exceptionsFacts = new ArrayList<>();
    private BaseDataManager _baseDatamanager;
    private List<SelectItem> _listExceptionsFacts = new ArrayList<>();
    private List<SortMeta> _preSortOrder = new ArrayList<>();

    public List<SelectItem> getListExceptionsFacts() {
        return _listExceptionsFacts;
    }

    public void setListExceptionsFacts(List<SelectItem> listExceptionsFacts) {
        this._listExceptionsFacts = listExceptionsFacts;
    }

    public String getUploadMessage() {
        return _uploadMessage;
    }

    public void setUploadMessage(String uploadMessage) {
        this._uploadMessage = uploadMessage;
    }

    public List<ProofExceptionFact> getExceptionsFacts() {
        return _exceptionsFacts;
    }

    public void setExceptionsFacts(List<ProofExceptionFact> exceptionsFacts) {
        this._exceptionsFacts = exceptionsFacts;
    }

    public Set<Integer> getValidIks() {
        return possibleIkYearQuarters.stream().map(i -> i.getIk()).collect(Collectors.toSet());
    }

    public Set<Integer> getValidYears() {
        return possibleIkYearQuarters.stream()
                .filter(i -> i.getIk() == _proofBaseInformation.getIk())
                .map(i -> i.getYear()).collect(Collectors.toSet());
    }

    public Set<Integer> getValidQuarters() {
        return possibleIkYearQuarters.stream()
                .filter(i -> i.getIk() == _proofBaseInformation.getIk() && i.getYear() == _proofBaseInformation.getYear())
                .map(i -> i.getQuarter()).collect(Collectors.toSet());
    }

    public boolean getIsReadOnly() {
        return _isReadOnly;
    }

    public void setIsReadOnly(boolean isReadOnly) {
        this._isReadOnly = isReadOnly;
    }

    public boolean getIsExceptionFactsChangeMode() {
        return _isExceptionFactsChangeMode;
    }

    public void setIsExceptionFactsChangeMode(boolean isExceptionFactsChangeMode) {
        this._isExceptionFactsChangeMode = isExceptionFactsChangeMode;
    }

    public ProofRegulationBaseInformation getProofRegulationBaseInformation() {
        return _proofBaseInformation;
    }

    public void setProofRegulationBaseInformation(ProofRegulationBaseInformation proofRegulationBaseInformation) {
        this._proofBaseInformation = proofRegulationBaseInformation;
    }

    public List<SortMeta> getPreSortOrder() {
        return _preSortOrder;
    }

    public void setPreSortOrder(List<SortMeta> preSortOrder) {
        this._preSortOrder = preSortOrder;
    }

    private List<IkYearQuarter> possibleIkYearQuarters = Collections.emptyList();

    @PostConstruct
    private void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
            return;
        } else if ("new".equals(id)) {
            _proofBaseInformation = createNewBaseInformation();
            _proofBaseInformation.setCreatedBy(_accessManager.getSessionAccountId());
            Set<Integer> allowedIks = _accessManager.obtainIksForCreation(Feature.CARE);
            possibleIkYearQuarters = _proofFacade.retrievePossibleIkYearQuarters(allowedIks);
        } else {
            _proofBaseInformation = _proofFacade.findBaseInformation(Integer.parseInt(id));
            if (!isAccessAllowed(_proofBaseInformation)) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            loadBaseDataManager();
            loadExceptionsFactsList();
            fillExceptionsFactsList(_proofBaseInformation);
            _baseDatamanager.fillBaseDataToProofs(_proofBaseInformation.getProofs());
        }
        setReadOnly();
        buildSortOrder();
    }

    private void buildSortOrder() {
        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        String componentId = "form:proofTable:";
        addSortOrder(viewRoot, componentId, "psAreaId", SortOrder.ASCENDING);
        addSortOrder(viewRoot, componentId, "fabNumberId", SortOrder.ASCENDING);
        addSortOrder(viewRoot, componentId, "fabId", SortOrder.ASCENDING);
        addSortOrder(viewRoot, componentId, "stationNameId", SortOrder.ASCENDING);
        addSortOrder(viewRoot, componentId, "locationId", SortOrder.ASCENDING);
        addSortOrder(viewRoot, componentId, "monthId", SortOrder.ASCENDING);
        addSortOrder(viewRoot, componentId, "shiftId", SortOrder.ASCENDING);
    }

    private void addSortOrder(UIViewRoot root, String component, String colId, SortOrder order) {
        UIComponent column = root.findComponent(component + colId);
        SortMeta sm = new SortMeta();
        sm.setSortBy((UIColumn) column);
        sm.setSortField(colId);
        sm.setSortOrder(order);
        _preSortOrder.add(sm);
    }

    private void loadExceptionsFactsList() {
        _listExceptionsFacts = _proofFacade.getExceptionsFactsForYear(_proofBaseInformation.getYear());
    }

    private void fillExceptionsFactsList(ProofRegulationBaseInformation info) {
        _exceptionsFacts.clear();
        for (Proof proof : info.getProofs().stream()
                .filter(c -> c.getExceptionFact().size() > 0)
                .collect(Collectors.toList())) {
            _exceptionsFacts.addAll(proof.getExceptionFact());
        }
    }

    private boolean isAccessAllowed(ProofRegulationBaseInformation info) {
        return _accessManager.isAccessAllowed(Feature.CARE, info.getStatus(),
                Integer.MIN_VALUE, info.getIk());
    }

    private void setReadOnly() {
        if (!_configFacade.readConfigBool(ConfigKey.IsCareProofSendEnabled)) {
            setIsReadOnly(true);
            return;
        }
        if (_proofBaseInformation != null) {
            setIsReadOnly(_accessManager.isReadOnly(Feature.CARE,
                    _proofBaseInformation.getStatus(),
                    Integer.MIN_VALUE,
                    _proofBaseInformation.getIk()));
        }
    }

    private ProofRegulationBaseInformation createNewBaseInformation() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        info.setStatus(WorkflowStatus.New);
        info.setCreated(new Date());

        return info;
    }

    public void ikChanged() {
        _proofBaseInformation.setYear(0);
        validYearsChanged();
    }

    public void validYearsChanged() {
        _proofBaseInformation.setQuarter(0);
    }

    public void firstSave() {
        int year = _proofBaseInformation.getYear();
        int quarter = _proofBaseInformation.getQuarter();
        int ik = _proofBaseInformation.getIk();
        List<ProofWard> proofWards = new ArrayList<>();


        DeptBaseInformation deptBaseInfo = _deptFacade.findDeptBaseInformationByIkAndBaseYear(ik, 2018);
        String errorMsg = ProofChecker.checkForMissingLocationNumber(deptBaseInfo.obtainCurrentWards(), year, quarter);
        if (!errorMsg.isEmpty()) {
            DialogController.showErrorDialog(DATA_INCOMPLETE, errorMsg);
            return;
        }

        for (int month = quarter * 3 - 2; month <= quarter * 3; month++) {
            FromToDate dates = DateUtils.firstAndLastDayOfMonth(year, month);
            proofWards.addAll(ProofAggregator.aggregateDeptWards(deptBaseInfo.obtainCurrentWards(), dates.from(), dates.to()));
        }

        // todo
/*        List<ProofRegulationStation> stations = _proofFacade.getStationsForProof(_proofBaseInformation.getIk(),
                _proofBaseInformation.getYear());
        ProofFiller.createProofEntrysFromStations(_proofBaseInformation, stations,
                _proofBaseInformation.getYear(), _proofBaseInformation.getQuarter());
        loadBaseDataManager();
        loadExceptionsFactsList();
        save();
        _baseDatamanager.fillBaseDataToProofs(_proofBaseInformation.getProofs());
        setReadOnly();*/
    }


    private void loadBaseDataManager() {
        _baseDatamanager = new BaseDataManager(_proofBaseInformation.getYear(), _baseDataFacade);
    }

    public void save() {
        for (Proof proof : _proofBaseInformation.getProofs()) {
            calculateCountHelpeNurseChargeable(proof);
            calculatePatientPerNurse(proof);
        }

        List<String> errorMessages = ProofChecker.proofIsReadyForSave(_proofBaseInformation, _listExceptionsFacts.size());
        if (!errorMessages.isEmpty()) {
            DialogController.showErrorDialog(DATA_INCOMPLETE, errorMessages.get(0));
            return;
        }

        _proofBaseInformation.setLastChangeBy(_accessManager.getSessionAccountId());
        _proofBaseInformation.setLastChanged(new Date());

        try {
            if (_oldProofRegulationBaseInformation != null &&
                    (_proofBaseInformation.getStatus() == WorkflowStatus.CorrectionRequested ||
                            _proofBaseInformation.getStatus() == WorkflowStatus.Provided)) {
                _proofFacade.save(_oldProofRegulationBaseInformation);
            }

            _proofBaseInformation = _proofFacade.save(_proofBaseInformation);
            _baseDatamanager.fillBaseDataToProofs(_proofBaseInformation.getProofs());


            if (_proofBaseInformation.getStatus() == WorkflowStatus.Provided) {
                sendMail("Care Proof Senden Bestätigung");
                DialogController.showSendDialog();
            } else {
                sendMail("Care Proof Speicher Bestätigung");
                DialogController.showSaveDialog();
            }
        } catch (EJBException ex) {
            LOGGER.log(Level.INFO, "Fehler beim speichern PPUGV (" + _proofBaseInformation.getIk() + "): " +
                    "Eintrag wurde von jemanden anderen geändert");
            DialogController.showErrorDialog("Fehler beim speichern", "Ihre Daten konnten nicht gespeichert werden."
                    + "Bitte laden Sie die Meldung neu. Die Daten wurden bereits von einem anderen Benutzer geändert.");
        }
    }


    private void sendMail(String mailTemplateName) {
        String salutation = _mailer.getFormalSalutation(_accessManager.getSessionAccount());

        MailTemplate template = _mailer.getMailTemplate(mailTemplateName);
        MailTemplateHelper.setPlaceholderInTemplate(template, "{ik}", Integer.toString(_proofBaseInformation.getIk()));

        MailTemplateHelper.setPlaceholderInTemplateBody(template, "{salutation}", salutation);

        if (!_mailer.sendMailTemplate(template, _accessManager.getSessionAccount().getEmail())) {
            LOGGER.log(Level.SEVERE, "Fehler beim Emailversand an " + _proofBaseInformation.getIk() + "(Care Proof)");
            _mailer.sendException(Level.SEVERE,
                    "Fehler beim Emailversand an " + _proofBaseInformation.getIk() + "(Care Proof)", new Exception());
        }
    }

    public void send() {
        for (Proof proof : _proofBaseInformation.getProofs()) {
            calculateCountHelpeNurseChargeable(proof);
            calculatePatientPerNurse(proof);
        }

        List<String> errorMessages = ProofChecker.proofIsReadyForSend(_proofBaseInformation, _listExceptionsFacts.size());

        if (!errorMessages.isEmpty()) {
            for (String message : errorMessages) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unplausible Daten", message));
            }
            return;
        }


        _proofBaseInformation.setSend(new Date());
        _proofBaseInformation.setStatus(WorkflowStatus.Provided);
        _proofBaseInformation.setSignature(CareSignatureCreater.createPvSignature(_proofBaseInformation));
        save();
        try {
            TransferFileCreator.createObjectTransferFile(_sessionController, _proofBaseInformation,
                    _proofBaseInformation.getIk(), PPUGV);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error duringTransferFileCreation PPUG: ik: " + _proofBaseInformation.getIk());
            _mailer.sendError("Error duringTransferFileCreation PPUG: ik: " + _proofBaseInformation.getIk() + " year: " +
                    _proofBaseInformation.getYear() + " quarter: " + _proofBaseInformation.getQuarter(), ex);
        }
        setIsReadOnly(true);
    }

    public boolean excelExportAllowed() {
        if (_proofBaseInformation == null || _proofBaseInformation.getStatusId() < 10) {
            return false;
        } else {
            return _accessManager.userHasReadAccess(Feature.CARE, _proofBaseInformation.getIk());
        }
    }

    public boolean annualReportAllowed() {
        if (!excelExportAllowed()) {
            return false;
        }
        return _proofFacade.hasAllQuartersSend(_proofBaseInformation.getIk(), _proofBaseInformation.getYear());
    }

    public boolean changeAllowed() {
        if (!_configFacade.readConfigBool(ConfigKey.IsCareProofChangeEnabled)) {
            return false;
        }
        if (_proofBaseInformation == null || _proofBaseInformation.getStatusId() < 10) {
            return false;
        } else {
            return _accessManager.userHasWriteAccess(Feature.CARE, _proofBaseInformation.getIk());
        }
    }

    public boolean sendAllowed() {
        return _configFacade.readConfigBool(ConfigKey.IsCareProofSendEnabled);
    }

    public boolean sendAllowedForToday() {
        return ProofHelper.proofIsAllowedForSend(_proofBaseInformation);
    }

    public void change() {
        _oldProofRegulationBaseInformation = copyBaseInformation(_proofBaseInformation);
        _proofBaseInformation.setStatus(WorkflowStatus.CorrectionRequested);
        _proofBaseInformation.setSignature("");
        _proofBaseInformation.setSend(Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC)));
        _proofBaseInformation.setCreated(new Date());
        _proofBaseInformation.setCreatedBy(_accessManager.getSessionAccountId());
        setIsReadOnly(false);
    }

    public void changeExceptionsFacts() {
        setIsExceptionFactsChangeMode(true);
    }

    public void saveChangedExceptionFacts() {
        List<String> errorMessages = ProofChecker.proofIsReadyForSave(_proofBaseInformation, _listExceptionsFacts.size());
        if (!errorMessages.isEmpty()) {
            DialogController.showErrorDialog(DATA_INCOMPLETE, errorMessages.get(0));
            return;
        }

        try {
            _proofBaseInformation = _proofFacade.save(_proofBaseInformation);
            _baseDatamanager.fillBaseDataToProofs(_proofBaseInformation.getProofs());
            setIsExceptionFactsChangeMode(false);
            DialogController.showSaveDialog();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Fehler beim speichern PPUGV: " + ex.getMessage(), ex);
            _mailer.sendError("Fehler beim speichern PPUGV: " + ex.getMessage(), ex);
            DialogController.showErrorDialog("Fehler beim speichern", "Ihre Daten konnten nicht gespeichert werden."
                    + "Bitte versuchen Sie es erneut. Fehlercode: " + ex.getMessage());
        }
    }

    private ProofRegulationBaseInformation copyBaseInformation(ProofRegulationBaseInformation baseInfo) {
        ProofRegulationBaseInformation newInfo = new ProofRegulationBaseInformation(baseInfo);
        newInfo.setStatus(WorkflowStatus.Retired);
        newInfo.setLastChangeBy(_accessManager.getSessionAccountId());
        newInfo.setLastChanged(new Date());
        return newInfo;
    }

    public void navigateToSummary() {
        Utils.navigate(Pages.CareProofSummary.RedirectURL());
    }

    public double calculatePatientPerNurse(Proof proof) {
        CalculatorPpug.calculateAll(proof);
        return proof.getPatientPerNurse();
    }

    public double calculateCountHelpeNurseChargeable(Proof proof) {
        CalculatorPpug.calculateAll(proof);
        return proof.getCountHelpeNurseChargeable();
    }

    public void addNewException(Proof proof) {
        ProofExceptionFact exceptionFact = new ProofExceptionFact();
        exceptionFact.setProof(proof);
        proof.addExceptionFact(exceptionFact);
        _exceptionsFacts.add(exceptionFact);
    }

    public void deleteExceptionsFact(ProofExceptionFact exceptionFact) {
        _exceptionsFacts.remove(exceptionFact);
        exceptionFact.getProof().removeExceptionFact(exceptionFact);
    }

    public List<Proof> getProofsForExceptionFact() {
        return _proofBaseInformation.getProofs().stream()
                .filter(c -> c.getPatientPerNurse() > c.getPpug() || c.getCountShiftNotRespected() > 0)
                .filter(c -> c.getExceptionFact().size() < _listExceptionsFacts.size())
                .collect(Collectors.toList());
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            ProofImporter importer = new ProofImporter(true);
            importer.handleProofUpload(_proofBaseInformation, event.getFile().getInputstream());
            setUploadMessage(importer.getMessage());
        } catch (Exception ex) {
            DialogController.showWarningDialog("Upload fehlgeschlagen", "Fehler beim Upload. Bitte versuchen Sie es erneut");
            LOGGER.log(Level.WARNING, "Error on upload ppugv-excel: " + ex.getMessage(), ex);
        }
    }

    public StreamedContent downloadExcelTemplate() {
        byte[] singleDocument = _reportController.getSingleDocument("PPUGV_Poof_Upload_Template",
                _proofBaseInformation.getId(), "Upload_Vorlage");

        return new DefaultStreamedContent(new ByteArrayInputStream(singleDocument),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "Upload_Vorlage.xlsx");

    }

    public StreamedContent exportQuarterAsExcel() {
        String fileName = "Nachweis_" + _proofBaseInformation.getIk() + "_Q" +
                _proofBaseInformation.getQuarter() + "_" +
                _proofBaseInformation.getYear() + ".xlsx";

        byte[] singleDocument = _reportController.getSingleDocument("PPUGV_Proof_Quarter_Report",
                _proofBaseInformation.getId(), fileName);

        return new DefaultStreamedContent(new ByteArrayInputStream(singleDocument),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fileName);
    }

    public StreamedContent exportAnnualReportAsExcel() {
        String fileName = "Nachweis_" + _proofBaseInformation.getIk() +
                _proofBaseInformation.getYear() + ".xlsx";

        byte[] singleDocument = _reportController.getSingleDocumentByIkAndYear("PPUGV_Proof_Annual_Report",
                _proofBaseInformation.getIk(),
                _proofBaseInformation.getYear(), fileName);

        return new DefaultStreamedContent(new ByteArrayInputStream(singleDocument),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fileName);
    }

    public void requestExtension() {
        int ik = _proofBaseInformation.getIk();
        int year = _proofBaseInformation.getYear();
        int quarter = _proofBaseInformation.getQuarter();
        Extension extension = new Extension(ik, year, quarter, _accessManager.getSessionAccountId());
        extension.setAccountId(_accessManager.getSessionAccountId());
        _proofFacade.saveExtension(extension);
        sendExtensionMail(ik, year, quarter);
    }

    private boolean sendExtensionMail(int ik, int year, int quarter) {
        int extensionYear = year + (quarter == 4 ? 1 : 0);
        int extensionMonth = quarter == 4 ? 1 : quarter * 3 + 1;
        LocalDate extensionDate = LocalDate.of(extensionYear, extensionMonth, 29);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        MailTemplate template = _mailer.getMailTemplate("CareProofExtension");
        MailTemplateHelper.setPlaceholderInTemplate(template, "{formalSalutation}",
                _mailer.getFormalSalutation(_accessManager.getSessionAccount()));
        MailTemplateHelper.setPlaceholderInTemplate(template, "{ik}", String.valueOf(ik));
        MailTemplateHelper.setPlaceholderInTemplate(template, "{quarter}", String.valueOf(quarter));
        MailTemplateHelper.setPlaceholderInTemplate(template, "{year}", String.valueOf(year));
        MailTemplateHelper.setPlaceholderInTemplate(template, "{date}", extensionDate.format(formatter));

        DialogController.showInfoDialog("Erfolgreich beantragt", "Sie haben erfolgreich eine " +
                "Fristverlängerung beantragt. Sie erhalten eine Bestätung per E-Mail.");
        return _mailer.sendMailTemplate(template, _accessManager.getSessionAccount().getEmail());
    }

    public boolean getRequestExtensionAllowed() {
        int ik = _proofBaseInformation.getIk();
        int year = _proofBaseInformation.getYear();
        int quarter = _proofBaseInformation.getQuarter();
        int extensionYear = year + (quarter == 4 ? 1 : 0);
        int extensionMonth = quarter == 4 ? 1 : quarter * 3 + 1;
        LocalDate extensionDate = LocalDate.of(extensionYear, extensionMonth, 15);
        if (LocalDate.now().isAfter(extensionDate)) {
            return false;
        }
        return !_proofFacade.hasExtension(ik, year, quarter);
    }

    public void uploadDocument(FileUploadEvent event) {
        LOGGER.log(Level.INFO, "File uploaded: " + event.getFile().getFileName());
        putDocument(event.getFile().getFileName(), event.getFile().getContents());
        DialogController.showInfoDialog("Upload erfolgreich",
                "Die Datei " + event.getFile().getFileName() + " wurde erfolgreich hochgeladen");
    }

    private List<String> allowedFileExtensions() {
        return Arrays.asList(".pdf", ".jpg", ".jpeg", ".png", ".bmp", ".gif");
    }

    public void putDocument(String fileName, byte[] content) {

        int pos = fileName.lastIndexOf(".");
        String extension = pos < 0 ? "" : fileName.toLowerCase().substring(pos);
        if (allowedFileExtensions().contains(extension)) {
            ProofDocument document = new ProofDocument(fileName);
            document.setIk(_proofBaseInformation.getIk());
            document.setYear(_proofBaseInformation.getYear());
            document.setContent(content);
            _proofFacade.saveProofDocument(document);
            documentName = fileName;
        }
    }

    public String downloadDocument() {
        ProofDocument doc = _proofFacade.findProofDocumentByIkAndYear(
                _proofBaseInformation.getIk(),
                _proofBaseInformation.getYear()
        );
        Utils.downloadDocument(doc);
        return "";
    }

    private String documentName;

    public String getDocumentName() {
        if (documentName == null) {
            documentName = _proofFacade.findProofDocumentNameByIkAndYear(
                    _proofBaseInformation.getIk(),
                    _proofBaseInformation.getYear()
            );
        }
        return documentName;
    }

    public List<Proof> getProofs() {
        // display data in old format
        return _proofBaseInformation.getProofs().stream().filter(p -> p.getProofRegulationStationId() == 0).collect(Collectors.toList());
    }

    public Date determineStartDate (Proof proof){
        return DateUtils.firstAndLastDayOfMonth(_proofBaseInformation.getYear(), proof.getMonth().getId()).from();
    }
}
