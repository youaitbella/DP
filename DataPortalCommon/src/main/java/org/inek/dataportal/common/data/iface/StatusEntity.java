/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.iface;

import org.inek.dataportal.common.enums.WorkflowStatus;

/**
 *
 * @author muellermi
 */
public interface StatusEntity {
    int getId();
    void setId(int id);
    
    WorkflowStatus getStatus();
    void setStatus(WorkflowStatus status);
    
}
