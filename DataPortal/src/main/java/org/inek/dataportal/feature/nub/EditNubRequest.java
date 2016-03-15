/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.nub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.Customer;
import org.inek.dataportal.entities.Document;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.entities.nub.NubRequest;
import org.inek.dataportal.entities.nub.NubRequestDocument;
import org.inek.dataportal.enums.CodeType;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.facades.NubRequestFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.common.ProcedureFacade;
import org.inek.dataportal.facades.cooperation.PortalMessageFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.ObjectUtils;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.helper.structures.FieldValues;
import org.inek.dataportal.helper.structures.MessageContainer;
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
    @Inject ApplicationTools _appTools;
    private NubRequest _nubRequest;
    private NubRequest _nubRequestBaseline;
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
            _nubRequestBaseline = newNubRequest();
            _nubRequestBaseline.setCreatedBy(_sessionController.getAccountId());
            ensureCooperativeRight(_nubRequest);
            ensureSupervisorRight(_nubRequest);
        } else {
            _nubRequest = loadNubRequest(ppId);
        }
    }

    private NubRequest loadNubRequest(Object ppId) {
        try {
            int id = Integer.parseInt("" + ppId);
            NubRequest nubRequest = _nubRequestFacade.findFresh(id);
            if (hasSufficientRights(nubRequest)) {
                _nubRequestBaseline = _nubRequestFacade.find(id);
                return nubRequest;
            }
        } catch (NumberFormatException ex) {
            _logger.info(ex.getMessage());
        }
        _nubRequestBaseline = newNubRequest();
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

    public List<SelectItem> getIks() {
        Account account = _sessionController.getAccount();
        Set<Integer> iks = _sessionController.getAccount().getAdditionalIKs().stream().map(i -> i.getIK()).collect(Collectors.toSet());
        List<SelectItem> items = new ArrayList<>();
        if (account.getIK() != null) {
            iks.add(account.getIK());
        }
        if (_nubRequest.getIk() != null) {
            iks.add(_nubRequest.getIk());
        }
        for (int ik : iks) {
            items.add(new SelectItem(ik));
        }
        if (_nubRequest.getIk() == null) {
            items.add(0, new SelectItem(""));
        }
        return items;
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
        String msg = _nubSessionTools.checkProxyIKs(value.toString());
        if (!msg.isEmpty()) {
            FacesMessage fmsg = new FacesMessage(msg);
            throw new ValidatorException(fmsg);

        }
    }

    public void formatProxyIks() {
        String iks[] = _nubRequest.getProxyIKs().split("\\s|,|\r|\n");
        String formatted = "";
        for (String ik : iks) {
            if (ik.isEmpty() || ik.equals("" + _nubRequest.getIk())) {
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
        if (isReadOnly() || _nubRequest.isSealed()) {
            // paranoid reload
            // If the developer forgot about setting all fields to readonly,
            // or if the partner sealed
            // then the user might have edited some fields before setting the owener
            // A reload forces the data into the original state.
            _nubRequest = _nubRequestFacade.find(_nubRequest.getId());
        }
        _nubRequest.setAccountId(_sessionController.getAccountId());
        _nubRequest = _nubRequestFacade.saveNubRequest(_nubRequest);
        return "";
    }

    public String reloadMaster() {
        getNubController().populateMasterData(_nubRequest, _sessionController.getAccount());
        return "";
    }

    public String save() {
        setModifiedInfo();
        formatProxyIks();
        boolean isNewRequest = !isValidId(_nubRequest.getId());
        String msg = "";
        try {
            _nubRequest = _nubRequestFacade.saveNubRequest(_nubRequest);
            if (!isValidId(_nubRequest.getId())) {
                return Pages.Error.URL();
            }
            msg = Utils.getMessage("msgSave");
        } catch (Exception ex) {
            if (isNewRequest || !(ex.getCause() instanceof OptimisticLockException)) {
                throw ex;
            }
            msg = mergeAndReportChanges();
        }
        if (_nubRequest != null) {
            _nubRequestBaseline = _nubRequestFacade.findFresh(_nubRequest.getId());  // update base line
        }
        String script = "alert ('" + msg.replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
        _sessionController.setScript(script);
        return "";

    }

    private String mergeAndReportChanges() {
        NubRequest modifiedNubRequest = _nubRequest;
        _nubRequest = _nubRequestFacade.findFresh(modifiedNubRequest.getId());
        if (_nubRequest == null) {
            _sessionController.logMessage("ConcurrentUpdate [DatasetDeleted], NUB: " + modifiedNubRequest.getId());
            Utils.navigate(Pages.NubSummary.URL());
            return Utils.getMessage("msgDatasetDeleted");
        }
        Map<String, FieldValues> differencesPartner = getDifferencesPartner(getExcludedTypes());
        Map<String, FieldValues> differencesUser = getDifferencesUser(modifiedNubRequest, getExcludedTypes());

        List<String> collisions = updateFields(differencesUser, differencesPartner, modifiedNubRequest);

        Map<String, String> documentationFields = DocumentationUtil.getFieldTranslationMap(_nubRequest);

        String msgKey = _nubRequest.isSealed() ? "msgDatasetSealed" : collisions.isEmpty() ? "msgMergeOk" : "msgMergeCollision";
        _sessionController.logMessage("ConcurrentUpdate [" + msgKey.substring(3) + "], NUB: " + modifiedNubRequest.getId());
        String msg = Utils.getMessage(msgKey);
        for (String fieldName : collisions) {
            msg += "\r\n### " + documentationFields.get(fieldName) + " ###";
        }
        for (String fieldName : differencesPartner.keySet()) {
            msg += "\r\n" + documentationFields.get(fieldName);
        }
        if (!_nubRequest.isSealed()) {
            _nubRequest = _nubRequestFacade.saveNubRequest(_nubRequest);
        }
        return msg;
    }

    private List<Class> getExcludedTypes() {
        List<Class> excludedTypes = new ArrayList<>();
        excludedTypes.add(Date.class);
        return excludedTypes;
    }

    private Map<String, FieldValues> getDifferencesUser(NubRequest modifiedNubRequest, List<Class> excludedTypes) {
        Map<String, FieldValues> differencesUser = ObjectUtils.getDifferences(_nubRequestBaseline, modifiedNubRequest, excludedTypes);
        differencesUser.remove("_status");
        differencesUser.remove("_lastChangedBy");
        return differencesUser;
    }

    private Map<String, FieldValues> getDifferencesPartner(List<Class> excludedTypes) {
        if (_nubRequest.isSealed()) {
            // sealed by partner. By definition this is the new version and there are no differences
            return Collections.EMPTY_MAP;
        }
        Map<String, FieldValues> differencesPartner = ObjectUtils.getDifferences(_nubRequestBaseline, _nubRequest, excludedTypes);
        differencesPartner.remove("_status");
        differencesPartner.remove("_version");
        differencesPartner.remove("_lastChangedBy");
        return differencesPartner;
    }

    private List<String> updateFields(Map<String, FieldValues> differencesUser, Map<String, FieldValues> differencesPartner, NubRequest modifiedNubRequest) {
        List<String> collisions = new ArrayList<>();
        for (String fieldName : differencesUser.keySet()) {
            if (differencesPartner.containsKey(fieldName) || _nubRequest.isSealed()) {
                collisions.add(fieldName);
                differencesPartner.remove(fieldName);
                continue;
            }
            FieldValues fieldValues = differencesUser.get(fieldName);
            Field field = fieldValues.getField();
            ObjectUtils.setField(field, modifiedNubRequest, _nubRequest);
        }
        return collisions;
    }

    private void setModifiedInfo() {
        _nubRequest.setLastChangedBy(_sessionController.getAccountId());
        _nubRequest.setLastModified(Calendar.getInstance().getTime());
    }

    public boolean isValidId(Integer id) {
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
        return _nubSessionTools.isSealEnabled(_nubRequest);
    }

    public boolean isUpdateEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsNubSendEnabled)) {
            return false;
        }
        return _cooperationTools.isUpdateEnabled(Feature.NUB, _nubRequest.getStatus(), _nubRequest.getAccountId(), _nubRequest.getIk());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsNubSendEnabled)) {
            return false;
        }
        return _cooperationTools.isApprovalRequestEnabled(Feature.NUB, _nubRequest.getStatus(), _nubRequest.getAccountId(), _nubRequest.getIk());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsNubSendEnabled)) {
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
        if (!requestIsComplete(_nubRequest)) {
            return getActiveTopic().getOutcome();
        }
        if (_nubRequest.getStatus().compareTo(WorkflowStatus.Provided) >= 0) {
            return Pages.Error.URL();
        }

        _nubRequest = _nubSessionTools.prepareSeal(_nubRequest);
        
        boolean isNewRequest = !isValidId(_nubRequest.getId());
        String msg = "";
        try {
            _nubRequest = _nubRequestFacade.saveNubRequest(_nubRequest);
            if (isValidId(_nubRequest.getId())) {
                _nubSessionTools.sendNubConfirmationMail(_nubRequest);

                Utils.getFlash().put("headLine", Utils.getMessage("nameNUB") + " " + _nubRequest.getExternalId());
                Utils.getFlash().put("targetPage", Pages.NubSummary.URL());
                Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_nubRequest));
                return Pages.PrintView.URL();
            }
        } catch (Exception ex) {
            if (isNewRequest || !(ex.getCause() instanceof OptimisticLockException)) {
                throw ex;
            }
            msg = mergeAndReportChanges();
        }
        if (_nubRequest != null) {
            _nubRequestBaseline = _nubRequestFacade.findFresh(_nubRequest.getId());  // update base line
        }
        String script = "alert ('" + msg.replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
        _sessionController.setScript(script);
        return "";
    }

    public String updateNubRequest() {
        if (!requestIsComplete(_nubRequest)) {
            return getActiveTopic().getOutcome();
        }
        if (_nubRequest.getStatus() != WorkflowStatus.CorrectionRequested) {
            return Pages.Error.URL();
        }

        _nubRequest.setStatus(WorkflowStatus.Updated);
        _nubRequest.setLastChangedBy(_sessionController.getAccountId());
        _nubRequest.setLastModified(Calendar.getInstance().getTime());

        _nubRequest = _nubRequestFacade.saveNubRequest(_nubRequest);

        if (isValidId(_nubRequest.getId())) {
            //sendNubConfirmationMail(); todo? sendUpdateMail?

            Utils.getFlash().put("headLine", Utils.getMessage("nameNUB") + " " + _nubRequest.getExternalId());
            Utils.getFlash().put("targetPage", Pages.NubSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_nubRequest));
            return Pages.PrintView.URL();
        }
        return "";
    }

    public String requestApproval() {
        if (!requestIsComplete(_nubRequest)) {
            return getActiveTopic().getOutcome();
        }
        if (_nubRequest.getStatus().compareTo(WorkflowStatus.ApprovalRequested) >= 0) {
            return Pages.Error.URL();
        }
        _nubRequest.setStatus(WorkflowStatus.ApprovalRequested);
        setModifiedInfo();

        boolean isNewRequest = !isValidId(_nubRequest.getId());
        String msg = "";
        try {
            _nubRequest = _nubRequestFacade.saveNubRequest(_nubRequest);
            return "";
        } catch (Exception ex) {
            if (isNewRequest || !(ex.getCause() instanceof OptimisticLockException)) {
                throw ex;
            }
            msg = mergeAndReportChanges();
        }
        if (_nubRequest != null) {
            _nubRequestBaseline = _nubRequestFacade.findFresh(_nubRequest.getId());  // update base line
        }
        String script = "alert ('" + msg.replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
        _sessionController.setScript(script);
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

    private boolean requestIsComplete(NubRequest nubRequest) {
        MessageContainer message = _nubSessionTools.composeMissingFieldsMessage(nubRequest);
        if (message.containsMessage()) {
            message.setMessage(Utils.getMessage("infoMissingFields") + "\\r\\n" + message.getMessage());
            setActiveTopic(message.getTopic());
            String script = "alert ('" + message.getMessage() + "');";
            if (!message.getElementId().isEmpty()) {
                script += "\r\n document.getElementById('" + message.getElementId() + "').focus();";
            }
            _sessionController.setScript(script);
        }
        return !message.containsMessage();
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
        if (_nubSessionTools.copyNubRequest(_nubRequest)){
            Utils.showMessageInBrowser("NUB erfolgreich angelegt");
        }
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
