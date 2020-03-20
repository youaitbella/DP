package org.inek.dataportal.care.proof;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.inek.dataportal.care.utils.CareSignatureCreater;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.utils.DateUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ProofSummary implements Serializable {

    @Inject
    private ApplicationTools _applicationTools;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private SessionController _sessionController;
    @Inject
    private ProofFacade _proofFacade;
    @Inject
    private ConfigFacade _configFacade;

    private List<listItem> _listComplete = new ArrayList<>();
    private List<listItem> _listWorking = new ArrayList<>();
    private List<listItem> _listInek = new ArrayList<>();

    public List<listItem> getListComplete() {
        return _listComplete;
    }

    public void setListComplete(List<listItem> listComplete) {
        this._listComplete = listComplete;
    }

    public List<listItem> getListWorking() {
        return _listWorking;
    }

    public void setListWorking(List<listItem> listWorking) {
        this._listWorking = listWorking;
    }

    public List<listItem> getListInek() {
        return _listInek;
    }

    public void setListInek(List<listItem> listInek) {
        this._listInek = listInek;
    }

    @PostConstruct
    public void init() {
        setWorkingList();
        setCompleteList();
        if(isInekUser()) {
            setInekList();
        }
    }

    private void setWorkingList() {
        _listWorking.clear();
        for (AccessRight right : _sessionController.getAccount().getAccessRights().stream()
                .filter(c -> c.canRead() && c.getFeature() == Feature.CARE)
                .collect(Collectors.toList())) {
            _listWorking.addAll(createListItems(_proofFacade.getAllByStatusAndIk(WorkflowStatus.New, right.getIk())));
            _listWorking.addAll(createListItems(_proofFacade.getAllByStatusAndIk(WorkflowStatus.CorrectionRequested, right.getIk())));
        }
    }

    private void setCompleteList() {
        _listComplete.clear();
        for (AccessRight right : _sessionController.getAccount().getAccessRights().stream()
                .filter(c -> c.canRead() && c.getFeature() == Feature.CARE)
                .collect(Collectors.toList())) {
            _listComplete.addAll(createListItems(_proofFacade.getAllByStatusAndIk(WorkflowStatus.Provided, right.getIk())));
        }
    }

    private void setInekList() {
        _listInek.clear();
        _listInek.addAll(createListItems(_proofFacade.getAllByStatus(WorkflowStatus.Provided)));
        _listInek.addAll(createListItems(_proofFacade.getAllByStatus(WorkflowStatus.CorrectionRequested)));
        _listInek.addAll(createListItems(_proofFacade.getAllByStatus(WorkflowStatus.Retired)));
    }

    public String careProofStationOpen() {
        return Pages.CareProofEdit.URL();
    }

    public boolean isCreateEntryAllowed() {
        if (!_configFacade.readConfigBool(ConfigKey.IsCareProofCreateEnabled)) {
            return false;
        }
        Set<Integer> allowedIks = _accessManager.obtainIksForCreation(Feature.CARE);
        return _proofFacade.retrievePossibleIkYearQuarters(allowedIks).size() > 0;
    }

    public void deleteBaseInformation(ProofRegulationBaseInformation info) {
        _proofFacade.deleteBaseInformation(info);
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

    public Boolean isInekUser() {
        return _sessionController.isInekUser(Feature.CARE);
    }

    public List<listItem> createListItems(List<ProofRegulationBaseInformation> baseInfos) {
        List<listItem> listItems = new ArrayList<>();

        for (ProofRegulationBaseInformation info : baseInfos) {
            listItem item = new listItem();
            item.setId(info.getId());
            item.setIk(info.getIk());
            item.setHospitalName(_applicationTools.retrieveHospitalInfo(info.getIk()));
            item.setLastChangeDate(info.getLastChanged());
            item.setStatusId(info.getStatusId());
            item.setYear(info.getYear());
            item.setQuarter(info.getQuarter());
            item.setBaseInfo(info);
            item.setSignature(info.getSignature());
            listItems.add(item);
        }

        return listItems;
    }

    public String retrieveHospitalName(int ik) {
        return _applicationTools.retrieveHospitalInfo(ik);
    }

    public List<String> getIkForAnnualReport() {

        if (!_configFacade.readConfigBool(ConfigKey.IsCareProofCreateEnabled)) {
            Collections.emptyList();
        }
        Set<Integer> allowedIks = _accessManager.obtainIksForCreation(Feature.CARE);
        return _proofFacade.retrievePossibleIkForAnnualReport(allowedIks).stream().map(i -> "" + i).collect(Collectors.toList());

    }

    public boolean getAnnualReportAllowed() {
        return getIkForAnnualReport().size() > 0;
    }

    public void createAnnualReport(String ikString) {
        int ik = Integer.parseInt(ikString);
        ProofRegulationBaseInformation baseInfo = new ProofRegulationBaseInformation();
        baseInfo.setIk(ik);
        baseInfo.setStatus(WorkflowStatus.Provided);
        baseInfo.setSignature(CareSignatureCreater.createPvSignature(baseInfo));
        baseInfo.setCreatedBy(_sessionController.getAccountId());
        baseInfo.setLastChangeBy(_sessionController.getAccountId());
        baseInfo.setSend(new Date());
        baseInfo.setYear(DateUtils.currentYear() - 1);
        baseInfo.setQuarter(5);

        List<ProofRegulationBaseInformation> baseInfos = _proofFacade.retrievePreviousYearInfos(ik);
        baseInfos.stream().sorted(Comparator.comparingInt(ProofRegulationBaseInformation::getQuarter)).forEach(bi -> {
            bi.getProofs().stream().forEach(p -> {
                Proof proof = new Proof(p);
                proof.setBaseInformation(baseInfo);
                baseInfo.addProof(proof);
            });
        });
        _proofFacade.save(baseInfo);
    }

    public class listItem implements Serializable {

        private int _id;
        private int _ik;
        private int _statusId;
        private int _year;
        private int _quarter;
        private String _hospitalName;
        private Date _lastChangeDate;
        private String _signature;
        private ProofRegulationBaseInformation _baseInfo;

        public listItem() {

        }

        public ProofRegulationBaseInformation getBaseInfo() {
            return _baseInfo;
        }

        public void setBaseInfo(ProofRegulationBaseInformation baseInfo) {
            this._baseInfo = baseInfo;
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

        public int getStatusId() {
            return _statusId;
        }

        public void setStatusId(int statusId) {
            this._statusId = statusId;
        }

        public int getYear() { return _year; }

        public void setYear(int year) { this._year = year; }

        public int getQuarter() { return _quarter; }

        public void setQuarter(int quarter) { this._quarter = quarter; }

        public String getHospitalName() {
            return _hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this._hospitalName = hospitalName;
        }

        public Date getLastChangeDate() {
            return _lastChangeDate;
        }

        public void setLastChangeDate(Date lastChangeDate) {
            this._lastChangeDate = lastChangeDate;
        }

        public String getSignature() {
            return _signature;
        }

        public void setSignature(String signature) {
            this._signature = signature;
        }
    }
}
