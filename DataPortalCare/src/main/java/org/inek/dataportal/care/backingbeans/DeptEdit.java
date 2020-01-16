/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.care.bo.AggregatedWards;
import org.inek.dataportal.care.entities.*;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChangesBaseInformation;
import org.inek.dataportal.care.entities.version.MapVersion;
import org.inek.dataportal.care.facades.DeptFacade;
import org.inek.dataportal.care.facades.StructuralChangesFacade;
import org.inek.dataportal.care.utils.AggregatedWardsHelper;
import org.inek.dataportal.care.utils.CareExcelExporter;
import org.inek.dataportal.care.utils.CareValidator;
import org.inek.dataportal.care.utils.CareValueChecker;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.ReportController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.TransferFileCreator;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.utils.DateUtils;
import org.inek.dataportal.common.utils.VzUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.time.Month;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.inek.dataportal.api.helper.PortalConstants.EXCEL_EXTENSION;
import static org.inek.dataportal.common.enums.TransferFileType.CareWardNames;
import static org.inek.dataportal.common.utils.DateUtils.MAX_DATE;
import static org.inek.dataportal.common.utils.DateUtils.createDate;

/**
 * @author lautenti
 */
@Named
@ViewScoped
public class DeptEdit implements Serializable {

    private static Logger LOGGER = Logger.getLogger("DeptEdit");

    //todo: reduce injections and change from field to constructor injection
    @Inject
    private SessionController _sessionController;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private DeptFacade _deptFacade;
    @Inject
    private StructuralChangesFacade _structuralChangesFacade;
    @Inject
    private Mailer _mailer;
    @Inject
    private ApplicationTools _applicationTools;
    @Inject
    private ConfigFacade _configFacade;
    @Inject
    private transient VzUtils _vzUtils;
    @Inject
    private ReportController _reportController;

    private DeptBaseInformation _deptBaseInformation;
    private DeptBaseInformation _oldDeptbaseInformation;
    private Boolean _isReadOnly;
    private Set<Integer> _validIks;
    private String _errorMessages = "";
    private List<String> _stationPrefillNames = new ArrayList<>();

    private Set<Integer> _allowedP21LocationCodes = new HashSet<>();

    // quick and ugly extensions by mail (todo: remove after 2020-01-10!)
    private List<Integer> extensions = Stream.of(260611258, 260590059, 260590139, 260200035, 260591619, 260950124,
            260833519, 260550643, 260590242, 260340568, 261110027, 260551837, 261101300, 260551154, 260960785, 260820433,
            260510212, 260970082, 260821229, 260551165, 260940688, 260820013, 260531661).collect(Collectors.toList());

    public DeptEdit() {
    }

    public List<AggregatedWards> getAggregatedWards() {
        return new AggregatedWardsHelper().aggregatedWards(_deptBaseInformation.obtainCurrentWards());
    }

    public Set<Integer> getValidIks() {
        return _validIks;
    }

    public void setValidIks(Set<Integer> validIks) {
        this._validIks = validIks;
    }

    public Boolean getIsReadOnly() {
        return _isReadOnly;
    }

    public void setIsReadOnly(Boolean isReadOnly) {
        this._isReadOnly = isReadOnly;
    }

    public DeptBaseInformation getDeptBaseInformation() {
        return _deptBaseInformation;
    }

    public void setDeptBaseInformation(DeptBaseInformation deptBaseInformation) {
        this._deptBaseInformation = deptBaseInformation;
    }

    public String getErrorMessages() {
        return _errorMessages;
    }

    public List<String> getStationPrefillNames() {
        return _stationPrefillNames;
    }

    public void setStationPrefillNames(List<String> stationPrefillNames) {
        this._stationPrefillNames = stationPrefillNames;
    }

    @PostConstruct
    private void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
            return;
        } else if ("new".equals(id)) {
            _deptBaseInformation = createNewDeptBaseInformation();
            loadValidIks();

            if (_validIks.size() == 1) {
                int ik = (int) _validIks.toArray()[0];
                _deptBaseInformation.setIk(ik);
                // todo: remove after 2020-01-10
                if (extensions.contains(ik)) {
                    _deptBaseInformation.setExtensionRequested(DateUtils.createDate(2019, Month.DECEMBER, 31));
                }
                preloadDataForIk(_deptBaseInformation);
                loadP21LocationsForIk(_deptBaseInformation.getIk(), _deptBaseInformation.getYear());
                loadStationPrefillNames(_deptBaseInformation.getIk(), _deptBaseInformation.getYear() - 1);
            }
        } else {
            try {
                _deptBaseInformation = _deptFacade.findDeptBaseInformation(Integer.parseInt(id));
                loadP21LocationsForIk(_deptBaseInformation.getIk(), _deptBaseInformation.getYear());
                loadStationPrefillNames(_deptBaseInformation.getIk(), _deptBaseInformation.getYear() - 1);
                if (!isAccessAllowed(_deptBaseInformation)) {
                    Utils.navigate(Pages.NotAllowed.RedirectURL());
                    return;
                }
                loadStationsAfterTargetYear(_deptBaseInformation);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "No DeptBaseInfo found for " + id);
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
        }
        setReadOnly();
    }

    private boolean isAccessAllowed(DeptBaseInformation info) {
        return _accessManager.isAccessAllowed(Feature.CARE, info.getStatus(),
                Integer.MIN_VALUE, info.getIk());
    }

    private void loadP21LocationsForIk(int ik, int year) {
        _allowedP21LocationCodes = _deptFacade.findP21LocationCodesForIkAndYear(ik, year);
    }

    private void loadStationPrefillNames(int ik, int year) {
        _stationPrefillNames = _deptFacade.findStationNamesForPrefill(ik, year);
    }

    private void setReadOnly() {
        if (!_configFacade.readConfigBool(ConfigKey.IsCareSendEnabled) || _deptBaseInformation == null) {
            setIsReadOnly(true);
            return;
        }
        setIsReadOnly(_accessManager.isReadOnly(Feature.CARE,
                _deptBaseInformation.getStatus(),
                Integer.MIN_VALUE,
                _deptBaseInformation.getIk()));
    }

    private DeptBaseInformation createNewDeptBaseInformation() {
        DeptBaseInformation info = new DeptBaseInformation();
        info.setCurrentVersion(new MapVersion(_sessionController.getAccountId()));
        info.setCreatedBy(_sessionController.getAccountId());
        info.setStatus(WorkflowStatus.New);
        info.setCreated(new Date());
        info.setYear(2018);
        return info;
    }

    public void ikChanged() {
        preloadDataForIk(_deptBaseInformation);
        loadP21LocationsForIk(_deptBaseInformation.getIk(), _deptBaseInformation.getYear());
        loadStationPrefillNames(_deptBaseInformation.getIk(), _deptBaseInformation.getYear() - 1);
        // todo: remove after 2020-01-10
        if (extensions.contains(_deptBaseInformation.getIk())) {
            _deptBaseInformation.setExtensionRequested(DateUtils.createDate(2019, Month.DECEMBER, 31));
        } else {
            _deptBaseInformation.setExtensionRequested(DateUtils.MIN_DATE);
        }
    }

    public void save() {
        boolean needsTransfer = _deptBaseInformation.getId() == -1;
        save(needsTransfer);
    }

    public void save(boolean needsTransfer) {
        _deptBaseInformation.setLastChangeBy(_sessionController.getAccountId());
        _deptBaseInformation.setLastChanged(new Date());

        try {
            if (_oldDeptbaseInformation != null) {
                _deptFacade.save(_oldDeptbaseInformation);
                _oldDeptbaseInformation = null;
            }

            _deptBaseInformation = _deptFacade.save(_deptBaseInformation);

            if (_deptBaseInformation.getStatus() == WorkflowStatus.Provided) {
                sendMail("Care Senden Bestätigung");
            } else {
                sendMail("Care Speicher Bestätigung");
            }
            DialogController.showSaveDialog();
            if (needsTransfer) {
                TransferFileCreator.createObjectTransferFile(_sessionController, _deptBaseInformation, _deptBaseInformation.getIk(), CareWardNames);
            }
        } catch (Exception ex) {
            _mailer.sendError("Fehler beim Speichern PPUG", ex);
            DialogController.showErrorDialog("Fehler beim Speichern", "Ihre Daten konnten nicht gespeichert werden. "
                    + "Bitte versuchen Sie es erneut");
        }
    }

    public void send() {
        if (isAllowedForSend()) {
            String errors = CareValidator.checkDeptBaseinformationIsAllowedToSend(_deptBaseInformation);

            if (errors.isEmpty()) {
                _deptBaseInformation.setSend(new Date());
                _deptBaseInformation.setStatus(WorkflowStatus.Provided);
                save(true);

                setIsReadOnly(true);
            } else {
                DialogController.showErrorDialog("Daten nicht vollständig", errors);
            }
        } else {
            DialogController.openDialogByName("errorMessageDialog");
        }
    }

    private boolean isAllowedForSend() {
        _errorMessages = new AggregatedWardsHelper()
                .aggregatedWards(_deptBaseInformation.obtainCurrentWards())
                .stream()
                .filter(w -> w.getDifferentBedCount())
                .map(w -> String.format(AggregatedWardsHelper.ERROR_MESSAGE_MULTIPLE_BEDS, w.getWardName(), w.getDistinctBedCounts()))
                .collect(Collectors.joining("\n"));
        return _errorMessages.isEmpty();
    }

    private void loadValidIks() {
        Set<Integer> allowedIks = _accessManager.obtainIksForCreation(Feature.CARE);
        setValidIks(_deptFacade.retrievePossibleIks(allowedIks));
    }

    public void addNewStation(Dept dept) {
        dept.addNewInitialDeptWard(_deptBaseInformation.getCurrentVersion(), createNewValidFromDate(), MAX_DATE);
    }

    private Date createNewValidFromDate() {
        return createDate(_deptBaseInformation.getYear(), Month.JANUARY, 1);
    }

    private void preloadDataForIk(DeptBaseInformation info) {
        _deptFacade.prefillDeptsForBaseInformation(info);
    }

    public List<Dept> getDeptsByArea(int area) {
        if (_deptBaseInformation == null) {
            return new ArrayList<>();
        }
        return _deptBaseInformation.getDepts().stream()
                .filter(c -> c.getDeptArea() == area)
                .collect(Collectors.toList());
    }

    private List<DeptArea> areas;

    public String getDeptAreaText(int areaId) {
        if (areas == null) {
            areas = _deptFacade.getAreas();
        }
        return areas.stream().filter(a -> a.getId() == areaId).map(a -> a.getText()).findAny().orElse("???");
    }

    public void deleteStationFromDept(Dept dept, DeptWard station) {
        dept.removeDeptStation(station);
    }

    public StreamedContent exportAsExcel() {
        CareExcelExporter exporter = new CareExcelExporter();
        String hospitalName = _applicationTools.retrieveHospitalName(_deptBaseInformation.getIk());
        String hospitalTown = _applicationTools.retrieveHospitalTown(_deptBaseInformation.getIk());
        String fileName = "Mitteilung gem. Paragraph 5 PpUGV_" + _deptBaseInformation.getIk();
        StreamedContent content = new DefaultStreamedContent(exporter.createExcelExportFile(_deptBaseInformation,
                hospitalName, hospitalTown), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fileName + EXCEL_EXTENSION);

        return content;
    }

    public StreamedContent exportAsExcel2018() {
        String fileName = "Mitteilung gem. Paragraph 5 PpUGV_" + _deptBaseInformation.getIk();

        byte[] singleDocument = _reportController.getSingleDocument("PPUG_DEPT_2018",
                _deptBaseInformation.getId(), fileName);

        return new DefaultStreamedContent(new ByteArrayInputStream(singleDocument),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fileName + EXCEL_EXTENSION);
    }

    public StreamedContent exportAsExcelWithStructuralChanges() {
        String fileName = "Mitteilung gem. Paragraph 5 PpUGV_" + _deptBaseInformation.getIk();

        byte[] singleDocument = _reportController.getSingleDocument("PPUG_STRUCTURAL_CHANGES",
                _deptBaseInformation.getId(), fileName);

        return new DefaultStreamedContent(new ByteArrayInputStream(singleDocument),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fileName + EXCEL_EXTENSION);
    }


    private void sendMail(String template) {
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("{ik}", "" + _deptBaseInformation.getIk());
        substitutions.put("{year}", "" + _deptBaseInformation.getYear());
        Date date = createDate(_deptBaseInformation.getYear() + 2, Month.JANUARY, 10);
        substitutions.put("{date}", DateUtils.toGerman(date));
        Account account = _sessionController.getAccount();
        boolean success = _sessionController.getMailer().sendMailWithTemplate(template, substitutions, account);


        if (!success) {
            _mailer.sendException(Level.SEVERE,
                    "Fehler beim Emailversand an " + _deptBaseInformation.getIk() + "(Care)", new Exception());
        }
    }

    public Boolean changeAllowed() {
        if (!_configFacade.readConfigBool(ConfigKey.IsCareChangeEnabled)) {
            return false;
        }
        if (_deptBaseInformation == null || _deptBaseInformation.getStatusId() < 10) {
            return false;
        } else {
            return _accessManager.userHasWriteAccess(Feature.CARE, _deptBaseInformation.getIk()) && _deptBaseInformation.getYear() > 2017;
        }
    }

    public Boolean excelExportAllowed() {
        if (_deptBaseInformation == null || _deptBaseInformation.getStatusId() < 10) {
            return false;
        } else {
            return _accessManager.userHasReadAccess(Feature.CARE, _deptBaseInformation.getIk());
        }
    }

    public Boolean saveAllowed() {
        return !isAfterEndDate();
    }

    private boolean isAfterEndDate() {
        Date endDate = _deptBaseInformation == null || _deptBaseInformation.getExtensionRequested().equals(DateUtils.MIN_DATE)
                ? DateUtils.createDate(2019, Month.DECEMBER, 21)
                : DateUtils.createDate(2020, Month.JANUARY, 11);
        return new Date().compareTo(endDate) >= 0;
    }

    public Boolean sendAllowed() {
        if (isAfterEndDate()) return false;
        return _configFacade.readConfigBool(ConfigKey.IsCareSendEnabled);
    }

    public void change() {
        _oldDeptbaseInformation = _deptBaseInformation;
        _oldDeptbaseInformation.setStatus(WorkflowStatus.Retired);

        deleteStructuralChanges();
        _deptBaseInformation = new DeptBaseInformation(_oldDeptbaseInformation, _sessionController.getAccountId());
        ResetWards();
        _deptBaseInformation.setStatus(WorkflowStatus.CorrectionRequested);
        setIsReadOnly(false);
    }

    private void deleteStructuralChanges() {
        List<StructuralChangesBaseInformation> changes = _structuralChangesFacade.findBaseInformationsByIk(_deptBaseInformation.getIk());
        for (StructuralChangesBaseInformation change : changes) {
            _structuralChangesFacade.deleteBaseInformation(change);
        }

    }

    private void ResetWards() {
        MapVersion initialVersion = new MapVersion(_sessionController.getAccountId());
        for (Dept dept : _deptBaseInformation.getDepts()) {
            List<DeptWard> deptWards = new ArrayList<>(dept.getDeptWards());
            for (DeptWard ward : deptWards) {
                if (ward.getIsInitial()) {
                    initialVersion = ward.getMapVersion();
                } else {
                    dept.removeDeptStation(ward);
                }
            }
        }
        _deptBaseInformation.setCurrentVersion(initialVersion);
    }

    public void isFabCodeValid(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        if ("".equals(value.toString().trim())) {
            return;
        }

        if (!CareValueChecker.isValidFabNumber(value.toString())) {
            throw new ValidatorException(new FacesMessage("Ungültige FAB"));
        }
        if (!_deptFacade.isValidFab(value.toString())) {
            throw new ValidatorException(new FacesMessage("Ungültige FAB"));
        }
    }

    public void isP21LocationCodeValid(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        int locationCode = (Integer) value;

        if (_allowedP21LocationCodes.isEmpty() && locationCode == 0) {
            return;
        }

        if (!_allowedP21LocationCodes.contains(locationCode)) {
            throw new ValidatorException(new FacesMessage("Ungültiger Standort nach § 21 KHEntgG für diese IK"));
        }
    }

    public void isVZLocationCodeValid(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        int locationCode = (Integer) value;
        if (locationCode == 0) {
            // allow to remove the code
            return;
        }
        if (!CareValueChecker.isFormalValidVzNumber(value.toString())) {
            throw new ValidatorException(new FacesMessage("Ungültiger Standort für dieses IK"));
        }
        if (!_vzUtils.locationCodeIsValidForIk(_deptBaseInformation.getIk(), locationCode)) {
            List<Integer> iks = _deptFacade.retrievePriorIk(_deptBaseInformation.getIk());
            for (int ik : iks) {
                if (_vzUtils.locationCodeIsValidForIk(ik, locationCode)) {
                    return;
                }
            }
            throw new ValidatorException(new FacesMessage("Ungültiger Standort für dieses IK"));
        }
    }

    public void extractAndCheckLocationCode(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        int locationCode = CareValueChecker.extractFormalValidVzNumber("" + value);
        if (locationCode == 0) {
            return;
        }
        if (!_vzUtils.locationCodeIsValidForIk(_deptBaseInformation.getIk(), locationCode)) {
            List<Integer> iks = _deptFacade.retrievePriorIk(_deptBaseInformation.getIk());
            for (int ik : iks) {
                if (_vzUtils.locationCodeIsValidForIk(ik, locationCode)) {
                    return;
                }
            }
            throw new ValidatorException(new FacesMessage(
                    "In Ihrer Eingabe wurde eine Standortnummer erkannt. Sie ist jedoch für dieses IK ungültig."));
        }
    }

    //***************************************************************************
    // 2017
    //***************************************************************************
    private List<DeptStationsAfterTargetYear> _stationsAfterTargetYear = new ArrayList<>();

    public List<DeptStationsAfterTargetYear> getStationsAfterTargetYear() {
        return _stationsAfterTargetYear;
    }

    public void setStationsAfterTargetYear(List<DeptStationsAfterTargetYear> stationsAfterTargetYear) {
        this._stationsAfterTargetYear = stationsAfterTargetYear;
    }

    private void loadStationsAfterTargetYear(DeptBaseInformation info) {
        _stationsAfterTargetYear.clear();
        if (info.getYear() > 2017) {
            return;
        }

        for (Dept dept : info.getDepts()) {
            _stationsAfterTargetYear.addAll(dept.getDeptsAftertargetYear());
        }
    }

    public boolean isExtensionAllowed() {
        return _deptBaseInformation != null
                && _deptBaseInformation.getIk() > 0
                && isMinDate()
                && new Date().compareTo(DateUtils.createDate(2020, Month.JANUARY, 11)) < 0;
    }

    public boolean isMinDate() {
        return _deptBaseInformation != null && _deptBaseInformation.getExtensionRequested().compareTo(DateUtils.MIN_DATE) == 0;
    }

    public void applyExtension() {
        _deptBaseInformation.setExtensionRequested(new Date());
        save(true);
        sendMail("CareDeptExtensionRequested");
    }


}
