/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.calc.entities.cdm.DistributionModel;
import org.inek.dataportal.calc.entities.cdm.DistributionModelDetail;
import org.inek.dataportal.calc.enums.CalcHospitalFunction;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.calc.facades.DistributionModelFacade;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.helper.structures.MessageContainer;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class EditDistributionModel extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger LOGGER = Logger.getLogger("EditDistributionModel");

    @Inject private AccessManager _accessManager;
    @Inject private SessionController _sessionController;
    @Inject private DistributionModelFacade _distModelFacade;
    @Inject private ApplicationTools _appTools;

    private String _script;
    private DistributionModel _model;

    public DistributionModel getModel() {
        return _model;
    }

    public void setModel(DistributionModel model) {
        this._model = model;
    }

    private DistributionModel _priorModel;
    public DistributionModel getPriorModel() {
        return _priorModel;
    }

    public void setPriorModel(DistributionModel model) {
        this._priorModel = model;
    }

    private boolean _showWide = false;

    public boolean isShowWide() {
        return _showWide;
    }

    public void setShowWide(boolean showWide) {
        this._showWide = showWide;
    }

    // </editor-fold>
    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = "" + params.get("id");
        String type = "" + params.get("type");
        if ("new".equals(id) && !"0".equals(type) && !"1".equals(type)) {
            _model = new DistributionModel();
            Utils.navigate(Pages.NotAllowed.RedirectURL());
            return;
        }
        if ("new".equals(id)) {
            _model = newDistributionModel(type);
        } else if (Utils.isInteger(id)) {
            DistributionModel model = loadDistributionModel(id);
            _model = model;
            if (model.getId() == -1) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }            
            if (isRequestCorrectionEnabled()){
                _priorModel = _distModelFacade.findPriorDistributionModel(_model);
            }
        } else {
            Utils.navigate(Pages.Error.RedirectURL());
        }
        addDetailIfMissing();
    }

    private DistributionModel loadDistributionModel(String idObject) {
        int id = Integer.parseInt(idObject);
        DistributionModel model = _distModelFacade.findDistributionModel(id);
        if (hasSufficientRights(model)) {
            return model;
        }
        return new DistributionModel();
    }

    private boolean hasSufficientRights(DistributionModel model) {
        if (isInekViewable(model)) {
            return true;
        }
        return _accessManager.isAccessAllowed(Feature.CALCULATION_HOSPITAL, model.getStatus(), model.getAccountId(), model.getIk());
    }

    private DistributionModel newDistributionModel(String type) {
        DistributionModel model = new DistributionModel();
        Account account = _sessionController.getAccount();
        model.setAccountId(account.getId());
        model.setDataYear(Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        model.setType("0".equals(type) ? 0 : 1);
        List<SelectItem> ikItems = getIkItems(model);
        if (ikItems.size() == 1) {
            model.setIk((int) ikItems.get(0).getValue());
        }
        return model;
    }

    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isOwnModel() {
        return _sessionController.isMyAccount(_model.getAccountId(), false);
    }

    public boolean isReadOnly() {
        return _accessManager.isReadOnly(Feature.CALCULATION_HOSPITAL, _model.getStatus(), _model.getAccountId(), _model.getIk())
                || _sessionController.isInekUser(Feature.CALCULATION_HOSPITAL) && !isOwnModel();
    }

    private boolean isInekEditable(DistributionModel model) {
        return _sessionController.isInekUser(Feature.CALCULATION_HOSPITAL, true) 
                && model != null && (model.getStatus() == WorkflowStatus.Provided || model.getStatus() == WorkflowStatus.ReProvided);
    }

    private boolean isInekViewable(DistributionModel model) {
        return _sessionController.isInekUser(Feature.CALCULATION_HOSPITAL, true) 
                && model != null && model.getStatusId() >= WorkflowStatus.Provided.getId();
    }

    @Override
    protected void addTopics() {
        addTopic("TopicFrontPage", Pages.CalcDrgBasics.URL());
    }

    public String save() {
        removeEmptyCenters();
        setModifiedInfo();
        _model = _distModelFacade.saveDistributionModel(_model);
        addDetailIfMissing();

        if (isValidId(_model.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSaveAndMentionSend").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            if (_model.getStatus() == WorkflowStatus.Taken) {
                return Pages.CalculationHospitalSummary.URL();
            }
            return null;
        }
        return Pages.Error.URL();
    }

    private void setModifiedInfo() {
        _model.setLastChanged(Calendar.getInstance().getTime());
        _model.setAccountIdLastChange(_sessionController.getAccountId());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsDistributionModelSendEnabled)) {
            return false;
        }
        return _accessManager.isSealedEnabled(Feature.CALCULATION_HOSPITAL, _model.getStatus(), _model.getAccountId(), _model.getIk());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsDistributionModelSendEnabled)) {
            return false;
        }
        return _accessManager.isApprovalRequestEnabled(Feature.CALCULATION_HOSPITAL, _model.getStatus(), _model.getAccountId(), _model.getIk());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsDistributionModelSendEnabled)) {
            return false;
        }
        return (_model.getStatus() == WorkflowStatus.Provided || _model.getStatus() == WorkflowStatus.ReProvided) 
                && _sessionController.isInekUser(Feature.CALCULATION_HOSPITAL, true)
                && _model != null;
    }

    public String requestCorrection() {
        if (!isRequestCorrectionEnabled()) {
            return "";
        }
        removeEmptyCenters();
        setModifiedInfo();
        // set as retired
        _model.setStatus(WorkflowStatus.Retired);
        _distModelFacade.saveDistributionModel(_model);
        
        // create copy to edit (persist detached object with default Ids)
        _model.setStatus(WorkflowStatus.New);
        _model.setId(-1);
        for (DistributionModelDetail detail : _model.getDetails()) {
            detail.setId(-1);
            detail.setMasterId(-1);
        }
        _distModelFacade.saveDistributionModel(_model);
        sendMessage("KVM Konkretisierung");

        return Pages.CalculationHospitalSummary.URL();
    }
    
    public boolean isSendApprovalEnabled() {
        return (_model.getStatus() == WorkflowStatus.Provided || _model.getStatus() == WorkflowStatus.ReProvided) 
                && _sessionController.isInekUser(Feature.CALCULATION_HOSPITAL, true)
                && _model != null
                && _model.getDetails().stream().allMatch(d -> d.isApproved());
    }

    public String sendApproval(){
        if (!isInekEditable(_model) || _model.getDetails().stream().anyMatch(d -> !d.isApproved())) {
            return "";
        }
        removeEmptyCenters();
        setModifiedInfo();
        _model.setStatus(WorkflowStatus.Taken);
        _distModelFacade.saveDistributionModel(_model);
        sendMessage("KVM Genehmigung");
        
        return Pages.CalculationHospitalSummary.URL();
    }
    
    @Inject private AccountFacade _accountFacade;
    @Inject private Mailer _mailer;

    private void sendMessage(String name) {
        Account receiver = _accountFacade.findAccount(_appTools.isEnabled(ConfigKey.TestMode) 
                ? _sessionController.getAccountId() 
                : _model.getAccountId());
        MailTemplate template = _mailer.getMailTemplate(name);
        String subject = template.getSubject()
                .replace("{type}", _model.getType() == 0 ? "DRG" : "PSY")
                .replace("{ik}", "" + _model.getIk());
        String body = template.getBody()
                .replace("{formalSalutation}", _mailer.getFormalSalutation(receiver))
                .replace("{note}", _model.getNoteInek());
        String bcc = template.getBcc().replace("{accountMail}", _sessionController.getAccount().getEmail());
        _mailer.sendMailFrom(template.getFrom(), receiver.getEmail(), "", bcc, subject, body);
    }

    public boolean isTakeEnabled() {
        return false;
        // todo: do not allow consultant
        //return _accessManager.isTakeEnabled(Feature.CALCULATION_HOSPITAL, _model.getStatus(), _model.getAccountId(), _model.getIk());
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
        if (!modelIsComplete()) {
            return "";
        }
        removeEmptyCenters();
        _model.setStatus(WorkflowStatus.Provided);
        setModifiedInfo();
        _model.setSealed(Calendar.getInstance().getTime());
        _model = _distModelFacade.saveDistributionModel(_model);

        if (isValidId(_model.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL"));
            Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_model));
            return Pages.PrintView.URL();
        }
        return "";
    }

    private boolean modelIsComplete() {
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

        checkField(message, model.getIk(), 100000000, 999999999, "lblIK", "distributionModel:ikMulti");

        int line = 0;
        for (DistributionModelDetail detail : model.getDetails()) {
            line++;
            checkField(message, detail.getArticle(), "Zeile " + line + ": Bitte Artikel angeben", "distributionModel:details");
            checkField(message, detail.getCostCenterId(), 1, 999, "Zeile " + line + ": Bitte Kostenstellengruppe wählen"
                    , "distributionModel:details");
            checkField(message, detail.getCostTypeId(), 1, 999, "Zeile " + line + ": Bitte Kostenartengruppe wählen"
                    , "distributionModel:details");
            if (_model.getType() == 1) {
                checkField(message, detail.getCountCaredays(), 1, 999999, "Zeile " + line + ": Bitte Anzahl Pflegetage angeben"
                        , "distributionModel:details");
                if (detail.getCountCaredays() > 0 && detail.getCountCaredays() < detail.getCountCases()) {
                    applyMessageValues(message, "Zeile " + line + ": Die Anzahl Pflegetage kann nicht kleiner als die Fallzahl sein.", 
                            "distributionModel:details");
                }
            }
            checkField(message, detail.getCountCases(), 1, 999999, "Zeile " + line + ": Bitte Fallzahl angeben"
                    , "distributionModel:details");
            checkField(message, detail.getCostVolume(), 1, 999999999, "Zeile " + line + ": Bitte Kostenvolumen angeben"
                    , "distributionModel:details");
            if (detail.isUseOtherCode()) {
                checkField(message, detail.getNoteOtherCode(), "Zeile " + line + ": Verteilung über sonstigen Schlüssel bitte erläutern"
                        , "distributionModel:details");
            }
            if (!detail.isUseProcCode() && !detail.isUseDiagCode() && !detail.isUseGroupResult() && !detail.isUseOtherCode()) {
                applyMessageValues(message, "Zeile " + line + ": Bitte mindestens einen Schlüssel zur Verteilung angeben."
                        , "distributionModel:details");
            }
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
        if (!modelIsComplete()) {
            return null;
        }
        _model.setStatus(WorkflowStatus.ApprovalRequested);
        setModifiedInfo();
        _model = _distModelFacade.saveDistributionModel(_model);
        return "";
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        _model.setAccountId(_sessionController.getAccountId());
        setModifiedInfo();
        _model = _distModelFacade.saveDistributionModel(_model);
        return "";
    }

    public void ikChanged() {
        // dummy listener, used by component MultiIk - do not delete
    }

    private List<SelectItem> _ikItems;

    public List<SelectItem> getIkItems() {
        return getIkItems(_model);
    }

    public List<SelectItem> getIkItems(DistributionModel model) {
        // todo: get correct IK list, depending on type
        if (_ikItems == null && model != null) {
            Account account = _sessionController.getAccount();
            CalcHospitalFunction calcFunct = model.getType() == 0 
                    ? CalcHospitalFunction.ClinicalDistributionModelDrg 
                    : CalcHospitalFunction.ClinicalDistributionModelPepp;
            boolean testMode = _appTools.isEnabled(ConfigKey.TestMode);
            Set<Integer> possibleIks = _distModelFacade.obtainIks4NewDistributionModel(calcFunct, account.getId()
                    , Utils.getTargetYear(Feature.CALCULATION_HOSPITAL), testMode);

            _ikItems = new ArrayList<>();
            if (model.getIk() > 0) {
                _ikItems.add(new SelectItem(model.getIk()));
            }
            for (int ik : _accessManager.ObtainIksForCreation(Feature.CALCULATION_HOSPITAL)) {
                if (possibleIks.contains(ik)) {
                    _ikItems.add(new SelectItem(ik));
                }
                
            }
        }
        return _ikItems;
    }

    public void addDetail() {
        DistributionModelDetail detail = new DistributionModelDetail(_model.getId());
        _model.getDetails().add(detail);
    }

    public void deleteDetail(DistributionModelDetail detail) {
        _model.getDetails().remove(detail);
    }

    // </editor-fold>
    private void removeEmptyCenters() {
        Iterator<DistributionModelDetail> iter = _model.getDetails().iterator();
        while (iter.hasNext()) {
            DistributionModelDetail detail = iter.next();
            if (detail.isEmpty()) {
                iter.remove();
            }
        }
    }

    private void addDetailIfMissing() {
        if (_model.getDetails().isEmpty()) {
            _model.getDetails().add(new DistributionModelDetail(_model.getId()));
        }
    }

}
