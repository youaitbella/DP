/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
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
import org.inek.dataportal.common.overall.ApplicationTools;

/**
 *
 * @author lautenti
 */
@Named
@ViewScoped
public class DeptSummary implements Serializable {

    @Inject
    private ApplicationTools _applicationTools;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private SessionController _sessionController;
    @Inject
    private DeptFacade _deptFacade;

    private List<DeptBaseInformation> _listComplete = new ArrayList<>();
    private List<DeptBaseInformation> _listWorking = new ArrayList<>();
    private List<listItem> _listInek = new ArrayList<>();

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
        if (isInekUser()) {
            setInekList();
        }
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
                .filter(c -> c.canRead() && c.getFeature() == Feature.CARE)
                .collect(Collectors.toList())) {
            _listComplete.addAll(_deptFacade.getAllByStatusAndIk(WorkflowStatus.Provided, right.getIk()));
        }
    }

    private void setInekList() {
        _listInek.clear();
        _listInek.addAll(createListItems(_deptFacade.getAllByStatus(WorkflowStatus.Provided)));
        _listInek.addAll(createListItems(_deptFacade.getAllByStatus(WorkflowStatus.CorrectionRequested)));
    }

    public String careDeptStationOpen() {
        return Pages.CareDeptEdit.URL();
    }

    public boolean isCreateEntryAllowed() {
        Set<Integer> allowedIks = _accessManager.ObtainIksForCreation(Feature.CARE);
        return _deptFacade.retrievePossibleIks(allowedIks).size() > 0;
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

    public Boolean isInekUser() {
        return _sessionController.isInekUser(Feature.CARE);
    }

    public List<listItem> createListItems(List<DeptBaseInformation> baseInfos) {
        List<listItem> listItems = new ArrayList<>();

        for (DeptBaseInformation info : baseInfos) {
            listItem item = new listItem();
            item.setId(info.getId());
            item.setIk(info.getIk());
            item.setHospitalName(_applicationTools.retrieveHospitalInfo(info.getIk()));
            item.setLastChangeDate(info.getLastChanged());
            item.setStatusId(info.getStatusId());

            listItems.add(item);
        }

        return listItems;
    }

    public String retrieveHospitalName(int ik) {
        return _applicationTools.retrieveHospitalInfo(ik);
    }

    public class listItem implements Serializable {

        private int _id;
        private int _ik;
        private int _statusId;
        private String _hospitalName;
        private Date _lastChangeDate;

        public listItem() {

        }

        public listItem(int id, int ik, int statusId, String hospitalName, Date lastChangeDate) {
            this._id = id;
            this._ik = ik;
            this._statusId = statusId;
            this._hospitalName = hospitalName;
            this._lastChangeDate = lastChangeDate;
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

    }
}
