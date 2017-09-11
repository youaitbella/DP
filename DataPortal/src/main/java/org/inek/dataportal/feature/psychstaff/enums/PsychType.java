/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.psychstaff.enums;

/**
 *
 * @author muellermi
 */
public enum PsychType {
    Unknown("U", "???", "???"),
    Adults("A", "ERW", "Erwachsene"),
    Kids("K", "KJP", "Kinder und Jugend");

    private final String _key;
    private final String _shortName;
    private final String _name;

    PsychType(String key, String shortName, String name) {
        _key = key;
        _shortName = shortName;
        _name = name;
    }

    public String getKey() {
        return _key;
    }

    public String getShortName() {
        return _shortName;
    }
    
    public String getName() {
        return _name;
    }
    
    public static PsychType getTypeFromKey(String key) {
        for(PsychType psychType : PsychType.values()) {
            if(psychType.getKey().equals(key))
                return psychType;
        }
        return PsychType.Unknown;
    }
    
    public static PsychType getTypeFromShortName(String shortName) {
        for(PsychType psychType : PsychType.values()) {
            if(psychType.getShortName().equals(shortName.toUpperCase()))
                return psychType;
        }
        return PsychType.Unknown;
    }
    
    
}
