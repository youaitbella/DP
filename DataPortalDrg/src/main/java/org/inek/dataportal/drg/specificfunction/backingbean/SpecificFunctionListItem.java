package org.inek.dataportal.drg.specificfunction.backingbean;

import java.io.Serializable;
import java.util.Date;
import org.inek.dataportal.common.enums.WorkflowStatus;

public class SpecificFunctionListItem implements Serializable {

    public SpecificFunctionListItem(int id, int year, int statusId, 
            int ik, String name, String town, Date sendDate, String code) {
        _id = id;
        _year = year;
        _status = WorkflowStatus.fromValue(statusId);
        _ik = ik;
        _hospitalName = name;
        _town = town;
        _sendDate = sendDate;
        _code = code;
    }

    private final int _id;
    private final int _year;
    private final WorkflowStatus _status;
    private final int _ik;
    private final String _hospitalName;
    private final String _town;
    private final Date _sendDate;
    private final String _code;

    public int getId() {
        return _id;
    }

    public int getYear() {
        return _year;
    }

    public WorkflowStatus getStatus() {
        return _status;
    }

    public int getIk() {
        return _ik;
    }

    public String getHospitalName() {
        return _hospitalName;
    }

    public String getTown() {
        return _town;
    }

    public Date getSendDate() {
        return _sendDate;
    }

    public String getCode() {
        return _code;
    }

    public String getSequence() {
        if (_code.isEmpty()) {
            return "";
        }
        return _code.substring(_code.length() - 3, _code.length());
    }
}
