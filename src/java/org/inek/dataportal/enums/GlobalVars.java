/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.enums;

/**
 * Defines global numeric values
 * @author muellermi
 */
public enum GlobalVars {
    PeppProposalCodeFirstYear(2012),
    PeppProposalCodeLastYear(2013);
    
    private int _val;
    private GlobalVars(int val){
        _val = val;
    }
    
    public int getVal(){
        return _val;
    }
}
