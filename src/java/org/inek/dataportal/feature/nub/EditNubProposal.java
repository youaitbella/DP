/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.nub;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.Account;
import org.inek.dataportal.entities.AccountAdditionalIK;
import org.inek.dataportal.entities.Customer;
import org.inek.dataportal.entities.NubProposal;
import org.inek.dataportal.enums.CodeType;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.GlobalVars;
import org.inek.dataportal.enums.NubStatus;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.facades.NubProposalFacade;
import org.inek.dataportal.facades.ProcedureFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.feature.peppproposal.EditPeppProposal;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.faceletvalidators.IkValidator;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@ConversationScoped
public class EditNubProposal extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("EditNubProposal");

    @Inject private ProcedureFacade _procedureFacade;
    @Inject private SessionController _sessionController;
    @Inject private NubProposalFacade _nubProposalFacade;
    @Inject private CustomerFacade _customerFacade;
    @Inject NubSessionTools _nubSessionTools;
    private String _conversationId;
    private NubProposal _nubProposal;
    private CooperativeRight _cooperativeRight;
    private CooperativeRight _supervisorRight;

    @Override
    protected void addTopics() {
        addTopic(NubProposalTabs.tabNubAddress.name(), Pages.NubEditAddress.URL());
        addTopic(NubProposalTabs.tabNubPage1.name(), Pages.NubEditPage1.URL());
        addTopic(NubProposalTabs.tabNubPage2.name(), Pages.NubEditPage2.URL());
        addTopic(NubProposalTabs.tabNubPage3.name(), Pages.NubEditPage3.URL());
        addTopic(NubProposalTabs.tabNubPage4.name(), Pages.NubEditPage4.URL());
    }

    enum NubProposalTabs {

        tabNubAddress,
        tabNubPage1,
        tabNubPage2,
        tabNubPage3,
        tabNubPage4;
    }
    // <editor-fold defaultstate="collapsed" desc="fields">
    private int _currentYear;
    private SelectItem[] _multiIks = new SelectItem[0];
    private String _singleKhName;
    // </editor-fold>

    public EditNubProposal() {
        //System.out.println("EditNubProposal");
        _currentYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public NubProposal getNubProposal() {
        return _nubProposal;
    }

    public int getCurrentYear() {
        return _currentYear;
    }

    public String getPatientsText() {
        return String.format(Utils.getMessage("lblNubPatients"), _currentYear + 1);
    }

    public String getPatientsPastText() {
        return String.format(Utils.getMessage("lblNubPatientsPast"), _currentYear - 1, _currentYear);
    }

    public void changedIk() {
        if (_nubProposal != null) {
            Customer c = _customerFacade.getCustomerByIK(_nubProposal.getIk());
            if (c.getName() == null || c.getName().equals("")) {
                if (_nubProposal.getIkName() == null || c.getName() == null) {
                    _nubProposal.setIkName("");
                }
                if (_nubProposal.getIkName().trim().equals("")) {
                    _nubProposal.setIkName(_sessionController.getAccount().getCompany());
                }
            } else {
                _nubProposal.setIkName(c.getName());
                if (_nubProposal.getIk().intValue() == _sessionController.getAccount().getIK().intValue()) {
                    _nubProposal.setIkName(_sessionController.getAccount().getCompany());
                }
            }
        }
    }

    public String getUserMaintenancePage() {
        return Pages.UserMaintenance.URL();
    }

    // </editor-fold>
    @PostConstruct
    private void init() {
        _conversationId = (String) Utils.getFlash().get("conversationId");
        Object ppId = Utils.getFlash().get("nubId");
        if (ppId == null) {
            _nubProposal = newNubProposal();
            ensureCooperativeRight(_nubProposal);
            ensureSupervisorRight(_nubProposal);
        } else {
            _nubProposal = loadNubProposal(ppId);
        }
        initMenuMultiIK();
        //ensureEmptyEntry(_peppProposal.getProcedures());
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

        if (_nubProposal.getIk() == null || !acc.getFullIkList().contains(_nubProposal.getIk().intValue())) {
            items.add(0, new SelectItem(""));
        }
        _multiIks = items.toArray(_multiIks);
    }

    private NubProposal loadNubProposal(Object ppId) {
        try {
            int id = Integer.parseInt("" + ppId);
            NubProposal nubProposal = _nubProposalFacade.find(id);

            if (hasSufficientRights(nubProposal)) {
                return nubProposal;
            }
        } catch (NumberFormatException ex) {
            _logger.info(ex.getMessage());
        }
        return newNubProposal();
    }

    private boolean hasSufficientRights(NubProposal nubProposal) {
        if (isOwnNub(nubProposal)) {
            return true;
        }
        ensureCooperativeRight(nubProposal);
        ensureSupervisorRight(nubProposal);
        return !_cooperativeRight.equals(CooperativeRight.None) || !_supervisorRight.equals(CooperativeRight.None);
    }

    private boolean isOwnNub(NubProposal nubProposal) {
        return _sessionController.isMyAccount(nubProposal.getAccountId());
    }

    private void ensureCooperativeRight(NubProposal nubProposal) {
        if (_cooperativeRight == null) {
            _cooperativeRight = _nubSessionTools.getCooperativeRight(nubProposal);
        }
    }

    private void ensureSupervisorRight(NubProposal nubProposal) {
        if (_supervisorRight == null) {
            _supervisorRight = _nubSessionTools.getSupervisorRight(nubProposal);
        }
    }

    private NubProposal newNubProposal() {
        Account account = _sessionController.getAccount();
        Integer ik = account.getIK();
        NubProposal proposal = getNubController().createNubProposal();
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

    public void checkIK(FacesContext context, UIComponent component, Object value) {
        String ik = ((String) String.valueOf(value)).trim();
        if (!IkValidator.isValidIK(ik)) {
            String msg = Utils.getMessage("errIK");
            throw new ValidatorException(new FacesMessage(msg));
        }
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
            Integer tmp = Integer.parseInt((String) _nubProposal.getPostalCode());
            if (tmp > 99999) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            String msg = Utils.getMessage("errPostalCode");
            return false;
        }
        return true;
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
        return "/common/SearchCode";
    }

    @Override
    public void addProcedure(String code) {
        String procCodes = getNubProposal().getProcs();
        procCodes = (procCodes == null || procCodes.length() == 0) ? code : procCodes + "\r\n" + code;
        getNubProposal().setProcs(procCodes);
    }

    @Override
    public void addDrg(String code) {
        String drgCodes = getNubProposal().getDrgs();
        drgCodes = (drgCodes == null || drgCodes.length() == 0) ? code : drgCodes + "\r\n" + code;
        getNubProposal().setDrgs(drgCodes);
    }

    public void checkProcedureCodes(FacesContext context, UIComponent component, Object value) {
        String codes[] = value.toString().split("\\s");
        StringBuilder invalidCodes = new StringBuilder();
        for (String code : codes) {
            if (_procedureFacade.findProcedure(code, GlobalVars.PeppProposalCodeFirstYear.getVal(), GlobalVars.PeppProposalCodeLastYear.getVal()).equals("")) {
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

    public boolean checkProxyIKs(String value) {
        String iks[] = value.toString().split("\\s");
        StringBuilder invalidIKs = new StringBuilder();
        for (String ik : iks) {
            if (!IkValidator.isValidIK(ik)) {
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
            FacesMessage msg = new FacesMessage(invalidIKs.toString());
            return false;
        }
        return true;
    }

    // </editor-fold>
    public String save() {
        if (!check4validSession()) {
            return Pages.InvalidConversation.URL();
        }
        _nubProposal.setLastChangedBy(_sessionController.getAccountId());
        _nubProposal = _nubProposalFacade.saveNubProposal(_nubProposal);

        if (isValidId(_nubProposal.getNubId())) {
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

    public boolean isOwnNub() {
        return isOwnNub(_nubProposal);
    }

    public boolean isReadOnly() {
        return isReadOnly(false);
    }

    public boolean isReadOnly(boolean laxCheck) {
        if (_nubProposal.getStatus().getValue() >= NubStatus.Provided.getValue()) {
            // is sealed
            return true;
        }
        if (isOwnNub()) {
            // own nub depends on status
            return _nubProposal.getStatus().getValue() >= NubStatus.ApprovalRequested.getValue();
        }
        if (_supervisorRight == CooperativeRight.ReadWriteCompletedSealSupervisor || _supervisorRight == CooperativeRight.ReadWriteSealSupervisor) {
            // supervisor write preceeds
            return false;
        }
        return _cooperativeRight == CooperativeRight.ReadOnly
                || !laxCheck && (_cooperativeRight == CooperativeRight.ReadCompletedSealSupervisor || _supervisorRight == CooperativeRight.ReadCompletedSealSupervisor);
    }

    public boolean isRejectedNub() {
        return NubStatus.Rejected.getValue() == _nubProposal.getStatus().getValue();
    }

    public boolean isSealEnabled() {
        ensureSupervisorRight(_nubProposal);
        boolean enabled;
        if (isOwnNub()) {
            if (_nubProposal.getIk() == null) {
                return false;
            }
            if (_nubSessionTools.getSealOwnNub().get(_nubProposal.getIk()) == null) {
                return false;
            }
            enabled = _nubSessionTools.getSealOwnNub().get(_nubProposal.getIk());
        } else {
            enabled = _cooperativeRight == CooperativeRight.ReadWriteSeal
                    || _cooperativeRight == CooperativeRight.ReadCompletedSealSupervisor
                    || _cooperativeRight == CooperativeRight.ReadWriteCompletedSealSupervisor
                    || _cooperativeRight == CooperativeRight.ReadWriteSealSupervisor
                    || !_supervisorRight.equals(CooperativeRight.None);
        }

        return !isReadOnly(true) && enabled;
    }

    public boolean isApprovalRequestEnabled() {
        ensureSupervisorRight(_nubProposal);
        boolean enabled = false;
        if (_sessionController.isMyAccount(_nubProposal.getAccountId())) {
            if (_nubProposal.getIk() == null) {
                return false;
            }
            if (_nubSessionTools.getSealOwnNub().get(_nubProposal.getIk()) == null) {
                return false;
            }
            enabled = !_nubSessionTools.getSealOwnNub().get(_nubProposal.getIk())
                    && !_nubProposal.getStatus().equals(NubStatus.ApprovalRequested)
                    && _supervisorRight.equals(CooperativeRight.None);
        }
        return !isReadOnly() && enabled;
    }

    /**
     * requests sealing of a formal request if the form is completely full
     * filled, this function displays a confirmation dialog confirming with "ok"
     * performs a call to seal
     *
     * @return
     */
    public String requestSeal() {
        if (!check4validSession()) {
            return Pages.InvalidConversation.URL();
        }
        if (!requestIsComplete()) {
            return getActiveTopic().getOutcome();
        }
        String script = "if (confirm ('" + Utils.getMessage("msgConfirmSeal") + "')) {document.getElementById('form:seal').click();}";
        _sessionController.setScript(script);
        return null;
    }

    /**
     * this function seals a request usually it can only be called is the
     * request to seal is confirmed. As a precaution, it performs some checks
     * which have been done in requestSeal.
     *
     * @return
     */
    public String seal() {
        if (!check4validSession() || !requestIsComplete()) {
            return Pages.Error.URL();
        }
        if (_nubProposal.getStatus().getValue() >= 10) {
            return Pages.Error.URL();
        }

        _nubProposal.setStatus(_nubProposal.getStatus() == NubStatus.Rejected ? NubStatus.ReProvided : NubStatus.Provided);
        _nubProposal.setSealedBy(_sessionController.getAccountId());
        _nubProposal = _nubProposalFacade.saveNubProposal(_nubProposal);
        if (isValidId(_nubProposal.getNubId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameNUB"));
            Utils.getFlash().put("targetPage", Pages.NubSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_nubProposal));
            _sessionController.endConversation();
            return Pages.PrintView.URL();
        }
        return null;
    }

    public String requestApproval() {
        if (!check4validSession()) {
            return Pages.InvalidConversation.URL();
        }
        if (!requestIsComplete()) {
            return getActiveTopic().getOutcome();
        }
        String script = "if (confirm ('" + Utils.getMessage("msgRequestApproval") + "')) {document.getElementById('form:confirmApprovalRequest').click();}";
        _sessionController.setScript(script);
        return null;
    }

    public String confirmApprovalRequest() {
        if (!check4validSession() || !requestIsComplete()) {
            return Pages.Error.URL();
        }
        if (_nubProposal.getStatus().getValue() >= 10) {
            return Pages.Error.URL();
        }

        _nubProposal.setStatus(NubStatus.ApprovalRequested);
        _nubProposal.setLastChangedBy(_sessionController.getAccountId());
        _nubProposal = _nubProposalFacade.saveNubProposal(_nubProposal);

        return "";
    }

    /**
     * checks, whether the session is still valid
     *
     * @return
     */
    private boolean check4validSession() {
        return _conversationId.equals(_sessionController.getConversationId());
    }

    // <editor-fold defaultstate="collapsed" desc="CheckElements">
    String _msg = "";
    String _elementId = "";

    private boolean requestIsComplete() {
        _msg = "";
        String newTopic = "";
        //newTopic = checkField(newTopic, _nubProposal.getCategory() == null ? null : _nubProposal.getCategory().name(), "lblCategory", "form:category", NubProposalTabs.tabNubAddress);
        //newTopic = checkField(newTopic, _nubProposal.getInstitute(), "lblPeppProposalingInstitute", "form:institute", NubProposalTabs.tabNubAddress);
        String ik = "";
        if (_nubProposal.getIk() != null) {
            ik = _nubProposal.getIk().toString();
        }
        newTopic = checkField(newTopic, ik, "lblIK", "form:ikMulti", NubProposalTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubProposal.getGender(), 1, 2, "lblSalutation", "form:cbxGender", NubProposalTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubProposal.getFirstName(), "lblFirstName", "form:firstname", NubProposalTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubProposal.getLastName(), "lblLastName", "form:lastname", NubProposalTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubProposal.getStreet(), "lblStreet", "form:street", NubProposalTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubProposal.getPostalCode(), "lblPostalCode", "form:zip", NubProposalTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubProposal.getTown(), "lblTown", "form:town", NubProposalTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubProposal.getPhone(), "lblPhone", "form:phone", NubProposalTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubProposal.getEmail(), "lblMail", "form:email", NubProposalTabs.tabNubAddress);
        newTopic = checkField(newTopic, _nubProposal.getName(), "lblAppellation", "form:nubName", NubProposalTabs.tabNubPage1);
        newTopic = checkField(newTopic, _nubProposal.getDescription(), "lblNubDescription", "form:nubDescription", NubProposalTabs.tabNubPage1);
        newTopic = checkField(newTopic, _nubProposal.getIndication(), "lblIndication", "form:nubIndic", NubProposalTabs.tabNubPage2);
        newTopic = checkField(newTopic, _nubProposal.getReplacement(), "lblNubReplacementPrint", "form:nubReplacement", NubProposalTabs.tabNubPage2);
        newTopic = checkField(newTopic, _nubProposal.getWhatsNew(), "lblWhatsNew", "form:nubWhatsNew", NubProposalTabs.tabNubPage2);
        newTopic = checkField(newTopic, _nubProposal.getInHouseSince(), "lblMethodInHouse", "form:nubInHouse", NubProposalTabs.tabNubPage3);
        newTopic = checkField(newTopic, _nubProposal.getPatientsLastYear(), "lblPatientsLastYear", "form:patientsLastYear", NubProposalTabs.tabNubPage3);
        newTopic = checkField(newTopic, _nubProposal.getPatientsThisYear(), "lblPatientsThisYear", "form:patientsThisYear", NubProposalTabs.tabNubPage3);
        newTopic = checkField(newTopic, _nubProposal.getPatientsFuture(), "lblPatientsFuture", "form:patientsFuture", NubProposalTabs.tabNubPage3);
        newTopic = checkField(newTopic, _nubProposal.getAddCosts(), "lblAddCosts", "form:nubAddCost", NubProposalTabs.tabNubPage4);
        newTopic = checkField(newTopic, _nubProposal.getWhyNotRepresented(), "lblWhyNotRepresented", "form:nubNotRepresented", NubProposalTabs.tabNubPage4);
        if (_nubProposal.getRoleId() < 0) {
            _msg = Utils.getMessage("lblContactRole");
            newTopic = NubProposalTabs.tabNubAddress.name();
        }
        if (!checkProxyIKs(_nubProposal.getProxyIKs())) {
            _msg = Utils.getMessage("lblErrorProxyIKs");
            newTopic = NubProposalTabs.tabNubAddress.name();
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

    private String checkField(String newTopic, String value, String msgKey, String elementId, NubProposalTabs tab) {
        if (Utils.isNullOrEmpty(value)) {
            _msg += "\\r\\n" + Utils.getMessage(msgKey);
            if (newTopic.isEmpty()) {
                newTopic = tab.name();
                _elementId = elementId;
            }
        }
        return newTopic;
    }

    private String checkField(String newTopic, Integer value, Integer minValue, Integer maxValue, String msgKey, String elementId, NubProposalTabs tab) {
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
        if (!check4validSession()) {
            return Pages.InvalidConversation.URL();
        }
        downloadDocument(getNubController().createTemplate(_nubProposal));
        return null;
    }

    public String downloadDocument(String document) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        try {
            byte[] buffer = document.getBytes("UTF-8");
            externalContext.setResponseHeader("Content-Type", "text/plain");
            externalContext.setResponseHeader("Content-Length", "" + buffer.length);
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + _nubProposal.getName() + ".nub\"");
            ByteArrayInputStream is = new ByteArrayInputStream(buffer);
            copyStream(is, externalContext.getResponseOutputStream());

        } catch (IOException ex) {
            Logger.getLogger(EditPeppProposal.class
                    .getName()).log(Level.SEVERE, null, ex);
            return Pages.Error.URL();
        }
        facesContext.responseComplete();
        return null;
    }

    private void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[8192];
        int n;
        while ((n = is.read(buffer)) != -1) {
            os.write(buffer);
        }
    }

}
