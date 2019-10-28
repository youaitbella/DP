/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.care.bo.AggregatedWards;
import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.DeptStation;
import org.inek.dataportal.care.entities.DeptStationsAfterTargetYear;
import org.inek.dataportal.care.facades.DeptFacade;
import org.inek.dataportal.care.utils.CareExcelExporter;
import org.inek.dataportal.care.utils.CareValidator;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.MailTemplateHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.scope.FeatureScoped;
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
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @author lautenti
 */
@Named
@ViewScoped
public class DeptEdit implements Serializable {

    @Inject
    private SessionController _sessionController;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private DeptFacade _deptFacade;
    @Inject
    private Mailer _mailer;
    @Inject
    private ApplicationTools _applicationTools;
    @Inject
    private ConfigFacade _configFacade;

    private DeptBaseInformation _deptBaseInformation;
    private DeptBaseInformation _oldDeptbaseInformation;
    private Boolean _isReadOnly;
    private Set<Integer> _validIks;
    private List<AggregatedWards> _aggregatedWards = new ArrayList<>();

    private Set<Integer> _allowedP21LocationCodes = new HashSet<>();

    public List<AggregatedWards> getAggregatedWards() {
        return _aggregatedWards;
    }

    public void setAggregatedWards(List<AggregatedWards> aggregatedWards) {
        this._aggregatedWards = aggregatedWards;
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

    @PostConstruct
    private void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
            return;
        } else if ("new".equals(id)) {
            _deptBaseInformation = createNewDeptBaseInformation();
            _deptBaseInformation.setCreatedBy(_sessionController.getAccountId());
            loadValidIks();

            if (_validIks.size() == 1) {
                _deptBaseInformation.setIk((int) _validIks.toArray()[0]);
                preloadDataForIk(_deptBaseInformation);
                loadP21LocationsForIk(_deptBaseInformation.getIk(), _deptBaseInformation.getYear());
            }
        } else {
            _deptBaseInformation = _deptFacade.findDeptBaseInformation(Integer.parseInt(id));
            loadP21LocationsForIk(_deptBaseInformation.getIk(), _deptBaseInformation.getYear());
            if (!isAccessAllowed(_deptBaseInformation)) {
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

    private void setReadOnly() {
        if (!_configFacade.readConfigBool(ConfigKey.IsCareSendEnabled)) {
            setIsReadOnly(true);
            return;
        }
        if (_deptBaseInformation != null) {
            setIsReadOnly(_accessManager.isReadOnly(Feature.CARE,
                    _deptBaseInformation.getStatus(),
                    Integer.MIN_VALUE,
                    _deptBaseInformation.getIk()));
        }
    }

    private DeptBaseInformation createNewDeptBaseInformation() {
        DeptBaseInformation info = new DeptBaseInformation();

        info.setStatus(WorkflowStatus.New);
        info.setCreated(new Date());
        info.setYear(2018);

        return info;
    }

    public void ikChanged() {
        preloadDataForIk(_deptBaseInformation);
        loadP21LocationsForIk(_deptBaseInformation.getIk(), _deptBaseInformation.getYear());
    }

    public void save() {
        _deptBaseInformation.setLastChangeBy(_sessionController.getAccountId());
        _deptBaseInformation.setLastChanged(new Date());

        try {
            if (_oldDeptbaseInformation != null && _deptBaseInformation.getStatus() == WorkflowStatus.CorrectionRequested) {
                _deptFacade.save(_oldDeptbaseInformation);
            }

            _deptBaseInformation = _deptFacade.save(_deptBaseInformation);

            if (_deptBaseInformation.getStatus() == WorkflowStatus.Provided) {
                sendMail("Care Senden Bestätigung");
            } else {
                sendMail("Care Speicher Bestätigung");
            }
            DialogController.showSaveDialog();
        } catch (Exception ex) {
            _mailer.sendError("Fehler beim speichern PPUG", ex);
            DialogController.showErrorDialog("Fehler beim speichern", "Ihre Daten konnten nicht gespeichert werden."
                    + "Bitte versuchen Sie es erneut");
        }
    }

    public void send() {
        String errors = CareValidator.checkDeptBaseinformationIsAllowedToSend(_deptBaseInformation);

        if (errors.isEmpty()) {
            _deptBaseInformation.setSend(new Date());
            _deptBaseInformation.setStatus(WorkflowStatus.Provided);
            save();
            setIsReadOnly(true);
        } else {
            DialogController.showErrorDialog("Daten nicht vollständig", errors);
        }
    }

    private void loadValidIks() {
        Set<Integer> allowedIks = _accessManager.obtainIksForCreation(Feature.CARE);
        setValidIks(_deptFacade.retrievePossibleIks(allowedIks));
    }

    public void addNewStation(Dept dept) {

        dept.addNewDeptStation(createNewValidFromDate(), createNewValidToDate());
    }

    private Date createNewValidFromDate() {
        return createDate(1, Month.JANUARY, _deptBaseInformation.getYear(), 0, 0, 1);
    }

    private Date createNewValidToDate() {
        return createDate(31, Month.DECEMBER, 2069, 23, 59, 58);
    }

    private Date createDate(int day, Month month, int year, int hour, int minute, int second) {
        LocalDateTime datetime = LocalDateTime.of(year, month, day, hour, minute, second);
        return java.util.Date.from(datetime
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    private void preloadDataForIk(DeptBaseInformation info) {
        _deptFacade.prefillDeptsForBaseInformation(info);
    }

    public List<Dept> getDeptsByArea(int area) {
        if (_deptBaseInformation == null) {
            return new ArrayList<Dept>();
        }
        return _deptBaseInformation.getDepts().stream()
                .filter(c -> c.getDeptArea() == area)
                .collect(Collectors.toList());
    }

    public void deleteStationFromDept(Dept dept, DeptStation station) {
        dept.removeDeptStation(station);
    }

    public StreamedContent exportAsExcel() {
        CareExcelExporter exporter = new CareExcelExporter();
        String hospitalName = _applicationTools.retrieveHospitalName(_deptBaseInformation.getIk());
        String hospitalTown = _applicationTools.retrieveHospitalTown(_deptBaseInformation.getIk());
        String fileName = "Mitteilung gem. Paragraph 5 PpUGV_" + _deptBaseInformation.getIk();
        StreamedContent content = new DefaultStreamedContent(exporter.createExcelExportFile(_deptBaseInformation,
                hospitalName, hospitalTown), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fileName + ".xlsx");

        return content;
    }


    private void sendMail(String mailTemplateName) {
        String salutation = _mailer.getFormalSalutation(_sessionController.getAccount());

        MailTemplate template = _mailer.getMailTemplate(mailTemplateName);
        MailTemplateHelper.setPlaceholderInTemplate(template, "{ik}", Integer.toString(_deptBaseInformation.getIk()));

        MailTemplateHelper.setPlaceholderInTemplateBody(template, "{salutation}", salutation);

        if (!_mailer.sendMailTemplate(template, _sessionController.getAccount().getEmail())) {
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
            return _accessManager.userHasWriteAccess(Feature.CARE, _deptBaseInformation.getIk());
        }
    }

    public Boolean excelExportAllowed() {
        if (_deptBaseInformation == null || _deptBaseInformation.getStatusId() < 10) {
            return false;
        } else {
            return _accessManager.userHasReadAccess(Feature.CARE, _deptBaseInformation.getIk());
        }
    }

    public Boolean sendAllowed() {
        return _configFacade.readConfigBool(ConfigKey.IsCareSendEnabled);
    }

    public void change() {
        _deptBaseInformation.setStatus(WorkflowStatus.CorrectionRequested);
        _oldDeptbaseInformation = copyBaseInformation(_deptBaseInformation);
        setIsReadOnly(false);
    }

    private DeptBaseInformation copyBaseInformation(DeptBaseInformation deptBaseInformation) {
        DeptBaseInformation baseInfo = new DeptBaseInformation(deptBaseInformation);
        baseInfo.setStatus(WorkflowStatus.Retired);
        return baseInfo;
    }

    public void isP21LocationCodeValid(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        int locationCode = (Integer) value;

        if (_allowedP21LocationCodes.isEmpty() && locationCode == 0) {
            return;
        }

        if (!_allowedP21LocationCodes.contains(locationCode)) {
            throw new ValidatorException(new FacesMessage("Ungültiger P21 - Standort für diese IK"));
        }
    }

    public void isVZLocationCodeValid(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        int locationCode = (Integer) value;

        //TODO Check VZ REST
    }

}
