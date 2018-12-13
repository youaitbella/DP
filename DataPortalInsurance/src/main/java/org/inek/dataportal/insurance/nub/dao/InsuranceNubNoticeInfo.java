package org.inek.dataportal.insurance.nub.dao;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import org.inek.dataportal.common.data.iface.StatusEntity;
import org.inek.dataportal.common.enums.WorkflowStatus;

public class InsuranceNubNoticeInfo implements StatusEntity {

    public InsuranceNubNoticeInfo(int id) {
        _id = id;
    }

    public InsuranceNubNoticeInfo(int id, Date creationDate, int workflowStatusId, int year, int hospitalIk) {
        _id = id;
        _creationDate = creationDate;
        _workflowStatus = WorkflowStatus.fromValue(workflowStatusId);
        _year = year;
        _hospitalIk = hospitalIk;
    }
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int value) {
        _id = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CreationDate">
    private Date _creationDate;

    public Date getCreationDate() {
        return _creationDate;
    }

    public void setCreationDate(Date value) {
        _creationDate = value;
    }

    public String getCreationDateFormatted() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return df.format(_creationDate);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property WorkflowStatusId">
    private WorkflowStatus _workflowStatus;

    @Override
    public WorkflowStatus getStatus() {
        return _workflowStatus;
    }

    @Override
    public void setStatus(WorkflowStatus value) {
        _workflowStatus = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Year">
    private int _year = Calendar.getInstance().get(Calendar.YEAR);

    public int getYear() {
        return _year;
    }

    public void setYear(int value) {
        _year = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property HospitalIk">
    private int _hospitalIk = -1;

    public int getHospitalIk() {
        return _hospitalIk;
    }

    public void setHospitalIk(int value) {
        _hospitalIk = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    // </editor-fold>
}
