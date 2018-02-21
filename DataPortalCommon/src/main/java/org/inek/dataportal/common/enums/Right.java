/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.enums;

/**
 *
 * @author muellermi
 */
public enum Right {
    Deny("Zugriff verboten", false, false, false, false),
    Read("Daten nur anzeigen", true, false, false, false),
    Write("Daten anzeigen und ändern", true, true, false, false),
    Create("Daten anlegen, anzeigen, ändern", true, true, true, false),
    Seal("Daten anzeigen und senden", true, false, false, true),
    All("Daten anlegen, anzeigen, ändern, senden", true, true, true, true);

    private final String _description;
    
    Right(String description, boolean canRead, boolean canWrite, boolean canCreate, boolean canSeal){
        _description = description;
        _canRead = canRead;
        _canWrite = canWrite;
        _canCreate = canCreate;
        _canSeal = canSeal;
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

    private final boolean _canRead;
    public boolean canRead(){
        return _canRead;
    }

    private final boolean _canWrite;
    public boolean canWrite(){
        return _canWrite;
    }

    private final boolean _canCreate;
    public boolean canCreate(){
        return _canCreate;
    }

    private final boolean _canSeal;
    public boolean canSeal(){
        return _canSeal;
    }
}
