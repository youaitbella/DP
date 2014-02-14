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
public enum CooperativeRight {
    None,                       // no access granted
    ReadOnly,                   // partner may read
    ReadWrite,                  // partner may read, write
    ReadWriteSeal,              // partner may read, write, seal
    ReadSealed,                // partner may read sealed
    ReadCompletedSealSupervisor,         // partner may read completed. To be sealed by partner only.
    ReadWriteCompletedSealSupervisor,    // partner may read, write completed. To be sealed by partner only.
    ReadWriteSealSupervisor;    // partner may read, write incompleted. To be sealed by partner only.
}
