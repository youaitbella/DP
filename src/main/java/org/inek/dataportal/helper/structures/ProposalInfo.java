package org.inek.dataportal.helper.structures;

import org.inek.dataportal.enums.WorkflowStatus;

/**
 *
 * @author muellermi
 */
public class ProposalInfo {

    private int _id;
    private String _name;
    private int _year;
    private WorkflowStatus _status;  

    public ProposalInfo() {
    }

    public ProposalInfo(final int id, final String name, final int year, final WorkflowStatus status) {
        _id = id;
        _name = name;
        _status = status;
        _year = year;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public WorkflowStatus getStatus() {
        return _status;
    }

    public void setStatus(WorkflowStatus status) {
        _status = status;
    }

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        _year = year;
    }

}
