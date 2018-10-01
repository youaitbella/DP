/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.DeptStation;
import org.inek.dataportal.care.facades.DeptFacade;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.MailTemplateHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;

/**
 *
 * @author lautenti
 */
@Named
@FeatureScoped
public class DeptEdit {

    @Inject
    private SessionController _sessionController;
    @Inject
    private DialogController _dialogController;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private DeptFacade _deptFacade;
    @Inject
    private Mailer _mailer;

    private DeptBaseInformation _deptBaseInformation;
    private Boolean _isReadOnly;
    private Set<Integer> _validIks;

    private Dept _selectedDept;

    public Dept getSelectedDept() {
        return _selectedDept;
    }

    public void setSelectedDept(Dept selectedDept) {
        this._selectedDept = selectedDept;
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
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
            return;
        } else if ("new".equals(id)) {
            _deptBaseInformation = createNewDeptBaseInformation();
            _deptBaseInformation.setCreatedFrom(_sessionController.getAccountId());
            loadValidIks();

            if (_validIks.size() == 1) {
                _deptBaseInformation.setIk((int) _validIks.toArray()[0]);
                preloadDataForIk(_deptBaseInformation);
            }
        } else {
            _deptBaseInformation = _deptFacade.findDeptBaseInformation(Integer.parseInt(id));
        }
        setReadOnly();
    }

    private void setReadOnly() {
        if (_deptBaseInformation != null) {
            setIsReadOnly(_accessManager.isReadOnly(Feature.CARE,
                    _deptBaseInformation.getStatus(),
                    _sessionController.getAccountId(),
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

    public void createNewDeptStation() {
        _selectedDept.addNewDeptStation();
    }

    public void save() {
        _deptBaseInformation.setLastChangeFrom(_sessionController.getAccountId());
        _deptBaseInformation.setLastChanged(new Date());

        try {
            _deptBaseInformation = _deptFacade.save(_deptBaseInformation);
            if (_deptBaseInformation.getStatus() == WorkflowStatus.Provided) {
                sendMail("Care Senden Bestätigung");
            } else {
                sendMail("Care Speicher Bestätigung");
            }
            _dialogController.showSaveDialog();
        } catch (Exception ex) {
            _dialogController.showErrorDialog("Fehler beim speichern", "Ihre Daten konnten nicht gespeichert werden."
                    + "Bitte versuchen Sie es erneut");
        }
    }

    public void send() {
        _deptBaseInformation.setSend(new Date());
        _deptBaseInformation.setStatus(WorkflowStatus.Provided);
        save();
        setIsReadOnly(true);
    }

    private void loadValidIks() {
        Set<Integer> iks = new HashSet<>();

        iks.add(222222222);

        setValidIks(iks);
    }

    public void addNewStation(Dept dept) {
        dept.addNewDeptStation();
    }

    private void preloadDataForIk(DeptBaseInformation info) {
        _deptFacade.prefillDeptsForBaseInformation(info);
    }

    public List<Dept> getDeptsByArea(int area) {
        return _deptBaseInformation.getDepts().stream()
                .filter(c -> c.getDeptArea() == area)
                .collect(Collectors.toList());
    }

    public void deleteStationFromDept(Dept dept, DeptStation station) {
        dept.removeDeptStation(station);
    }

    private void sendMail(String mailTemplateName) {
        String salutation = _mailer.getFormalSalutation(_sessionController.getAccount());

        MailTemplate template = _mailer.getMailTemplate(mailTemplateName);
        MailTemplateHelper.setPlaceholderInTemplateBody(template, "{salutation}", salutation);
        if (!_mailer.sendMailTemplate(template, _sessionController.getAccount().getEmail())) {
            _mailer.sendException(Level.SEVERE,
                    "Fehler beim Emailversand an " + _deptBaseInformation.getIk() + "(Care)", new Exception());
        }
    }

}