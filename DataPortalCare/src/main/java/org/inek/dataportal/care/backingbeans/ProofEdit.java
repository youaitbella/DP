package org.inek.dataportal.care.backingbeans;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.care.entities.Proof;
import org.inek.dataportal.care.entities.ProofExceptionFact;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;
import org.inek.dataportal.care.entities.ProofRegulationStation;
import org.inek.dataportal.care.facades.BaseDataFacade;
import org.inek.dataportal.care.facades.ProofFacade;
import org.inek.dataportal.care.utils.*;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.MailTemplateHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.overall.AccessManager;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author lautenti
 */
@Named
@ViewScoped
public class ProofEdit implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("ProofEdit");
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

    private ProofRegulationBaseInformation _proofRegulationBaseInformation;
    private ProofRegulationBaseInformation _oldProofRegulationBaseInformation;
    private Boolean _isReadOnly;
    private String _uploadMessage;
    private Set<Integer> _validIks;
    private Set<Integer> _validYears;
    private Set<Integer> _validQuarters;
    private List<ProofExceptionFact> _exceptionsFacts = new ArrayList<>();
    private BaseDataManager _baseDatamanager;
    private List<SelectItem> _listExceptionsFacts;

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
        return _validIks;
    }

    public void setValidIks(Set<Integer> validIks) {
        this._validIks = validIks;
    }

    public Set<Integer> getValidYears() {
        return _validYears;
    }

    public void setValidYears(Set<Integer> validYears) {
        this._validYears = validYears;
    }

    public Set<Integer> getValidQuarters() {
        return _validQuarters;
    }

    public void setValidQuarters(Set<Integer> validQuarters) {
        this._validQuarters = validQuarters;
    }

    public Boolean getIsReadOnly() {
        return _isReadOnly;
    }

    public void setIsReadOnly(Boolean isReadOnly) {
        this._isReadOnly = isReadOnly;
    }

    public ProofRegulationBaseInformation getProofRegulationBaseInformation() {
        return _proofRegulationBaseInformation;
    }

    public void setProofRegulationBaseInformation(ProofRegulationBaseInformation proofRegulationBaseInformation) {
        this._proofRegulationBaseInformation = proofRegulationBaseInformation;
    }

    @PostConstruct
    private void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
            return;
        } else if ("new".equals(id)) {
            _proofRegulationBaseInformation = createNewBaseInformation();
            _proofRegulationBaseInformation.setCreatedBy(_sessionController.getAccountId());

            loadValidIks();
        } else {
            _proofRegulationBaseInformation = _proofFacade.findBaseInformation(Integer.parseInt(id));
            if (!isAccessAllowed(_proofRegulationBaseInformation)) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            loadBaseDataManager();
            loadExceptionsFactsList();
            fillExceptionsFactsList(_proofRegulationBaseInformation);
            _baseDatamanager.fillBaseDataToProofs(_proofRegulationBaseInformation.getProofs());
        }
        setReadOnly();
    }

    private void loadExceptionsFactsList() {
        _listExceptionsFacts = _proofFacade.getExceptionsFactsForYear(_proofRegulationBaseInformation.getYear());
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
        if (_proofRegulationBaseInformation != null) {
            setIsReadOnly(_accessManager.isReadOnly(Feature.CARE,
                    _proofRegulationBaseInformation.getStatus(),
                    Integer.MIN_VALUE,
                    _proofRegulationBaseInformation.getIk()));
        }
    }

    private ProofRegulationBaseInformation createNewBaseInformation() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        info.setStatus(WorkflowStatus.New);
        info.setCreated(new Date());

        return info;
    }

    public void ikChanged() {
        loadValidYears(_proofRegulationBaseInformation.getIk());
    }

    public void validYearsChanged() {
        loadValidQuarter(_proofRegulationBaseInformation.getIk(), _proofRegulationBaseInformation.getYear());
    }

    private void loadValidQuarter(int ik, int year) {
        _validQuarters = _proofFacade.retrievePossibleQuarter(ik, year);
    }

    private void loadValidYears(int ik) {
        _validYears = _proofFacade.retrievePossibleYears(ik);
    }

    public void firstSave() {
        List<ProofRegulationStation> stations = _proofFacade.getStationsForProof(_proofRegulationBaseInformation.getIk(),
                _proofRegulationBaseInformation.getYear());
        ProofFiller.createProofEntrysFromStations(_proofRegulationBaseInformation, stations,
                _proofRegulationBaseInformation.getYear(), _proofRegulationBaseInformation.getQuarter());
        loadBaseDataManager();
        loadExceptionsFactsList();
        save();
        _baseDatamanager.fillBaseDataToProofs(_proofRegulationBaseInformation.getProofs());
        setReadOnly();
    }

    private void loadBaseDataManager() {
        _baseDatamanager = new BaseDataManager(_proofRegulationBaseInformation.getYear(), _baseDataFacade);
    }

    public void save() {
        List<String> errorMessages = ProofChecker.proofIsReadyForSave(_proofRegulationBaseInformation, _listExceptionsFacts.size());
        if (!errorMessages.isEmpty()) {
            DialogController.showErrorDialog("Daten unvollständig", errorMessages.get(0));
            return;
        }

        _proofRegulationBaseInformation.setLastChangeBy(_sessionController.getAccountId());
        _proofRegulationBaseInformation.setLastChanged(new Date());

        try {
            if (_oldProofRegulationBaseInformation != null &&
                    _proofRegulationBaseInformation.getStatus() == WorkflowStatus.CorrectionRequested) {
                _proofFacade.save(_oldProofRegulationBaseInformation);
            }

            _proofRegulationBaseInformation = _proofFacade.save(_proofRegulationBaseInformation);
            _baseDatamanager.fillBaseDataToProofs(_proofRegulationBaseInformation.getProofs());


            if (_proofRegulationBaseInformation.getStatus() == WorkflowStatus.Provided) {
                sendMail("Care Proof Senden Bestätigung");
                DialogController.showSendDialog();
            } else {
                sendMail("Care Proof Speicher Bestätigung");
                DialogController.showSaveDialog();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Fehler beim speichern PPUGV: " + ex.getMessage(), ex);
            _mailer.sendError("Fehler beim speichern PPUGV: " + ex.getMessage(), ex);
            DialogController.showErrorDialog("Fehler beim speichern", "Ihre Daten konnten nicht gespeichert werden."
                    + "Bitte versuchen Sie es erneut. Fehlercode: " + ex.getMessage());
        }
    }


    private void sendMail(String mailTemplateName) {
        String salutation = _mailer.getFormalSalutation(_sessionController.getAccount());

        MailTemplate template = _mailer.getMailTemplate(mailTemplateName);
        MailTemplateHelper.setPlaceholderInTemplateSubject(template, "{ik}", Integer.toString(_proofRegulationBaseInformation.getIk()));

        MailTemplateHelper.setPlaceholderInTemplateBody(template, "{salutation}", salutation);
        MailTemplateHelper.setPlaceholderInTemplateBody(template, "{ik}", Integer.toString(_proofRegulationBaseInformation.getIk()));

        if (!_mailer.sendMailTemplate(template, _sessionController.getAccount().getEmail())) {
            LOGGER.log(Level.SEVERE, "Fehler beim Emailversand an " + _proofRegulationBaseInformation.getIk() + "(Care Proof)");
            _mailer.sendException(Level.SEVERE,
                    "Fehler beim Emailversand an " + _proofRegulationBaseInformation.getIk() + "(Care Proof)", new Exception());
        }
    }

    public void send() {
        List<String> errorMessages = ProofChecker.proofIsReadyForSend(_proofRegulationBaseInformation, _listExceptionsFacts.size());

        if (!errorMessages.isEmpty()) {
            for (String message : errorMessages) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unplausible Daten", message));
            }
            return;
        }
        _proofRegulationBaseInformation.setSend(new Date());
        _proofRegulationBaseInformation.setStatus(WorkflowStatus.Provided);
        _proofRegulationBaseInformation.setSignature(CareSignatureCreater.createPvSignature());
        save();
        setIsReadOnly(true);
    }

    private void loadValidIks() {
        Set<Integer> allowedIks = _accessManager.ObtainIksForCreation(Feature.CARE);
        setValidIks(_proofFacade.retrievePossibleIks(allowedIks));
    }

    public Boolean changeAllowed() {
        if (!_configFacade.readConfigBool(ConfigKey.IsCareProofChangeEnabled)) {
            return false;
        }
        if (_proofRegulationBaseInformation == null || _proofRegulationBaseInformation.getStatusId() < 10) {
            return false;
        } else {
            return _accessManager.isWriteAllowed(Feature.CARE, _proofRegulationBaseInformation.getIk());
        }
    }

    public Boolean sendAllowed() {
        return _configFacade.readConfigBool(ConfigKey.IsCareProofSendEnabled);
    }

    public void change() {
        _oldProofRegulationBaseInformation = copyBaseInformation(_proofRegulationBaseInformation);
        _proofRegulationBaseInformation.setStatus(WorkflowStatus.CorrectionRequested);
        _proofRegulationBaseInformation.setSignature("");
        _proofRegulationBaseInformation.setSend(Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC)));
        _proofRegulationBaseInformation.setCreated(new Date());
        _proofRegulationBaseInformation.setCreatedBy(_sessionController.getAccountId());
        setIsReadOnly(false);
    }

    private ProofRegulationBaseInformation copyBaseInformation(ProofRegulationBaseInformation baseInfo) {
        ProofRegulationBaseInformation newInfo = new ProofRegulationBaseInformation(baseInfo);
        newInfo.setStatus(WorkflowStatus.Retired);
        newInfo.setLastChangeBy(_sessionController.getAccountId());
        newInfo.setLastChanged(new Date());
        return newInfo;
    }

    public void navigateToSummary() {
        Utils.navigate(Pages.CareProofSummary.RedirectURL());
    }

    public double calculatePatientPerNurse(Proof proof) {
        CallculatorPpug.calculateAll(proof);
        return proof.getPatientPerNurse();
    }

    public double calculateCountHelpeNurseChargeable(Proof proof) {
        CallculatorPpug.calculateAll(proof);
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
        return _proofRegulationBaseInformation.getProofs().stream()
                .filter(c -> c.getPatientPerNurse() > c.getPpug())
                .filter(c -> c.getExceptionFact().size() < _listExceptionsFacts.size())
                .collect(Collectors.toList());
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            ProofImporter importer = new ProofImporter();
            importer.handleProofUpload(_proofRegulationBaseInformation, event.getFile().getInputstream());
            setUploadMessage(importer.getMessage());
        } catch (Exception ex) {
            DialogController.showWarningDialog("Upload fehlgeschlagen", "Fehler beim Upload. Bitte versuchen Sie es erneut");
            LOGGER.log(Level.WARNING, "Error on upload ppugv-excel: " + ex.getMessage(), ex);
        }
    }

    public StreamedContent downloadExcelTemplate() {

        /* TODO: use ReportServer*/
        /*byte[] singleDocument = _reportController.getSingleDocument("PPUGV_Poof_Upload_Template",
        _proofRegulationBaseInformation.getId(), "TestName");

        StreamedContent content = new DefaultStreamedContent(new ByteArrayInputStream(singleDocument),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "Test.xlsx");
        return content;*/

        ProofExcelExporter exporter = new ProofExcelExporter();
        String fileName = "Upload_Vorlage";
        StreamedContent content = new DefaultStreamedContent(exporter.createExcelExportFile(_proofRegulationBaseInformation),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fileName + ".xlsx");

        return content;

    }
}
