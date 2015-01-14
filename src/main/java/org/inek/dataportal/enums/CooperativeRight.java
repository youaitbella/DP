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

    None, // no access granted
    ReadOnly, // partner may read
    ReadSealed, // partner may read sealed
    ReadWrite, // partner may read, write
    ReadWriteSeal, // partner may read, write, seal
    ReadCompletedSealSupervisor, // partner may read completed. To be sealed by partner only.
    ReadWriteCompletedSealSupervisor, // partner may read, write completed. To be sealed by partner only.
    ReadWriteSealSupervisor;    // partner may read, write incompleted. To be sealed by partner only.

    private CooperativeRight() {
    }

    public boolean canReadAlways() {
        return name().matches("^Read(?!.*(Sealed|Completed)).*");
    }

    public boolean canReadCompleted() {
        return name().startsWith("Read") && !name().equals("ReadSealed");
    }

    public boolean canReadSealed() {
        return name().startsWith("Read");
    }

    public boolean canWriteAlways() {
        return name().matches("^ReadWrite(?!.*(Completed)).*");
    }
    
    public boolean canWriteCompleted() {
        return name().startsWith("ReadWrite");
    }
    
    public boolean canSealAlways() {
        return !name().contains("Completed") && canSealCompleted();
    }
    
    public boolean canSealCompleted() {
        return name().endsWith("SealSupervisor") || name().equals("ReadWriteSeal");
    }
    
    public boolean isSupervisor() {
        return name().contains("Supervisor");
    }
    
    public String Description() {
        return Utils.getMessage("cor" + name());
    }
}
