/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.enums;

import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public enum CooperativeRight {

    None("---"), // no access granted
    ReadOnly("a--"), // partner may read
    ReadSealed("s--"), // partner may read sealed
    ReadWrite("aa-"), // partner may read, write
    ReadWriteSeal("aa+"), // partner may read, write, seal
    ReadCompletedSealSupervisor("c-+"), // partner may read completed. To be sealed by partner only.
    ReadWriteCompletedSealSupervisor("cc+"), // partner may read, write completed. To be sealed by partner only.
    ReadSealSupervisor("a-+"), // partner may read always. To be sealed by partner only.
    ReadWriteSealSupervisor("aa+");    // partner may read, write incompleted. To be sealed by partner only.

    /**
     * rights are defined as a three letter string
     * first letter is read access: - = none; a = always; c = if at least completed; s = if sealed
     * second letter is write access: - = none; a = always; c = if at least completed; s = if sealed
     * third letter is supervisor: - = none; + = right to seal 
     */
    private final String _rights;
    private CooperativeRight(String rights) {
        _rights=rights;
    }

    public boolean canReadAlways() {
        return _rights.matches("a..");
    }

    public boolean canReadCompleted() {
        return _rights.matches("[ac]..");
    }

    public boolean canReadSealed() {
        return _rights.matches("[acs].*");
    }

    public boolean canWriteAlways() {
        return _rights.matches(".a.");
    }
    
    public boolean canWriteCompleted() {
        return _rights.matches(".[ac].");
    }
    
    public boolean canSeal() {
        return _rights.matches("..\\+");
    }
   
    public CooperativeRight fromRightsAsString(String rights){
        for (CooperativeRight right : CooperativeRight.values()){
            if (rights.equalsIgnoreCase(right._rights)){
                return right;
            }
        }
        return CooperativeRight.None;
    }
    
    public String getRightsAsString(){
        return _rights;
    }
    
    public String Description() {
        return Utils.getMessage("cor" + name());
    }
}
