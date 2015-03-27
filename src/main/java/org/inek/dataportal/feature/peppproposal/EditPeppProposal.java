package org.inek.dataportal.feature.peppproposal;

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
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.common.ProcedureInfo;
import org.inek.dataportal.entities.pepp.PeppProposal;
import org.inek.dataportal.entities.pepp.PeppProposalDocument;
import org.inek.dataportal.enums.CodeType;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.DrgProposalCategory;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.GlobalVars;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.PeppProposalCategory;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.PeppProposalFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.common.DiagnosisFacade;
import org.inek.dataportal.facades.common.ProcedureFacade;
import org.inek.dataportal.facades.cooperation.PortalMessageFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.StreamHelper;
import org.inek.dataportal.helper.Utils;
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
public class EditPeppProposal extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("EditPeppProposal");
    @Inject CooperationTools _cooperationTools;

    // <editor-fold defaultstate="collapsed" desc="fields">
    @Inject
    private SessionController _sessionController;
    @Inject
    private ProcedureFacade _procedureFacade;
    @Inject
    private DiagnosisFacade _diagnosisFacade;
    @Inject
    private PeppProposalFacade _peppProposalFacade;
    private String _script;
    private PeppProposal _peppProposal;

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

    enum PeppProposalTabs {

        tabPPAddress,
        tabPPProblem,
        tabPPSolution,
        tabPPCodes,
        tabPPDocuments,
    }

    // </editor-fold>
    public EditPeppProposal() {
        //System.out.println("ctor EditPeppProposal");
    }

    @PostConstruct
    private void init() {

        //_logger.log(Level.WARNING, "Init EditPeppProposal");
        Object ppId = Utils.getFlash().get("ppId");
        if (ppId == null) {
            _peppProposal = newPeppProposal();
        } else {
            _peppProposal = loadPeppProposal(ppId);
        }
        //ensureEmptyEntry(_peppProposal.getProcedures());
        setVisible(_peppProposal.getCategory());
    }

    @PreDestroy
    private void destroy() {
        //_logger.log(Level.WARNING, "Destroy EditPeppProposal");
    }

    private PeppProposal loadPeppProposal(Object ppId) {
        try {
            int id = Integer.parseInt("" + ppId);
            PeppProposal peppProposal = _peppProposalFacade.findFresh(id);
            if (peppProposal.getAccountId() != _sessionController.getAccountId() && _sessionController.isInekUser(Feature.PEPP_PROPOSAL)) {
                // it's  not mine, but I'm an authorized InEK user: add right to read
                _cooperationTools.addReadRight(Feature.PEPP_PROPOSAL, peppProposal.getAccountId());
            }
            if (_cooperationTools.isAllowed(Feature.PEPP_PROPOSAL, peppProposal.getStatus(), peppProposal.getAccountId())) {
                return peppProposal;
            }
        } catch (NumberFormatException ex) {
            _logger.info(ex.getMessage());
        }
        return newPeppProposal();
    }

    private PeppProposal newPeppProposal() {
        Account account = _sessionController.getAccount();
        PeppProposal proposal = new PeppProposal();
        proposal.setAccountId(account.getId());
        proposal.setCreatedBy(account.getId());
        populateMasterData(proposal, account);
        return proposal;
    }

    private void populateMasterData(PeppProposal proposal, Account account) {
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
    public PeppProposal getPeppProposal() {
        return _peppProposal;
    }

    // </editor-fold>
    @Override
    protected void addTopics() {
        addTopic(PeppProposalTabs.tabPPAddress.name(), Pages.PeppProposalEditAddress.URL());
        addTopic(PeppProposalTabs.tabPPProblem.name(), Pages.PeppProposalEditProblem.URL());
        addTopic(PeppProposalTabs.tabPPSolution.name(), Pages.PeppProposalEditSolution.URL());
        addTopic(PeppProposalTabs.tabPPCodes.name(), Pages.PeppProposalEditCoding.URL(), false);
        addTopic(PeppProposalTabs.tabPPDocuments.name(), Pages.PeppProposalEditDocuments.URL());
    }

    // <editor-fold defaultstate="collapsed" desc="Tab master data">
    private List<SelectItem> _categoryItems;

    public List<SelectItem> getCategories() {
        if (_categoryItems == null) {
            _categoryItems = new ArrayList<>();
            _categoryItems.add(new SelectItem(null, Utils.getMessage("lblChooseEntry")));
            for (PeppProposalCategory cat : PeppProposalCategory.values()) {
                if (cat != PeppProposalCategory.UNKNOWN) {
                    SelectItem item = new SelectItem(cat.name(), Utils.getMessage("PeppProposalCategory." + cat.name()));
                    _categoryItems.add(item);
                }
            }
        }
        return _categoryItems;
    }

    public void changeCategory(ValueChangeEvent e) {
        if (!e.getNewValue().equals(e.getOldValue())) {
            setVisible((PeppProposalCategory) e.getNewValue());
        }
    }

    private void setVisible(PeppProposalCategory cat) {
        if (cat == null) {
            return;
        }

        findTopic(PeppProposalTabs.tabPPCodes.name()).setVisible(cat.equals(PeppProposalCategory.CODES) || cat.equals(PeppProposalCategory.SYSTEM));
    }

    public boolean isSystem() {
        return getPeppProposal().getCategory() == PeppProposalCategory.SYSTEM;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Codes">

    public String searchDiag() {
        return searchCode(CodeType.Diag);
    }

    public String searchProc() {
        return searchCode(CodeType.Proc);
    }

    public String searchPepp() {
        return searchCode(CodeType.Pepp);
    }

    public String searchCode(CodeType codeType) {
        _sessionController.getSearchController().bindSearchConsumer(this)
                .bindTargetPage(Pages.PeppProposalEdit.URL())
                .enableCodeType(CodeType.Diag).enableCodeType(CodeType.Proc).enableCodeType(CodeType.Pepp)
                .bindCodeType(codeType).setCodeSystem(CodeType.Pepp);;
        return "/common/SearchCode";
    }

    @Override
    public void addProcedure(String code) {
        String procCodes = getPeppProposal().getProcCodes();
        procCodes = (procCodes == null || procCodes.length() == 0) ? code : procCodes + "\r\n" + code;
        getPeppProposal().setProcCodes(procCodes);
    }

    @Override
    public void addDiagnosis(String code) {
        String diagCodes = getPeppProposal().getDiagCodes();
        diagCodes = (diagCodes == null || diagCodes.length() == 0) ? code : diagCodes + "\r\n" + code;
        getPeppProposal().setDiagCodes(diagCodes);
    }

    @Override
    public void addPepp(String code) {
        String peppCodes = getPeppProposal().getPepp();
        peppCodes = (peppCodes == null || peppCodes.length() == 0) ? code : peppCodes + "\r\n" + code;
        getPeppProposal().setPepp(peppCodes);
    }

    public void checkDiagnosisCodes(FacesContext context, UIComponent component, Object value) {
        checkCodes(value.toString(), CodeType.Diag);
    }

    public void checkProcedureCodes(FacesContext context, UIComponent component, Object value) {
        checkCodes(value.toString(), CodeType.Proc);
    }

    private void checkCodes(String codeString, CodeType type) throws ValidatorException {
        String codes[] = codeString.split("\\s");
        StringBuilder invalidCodes = new StringBuilder();
        for (String code : codes) {
            if (type.equals(CodeType.Diag) && _diagnosisFacade.findDiagnosis(code, GlobalVars.PeppProposalSystemYear.getVal() - 2, GlobalVars.PeppProposalSystemYear.getVal() - 1).equals("")
                    || type.equals(CodeType.Proc) && _procedureFacade.findProcedure(code, GlobalVars.PeppProposalSystemYear.getVal() - 2, GlobalVars.PeppProposalSystemYear.getVal() - 1).equals("")) {
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

    public String removeProcedure(String code) {
        return "";
    }

    public void keyUp(AjaxBehaviorEvent event) {
        HtmlInputText t = (HtmlInputText) event.getSource();
        String currentId = t.getClientId();
        List<ProcedureInfo> procedures = getPeppProposal().getProcedures();
        if (ensureEmptyEntry(procedures)) {
            _script = "setCaretPosition('" + currentId + "', -1);";
        } else {
            _script = "";
            FacesContext.getCurrentInstance().responseComplete();
        }
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Tab Documents">

    public boolean isAnonymousData() {
        return getPeppProposal().isAnonymousData() == null ? false : getPeppProposal().isAnonymousData();
    }

    public void setAnonymousData(boolean value) {
        getPeppProposal().setAnonymousData(value);
    }

    public boolean isDocumentAvailable() {
        return !getPeppProposal().getDocuments().isEmpty() || !getPeppProposal().getDocumentsOffline().isEmpty();
    }
    // </editor-fold>

    public boolean isReadOnly() {
        return _cooperationTools.isReadOnly(Feature.PEPP_PROPOSAL, getPeppProposal().getStatus(), getPeppProposal().getAccountId());
    }

    public String save() {
        setModifiedInfo();
        _peppProposal = _peppProposalFacade.savePeppProposal(getPeppProposal());

        if (isValidId(getPeppProposal().getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_sessionController.isEnabled(ConfigKey.IsPeppProposalSendEnabled)) {
            return false;
        }
        return _cooperationTools.isSealedEnabled(Feature.PEPP_PROPOSAL, _peppProposal.getStatus(), _peppProposal.getAccountId());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_sessionController.isEnabled(ConfigKey.IsPeppProposalSendEnabled)) {
            return false;
        }
        return _cooperationTools.isApprovalRequestEnabled(Feature.PEPP_PROPOSAL, _peppProposal.getStatus(), _peppProposal.getAccountId());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_sessionController.isEnabled(ConfigKey.IsPeppProposalSendEnabled)) {
            return false;
        }
        return _cooperationTools.isRequestCorrectionEnabled(Feature.PEPP_PROPOSAL, _peppProposal.getStatus(), _peppProposal.getAccountId());
    }

    public boolean isTakeEnabled() {
        return _cooperationTools.isTakeEnabled(Feature.PEPP_PROPOSAL, _peppProposal.getStatus(), _peppProposal.getAccountId());
    }

    /**
     * This function seals a peppProposal. Usually it can only be called if the
     * peppProposal to seal is confirmed. As a precaution, it performs some
     * checks which have been done in peppProposalSeal.
     *
     * @return
     */
    public String sealPeppProposal() {
        if (!peppProposalIsComplete()) {
            return getActiveTopic().getOutcome();
        }

        _peppProposal.setStatus(WorkflowStatus.Provided.getValue());
        _peppProposal.setDateSealed(Calendar.getInstance().getTime());
        _peppProposal.setSealedBy(_sessionController.getAccountId());
        if (_peppProposal.getLastModified() == null ){
            setModifiedInfo();
        }
        _peppProposal = _peppProposalFacade.savePeppProposal(_peppProposal);

        if (isValidId(_peppProposal.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("namePEPP_PROPOSAL") + " " + _peppProposal.getExternalId());
            Utils.getFlash().put("targetPage", Pages.PeppProposalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_peppProposal));
            String msg = Utils.getMessage("msgConfirmPeppProposal").replace("\r", "").replace("\n", "\\r\\n");
            msg = String.format(msg, _peppProposal.getExternalId(), GlobalVars.PeppProposalSystemYear.getVal());
            String script = "alert ('" + msg + "');";
            _sessionController.setScript(script);
            return Pages.PrintView.URL();
        }
        return null;
    }

    public String requestApprovalPeppProposal() {
        if (!peppProposalIsComplete()) {
            return null;
        }
        _peppProposal.setStatus(WorkflowStatus.ApprovalRequested.getValue());
        setModifiedInfo();
        _peppProposal = _peppProposalFacade.savePeppProposal(_peppProposal);
        return null;
    }

    private void setModifiedInfo() {
        _peppProposal.setLastChangedBy(_sessionController.getAccountId());
        _peppProposal.setLastModified(Calendar.getInstance().getTime());
    }

    public String takeDocuments() {
        PeppProposalController ppController = (PeppProposalController) _sessionController.getFeatureController(Feature.PEPP_PROPOSAL);
        for (PeppProposalDocument doc : ppController.getDocuments()) {
            PeppProposalDocument existingDoc = findByName(doc.getName());
            if (existingDoc != null) {
                getPeppProposal().getDocuments().remove(existingDoc);
            }
            getPeppProposal().getDocuments().add(doc);
        }
        ppController.getDocuments().clear();
        return null;
    }

    public String deleteDocument(String name) {
        for (Iterator<PeppProposalDocument> itr = getPeppProposal().getDocuments().iterator(); itr.hasNext();) {
            PeppProposalDocument document = itr.next();
            if (document.getName().equals(name)) {
                itr.remove();
            }
        }
        return "";
    }

    public String downloadDocument(String name) {
        PeppProposalDocument document = findByName(name);
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
                _logger.log(Level.SEVERE, null, ex);
                return Pages.Error.URL();
            }
            facesContext.responseComplete();
        }
        return null;
    }

    private PeppProposalDocument findByName(String name) {
        for (PeppProposalDocument peppProposal : getPeppProposal().getDocuments()) {
            if (peppProposal.getName().equals(name)) {
                return peppProposal;
            }
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="CheckElements">
    String _msg = "";
    String _elementId = "";

    private boolean peppProposalIsComplete() {
        _msg = "";
        String newTopic = "";
        PeppProposal peppProposal = getPeppProposal();
        newTopic = checkField(newTopic, peppProposal.getName(), "lblAppellation", "form:name", PeppProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, peppProposal.getCategory() == null || peppProposal.getCategory() == PeppProposalCategory.UNKNOWN ? null : peppProposal.getCategory().name(), "lblCategory", "form:category", PeppProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, peppProposal.getInstitute(), "lblPeppProposalingInstitute", "form:institute", PeppProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, peppProposal.getGender(), 1, 2, "lblSalutation", "form:cbxGender", PeppProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, peppProposal.getFirstName(), "lblFirstName", "form:firstname", PeppProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, peppProposal.getLastName(), "lblLastName", "form:lastname", PeppProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, peppProposal.getStreet(), "lblStreet", "form:street", PeppProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, peppProposal.getPostalCode(), "lblPostalCode", "form:zip", PeppProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, peppProposal.getTown(), "lblTown", "form:town", PeppProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, peppProposal.getPhone(), "lblPhone", "form:phone", PeppProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, peppProposal.getEmail(), "lblMail", "form:email", PeppProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, peppProposal.getProblem(), "lblProblemDescription", "form:problem", PeppProposalTabs.tabPPProblem);
        newTopic = checkField(newTopic, peppProposal.getSolution(), "lblSuggestedSolution", "form:solution", PeppProposalTabs.tabPPSolution);
        if (peppProposal.getDocuments() != null && peppProposal.getDocuments().size() > 0
                || peppProposal.getDocumentsOffline() != null && peppProposal.getDocumentsOffline().length() > 0) {
            newTopic = checkField(newTopic, peppProposal.isAnonymousData() ? "true" : "", "lblAnonymousData", "form:anonymousData", PeppProposalTabs.tabPPDocuments);
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

    private String checkField(String newTopic, String value, String msgKey, String elementId, PeppProposalTabs tab) {
        if (Utils.isNullOrEmpty(value)) {
            _msg += "\\r\\n" + Utils.getMessage(msgKey);
            if (newTopic.isEmpty()) {
                newTopic = tab.name();
                _elementId = elementId;
            }
        }
        return newTopic;
    }

    private String checkField(String newTopic, Integer value, Integer minValue, Integer maxValue, String msgKey, String elementId, PeppProposalTabs tab) {
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
    @Inject private Mailer _mailer;
    @Inject AccountFacade _accountFacade;
    @Inject PortalMessageFacade _messageFacade;
    @Inject MessageService _messageService;

    public String requestCorrection() {
        if (!isReadOnly()) {
            setModifiedInfo();
            _peppProposal = _peppProposalFacade.savePeppProposal(getPeppProposal());
        }
        return Pages.PeppProposalRequestCorrection.URL();
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
            _peppProposal.setAccountId(_sessionController.getAccountId());
            _peppProposal = _peppProposalFacade.savePeppProposal(getPeppProposal());
        return "";
    }

    public String reloadMaster() {
        populateMasterData(_peppProposal, _sessionController.getAccount());
        return "";
    }

    private String _message = "";

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public String sendMessage() {
        String subject = "Korrektur Pepp-Vorschlag \"" + _peppProposal.getName() + "\" erforderlich";
        Account sender = _sessionController.getAccount();
        Account receiver = _accountFacade.find(_peppProposal.getAccountId());
        _peppProposal.setStatus(WorkflowStatus.New.getValue());
        if (!isReadOnly()) {
            // there might have been changes by that user
            setModifiedInfo();
        }
        _peppProposal = _peppProposalFacade.savePeppProposal(_peppProposal);
        _messageService.sendMessage(sender, receiver, subject, _message, Feature.PEPP_PROPOSAL, _peppProposal.getId());
        return Pages.PeppProposalSummary.RedirectURL();
    }

    public String cancelMessage() {
        return Pages.PeppProposalSummary.RedirectURL();
    }
    // </editor-fold>

}
