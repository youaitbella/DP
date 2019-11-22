/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChanges;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChangesBaseInformation;
import org.inek.dataportal.care.facades.DeptFacade;
import org.inek.dataportal.care.facades.StructuralChangesFacade;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.TransferFileType;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.TransferFileCreator;

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

    @Inject
    private ConfigFacade _configFacade;

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

    private List<StructuralChangesBaseInformation> _listInek = new ArrayList<>();

    public List<StructuralChangesBaseInformation> getListInek() {
        return _listInek;
    }

    public void setListInek() {
        _listInek = _structuralChangesFacade.getAllOpen();
    }

    @PostConstruct
    private void init() {
        _allowedIks = _sessionController.getAccount().getAccessRights().stream()
                .filter(c -> c.canRead() && c.getFeature() == Feature.CARE)
                .map(c -> c.getIk())
                .collect(Collectors.toSet());

        loadOpenStructuralChangesBaseInformation();
        loadSendedStructuralChangesBaseInformation();
        if (isInekUser()) {
            setListInek();
        }
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
        if (!_configFacade.readConfigBool(ConfigKey.CareStructuralChangesEnable)) {
            return false;
        }

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
            if (_structuralChangesFacade.findOpenOrSendBaseInformationsByIk(ik).isPresent()) {
                continue;
            }

            List<DeptBaseInformation> allByStatusAndIk = _deptFacade.getAllByStatusAndIk(WorkflowStatus.Provided, ik);
            if (allByStatusAndIk.stream().anyMatch(i -> i.getYear() >= 2018)) {
                iks.add(ik);
            }
        }
        return iks;
    }


    public void deleteBaseInformation(StructuralChangesBaseInformation baseInfo) {
        baseInfo.setStatus(WorkflowStatus.Retired);
        TransferFileCreator.createObjectTransferFile(_sessionController, baseInfo,
                baseInfo.getIk(), TransferFileType.CareChanges);
        _structuralChangesFacade.deleteBaseInformation(baseInfo);
        _openBaseInformations.remove(baseInfo);
    }

    public void changeBaseInformation(StructuralChangesBaseInformation baseInfo) {
        baseInfo.setStatus(WorkflowStatus.New);
        for (StructuralChanges changes : baseInfo.getStructuralChanges()) {
            changes.setStatus(WorkflowStatus.New);
        }
        _sendedBaseInformations.remove(baseInfo);
        _openBaseInformations.add(baseInfo);
        _structuralChangesFacade.save(baseInfo);
        TransferFileCreator.createObjectTransferFile(_sessionController, baseInfo,
                baseInfo.getIk(), TransferFileType.CareChanges);
    }

    public boolean isDeleteAllowed(StructuralChangesBaseInformation baseInfo) {
        //TODO check
        return true;
    }

    public boolean isChangeAllowed(StructuralChangesBaseInformation baseInfo) {
        return baseInfo.getStatus().getId() >= WorkflowStatus.Provided.getId();
    }

    public String formatDate(Date date) {
        Format formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(date);
    }

    public Boolean isInekUser() {
        return _sessionController.isInekUser(Feature.CARE);
    }
}
