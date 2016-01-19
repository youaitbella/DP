package org.inek.dataportal.helper.structures;

import org.inek.dataportal.enums.WorkflowStatus;

/**
 *
 * @author muellermi
 */
public class EntityInfo {

    public EntityInfo(Integer id, String code, String description, WorkflowStatus status, int accountId) {
        _id = id;
        _code = code;
        _description = description;
        _status = status;
        _accountId = accountId;
    }

    // <editor-fold defaultstate="collapsed" desc="Id">
    private final Integer _id;
    public Integer getId() {
        return _id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Code">
    private final String _code;

    public String getCode() {
        return _code;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Description">
    private final String _description;

    public String getDescription() {
        return _description;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Status">
    private final WorkflowStatus _status;
    public WorkflowStatus getStatus() {
        return _status;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Id">
    private final int _accountId;
    public int getAccountId() {
        return _accountId;
    }
    // </editor-fold>


}
