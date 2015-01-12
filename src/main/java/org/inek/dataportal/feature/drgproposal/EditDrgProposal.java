package org.inek.dataportal.feature.drgproposal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.drg.DrgProposal;
import org.inek.dataportal.entities.drg.DrgProposalDocument;

import org.inek.dataportal.entities.common.ProcedureInfo;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.CodeType;
import org.inek.dataportal.enums.DrgProposalCategory;
import org.inek.dataportal.enums.DrgProposalChangeMethod;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.GlobalVars;
import org.inek.dataportal.enums.Pages;

import org.inek.dataportal.facades.common.DiagnosisFacade;
import org.inek.dataportal.facades.DrgProposalFacade;

import org.inek.dataportal.facades.common.ProcedureFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.StreamHelper;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped
public class EditDrgProposal extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("EditDrgProposal");

    // <editor-fold defaultstate="collapsed" desc="fields">
    @Inject
    private SessionController _sessionController;
    @Inject
    private ProcedureFacade _procedureFacade;
    @Inject
    private DiagnosisFacade _diagnosisFacade;
    @Inject
    private DrgProposalFacade _drgProposalFacade;
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
        tabPPPolicy,
        tabPPProblem,
        tabPPSolution,
        tabPPCodes,
        tabPPDocuments,
    }

    // </editor-fold>
    public EditDrgProposal() {
        //System.out.println("ctor EditDrgProposal");
    }

    @PostConstruct
    private void init() {

        //_logger.log(Level.WARNING, "Init EditDrgProposal");
        Object drgId = Utils.getFlash().get("drgId");
        if (drgId == null) {
            _drgProposal = newDrgProposal();
        } else {
            _drgProposal = loadDrgProposal(drgId);
        }
       
        setVisibleCategory(_drgProposal.getCategory());
    }

    @PreDestroy
    private void destroy() {
        //_logger.log(Level.WARNING, "Destroy EditPeppProposal");
    }

    private DrgProposal loadDrgProposal(Object drgId) {
        try {
            int id = Integer.parseInt("" + drgId);
            DrgProposal drgProposal = _drgProposalFacade.find(id);
            if (_sessionController.isMyAccount(drgProposal.getAccountId())) {
                return drgProposal;
            }
        } catch (NumberFormatException ex) {
            _logger.info(ex.getMessage());
        }
        return newDrgProposal();
    }

    private DrgProposal newDrgProposal() {
        Account account = _sessionController.getAccount();
        DrgProposal proposal = new DrgProposal();
        proposal.setAccountId(account.getAccountId());
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
        return proposal;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public DrgProposal getDrgProposal() {
        return _drgProposal;
    }

    // </editor-fold>
    @Override
    protected void addTopics() {
        addTopic(DrgProposalTabs.tabPPAddress.name(), Pages.DrgProposalEditAddress.URL());
        addTopic(DrgProposalTabs.tabPPProblem.name(), Pages.DrgProposalEditProblem.URL());
        addTopic(DrgProposalTabs.tabPPSolution.name(), Pages.DrgProposalEditSolution.URL());
        addTopic(DrgProposalTabs.tabPPPolicy.name(), Pages.DrgProposalEditPolicy.URL(), false);
    
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
                
                if ( getDrgProposal().getCategory() == DrgProposalCategory.CCL)
                {
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
    
    public void changeChangeMethodDiag(ValueChangeEvent e) {
//        if (!e.getNewValue().equals(e.getOldValue())) {
//            setVisibleChangeMethod((DrgProposalChangeMethod) e.getNewValue());
//        }
    }
    
    public void changeChangeMethodProc(ValueChangeEvent e) {
//        if (!e.getNewValue().equals(e.getOldValue())) {
//            setVisibleChangeMethod((DrgProposalChangeMethod) e.getNewValue());
//        }
    }

    private void setVisibleCategory(DrgProposalCategory cat) {
        if (cat == null) {
            return;
        }
      
        findTopic(DrgProposalTabs.tabPPCodes.name()).setVisible(cat.equals(DrgProposalCategory.CODES) || cat.equals(DrgProposalCategory.SYSTEM)  ||  cat.equals(DrgProposalCategory.CCL));
    }
    
    private void setVisibleChangeMethod(DrgProposalChangeMethod pcm) {
//        if (pcm == null) {
//            return;
//        }
      
        //findTopic(DrgProposalTabs.tabPPCodes.name()).setVisible(pcm.equals(DrgProposalCategory.CODES) || pcm.equals(DrgProposalCategory.SYSTEM));
    }

    public boolean isSystem() {
        return getDrgProposal().getCategory() == DrgProposalCategory.SYSTEM;
    }
    
    public String getCcl() {
        //return "display: inline-block; width: 49%;";
        return getDrgProposal().getCategory() == DrgProposalCategory.CCL ?  "display: none;" : "display: inline-block; width: 49%;";
    }
    
    public String getCcl2() {
        //return "display: inline-block; width: 49%;";
        return getDrgProposal().getCategory() == DrgProposalCategory.CCL ?  "display: inline-block; width: 98%; padding-right: 1%; border-right: solid 1px;" : "display: inline-block; width: 49%; padding-right: 1%; border-right: solid 1px;";
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
        _sessionController.getSearchController().bindSearchConsumer(this)
                .bindTargetPage(Pages.DrgProposalEditCoding.URL())
                .enableCodeType(CodeType.Diag).enableCodeType(CodeType.Proc).enableCodeType(CodeType.Drg)
                .bindCodeType(codeType).setCodeSystem(CodeType.Drg);
        return Pages.SearchCode.URL();
    }

    @Override
    public void addProcedure(String code) {
        String procCodes = getDrgProposal().getProcs();
        procCodes = (procCodes == null || procCodes.length() == 0) ? code : procCodes + "\r\n" + code;
        getDrgProposal().setProcs(procCodes);
    }

    @Override
    public void addDiagnosis(String code) {
        String diagCodes = getDrgProposal().getDiagCodes();
        diagCodes = (diagCodes == null || diagCodes.length() == 0) ? code : diagCodes + "\r\n" + code;
        getDrgProposal().setDiagCodes(diagCodes);
    }

    @Override
    public void addDrg(String code) {
        String drgCodes = getDrgProposal().getDrg();
        drgCodes = (drgCodes == null || drgCodes.length() == 0) ? code : drgCodes + "\r\n" + code;
        getDrgProposal().setDrg(drgCodes);
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
            if (type.equals(CodeType.Diag) && _diagnosisFacade.findDiagnosis(code, GlobalVars.DrgProposalSystemYear.getVal() - 2, GlobalVars.DrgProposalSystemYear.getVal() - 1).equals("")
                    || type.equals(CodeType.Proc) && _procedureFacade.findProcedure(code, GlobalVars.DrgProposalSystemYear.getVal() - 2, GlobalVars.DrgProposalSystemYear.getVal() - 1).equals("")) {
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

//    public void keyUp(AjaxBehaviorEvent event) {
//        HtmlInputText t = (HtmlInputText) event.getSource();
//        String currentId = t.getClientId();
//        List<ProcedureInfo> procedures = getDrgProposal().getProcedures();
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
//        return getDrgProposal().isAnonymousData() == null ? false : getDrgProposal().isAnonymousData();
//    }

//    public void setAnonymousData(boolean value) {
//        getDrgProposal().setAnonymousData(value);
//    }

    public boolean isDocumentAvailable() {
        return !getDrgProposal().getDocuments().isEmpty() || !getDrgProposal().getDocumentsOffline().isEmpty();
    }
    // </editor-fold>

    public boolean isReadOnly() {
        return getDrgProposal().getStatus() > 0;
    }
    
    public boolean isAnonymousData() {
        return getDrgProposal().isAnonymousData() == null ? false : getDrgProposal().isAnonymousData();
    }

    public void setAnonymousData(boolean value) {
        getDrgProposal().setAnonymousData(value);
    }

    public String save() {
        _drgProposal = _drgProposalFacade.saveDrgProposal(getDrgProposal());

        if (isValidId(getDrgProposal().getDrgProposalId())) {
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


    /**
     * This function seals a drgProposal if possible.
     * Sealing is possible, if all mandatory fields are fulfilled.
     * After sealing, the proposal can not be edited and is available for the InEK.
     *
     * @return
     */
    public String sealDrgProposal() {
        if (!drgProposalIsComplete()) {
            return null;
        }

        _drgProposal.setStatus(1);
        _drgProposal = _drgProposalFacade.saveDrgProposal(_drgProposal);

        if (isValidId(_drgProposal.getDrgProposalId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameDRG_PROPOSAL") + " " + _drgProposal.getExternalId());
            Utils.getFlash().put("targetPage", Pages.DrgProposalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_drgProposal));
            String msg = Utils.getMessage("msgConfirmDrgProposal").replace("\r", "").replace("\n", "\\r\\n");
            msg = String.format(msg, _drgProposal.getExternalId(), GlobalVars.DrgProposalSystemYear.getVal());
            String script = "alert ('" + msg + "');";
            _sessionController.setScript(script);
            return Pages.PrintView.URL();
        }
        return null;
    }

    public String takeDocuments() {
        DrgProposalController ppController = (DrgProposalController) _sessionController.getFeatureController(Feature.DRG_PROPOSAL);
        
        for (DrgProposalDocument doc : ppController.getDocuments()) {
            DrgProposalDocument existingDoc = findByName(doc.getName());
            if (existingDoc != null) {
                getDrgProposal().getDocuments().remove(existingDoc);
            }
            getDrgProposal().getDocuments().add(doc);
        }
        ppController.getDocuments().clear();
        
        return null;
    }

    public String deleteDocument(String name) {
        DrgProposalDocument existingDoc = findByName(name);
        if (existingDoc != null) {
            getDrgProposal().getDocuments().remove(existingDoc);
        }
        return null;
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
                _logger.log(Level.SEVERE, null, ex);
                return Pages.Error.URL();
            }
            facesContext.responseComplete();
        }
        return null;
    }

    private DrgProposalDocument findByName(String name) {
        for (DrgProposalDocument drgProposal : getDrgProposal().getDocuments()) {
            if (drgProposal.getName().equals(name)) {
                return drgProposal;
            }
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="CheckElements">
    String _msg = "";
    String _elementId = "";

    private boolean drgProposalIsComplete() {
        _msg = "";
        String newTopic = "";
        DrgProposal drgProposal = getDrgProposal();
        newTopic = checkField(newTopic, drgProposal.getName(), "lblAppellation", "form:name", DrgProposalTabs.tabPPAddress);
        newTopic = checkField(newTopic, drgProposal.getCategory() == null ? null : drgProposal.getCategory().name(), "lblCategory", "form:category", DrgProposalTabs.tabPPAddress);
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
            newTopic = checkField(newTopic, drgProposal.isAnonymousData() ? "true" : "", "lblAnonymousData", "form:anonymousData", DrgProposalTabs.tabPPDocuments);
           
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

    private String checkField(String newTopic, Integer value, Integer minValue, Integer maxValue, String msgKey, String elementId, DrgProposalTabs tab) {
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

}
