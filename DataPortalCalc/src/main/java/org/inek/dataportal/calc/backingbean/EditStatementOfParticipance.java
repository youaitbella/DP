/*
 * To copyForResend this license header, choose License Headers in Project Properties.
 * To copyForResend this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.backingbean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.calc.entities.sop.CalcContact;
import org.inek.dataportal.calc.entities.sop.StatementOfParticipance;
import org.inek.dataportal.calc.facades.CalcSopFacade;
import org.inek.dataportal.calc.facades.IcmtUpdater;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.data.adm.InekRole;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.helper.structures.MessageContainer;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.utils.DocumentationUtil;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped
public class EditStatementOfParticipance extends AbstractEditController {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger LOGGER = Logger.getLogger("EditStatementOfParticipance");

    @Inject
    private AccessManager _accessManager;
    @Inject
    private SessionController _sessionController;
    @Inject
    private CalcSopFacade _calcFacade;
    @Inject
    private IcmtUpdater _icmtUpdater;
    @Inject
    private ApplicationTools _appTools;
    @Inject
    private AccountFacade _accFacade;

    private StatementOfParticipance _statement;
    private boolean _enableDrgCalc;
    private boolean _enablePsyCalc;
    private boolean _enableInvCalc;
    private boolean _obligatorDrg;
    private boolean _obligatorPsy;
    private boolean _obligatorInv;

    private boolean statementIsNoParticipation() {
        return !_statement.isDrgCalc() && !_statement.isPsyCalc() && !_statement.isInvCalc() && !_statement.isTpgCalc() && !_statement.
                isObdCalc();
    }

    enum StatementOfParticipanceTabs {
        tabStatementOfParticipanceAddress,
        tabStatementOfParticipanceStatements
    }
    // </editor-fold>

    /**
     * @return the _obligatorInv
     */
    public boolean isObligatorInv() {
        return _obligatorInv;
    }

    /**
     * @param obligatorInv the _obligatorInv to set
     */
    public void setObligatorInv(boolean obligatorInv) {
        this._obligatorInv = obligatorInv;
    }

    public boolean isEnableDrgCalc() {
        return _enableDrgCalc;
    }

    public void setEnableDrgCalc(boolean enableDrgCalc) {
        this._enableDrgCalc = enableDrgCalc;
    }

    public boolean isEnablePsyCalc() {
        return _enablePsyCalc;
    }

    public void setEnablePsyCalc(boolean enablePsyCalc) {
        this._enablePsyCalc = enablePsyCalc;
    }

    public boolean isEnableInvCalc() {
        return _enableInvCalc;
    }

    public void setEnableInvCalc(boolean enableInvCalc) {
        this._enableInvCalc = enableInvCalc;
    }

    /**
     * @return the _obligatorDrg
     */
    public boolean isObligatorDrg() {
        return _obligatorDrg;
    }

    /**
     * @param obligatorDrg the _obligatorDrg to set
     */
    public void setObligatorDrg(boolean obligatorDrg) {
        this._obligatorDrg = obligatorDrg;
    }

    /**
     * @return the _obligatorPsy
     */
    public boolean isObligatorPsy() {
        return _obligatorPsy;
    }

    /**
     * @param obligatorPsy the _obligatorPsy to set
     */
    public void setObligatorPsy(boolean obligatorPsy) {
        this._obligatorPsy = obligatorPsy;
    }

    @PostConstruct
    private void init() {
        Object id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
            _statement = newStatementOfParticipance();
            return;
        } else if ("new".equals(id.toString())) {
            if (!_appTools.isEnabled(ConfigKey.IsStatemenOfParticipanceCreateEnabled)) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            _statement = newStatementOfParticipance();
        } else {
            _statement = loadStatementOfParticipance(id);
        }
        enableDisablePageElements();
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
            if (_accessManager.isAccessAllowed(Feature.CALCULATION_HOSPITAL, statement.getStatus(),
                    statement.getAccountId(), statement.getIk())) {
                updateObligatorySetting(statement);
                return statement;
            }
        } catch (NumberFormatException ex) {
            LOGGER.info(ex.getMessage());
        }
        return newStatementOfParticipance();
    }

    private StatementOfParticipance newStatementOfParticipance() {
        int ik = getIks().size() == 1 ? (int) getIks().get(0).getValue() : 0;
        int year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
        return retrievePriorData(ik, year, _sessionController.getAccount().getId());
    }

    @SuppressWarnings("CyclomaticComplexity")
    public StatementOfParticipance retrievePriorData(int ik, int year, int accountId) {
        StatementOfParticipance statement = new StatementOfParticipance();
        statement.setIk(ik);
        statement.setDataYear(year);
        statement.setAccountId(accountId);
        List<Object[]> currentData = _calcFacade.retrieveCurrentStatementOfParticipanceData(ik, year - 1);
        if (currentData.size() == 1) {
            Object[] obj = currentData.get(0);
            String domain = (String) obj[0];
            String drgMultiyear = obj[2].toString();
            String psyMultiyear = obj[4].toString();
            boolean isDrg = (boolean) obj[5];
            boolean isPsy = (boolean) obj[6];
            statement.setObligatory(domain.contains("obligatory"));
            if (statement.isObligatory()) {
                statement.setObligatoryFollowingYears(_calcFacade.isObligatoryFollowingYear(ik, year));
                statement.setInvCalc(_calcFacade.isObligateInCalcType(ik, year, 4) || _calcFacade.
                        isParticipationInCalcType(ik, year - 1, 4));
                setObligatorDrg(_calcFacade.isObligateInCalcType(ik, year, 1));
                setObligatorPsy(_calcFacade.isObligateInCalcType(ik, year, 3));
                setObligatorInv(_calcFacade.isObligateInCalcType(ik, year, 4));

                if (!statement.isObligatoryFollowingYears() && (isObligatorDrg() || isObligatorPsy())) {
                    statement.setObligatoryCalcType(2);
                }

                statement.setDrgCalc(isDrg && (isObligatorDrg() || isObligatorPsy() || _calcFacade.
                        isParticipationInCalcType(ik, year - 1, 1)));
                statement.setPsyCalc(isPsy && (isObligatorDrg() || isObligatorPsy() || _calcFacade.
                        isParticipationInCalcType(ik, year - 1, 3)));
                statement.setTpgCalc(_calcFacade.isParticipationInCalcType(ik, year - 1, 5));
                statement.setObdCalc(_calcFacade.isParticipationInCalcType(ik, year - 1, 7));
                if (isObligatorDrg() && isObligatorPsy()) {
                    statement.setDrgCalc(_calcFacade.isParticipationInCalcType(ik, year - 1, 1));
                    statement.setPsyCalc(_calcFacade.isParticipationInCalcType(ik, year - 1, 3));
                }
            } else {
                statement.setDrgCalc(domain.contains("DRG"));
                int drgMulti = Integer.parseInt(drgMultiyear);
                if (drgMulti < 4 || drgMulti > 5) {
                    // theses former values are not supported anymore
                    drgMulti = 0;
                }
                statement.setMultiyearDrg(drgMulti);
                statement.setPsyCalc(domain.contains("PSY"));
                int psyMulti = Integer.parseInt(psyMultiyear);
                if (psyMulti < 4 || psyMulti > 5) {
                    // theses former values are not supported anymore
                    psyMulti = 0;
                }
                statement.setMultiyearPsy(psyMulti);
                statement.setInvCalc(domain.contains("INV"));
                statement.setTpgCalc(domain.contains("TPG"));
                statement.setObdCalc(domain.contains("OBD"));
            }

            statement.setClinicalDistributionModelPsy(-1);
            statement.setClinicalDistributionModelDrg(-1);
        }
        setStatementContacts(statement, ik);
        return statement;
    }

    private void setStatementContacts(StatementOfParticipance statement, int ik) {

        List<CalcContact> calcContacts = _calcFacade.retrieveCalcContactsForIk(ik);

        if (statement.isDrgCalc()) {
            for (CalcContact con : calcContacts.stream().filter(co -> co.isDrg()).collect(Collectors.toList())) {
                addContactifMissing(statement, con);
            }
        }
        if (statement.isPsyCalc()) {
            for (CalcContact con : calcContacts.stream().filter(co -> co.isPsy()).collect(Collectors.toList())) {
                addContactifMissing(statement, con);
            }
        }
        if (statement.isTpgCalc()) {
            for (CalcContact con : calcContacts.stream().filter(co -> co.isTpg()).collect(Collectors.toList())) {
                addContactifMissing(statement, con);
            }
        }
        if (statement.isInvCalc()) {
            for (CalcContact con : calcContacts.stream().filter(co -> co.isInv()).collect(Collectors.toList())) {
                addContactifMissing(statement, con);
            }
        }
        if (statement.isObdCalc()) {
            for (CalcContact con : calcContacts.stream().filter(co -> co.isObd()).collect(Collectors.toList())) {
                addContactifMissing(statement, con);
            }
        }
    }

    private void addContactifMissing(StatementOfParticipance statement, CalcContact con) {
        if (statement.getContacts().stream().noneMatch(co -> co.equals(con))) {
            statement.addContact(con);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public StatementOfParticipance getStatement() {
        return _statement;
    }

    public void setStatement(StatementOfParticipance statement) {
        _statement = statement;
    }

    public List<CalcContact> getContacts(boolean isConsultant) {
        ensureContacts(_statement);
        return _statement.getContacts().stream().filter(c -> c.isConsultant() == isConsultant).collect(Collectors.
                toList());
    }

    public List<CalcContact> getHospitalContacts() {
        return getContacts(false);
    }

    public List<CalcContact> getConsultants() {
        return getContacts(true);
    }
    // </editor-fold>

    @Override
    protected void addTopics() {
        addTopic(StatementOfParticipanceTabs.tabStatementOfParticipanceAddress.name(), Pages.StatementOfParticipanceEditAddress.
                URL());
        addTopic(StatementOfParticipanceTabs.tabStatementOfParticipanceStatements.name(), Pages.StatementOfParticipanceEditStatements.
                URL());
    }

    public void enableDisablePageElements() {
        setEnableDrgCalc(false);
        setEnableInvCalc(false);
        setEnablePsyCalc(false);
        if (_statement.isObligatory()) {
            setEnableDrgCalc(_statement.isDrgCalc() && (isObligatorDrg() || isObligatorPsy()));
            setEnableInvCalc(_statement.isInvCalc() && isObligatorInv());
            setEnablePsyCalc(_statement.isPsyCalc() && (isObligatorDrg() || isObligatorPsy()));
        }
        enableDisableStatementPage();
    }

    public void enableDisableStatementPage() {
        boolean enable = isExplanationPageEnabled();
        findTopic(StatementOfParticipanceTabs.tabStatementOfParticipanceStatements.name()).setVisible(enable);
    }

    public boolean isExplanationPageEnabled() {
        boolean enable = (!_statement.isObligatory() || _statement.getObligatoryCalcType() > 1 || _statement.
                isObligatoryFollowingYears()
                || (isObligatorInv() && !isObligatorDrg() && !isObligatorPsy()))
                && (_statement.isDrgCalc() || _statement.isPsyCalc());
        return enable;
    }

    public boolean isInInekRole() {
        // todo: move to a central place, using a more general concept
        if (_sessionController == null) {
            return false;
        }

        for (InekRole role : _sessionController.getAccount().getInekRoles()) {
            if ("TE Admin".equals(role.getText())) {
                return true;
            }
        }
        return false;
    }

    public String getEmailInfo() {
        String info = "";
        boolean first = true;
        if (_statement == null) {
            return "";
        }
        for (CalcContact cc : _statement.getContacts()) {
            if (!_accFacade.existsMail(cc.getMail()) && cc.getMail().length() > 0) {
                if (first) {
                    info = "<b>Hinweis:</b> Die angegebene/n E-Mailadresse/n ist/sind noch nicht im InEK-Datenportal registriert: ";
                    info += "<u>" + cc.getMail() + "</u>";
                    first = false;
                    continue;
                }
                info += ", <u>" + cc.getMail() + "</u>";
            }
        }
        if (info.length() > 0) {
            info += ".<br/>";
        }
        return info;
    }

    @SuppressWarnings("CyclomaticComplexity")
    public String getObligatoryMessage() {
        if (_statement.isDrgCalc() && isObligatorDrg()) {
            return "Das Krankenhaus wird für den Entgeltbereich DRG im Datenjahr " + _statement.getDataYear() + " eine ";
        } else if (_statement.isPsyCalc() && isObligatorPsy()) {
            return "Das Krankenhaus wird für den Entgeltbereich PSY im Datenjahr " + _statement.getDataYear() + " eine ";
        }

        return "Das Krankenhaus wird eine ";
    }

    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isReadOnly() {
        if (_accessManager == null || _statement == null) {
            return true;
        }
        if (isInInekRole()) {
            return false;
        }
        return _accessManager.
                isReadOnly(Feature.CALCULATION_HOSPITAL, _statement.getStatus(), _statement.getAccountId(), _statement.
                        getIk());
    }

    public String save() {
        setModifiedInfo();
        _statement = _calcFacade.saveStatementOfParticipance(_statement);

        if (isValidId(_statement.getId())) {
            DialogController.showSaveDialog();
            return null;
        }
        return Pages.Error.URL();
    }

    private void setModifiedInfo() {
        _statement.setLastChanged(Calendar.getInstance().getTime());
        _statement.setAccountIdLastChange(_sessionController.getAccountId());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsStatemenOfParticipanceSendEnabled)) {
            return false;
        }

        if (isInInekRole()) {
            return true;
        }
        return _accessManager.isSealedEnabled(Feature.CALCULATION_HOSPITAL, _statement.getStatus(),
                _statement.getAccountId(), _statement.getIk());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsStatemenOfParticipanceSendEnabled)) {
            return false;
        }
        return _accessManager.isApprovalRequestEnabled(Feature.CALCULATION_HOSPITAL, _statement.getStatus(),
                _statement.getAccountId(), _statement.getIk());
    }

    public boolean isTakeEnabled() {
        return false;
        // todo: do not allow consultant
        //return _cooperationTools.isTakeEnabled(Feature.CALCULATION_HOSPITAL, _statement.getStatus(), _statement.getAccountId());
    }

    @Inject
    private Mailer _mailer;

    /**
     * This function seals a statement of participance if possible. Sealing is possible, if all mandatory fields are
     * fulfilled. After sealing, the statement od participance can not be edited anymore and is available for the InEK.
     *
     * @return
     */
    public String seal() {

        setDistibutionModelAndMuliyear();

        populateDefaultsForUnreachableFields();
        if (!statementIsNoParticipation()) {
            if (!statementIsComplete()) {
                return getActiveTopic().getOutcome();
            }
        }
        _statement.setStatus(WorkflowStatus.Provided);
        setModifiedInfo();
        _statement.setSealed(new Date());

        for (StatementOfParticipance sop : _calcFacade.listStatementOfParticipanceByIk(_statement.getIk())) {
            sop.setStatus(WorkflowStatus.Retired);
            _calcFacade.saveStatementOfParticipance(sop);
        }

        _statement = _calcFacade.saveStatementOfParticipance(_statement);

        boolean testMode = _appTools.isEnabled(ConfigKey.TestMode);
        if (!testMode) {
            _icmtUpdater.saveStatementOfParticipanceForIcmt(_statement);
            if (isObligatorInv()) {
                _icmtUpdater.saveObligateInvCalc(_statement);
            }
            if (_statement.isObligatory() && !isObligatorInv() && _statement.isInvCalc()) {
                _icmtUpdater.saveParticipanceInCalcType(_statement, 4);
            }
        }

        if (!_appTools.isEnabled(ConfigKey.IsStatemenOfParticipanceResendEnabled)) {
            _mailer.sendMail("Anfragen@datenstelle.de", "Teilnahmeerklärung", "" + _statement.getIk());
        }
        //createTransferFile(_statement);
        if (isValidId(_statement.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL"));
            Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_statement));
            return Pages.PrintView.URL();
        }
        return "";
    }

    public void setDistibutionModelAndMuliyear() {
        if (!_statement.isDrgCalc()) {
            _statement.setClinicalDistributionModelDrg(-1);
            _statement.setMultiyearDrg(0);
        }
        if (!_statement.isPsyCalc()) {
            _statement.setClinicalDistributionModelPsy(-1);
            _statement.setMultiyearPsy(0);
        }
    }

    @SuppressWarnings("CyclomaticComplexity")
    private boolean missingContact() {
        for (CalcContact cc : _statement.getContacts().stream().filter(c -> !c.isConsultant()).collect(Collectors.
                toList())) {
            if (!cc.isDrg() && !cc.isInv() && !cc.isObd() && !cc.isPsy() && !cc.isTpg()) {
                _sessionController.setScript("alert('Für die folgenden Felder ist noch eine Eingabe erforderlich:\\n\\n"
                        + "Jedem Ansprechpartner ist mindestens ein Kalkulationsbereich (DRG, PSY, INV, TPG, OBD) zuzuordnen.')");
                return true;
            }
        }
        if (_statement.isConsultantSendMail()) {
            for (CalcContact cc : _statement.getContacts().stream().filter(c -> c.isConsultant()).collect(Collectors.
                    toList())) {
                if (!cc.isDrg() && !cc.isInv() && !cc.isObd() && !cc.isPsy() && !cc.isTpg()) {
                    _sessionController.
                            setScript("alert('Für die folgenden Felder ist noch eine Eingabe erforderlich:\\n\\n"
                                    + "Jedem Berater ist mindestens ein Kalkulationsbereich (DRG, PSY, INV, TPG, OBD) zuzuordnen.')");
                    return true;
                }
            }
        }
        return false;
    }

    private void updateObligatorySetting(StatementOfParticipance statement) {
        if (statement.isObligatory()) {
            setObligatorDrg(_calcFacade.isObligateInCalcType(statement.getIk(), statement.getDataYear(), 1));
            setObligatorPsy(_calcFacade.isObligateInCalcType(statement.getIk(), statement.getDataYear(), 3));
            setObligatorInv(_calcFacade.isObligateInCalcType(statement.getIk(), statement.getDataYear(), 4));

        }
    }

    @SuppressWarnings("CyclomaticComplexity")
    private void populateDefaultsForUnreachableFields() {
        // Some input fields can't be reached depending on other fields.
        // But they might contain elder values, which became obsolte by setting the other field 
        // Such fields will be removed or populated with default values
        if (!_statement.isWithConsultant()) {
            _statement.setConsultantCompany("");
            _statement.setConsultantSendMail(false);
            _statement.getContacts();
            List<CalcContact> consultContacts = _statement.getContacts().stream().filter(c -> c.isConsultant()).
                    collect(Collectors.toList());
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
        if (_statement.getMultiyearDrg() != 15) {
            _statement.setMultiyearDrgText("");
        }
        if (_statement.getMultiyearPsy() != 15) {
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
        if (_statement.getStatusId() == 10 && !_appTools.isEnabled(ConfigKey.IsStatemenOfParticipanceResendEnabled)) {
            DialogController.showInfoDialog("Vorgang nicht möglich",
                    "Eine Änderung der Teilnahmeerklärung im Datenportal ist leider nicht mehr möglich. " +
                            "Bitte teilen Sie Ihre Änderungswünsche Ihrem zuständigen Referenten mit.");
            return;
        }
        _statement.setId(-1);
        _statement.setStatus(WorkflowStatus.New);
        // do not set: _statement.setAccountId(_sessionController.getAccountId());
        for (CalcContact contact : _statement.getContacts()) {
            contact.setId(-1);
            contact.setStatementOfParticipanceId(-1);
        }
    }

    private boolean statementIsComplete() {
        MessageContainer message = composeMissingFieldsMessage(_statement);

        if (missingContact()) {
            return false;
        }

        if (message.containsMessage()) {
            message.setMessage("Bitte überprüfen und korrigieren Sie folgende Angaben:\\r\\n" + message.getMessage());
            setActiveTopic(message.getTopic());
            String script = "alert ('" + message.getMessage() + "');";
            if (!message.getElementId().isEmpty()) {
                script += "\r\n document.getElementById('" + message.getElementId() + "').focus();";
            }
            _sessionController.setScript(script);
        }
        return !message.containsMessage();
    }

    @SuppressWarnings("CyclomaticComplexity")
    public MessageContainer composeMissingFieldsMessage(StatementOfParticipance statement) {
        MessageContainer message = new MessageContainer();

        String ik = statement.getIk() < 0 ? "" : "" + statement.getIk();
        checkField(message, ik, "lblIK", "sop:ikMulti", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress);

        if (statement.isDrgCalc() && !statement.getContacts().stream().anyMatch(c -> c.isDrg() && !c.isConsultant())) {
            applyMessageValues(message, "lblNeedContactDrg", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contact");
        }
        if (statement.isPsyCalc() && !statement.getContacts().stream().anyMatch(c -> c.isPsy() && !c.isConsultant())) {
            applyMessageValues(message, "lblNeedContactPsy", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contact");
        }
        if (statement.isInvCalc() && !statement.getContacts().stream().anyMatch(c -> c.isInv() && !c.isConsultant())) {
            applyMessageValues(message, "lblNeedContactInv", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contact");
        }
        if (statement.isTpgCalc() && !statement.getContacts().stream().anyMatch(c -> c.isTpg() && !c.isConsultant())) {
            applyMessageValues(message, "lblNeedContactTpg", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contact");
        }
        if (statement.isObdCalc() && !statement.getContacts().stream().anyMatch(c -> c.isObd() && !c.isConsultant())) {
            applyMessageValues(message, "lblNeedContactObd", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contact");
        }

        if (statement.getContacts().stream()
                .filter(c -> !c.isEmpty())
                .anyMatch(c -> c.getFirstName() == null || c.getFirstName().isEmpty()
                || c.getLastName() == null || c.getLastName().isEmpty()
                || c.getMail() == null || c.getMail().isEmpty()
                || c.getPhone() == null || c.getPhone().isEmpty()
                || c.getGender() == 0
                || !hasContactAnyRole(c))) {
            applyMessageValues(message, "msgContactIncomplete", StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contact");
        }
        if (statement.isWithConsultant()) {
            checkField(message, statement.getConsultantCompany(), "lblNameConsultant", "sop:consultantCompany",
                    StatementOfParticipanceTabs.tabStatementOfParticipanceAddress);
        }
        if (statement.isConsultantSendMail()) {
            List<CalcContact> consultantContacts = _statement.getContacts().stream().filter(c -> c.isConsultant()).
                    collect(Collectors.toList());
            if (consultantContacts.isEmpty() || consultantContacts.get(0).isEmpty()) {
                applyMessageValues(message, "lblNeedContactConsultant",
                        StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contactConsultant");
            }
        }

        if (statement.isDrgCalc() && (statement.getObligatoryCalcType() == 2
                || statement.isObligatoryFollowingYears() || !statement.isObligatory()
                || (isObligatorInv() && !isObligatorDrg() && !isObligatorPsy()))) {
            checkField(message, statement.getClinicalDistributionModelDrg(), 0, 1,
                    "lblNeedSingleCostAttributionDrg", "sop:clinicalDistributionModelDrg",
                    StatementOfParticipanceTabs.tabStatementOfParticipanceStatements);
            checkField(message, statement.getMultiyearDrg(), 1, 15,
                    "lblQuestionOverlayerDrg", "sop:multiyearDrg",
                    StatementOfParticipanceTabs.tabStatementOfParticipanceStatements);
            if (statement.getMultiyearDrg() == 15 && statement.getMultiyearDrgText().isEmpty()) {
                applyMessageValues(message, "lblDescriptionOfAlternativeDrg",
                        StatementOfParticipanceTabs.tabStatementOfParticipanceStatements, "form");
            }
        }

        if (statement.isPsyCalc() && (statement.isObligatoryFollowingYears()
                || statement.getObligatoryCalcType() == 2 || !statement.isObligatory()
                || (isObligatorInv() && !isObligatorDrg() && !isObligatorPsy()))) {
            checkField(message, statement.getClinicalDistributionModelPsy(), 0, 1,
                    "lblNeedSingleCostAttributionPsy", "sop:clinicalDistributionModelPsy",
                    StatementOfParticipanceTabs.tabStatementOfParticipanceStatements);
            checkField(message, statement.getMultiyearPsy(), 1, 15,
                    "lblQuestionOverlayerPsy", "sop:multiyearPsy",
                    StatementOfParticipanceTabs.tabStatementOfParticipanceStatements);
            if (statement.getMultiyearPsy() == 15 && statement.getMultiyearPsyText().isEmpty()) {
                applyMessageValues(message, "lblDescriptionOfAlternativePsy",
                        StatementOfParticipanceTabs.tabStatementOfParticipanceStatements, "form");
            }
        }

        if (contactsHaveToManyRoles(statement.getContacts().stream().filter(c -> !c.isConsultant()).collect(Collectors.toList()))) {
            applyMessageValues(message, "Bitte geben Sie pro Kalkulationsbereich maximal drei Ansprechpartner an.",
                    StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contactConsultant");
        }

        if (contactsHaveToManyRoles(statement.getContacts().stream().filter(c -> c.isConsultant()).collect(Collectors.toList()))) {
            applyMessageValues(message, "Bitte geben Sie pro Kalkulationsbereich maximal drei Berater an.",
                    StatementOfParticipanceTabs.tabStatementOfParticipanceAddress, "sop:contactConsultant");
        }

        return message;
    }

    private boolean contactsHaveToManyRoles(List<CalcContact> contacts) {
        int maxRoles = 3;
        if (contacts.stream().filter(CalcContact::isDrg).count() > maxRoles) {
            return true;
        }
        if (contacts.stream().filter(CalcContact::isPsy).count() > maxRoles) {
            return true;
        }
        if (contacts.stream().filter(CalcContact::isInv).count() > maxRoles) {
            return true;
        }
        if (contacts.stream().filter(CalcContact::isTpg).count() > maxRoles) {
            return true;
        }
        if (contacts.stream().filter(CalcContact::isObd).count() > maxRoles) {
            return true;
        }
        return false;
    }

    private boolean hasContactAnyRole(CalcContact c) {
        if (c.isDrg()) {
            return true;
        }
        if (c.isPsy()) {
            return true;
        }
        if (c.isTpg()) {
            return true;
        }
        if (c.isInv()) {
            return true;
        }
        if (c.isObd()) {
            return true;
        }
        return false;
    }

    private void checkField(MessageContainer message, String value, String msgKey, String elementId,
            StatementOfParticipanceTabs tab) {
        if (Utils.isNullOrEmpty(value)) {
            applyMessageValues(message, msgKey, tab, elementId);
        }
    }

    private void checkField(MessageContainer message, Integer value, Integer minValue, Integer maxValue, String msgKey,
            String elementId, StatementOfParticipanceTabs tab) {
        if (value == null
                || minValue != null && value < minValue
                || maxValue != null && value > maxValue) {
            applyMessageValues(message, msgKey, tab, elementId);
        }
    }

    private void applyMessageValues(MessageContainer message, String msgKey, StatementOfParticipanceTabs tab,
            String elementId) {
        if (msgKey.startsWith("lbl") || msgKey.startsWith("msg")) {
            message.setMessage(message.getMessage() + "\\r\\n" + Utils.getMessage(msgKey));
        }
        else {
            message.setMessage(message.getMessage() + "\\r\\n" + msgKey);
        }

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
        File dir = new File(_sessionController.getApplicationTools().readConfig(ConfigKey.FolderRoot),
                _sessionController.getApplicationTools().readConfig(ConfigKey.FolderUpload));
        File file;
        Date ts;
        do {
            ts = Calendar.getInstance().getTime();
            file = new File(dir, "Transfer" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(ts) + ".txt");
        } while (file.exists());

        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file))) {
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
    public List<SelectItem> getIks() {
        Account account = _sessionController.getAccount();
        int year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
        Set<Integer> iks = _calcFacade.obtainIks4NewStatementOfParticipance(account.getId(), year, _appTools.isEnabled(ConfigKey.TestMode));
        Set<Integer> allowedIks = _accessManager.obtainIksForCreation(Feature.CALCULATION_HOSPITAL);
        iks.removeIf(ik -> !allowedIks.contains(ik));

        List<SelectItem> _ikItems = new ArrayList<>();
        for (int ik : iks) {
            _ikItems.add(new SelectItem(ik));
        }
        return _ikItems;
    }

    public void ikChanged() {
        if (_statement.getId() == -1) {
            // paranoid check. usually the ik cannot be changed, once the statement is stored
            setObligatorDrg(false);
            setObligatorPsy(false);
            _statement = retrievePriorData(_statement.getIk(), _statement.getDataYear(), _sessionController.getAccount().
                    getId());
            enableDisablePageElements();
        }
    }

    public void addContact() {
        _statement.getContacts().add(new CalcContact());
    }

    public void addConsultant() {
        CalcContact consultant = new CalcContact();
        consultant.setConsultant(true);
        _statement.getContacts().add(consultant);
    }

    public void deleteContact(CalcContact contact) {
        _statement.getContacts().remove(contact);
    }

// </editor-fold>
}
