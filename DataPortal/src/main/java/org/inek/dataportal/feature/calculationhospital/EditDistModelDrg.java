/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
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
import org.inek.dataportal.entities.calc.DistributionModel;
import org.inek.dataportal.entities.calc.DistributionModelDetail;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.calc.DistModelFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.MessageContainer;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class EditDistModelDrg extends AbstractEditController implements Serializable{

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger _logger = Logger.getLogger("EditDistributionModel");

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private DistModelFacade _calcFacade;
    @Inject ApplicationTools _appTools;

    private String _script;
    private DistributionModel _model;

    public DistributionModel getRequest() {
        return _model;
    }

    public void setRequest(DistributionModel model) {
        this._model = model;
    }
    // </editor-fold>
    
    
    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = "" + params.get("id");
        if (id.equals("new")) {
            _model = newDistributionModel();
        } else if (Utils.isInteger(id)) {
            DistributionModel model = loadDistributionModel(id);
            if (model.getId() == -1) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            _model = model;
        } else {
            Utils.navigate(Pages.Error.RedirectURL());
        }
    }

    private DistributionModel loadDistributionModel(String idObject) {
        int id = Integer.parseInt(idObject);
        DistributionModel model = _calcFacade.findDistributionModel(id);
        if (hasSufficientRights(model)) {
            return model;
        }
        return new DistributionModel();
    }

    private boolean hasSufficientRights(DistributionModel calcBasics) {
        if (_sessionController.isMyAccount(calcBasics.getAccountId(), false)) {
            return true;
        }
        return _cooperationTools.isAllowed(Feature.CALCULATION_HOSPITAL, calcBasics.getStatus(), calcBasics.getAccountId());
    }
    
    private DistributionModel newDistributionModel() {
        Account account = _sessionController.getAccount();
        DistributionModel request = new DistributionModel();
        request.setAccountId(account.getId());
        request.setDataYear(Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        return request;
    }

    
    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isOwnStatement() {
        return _sessionController.isMyAccount(_model.getAccountId(), false);
    }

    public boolean isReadOnly() {
        return _cooperationTools.isReadOnly(Feature.CALCULATION_HOSPITAL, _model.getStatus(), _model.getAccountId(), _model.getIk());
    }

    @Override
    protected void addTopics() {
        addTopic("lblFrontPage", Pages.CalcDrgBasics.URL());
    }

    public String save() {
        removeEmptyCenters();
        setModifiedInfo();
        _model = _calcFacade.saveDistributionModel(_model);
        addDetailIfMissing();
        
        if (isValidId(_model.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    private void setModifiedInfo() {
        _model.setLastChanged(Calendar.getInstance().getTime());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsDrgSendEnabled)) {
            return false;
        }
        return _cooperationTools.isSealedEnabled(Feature.CALCULATION_HOSPITAL, _model.getStatus(), _model.getAccountId());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsDrgSendEnabled)) {
            return false;
        }
        return _cooperationTools.isApprovalRequestEnabled(Feature.CALCULATION_HOSPITAL, _model.getStatus(), _model.getAccountId());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsDrgSendEnabled)) {
            return false;
        }
        return _cooperationTools.isRequestCorrectionEnabled(Feature.CALCULATION_HOSPITAL, _model.getStatus(), _model.getAccountId());
    }

    public boolean isTakeEnabled() {
        return _cooperationTools.isTakeEnabled(Feature.CALCULATION_HOSPITAL, _model.getStatus(), _model.getAccountId());
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
        removeEmptyCenters();
        _model.setStatus(WorkflowStatus.Provided);
        setModifiedInfo();
        _model = _calcFacade.saveDistributionModel(_model);

        if (isValidId(_model.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL") + " " + _model.getId());
            Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_model));
            return Pages.PrintView.URL();
        }
        return "";
    }

    private boolean requestIsComplete() {
        MessageContainer message = composeMissingFieldsMessage(_model);
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

    public MessageContainer composeMissingFieldsMessage(DistributionModel model) {
        MessageContainer message = new MessageContainer();

        String ik = model.getIk() < 0 ? "" : "" + model.getIk();
        checkField(message, ik, "lblIK", "specificFuntion:ikMulti");

        for (DistributionModelDetail detail : model.getDetails()) {
            checkField(message, detail.getArticle(), "Bitte Artikel angeben", "");
            // todo
        }
                
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

    public String requestApproval() {
        if (!requestIsComplete()) {
            return null;
        }
        _model.setStatus(WorkflowStatus.ApprovalRequested);
        setModifiedInfo();
        _model = _calcFacade.saveDistributionModel(_model);
        return "";
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        _model.setAccountId(_sessionController.getAccountId());
        setModifiedInfo();
        _model = _calcFacade.saveDistributionModel(_model);
        return "";
    }
    
    public void ikChanged() {
        System.out.println("IK changed");
        // dummy listener
    }

    public List<SelectItem> getIks() {
        Set<Integer> iks = new HashSet<>();
        if (_model != null && _model.getIk() > 0) {
            iks.add(_model.getIk());
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
    
    public void addDetail(){
        DistributionModelDetail detail = new DistributionModelDetail(_model.getId());
        _model.getDetails().add(detail);
    }
    
    public void deleteDetail(DistributionModelDetail detail){
        _model.getDetails().remove(detail);
    }
    
    // </editor-fold>

    private void removeEmptyCenters() {
        Iterator<DistributionModelDetail> iter = _model.getDetails().iterator();
        while (iter.hasNext()){
            DistributionModelDetail detail = iter.next();
            if (detail.isEmpty()){
                iter.remove();
            }
        }
    }

    private void addDetailIfMissing() {
        if (_model.getDetails().isEmpty()){
            _model.getDetails().add(new DistributionModelDetail(_model.getId()));
        }
    }

}

