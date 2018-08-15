/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.drg.specificfunction.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.specificfunction.entity.SpecificFunctionRequest;
import org.inek.dataportal.drg.specificfunction.facade.SpecificFunctionFacade;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class SpecificFunctionSummary implements Serializable {

    @Inject
    private SpecificFunctionFacade _specificFunctionFacade;
    @Inject
    private ApplicationTools _applicationTools;

    private List<SpecificFunctionListItem> _inekSpecificFunctions = new ArrayList<>();

    public List<SpecificFunctionListItem> getInekSpecificFunctions() {
        return _inekSpecificFunctions;
    }

    public void setInekSpecificFunctions(List<SpecificFunctionListItem> inekSpecificFunctions) {
        this._inekSpecificFunctions = inekSpecificFunctions;
    }

    @PostConstruct
    public void init() {
        List<SpecificFunctionRequest> request = _specificFunctionFacade.getSpecificFunctionsForInek();

        for (SpecificFunctionRequest req : request) {
            SpecificFunctionListItem item = new SpecificFunctionListItem();
            item.setId(req.getId());
            item.setYear(req.getDataYear());
            item.setStatus(req.getStatus());
            item.setIk(req.getIk());
            item.setHospitalName(_applicationTools.retrieveHospitalName(req.getIk()));
            item.setTown(_applicationTools.retrieveHospitalTown(req.getIk()));
            item.setSendDate(req.getSealed());
            item.setCode(req.getCode());
            item.generateLfdNumber();
            _inekSpecificFunctions.add(item);
        }
    }

    public class SpecificFunctionListItem implements Serializable {

        private int _id;
        private int _year;
        private WorkflowStatus _status;
        private int _ik;
        private String _hospitalName;
        private String _town;
        private Date _sendDate;
        private String _code;
        private String _lfdNumber;

        public int getId() {
            return _id;
        }

        public void setId(int id) {
            this._id = id;
        }

        public int getYear() {
            return _year;
        }

        public void setYear(int year) {
            this._year = year;
        }

        public WorkflowStatus getStatus() {
            return _status;
        }

        public void setStatus(WorkflowStatus status) {
            this._status = status;
        }

        public int getIk() {
            return _ik;
        }

        public void setIk(int ik) {
            this._ik = ik;
        }

        public String getHospitalName() {
            return _hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this._hospitalName = hospitalName;
        }

        public String getTown() {
            return _town;
        }

        public void setTown(String town) {
            this._town = town;
        }

        public Date getSendDate() {
            return _sendDate;
        }

        public void setSendDate(Date sendDate) {
            this._sendDate = sendDate;
        }

        public String getCode() {
            return _code;
        }

        public void setCode(String code) {
            this._code = code;
        }

        public String getLfdNumber() {
            return _lfdNumber;
        }

        public void setLfdNumber(String lfdNumber) {
            this._lfdNumber = lfdNumber;
        }

        public void generateLfdNumber() {
            if (!_code.isEmpty()) {
                setLfdNumber(_code.substring(_code.length() - 3, _code.length()));
            }
        }
    }
}
