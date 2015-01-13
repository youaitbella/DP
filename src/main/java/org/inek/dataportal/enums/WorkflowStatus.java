package org.inek.dataportal.enums;

/**
 *
 * @author muellermi
 */
public enum WorkflowStatus {

    Unknown(-1, "Unbekannt", ""),
    New(0, "Neu / in Erfassung", "edit.png"),
    Rejected(1, "Abgelehnt (Fehler)", "error.png"),
    ApprovalRequested(5, "Freigabe erforderlich", "timed.png"),
    Provided(10, "Bereitgestellt", "timed.png"),  // aka Sealed
    ReProvided(11, "Bereitgestellt (korrigiert)", "timed.png"),
    Accepted(20, "Angenommen", "accept.png"),
    Retired(200, "Zurückgezogen", "delete-cross.png")
    ;

    private final int _value;
    private final String _description;
    private final String _icon;

    public static WorkflowStatus fromValue (int value){
        for (WorkflowStatus status : WorkflowStatus.values()){
            if (status.getValue() == value){return status;}
        }
        return WorkflowStatus.Unknown;
    }
            
    private WorkflowStatus(int value, String description, String icon) {
        _value = value;
        _description = description;
        _icon = icon;
    }

    public int getValue() {
        return _value;
    }

    public String getDescription() {
        return _description;
    }

    public String getIcon() {
        return _icon;
    }

    @Override
    public String toString(){
        return _description;
    }
}
