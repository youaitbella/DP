/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.additionalcost;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.AdditionalCostFacade;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;

/**
 *
 * @author aitbellayo
 */

@Named
@RequestScoped
public class AdditionalCostList {
    
    @Inject private AdditionalCostFacade _additionalCostFacade;
    @Inject private SessionController _sessionController;
    @Inject private CooperationTools _cooperationTools;
    @Inject private ApplicationTools _appTools;
    
        public String newAdditionalCost() {
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("EditAdditionalCost");
        return Pages.AdditionalCostEdit.URL();
    }
}
