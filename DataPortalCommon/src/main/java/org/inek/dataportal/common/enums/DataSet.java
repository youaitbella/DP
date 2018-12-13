package org.inek.dataportal.common.enums;

public enum DataSet {
    None(WorkflowStatus.Deleted, WorkflowStatus.Deleted),
    AllOpen(WorkflowStatus.New, WorkflowStatus.ApprovalRequested),
    ApprovalRequested(WorkflowStatus.ApprovalRequested, WorkflowStatus.ApprovalRequested),  // this is a subset of AllOpen
    AllSealed(WorkflowStatus.Provided, WorkflowStatus.Retired),
    All(WorkflowStatus.New, WorkflowStatus.Retired);
    
    private final WorkflowStatus _minStatus;
    private final WorkflowStatus _maxStatus;
    
    DataSet(WorkflowStatus minStatus, WorkflowStatus maxStatus){
        _minStatus = minStatus;
        _maxStatus = maxStatus;
    }

    public WorkflowStatus getMinStatus() {
        return _minStatus;
    }

    public WorkflowStatus getMaxStatus() {
        return _maxStatus;
    }
    
}
