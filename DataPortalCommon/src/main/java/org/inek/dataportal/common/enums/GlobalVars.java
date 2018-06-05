/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.enums;

/**
 * Defines global numeric values
 * @author muellermi
 */
public enum GlobalVars {
    ProposalSectionDg(101),
    ProposalSectionPepp(102),
    ProposalSectionNub(103);
    
    private final int _val;
    GlobalVars(int val){
        _val = val;
    }
    
    public int getVal(){
        return _val;
    }
}
