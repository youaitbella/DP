/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChangesBaseInformation;
import org.inek.dataportal.care.facades.DeptFacade;
import org.inek.dataportal.care.facades.StructuralChangesFacade;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.overall.AccessManager;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lautenti
 */
@Named
@ViewScoped
public class StructuralChangesSummary implements Serializable {

    @Inject
    private SessionController _sessionController;

    @Inject
    private StructuralChangesFacade _structuralChangesFacade;

    @Inject
    private DeptFacade _deptFacade;

    private Map<Integer, String> _iks = new HashMap<>();

    private List<StructuralChangesBaseInformation> _exitsBaseInformations = new ArrayList<>();

    public List<StructuralChangesBaseInformation> getExitsBaseInformations() {
        return _exitsBaseInformations;
    }

    public void setExitsBaseInformations(List<StructuralChangesBaseInformation> exitsBaseInformations) {
        this._exitsBaseInformations = exitsBaseInformations;
    }

    public Map<Integer, String> getIks() {
        return _iks;
    }

    public void setIks(Map<Integer, String> iks) {
        this._iks = iks;
    }

    @PostConstruct
    private void init() {
        for (AccessRight right : _sessionController.getAccount().getAccessRights().stream()
                .filter(c -> c.canRead() && c.getFeature() == Feature.CARE)
                .collect(Collectors.toList())) {
            List<DeptBaseInformation> allByStatusAndIk = _deptFacade.getAllByStatusAndIk(WorkflowStatus.Provided, right.getIk());
            if (allByStatusAndIk.size() > 0) {
                _iks.put(right.getIk(), _sessionController.getApplicationTools().retrieveHospitalName(right.getIk()));
            }
        }

        loadStructuralChangesBaseInformation();
    }

    public String retrieveHospitalName(int ik) {
        return _sessionController.getApplicationTools().retrieveHospitalName(ik);
    }

    private void loadStructuralChangesBaseInformation() {
        _exitsBaseInformations.clear();
        for (Map.Entry<Integer, String> entry : _iks.entrySet()) {
            _exitsBaseInformations.addAll(_structuralChangesFacade.findBaseInformationsByIk(entry.getKey()));
        }
    }

    public String openStructuralChangesPage() {
        return Pages.CareStructuralChangesEdit.URL();
    }

}
