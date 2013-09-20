/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.enums;

/**
 *
 * @author muellermi
 */
public enum NubStatus {

    Unknown(-1, "Unbekannt", ""),
    New(0, "Neu", ""),
    Rejected(1, "Abgelehnt (Fehler)", "error.png"),
    ApprovalRequested(5, "Freigabe erforderlich", "arrow-expand-icon.png"),
    Provided(10, "Bereitgestellt", "timed.png"),
    ReProvided(11, "Bereitgestellt (korrigiert)", "timed.png"),
    Accepted(20, "Angenommen", "accept.png"),
    Retired(200, "Zurückgezogen", "delete-cross.png")
    ;

    private final int _value;
    private final String _description;
    private final String _icon;

    public static NubStatus fromValue (int value){
        for (NubStatus status : NubStatus.values()){
            if (status.getValue() == value){return status;}
        }
        return NubStatus.Unknown;
    }
            
    private NubStatus(int value, String description, String icon) {
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
