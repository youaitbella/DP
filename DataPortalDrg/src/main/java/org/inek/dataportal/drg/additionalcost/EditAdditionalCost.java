/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.drg.additionalcost;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.helper.FeatureMessageHandler;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.adm.facade.InekRoleFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.helper.structures.MessageContainer;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.utils.DocumentationUtil;
import org.inek.dataportal.drg.additionalcost.entity.AdditionalCost;
import org.inek.dataportal.drg.additionalcost.facade.AdditionalCostFacade;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "AdditionalCost")
public class EditAdditionalCost extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger LOGGER = Logger.getLogger("EditAdditionalCostRequest");
    private static final long serialVersionUID = 1L;
    @Inject private AccessManager _accessManager;
    @Inject private SessionController _sessionController;
    @Inject private AdditionalCostFacade _additionalCostFacade;
    @Inject private ApplicationTools _appTools;
    @Inject private Mailer _mailer;

    private static final int LAST_YEAR = 2021;
    private static final int FIRST_YEAR = 2017;
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
        return _accessManager.isAccessAllowed(
                Feature.ADDITIONAL_COST,
                additionalCost.getStatus(),
                additionalCost.getAccountId(),
                additionalCost.getIk()
        );
    }

    private AdditionalCost newAdditionalCost() {
        Account account = _accessManager.getSessionAccount();
        AdditionalCost additionalCost = new AdditionalCost();
        additionalCost.setAccountId(account.getId());
        additionalCost.setContactFirstName(account.getFirstName());
        additionalCost.setContactLastName(account.getLastName());
        additionalCost.setContactPhone(account.getPhone());
        additionalCost.setContactEmail(account.getEmail());
        int defaultYear = Utils.getTargetYear(Feature.ADDITIONAL_COST);
        additionalCost.setCalenderYear(defaultYear);
        additionalCost.setPeriodFrom(defaultYear);
        additionalCost.setPeriodTo(defaultYear);
        additionalCost.setPeriodTo(defaultYear);
        additionalCost.setRepaymentPeriodFrom(defaultYear);
        additionalCost.setRepaymentPeriodTo(defaultYear);
        Set<Integer> iks = getIks();
        if (iks.size() == 1) {
            additionalCost.setIk(iks.stream().findFirst().get());
        }

        return additionalCost;
    }

    public List<Integer> getAvailableYears() {
        // might be a restricted list
        return getYears();
    }

    public List<Integer> getYears() {
        List<Integer> availableYears = new ArrayList<>();
        IntStream.rangeClosed(FIRST_YEAR, LAST_YEAR) // as of the contract
                .forEach(y -> availableYears.add(y));

        return availableYears;
    }

    @Override
    protected void addTopics() {
        addTopic("TopicFrontPage", Pages.CalcDrgBasics.URL());
    }

    public String save() {
        setModifiedInfo();
        _additionalCost = _additionalCostFacade.saveAdditionalCost(_additionalCost);

        if (isValidId(_additionalCost.getId())) {
            String script = "alert ('" + Utils.getMessage("msgSaveAndMentionSend").replace("\r\n", "\n").
                    replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    // used by XHTML
    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isReadOnly() {
        if (_additionalCost == null) {
            return true;
        }
        return _accessManager.isReadOnly(
                Feature.ADDITIONAL_COST,
                _additionalCost.getStatus(),
                _additionalCost.getAccountId(),
                _additionalCost.getIk());
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsAdditionalRequestSendEnabled)) {
            return false;
        }
        if (_additionalCost == null) {
            return false;
        }
        return _accessManager.isSealedEnabled(
                Feature.ADDITIONAL_COST,
                _additionalCost.getStatus(),
                _additionalCost.getAccountId(),
                _additionalCost.getIk()
        );
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsAdditionalRequestSendEnabled)) {
            return false;
        }
        if (_additionalCost == null) {
            return false;
        }
        return _accessManager.isApprovalRequestEnabled(
                Feature.ADDITIONAL_COST,
                _additionalCost.getStatus(),
                _additionalCost.getAccountId(),
                _additionalCost.getIk()
        );
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
        return _accessManager != null
                && _additionalCost != null
                && _accessManager.isTakeEnabled(
                        Feature.ADDITIONAL_COST,
                        _additionalCost.getStatus(),
                        _additionalCost.getAccountId(),
                        _additionalCost.getIk()
                );
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
            Utils.getFlash().put("headLine", FeatureMessageHandler.getMessage("nameADDITIONAL_COST"));
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
        _additionalCost.setAccountId(_accessManager.getSessionAccountId());
        _additionalCost = _additionalCostFacade.merge(_additionalCost);
        return "";
    }

    public void ikChanged() {
        // dummy listener, used by component MultiIk - do not delete
    }

    private Set<Integer> _iks = new HashSet<>();

    public Set<Integer> getIks() {
        if (_iks.isEmpty()) {
            _iks = _accessManager.obtainIksForCreation(Feature.ADDITIONAL_COST);
        }
        return _iks;
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

    private void checkField(MessageContainer message, Integer value, Integer minValue, Integer maxValue, String msgKey,
            String elementId) {
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
