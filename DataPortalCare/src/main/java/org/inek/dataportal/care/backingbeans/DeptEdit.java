/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.facades.DeptFacade;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;
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
        } else {
            _deptBaseInformation = _deptFacade.findDeptBaseInformation(Integer.parseInt(id));
        }
        setReadOnly();
        loadValidIks();
    }

    private void setReadOnly() {
        if (_deptBaseInformation != null) {
            setIsReadOnly(false);
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
        // Todo Preload Stations
    }

    public void createNewDeptStation() {
        _selectedDept.addNewDeptStation();
    }

    public void save() {
        _deptBaseInformation.setLastChangeFrom(_sessionController.getAccountId());
        _deptBaseInformation.setLastChanged(new Date());

        try {
            _deptBaseInformation = _deptFacade.save(_deptBaseInformation);
            _dialogController.showSaveDialog();
        } catch (Exception ex) {
            _dialogController.showErrorDialog("Fehler beim speichern", "Ihre Daten konnten nicht gespeichert werden."
                    + "Bitte versuchen Sie es erneut");
        }
    }

    public void send() {
        _deptBaseInformation.setSend(new Date());
        _deptBaseInformation.setStatus(WorkflowStatus.Taken);
        save();
        setIsReadOnly(true);
    }

    private void loadValidIks() {
        Set<Integer> iks = new HashSet<>();

        iks.add(222222222);

        setValidIks(iks);
    }

}
