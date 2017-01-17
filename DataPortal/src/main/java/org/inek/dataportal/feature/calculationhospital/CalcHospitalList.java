/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import static org.inek.dataportal.common.CooperationTools.canReadSealed;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.entities.calc.DrgCalcBasics;
import org.inek.dataportal.entities.calc.PeppCalcBasics;
import org.inek.dataportal.entities.calc.StatementOfParticipance;
import org.inek.dataportal.enums.CalcHospitalFunction;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.CalcFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.utils.DocumentationUtil;

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
    @Inject private AccountFacade _accountFacade;
    @Inject ApplicationTools _appTools;
    private final Map<CalcHospitalFunction, Boolean> _allowedButtons = new HashMap<>();
    // </editor-fold>

    public boolean isNewStatementOfParticipanceAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        if (!_allowedButtons.containsKey(CalcHospitalFunction.StatementOfParticipance)) {
            Set<Integer> iks = _calcFacade.obtainIks4NewStatementOfParticipance(_sessionController.getAccountId(), Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
            _allowedButtons.put(CalcHospitalFunction.StatementOfParticipance, iks.size() > 0);
        }
        return _allowedButtons.get(CalcHospitalFunction.StatementOfParticipance);
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

    public boolean isNewCalculationBasicsDrgAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        return determineButtonAllowed(CalcHospitalFunction.CalculationBasicsDrg);
    }

    public String newCalculationBasicsDrg() {
        destroyFeatureBeans();

        return Pages.CalcDrgEdit.RedirectURL();
    }

    public boolean isNewCalculationBasicsPeppAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        return determineButtonAllowed(CalcHospitalFunction.CalculationBasicsPepp);
    }

    private boolean determineButtonAllowed(CalcHospitalFunction calcFunct) {
        if (!_allowedButtons.containsKey(calcFunct)) {
            Set<Integer> accountIds = _cooperationTools.determineAccountIds(Feature.CALCULATION_HOSPITAL, canReadSealed());
            Set<Integer> possibleIks = _calcFacade.obtainIks4NewBasiscs(calcFunct, accountIds, Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
            Account account = _sessionController.getAccount();
            boolean isAllowed = possibleIks.contains(account.getIK())
                    || account.getAdditionalIKs().stream().anyMatch(ai -> possibleIks.contains(ai.getIK()));
            _allowedButtons.put(calcFunct, isAllowed);
        }
        return _allowedButtons.get(calcFunct);
    }

    public String newCalculationBasicsPepp() {
        destroyFeatureBeans();
        return Pages.StatementOfParticipanceEditAddress.URL();  // todo
    }

    public String printHospitalInfo(CalcHospitalInfo hospitalInfo) {
        switch (hospitalInfo.getType()) {
            case 0:
                return printStatementOfParticipance(hospitalInfo);
            case 1:
                return printCalculationBasicsDrg(hospitalInfo);
            case 2:
                return printCalculationBasicsPepp(hospitalInfo);
        }
        return "";
    }

    public String printStatementOfParticipance(CalcHospitalInfo hospitalInfo) {
        StatementOfParticipance statement = _calcFacade.findStatementOfParticipance(hospitalInfo.getId());
        Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL"));
        Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(statement));
        return Pages.PrintView.URL();
    }

    private String printCalculationBasicsDrg(CalcHospitalInfo hospitalInfo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String printCalculationBasicsPepp(CalcHospitalInfo hospitalInfo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String deleteHospitalInfo(CalcHospitalInfo hospitalInfo) {
        switch (hospitalInfo.getType()) {
            case 0:
                deleteStatementOfParticipance(hospitalInfo);
                break;
            case 1:
                deleteCalculationBasicsDrg(hospitalInfo);
                break;
            case 2:
                deleteCalculationBasicsPepp(hospitalInfo);
                break;
        }
        return "";
    }

    private void deleteStatementOfParticipance(CalcHospitalInfo hospitalInfo) {
        StatementOfParticipance statement = _calcFacade.findStatementOfParticipance(hospitalInfo.getId());
        if (statement.getStatus().getValue() >= WorkflowStatus.Provided.getValue()) {
            statement.setStatus(WorkflowStatus.Retired);
            _calcFacade.saveStatementOfParticipance(statement);
        } else {
            _calcFacade.delete(statement);
        }
    }

    private void deleteCalculationBasicsDrg(CalcHospitalInfo hospitalInfo) {
        DrgCalcBasics calcBasics = _calcFacade.findCalcBasicsDrg(hospitalInfo.getId());
        if (calcBasics.getStatus().getValue() >= WorkflowStatus.Provided.getValue()) {
            calcBasics.setStatus(WorkflowStatus.Retired);
            _calcFacade.saveCalcBasicsDrg(calcBasics);
        } else {
            _calcFacade.delete(calcBasics);
        }
    }

    private void deleteCalculationBasicsPepp(CalcHospitalInfo hospitalInfo) {
        PeppCalcBasics calcBasics = _calcFacade.findCalcBasicsPepp(hospitalInfo.getId());
        if (calcBasics.getStatus().getValue() >= WorkflowStatus.Provided.getValue()) {
            calcBasics.setStatus(WorkflowStatus.Retired);
            _calcFacade.saveCalcBasicsPepp(calcBasics);
        } else {
            _calcFacade.delete(calcBasics);
        }
    }

    public String editHospitalInfo(int type) {
        destroyFeatureBeans();
        switch (type) {
            case 0:
                return Pages.StatementOfParticipanceEditAddress.URL();
            case 1:
                return Pages.CalcDrgEdit.RedirectURL();
            case 2:
                return Pages.StatementOfParticipanceEditAddress.URL();
        }
        return "";
    }

}
