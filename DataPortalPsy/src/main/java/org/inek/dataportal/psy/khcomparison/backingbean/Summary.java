/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.backingbean;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.KhComparison.entities.AEBBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.StructureBaseInformation;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.CustomerTyp;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
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
    private List<StructureBaseInformation> _listStructureBaseInformation = new ArrayList<>();

    private List<AebListEntry> _listInek = new ArrayList<>();

    public List<AebListEntry> getListInek() {
        return _listInek;
    }

    public void setListInek(List<AebListEntry> listInek) {
        this._listInek = listInek;
    }

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

    public List<StructureBaseInformation> getListStructureBaseInformation() {
        return _listStructureBaseInformation;
    }

    public void setListStructureInformation(List<StructureBaseInformation> listStructureBaseInformation) {
        this._listStructureBaseInformation = listStructureBaseInformation;
    }

    @PostConstruct
    public void init() {
        setWorkingList();
        setCompleteList();
        if (_sessionController.isInekUser("HC_HOSPITAL")) {
            setInekList();
        }
        setStructureBaseInformationList();
    }

    private void setInekList() {
        _listInek.clear();

        for(AEBBaseInformation info : _aebfacade.getAllByStatus(WorkflowStatus.Provided)) {
            _listInek.add(new AebListEntry(info));
        }
    }

    private void setWorkingList() {
        _listWorking.clear();
        if (_configFacade.readConfigBool(ConfigKey.IkAdminEnable)) {
            for (AccessRight right : _sessionController.getAccount().getAccessRights().stream()
                    .filter(c -> c.canRead() && c.getFeature() == Feature.HC_HOSPITAL)
                    .collect(Collectors.toList())) {
                _listWorking.addAll(_aebfacade.getAllByStatusAndIk(WorkflowStatus.New, right.getIk(), CustomerTyp.Hospital));
                _listWorking.
                        addAll(_aebfacade.getAllByStatusAndIk(WorkflowStatus.CorrectionRequested, right.getIk(), CustomerTyp.Hospital));
            }
        } else {
            for (Integer ik : _sessionController.getAccount().getFullIkSet()) {
                _listWorking.addAll(_aebfacade.getAllByStatusAndIk(WorkflowStatus.New, ik, CustomerTyp.Hospital));
                _listWorking.addAll(_aebfacade.getAllByStatusAndIk(WorkflowStatus.CorrectionRequested, ik, CustomerTyp.Hospital));
            }
        }
    }

    private void setCompleteList() {
        _listComplete.clear();
        if (_configFacade.readConfigBool(ConfigKey.IkAdminEnable)) {
            for (AccessRight right : _sessionController.getAccount().getAccessRights().stream()
                    .filter(c -> c.canRead() && c.getFeature() == Feature.HC_HOSPITAL)
                    .collect(Collectors.toList())) {
                _listComplete.addAll(_aebfacade.getAllByStatusAndIk(WorkflowStatus.Provided, right.getIk(), CustomerTyp.Hospital));
            }
        } else {
            for (Integer ik : _sessionController.getAccount().getFullIkSet()) {
                _listComplete.addAll(_aebfacade.getAllByStatusAndIk(WorkflowStatus.Provided, ik, CustomerTyp.Hospital));
            }
        }
    }

    public String khComparisonOpen() {
        return Pages.KhComparisonEdit.URL();
    }

    public String structureBaseInformationOpen() {
        return Pages.StructureBaseInformationEdit.URL();
    }

    public boolean isCreateEntryAllowed() {
        Set<Integer> allowedIks = _accessManager.ObtainIksForCreation(Feature.HC_HOSPITAL);
        return _aebfacade.retrievePossibleIks(allowedIks, CustomerTyp.Hospital).size() > 0;
    }

    public boolean isCreateStructureBaseInformationAllowed() {
        Set<Integer> allowedIks = _accessManager.ObtainIksForCreation(Feature.HC_HOSPITAL);
        return allowedIks.stream().
                anyMatch(ik -> !_aebfacade.structureBaseInformaionAvailable(ik));
    }

    public void deleteBaseInformation(AEBBaseInformation info) {
        _aebfacade.deleteBaseInformation(info);
        setWorkingList();
    }

    private void setStructureBaseInformationList() {
        Set<Integer> allowedIks = _accessManager.ObtainAllowedIks(Feature.HC_HOSPITAL);
        for (Integer ik : _sessionController.getAccount().getFullIkSet()) {
            if (_aebfacade.structureBaseInformaionAvailable(ik)) {
                if (allowedIks.contains(ik)) {
                    _listStructureBaseInformation.add(_aebfacade.getStructureBaseInformationByIk(ik));
                }
            }
        }
    }

    public Boolean isDeleteAllowed(int ik) {
        return !_sessionController.getAccount().getAccessRights().stream()
                .filter(c -> c.getFeature() == Feature.HC_HOSPITAL
                        && c.getIk() == ik
                        && c.canWrite())
                .collect(Collectors.toList())
                .isEmpty();
    }

    public class AebListEntry implements Serializable {
        private int _id;
        private int _ik;
        private int _year;
        private String _type;
        private Date _sendAt;
        private String _sendBy;

        public AebListEntry(AEBBaseInformation baseInfo) {
            setId(baseInfo.getId());
            setIk(baseInfo.getIk());
            setYear(baseInfo.getYear());
            setType(baseInfo.getTyp() == CustomerTyp.Hospital.id() ? "Krankenhaus" : "Krankenkassen");
            setSendAt(baseInfo.getSend());
            setSendBy(baseInfo.getLastChangeFrom() + "");
        }

        public int getId() {
            return _id;
        }

        public void setId(int id) {
            this._id = id;
        }

        public int getIk() {
            return _ik;
        }

        public void setIk(int ik) {
            this._ik = ik;
        }

        public int getYear() {
            return _year;
        }

        public void setYear(int year) {
            this._year = year;
        }

        public String getType() {
            return _type;
        }

        public void setType(String type) {
            this._type = type;
        }

        public Date getSendAt() {
            return _sendAt;
        }

        public void setSendAt(Date sendAt) {
            this._sendAt = sendAt;
        }

        public String getSendBy() {
            return _sendBy;
        }

        public void setSendBy(String sendBy) {
            this._sendBy = sendBy;
        }
    }
}
