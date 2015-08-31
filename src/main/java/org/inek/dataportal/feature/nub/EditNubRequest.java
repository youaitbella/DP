/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.nub;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.Customer;
import org.inek.dataportal.entities.Document;
import org.inek.dataportal.entities.NubRequest;
import org.inek.dataportal.entities.NubRequestDocument;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountAdditionalIK;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.enums.CodeType;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.GlobalVars;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.facades.NubRequestFacade;
import org.inek.dataportal.facades.common.ProcedureFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.cooperation.PortalMessageFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.ObjectUtils;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.faceletvalidators.IkValidator;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.mail.Mailer;
import org.inek.dataportal.services.MessageService;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped
public class EditNubRequest extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("EditNubRequest");

    @Inject private CooperationTools _cooperationTools;
    @Inject private ProcedureFacade _procedureFacade;
    @Inject private SessionController _sessionController;
    @Inject private NubRequestFacade _nubRequestFacade;
    @Inject private CustomerFacade _customerFacade;
    @Inject private NubSessionTools _nubSessionTools;
    private NubRequest _nubRequest;
    private CooperativeRight _cooperativeRight;
    private CooperativeRight _supervisorRight;

    @Override
    protected void addTopics() {
        addTopic(NubRequestTabs.tabNubAddress.name(), Pages.NubEditAddress.URL());
        addTopic(NubRequestTabs.tabNubPage1.name(), Pages.NubEditPage1.URL());
        addTopic(NubRequestTabs.tabNubPage2.name(), Pages.NubEditPage2.URL());
        addTopic(NubRequestTabs.tabNubPage3.name(), Pages.NubEditPage3.URL());
        addTopic(NubRequestTabs.tabNubPage4.name(), Pages.NubEditPage4.URL());
        addTopic(NubRequestTabs.tabNubPageDocuments.name(), Pages.NubEditPageDocuments.URL());
    }

    enum NubRequestTabs {

        tabNubAddress,
        tabNubPage1,
        tabNubPage2,
        tabNubPage3,
        tabNubPage4,
        tabNubPageDocuments;
    }
    // <editor-fold defaultstate="collapsed" desc="fields">
    private SelectItem[] _multiIks = new SelectItem[0];
    private String _singleKhName;
    // </editor-fold>

    public EditNubRequest() {
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public NubRequest getNubRequest() {
        return _nubRequest;
    }

    public String getPatientsText() {
        return String.format(Utils.getMessage("lblNubPatients"), _nubRequest.getTargetYear());
    }

    public String getPatientsPastText() {
        return String.format(Utils.getMessage("lblNubPatientsPast"), _nubRequest.getTargetYear() - 2, _nubRequest.getTargetYear() - 1);
    }

    public void changedIk() {
        if (_nubRequest != null) {
            Customer c = _customerFacade.getCustomerByIK(_nubRequest.getIk());
            if (c.getName() == null || c.getName().equals("")) {
                if (_nubRequest.getIkName() == null || c.getName() == null) {
                    _nubRequest.setIkName("");
                }
                if (_nubRequest.getIkName().trim().equals("")) {
                    _nubRequest.setIkName(_sessionController.getAccount().getCompany());
                }
            } else {
                _nubRequest.setIkName(c.getName());
                if (_sessionController.getAccount().getIK() != null && _nubRequest.getIk().intValue() == _sessionController.getAccount().getIK()) {
                    _nubRequest.setIkName(_sessionController.getAccount().getCompany());
                }
            }
        }
    }

    // </editor-fold>
    @PostConstruct
    private void init() {

        Object ppId = Utils.getFlash().get("nubId");
        if (ppId == null) {
            _nubRequest = newNubRequest();
            _nubRequest.setCreatedBy(_sessionController.getAccountId());
            ensureCooperativeRight(_nubRequest);
            ensureSupervisorRight(_nubRequest);
        } else {
            _nubRequest = loadNubRequest(ppId);
        }
        initMenuMultiIK();
    }

    private void initMenuMultiIK() {
        Account acc = _sessionController.getAccount();
        List<AccountAdditionalIK> addIks = _sessionController.getAccount().getAdditionalIKs();
        ArrayList<SelectItem> items = new ArrayList<>();
        for (AccountAdditionalIK addIk : addIks) {
            items.add(new SelectItem(addIk.getIK()));
        }

        if (acc.getIK() != null) {
            items.add(0, new SelectItem(_sessionController.getAccount().getIK()));
        }

        if (_nubRequest.getIk() == null || !acc.getFullIkList().contains(_nubRequest.getIk().intValue())) {
            items.add(0, new SelectItem(""));
        }
        _multiIks = items.toArray(_multiIks);
    }

    private NubRequest loadNubRequest(Object ppId) {
        try {
            int id = Integer.parseInt("" + ppId);
            NubRequest nubRequest = _nubRequestFacade.findFresh(id);

            if (hasSufficientRights(nubRequest)) {
                return nubRequest;
            }
        } catch (NumberFormatException ex) {
            _logger.info(ex.getMessage());
        }
        return newNubRequest();
    }

    private boolean hasSufficientRights(NubRequest nubRequest) {
        if (isOwnNub(nubRequest)) {
            return true;
        }
        ensureCooperativeRight(nubRequest);
        ensureSupervisorRight(nubRequest);
        return !_cooperativeRight.equals(CooperativeRight.None) || !_supervisorRight.equals(CooperativeRight.None);
    }

    private boolean isOwnNub(NubRequest nubRequest) {
        return _sessionController.isMyAccount(nubRequest.getAccountId(), false);
    }

    private void ensureCooperativeRight(NubRequest nubRequest) {
        if (_cooperativeRight == null) {
            _cooperativeRight = _nubSessionTools.getCooperativeRight(nubRequest);
        }
    }

    private void ensureSupervisorRight(NubRequest nubRequest) {
        if (_supervisorRight == null) {
            _supervisorRight = _nubSessionTools.getSupervisorRight(nubRequest);
        }
    }

    private NubRequest newNubRequest() {
        NubRequest proposal = getNubController().createNubRequest();
        return proposal;
    }

    public String fromTemplate() {
        return null;
    }

    private NubController getNubController() {
        return (NubController) _sessionController.getFeatureController(Feature.NUB);
    }

    public SelectItem[] getGenderItems() {
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem("0", "");
        items[1] = new SelectItem("1", Utils.getMessage("salutationFemale"));
        items[2] = new SelectItem("2", Utils.getMessage("salutationMale"));
        return items;
    }

    public SelectItem[] getMultiIks() {
        return _multiIks;
    }

    public void setMultiIks(SelectItem[] multiIks) {
        _multiIks = multiIks;
    }

    public void checkPostalCode(FacesContext context, UIComponent component, Object value) {
        try {
            Integer tmp = Integer.parseInt((String) value);
            if (tmp > 99999) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            String msg = Utils.getMessage("errPostalCode");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public boolean checkPostalCode() {
        try {
            Integer tmp = Integer.parseInt((String) _nubRequest.getPostalCode());
            if (tmp > 99999) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            String msg = Utils.getMessage("errPostalCode");
            return false;
        }
        return true;
    }

    public boolean isExternalStateVisible() {
        return !_nubRequest.getExternalState().isEmpty();
    }

    // <editor-fold defaultstate="collapsed" desc="Codes">
    public String searchProc() {
        return searchCode(CodeType.Proc);
    }

    public String searchDrg() {
        return searchCode(CodeType.Drg);
    }

    public String searchCode(CodeType codeType) {
        _sessionController.getSearchController().bindSearchConsumer(this)
                .bindTargetPage(Pages.NubEditPage1.URL())
                .enableCodeType(CodeType.Proc).enableCodeType(CodeType.Drg)
                .bindCodeType(codeType);
        return Pages.SearchCode.URL();
    }

    @Override
    public void addProcedure(String code) {
        String procCodes = getNubRequest().getProcs();
        procCodes = (procCodes == null || procCodes.length() == 0) ? code : procCodes + "\r\n" + code;
        getNubRequest().setProcs(procCodes);
    }

    @Override
    public void addDrg(String code) {
        String drgCodes = getNubRequest().getDrgs();
        drgCodes = (drgCodes == null || drgCodes.length() == 0) ? code : drgCodes + "\r\n" + code;
        getNubRequest().setDrgs(drgCodes);
    }

    public void checkProcedureCodes(FacesContext context, UIComponent component, Object value) {
        String codes[] = value.toString().split("\\s");
        StringBuilder invalidCodes = new StringBuilder();
        for (String code : codes) {
            if (_procedureFacade.findProcedure(code, Utils.getTargetYear(Feature.NUB) - 1, Utils.getTargetYear(Feature.NUB)).equals("")) {
                if (invalidCodes.length() > 0) {
                    invalidCodes.append(", ");
                }
                invalidCodes.append(code);
            }
        }
        if (invalidCodes.length() > 0) {
            if (invalidCodes.indexOf(",") > 0) {
                invalidCodes.insert(0, "Unbekannte Codes: ");
            } else {
                invalidCodes.insert(0, "Unbekannter Code: ");
            }
            FacesMessage msg = new FacesMessage(invalidCodes.toString());
            throw new ValidatorException(msg);
        }
    }

    public void checkProxyIKs(FacesContext context, UIComponent component, Object value) {
        String msg = checkProxyIKs(value.toString());
        if (!msg.isEmpty()) {
            FacesMessage fmsg = new FacesMessage(msg);
            throw new ValidatorException(fmsg);

        }
    }

    public String checkProxyIKs(String value) {
        String iks[] = value.split("\\s|,|\r|\n");
        StringBuilder invalidIKs = new StringBuilder();
        for (String ik : iks) {
            if (ik.isEmpty()) {
                continue;
            }
            if (!_customerFacade.isValidIK(ik)) {
                if (invalidIKs.length() > 0) {
                    invalidIKs.append(", ");
                }
                invalidIKs.append(ik);
            }
        }
        if (invalidIKs.length() > 0) {
            if (invalidIKs.indexOf(",") < 0) {
                invalidIKs.insert(0, "Ungültige IK: ");
            } else {
                invalidIKs.insert(0, "Ungültige IKs: ");
            }
        }
        return invalidIKs.toString();
    }

    public void formatProxyIks() {
        String iks[] = _nubRequest.getProxyIKs().split("\\s|,|\r|\n");
        String formatted = "";
        for (String ik : iks) {
            if (ik.isEmpty()) {
                continue;
            }
            if (formatted.length() > 0) {
                formatted += ", ";
            }
            formatted += ik;
        }
        _nubRequest.setProxyIKs(formatted);
    }

    // </editor-fold>
    /**
     * Changes the owner of the request Becomes effective only after storing
     *
     * @return
     */
    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        if (isReadOnly()) {
            // paranoid reload
            // If, and only if the developer forgot about setting all fields to readonly,
            // then the user might be able to change that field before setting the owener
            // A paranoid reload forces the data into the original state.
            _nubRequest = _nubRequestFacade.find(_nubRequest.getId());
        }
        _nubRequest.setAccountId(_sessionController.getAccountId());
        _nubRequest = _nubRequestFacade.saveNubRequest(_nubRequest);
        _nubSessionTools.refreshNodes();
        return "";
    }

    public String reloadMaster() {
        getNubController().populateMasterData(_nubRequest, _sessionController.getAccount());
        return "";
    }

    public String save() {
        setModifiedInfo();
        formatProxyIks();
        _nubRequest = _nubRequestFacade.saveNubRequest(_nubRequest);

        if (isValidId(_nubRequest.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            _nubSessionTools.refreshNodes();
            return "";
        }
        return Pages.Error.URL();
    }

    private void setModifiedInfo() {
        _nubRequest.setLastChangedBy(_sessionController.getAccountId());
        _nubRequest.setLastModified(Calendar.getInstance().getTime());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isOwnNub() {
        return isOwnNub(_nubRequest);
    }

    public boolean isReadOnly() {
        return _cooperationTools.isReadOnly(Feature.NUB, _nubRequest.getStatus(), _nubRequest.getAccountId(), _nubRequest.getIk());
    }

    public boolean isRejectedNub() {
        return WorkflowStatus.Rejected.getValue() == _nubRequest.getStatus().getValue();
    }

    public boolean isSealEnabled() {
        if (!_sessionController.isEnabled(ConfigKey.IsNubSendEnabled)) {
            return false;
        }
        return _cooperationTools.isSealedEnabled(Feature.NUB, _nubRequest.getStatus(), _nubRequest.getAccountId(), _nubRequest.getIk());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_sessionController.isEnabled(ConfigKey.IsNubSendEnabled)) {
            return false;
        }
        return _cooperationTools.isApprovalRequestEnabled(Feature.NUB, _nubRequest.getStatus(), _nubRequest.getAccountId(), _nubRequest.getIk());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_sessionController.isEnabled(ConfigKey.IsNubSendEnabled)) {
            return false;
        }
        return _cooperationTools.isRequestCorrectionEnabled(Feature.NUB, _nubRequest.getStatus(), _nubRequest.getAccountId(), _nubRequest.getIk());
    }

    public boolean isTakeEnabled() {
        return _cooperationTools.isTakeEnabled(Feature.NUB, _nubRequest.getStatus(), _nubRequest.getAccountId(), _nubRequest.getIk());
    }

    /**
     * This function seals a request. As a precaution, it performs some checks
     * like completeness
     *
     * @return targetPage
     */
    public String sealNubRequest() {
        if (!requestIsComplete()) {
            return getActiveTopic().getOutcome();
        }
        if (_nubRequest.getStatus().compareTo(WorkflowStatus.Provided) >= 0) {
            return Pages.Error.URL();
        }

        _nubRequest.setStatus(WorkflowStatus.Accepted);
        _nubRequest.setSealedBy(_sessionController.getAccountId());
        _nubRequest.setDateSealed(Calendar.getInstance().getTime());

        int targetYear = 1 + Calendar.getInstance().get(Calendar.YEAR);
        if (_nubRequest.getTargetYear() < targetYear) {
            // data from last year, not sealed so far
            // we need a new id, thus delete old and create new nub request
            NubRequest copy = ObjectUtils.copy(_nubRequest);
            copy.setId(-1);
            copy.setTargetYear(targetYear);
            _nubRequestFacade.remove(_nubRequest);
            _nubRequest = _nubRequestFacade.saveNubRequest(copy);
        } else {
            _nubRequest = _nubRequestFacade.saveNubRequest(_nubRequest);
        }

        if (isValidId(_nubRequest.getId())) {
            _nubSessionTools.refreshNodes();
            sendNubConfirmationMail();

            Utils.getFlash().put("headLine", Utils.getMessage("nameNUB") + " " + _nubRequest.getExternalId());
            Utils.getFlash().put("targetPage", Pages.NubSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_nubRequest));
            return Pages.PrintView.URL();
        }
        return "";
    }

    @Inject Mailer _mailer;

    public boolean sendNubConfirmationMail() {
        Account current = _sessionController.getAccount();
        Account owner = _accountFacade.find(_nubRequest.getAccountId());
        if (!current.isNubConfirmation() && !owner.isNubConfirmation()) {
            return true;
        }
        if (!current.isNubConfirmation()) {
            current = owner;
        }
        if (!owner.isNubConfirmation()) {
            owner = current;
        }

        MailTemplate template = _mailer.getMailTemplate("NUB confirmation");
        if (template == null) {
            return false;
        }

        String proxy = _nubRequest.getProxyIKs().trim();
        if (!proxy.isEmpty()) {
            proxy = "\r\nSie haben diese Anfrage auch stellvertretend für die folgenden IKs gestellt:\r\n" + proxy + "\r\n";
        }

        String salutation = _mailer.getFormalSalutation(current);
        String body = template.getBody()
                .replace("{formalSalutation}", salutation)
                .replace("{id}", "N" + _nubRequest.getId())
                .replace("{name}", _nubRequest.getName())
                .replace("{ik}", "" + _nubRequest.getIk())
                .replace("{proxyIks}", proxy)
                .replace("{targetYear}", "" + _nubRequest.getTargetYear());

        String subject = template.getSubject()
                .replace("{id}", "N" + _nubRequest.getId())
                .replace("{ik}", "" + _nubRequest.getIk());

        return _mailer.sendMailFrom("NUB Datenannahme <nub@inek-drg.de>", current.getEmail(), owner.getEmail(), template.getBcc(), subject, body);
    }

    public String requestApproval() {
        if (!requestIsComplete()) {
            return getActiveTopic().getOutcome();
        }
        if (_nubRequest.getStatus().compareTo(WorkflowStatus.ApprovalRequested) >= 0) {
            return Pages.Error.URL();
        }
        _nubRequest.setStatus(WorkflowStatus.ApprovalRequested);
        setModifiedInfo();
        _nubRequest = _nubRequestFacade.saveNubRequest(_nubRequest);
        _nubSessionTools.refreshNodes();
        return "";
    }

    public String deleteDocument(String name) {
        NubRequestDocument existingDoc = findDocumentByName(name);
        if (existingDoc != null) {
            _nubRequest.getDocuments().remove(existingDoc);
        }
        return null;
    }

    public String downloadDocument(String name) {
        Document document = findDocumentByName(name);
        if (document != null) {
            return Utils.downloadDocument(document);
        }
        return "";
    }

    public NubRequestDocument findDocumentByName(String name) {
        for (NubRequestDocument document : _nubRequest.getDocuments()) {
            if (document.getName().equals(name)) {
                return document;
            }
        }
        return null;
    }

    public String refresh() {
        return "";
    }

    // <editor-fold defaultstate="collapsed" desc="CheckElements">
    String _msg = "";
    String _elementId = "";

    private boolean requestIsComplete() {
        _msg = "";
        String newTopic = "";
        String ik = "";
        if (_nubRequest.getIk() != null) {
            ik = _nubRequest.getIk().toString();
        }
        newTopic = checkField(newTopic, ik, "lblIK", "form:ikMulti", NubRequestTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubRequest.getGender(), 1, 2, "lblSalutation", "form:cbxGender", NubRequestTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubRequest.getFirstName(), "lblFirstName", "form:firstname", NubRequestTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubRequest.getLastName(), "lblLastName", "form:lastname", NubRequestTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubRequest.getStreet(), "lblStreet", "form:street", NubRequestTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubRequest.getPostalCode(), "lblPostalCode", "form:zip", NubRequestTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubRequest.getTown(), "lblTown", "form:town", NubRequestTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubRequest.getPhone(), "lblPhone", "form:phone", NubRequestTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubRequest.getEmail(), "lblMail", "form:email", NubRequestTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubRequest.getName(), "lblAppellation", "form:nubName", NubRequestTabs.tabNubPage1);
        newTopic = checkField(newTopic, _nubRequest.getDescription(), "lblNubDescription", "form:nubDescription", NubRequestTabs.tabNubPage1);
        if (!_nubRequest.isHasNoProcs()) {
            newTopic = checkField(newTopic, _nubRequest.getProcs(), "lblNubProcRelated", "form:nubProcedures", NubRequestTabs.tabNubPage1);
        }
        newTopic = checkField(newTopic, _nubRequest.getIndication(), "lblIndication", "form:nubIndic", NubRequestTabs.tabNubPage2);
        newTopic = checkField(newTopic, _nubRequest.getReplacement(), "lblNubReplacementPrint", "form:nubReplacement", NubRequestTabs.tabNubPage2);
        newTopic = checkField(newTopic, _nubRequest.getWhatsNew(), "lblWhatsNew", "form:nubWhatsNew", NubRequestTabs.tabNubPage2);
        newTopic = checkField(newTopic, _nubRequest.getInHouseSince(), "lblMethodInHouse", "form:nubInHouse", NubRequestTabs.tabNubPage3);
        newTopic = checkField(newTopic, _nubRequest.getPatientsLastYear(), "lblPatientsLastYear", "form:patientsLastYear", NubRequestTabs.tabNubPage3);
        newTopic = checkField(newTopic, _nubRequest.getPatientsThisYear(), "lblPatientsThisYear", "form:patientsThisYear", NubRequestTabs.tabNubPage3);
        newTopic = checkField(newTopic, _nubRequest.getPatientsFuture(), "lblPatientsFuture", "form:patientsFuture", NubRequestTabs.tabNubPage3);
        newTopic = checkField(newTopic, _nubRequest.getAddCosts(), "lblAddCosts", "form:nubAddCost", NubRequestTabs.tabNubPage4);
        newTopic = checkField(newTopic, _nubRequest.getWhyNotRepresented(), "lblWhyNotRepresented", "form:nubNotRepresented", NubRequestTabs.tabNubPage4);
        if (_nubRequest.getRoleId() < 0) {
            _msg = Utils.getMessage("lblContactRole");
            newTopic = NubRequestTabs.tabNubAddress.name();
        }
        String proxyErr = checkProxyIKs(_nubRequest.getProxyIKs());
        if (!proxyErr.isEmpty()) {
            _msg = Utils.getMessage("lblErrorProxyIKs");
            newTopic = NubRequestTabs.tabNubAddress.name();
        }
        if (!_msg.isEmpty()) {
            _msg = Utils.getMessage("infoMissingFields") + "\\r\\n" + _msg;
            setActiveTopic(newTopic);
            String script = "alert ('" + _msg + "');";
            if (!_elementId.isEmpty()) {
                script += "\r\n document.getElementById('" + _elementId + "').focus();";
            }
            _sessionController.setScript(script);
        }
        return _msg.isEmpty();
    }

    private String checkField(String newTopic, String value, String msgKey, String elementId, NubRequestTabs tab) {
        if (Utils.isNullOrEmpty(value)) {
            _msg += "\\r\\n" + Utils.getMessage(msgKey);
            if (newTopic.isEmpty()) {
                newTopic = tab.name();
                _elementId = elementId;
            }
        }
        return newTopic;
    }

    private String checkField(String newTopic, Integer value, Integer minValue, Integer maxValue, String msgKey, String elementId, NubRequestTabs tab) {
        if (value == null
                || minValue != null && value.intValue() < minValue.intValue()
                || maxValue != null && value.intValue() > maxValue.intValue()) {
            _msg += "\\r\\n" + Utils.getMessage(msgKey);
            if (newTopic.isEmpty()) {
                newTopic = tab.name();
                _elementId = elementId;
            }
        }
        return newTopic;
    }

    // </editor-fold>
    public String downloadTemplate() {
        String content = getNubController().createTemplate(_nubRequest);
        Utils.downloadDocument(content, _nubRequest.getName() + ".nub\"");
        return null;
    }

    @Inject
    AccountFacade _accountFacade;

    public void copyNubRequest(AjaxBehaviorEvent event) {
        int targetYear = 1 + Calendar.getInstance().get(Calendar.YEAR);
        int targetAccountId = _sessionController.getAccountId();
        NubRequest copy = ObjectUtils.copy(_nubRequest);
        copy.setId(-1);
        copy.setStatus(WorkflowStatus.New);
        copy.setDateSealed(null);
        copy.setSealedBy(0);
        copy.setLastModified(null);
        copy.setCreationDate(null);
        copy.setDateOfReview(null);
        copy.setExternalState("");
        copy.setByEmail(false);
        copy.setErrorText("");
        copy.setCreatedBy(targetAccountId);
        copy.setLastChangedBy(targetAccountId);
        if (copy.getAccountId() != targetAccountId) {
            // from partner
            copy.setPatientsLastYear("");
            copy.setPatientsThisYear("");
            copy.setPatientsFuture("");
            copy.setHelperId(copy.getAccountId());
            Account partner = _accountFacade.find(copy.getAccountId());
            copy.setFormFillHelper("Koperationspartner: " + partner.getCompany());
            copy.setAccountId(_sessionController.getAccountId());
            getNubController().populateMasterData(copy, _sessionController.getAccount());
        } else if (copy.getTargetYear() == targetYear - 1) {
            // from previous year
            copy.setPatientsLastYear(_nubRequest.getPatientsThisYear());
            copy.setPatientsThisYear(_nubRequest.getPatientsFuture());
            copy.setPatientsFuture("");
        } else {
            // elder
            copy.setPatientsLastYear("");
            copy.setPatientsThisYear("");
            copy.setPatientsFuture("");
        }
        copy.setTargetYear(targetYear);
        copy = _nubRequestFacade.saveNubRequest(copy);
        if (copy.getId() != -1) {
            Utils.showMessageInBrowser("NUB erfolgreich angelegt");
        }
        _nubSessionTools.refreshNodes();
    }

    // <editor-fold defaultstate="collapsed" desc="Request correction">
    @Inject PortalMessageFacade _messageFacade;
    @Inject MessageService _messageService;

    public String requestCorrection() {
        if (!isReadOnly()) {
            setModifiedInfo();
            _nubRequest = _nubRequestFacade.saveNubRequest(getNubRequest());
        }
        return Pages.NubRequestCorrection.URL();
    }

    private String _message = "";

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public String sendMessage() {
        String subject = "Korrektur NUB-Anfrage \"" + _nubRequest.getName() + "\" erforderlich";
        Account sender = _sessionController.getAccount();
        Account receiver = _accountFacade.find(_nubRequest.getAccountId());
        _nubRequest.setStatus(WorkflowStatus.New.getValue());
        if (!isReadOnly()) {
            // there might have been changes by that user
            setModifiedInfo();
        }
        _nubRequest = _nubRequestFacade.saveNubRequest(_nubRequest);
        _messageService.sendMessage(sender, receiver, subject, _message, Feature.NUB, _nubRequest.getId());
        _nubSessionTools.refreshNodes();
        return Pages.NubSummary.RedirectURL();
    }

    public String cancelMessage() {
        return Pages.NubSummary.RedirectURL();
    }
    // </editor-fold>

}
