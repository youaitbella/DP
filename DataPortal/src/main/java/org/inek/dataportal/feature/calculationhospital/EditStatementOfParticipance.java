/*
 * To copyForResend this license header, choose License Headers in Project Properties.
 * To copyForResend this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.CalcContact;
import org.inek.dataportal.entities.calc.StatementOfParticipance;
import org.inek.dataportal.entities.icmt.Customer;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.CalcFacade;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.helper.structures.MessageContainer;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped
public class EditStatementOfParticipance extends AbstractEditController {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger _logger = Logger.getLogger("EditStatementOfParticipance");

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private CalcFacade _calcFacade;
    @Inject ApplicationTools _appTools;
    @Inject private CustomerFacade _customerFacade;

    private StatementOfParticipance _statement;

    enum StatementOfParticipanceTabs {
        tabStatementOfParticipanceAddress,
        tabStatementOfParticipanceStatements
    }
    // </editor-fold>

    @PostConstruct
    private void init() {
        Object id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
            return;
        } else if (id.toString().equals("new")) {
            if (!_appTools.isEnabled(ConfigKey.IsStatemenOfParticipanceCreateEnabled)) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            _statement = newStatementOfParticipance();
        } else {
            _statement = loadStatementOfParticipance(id);
        }
        enableDisableStatementPage();
    }

    private void ensureContacts(StatementOfParticipance statement) {
        if (statement.getContacts().stream().filter(c -> !c.isConsultant()).count() == 0) {
            statement.getContacts().add(new CalcContact());
        }
        if (statement.getContacts().stream().filter(c -> c.isConsultant()).count() == 0) {
            CalcContact contact = new CalcContact();
            contact.setConsultant(true);
            statement.getContacts().add(contact);
        }
    }

    private StatementOfParticipance loadStatementOfParticipance(Object idObject) {
        try {
            int id = Integer.parseInt("" + idObject);
            StatementOfParticipance statement = _calcFacade.findStatementOfParticipance(id);
            if (_cooperationTools.isAllowed(Feature.CALCULATION_HOSPITAL, statement.getStatus(), statement.getAccountId())) {
                return statement;
            }
        } catch (NumberFormatException ex) {
            _logger.info(ex.getMessage());
        }
        return newStatementOfParticipance();
    }

    private StatementOfParticipance newStatementOfParticipance() {
        int ik = getIks().size() == 1 ? (int) getIks().get(0).getValue() : 0;
        int year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
        return retrievePriorData(ik, year);
    }

    private StatementOfParticipance retrievePriorData(int ik, int year) {
        /* 
        This code might be used, if we load the data from a former StatementOfParticipance
        Although we did this in a first approach, we decided to load the data from ICMT, at least for the first time (2017).
        If we load it from ICMT in 2018, then get the code of the pre 20170118 version.
         */

        StatementOfParticipance statement = new StatementOfParticipance();
        statement.setIk(ik);
        statement.setDataYear(year);
        statement.setAccountId(_sessionController.getAccountId());
        List<Object[]> currentData = _calcFacade.retrieveCurrentStatementOfParticipanceData(ik);
        if (currentData.size() == 1) {
            Object[] obj = currentData.get(0);
            String domain = (String) obj[0];
            String drgKvm = (String) obj[1];
            String drgMultiyear = (String) obj[2];
            String psyKvm = (String) obj[3];
            String psyMultiyear = (String) obj[4];
            boolean isDrg = (boolean) obj[5];
            boolean isPsy = (boolean) obj[6];
            statement.setObligatory(domain.contains("obligatory"));
            if (statement.isObligatory()) {
                statement.setDrgCalc(isDrg);
                statement.setPsyCalc(isPsy);
            } else {
                statement.setDrgCalc(domain.contains("DRG"));
                statement.setClinicalDistributionModelDrg(drgKvm.equals("T") ? 1 : 0);
                statement.setMultiyearDrg(Integer.parseInt(drgMultiyear));
                statement.setPsyCalc(domain.contains("PSY"));
                statement.setClinicalDistributionModelPsy(psyKvm.equals("T") ? 1 : 0);
                statement.setMultiyearPsy(Integer.parseInt(psyMultiyear));
                statement.setInvCalc(domain.contains("INV"));
                statement.setTpgCalc(domain.contains("TPG"));
                statement.setObdCalc(domain.contains("OBD"));
            }
            _sessionController.setScript("alert('Zu Ihrer Unterstützung wurden die aktuell im InEK vorliegenden Informationen bereits in den Dialog geladen. "
                    + "Bevor Sie die Daten an das InEK senden, überprüfen Sie diese bitte auf eventuelle Änderungen.');");
        }

        statement.setContacts(_calcFacade.retrieveCurrentContacts(ik));
        return statement;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public StatementOfParticipance getStatement() {
        return _statement;
    }

    public void setStatement(StatementOfParticipance statement) {
        _statement = statement;
    }

    public List<CalcContact> getContacts() {
        ensureContacts(_statement);
        return _statement.getContacts().stream().filter(c -> !c.isConsultant()).collect(Collectors.toList());
    }

    public List<CalcContact> getConsultants() {
        ensureContacts(_statement);
        return _statement.getContacts().stream().filter(c -> c.isConsultant()).collect(Collectors.toList());
    }
    // </editor-fold>

    @Override
    protected void addTopics() {
        addTopic(StatementOfParticipanceTabs.tabStatementOfParticipanceAddress.name(), Pages.StatementOfParticipanceEditAddress.URL());
        addTopic(StatementOfParticipanceTabs.tabStatementOfParticipanceStatements.name(), Pages.StatementOfParticipanceEditStatements.URL());
    }

    public void enableDisableStatementPage() {
        boolean enable = !_statement.isObligatory() || _statement.getObligatoryCalcType() > 1;
        findTopic(StatementOfParticipanceTabs.tabStatementOfParticipanceStatements.name()).setVisible(enable);
    }

    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isReadOnly() {
        return _cooperationTools.isReadOnly(Feature.CALCULATION_HOSPITAL, _statement.getStatus(), _statement.getAccountId(), _statement.getIk());
    }

    public String save() {
        setModifiedInfo();
        _statement = _calcFacade.saveStatementOfParticipance(_statement);

        if (isValidId(_statement.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    private void setModifiedInfo() {
        _statement.setLastChanged(Calendar.getInstance().getTime());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsStatemenOfParticipanceSendEnabled)) {
            return false;
        }
        return _cooperationTools.isSealedEnabled(Feature.CALCULATION_HOSPITAL, _statement.getStatus(), _statement.getAccountId());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsStatemenOfParticipanceSendEnabled)) {
            return false;
        }
        return _cooperationTools.isApprovalRequestEnabled(Feature.CALCULATION_HOSPITAL, _statement.getStatus(), _statement.getAccountId());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsStatemenOfParticipanceSendEnabled)) {
            return false;
        }
        return _cooperationTools.isRequestCorrectionEnabled(Feature.CALCULATION_HOSPITAL, _statement.getStatus(), _statement.getAccountId());
    }

    public boolean isTakeEnabled() {
        return _cooperationTools.isTakeEnabled(Feature.CALCULATION_HOSPITAL, _statement.getStatus(), _statement.getAccountId());
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
        populateDefaultsForUnreachableFields();
        if (!statementIsComplete()) {
            return getActiveTopic().getOutcome();
        }
        _statement.setStatus(WorkflowStatus.Provided);
        setModifiedInfo();
        _statement = _calcFacade.saveStatementOfParticipance(_statement);
        createTransferFile(_statement);

        if (isValidId(_statement.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL"));
            Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_statement));
            return Pages.PrintView.URL();
        }
        return "";
    }

    private void populateDefaultsForUnreachableFields() {
        // Some input fields can't be reached depending on other fields.
        // But they might contain elder values, which became obsolte by setting the other field 
        // Such fields will be removed or populated with default values
        if (!_statement.isWithConsultant()) {
            _statement.setConsultantCompany("");
            _statement.setConsultantSendMail(false);
            _statement.getContacts();
            List<CalcContact> consultContacts = _statement.getContacts().stream().filter(c -> c.isConsultant()).collect(Collectors.toList());
            consultContacts.forEach((consultContact) -> {
                _statement.getContacts().remove(consultContact);
            });
        }
        for (CalcContact contact : _statement.getContacts()) {
            if (!_statement.isDrgCalc()) {
                contact.setDrg(false);
            }
            if (!_statement.isPsyCalc()) {
                contact.setPsy(false);
            }
            if (!_statement.isInvCalc()) {
                contact.setInv(false);
            }
            if (!_statement.isTpgCalc()) {
                contact.setTpg(false);
            }
            if (!_statement.isObdCalc()) {
                contact.setObd(false);
            }
        }
        if (_statement.isObligatory() && _statement.getObligatoryCalcType() < 2) {
            _statement.setMultiyearDrg(0);
            _statement.setClinicalDistributionModelDrg(-1);
            _statement.setMultiyearPsy(0);
            _statement.setClinicalDistributionModelPsy(-1);
        }
        if (_statement.getMultiyearDrg() < 4) {
            _statement.setMultiyearDrgText("");
        }
        if (_statement.getMultiyearPsy() < 4) {
            _statement.setMultiyearPsyText("");
        }
    }

    public boolean isCopyForResendAllowed() {
        if (_statement.getStatusId() < 10 || !_appTools.isEnabled(ConfigKey.IsStatemenOfParticipanceSendEnabled)) {
            return false;
        }
        return !_calcFacade.existActiveStatementOfParticipance(_statement.getIk());
    }

    public void copyForResend() {
        _statement.setId(-1);
        _statement.setStatus(WorkflowStatus.New);
        _statement.setAccountId(_sessionController.getAccountId());
        for (CalcContact contact : _statement.getContacts()) {
            contact.setId(-1);
            contact.setStatementOfParticipanceId(-1);
        }
    }

    private boolean statementIsComplete() {
        MessageContainer message = composeMissingFieldsMessage(_statement);
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

    public MessageContainer composeMissingFieldsMessage(StatementOfParticipance statement) {
        MessageContainer message = new MessageContainer();

        String ik = statement.getIk() < 0 ? "" : "" + statement.getIk();
        checkField(message, ik, "lblIK", "sop:ikMulti", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress);

        if (_statement.isObligatory()) {
            if (statement.getContacts().stream().filter(c -> !c.isConsultant()).count() == 0) {
                applyMessageValues(message, "lblNeedContact", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contact");
            }
            if (_statement.getObligatoryCalcType() < 1) {
                applyMessageValues(message, "lblObligatoryCalcType", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:calcType");
            }
        } else {
            if (statement.isDrgCalc() && !statement.getContacts().stream().anyMatch(c -> c.isDrg())) {
                applyMessageValues(message, "lblNeedContactDrg", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contact");
            }
            if (statement.isPsyCalc() && !statement.getContacts().stream().anyMatch(c -> c.isPsy())) {
                applyMessageValues(message, "lblNeedContactPsy", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contact");
            }
            if (statement.isInvCalc() && !statement.getContacts().stream().anyMatch(c -> c.isInv())) {
                applyMessageValues(message, "lblNeedContactInv", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contact");
            }
            if (statement.isTpgCalc() && !statement.getContacts().stream().anyMatch(c -> c.isTpg())) {
                applyMessageValues(message, "lblNeedContactTpg", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contact");
            }
            if (statement.isObdCalc() && !statement.getContacts().stream().anyMatch(c -> c.isObd())) {
                applyMessageValues(message, "lblNeedContactObd", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contact");
            }
        }
        if (statement.getContacts().stream()
                .filter(c -> !c.isEmpty())
                .anyMatch(c -> c.getFirstName() == null || c.getFirstName().isEmpty()
                || c.getLastName() == null || c.getLastName().isEmpty()
                || c.getMail() == null || c.getMail().isEmpty()
                || c.getPhone() == null || c.getPhone().isEmpty())) {
            applyMessageValues(message, "msgContactIncomplete", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contact");
        }
        if (statement.isWithConsultant()) {
            checkField(message, statement.getConsultantCompany(), "lblNameConsultant", "sop:consultantCompany", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress);
        }
        if(statement.isConsultantSendMail()){
            List<CalcContact> consultantContacts = _statement.getContacts().stream().filter(c -> c.isConsultant()).collect(Collectors.toList());
            if (consultantContacts.isEmpty() || consultantContacts.get(0).isEmpty()) {
                applyMessageValues(message, "lblNeedContactConsultant", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contactConsultant");
            }
        }

        if (statement.isDrgCalc() && (!statement.isObligatory() || statement.getObligatoryCalcType() == 2)) {
            checkField(message, statement.getClinicalDistributionModelDrg(), 0, 1,
                    "lblStatementSingleCostAttributionDrg", "sop:clinicalDistributionModelDrg",
                    StatementOfParticipanceTabs.tabStatementOfParticipanceStatements);
            checkField(message, statement.getMultiyearDrg(), 1, 4,
                    "lblQuestionOverlayer", "sop:multiyearDrg",
                    StatementOfParticipanceTabs.tabStatementOfParticipanceStatements);
            if (statement.getMultiyearDrg() == 4 && statement.getMultiyearDrgText().isEmpty()) {
                applyMessageValues(message, "lblDescriptionOfAlternative", StatementOfParticipanceTabs.tabStatementOfParticipanceStatements, "form");
            }
        }

        if (statement.isPsyCalc() && (!statement.isObligatory() || statement.getObligatoryCalcType() == 2)) {
            checkField(message, statement.getClinicalDistributionModelDrg(), 0, 1,
                    "lblStatementSingleCostAttributionPsy", "sop:clinicalDistributionModelPsy",
                    StatementOfParticipanceTabs.tabStatementOfParticipanceStatements);
            checkField(message, statement.getMultiyearPsy(), 1, 4,
                    "lblQuestionOverlayer", "sop:multiyearPsy",
                    StatementOfParticipanceTabs.tabStatementOfParticipanceStatements);
            if (statement.getMultiyearPsy() == 4 && statement.getMultiyearPsyText().isEmpty()) {
                applyMessageValues(message, "lblDescriptionOfAlternative", StatementOfParticipanceTabs.tabStatementOfParticipanceStatements, "form");
            }
        }
        return message;
    }

    private void checkField(MessageContainer message, String value, String msgKey, String elementId, StatementOfParticipanceTabs tab) {
        if (Utils.isNullOrEmpty(value)) {
            applyMessageValues(message, msgKey, tab, elementId);
        }
    }

    private void checkField(MessageContainer message, Integer value, Integer minValue, Integer maxValue, String msgKey, String elementId, StatementOfParticipanceTabs tab) {
        if (value == null
                || minValue != null && value < minValue
                || maxValue != null && value > maxValue) {
            applyMessageValues(message, msgKey, tab, elementId);
        }
    }

    private void applyMessageValues(MessageContainer message, String msgKey, StatementOfParticipanceTabs tab, String elementId) {
        message.setMessage(message.getMessage() + "\\r\\n" + Utils.getMessage(msgKey));
        if (message.getTopic().isEmpty()) {
            message.setTopic(tab.name());
            message.setElementId(elementId);
        }
    }

    public String requestApproval() {
        if (!statementIsComplete()) {
            return null;
        }
        _statement.setStatus(WorkflowStatus.ApprovalRequested);
        setModifiedInfo();
        _statement = _calcFacade.saveStatementOfParticipance(_statement);
        return "";
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        _statement.setAccountId(_sessionController.getAccountId());
        setModifiedInfo();
        _statement = _calcFacade.saveStatementOfParticipance(_statement);
        return "";
    }

    private void createTransferFile(StatementOfParticipance statement) {
        File dir = new File(_sessionController.getApplicationTools().readConfig(ConfigKey.FolderRoot), _sessionController.getApplicationTools().readConfig(ConfigKey.FolderUpload));
        File file;
        Date ts;
        do {
            ts = Calendar.getInstance().getTime();
            file = new File(dir, "Transfer" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(ts) + ".txt");
        } while (file.exists());

        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file))) {
            if (_sessionController.getAccount().isReportViaPortal()) {
                pw.println("Account.Mail=" + _sessionController.getAccount().getEmail());
            }
            pw.println("From=" + _sessionController.getAccount().getEmail());
            pw.println("Received=" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(ts));
            pw.println("Subject=Teilnahmeerklärung_" + statement.getIk());

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String json = mapper.writeValueAsString(statement);
            pw.println("Content=" + json);

            pw.flush();
        } catch (FileNotFoundException | JsonProcessingException ex) {
            throw new IllegalStateException(ex);
        } finally {
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Tab Address">
    List<SelectItem> _iks;

    public List<SelectItem> getIks() {
        Account account = _sessionController.getAccount();
        Set<Integer> iks = _calcFacade.obtainIks4NewStatementOfParticipance(account.getId(), Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        if (_statement != null && _statement.getIk() > 0) {
            iks.add(_statement.getIk());
        }

        List<SelectItem> items = new ArrayList<>();
        for (int ik : iks) {
            items.add(new SelectItem(ik));
        }
        return items;
    }

    public String getHospitalInfo() {
        Customer c = _customerFacade.getCustomerByIK(_statement.getIk());
        if (c == null || c.getName() == null) {
            return "";
        }
        return c.getName() + ", " + c.getTown();
    }

    public void ikChanged() {
        if (_statement.getId() == -1) {
            // paranoid check. usually the ik cannot be changed, once the statement is stored
            _statement = retrievePriorData(_statement.getIk(), _statement.getDataYear());
            enableDisableStatementPage();
        }
    }

    public void addContact() {
        _statement.getContacts().add(new CalcContact());
    }

    public void deleteContact(CalcContact contact) {
        _statement.getContacts().remove(contact);
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Tab Statements">
// </editor-fold>
}
