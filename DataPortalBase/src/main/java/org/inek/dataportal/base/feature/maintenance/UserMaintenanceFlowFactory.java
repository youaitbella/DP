/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.base.feature.maintenance;

import java.io.Serializable;
import javax.enterprise.inject.Produces;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowBuilderParameter;
import javax.faces.flow.builder.FlowDefinition;
import org.inek.dataportal.common.enums.Pages;

/**
 *
 * @author muellermi
 */

public class UserMaintenanceFlowFactory implements Serializable{
    @Produces @FlowDefinition
    public Flow defineUserMaintenanceFlow( @FlowBuilderParameter FlowBuilder flowBuilder) {
        String flowId = "UserMaintenance";   
        flowBuilder.id("", flowId); 
        
        
        flowBuilder.viewNode(flowId, Pages.UserMaintenanceMasterData.URL()).markAsStartNode();
        
        
        return flowBuilder.getFlow();
    }
    
}
