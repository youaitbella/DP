/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.facades.DeptFacade;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;

/**
 *
 * @author lautenti
 */
@Named
@FeatureScoped
public class DeptSummary {

    @Inject
    private AccessManager _accessManager;
    @Inject
    private SessionController _sessionController;
    @Inject
    private DeptFacade _deptFacade;

    private List<DeptBaseInformation> _listComplete = new ArrayList<>();
    private List<DeptBaseInformation> _listWorking = new ArrayList<>();

    public List<DeptBaseInformation> getListComplete() {
        return _listComplete;
    }

    public void setListComplete(List<DeptBaseInformation> listComplete) {
        this._listComplete = listComplete;
    }

    public List<DeptBaseInformation> getListWorking() {
        return _listWorking;
    }

    public void setListWorking(List<DeptBaseInformation> listWorking) {
        this._listWorking = listWorking;
    }

    @PostConstruct
    public void init() {
        setWorkingList();
        setCompleteList();
    }

    private void setWorkingList() {
        _listWorking.clear();
        for (AccessRight right : _sessionController.getAccount().getAccessRights().stream()
                .filter(c -> c.canRead() && c.getFeature() == Feature.CARE)
                .collect(Collectors.toList())) {
            _listWorking.addAll(_deptFacade.getAllByStatusAndIk(WorkflowStatus.New, right.getIk()));
            _listWorking.addAll(_deptFacade.getAllByStatusAndIk(WorkflowStatus.CorrectionRequested, right.getIk()));
        }
    }

    private void setCompleteList() {
        _listComplete.clear();
        for (AccessRight right : _sessionController.getAccount().getAccessRights().stream()
                .filter(c -> c.canRead() && c.getFeature() == Feature.HC_HOSPITAL)
                .collect(Collectors.toList())) {
            _listComplete.addAll(_deptFacade.getAllByStatusAndIk(WorkflowStatus.Provided, right.getIk()));
        }
    }

    public String careDeptStationOpen() {
        return Pages.CareDeptEdit.URL();
    }

    public boolean isCreateEntryAllowed() {
        //Todo Just for development
        return true;
        //Set<Integer> allowedIks = _accessManager.ObtainIksForCreation(Feature.CARE);
        //return _deptFacade.retrievePossibleIks(allowedIks).size() > 0;
    }

    public void deleteBaseInformation(DeptBaseInformation info) {
        _deptFacade.deleteBaseInformation(info);
        setWorkingList();
    }

    public Boolean isDeleteAllowed(int ik) {
        return !_sessionController.getAccount().getAccessRights().stream()
                .filter(c -> c.getFeature() == Feature.CARE
                && c.getIk() == ik
                && c.canWrite())
                .collect(Collectors.toList())
                .isEmpty();
    }
}
