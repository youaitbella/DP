/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.insurance.khcomparison.backingbean;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.KhComparison.entities.AEBBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.StructureInformation;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.enums.CustomerTyp;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author lautenti
 */
@Named
@FeatureScoped
public class Summary {

    @Inject
    private AEBFacade _aebfacade;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private SessionController _sessionController;

    private List<AEBBaseInformation> _listComplete = new ArrayList<>();
    private List<AEBBaseInformation> _listProvided = new ArrayList<>();
    private List<AEBBaseInformation> _listWorking = new ArrayList<>();
    private List<StructureInformation> _listStructureInformation = new ArrayList<>();

    public List<AEBBaseInformation> getListComplete() {
        return _listComplete;
    }

    public List<AEBBaseInformation> getListProvided() {
        return _listProvided;
    }

    public void setListComplete(List<AEBBaseInformation> listComplete) {
        this._listComplete = listComplete;
    }

    public void setListProvided(List<AEBBaseInformation> listProvided) {  this._listProvided = listProvided; }

    public List<AEBBaseInformation> getListWorking() {
        return _listWorking;
    }

    public void setListWorking(List<AEBBaseInformation> listWorking) {
        this._listWorking = listWorking;
    }

    public String readHospitalNameByIk(int ik){
        return _sessionController.getApplicationTools().retrieveHospitalName(ik);
    }

    @PostConstruct
    public void init() {
        setWorkingList();
        setCompleteList();
        setProvidedList();
    }

    private void setWorkingList() {
        Set<Integer> iks = _accessManager.retrieveAllowedManagedIks(Feature.HC_INSURANCE);
        List<WorkflowStatus> status = new ArrayList<>();
        status.add(WorkflowStatus.New);
        status.add(WorkflowStatus.CorrectionRequested);
        _listWorking = _aebfacade.getAllByStatusAndIk(status, iks, CustomerTyp.Insurance);
    }

    private void setCompleteList() {
        Set<Integer> iks = _accessManager.retrieveAllowedManagedIks(Feature.HC_INSURANCE);
        List<WorkflowStatus> status = new ArrayList<>();
        status.add(WorkflowStatus.Provided);
        _listComplete = _aebfacade.getAllByStatusAndIk(status, iks, CustomerTyp.Insurance);
    }

    private void setProvidedList() {
        Set<Integer> iks = _accessManager.retrieveAllowedManagedIks(Feature.HC_INSURANCE);
        List<WorkflowStatus> status = new ArrayList<>();
        status.add(WorkflowStatus.Provided);
        _listProvided = _aebfacade.getAllByStatusAndIk(status, iks, CustomerTyp.Hospital);
    }

    public String khComparisonOpen() {
        return Pages.InsuranceKhComparisonEdit.URL();
    }

    public boolean isCreateEntryAllowed() {
        Set<Integer> allowedIks = _accessManager.obtainIksForCreation(Feature.HC_INSURANCE);
        return _aebfacade.retrievePossibleIks(allowedIks, CustomerTyp.Insurance).size() > 0;
    }

    public void deleteBaseInformation(AEBBaseInformation info) {
        _aebfacade.deleteBaseInformation(info);
        setWorkingList();
    }

    public Boolean isDeleteAllowed(int ik) {
        return _accessManager.obtainIksForCreation(Feature.HC_INSURANCE).contains(ik);
    }
}
