/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.AccessManager;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.autopsy.AutopsyItem;
import org.inek.dataportal.entities.calc.autopsy.AutopsyServiceText;
import org.inek.dataportal.entities.calc.autopsy.CalcBasicsAutopsy;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.calc.CalcAutopsyFacade;
import org.inek.dataportal.facades.calc.CalcFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.helper.TransferFileCreator;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.MessageContainer;
import org.inek.dataportal.mail.Mailer;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class EditCalcBasicsAutopsy extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger LOGGER = Logger.getLogger("EditCalcBasicsAutopsy");

    @Inject private AccessManager _accessManager;
    @Inject private SessionController _sessionController;
    @Inject private CalcFacade _calcFacade;
    @Inject private CalcAutopsyFacade _calcAutopsyFacade;
    @Inject private ApplicationTools _appTools;

    private CalcBasicsAutopsy _calcBasics;

    public CalcBasicsAutopsy getCalcBasics() {
        return _calcBasics;
    }

    public void setCalcBasics(CalcBasicsAutopsy calcBasics) {
        this._calcBasics = calcBasics;
    }

    private CalcBasicsAutopsy _priorCalcBasics;

    public CalcBasicsAutopsy getPriorCalcBasics() {
        return _priorCalcBasics;
    }

    public void setPriorCalcBasics(CalcBasicsAutopsy calcBasics) {
        this._priorCalcBasics = calcBasics;
    }
    // </editor-fold>

    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = "" + params.get("id");
        if ("new".equals(id)) {
            _calcBasics = newCalcBasics();
        } else if (Utils.isInteger(id)) {
            CalcBasicsAutopsy calcBasics = loadCalcBasics(id);
            if (calcBasics.getId() == -1) {
                _calcBasics = calcBasics;
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            _calcBasics = calcBasics;
            if (isRequestCorrectionEnabled()) {
                //todo  _priorCalcBasics = _calcFacade.findPriorCalcBasics(_calcBasics);
            }
        } else {
            Utils.navigate(Pages.Error.RedirectURL());
        }
    }

    private CalcBasicsAutopsy loadCalcBasics(String idObject) {
        int id = Integer.parseInt(idObject);
        CalcBasicsAutopsy calcBasics = _calcAutopsyFacade.findCalcBasicsAutopsy(id);
        if (hasSufficientRights(calcBasics)) {
            return calcBasics;
        }
        return new CalcBasicsAutopsy();
    }

    private boolean hasSufficientRights(CalcBasicsAutopsy model) {
        if (isInekViewable(model)) {
            return true;
        }
        return _accessManager.isAccessAllowed(Feature.CALCULATION_HOSPITAL, model.getStatus(), model.getAccountId());
    }

    private CalcBasicsAutopsy newCalcBasics() {
        CalcBasicsAutopsy calcBasics = new CalcBasicsAutopsy();
        Account account = _sessionController.getAccount();
        calcBasics.setAccountId(account.getId());
        calcBasics.setDataYear(Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        List<SelectItem> ikItems = getIks();
        if (ikItems.size() == 1) {
            calcBasics.setIk((int) ikItems.get(0).getValue());
        }
        initAutopsyItems(calcBasics);
        return calcBasics;
    }

    private void initAutopsyItems(CalcBasicsAutopsy calcBasics) {
        List<AutopsyServiceText> serviceTexts = _calcAutopsyFacade.findAllServiceTexts();
        for (AutopsyServiceText serviceText : serviceTexts) {
            calcBasics.addAutopsyItem(serviceText);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isOwnModel() {
        return _sessionController.isMyAccount(_calcBasics.getAccountId(), false);
    }

    public boolean isReadOnly() {
        return _accessManager.isReadOnly(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId())
                || _sessionController.isInekUser(Feature.CALCULATION_HOSPITAL) && !isOwnModel();
    }

    private boolean isInekEditable(CalcBasicsAutopsy calcBasics) {
        return _sessionController.isInekUser(Feature.CALCULATION_HOSPITAL, true) 
                && calcBasics != null 
                && (calcBasics.getStatus() == WorkflowStatus.Provided || calcBasics.getStatus() == WorkflowStatus.ReProvided);
    }

    private boolean isInekViewable(CalcBasicsAutopsy calcBasics) {
        return _sessionController.isInekUser(Feature.CALCULATION_HOSPITAL, true) 
                && calcBasics != null 
                && calcBasics.getStatusId() >= WorkflowStatus.Provided.getId();
    }

    @Override
    protected void addTopics() {
        addTopic("TopicFrontPage", Pages.CalcDrgBasics.URL());
    }

    private List<SelectItem> _ikItems;

    public List<SelectItem> getIks() {
        if (_ikItems == null) {
            boolean testMode = _appTools.isEnabled(ConfigKey.TestMode);
            int year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
            Set<Integer> iks = _calcAutopsyFacade.obtainIks4NewBasicsAutopsy(_sessionController.getAccountId(), year, testMode);
            if (_calcBasics != null && _calcBasics.getIk() > 0) {
                iks.add(_calcBasics.getIk());
            }

            _ikItems = new ArrayList<>();
            for (int ik : iks) {
                _ikItems.add(new SelectItem(ik));
            }
        }
        return _ikItems;
    }

    public void ikChanged() {
        // dummy listener, used by component MultiIk - do not delete
    }

    public String save() {
        setModifiedInfo();
        _calcBasics = _calcAutopsyFacade.saveCalcBasicsAutopsy(_calcBasics);

        if (isValidId(_calcBasics.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSaveAndMentionSend").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            if (_calcBasics.getStatus() == WorkflowStatus.Taken) {
                return Pages.CalculationHospitalSummary.URL();
            }
            return null;
        }
        return Pages.Error.URL();
    }

    private void setModifiedInfo() {
        _calcBasics.setLastChanged(Calendar.getInstance().getTime());
        _calcBasics.setAccountIdLastChange(_sessionController.getAccountId());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsObdSendEnabled)) {
            return false;
        }
        return _accessManager.isSealedEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsObdSendEnabled)) {
            return false;
        }
        return _accessManager.isApprovalRequestEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsObdSendEnabled)) {
            return false;
        }
        return (_calcBasics.getStatus() == WorkflowStatus.Provided || _calcBasics.getStatus() == WorkflowStatus.ReProvided)
                && _sessionController.isInekUser(Feature.CALCULATION_HOSPITAL, true)
                && _calcBasics != null;
    }

    public String requestCorrection() {
        if (!isRequestCorrectionEnabled()) {
            return "";
        }
        setModifiedInfo();
        // set as retired
        _calcBasics.setStatus(WorkflowStatus.Retired);
        _calcAutopsyFacade.saveCalcBasicsAutopsy(_calcBasics);

        // create copy to edit (persist detached object with default Ids)
        _calcBasics.setStatus(WorkflowStatus.New);
        _calcBasics.setId(-1);
        _calcAutopsyFacade.saveCalcBasicsAutopsy(_calcBasics);
        sendMessage("KVM Konkretisierung");

        return Pages.CalculationHospitalSummary.URL();
    }

    public boolean isSendApprovalEnabled() {
        return (_calcBasics.getStatus() == WorkflowStatus.Provided || _calcBasics.getStatus() == WorkflowStatus.ReProvided)
                && _sessionController.isInekUser(Feature.CALCULATION_HOSPITAL, true)
                && _calcBasics != null;
    }

    public String sendApproval() {
        if (!isInekEditable(_calcBasics)) {
            return "";
        }
        setModifiedInfo();
        _calcBasics.setStatus(WorkflowStatus.Taken);
        _calcAutopsyFacade.saveCalcBasicsAutopsy(_calcBasics);
        sendMessage("KVM Genehmigung");

        return Pages.CalculationHospitalSummary.URL();
    }

    @Inject private AccountFacade _accountFacade;
    @Inject private Mailer _mailer;

    private void sendMessage(String name) {
        Account receiver = _accountFacade.findAccount(_appTools.isEnabled(ConfigKey.TestMode) 
                ? _sessionController.getAccountId() 
                : _calcBasics.getAccountId());
        MailTemplate template = _mailer.getMailTemplate(name);
        String subject = template.getSubject()
                .replace("{ik}", "" + _calcBasics.getIk());
        String body = template.getBody()
                .replace("{formalSalutation}", _mailer.getFormalSalutation(receiver)); // todo.replace("{note}", _calcBasics.getNoteInek());
        String bcc = template.getBcc().replace("{accountMail}", _sessionController.getAccount().getEmail());
        _mailer.sendMailFrom(template.getFrom(), receiver.getEmail(), "", bcc, subject, body);
    }

    public boolean isTakeEnabled() {
        return false;
        // todo: do not allow consultant
        //return _accessManager.isTakeEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isCopyForResendAllowed() {
        if (_calcBasics.getStatusId() < 10 
                || _calcBasics.getStatusId() > 20 
                || !_appTools.isEnabled(ConfigKey.IsCalculationBasicsObdSendEnabled)) {
            return false;
        }
        if (_sessionController.isInekUser(Feature.CALCULATION_HOSPITAL) && !_appTools.isEnabled(ConfigKey.TestMode)) {
            return false;
        }
        return !_calcAutopsyFacade.existActiveCalcBasicsAutopsy(_calcBasics.getIk());
    }

    public void copyForResend() {
        _calcBasics.setStatus(WorkflowStatus.Retired);
        _calcAutopsyFacade.saveCalcBasicsAutopsy(_calcBasics);

        _calcAutopsyFacade.detach(_calcBasics);
        _calcBasics.setId(-1);
        _calcBasics.setStatus(WorkflowStatus.New);
        // do not set current account: _calcBasics.setAccountId(_sessionController.getAccountId()); 
        for (AutopsyItem item : _calcBasics.getAutopsyItems()) {
            item.setId(-1);
            item.setCalcBasicsAutopsyId(-1);
        }
        _calcBasics.setStatus(WorkflowStatus.CorrectionRequested);
        try {
            _calcBasics = _calcAutopsyFacade.saveCalcBasicsAutopsy(_calcBasics);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Exception during setDataToNew: {0}", ex.getMessage());
        }
    }

    /**
     * This function seals the calculation basics if possible. Sealing is possible, if all mandatory fields are
     * fulfilled. After sealing, the statement od participance can not be edited anymore and is available for the InEK.
     *
     * @return
     */
    public String seal() {
        if (!modelIsComplete()) {
            return "";
        }
        _calcBasics.setStatus(WorkflowStatus.Provided);
        setModifiedInfo();
        _calcBasics.setSealed(Calendar.getInstance().getTime());
        _calcBasics = _calcAutopsyFacade.saveCalcBasicsAutopsy(_calcBasics);

        TransferFileCreator.createCalcBasicsTransferFile(_sessionController, _calcBasics);
        
        if (isValidId(_calcBasics.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL"));
            Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_calcBasics));
            return Pages.PrintView.URL();
        }
        return "";
    }

    private boolean modelIsComplete() {
        MessageContainer message = composeMissingFieldsMessage(_calcBasics);
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

    public MessageContainer composeMissingFieldsMessage(CalcBasicsAutopsy model) {
        MessageContainer message = new MessageContainer();

        checkField(message, model.getIk(), 100000000, 999999999, "lblIK", "calcBasicsAutopsy:ikMulti");

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
        _calcBasics.setStatus(WorkflowStatus.ApprovalRequested);
        setModifiedInfo();
        _calcBasics = _calcAutopsyFacade.saveCalcBasicsAutopsy(_calcBasics);
        return "";
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        _calcBasics.setAccountId(_sessionController.getAccountId());
        setModifiedInfo();
        _calcBasics = _calcAutopsyFacade.saveCalcBasicsAutopsy(_calcBasics);
        return "";
    }

}
