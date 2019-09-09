package org.inek.dataportal.common.helper.structures;

import org.inek.dataportal.common.enums.WorkflowStatus;

import java.io.Serializable;

/**
 *
 * @author muellermi
 */
public class ProposalInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int _id;
    private final String _name;
    private final int _year;
    private final WorkflowStatus _status;
    private final int _ik;
    private final String _tag;  // for additional info, e.g. external status

    public ProposalInfo(final int id, final String name, final int year, final WorkflowStatus status) {
        this(id, name, year, status, -1, "");
    }

    public ProposalInfo(final int id, final String name, String displayName, final int year, final int statusId, Integer ik, String tag) {
        this(id, (displayName.trim().length() == 0 ? name : displayName), year, WorkflowStatus.fromValue(statusId), ik, tag);
    }

    public ProposalInfo(final int id, final String name, final int year, final WorkflowStatus status, Integer ik, String tag) {
        _id = id;
        _name = name;
        _status = status;
        _year = year;
        _ik = ik == null ? 0 : ik;
        _tag = tag;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public WorkflowStatus getStatus() {
        return _status;
    }

    public int getYear() {
        return _year;
    }

    public int getIk() {
        return _ik;
    }

    public String getTag() {
        return _tag;
    }
}
