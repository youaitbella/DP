/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.StatementOfParticipance;
import org.inek.dataportal.entities.icmt.Customer;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.CalcFacade;
import org.inek.dataportal.facades.CustomerFacade;
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
    @Inject private CustomerFacade _customerFacade;

    private String _script;
    private StatementOfParticipance _statement;

    enum StatementOfParticipanceTabs {
        tabStatementOfParticipanceAddress,
        tabStatementOfParticipanceStatements
    }

    // </editor-fold>
    @PostConstruct
    private void init() {

        Object id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
        } else if (id.toString().equals("new")) {
            if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            _statement = newStatementOfParticipance();
        } else {
            _statement = loadStatementOfParticipance(id);
        }

    }

    private StatementOfParticipance loadStatementOfParticipance(Object idObject) {
        try {
            int id = Integer.parseInt("" + idObject);
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
    public StatementOfParticipance getStatement() {
        return _statement;
    }

    public void setStatement(StatementOfParticipance statement) {
        _statement = statement;
    }

    // </editor-fold>
    @Override
    protected void addTopics() {
        addTopic(StatementOfParticipanceTabs.tabStatementOfParticipanceAddress.name(), Pages.StatementOfParticipanceEditAddress.URL());
        addTopic(StatementOfParticipanceTabs.tabStatementOfParticipanceStatements.name(), Pages.StatementOfParticipanceEditStatements.URL());
    }

    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isOwnStatement() {
        return _sessionController.isMyAccount(_statement.getAccountId(), false);
    }

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
     * This function seals a statement od participance if possible. Sealing is
     * possible, if all mandatory fields are fulfilled. After sealing, the
     * statement od participance can not be edited anymore and is available for
     * the InEK.
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
        // todo
        return true;
    }

    public String requestApproval() {
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
    // <editor-fold defaultstate="collapsed" desc="Tab Address">
    public List<SelectItem> getIks() {
        Account account = _sessionController.getAccount();
        Set<Integer> iks = _sessionController.getAccount().getAdditionalIKs().stream().map(i -> i.getIK()).collect(Collectors.toSet());
        List<SelectItem> items = new ArrayList<>();
        if (account.getIK() != null) {
            iks.add(account.getIK());
        }
        if (_statement.getIk() > 0) {
            iks.add(_statement.getIk());
        }
        for (int ik : iks) {
            items.add(new SelectItem(ik));
        }
        if (_statement.getIk() <= 0) {
            items.add(0, new SelectItem(""));
        }
        return items;
    }

    String _hospitalInfo = "";

    public String getHospitalInfo() {
        return _hospitalInfo;
    }

    public void changedIk() {
        if (_statement != null) {
            Customer c = _customerFacade.getCustomerByIK(_statement.getIk());
            _hospitalInfo = c.getName() + ", " + c.getTown();
        }
    }

// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Tab Statements">
// </editor-fold>
}
