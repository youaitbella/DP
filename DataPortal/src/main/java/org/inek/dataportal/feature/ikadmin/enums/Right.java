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
    Deny,
    Read,
    Write,
    Seal;

    Right() {
    }

    public String getKey() {
        return name().substring(0, 1);
    }
    
    public static Right getRightFromId(String key) {
        for(Right right : Right.values()) {
            if(right.getKey().equals(key))
                return right;
        }
        throw new IllegalArgumentException("Failed to obtain right. Unknown key " + key);
    }
    
}
