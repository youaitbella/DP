/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.additionalcost;

import org.inek.dataportal.entities.additionalcost.AdditionalCost;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.common.SessionTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountAdditionalIK;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.AdditionalCostFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.feature.admin.facade.InekRoleFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.helper.structures.MessageContainer;
import org.inek.dataportal.mail.Mailer;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "AdditionalCost")
public class EditAdditionalCost extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger LOGGER = Logger.getLogger("EditAdditionalCostRequest");

    @Inject
    private CooperationTools _cooperationTools;
    @Inject
    private SessionController _sessionController;
    @Inject
    private AdditionalCostFacade _additionalCostFacade;
    @Inject
    private ApplicationTools _appTools;

    @Inject
    private AccountFacade _accountFacade;
    @Inject
    private Mailer _mailer;
    @Inject
    private SessionTools _sessionTools;

    private AdditionalCost _additionalCost;

    public AdditionalCost getAdditionalCost() {
        return _additionalCost;
    }

    public void setAdditionalCost(AdditionalCost additionalCost) {
        _additionalCost = additionalCost;
    }
    // </editor-fold>

    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = "" + params.get("id");
        if ("new".equals(id)) {
            _additionalCost = newAdditionalCost();
        } else if (Utils.isInteger(id)) {
            AdditionalCost additionalCost = loadAdditionalCost(id);
            if (additionalCost.getId() == -1) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            _additionalCost = additionalCost;
        } else {
            Utils.navigate(Pages.Error.RedirectURL());
        }
    }

    private AdditionalCost loadAdditionalCost(String idObject) {
        int id = Integer.parseInt(idObject);
        AdditionalCost additionalCost = _additionalCostFacade.findAdditionalCost(id);
        if (hasSufficientRights(additionalCost)) {
            return additionalCost;
        }
        return new AdditionalCost();
    }

    private boolean hasSufficientRights(AdditionalCost additionalCost) {
        if (_sessionController.isMyAccount(additionalCost.getAccountId(), false)) {
            return true;
        }
        if (_sessionController.isInekUser(Feature.ADDITIONAL_COST)) {
            return true;
        }
        return _cooperationTools.isAccessAllowed(Feature.ADDITIONAL_COST, additionalCost.getStatus(), additionalCost.getAccountId());
    }

    private AdditionalCost newAdditionalCost() {
        Account account = _sessionController.getAccount();
        AdditionalCost additionalCost = new AdditionalCost();
        additionalCost.setAccountId(account.getId());
        additionalCost.setContactFirstName(account.getFirstName());
        additionalCost.setContactLastName(account.getLastName());
        additionalCost.setContactPhone(account.getPhone());
        additionalCost.setContactEmail(account.getEmail());
        additionalCost.setCalenderYear(Utils.getTargetYear(Feature.SPECIFIC_FUNCTION));
        additionalCost.setPeriodFrom(Utils.getTargetYear(Feature.SPECIFIC_FUNCTION));
        additionalCost.setPeriodTo(Utils.getTargetYear(Feature.SPECIFIC_FUNCTION));
        additionalCost.setRepaymentPeriodFrom(Utils.getTargetYear(Feature.SPECIFIC_FUNCTION));
        additionalCost.setRepaymentPeriodFrom(Utils.getTargetYear(Feature.SPECIFIC_FUNCTION));
        if (getAvailableYears().size() > 0) {
            additionalCost.setPeriodTo(getAvailableYears().get(0));
        }
        return additionalCost;
    }

    public List<Integer> getAvailableYears() {
        // might be a restricted list
        return getYears();
    }
    public List<Integer> getYears() {
        List<Integer> availableYears = new ArrayList<>();
        IntStream.rangeClosed(2017, 2021) // as of the contract
                .forEach(y -> availableYears.add(y));

        return availableYears;
    }

    public boolean isReadOnly() {
        if (_additionalCost == null) {
            return true;
        }
        if (_sessionController.isInekUser(Feature.ADDITIONAL_COST) && !_appTools.isEnabled(ConfigKey.TestMode)) {
            return true;
        }
        return _cooperationTools.isReadOnly(
                Feature.ADDITIONAL_COST,
                _additionalCost.getStatus(),
                _additionalCost.getAccountId(),
                _additionalCost.getIk());
    }

    @Override
    protected void addTopics() {
        addTopic("TopicFrontPage", Pages.CalcDrgBasics.URL());
    }

    public String save() {
        setModifiedInfo();
        _additionalCost = _additionalCostFacade.saveAdditionalCost(_additionalCost);

        if (isValidId(_additionalCost.getId())) {
            String script = "alert ('" + Utils.getMessage("msgSaveAndMentionSend").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsAdditionalRequestSendEnabled)) {
            return false;
        }
        if (_additionalCost == null) {
            return false;
        }
        return _cooperationTools.isSealedEnabled(Feature.ADDITIONAL_COST, _additionalCost.getStatus(), _additionalCost.getAccountId());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsAdditionalRequestSendEnabled)) {
            return false;
        }
        if (_additionalCost == null) {
            return false;
        }
        return _cooperationTools.isApprovalRequestEnabled(Feature.ADDITIONAL_COST, _additionalCost.getStatus(), _additionalCost.getAccountId());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsAdditionalRequestSendEnabled)) {
            return false;
        }
        if (_additionalCost == null) {
            return false;
        }
        return (_additionalCost.getStatus() == WorkflowStatus.Provided || _additionalCost.getStatus() == WorkflowStatus.ReProvided)
                && _sessionController.isInekUser(Feature.ADDITIONAL_COST, true);
    }

    public boolean isTakeEnabled() {
        return _cooperationTools != null
                && _additionalCost != null
                && _cooperationTools.isTakeEnabled(Feature.ADDITIONAL_COST, _additionalCost.getStatus(), _additionalCost.getAccountId());
    }

    /**
     * This function seals a statement od participance if possible. Sealing is possible, if all mandatory fields are
     * fulfilled. After sealing, the statement od participance can not be edited anymore and is available for the InEK.
     *
     * @return
     */
    public String seal() {
        if (!requestIsComplete()) {
            return null;
        }
        setModifiedInfo();
        _additionalCost.setStatus(WorkflowStatus.Provided);
        _additionalCost = _additionalCostFacade.saveAdditionalCost(_additionalCost);

        if (isValidId(_additionalCost.getId())) {
            sendNotification();
            Utils.getFlash().put("headLine", Utils.getMessage("nameADDITIONAL_COST"));
            Utils.getFlash().put("targetPage", Pages.AdditionalCostSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_additionalCost));
            return Pages.PrintView.URL();
        }
        return "";
    }

    public String requestApproval() {
        _additionalCost.setStatus(WorkflowStatus.ApprovalRequested);
        _additionalCost = _additionalCostFacade.merge(_additionalCost);
        return "";
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        _additionalCost.setAccountId(_sessionController.getAccountId());
        _additionalCost = _additionalCostFacade.merge(_additionalCost);
        return "";
    }

    public void ikChanged() {
        // dummy listener, used by component MultiIk - do not delete
    }

    public List<SelectItem> getIks() {
        Set<Integer> iks = new HashSet<>();
        if (_additionalCost != null && _additionalCost.getIk() > 0) {
            iks.add(_additionalCost.getIk());
        }
        Account account = _sessionController.getAccount();
        if (account.getIK() != null && account.getIK() > 0) {
            iks.add(account.getIK());
        }
        for (AccountAdditionalIK additionalIK : account.getAdditionalIKs()) {
            iks.add(additionalIK.getIK());
        }
        List<SelectItem> items = new ArrayList<>();
        for (int ik : iks) {
            items.add(new SelectItem(ik));
        }
        return items;
    }
    // </editor-fold>

    @Inject
    private InekRoleFacade _inekRoleFacade;

    public void sendNotification() {
        List<Account> inekAccounts = _inekRoleFacade.findForFeature(Feature.ADDITIONAL_COST);
        String receipients = inekAccounts.stream().map(a -> a.getEmail()).collect(Collectors.joining(";"));
        _mailer.sendMail(receipients, "Besondere Aufgaben / Zentrum", "Es wurde ein Datensatz an das InEK gesendet.");
    }

    private boolean requestIsComplete() {
        MessageContainer message = composeMissingFieldsMessage(_additionalCost);
        if (message.containsMessage()) {
            message.setMessage(Utils.getMessage("infoMissingFields") + "\\r\\n" + message.getMessage());
            //setActiveTopic(message.getTopic());
            String script = "alert ('" + message.getMessage() + "');";
            if (!message.getElementId().isEmpty()) {
                script += "\r\n document.getElementById('" + message.getElementId() + "').focus();";
            }
            _sessionController.setScript(script);
        }
        return !message.containsMessage();
    }

    public MessageContainer composeMissingFieldsMessage(AdditionalCost additionalCost) {
        MessageContainer message = new MessageContainer();

        String ik = additionalCost.getIk() <= 0 ? "" : "" + additionalCost.getIk();
        checkField(message, ik, "lblIK", "additionalFunction:ikMulti");
        checkField(message, additionalCost.getContactFirstName(), "lblFirstName", "additionalFunction:firstName");
        checkField(message, additionalCost.getContactLastName(), "lblFirstName", "additionalFunction:lastName");
        checkField(message, additionalCost.getContactPhone(), "lblPhone", "additionalFunction:phone");
        checkField(message, additionalCost.getContactEmail(), "lblMail", "additionalFunction:mail");

        return message;
    }

    private void checkField(MessageContainer message, String value, String msgKey, String elementId) {
        if (Utils.isNullOrEmpty(value)) {
            applyMessageValues(message, msgKey, elementId);
        }
    }

    private void checkField(MessageContainer message, Integer value, Integer minValue, Integer maxValue, String msgKey, String elementId) {
        if (value == null
                || minValue != null && value < minValue
                || maxValue != null && value > maxValue) {
            applyMessageValues(message, msgKey, elementId);
        }
    }

    private void applyMessageValues(MessageContainer message, String msgKey, String elementId) {
        message.setMessage(message.getMessage() + "\\r\\n" + Utils.getMessageOrKey(msgKey));
        if (message.getTopic().isEmpty()) {
            message.setTopic("");
            message.setElementId(elementId);
        }
    }

    private void setModifiedInfo() {
        _additionalCost.setAdcLastChanged(Calendar.getInstance().getTime());
//        _additionalCost.setAccountIdLastChange(_sessionController.getAccountId());
    }
}
