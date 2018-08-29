/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.backingbean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.data.KhComparison.entities.AEBBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.StructureInformation;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;

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
    @Inject
    private ConfigFacade _configFacade;

    private List<AEBBaseInformation> _listComplete = new ArrayList<>();
    private List<AEBBaseInformation> _listWorking = new ArrayList<>();
    private List<StructureInformation> _listStructureInformation = new ArrayList<>();

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

    public List<StructureInformation> getListStructureInformation() {
        return _listStructureInformation;
    }

    public void setListStructureInformation(List<StructureInformation> listStructureInformation) {
        this._listStructureInformation = listStructureInformation;
    }

    @PostConstruct
    public void init() {
        setWorkingList();
        setCompleteList();
        setStructureInformationList();
    }

    private void setWorkingList() {
        _listWorking.clear();
        if (_configFacade.readConfigBool(ConfigKey.IkAdminEnable)) {
            for (AccessRight right : _sessionController.getAccount().getAccessRights().stream()
                    .filter(c -> c.canRead() && c.getFeature() == Feature.AEB)
                    .collect(Collectors.toList())) {
                _listWorking.addAll(_aebfacade.getAllByStatusAndIk(WorkflowStatus.New, right.getIk(), 0));
                _listWorking.addAll(_aebfacade.getAllByStatusAndIk(WorkflowStatus.CorrectionRequested, right.getIk(), 0));
            }
        } else {
            for (Integer ik : _sessionController.getAccount().getFullIkSet()) {
                _listWorking.addAll(_aebfacade.getAllByStatusAndIk(WorkflowStatus.New, ik, 0));
                _listWorking.addAll(_aebfacade.getAllByStatusAndIk(WorkflowStatus.CorrectionRequested, ik, 0));
            }
        }
    }

    private void setCompleteList() {
        _listComplete.clear();
        if (_configFacade.readConfigBool(ConfigKey.IkAdminEnable)) {
            for (AccessRight right : _sessionController.getAccount().getAccessRights().stream()
                    .filter(c -> c.canRead() && c.getFeature() == Feature.AEB)
                    .collect(Collectors.toList())) {
                _listComplete.addAll(_aebfacade.getAllByStatusAndIk(WorkflowStatus.Provided, right.getIk(), 0));
            }
        } else {
            for (Integer ik : _sessionController.getAccount().getFullIkSet()) {
                _listComplete.addAll(_aebfacade.getAllByStatusAndIk(WorkflowStatus.Provided, ik, 0));
            }
        }
    }

    public String khComparisonOpen() {
        return Pages.KhComparisonEdit.URL();
    }

    public String structureInformationOpen() {
        return Pages.StructureInformationEdit.URL();
    }

    public boolean isCreateEntryAllowed() {
        return _accessManager.isCreateAllowed(Feature.AEB);
    }

    public boolean isCreateStructureInformationAllowed() {
        return _accessManager.isCreateAllowed(Feature.AEB);
    }

    public void deleteBaseInformation(AEBBaseInformation info) {
        _aebfacade.deleteBaseInformation(info);
        setWorkingList();
    }

    private void setStructureInformationList() {
        for (Integer ik : _sessionController.getAccount().getFullIkSet()) {
            if (_aebfacade.structureInformaionAvailable(ik)) {
                if (_accessManager.isReadAllowed(Feature.AEB, _sessionController.getAccount(), ik)) {
                    _listStructureInformation.add(_aebfacade.getStructureInformationByIk(ik));
                }
            }
        }
    }

    public Boolean isDeleteAllowed(int ik) {
        return !_sessionController.getAccount().getAccessRights().stream()
                .filter(c -> c.getFeature() == Feature.AEB
                && c.getIk() == ik
                && c.canWrite())
                .collect(Collectors.toList())
                .isEmpty();
    }
}
