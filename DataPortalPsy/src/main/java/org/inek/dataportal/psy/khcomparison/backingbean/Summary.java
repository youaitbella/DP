/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.backingbean;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.psy.khcomparison.entity.AEBBaseInformation;
import org.inek.dataportal.psy.khcomparison.facade.AEBFacade;

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

    private List<AEBBaseInformation> _listComplete;
    private List<AEBBaseInformation> _listWorking;

    public List<AEBBaseInformation> getListComplete() {
        return _listComplete;
    }

    public void setListComplete(List<AEBBaseInformation> listComplete) {
        this._listComplete = listComplete;
    }

    public List<AEBBaseInformation> getListWorking() {
        return _listWorking;
    }

    public void setListWorking(List<AEBBaseInformation> listWorking) {
        this._listWorking = listWorking;
    }

    @PostConstruct
    public void init() {
        _listComplete = new ArrayList<>();
        _listWorking = new ArrayList<>();
        _listComplete.addAll(getAebsByStatus(WorkflowStatus.Provided));
        _listWorking.addAll(getAebsByStatus(WorkflowStatus.New));
    }

    private List<AEBBaseInformation> getAebsByStatus(WorkflowStatus status) {
        return _aebfacade.getAllByStatus(status);
    }

    public String khComparisonOpen() {
        return Pages.KhComparisonEdit.URL();
    }

    public boolean isCreateEntryAllowed() {
        return !getAllowedIks().isEmpty();
    }

    public List<Integer> getAllowedIks() {
        List<Integer> iks = new ArrayList<>();
        for (Integer ik : _aebfacade.getAllowedIks(_sessionController.getAccountId(),
                Utils.getTargetYear(Feature.AEB))) {
            if (_accessManager.isAccessAllowed(Feature.AEB, WorkflowStatus.Taken,
                    _sessionController.getAccountId(),
                    ik)) {
                iks.add(ik);
            }
        }
        return iks;
    }
}
