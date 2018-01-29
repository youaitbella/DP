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
public enum CalcInfoType {
    SOP("Statement of Participance"),
    CBD("Calculation Basics Drg"),
    CBP("Calculation Basics Psy"),
    CBI("Calculation Basics Investment"),
    CBA("Calculation Basics Autopsy"),
    CDM("Clinical Distribution Model");

    private final String _description;

    CalcInfoType(String description) {
        _description = description;
    }

    @Override
    public String toString() {
        return _description;
    }

}
