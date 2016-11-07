/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import static org.inek.dataportal.common.CooperationTools.canReadSealed;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.CalcFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class CalcHospitalList {

    // <editor-fold defaultstate="collapsed" desc="fields">
    private static final Logger _logger = Logger.getLogger(CalcHospitalList.class.getName());

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private CalcFacade _calcFacade;
    @Inject ApplicationTools _appTools;
    // </editor-fold>
    
    public boolean isNewStatementOfParticipanceAllowed(){
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        return true; // todo check other conditions
    }
    
    public String newStatementOfParticipance() {
        destroyFeatureBeans();
        return Pages.StatementOfParticipanceEditAddress.URL();
    }

    private void destroyFeatureBeans() {
        // if the user hit the browser's back-button, a request might be still active.
        // To prevent invoking the wrong, we destroy all Feature scoped beans first
        FeatureScopedContextHolder.Instance.destroyBeansOfScope(EditStatementOfParticipance.class.getSimpleName());
        // todo: add other classes
    }
    
    public boolean isNewCalculationBasicsDrgAllowed(){
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        Set<Integer> accountIds = _cooperationTools.determineAccountIds(Feature.CALCULATION_HOSPITAL, canReadSealed());
//        List<CalcHospitalInfo> calcInfos = _calcFacade.getListCalcInfo(accountIds, Utils.getTargetYear(Feature.CALCULATION_HOSPITAL), WorkflowStatus.New, WorkflowStatus.TakenUpdated);
//        List<CalcHospitalInfo> statementInfos = calcInfos
//                .stream()
//                .filter(i -> i.getType() == 0 && i.getStatusId() >= WorkflowStatus.Provided.getValue())
//                .collect(Collectors.toList());
        
        
        return true; // todo check other conditions
    }
    
    public String newCalculationBasicsDrg() {
        destroyFeatureBeans();
        // TODO: check if allowed to create new calculation basic
        return Pages.CalcDrgAdditionalInformationDiagnosticArea.URL();  // TODO: start new DRG calc at tab 1
    }
    
    public boolean isNewCalculationBasicsPeppAllowed(){
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        return true; // todo check other conditions
    }
    
    public String newCalculationBasicsPepp() {
        destroyFeatureBeans();
        return Pages.StatementOfParticipanceEditAddress.URL();  // todo
    }
    
    public String printHospitalInfo(int id){
        return "";
    }
    
    public String deleteHospitalInfo(int id){
        return "";
    }
    
    public String editHospitalInfo(int type){
        destroyFeatureBeans();
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
