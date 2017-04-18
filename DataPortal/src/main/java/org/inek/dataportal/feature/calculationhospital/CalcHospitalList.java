/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.util.HashMap;
import java.util.List;
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
import org.inek.dataportal.entities.calc.DistributionModel;
import org.inek.dataportal.entities.calc.DrgCalcBasics;
import org.inek.dataportal.entities.calc.PeppCalcBasics;
import org.inek.dataportal.entities.calc.StatementOfParticipance;
import org.inek.dataportal.enums.CalcHospitalFunction;
import org.inek.dataportal.enums.CalcInfoType;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.calc.CalcFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.calc.DistributionModelFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.utils.KeyValueLevel;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class CalcHospitalList {

    // <editor-fold defaultstate="collapsed" desc="fields">
    private static final Logger LOGGER = Logger.getLogger(CalcHospitalList.class.getName());

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private CalcFacade _calcFacade;
    @Inject private DistributionModelFacade _distModelFacade;
    @Inject private AccountFacade _accountFacade;
    @Inject private ApplicationTools _appTools;
    private final Map<CalcHospitalFunction, Boolean> _allowedButtons = new HashMap<>();
    // </editor-fold>

    public boolean isNewStatementOfParticipanceAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsStatemenOfParticipanceCreateEnabled)) {
            return false;
        }
        if (!_allowedButtons.containsKey(CalcHospitalFunction.StatementOfParticipance)) {
            boolean testMode = _appTools.isEnabled(ConfigKey.TestMode);
            Set<Integer> iks = _calcFacade.obtainIks4NewStatementOfParticipance(_sessionController.getAccountId(), Utils.getTargetYear(Feature.CALCULATION_HOSPITAL), testMode);
            _allowedButtons.put(CalcHospitalFunction.StatementOfParticipance, iks.size() > 0);
        }
        return _allowedButtons.get(CalcHospitalFunction.StatementOfParticipance);
    }

    public String newStatementOfParticipance() {
        return Pages.StatementOfParticipanceEditAddress.URL();
    }

    public boolean isNewDistributionModelDrgAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsDistributionModelDrgCreateEnabled)) {
            return false;
        }
        return determineDistModelButtonAllowed(CalcHospitalFunction.ClinicalDistributionModelDrg);
    }

    public boolean isNewDistributionModelPeppAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsDistributionModelPeppCreateEnabled)) {
            return false;
        }
        return determineDistModelButtonAllowed(CalcHospitalFunction.ClinicalDistributionModelPepp);
    }

    private boolean determineDistModelButtonAllowed(CalcHospitalFunction calcFunct) {
        if (!_allowedButtons.containsKey(calcFunct)) {
            boolean testMode = _appTools.isEnabled(ConfigKey.TestMode);
            Set<Integer> possibleIks = _distModelFacade.obtainIks4NewDistributionModel(calcFunct, _sessionController.getAccountId(), Utils.getTargetYear(Feature.CALCULATION_HOSPITAL), testMode);
            Account account = _sessionController.getAccount();
            boolean isAllowed = possibleIks.contains(account.getIK())
                    || account.getAdditionalIKs().stream().anyMatch(ai -> possibleIks.contains(ai.getIK()));
            _allowedButtons.put(calcFunct, isAllowed);
        }
        return _allowedButtons.get(calcFunct);
    }

    public boolean isNewCalculationBasicsDrgAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsDrgCreateEnabled)) {
            return false;
        }
        return determineButtonAllowed(CalcHospitalFunction.CalculationBasicsDrg);
    }

    public String newCalculationBasicsDrg() {
        return Pages.CalcDrgEdit.RedirectURL();
    }

    public boolean isNewCalculationBasicsPeppAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsPsyCreateEnabled)) {
            return false;
        }
        return determineButtonAllowed(CalcHospitalFunction.CalculationBasicsPepp);
    }

    public String newCalculationBasicsPepp() {
        return Pages.CalcPeppEdit.RedirectURL(); 
    }

    public boolean isNewCalculationBasicsObdAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsObdCreateEnabled)) {
            return false;
        }
        return determineButtonAllowed(CalcHospitalFunction.CalculationBasicsPepp);
    }

    public String newCalculationBasicsObd() {
        return Pages.CalcObdEdit.RedirectURL(); 
    }

    private boolean determineButtonAllowed(CalcHospitalFunction calcFunct) {
        if (!_allowedButtons.containsKey(calcFunct)) {
//            Set<Integer> accountIds = _cooperationTools.determineAccountIds(Feature.CALCULATION_HOSPITAL, canReadSealed());
            boolean testMode = _appTools.isEnabled(ConfigKey.TestMode);
            Set<Integer> possibleIks = _calcFacade.obtainIks4NewBasics(calcFunct, _sessionController.getAccountId(), Utils.getTargetYear(Feature.CALCULATION_HOSPITAL), testMode);
            Account account = _sessionController.getAccount();
            boolean isAllowed = possibleIks.contains(account.getIK())
                    || account.getAdditionalIKs().stream().anyMatch(ai -> possibleIks.contains(ai.getIK()));
            _allowedButtons.put(calcFunct, isAllowed);
        }
        return _allowedButtons.get(calcFunct);
    }

    public String printHospitalInfo(CalcHospitalInfo hospitalInfo) {
        switch (hospitalInfo.getType()) {
            case SOP:
                return printStatementOfParticipance(hospitalInfo);
            case CBD:
                return printCalculationBasicsDrg(hospitalInfo);
            case CBP:
                return printCalculationBasicsPepp(hospitalInfo);
            case CDM:
                return printDistributionModel(hospitalInfo);
            default:
                throw new IllegalArgumentException("Unknown calcInfoType: " + hospitalInfo.getType());
        }
    }

    public String printStatementOfParticipance(CalcHospitalInfo hospitalInfo) {
        StatementOfParticipance statement = _calcFacade.findStatementOfParticipance(hospitalInfo.getId());
        List<KeyValueLevel> documentation = DocumentationUtil.getDocumentation(statement);
        return preparePrintView(documentation);
    }

    private String preparePrintView(List<KeyValueLevel> documentation) {
        Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL"));
        Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
        Utils.getFlash().put("printContent", documentation);
        return Pages.PrintView.URL();
    }

    private String printCalculationBasicsDrg(CalcHospitalInfo hospitalInfo) {
        DrgCalcBasics calcBasics = _calcFacade.findCalcBasicsDrg(hospitalInfo.getId());
        List<KeyValueLevel> documentation = DocumentationUtil.getDocumentation(calcBasics);
        return preparePrintView(documentation);
    }

    private String printCalculationBasicsPepp(CalcHospitalInfo hospitalInfo) {
        PeppCalcBasics calcBasics = _calcFacade.findCalcBasicsPepp(hospitalInfo.getId());
        List<KeyValueLevel> documentation = DocumentationUtil.getDocumentation(calcBasics);
        return preparePrintView(documentation);
    }

    private String printDistributionModel(CalcHospitalInfo hospitalInfo) {
        DistributionModel model = _distModelFacade.findDistributionModel(hospitalInfo.getId());
        List<KeyValueLevel> documentation = DocumentationUtil.getDocumentation(model);
        return preparePrintView(documentation);
    }

    public String deleteHospitalInfo(CalcHospitalInfo hospitalInfo) {
        switch (hospitalInfo.getType()) {
            case SOP:
                deleteStatementOfParticipance(hospitalInfo);
                break;
            case CBD:
                deleteCalculationBasicsDrg(hospitalInfo);
                break;
            case CBP:
                deleteCalculationBasicsPepp(hospitalInfo);
                break;
            case CDM:
                deleteDistributionModel(hospitalInfo);
                break;
            default:
                throw new IllegalArgumentException("Unknown calcInfoType: " + hospitalInfo.getType());
        }
        return "";
    }

    private void deleteStatementOfParticipance(CalcHospitalInfo hospitalInfo) {
        StatementOfParticipance statement = _calcFacade.findStatementOfParticipance(hospitalInfo.getId());
        if (statement == null) {
            // might be deleted by somebody else
            return;
        }
        if (statement.getStatus().getId() >= WorkflowStatus.Provided.getId()) {
            statement.setStatus(WorkflowStatus.Retired);
            _calcFacade.saveStatementOfParticipance(statement);
        } else {
            _calcFacade.delete(statement);
        }
    }

    private void deleteCalculationBasicsDrg(CalcHospitalInfo hospitalInfo) {
        DrgCalcBasics calcBasics = _calcFacade.findCalcBasicsDrg(hospitalInfo.getId());
        if (calcBasics.getStatus().getId() >= WorkflowStatus.Provided.getId()) {
            calcBasics.setStatus(WorkflowStatus.Retired);
            _calcFacade.saveCalcBasicsDrg(calcBasics);
        } else {
            _calcFacade.delete(calcBasics);
        }
    }

    private void deleteCalculationBasicsPepp(CalcHospitalInfo hospitalInfo) {
        PeppCalcBasics calcBasics = _calcFacade.findCalcBasicsPepp(hospitalInfo.getId());
        if (calcBasics.getStatus().getId() >= WorkflowStatus.Provided.getId()) {
            calcBasics.setStatus(WorkflowStatus.Retired);
            _calcFacade.saveCalcBasicsPepp(calcBasics);
        } else {
            _calcFacade.delete(calcBasics);
        }
    }

    private void deleteDistributionModel(CalcHospitalInfo hospitalInfo) {
        DistributionModel model = _distModelFacade.findDistributionModel(hospitalInfo.getId());
        if (model.getStatus().getId() >= WorkflowStatus.Provided.getId()) {
            model.setStatus(WorkflowStatus.Retired);
            _distModelFacade.saveDistributionModel(model);
        } else {
            _distModelFacade.deleteDistributionModel(model);
        }
    }

    public String editHospitalInfo(CalcInfoType type) {
        switch (type) {
            case SOP:
                return Pages.StatementOfParticipanceEditAddress.URL();
            case CBD:
                return Pages.CalcDrgEdit.RedirectURL();
            case CBP:
                return Pages.CalcPeppEdit.URL();
            case CDM:
                return Pages.CalcDistributionModel.URL();
            default:
                throw new IllegalArgumentException("Unknown calcInfoType: " + type);
        }
    }

}
