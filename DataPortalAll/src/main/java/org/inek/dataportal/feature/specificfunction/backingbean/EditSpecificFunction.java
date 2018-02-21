/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.specificfunction.backingbean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.feature.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.feature.admin.facade.InekRoleFacade;
import org.inek.dataportal.feature.specificfunction.entity.CenterName;
import org.inek.dataportal.feature.specificfunction.entity.RequestAgreedCenter;
import org.inek.dataportal.feature.specificfunction.entity.RequestProjectedCenter;
import org.inek.dataportal.feature.specificfunction.entity.SpecificFunction;
import org.inek.dataportal.feature.specificfunction.entity.SpecificFunctionRequest;
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
public class EditSpecificFunction extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger LOGGER = Logger.getLogger("EditSpecificFunctionRequest");

    @Inject private AccessManager _accessManager;
    @Inject private SessionController _sessionController;
    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private ApplicationTools _appTools;

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
        if ("new".equals(id)) {
            _request = newSpecificFunctionRequest();
            addCentersIfMissing();
        } else {
            _request = loadSpecificFunctionRequest(id);
            if (_request.getId() == -1) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            addCentersIfMissing();
        }
    }

    private SpecificFunctionRequest loadSpecificFunctionRequest(String idObject) {
        try {
            int id = Integer.parseInt(idObject);
            SpecificFunctionRequest request = _specificFunctionFacade.findSpecificFunctionRequest(id);
            if (request != null && hasSufficientRights(request)) {
                return request;
            }
        } catch (NumberFormatException ex) {
            return newSpecificFunctionRequest();
        }
        return new SpecificFunctionRequest();
    }

    private boolean hasSufficientRights(SpecificFunctionRequest calcBasics) {
        return _accessManager.isAccessAllowed(Feature.SPECIFIC_FUNCTION, calcBasics.getStatus(), 
                calcBasics.getAccountId(), calcBasics.getIk());
    }

    private SpecificFunctionRequest newSpecificFunctionRequest() {
        Account account = _sessionController.getAccount();
        SpecificFunctionRequest request = new SpecificFunctionRequest();
        request.setAccountId(account.getId());
        request.setGender(account.getGender());
        request.setTitle(account.getTitle());
        request.setFirstName(account.getFirstName());
        request.setLastName(account.getLastName());
        request.setPhone(account.getPhone());
        request.setMail(account.getEmail());
        //request.setDataYear(Utils.getTargetYear(Feature.SPECIFIC_FUNCTION));
        List<SelectItem> iks = getIks();
        if (iks.size() == 1) {
            request.setIk((int) iks.get(0).getValue());
        }
        return request;
    }

    public List<SelectItem> getTypeItems() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(1, "im Krankenhausplan des Landes"));
        items.add(new SelectItem(2, "durch gleichartige Festlegung durch zuständige Landesbehörde"));
        return items;
    }

    private List<Integer> _availableYears;

    public List<Integer> getYears() {
        if (_request == null) {
            return new ArrayList<>();
        }
        if (_availableYears == null) {
            obtainAvailableYears();
        }
        return _availableYears;
    }

    private void obtainAvailableYears() {
        List<Integer> existingYears = _specificFunctionFacade.getExistingYears(_request.getIk());
        
        _availableYears = new ArrayList<>();
        IntStream.rangeClosed(2015, 2019)
                .filter(y -> y == _request.getDataYear() || !existingYears.contains(y))
                .forEach(y -> _availableYears.add(y));
    }

    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isOwnStatement() {
        return _sessionController.isMyAccount(_request.getAccountId(), false);
    }

    public boolean isReadOnly() {
        if (_sessionController.isInekUser(Feature.CALCULATION_HOSPITAL) && !_appTools.isEnabled(ConfigKey.TestMode)) {
            return true;
        }
        return _accessManager.
                isReadOnly(Feature.SPECIFIC_FUNCTION, _request.getStatus(), _request.getAccountId(), _request.getIk());
    }

    @Override
    protected void addTopics() {
        addTopic("TopicFrontPage", Pages.CalcDrgBasics.URL());
    }

    public String save() {
        removeEmptyCenters();
        setModifiedInfo();
        _request = _specificFunctionFacade.saveSpecificFunctionRequest(_request);
        addCentersIfMissing();

        if (isValidId(_request.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSaveAndMentionSend").replace("\r\n", "\n").
                    replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    public String saveAndMail() {
        removeEmptyCenters();
        setModifiedInfo();
        _request = _specificFunctionFacade.saveSpecificFunctionRequest(_request);
        addCentersIfMissing();

        if (isValidId(_request.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSaveAndMentionSend").replace("\r\n", "\n").
                    replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            sendMessage("Besondere Aufgaben / Zentrum: Vertragskennzeichen");
            return null;
        }
        return Pages.Error.URL();
    }

    @Inject private AccountFacade _accountFacade;
    @Inject private Mailer _mailer;

    private void sendMessage(String name) {
        //todo: refactor for gloabal usage (move to mailer?) and remove all similar methods
        Account receiver = _accountFacade.findAccount(_appTools.isEnabled(ConfigKey.TestMode)
                ? _sessionController.getAccountId()
                : _request.getAccountId());
        MailTemplate template = _mailer.getMailTemplate(name);
        String subject = template.getSubject()
                .replace("{ik}", "" + _request.getIk());
        String body = template.getBody()
                .replace("{formalSalutation}", _mailer.getFormalSalutation(receiver))
                .replace("{note}", _request.getNoteInek());
        String bcc = template.getBcc().replace("{accountMail}", _sessionController.getAccount().getEmail());
        _mailer.sendMailFrom(template.getFrom(), receiver.getEmail(), "", bcc, subject, body);
    }

    private void setModifiedInfo() {
        _request.setLastChanged(Calendar.getInstance().getTime());
        _request.setAccountIdLastChange(_sessionController.getAccountId());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsSpecificFunctionRequestSendEnabled)) {
            return false;
        }
        return _accessManager.isSealedEnabled(Feature.SPECIFIC_FUNCTION, _request.getStatus(), 
                _request.getAccountId(), _request.getIk());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsSpecificFunctionRequestSendEnabled)) {
            return false;
        }
        return _accessManager.isApprovalRequestEnabled(Feature.SPECIFIC_FUNCTION, _request.getStatus(), 
                _request.getAccountId(), _request.getIk());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsSpecificFunctionRequestSendEnabled)) {
            return false;
        }
        return (_request.getStatus() == WorkflowStatus.Provided || _request.getStatus() == WorkflowStatus.ReProvided)
                && _sessionController.isInekUser(Feature.SPECIFIC_FUNCTION, true);
    }

    public String requestCorrection() {
        if (!isRequestCorrectionEnabled()) {
            return "";
        }
        removeEmptyCenters();
        setModifiedInfo();
        // set as retired
        _request.setStatus(WorkflowStatus.Retired);
        _specificFunctionFacade.saveSpecificFunctionRequest(_request);

        // create copy to edit (persist detached object with default Ids)
        _request.setStatus(WorkflowStatus.CorrectionRequested);
        _request.setId(-1);
        for (RequestProjectedCenter requestProjectedCenter : _request.getRequestProjectedCenters()) {
            requestProjectedCenter.setId(-1);
            requestProjectedCenter.setRequestMasterId(-1);
        }
        for (RequestAgreedCenter requestAgreedCenter : _request.getRequestAgreedCenters()) {
            requestAgreedCenter.setId(-1);
            requestAgreedCenter.setRequestMasterId(-1);
        }
        _specificFunctionFacade.saveSpecificFunctionRequest(_request);
        sendMessage("BA Konkretisierung");

        return Pages.SpecificFunctionSummary.URL();
    }

    public boolean isTakeEnabled() {
        return _accessManager.isTakeEnabled(Feature.SPECIFIC_FUNCTION, _request.getStatus(), 
                _request.getAccountId(), _request.getIk());
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
        removeEmptyCenters();
        _request.setStatus(WorkflowStatus.Provided);
        setModifiedInfo();
        if (_request.getSealed().equals(Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().
                toInstant(ZoneOffset.UTC)))) {
            // seat seal date for the first time sealing only
            _request.setSealed(Calendar.getInstance().getTime());
        }
        _request = _specificFunctionFacade.saveSpecificFunctionRequest(_request);

        if (isValidId(_request.getId())) {
            sendNotification();
            Utils.getFlash().put("headLine", Utils.getMessage("nameSPECIFIC_FUNCTION"));
            Utils.getFlash().put("targetPage", Pages.SpecificFunctionSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_request));
            return Pages.PrintView.URL();
        }
        return "";
    }

    @Inject private InekRoleFacade _inekRoleFacade;

    public void sendNotification() {
        List<Account> inekAccounts = _inekRoleFacade.findForFeature(Feature.SPECIFIC_FUNCTION);
        String receipients = inekAccounts.stream().map(a -> a.getEmail()).collect(Collectors.joining(";"));
        _mailer.sendMail(receipients, "Besondere Aufgaben / Zentrum, IK: " + _request.getIk(), "Es wurde ein Datensatz an das InEK gesendet.");
    }

    private boolean requestIsComplete() {
        MessageContainer message = composeMissingFieldsMessage(_request);
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

    @SuppressWarnings("CyclomaticComplexity")
    public MessageContainer composeMissingFieldsMessage(SpecificFunctionRequest request) {
        MessageContainer message = new MessageContainer();

        String ik = request.getIk() <= 0 ? "" : "" + request.getIk();
        checkField(message, ik, "lblIK", "specificFuntion:ikMulti");
        checkField(message, request.getFirstName(), "lblFirstName", "specificFuntion:firstName");
        checkField(message, request.getLastName(), "lblFirstName", "specificFuntion:lastName");
        checkField(message, request.getPhone(), "lblPhone", "specificFuntion:phone");
        checkField(message, request.getMail(), "lblMail", "specificFuntion:mail");

        if (!request.isWillNegotiate() && !request.isHasAgreement()) {
            applyMessageValues(message, "Bitte mindestens eine zu verhandelnde oder vorhandene Vereinbarung angeben", "");
        }
        boolean hasCenters = false;
        for (RequestProjectedCenter center : request.getRequestProjectedCenters()) {
            if (center.isEmpty()) {
                continue;
            }
            if (center.getCenterId() == -1) {
                checkField(message, center.getOtherCenterName(), "Bitte Art des Zentrums angeben", "");
            }
            if (center.getSpecificFunctions().isEmpty()) {
                applyMessageValues(message, "Bitte mindestens eine besondere Aufgabe auswählen oder angeben", "");
            }
            if (center.getSpecificFunctions().stream().anyMatch(f -> f.getId() == -1)) {
                checkField(message, center.getOtherSpecificFunction(), "Bitte sonstige besondere Aufgaben angeben", "");
            }
            checkField(message, center.getTypeId(), 1, 2, "Bitte Ausweisung und Festsetzung angeben", "");
            checkField(message, center.getEstimatedPatientCount(), 1, 99999999, "Anzahl der Patienten muss größer 0 sein.", "");
            hasCenters = true;
        }
        if (request.isWillNegotiate() && !hasCenters) {
            applyMessageValues(message, "Bitte mindestens eine Vereinbarung angeben", "");
        }

        hasCenters = false;
        for (RequestAgreedCenter center : request.getRequestAgreedCenters()) {
            if (center.isEmpty()) {
                continue;
            }
            checkField(message, center.getCenter(), "Bitte Art des Zentrums angeben", "");
            checkField(message, center.getRemunerationKey(), "Bitte Entgeltschlüssel angeben", "");
            if (center.getPercent() <= 0.0) {
                checkField(message, center.getAmount(), 1, 99999999, "Bitte Betrag oder Prozentsatz angeben", "");
            }
            if (center.getAmount() > 0 && center.getPercent() > 0.0) {
                applyMessageValues(message, "Sie können entweder einen Betrag oder einen Prozentsatz angeben, nicht aber beides.", "");
            }
            hasCenters = true;
        }
        if (request.isHasAgreement() && !hasCenters) {
            applyMessageValues(message, "Sie haben 'vorliegende Vereinbarung' markiert, jedoch keine Vereinbarung angegeben.", "");
        }

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
        _availableYears = null;
    }

    public List<SelectItem> getIks() {
        Account account = _sessionController.getAccount();
        Set<Integer> iks = account.getFullIkSet();
        if (_request != null && _request.getIk() > 0) {
            iks.add(_request.getIk());
        }
        List<SelectItem> items = new ArrayList<>();
        for (int ik : iks) {
            items.add(new SelectItem(ik));
        }
        return items;
    }

    public void addProjectedCenter() {
        RequestProjectedCenter center = new RequestProjectedCenter(_request.getId());
        _request.getRequestProjectedCenters().add(center);
    }

    public void deleteProjectedCenter(RequestProjectedCenter center) {
        _request.getRequestProjectedCenters().remove(center);
    }

    public void addAgreedCenter() {
        RequestAgreedCenter center = new RequestAgreedCenter(_request.getId());
        _request.getRequestAgreedCenters().add(center);
    }

    public void deleteAgreedCenter(RequestAgreedCenter center) {
        _request.getRequestAgreedCenters().remove(center);
    }
    // </editor-fold>

    private void removeEmptyCenters() {
        removeEmptyProjectedCenters();
        removeEmptyAgreedCenters();
    }

    private void removeEmptyProjectedCenters() {
        _request.getRequestProjectedCenters().removeIf(c -> c.isEmpty());
        for (RequestProjectedCenter center : _request.getRequestProjectedCenters()) {
            if (!center.getSpecificFunctions().stream().anyMatch(f -> f.getId() == -1)) {
                center.setOtherSpecificFunction("");
            }
        }
    }

    private void removeEmptyAgreedCenters() {
        _request.getRequestAgreedCenters().removeIf(c -> c.isEmpty());
    }

    private void addCentersIfMissing() {
        if (_request.getRequestProjectedCenters().isEmpty()) {
            addProjectedCenter();
        }
        if (_request.getRequestAgreedCenters().isEmpty()) {
            addAgreedCenter();
        }
    }

    public List<CenterName> getCenterNames() {
        return _specificFunctionFacade.getCenterNames();
    }

    public List<SpecificFunction> getSpecificFunctions() {
        return _specificFunctionFacade.getSpecificFunctions(true);
    }

}
