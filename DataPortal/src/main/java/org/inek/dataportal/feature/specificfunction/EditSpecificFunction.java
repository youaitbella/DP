/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.specificfunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.inek.dataportal.entities.specificfunction.SpecificFunctionRequest;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.SpecificFunctionFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class EditSpecificFunction extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger _logger = Logger.getLogger("EditSpecificFunctionRequest");

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject ApplicationTools _appTools;

    private String _script;
    private SpecificFunctionRequest _request;

    public SpecificFunctionRequest getRequest() {
        return _request;
    }

    public void setRequest(SpecificFunctionRequest request) {
        this._request = request;
    }

    // </editor-fold>
    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = "" + params.get("id");
        if (id.equals("new")) {
            _request = newSpecificFunctionRequest();
        } else if (Utils.isInteger(id)) {
            SpecificFunctionRequest request = loadSpecificFunctionRequest(id);
            if (request.getId() == -1) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            _request = request;
        } else {
            Utils.navigate(Pages.Error.RedirectURL());
        }
    }

    private SpecificFunctionRequest loadSpecificFunctionRequest(String idObject) {
        int id = Integer.parseInt(idObject);
        SpecificFunctionRequest calcBasics = _specificFunctionFacade.findSpecificFunctionRequest(id);
        if (hasSufficientRights(calcBasics)) {
            return calcBasics;
        }
        return new SpecificFunctionRequest();
    }

    private boolean hasSufficientRights(SpecificFunctionRequest calcBasics) {
        if (_sessionController.isMyAccount(calcBasics.getAccountId(), false)) {
            return true;
        }
        return _cooperationTools.isAllowed(Feature.CALCULATION_HOSPITAL, calcBasics.getStatus(), calcBasics.getAccountId());
    }
    
    private SpecificFunctionRequest newSpecificFunctionRequest() {
        Account account = _sessionController.getAccount();
        SpecificFunctionRequest calcBasics = new SpecificFunctionRequest();
        calcBasics.setAccountId(account.getId());
        return calcBasics;
    }

    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isOwnStatement() {
        return _sessionController.isMyAccount(_request.getAccountId(), false);
    }

    public boolean isReadOnly() {
        return _cooperationTools.isReadOnly(Feature.CALCULATION_HOSPITAL, _request.getStatus(), _request.getAccountId(), _request.getIk());
    }

    @Override
    protected void addTopics() {
        addTopic("lblFrontPage", Pages.CalcDrgBasics.URL());
    }

    public String save() {
        setModifiedInfo();
        _request = _specificFunctionFacade.saveSpecificFunctionRequest(_request);

        if (isValidId(_request.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    private void setModifiedInfo() {
        _request.setLastChanged(Calendar.getInstance().getTime());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsDrgSendEnabled)) {
            return false;
        }
        return _cooperationTools.isSealedEnabled(Feature.CALCULATION_HOSPITAL, _request.getStatus(), _request.getAccountId());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsDrgSendEnabled)) {
            return false;
        }
        return _cooperationTools.isApprovalRequestEnabled(Feature.CALCULATION_HOSPITAL, _request.getStatus(), _request.getAccountId());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsDrgSendEnabled)) {
            return false;
        }
        return _cooperationTools.isRequestCorrectionEnabled(Feature.CALCULATION_HOSPITAL, _request.getStatus(), _request.getAccountId());
    }

    public boolean isTakeEnabled() {
        return _cooperationTools.isTakeEnabled(Feature.CALCULATION_HOSPITAL, _request.getStatus(), _request.getAccountId());
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
        if (!requestIsComplete()) {
            return getActiveTopic().getOutcome();
        }

        _request.setStatus(WorkflowStatus.Provided);
        setModifiedInfo();
        _request = _specificFunctionFacade.saveSpecificFunctionRequest(_request);

        if (isValidId(_request.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL") + " " + _request.getId());
            Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_request));
            return Pages.PrintView.URL();
        }
        return "";
    }

    private boolean requestIsComplete() {
        // todo
        return true;
    }

    public String requestApproval() {
        if (!requestIsComplete()) {
            return null;
        }
        _request.setStatus(WorkflowStatus.ApprovalRequested);
        setModifiedInfo();
        _request = _specificFunctionFacade.saveSpecificFunctionRequest(_request);
        return "";
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        _request.setAccountId(_sessionController.getAccountId());
        setModifiedInfo();
        _request = _specificFunctionFacade.saveSpecificFunctionRequest(_request);
        return "";
    }
    
    public void ikChanged() {
    }

    public List<SelectItem> getIks() {
        Set<Integer> iks = new HashSet<>();
        if (_request != null && _request.getIk() > 0) {
            iks.add(_request.getIk());
        }
        Account account = _sessionController.getAccount();
        if (account.getIK() > 0){
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
