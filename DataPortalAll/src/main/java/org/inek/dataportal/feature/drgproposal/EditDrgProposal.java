package org.inek.dataportal.feature.drgproposal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.AccessManager;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.entities.common.ProcedureInfo;
import org.inek.dataportal.entities.drg.DrgProposal;
import org.inek.dataportal.entities.drg.DrgProposalDocument;
import org.inek.dataportal.enums.CodeType;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.enums.DrgProposalCategory;
import org.inek.dataportal.enums.DrgProposalChangeMethod;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.facades.DrgProposalFacade;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.facades.common.DiagnosisFacade;
import org.inek.dataportal.facades.common.ProcedureFacade;
import org.inek.dataportal.facades.cooperation.PortalMessageFacade;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.services.MessageService;
import org.inek.dataportal.common.utils.DocumentationUtil;
import org.inek.dataportal.controller.SessionHelper;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped
public class EditDrgProposal extends AbstractEditController {

    // <editor-fold defaultstate="collapsed" desc="fields">
    private static final Logger LOGGER = Logger.getLogger("EditDrgProposal");

    @Inject private AccessManager _accessManager;
    @Inject private SessionController _sessionController;
    @Inject private SessionHelper _sessionHelper;
    @Inject private ProcedureFacade _procedureFacade;
    @Inject private DiagnosisFacade _diagnosisFacade;
    @Inject private DrgProposalFacade _drgProposalFacade;
    @Inject private ApplicationTools _appTools;
    // </editor-fold>
    
    private String _script;
    private DrgProposal _drgProposal;

    private boolean ensureEmptyEntry(List<ProcedureInfo> procedures) {
        if (procedures.isEmpty() || procedures.get(procedures.size() - 1).getCode() != null) {
            procedures.add(new ProcedureInfo());
            return true;
        }
        return false;
    }

    public String getScript() {
        return _script;
    }

    public void setScript(String script) {
        _script = script;
    }

    enum DrgProposalTabs {
        tabPPAddress,
        tabPPProblem,
        tabPPSolution,
        tabPPCodes,
        tabPPDocuments
    }

    // </editor-fold>
    public EditDrgProposal() {
        //System.out.println("ctor EditDrgProposal");
    }

    @PostConstruct
    private void init() {
        Object id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
            return;
        } else if (id.toString().equals("new")) {
            if (!_appTools.isEnabled(ConfigKey.IsDrgProposalCreateEnabled)) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            _drgProposal = newDrgProposal();
        } else {
            _drgProposal = loadDrgProposal(id);
        }

        setVisibleCategory(_drgProposal.getCategory());
    }

    @PreDestroy
    private void destroy() {
        //LOGGER.log(Level.WARNING, "Destroy EditPeppProposal");
    }

    private DrgProposal loadDrgProposal(Object drgId) {
        try {
            int id = Integer.parseInt("" + drgId);
            DrgProposal drgProposal = _drgProposalFacade.findFresh(id);
            if (_accessManager.isAccessAllowed(Feature.DRG_PROPOSAL, drgProposal.getStatus(), drgProposal.getAccountId())) {
                return drgProposal;
            }
        } catch (NumberFormatException ex) {
            LOGGER.info(ex.getMessage());
        }
        return newDrgProposal();
    }

    private DrgProposal newDrgProposal() {
        Account account = _sessionController.getAccount();
        DrgProposal proposal = new DrgProposal();
        proposal.setAccountId(account.getId());
        proposal.setCreatedBy(account.getId());
        populateMasterData(proposal, account);
        return proposal;
    }

    private void populateMasterData(DrgProposal proposal, Account account) {
        proposal.setInstitute(account.getCompany());
        proposal.setGender(account.getGender());
        proposal.setTitle(account.getTitle());
        proposal.setFirstName(account.getFirstName());
        proposal.setLastName(account.getLastName());
        proposal.setRoleId(account.getRoleId());
        proposal.setStreet(account.getStreet());
        proposal.setPostalCode(account.getPostalCode());
        proposal.setTown(account.getTown());
        String phone = account.getPhone();
        if (Utils.isNullOrEmpty(phone)) {
            phone = account.getCustomerPhone();
        }
        proposal.setPhone(phone);
        proposal.setFax(account.getCustomerFax());
        proposal.setEmail(account.getEmail());
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public DrgProposal getDrgProposal() {
        return _drgProposal;
    }

    public boolean isAnonymousData() {
        return _drgProposal.isAnonymousData() == null ? false : _drgProposal.isAnonymousData();
    }

    public void setAnonymousData(boolean value) {
        _drgProposal.setAnonymousData(value);
    }

    // </editor-fold>
    @Override
    protected void addTopics() {
        addTopic(DrgProposalTabs.tabPPAddress.name(), Pages.DrgProposalEditAddress.URL());
        addTopic(DrgProposalTabs.tabPPProblem.name(), Pages.DrgProposalEditProblem.URL());
        addTopic(DrgProposalTabs.tabPPSolution.name(), Pages.DrgProposalEditSolution.URL());
        addTopic(DrgProposalTabs.tabPPCodes.name(), Pages.DrgProposalEditCoding.URL());
        addTopic(DrgProposalTabs.tabPPDocuments.name(), Pages.DrgProposalEditDocuments.URL());
    }

    // <editor-fold defaultstate="collapsed" desc="Tab master data">
    private List<SelectItem> _categoryItems;

    private List<SelectItem> _changeMethodItems;

    public List<SelectItem> getCategories() {
        if (_categoryItems == null) {
            _categoryItems = new ArrayList<>();
            _categoryItems.add(new SelectItem(null, Utils.getMessage("lblChooseEntry")));
            for (DrgProposalCategory cat : DrgProposalCategory.values()) {
                if ((cat != DrgProposalCategory.UNKNOWN) && (cat != DrgProposalCategory.CODES)) {
                    SelectItem item = new SelectItem(cat.name(), Utils.getMessage("DrgProposalCategory." + cat.name()));
                    _categoryItems.add(item);
                }
            }
        }
        return _categoryItems;
    }

    public List<SelectItem> getChangeMethodDiag() {
        // if (_changeMethodItems == null) {
        _changeMethodItems = new ArrayList<>();
        _changeMethodItems.add(new SelectItem(null, Utils.getMessage("lblChooseMethodEntry")));
        for (DrgProposalChangeMethod pcm : DrgProposalChangeMethod.values()) {

            if (_drgProposal.getCategory() == DrgProposalCategory.CCL) {
                if ((pcm != DrgProposalChangeMethod.UNKNOWN) && (pcm.toString().contains("CCL"))) {
                    SelectItem item = new SelectItem(pcm.name(), Utils.getMessage("DrgChangeMethod." + pcm.name()));
                    _changeMethodItems.add(item);
                }
            } else {
                if ((pcm != DrgProposalChangeMethod.UNKNOWN) && (!pcm.toString().contains("CCL"))) {
                    SelectItem item = new SelectItem(pcm.name(), Utils.getMessage("DrgChangeMethod." + pcm.name()));
                    _changeMethodItems.add(item);
                }
            }
        }
        //}
        return _changeMethodItems;
    }

    public List<SelectItem> getChangeMethodProc() {

        // if (_changeMethodItems == null) {
        _changeMethodItems = new ArrayList<>();
        _changeMethodItems.add(new SelectItem(null, Utils.getMessage("lblChooseMethodEntry")));
        for (DrgProposalChangeMethod pcm : DrgProposalChangeMethod.values()) {
            if ((pcm != DrgProposalChangeMethod.UNKNOWN) && (!pcm.toString().contains("CCL"))) {
                SelectItem item = new SelectItem(pcm.name(), Utils.getMessage("DrgChangeMethod." + pcm.name()));
                _changeMethodItems.add(item);
            }
        }
        //}
        return _changeMethodItems;
    }

    public void changeCategory(ValueChangeEvent e) {
        if (!e.getNewValue().equals(e.getOldValue())) {
            setVisibleCategory((DrgProposalCategory) e.getNewValue());
        }
    }

    private void setVisibleCategory(DrgProposalCategory cat) {
        if (cat == null) {
            return;
        }

        findTopic(DrgProposalTabs.tabPPCodes.name())
                .setVisible(cat.equals(DrgProposalCategory.CODES) 
                        || cat.equals(DrgProposalCategory.SYSTEM) 
                        || cat.equals(DrgProposalCategory.CCL));
    }

    public boolean isSystem() {
        return _drgProposal.getCategory() == DrgProposalCategory.SYSTEM;
    }

    public String getCcl() {
        //return "display: inline-block; width: 49%;";
        return _drgProposal.getCategory() == DrgProposalCategory.CCL ? "display: none;" : "display: inline-block; width: 49%;";
    }

    public String getCcl2() {
        //return "display: inline-block; width: 49%;";
        return _drgProposal.getCategory() == DrgProposalCategory.CCL 
                ? "display: inline-block; width: 98%; padding-right: 1%; border-right: solid 1px;" 
                : "display: inline-block; width: 49%; padding-right: 1%; border-right: solid 1px;";
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Codes">
    public String searchDiag() {
        return searchCode(CodeType.Diag);
    }

    public String searchProc() {
        return searchCode(CodeType.Proc);
    }

    public String searchDrg() {
        return searchCode(CodeType.Drg);
    }

    public String searchCode(CodeType codeType) {
        _sessionHelper.getSearchController().bindSearchConsumer(this)
                .bindTargetPage(Pages.DrgProposalEditCoding.URL())
                .enableCodeType(CodeType.Diag).enableCodeType(CodeType.Proc).enableCodeType(CodeType.Drg)
                .bindCodeType(codeType).setCodeSystem(CodeType.Drg);
        return Pages.SearchCode.URL();
    }

    @Override
    public void addProcedure(String code) {
        String procCodes = _drgProposal.getProcs();
        procCodes = (procCodes == null || procCodes.length() == 0) ? code : procCodes + "\r\n" + code;
        _drgProposal.setProcs(procCodes);
    }

    @Override
    public void addDiagnosis(String code) {
        String diagCodes = _drgProposal.getDiagCodes();
        diagCodes = (diagCodes == null || diagCodes.length() == 0) ? code : diagCodes + "\r\n" + code;
        _drgProposal.setDiagCodes(diagCodes);
    }

    @Override
    public void addDrg(String code) {
        String drgCodes = _drgProposal.getDrg();
        drgCodes = (drgCodes == null || drgCodes.length() == 0) ? code : drgCodes + "\r\n" + code;
        _drgProposal.setDrg(drgCodes);
    }

    public void checkDiagnosisCodes(FacesContext context, UIComponent component, Object value) {
        int targetYear = Utils.getTargetYear(Feature.DRG_PROPOSAL);
        String invalidCodes = _diagnosisFacade.checkDiagnoses(value.toString(), targetYear - 2, targetYear - 1);
        if (invalidCodes.length() > 0) {
            FacesMessage msg = new FacesMessage(invalidCodes);
            throw new ValidatorException(msg);
        }
    }

    public void checkProcedureCodes(FacesContext context, UIComponent component, Object value) {
        int targetYear = Utils.getTargetYear(Feature.DRG_PROPOSAL);
        String invalidCodes = _procedureFacade.checkProcedures(value.toString(), targetYear - 2, targetYear - 1);
        if (invalidCodes.length() > 0) {
            FacesMessage msg = new FacesMessage(invalidCodes);
            throw new ValidatorException(msg);
        }
    }

    public String removeProcedure(String code) {
        return "";
    }

//    public void keyUp(AjaxBehaviorEvent event) {
//        HtmlInputText t = (HtmlInputText) event.getSource();
//        String currentId = t.getClientId();
//        List<ProcedureInfo> procedures = _drgProposal.getProcedures();
//        if (ensureEmptyEntry(procedures)) {
//            _script = "setCaretPosition('" + currentId + "', -1);";
//        } else {
//            _script = "";
//            FacesContext.getCurrentInstance().responseComplete();
//        }
//    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Tab Documents">
//    public boolean isAnonymousData() {
//        return _drgProposal.isAnonymousData() == null ? false : _drgProposal.isAnonymousData();
//    }
//    public void setAnonymousData(boolean value) {
//        _drgProposal.setAnonymousData(value);
//    }
    public boolean isDocumentAvailable() {
        return !_drgProposal.getDocuments().isEmpty() || !_drgProposal.getDocumentsOffline().isEmpty();
    }
    // </editor-fold>

    public boolean isReadOnly() {
        return _accessManager.isReadOnly(Feature.DRG_PROPOSAL, _drgProposal.getStatus(), _drgProposal.getAccountId());
    }

    @Override
    protected void topicChanged() {
        if (!isReadOnly()) {
            saveData();
        }
    }
    
    public String save() {
        saveData();

        if (_drgProposal != null && isValidId(_drgProposal.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSaveAndMentionSend").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    private void saveData() {
        setModifiedInfo();
        _drgProposal = _drgProposalFacade.saveDrgProposal(_drgProposal);
    }

    private void setModifiedInfo() {
        _drgProposal.setLastChangedBy(_sessionController.getAccountId());
        _drgProposal.setLastModified(Calendar.getInstance().getTime());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsDrgProposalSendEnabled)) {
            return false;
        }
        return _accessManager.isSealedEnabled(Feature.DRG_PROPOSAL, _drgProposal.getStatus(), _drgProposal.getAccountId());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsDrgProposalSendEnabled)) {
            return false;
        }
        return _accessManager.isApprovalRequestEnabled(Feature.DRG_PROPOSAL, _drgProposal.getStatus(), _drgProposal.getAccountId());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsDrgProposalSendEnabled)) {
            return false;
        }
        return _accessManager.isRequestCorrectionEnabled(Feature.DRG_PROPOSAL, _drgProposal.getStatus(), _drgProposal.getAccountId());
    }

    public boolean isTakeEnabled() {
        return _accessManager.isTakeEnabled(Feature.DRG_PROPOSAL, _drgProposal.getStatus(), _drgProposal.getAccountId());
    }

    /**
     * This function seals a drgProposal if possible. Sealing is possible, if
     * all mandatory fields are fulfilled. After sealing, the proposal can not
     * be edited and is available for the InEK.
     *
     * @return
     */
    public String sealDrgProposal() {
        if (!drgProposalIsComplete()) {
            return getActiveTopic().getOutcome();
        }

        _drgProposal.setStatus(WorkflowStatus.Provided.getId());
        _drgProposal.setDateSealed(Calendar.getInstance().getTime());
        _drgProposal.setSealedBy(_sessionController.getAccountId());
        if (_drgProposal.getLastModified() == null) {
            setModifiedInfo();
        }
        _drgProposal = _drgProposalFacade.saveDrgProposal(_drgProposal);

        if (isValidId(_drgProposal.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameDRG_PROPOSAL") + " " + _drgProposal.getExternalId());
            Utils.getFlash().put("targetPage", Pages.DrgProposalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_drgProposal));
            String msg = Utils.getMessage("msgConfirmDrgProposal").replace("\r", "").replace("\n", "\\r\\n");
            msg = String.format(msg, _drgProposal.getExternalId(), Utils.getTargetYear(Feature.DRG_PROPOSAL));
            String script = "alert ('" + msg + "');";
            _sessionController.setScript(script);
            return Pages.PrintView.URL();
        }
        return "";
    }

    public String requestApprovalDrgProposal() {
        if (!drgProposalIsComplete()) {
            return null;
        }
        _drgProposal.setStatus(WorkflowStatus.ApprovalRequested);
        saveData();
        return "";
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        _drgProposal.setAccountId(_sessionController.getAccountId());
        _drgProposal = _drgProposalFacade.saveDrgProposal(_drgProposal);
        return "";
    }

    public String reloadMaster() {
        populateMasterData(_drgProposal, _sessionController.getAccount());
        return "";
    }

    public String takeDocuments() {
        DrgProposalController ppController = (DrgProposalController) _sessionController.getFeatureController(Feature.DRG_PROPOSAL);

        for (DrgProposalDocument doc : ppController.getDocuments()) {
            DrgProposalDocument existingDoc = findByName(doc.getName());
            if (existingDoc != null) {
                _drgProposal.getDocuments().remove(existingDoc);
            }
            _drgProposal.getDocuments().add(doc);
        }
        ppController.getDocuments().clear();

        return "";
    }

    public String deleteDocument(String name) {
        for (Iterator<DrgProposalDocument> itr = _drgProposal.getDocuments().iterator(); itr.hasNext();) {
            DrgProposalDocument document = itr.next();
            if (document.getName().equals(name)) {
                itr.remove();
            }
        }
        return "";
    }

    public String downloadDocument(String name) {
        DrgProposalDocument document = findByName(name);
        if (document != null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseHeader("Content-Type", "application/octet-stream");
            externalContext.setResponseHeader("Content-Length", "" + document.getContent().length);
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + name + "\"");
            ByteArrayInputStream is = new ByteArrayInputStream(document.getContent());
            try {
                new StreamHelper().copyStream(is, externalContext.getResponseOutputStream());

            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                return Pages.Error.URL();
            }
            facesContext.responseComplete();
        }
        return null;
    }

    private DrgProposalDocument findByName(String name) {
        for (DrgProposalDocument drgProposal : _drgProposal.getDocuments()) {
            if (drgProposal.getName().equals(name)) {
                return drgProposal;
            }
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="CheckElements">
    private String _msg = "";
    private String _elementId = "";

    private boolean drgProposalIsComplete() {
        _msg = "";
        String newTopic = "";
        DrgProposal drgProposal = _drgProposal;
        newTopic = checkField(newTopic, drgProposal.getName(), "lblAppellation", "form:name", DrgProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, 
                drgProposal.getCategory() == null 
                        || drgProposal.getCategory() == DrgProposalCategory.UNKNOWN ? null : drgProposal.getCategory().name(), 
                "lblCategory", "form:category", DrgProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, drgProposal.getInstitute(), "lblDrgProposalingInstitute", "form:institute", DrgProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, drgProposal.getGender(), 1, 2, "lblSalutation", "form:cbxGender", DrgProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, drgProposal.getFirstName(), "lblFirstName", "form:firstname", DrgProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, drgProposal.getLastName(), "lblLastName", "form:lastname", DrgProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, drgProposal.getStreet(), "lblStreet", "form:street", DrgProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, drgProposal.getPostalCode(), "lblPostalCode", "form:zip", DrgProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, drgProposal.getTown(), "lblTown", "form:town", DrgProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, drgProposal.getPhone(), "lblPhone", "form:phone", DrgProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, drgProposal.getEmail(), "lblMail", "form:email", DrgProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, drgProposal.getProblem(), "lblProblemDescription", "form:problem", DrgProposalTabs.tabPPProblem);
        newTopic = checkField(newTopic, drgProposal.getSolution(), "lblSuggestedSolution", "form:solution", DrgProposalTabs.tabPPSolution);
        if (drgProposal.getDocuments() != null && drgProposal.getDocuments().size() > 0
                || drgProposal.getDocumentsOffline() != null && drgProposal.getDocumentsOffline().length() > 0) {
            newTopic = checkField(
                    newTopic, 
                    drgProposal.isAnonymousData() ? "true" : "", 
                    "lblAnonymousData", 
                    "form:anonymousData", 
                    DrgProposalTabs.tabPPDocuments);
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

    private String checkField(String newTopic, String value, String msgKey, String elementId, DrgProposalTabs tab) {
        if (Utils.isNullOrEmpty(value)) {
            _msg += "\\r\\n" + Utils.getMessage(msgKey);
            if (newTopic.isEmpty()) {
                newTopic = tab.name();
                _elementId = elementId;
            }
        }
        return newTopic;
    }

    private String checkField(
            String newTopic, 
            Integer value, 
            Integer minValue, 
            Integer maxValue, 
            String msgKey, 
            String elementId, 
            DrgProposalTabs tab) {
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
    // <editor-fold defaultstate="collapsed" desc="Request correction">
    @Inject private AccountFacade _accountFacade;
    @Inject private PortalMessageFacade _messageFacade;
    @Inject private MessageService _messageService;

    public String requestCorrection() {
        if (!isReadOnly()) {
            saveData();
        }
        return Pages.DrgProposalRequestCorrection.URL();
    }

    private String _message = "";

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public String sendMessage() {
        String subject = "Korrektur DRG-Vorschlag \"" + _drgProposal.getName() + "\" erforderlich";
        Account sender = _sessionController.getAccount();
        Account receiver = _accountFacade.findAccount(_drgProposal.getAccountId());
        _drgProposal.setStatus(WorkflowStatus.New.getId());
        if (!isReadOnly()) {
            // there might have been changes by that user
            setModifiedInfo();
        }
        _drgProposal = _drgProposalFacade.saveDrgProposal(_drgProposal);
        _messageService.sendMessage(sender, receiver, subject, _message, Feature.DRG_PROPOSAL, _drgProposal.getId());
        return Pages.DrgProposalSummary.RedirectURL();
    }

    public String cancelMessage() {
        return Pages.DrgProposalSummary.RedirectURL();
    }
    // </editor-fold>
}
