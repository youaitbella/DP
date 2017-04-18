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
    PeppProposalSystemYear(2015),
    NubRequestSystemYear(2015),
    DrgProposalSystemYear(2015),
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
