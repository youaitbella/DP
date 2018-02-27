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
public enum Quality {
    Poor("Red"),
    Medium("Orange"),
    Good("Yellow"),
    Strong("Green");
    
    private final String _color;

    Quality(String color) {
        _color = color;
    }
    
    public String getColor() {
        return _color;
    }
    
}
