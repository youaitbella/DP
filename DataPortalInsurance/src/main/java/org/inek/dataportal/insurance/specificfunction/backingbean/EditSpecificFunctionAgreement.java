/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.insurance.specificfunction.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.util.Pair;
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
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.adm.facade.InekRoleFacade;
import org.inek.dataportal.common.data.icmt.entities.Customer;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.insurance.specificfunction.entity.AgreedCenter;
import org.inek.dataportal.common.specificfunction.entity.CenterName;
import org.inek.dataportal.common.specificfunction.entity.RelatedName;
import org.inek.dataportal.common.specificfunction.entity.SpecificFunction;
import org.inek.dataportal.insurance.specificfunction.entity.SpecificFunctionAgreement;
import org.inek.dataportal.common.specificfunction.entity.TypeExtraCharge;
import org.inek.dataportal.insurance.specificfunction.facade.SpecificFunctionFacade;
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
public class EditSpecificFunctionAgreement extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger LOGGER = Logger.getLogger("EditSpecificFunctionAgreement");
    private static final long serialVersionUID = 1L;
    @Inject private AccessManager _accessManager;
    @Inject private SessionController _sessionController;
    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private ApplicationTools _appTools;
    @Inject private CustomerFacade _customerFacade;

    private SpecificFunctionAgreement _agreement;

    public SpecificFunctionAgreement getAgreement() {
        return _agreement;
    }

    public void setAgreement(SpecificFunctionAgreement agreement) {
        this._agreement = agreement;
    }
    // </editor-fold>

    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = "" + params.get("id");
        if ("new".equals(id)) {
            _agreement = newSpecificFunctionAgreement();
            addCentersIfMissing();
        } else if (Utils.isInteger(id)) {
            SpecificFunctionAgreement request = loadSpecificFunctionAgreement(id);
            if (request.getId() == -1) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            _agreement = request;
            addCentersIfMissing();
        } else {
            Utils.navigate(Pages.Error.RedirectURL());
        }
    }

    private SpecificFunctionAgreement loadSpecificFunctionAgreement(String idObject) {
        int id = Integer.parseInt(idObject);
        SpecificFunctionAgreement agreement = _specificFunctionFacade.findSpecificFunctionAgreement(id);
        if (hasSufficientRights(agreement)) {
            return agreement;
        }
        return new SpecificFunctionAgreement();
    }

    private boolean hasSufficientRights(SpecificFunctionAgreement calcBasics) {
        return _accessManager.isAccessAllowed(Feature.SPF_INSURANCE, calcBasics.getStatus(), calcBasics.getAccountId());
    }

    private SpecificFunctionAgreement newSpecificFunctionAgreement() {
        Account account = _sessionController.getAccount();
        SpecificFunctionAgreement agreement = new SpecificFunctionAgreement();
        agreement.setAccountId(account.getId());
        agreement.setGender(account.getGender());
        agreement.setTitle(account.getTitle());
        agreement.setFirstName(account.getFirstName());
        agreement.setLastName(account.getLastName());
        agreement.setPhone(account.getPhone());
        agreement.setMail(account.getEmail());
        agreement.setDataYear(Utils.getTargetYear(Feature.SPF_INSURANCE));
        agreement.addAgreedCenterRemunerationKey();
        List<SelectItem> iks = getIks();
        if (iks.size() == 1) {
            int ik = (int) iks.get(0).getValue();
            Customer customer = _customerFacade.getCustomerByIK(ik);
            if (customer.getCustomerTypeId() == 77) {  // todo: remove magic number
                agreement.setInsuranceIk(ik);
                agreement.setInsuranceName(customer.getName());
                agreement.setInsuranceStreet(customer.getStreet());
                agreement.setInsurancePostCode(customer.getPostCode());
                agreement.setInsuranceTown(customer.getTown());
            }
        }
        return agreement;
    }

    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isOwnAgreement() {
        return _sessionController.isMyAccount(_agreement.getAccountId(), false);
    }

    public boolean isReadOnly() {
        if (_agreement == null) {
            return true;
        }
        if (_sessionController.isInekUser(Feature.SPF_INSURANCE) && !_appTools.isEnabled(ConfigKey.TestMode)) {
            return true;
        }
        return _accessManager.
                isReadOnly(Feature.SPF_INSURANCE, _agreement.getStatus(), _agreement.getAccountId(), _agreement.getIk());
    }

    @Override
    protected void addTopics() {
        addTopic("TopicFrontPage", Pages.CalcDrgBasics.URL());
    }

    public String save() {
        removeEmptyCenters();
        setModifiedInfo();
        _agreement = _specificFunctionFacade.saveSpecificFunctionAgreement(_agreement);
        addCentersIfMissing();

        if (isValidId(_agreement.getId())) {
            _sessionController.alertClient(Utils.getMessage("msgSaveAndMentionSend"));
            return null;
        }
        return Pages.Error.URL();
    }

    public String saveAndMail() {
        removeEmptyCenters();
        setModifiedInfo();
        _agreement = _specificFunctionFacade.saveSpecificFunctionAgreement(_agreement);
        addCentersIfMissing();

        if (isValidId(_agreement.getId())) {
            _sessionController.alertClient(Utils.getMessage("msgSaveAndMentionSend"));
            sendMessage("Besondere Aufgaben / Zentrum: Vertragskennzeichen");
            return null;
        }
        return Pages.Error.URL();
    }

    @Inject private AccountFacade _accountFacade;
    @Inject private Mailer _mailer;

    private void sendMessage(String name) {
        //todo: refactor for gloabal usage (move to mailer?) and remove all similar methods
        Account receiver = _accountFacade.findAccount(
                _appTools.isEnabled(ConfigKey.TestMode)
                ? _sessionController.getAccountId()
                : _agreement.getAccountId());
        MailTemplate template = _mailer.getMailTemplate(name);
        String subject = template.getSubject()
                .replace("{ik}", "" + _agreement.getIk());
        String body = template.getBody()
                .replace("{formalSalutation}", _mailer.getFormalSalutation(receiver))
                .replace("{note}", _agreement.getNoteInek());
        String bcc = template.getBcc().replace("{accountMail}", _sessionController.getAccount().getEmail());
        _mailer.sendMailFrom(template.getFrom(), receiver.getEmail(), "", bcc, subject, body);
    }

    private void setModifiedInfo() {
        _agreement.setLastChanged(Calendar.getInstance().getTime());
        _agreement.setAccountIdLastChange(_sessionController.getAccountId());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsSpecificFunctionAgreementSendEnabled)) {
            return false;
        }
        if (_agreement == null) {
            return false;
        }
        return _accessManager.isSealedEnabled(Feature.SPF_INSURANCE, _agreement.getStatus(), _agreement.
                getAccountId());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsSpecificFunctionAgreementSendEnabled)) {
            return false;
        }
        if (_agreement == null) {
            return false;
        }
        return (_agreement.getStatus() == WorkflowStatus.Provided || _agreement.getStatus() == WorkflowStatus.ReProvided)
                && _sessionController.isInekUser(Feature.SPF_INSURANCE, true);
    }

    public String requestCorrection() {
        if (!isRequestCorrectionEnabled()) {
            return "";
        }
        removeEmptyCenters();
        setModifiedInfo();
        // set as retired
        _agreement.setStatus(WorkflowStatus.Retired);
        _specificFunctionFacade.saveSpecificFunctionAgreement(_agreement);

        // create copy to edit (persist detached object with default Ids)
        _agreement.setStatus(WorkflowStatus.New);
        _agreement.setId(-1);
        for (AgreedCenter agreedCenter : _agreement.getAgreedCenters()) {
            agreedCenter.setId(-1);
        }
        _specificFunctionFacade.saveSpecificFunctionAgreement(_agreement);
        sendMessage("BA Konkretisierung");

        return Pages.SpecificFunctionSummary.URL();
    }

    /**
     * This function seals a statement of participance if possible. Sealing is possible, if all mandatory fields are
     * fulfilled. After sealing, the statement of participance can not be edited anymore and is available for the InEK.
     *
     * @return
     */
    public String seal() {
        if (!requestIsComplete()) {
            return null;
        }
        removeEmptyCenters();
        _agreement.setStatus(WorkflowStatus.Provided);
        setModifiedInfo();
        _agreement.setSealed(Calendar.getInstance().getTime());
        _agreement = _specificFunctionFacade.saveSpecificFunctionAgreement(_agreement);

        if (isValidId(_agreement.getId())) {
            sendNotification();
            Utils.getFlash().put("headLine", Utils.getMessage("nameSPECIFIC_FUNCTION"));
            Utils.getFlash().put("targetPage", Pages.SpecificFunctionSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_agreement));
            return Pages.PrintView.URL();
        }
        return "";
    }

    @Inject private InekRoleFacade _inekRoleFacade;

    public void sendNotification() {
        List<Account> inekAccounts = _inekRoleFacade.findForFeature(Feature.SPECIFIC_FUNCTION); // not SPF_INSURANCE (same user)
        String receipients = inekAccounts.stream().map(a -> a.getEmail()).collect(Collectors.joining(";"));
        _mailer.sendMail(receipients, "Besondere Aufgaben / Zentrum", "Es wurde ein Datensatz an das InEK gesendet.");
    }

    private boolean requestIsComplete() {
        MessageContainer message = composeMissingFieldsMessage(_agreement);
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

    public MessageContainer composeMissingFieldsMessage(SpecificFunctionAgreement request) {
        MessageContainer message = new MessageContainer();

        String ik = request.getIk() <= 0 ? "" : "" + request.getIk();
        checkField(message, ik, "lblIK", "specificFuntion:ikMulti");
        checkField(message, request.getFirstName(), "lblFirstName", "specificFuntion:firstName");
        checkField(message, request.getLastName(), "lblFirstName", "specificFuntion:lastName");
        checkField(message, request.getPhone(), "lblPhone", "specificFuntion:phone");
        checkField(message, request.getMail(), "lblMail", "specificFuntion:mail");
        checkField(message, request.getTypeExtraCharge(), 0, 9, "lblTypeExtraCharge", "specificFuntion:typeExtraCharge");

// todo        
//        if (!request.isWillNegotiate() && !request.isHasAgreement()) {
//            applyMessageValues(message, "Bitte mindestens eine zu verhandelnde oder vorhandene Vereinbarung angeben", "");
//        }
        boolean hasCenters = false;
        for (AgreedCenter center : request.getAgreedCenters()) {
            if (center.isEmpty()) {
                continue;
            }
            if (center.getCenterName().getId() == -1) {
                checkField(message, center.getOtherCenterName(), "Bitte Art des Zentrums angeben", "");
            }
            if (center.getSpecificFunctions().isEmpty()) {
                applyMessageValues(message, "Bitte mindestens eine besondere Aufgabe auswählen oder angeben", "");
            }
            if (center.getSpecificFunctions().stream().anyMatch(f -> f.getId() == -1)) {
                checkField(message, center.getOtherSpecificFunction(), "Bitte sonstige besondere Aufgaben angeben", "");
            }
            hasCenters = true;
        }
// todo        
//        if (request.isWillNegotiate() && !hasCenters) {
//            applyMessageValues(message, "Bitte mindestens eine Vereinbarung angeben", "");
//        }

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

    public void ikChanged() {
        // dummy listener, used by component MultiIk - do not delete
    }

    public List<SelectItem> getIks() {
        Account account = _sessionController.getAccount();
        Set<Integer> iks = account.getFullIkSet();
        if (_agreement != null && _agreement.getIk() > 0) {
            iks.add(_agreement.getIk());
        }
        List<SelectItem> items = new ArrayList<>();
        for (int ik : iks) {
            items.add(new SelectItem(ik));
        }
        return items;
    }

    public void addAgreedCenter() {
        _agreement.addAgreedCenter();
    }

    public void deleteAgreedCenter(AgreedCenter center) {
        _agreement.deleteAgreedCenter(center);
    }
    // </editor-fold>

    private void removeEmptyCenters() {
        removeEmptyAgreedCenters();
    }

    private void removeEmptyAgreedCenters() {
        _agreement.removeEmptyAgreedCenters();
    }

    private void addCentersIfMissing() {
        if (_agreement.getAgreedCenters().isEmpty()) {
            addAgreedCenter();
        }
    }

    public List<CenterName> getCenterNames(int id) {
        return _specificFunctionFacade.getCenterNames(id == 0);
    }

    public List<RelatedName> getRelatedNames() {
        return _specificFunctionFacade.getRelatedNames();
    }

    public List<TypeExtraCharge> getTypeExtraCharges() {
        return _specificFunctionFacade.getTypeChargeExtra();
    }

    public List<SpecificFunction> getSpecificFunctions() {
        return _specificFunctionFacade.getSpecificFunctionsForInsurance();
    }

    public void changeCode() {
        Pair<Integer, Integer> ikYear = _specificFunctionFacade.findIkAndYearOfSpecificFunctionRequestByCode(_agreement.getCode());
        int ik  = ikYear.getKey();
        if (ik < 1) {
            Utils.showMessageInBrowser("Das Vertragskennzeichen " + _agreement.getCode() + " ist unbekannt.");
            return;
        }
        if (_specificFunctionFacade.SpecificFunctionAgreementExists(_agreement.getCode())) {
            Utils.
                    showMessageInBrowser("Zum Vertragskennzeichen " + _agreement.getCode() + " wurden bereits Daten erfasst.");
            return;
        }
        _agreement.setIk(ik);
        _agreement.setDataYear(ikYear.getValue());
    }

    public List<SelectItem> getSpecificFunctionRemunerationScopes() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(-1, ""));
        items.add(new SelectItem(0, "alle"));
        for (AgreedCenter center : _agreement.getAgreedCenters()) {
            items.add(new SelectItem(center.getSequence(), "laufende Nummer: " + center.getSequence()));
        }
        return items;
    }
}
