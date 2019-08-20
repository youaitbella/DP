/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.nub.backingbean;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.psy.nub.entities.PsyNubProposal;
import org.inek.dataportal.psy.nub.facade.PsyNubFacade;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lautenti
 */
@Named
@FeatureScoped
public class NubSummary implements Serializable {

    @Inject
    private PsyNubFacade _psyNubFacade;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private SessionController _sessionController;
    @Inject
    private ConfigFacade _configFacade;

    private List<PsyNubProposal> _listComplete = new ArrayList<>();
    private List<PsyNubProposal> _listWorking = new ArrayList<>();

    private List<PsyNubProposal> _selectedElements = new ArrayList<>();

    public List<PsyNubProposal> getSelectedElements() {
        return _selectedElements;
    }

    public void setSelectedElements(List<PsyNubProposal> selectedElements) {
        this._selectedElements = selectedElements;
    }

    public List<PsyNubProposal> getListComplete() {
        return _listComplete;
    }

    public void setListComplete(List<PsyNubProposal> listComplete) {
        this._listComplete = listComplete;
    }

    public List<PsyNubProposal> getListWorking() {
        return _listWorking;
    }

    public void setListWorking(List<PsyNubProposal> listWorking) {
        this._listWorking = listWorking;
    }

    @PostConstruct
    public void init() {
        setWorkingList();
        setCompleteList();
    }

    private void setWorkingList() {
        _listWorking.clear();
        _listWorking.addAll(_psyNubFacade.findAllByAccountIdAndNoIk(_sessionController.getAccountId(), WorkflowStatus.New));
        for (Integer ik : _sessionController.getAccount().getFullIkSet()) {
            if (_accessManager.ikIsManaged(ik, Feature.NUB_PSY)) {
                if (_accessManager.userHasReadAccess(Feature.NUB_PSY, ik)) {
                    _listWorking.addAll(_psyNubFacade.findAllByIkAndStatus(ik, WorkflowStatus.New));
                }
            } else {
                _listWorking.addAll(_psyNubFacade.findAllByAccountIdAndIkAndStatus(_sessionController.getAccountId(), ik, WorkflowStatus.New));
            }
        }
    }

    private void setCompleteList() {
        List<WorkflowStatus> statusForCompleteList = new ArrayList<>();
        statusForCompleteList.add(WorkflowStatus.Accepted);
        statusForCompleteList.add(WorkflowStatus.Updated);
        statusForCompleteList.add(WorkflowStatus.Retired);
        statusForCompleteList.add(WorkflowStatus.Taken);
        statusForCompleteList.add(WorkflowStatus.TakenUpdated);
        _listComplete.clear();

        _listComplete.addAll(_psyNubFacade.findAllByAccountIdAndNoIk(_sessionController.getAccountId(), statusForCompleteList));
        for (Integer ik : _sessionController.getAccount().getFullIkSet()) {
            if (_accessManager.ikIsManaged(ik, Feature.NUB_PSY)) {
                if (_accessManager.userHasReadAccess(Feature.NUB_PSY, ik)) {
                    _listComplete.addAll(_psyNubFacade.findAllByIkAndStatus(ik, statusForCompleteList));
                }
            } else {
                _listComplete.addAll(_psyNubFacade.findAllByAccountIdAndIkAndStatus(_sessionController.getAccountId(), ik, statusForCompleteList));
            }
        }
    }

    public String psyNubEditOpen() {
        return Pages.NubPsyEdit.URL();
    }

    public boolean isCreateEntryAllowed() {
        return _configFacade.readConfigBool(ConfigKey.IsPsyNubCreateEnabled);
    }

    public void deletePsyNubProposal(PsyNubProposal proposal) {
        if (deleteAllowed(proposal)) {
            _psyNubFacade.delete(proposal);
            setWorkingList();
        } else {
            DialogController.showAccessDeniedDialog();
        }
    }

    public void retirePsyNubProposal(PsyNubProposal proposal) {
        if (deleteAllowed(proposal)) {
            proposal.setStatus(WorkflowStatus.Retired);
            proposal.setLastModifiedAt(new Date());
            proposal.setLastChangedByAccountId(_sessionController.getAccountId());
            _psyNubFacade.save(proposal);
            setCompleteList();
        } else {
            DialogController.showAccessDeniedDialog();
        }
    }

    private Boolean deleteAllowed(PsyNubProposal proposal) {
        // TODO prüfen ob Benutzer nub löschen darf
        return true;
    }
}
