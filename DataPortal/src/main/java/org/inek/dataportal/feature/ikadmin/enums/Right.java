/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.ikadmin.enums;

/**
 *
 * @author muellermi
 */
public enum Right {
    Deny("Zugriff verboten"),
    Read("Daten nur anzeigen"),
    Write("Daten anzeigen und ändern"),
    Create("Daten anlegen, anzeigen, ändern"),
    Seal("Daten anzeigen und senden"),
    All("Daten anlegen, anzeigen, ändern, senden");

    private final String _description;
    
    Right(String description) {
        _description = description;
    }

    public String getKey() {
        return name().substring(0, 1);
    }
    
    public void setKey(String dummy){
    }
    
    public String getDescription() {
        return _description;
    }
    
    public static Right getRightFromKey(String key) {
        for(Right right : Right.values()) {
            if(right.getKey().equals(key))
                return right;
        }
        throw new IllegalArgumentException("Failed to obtain right. Unknown key " + key);
    }
    
}
