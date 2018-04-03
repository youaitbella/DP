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
public class FlowFactory implements Serializable {

    @Produces @FlowDefinition
    public Flow defineFlow(@FlowBuilderParameter FlowBuilder flowBuilder) {
        flowBuilder.id("", "UserMaintenanceFlow");
        flowBuilder.viewNode(Pages.UserMaintenanceMasterData.name(), Pages.UserMaintenanceMasterData.URL());
        flowBuilder.viewNode(Pages.UserMaintenanceAdditionalIKs.name(), Pages.UserMaintenanceAdditionalIKs.URL());
        flowBuilder.viewNode(Pages.UserMaintenanceConfig.name(), Pages.UserMaintenanceConfig.URL());
        flowBuilder.viewNode(Pages.UserMaintenanceFeatures.name(), Pages.UserMaintenanceFeatures.URL());
        flowBuilder.viewNode(Pages.UserMaintenanceOther.name(), Pages.UserMaintenanceOther.URL());
        return flowBuilder.getFlow();
    }

}
