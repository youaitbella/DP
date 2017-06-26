/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.additionalcost;

import org.inek.dataportal.entities.additionalcost.AdditionalCost;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountAdditionalIK;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.AdditionalCostFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class EditAdditionalCost extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger LOGGER = Logger.getLogger("EditAdditionalCostRequest");

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private AdditionalCostFacade _additionalCostFacade;
    @Inject private ApplicationTools _appTools;

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
        AdditionalCost additionalCost = _additionalCostFacade.find(id);
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
        return _cooperationTools.isAllowed(Feature.ADDITIONAL_COST, additionalCost.getStatus(), additionalCost.getAccountId());
    }

    private AdditionalCost newAdditionalCost() {
        Account account = _sessionController.getAccount();
        AdditionalCost additionalCost = new AdditionalCost();
        additionalCost.setHospital(account.getCompany());
        additionalCost.setStreet(account.getStreet());
        return additionalCost;
    }

    public List<SelectItem> getTypeItems() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(1, "im Krankenhausplan des Landes"));
        items.add(new SelectItem(2, "durch gleichartige Festlegung durch zuständige Landesbehörde"));
        return items;
    }

    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isOwnStatement() {
        return _sessionController.isMyAccount(_additionalCost.getAccountId(), false);
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

    public String save() {//TODO
        return "";
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
    public String seal() {//Todo
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

}
