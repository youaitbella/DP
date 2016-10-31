/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.util.Calendar;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.StatementOfParticipance;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.CalcFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped
public class EditStatementOfParticipance extends AbstractEditController {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger _logger = Logger.getLogger("EditStatementOfParticipance");

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private CalcFacade _calcFacade;
    @Inject ApplicationTools _appTools;
    
    private String _script;
    private StatementOfParticipance _statement;

    enum StatementOfParticipanceTabs {
        tabStatementOfParticipanceAddress,
        tabStatementOfParticipanceStatements
    }

    // </editor-fold>

    @PostConstruct
    private void init() {

        Object statementOfParticipanceControllerId = Utils.getFlash().get("statementOfParticipanceControllerId");
        if (statementOfParticipanceControllerId == null) {
            _statement = newStatementOfParticipance();
        } else {
            _statement = loadStatementOfParticipance(statementOfParticipanceControllerId);
        }

    }

    private StatementOfParticipance loadStatementOfParticipance(Object statementOfParticipanceControllerId) {
        try {
            int id = Integer.parseInt("" + statementOfParticipanceControllerId);
            StatementOfParticipance statement = _calcFacade.findStatementOfParticipance(id);
            if (_cooperationTools.isAllowed(Feature.CALCULATION_HOSPITAL, statement.getStatus(), statement.getAccountId())) {
                return statement;
            }
        } catch (NumberFormatException ex) {
            _logger.info(ex.getMessage());
        }
        return newStatementOfParticipance();
    }

    private StatementOfParticipance newStatementOfParticipance() {
        Account account = _sessionController.getAccount();
        StatementOfParticipance statement = new StatementOfParticipance();
        statement.setAccountId(account.getId());
        return statement;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">

    // </editor-fold>
    
    @Override
    protected void addTopics() {
        addTopic(StatementOfParticipanceTabs.tabStatementOfParticipanceAddress.name(), Pages.StatementOfParticipanceEditAddress.URL());
        addTopic(StatementOfParticipanceTabs.tabStatementOfParticipanceStatements.name(), Pages.StatementOfParticipanceEditStatements.URL());
    }

    
    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isReadOnly() {
        return _cooperationTools.isReadOnly(Feature.CALCULATION_HOSPITAL, _statement.getStatus(), _statement.getAccountId(), _statement.getIk());
    }


    public String save() {
        setModifiedInfo();
        _statement = _calcFacade.saveStatementOfParticipance(_statement);

        if (isValidId(_statement.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    private void setModifiedInfo() {
        _statement.setLastChanged(Calendar.getInstance().getTime());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        return _cooperationTools.isSealedEnabled(Feature.CALCULATION_HOSPITAL, _statement.getStatus(), _statement.getAccountId());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        return _cooperationTools.isApprovalRequestEnabled(Feature.CALCULATION_HOSPITAL, _statement.getStatus(), _statement.getAccountId());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        return _cooperationTools.isRequestCorrectionEnabled(Feature.CALCULATION_HOSPITAL, _statement.getStatus(), _statement.getAccountId());
    }

    public boolean isTakeEnabled() {
        return _cooperationTools.isTakeEnabled(Feature.CALCULATION_HOSPITAL, _statement.getStatus(), _statement.getAccountId());
    }

    /**
     * This function seals a statement od participance if possible. Sealing is possible, if
     * all mandatory fields are fulfilled. After sealing, the statement od participance can not
     * be edited anymore and is available for the InEK.
     *
     * @return
     */
    public String seal() {
        if (!statementIsComplete()) {
            return getActiveTopic().getOutcome();
        }

        _statement.setStatus(WorkflowStatus.Provided);
        setModifiedInfo();
        _statement = _calcFacade.saveStatementOfParticipance(_statement);

        if (isValidId(_statement.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL") + " " + _statement.getId());
            Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_statement));
            return Pages.PrintView.URL();
        }
        return "";
    }

    private boolean statementIsComplete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public String requestApprovalDrgProposal() {
        if (!statementIsComplete()) {
            return null;
        }
        _statement.setStatus(WorkflowStatus.ApprovalRequested);
        setModifiedInfo();
        _statement = _calcFacade.saveStatementOfParticipance(_statement);
        return "";
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        _statement.setAccountId(_sessionController.getAccountId());
        setModifiedInfo();
        _statement = _calcFacade.saveStatementOfParticipance(_statement);
        return "";
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Tab XXX">
    // </editor-fold>
    
}
