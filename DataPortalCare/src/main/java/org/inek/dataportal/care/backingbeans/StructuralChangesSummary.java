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
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
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

    private Set<Integer> _allowedIks = new HashSet<>();

    private List<StructuralChangesBaseInformation> _sendedBaseInformations = new ArrayList<>();

    private List<StructuralChangesBaseInformation> _openBaseInformations = new ArrayList<>();

    public List<StructuralChangesBaseInformation> getSendedBaseInformations() {
        return _sendedBaseInformations;
    }

    public void setSendedBaseInformations(List<StructuralChangesBaseInformation> sendedBaseInformations) {
        this._sendedBaseInformations = sendedBaseInformations;
    }

    public List<StructuralChangesBaseInformation> getOpenBaseInformations() {
        return _openBaseInformations;
    }

    public void setOpenBaseInformations(List<StructuralChangesBaseInformation> openBaseInformations) {
        this._openBaseInformations = openBaseInformations;
    }

    @PostConstruct
    private void init() {
        _allowedIks = _sessionController.getAccount().getAccessRights().stream()
                .filter(c -> c.canRead() && c.getFeature() == Feature.CARE)
                .map(c -> c.getIk())
                .collect(Collectors.toSet());

        loadOpenStructuralChangesBaseInformation();
        loadSendedStructuralChangesBaseInformation();
    }

    private void loadSendedStructuralChangesBaseInformation() {
        for (Integer allowedIk : _allowedIks) {
            _sendedBaseInformations.addAll(_structuralChangesFacade.findSendBaseInformationsByIk(allowedIk));
        }
    }

    public String retrieveHospitalName(int ik) {
        return _sessionController.getApplicationTools().retrieveHospitalName(ik);
    }

    private void loadOpenStructuralChangesBaseInformation() {
        _openBaseInformations.clear();
        for (Integer allowedIk : _allowedIks) {
            Optional<StructuralChangesBaseInformation> baseInfo = _structuralChangesFacade.findOpenBaseInformationsByIk(allowedIk);
            baseInfo.ifPresent(structuralChangesBaseInformation -> _openBaseInformations.add(structuralChangesBaseInformation));
        }
    }

    public String openStructuralChangesPage() {
        return Pages.CareStructuralChangesEdit.URL();
    }

    public boolean isCreateEntryAllowed() {
        Set<Integer> iks = loadValidIks();
        return iks.size() > 0;
    }

    private Set<Integer> loadValidIks() {
        Set<Integer> iks = new HashSet<>();

        Set<Integer> tmpAllowedIks = _sessionController.getAccount().getAccessRights().stream()
                .filter(c -> c.canRead() && c.getFeature() == Feature.CARE)
                .map(c -> c.getIk())
                .collect(Collectors.toSet());

        for (Integer ik : tmpAllowedIks) {
            Optional<StructuralChangesBaseInformation> openBaseInformationsByIk = _structuralChangesFacade.findOpenBaseInformationsByIk(ik);
            if (openBaseInformationsByIk.isPresent()) {
                continue;
            }

            List<StructuralChangesBaseInformation> sendBaseInformationsByIk = _structuralChangesFacade.findSendBaseInformationsByIk(ik);
            List<DeptBaseInformation> allByStatusAndIk = _deptFacade.getAllByStatusAndIk(WorkflowStatus.Provided, ik);
            if (allByStatusAndIk.size() >= 1 && sendBaseInformationsByIk.size() == 0) {
                iks.add(ik);
            }
        }
        return iks;
    }

    public void deleteBaseInformation(StructuralChangesBaseInformation baseInfo) {
        _structuralChangesFacade.deleteBaseInformation(baseInfo);
    }

    public boolean isDeleteAllowed(StructuralChangesBaseInformation baseInfo) {
        //TODO check
        return true;
    }

    public String formatDate(Date date) {
        Format formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(date);
    }
}
