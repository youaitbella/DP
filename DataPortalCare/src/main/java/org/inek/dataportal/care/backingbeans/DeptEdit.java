/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import org.inek.dataportal.api.enums.Feature;
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
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
    private List<DeptStationsAfterTargetYear> _stationsAfterTargetYear = new ArrayList<>();

    public List<DeptStationsAfterTargetYear> getStationsAfterTargetYear() {
        return _stationsAfterTargetYear;
    }

    public void setStationsAfterTargetYear(List<DeptStationsAfterTargetYear> stationsAfterTargetYear) {
        this._stationsAfterTargetYear = stationsAfterTargetYear;
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
            }
        } else {
            _deptBaseInformation = _deptFacade.findDeptBaseInformation(Integer.parseInt(id));
            if (!isAccessAllowed(_deptBaseInformation)) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            loadStationsAfterTargetYear(_deptBaseInformation);
        }
        setReadOnly();
    }

    private boolean isAccessAllowed(DeptBaseInformation info) {
        return _accessManager.isAccessAllowed(Feature.CARE, info.getStatus(),
                Integer.MIN_VALUE, info.getIk());
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
        info.setYear(2017);

        return info;
    }

    public void ikChanged() {
        preloadDataForIk(_deptBaseInformation);
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
        Set<Integer> allowedIks = _accessManager.ObtainIksForCreation(Feature.CARE);
        setValidIks(_deptFacade.retrievePossibleIks(allowedIks));
    }

    public void addNewStation(Dept dept) {
        dept.addNewDeptStation();
    }

    public void addNewDeptAfterTargetYear(Dept dept) {
        DeptStationsAfterTargetYear station = new DeptStationsAfterTargetYear();
        station.setDept(dept);
        dept.addDeptAfterTargetYear(station);
        _stationsAfterTargetYear.add(station);
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

    public void deleteDeptAfterTargetYear(DeptStationsAfterTargetYear dept) {
        dept.getDept().removeDeptAfterTargetYear(dept);
        _stationsAfterTargetYear.remove(dept);
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
        MailTemplateHelper.setPlaceholderInTemplateSubject(template, "{ik}", Integer.toString(_deptBaseInformation.getIk()));

        MailTemplateHelper.setPlaceholderInTemplateBody(template, "{salutation}", salutation);
        MailTemplateHelper.setPlaceholderInTemplateBody(template, "{ik}", Integer.toString(_deptBaseInformation.getIk()));

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
            for (AccessRight right : _sessionController.getAccount().getAccessRights().stream()
                    .filter(c -> c.canWrite() && c.getFeature() == Feature.CARE)
                    .collect(Collectors.toList())) {
                if (right.getIk() == _deptBaseInformation.getIk()) {
                    return true;
                }
            }

            return false;
        }
    }

    public Boolean excelExportAllowed() {
        if (_deptBaseInformation == null || _deptBaseInformation.getStatusId() < 10) {
            return false;
        } else {
            for (AccessRight right : _sessionController.getAccount().getAccessRights().stream()
                    .filter(c -> c.canRead() && c.getFeature() == Feature.CARE)
                    .collect(Collectors.toList())) {
                if (right.getIk() == _deptBaseInformation.getIk()) {
                    return true;
                }
            }
            return false;
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

    private void loadStationsAfterTargetYear(DeptBaseInformation info) {
        _stationsAfterTargetYear.clear();
        for (Dept dept : info.getDepts()) {
            _stationsAfterTargetYear.addAll(dept.getDeptsAftertargetYear());
        }
    }

}
