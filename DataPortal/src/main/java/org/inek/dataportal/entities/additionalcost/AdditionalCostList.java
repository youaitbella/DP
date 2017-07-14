/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.additionalcost;

import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.AdditionalCostFacade;
import org.inek.dataportal.feature.calculationhospital.EditStatementOfParticipance;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author aitbellayo
 */
@Named
@RequestScoped
public class AdditionalCostList {

    private static final Logger LOGGER = Logger.getLogger(AdditionalCostList.class.getName());
    
    //<editor-fold defaultstate="collapsed" desc="fields">
    @Inject private AdditionalCostFacade _additionalCostFacade;
    @Inject private ApplicationTools _appTools;
    //</editor-fold>
    
    public boolean isNewAllowed(){
        return _appTools.isEnabled(ConfigKey.IsAdditionalCostCreateEnabled);
    }

    public String newAdditionalCost() {
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("EditAdditionalCost");
        return Pages.AdditionalCostEdit.URL();
    }
    
    private void destroyFeatureBeans() {
        // if the user hit the browser's back-button, a request might be still active.
        // To prevent invoking the wrong, we destroy all Feature scoped beans first
        FeatureScopedContextHolder.Instance.destroyBeansOfScope(EditStatementOfParticipance.class.getSimpleName());
        // todo: add other classes
    }
    
    public String print(AdditionalCost additionalCost){
        Utils.getFlash().put("headLine", Utils.getMessage("nameADDITIONAL_COST"));
        Utils.getFlash().put("targetPage", Pages.AdditionalCostSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(additionalCost));
        return Pages.PrintView.URL();
    }
    
    public void delete(AdditionalCost addAdditionalCost){
        if(addAdditionalCost == null){
            return;
        }
        if(addAdditionalCost.getStatus().getId() >= WorkflowStatus.Provided.getId()){
            addAdditionalCost.setStatus(WorkflowStatus.Retired);
            _additionalCostFacade.saveAdditionalCost(addAdditionalCost);
        }else{
            _additionalCostFacade.deleteAdditionalCost(addAdditionalCost);
        }
    }
    
    public String edit(){
        destroyFeatureBeans();
        return Pages.AdditionalCostEdit.URL();
    }
    
}
