package org.inek.dataportal.drg.specificfunction.backingbean;

import java.io.Serializable;
import java.util.Date;
import org.inek.dataportal.common.enums.WorkflowStatus;

public class SpecificFunctionListItem implements Serializable {

    public SpecificFunctionListItem(int id, int year, int statusId, int ik, Date sendDate, String code) {
        _id = id;
        _year = year;
        _status = WorkflowStatus.fromValue(statusId);
        _ik = ik;
        _sendDate = sendDate;
        _code = code;
    }

    private int _id;
    private int _year;
    private WorkflowStatus _status;
    private int _ik;
    private String _hospitalName;
    private String _town;
    private Date _sendDate;
    private String _code;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        _year = year;
    }

    public WorkflowStatus getStatus() {
        return _status;
    }

    public void setStatus(WorkflowStatus status) {
        _status = status;
    }

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        _ik = ik;
    }

    public String getHospitalName() {
        return _hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        _hospitalName = hospitalName;
    }

    public String getTown() {
        return _town;
    }

    public void setTown(String town) {
        _town = town;
    }

    public Date getSendDate() {
        return _sendDate;
    }

    public void setSendDate(Date sendDate) {
        _sendDate = sendDate;
    }

    public String getCode() {
        return _code;
    }

    public void setCode(String code) {
        _code = code;
    }

    public String getSequence() {
        if (_code.isEmpty()) {
            return "";
        }
        return _code.substring(_code.length() - 3, _code.length());
    }
}
