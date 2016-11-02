/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class CalculationHospitalList {

    // <editor-fold defaultstate="collapsed" desc="fields">
    private static final Logger _logger = Logger.getLogger("CalculationHospitalList");

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject ApplicationTools _appTools;
    // </editor-fold>
    
    public boolean isNewStatementOfParticipanceAllowed(){
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        return true; // todo check other conditions
    }
    
    public String newStatementOfParticipance() {
        // if the user hit the browser's back-button, a request might be still active. 
        // To prevent invoking the wrong, we destroy all Feature scoped beans first
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("EditStatementOfParticipance");
        return Pages.StatementOfParticipanceEditAddress.URL();
    }
    
    public boolean isNewCalculationBasicsDrgAllowed(){
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        return true; // todo check other conditions
    }
    
    public String newCalculationBasicsDrg() {
        return Pages.StatementOfParticipanceEditAddress.URL();  // todo
    }
    
    public boolean isNewCalculationBasicsPeppAllowed(){
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        return true; // todo check other conditions
    }
    
    public String newCalculationBasicsPepp() {
        return Pages.StatementOfParticipanceEditAddress.URL();  // todo
    }
    
    public String printHospitalInfo(int id){
        return "";
    }
    
    public String deleteHospitalInfo(int id){
        return "";
    }
    
    public String editHospitalInfo(int id){
        int type = id % 10;
        switch (type){
            case 0:
                return Pages.StatementOfParticipanceEditAddress.URL();
            case 1:
                return Pages.StatementOfParticipanceEditAddress.URL();
            case 2:
                return Pages.StatementOfParticipanceEditAddress.URL();
        }
        return "";
    }
}
