/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.psychstaff.backingbean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountAdditionalIK;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.feature.admin.entity.MailTemplate;
import org.inek.dataportal.feature.admin.facade.InekRoleFacade;
import org.inek.dataportal.feature.psychstaff.entity.OccupationalCatagory;
import org.inek.dataportal.feature.psychstaff.entity.StaffProof;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofDocument;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofEffective;
import org.inek.dataportal.feature.psychstaff.enums.PsychType;
import org.inek.dataportal.feature.psychstaff.facade.PsychStaffFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.MessageContainer;
import org.inek.dataportal.mail.Mailer;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.utils.StreamUtils;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class EditPsyStaff extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger LOGGER = Logger.getLogger("EditPsyStaff");
    private static final String TOPIC_MASTER = "tabUMMaster";
    private static final String TOPIC_KIDS2 = "topicAppendix2Kids";
    private static final String TOPIC_ADULTS2 = "topicAppendix2Adults";
    private static final String TOPIC_KIDS1 = "topicAppendix1Kids";
    private static final String TOPIC_ADULTS1 = "topicAppendix1Adults";

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private PsychStaffFacade _psychStaffFacade;
    @Inject private ApplicationTools _appTools;

    private StaffProof _staffProof;

    public StaffProof getStaffProof() {
        return _staffProof;
    }

    public void setStaffProof(StaffProof staffProof) {
        _staffProof = staffProof;
    }
    // </editor-fold>

    @Override
    protected void addTopics() {
        addTopic(TOPIC_MASTER, Pages.PsychStaffBaseData.URL());
        addTopic(TOPIC_ADULTS1, Pages.PsychStaffAppendix1Adults.URL());
        addTopic(TOPIC_KIDS1, Pages.PsychStaffAppendix1Kids.URL());
        addTopic(TOPIC_ADULTS2, Pages.PsychStaffAppendix2Adults.URL());
        addTopic(TOPIC_KIDS2, Pages.PsychStaffAppendix2Kids.URL());
    }

    @Override
    protected String getOutcome() {
        return "";
    }

    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = "" + params.get("id");
        if ("new".equals(id)) {
            _staffProof = newStaffProof();
        } else if (Utils.isInteger(id)) {
            StaffProof staffProof = loadStaffProof(id);
            if (staffProof.getId() == -1) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            _staffProof = staffProof;
        } else {
            Utils.navigate(Pages.Error.RedirectURL());
        }
        setTopicVisibility();
    }

    private StaffProof loadStaffProof(String idObject) {
        int id = Integer.parseInt(idObject);
        StaffProof staffProof = _psychStaffFacade.findStaffProof(id);
        if (hasSufficientRights(staffProof)) {
            ensureStaffProofsAgreed(staffProof);
            ensureStaffProofsEffective(staffProof);
            return staffProof;
        }
        return new StaffProof();
    }

    private boolean hasSufficientRights(StaffProof staffProof) {
        if (_sessionController.isMyAccount(staffProof.getAccountId(), false)) {
            return true;
        }
        if (_sessionController.isInekUser(Feature.PSYCH_STAFF)) {
            return true;
        }
        return _cooperationTools.isAllowed(Feature.PSYCH_STAFF, staffProof.getStatus(), staffProof.getAccountId());
    }

    private StaffProof newStaffProof() {
        Account account = _sessionController.getAccount();
        StaffProof staffProof = new StaffProof();
        staffProof.setAccountId(account.getId());
        List<SelectItem> iks = getIks();
        if (iks.size() == 1) {
            staffProof.setIk((int) iks.get(0).getValue());
        }
        return staffProof;
    }

    private void setTopicVisibility() {
        if (_staffProof == null) {
            return;
        }
        boolean hasIk = _staffProof.getIk() > 0;
        findTopic(TOPIC_ADULTS1).setVisible(hasIk && _staffProof.isForAdults());
        findTopic(TOPIC_ADULTS2).setVisible(hasIk && _staffProof.isForAdults());
        findTopic(TOPIC_KIDS1).setVisible(hasIk && _staffProof.isForKids());
        findTopic(TOPIC_KIDS2).setVisible(hasIk && _staffProof.isForKids());
    }

    public void ikChanged() {
        setTopicVisibility();
        ensureStaffProofsAgreed(_staffProof);
        ensureStaffProofsEffective(_staffProof);
    }

    private void ensureStaffProofsAgreed(StaffProof staffProof) {
        if (staffProof.isForAdults()) {
            ensureStaffProofsAgreed(staffProof, PsychType.Adults);
        }
        if (staffProof.isForKids()) {
            ensureStaffProofsAgreed(staffProof, PsychType.Kids);
        }
    }

    public void ensureStaffProofsAgreed(StaffProof staffProof, PsychType type) {
        if (staffProof.getStaffProofsAgreed(type).size() > 0) {
            return;
        }
        for (OccupationalCatagory cat : getOccupationalCategories()) {
            StaffProofAgreed agreed = new StaffProofAgreed();
            agreed.setStaffProofMasterId(staffProof.getId());
            agreed.setPsychType(type);
            agreed.setOccupationalCatagory(cat);
            staffProof.addStaffProofAgreed(agreed);
        }
    }

    private void ensureStaffProofsEffective(StaffProof staffProof) {
        if (staffProof.isForAdults()) {
            ensureStaffProofsEffective(staffProof, PsychType.Adults);
        }
        if (staffProof.isForKids()) {
            ensureStaffProofsEffective(staffProof, PsychType.Kids);
        }
    }

    public void ensureStaffProofsEffective(StaffProof staffProof, PsychType type) {
        if (staffProof.getStaffProofsEffective(type).size() > 0) {
            return;
        }
        for (OccupationalCatagory cat : getOccupationalCategories()) {
            StaffProofEffective effective = new StaffProofEffective();
            effective.setStaffProofMasterId(staffProof.getId());
            effective.setPsychType(type);
            effective.setOccupationalCatagory(cat);
            staffProof.addStaffProofEffective(effective);
        }
    }

    public int getAdultPsyOccupationRowSpan(int personnelId) {
        return _psychStaffFacade.getSumSamePersonalGroup(personnelId);
    }

    public List<OccupationalCatagory> getOccupationalCategories() {
        return _psychStaffFacade.getOccupationalCategories();
    }

    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isReadOnly() {
        if (_staffProof == null) {
            return true;
        }
        if (_sessionController.isInekUser(Feature.PSYCH_STAFF) && !_appTools.isEnabled(ConfigKey.TestMode)) {
            return true;
        }
        return _cooperationTools.isReadOnly(Feature.PSYCH_STAFF, _staffProof.getStatus(), _staffProof.getAccountId());
    }

    public String save() {
        setModifiedInfo();
        _staffProof = _psychStaffFacade.saveStaffProof(_staffProof);

        if (isValidId(_staffProof.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    public String saveAndMail() {
        setModifiedInfo();
        _staffProof = _psychStaffFacade.saveStaffProof(_staffProof);

        if (isValidId(_staffProof.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            sendMessage("Psych-Personalnachweis-Verordnung");
            return null;
        }
        return Pages.Error.URL();
    }

    @Inject private AccountFacade _accountFacade;
    @Inject private Mailer _mailer;

    private void sendMessage(String name) {
        //todo: refactor for gloabal usage (move to mailer?) and remove all similar methods
        Account receiver = _accountFacade.find(_appTools.isEnabled(ConfigKey.TestMode)
                ? _sessionController.getAccountId()
                : _staffProof.getAccountId());
        MailTemplate template = _mailer.getMailTemplate(name);
        String subject = template.getSubject()
                .replace("{ik}", "" + _staffProof.getIk());
        String body = template.getBody()
                .replace("{formalSalutation}", _mailer.getFormalSalutation(receiver));
//                .replace("{note}", _staffProof.getNoteInek());
        String bcc = template.getBcc().replace("{accountMail}", _sessionController.getAccount().getEmail());
        _mailer.sendMailFrom(template.getFrom(), receiver.getEmail(), "", bcc, subject, body);
    }

    private void setModifiedInfo() {
        _staffProof.setLastChanged(Calendar.getInstance().getTime());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        return isSendEnabled()
                && _cooperationTools.isSealedEnabled(Feature.PSYCH_STAFF, _staffProof.getStatus(), _staffProof.getAccountId());
    }

    public boolean isCloseEnabled() {
        if (!isSendEnabled()) {
            return false;
        }
        if (!_cooperationTools.isSealedEnabled(Feature.PSYCH_STAFF, _staffProof.getStatus(), _staffProof.getAccountId())) {
            return false;
        }
        switch (getActiveTopicKey()) {
            case TOPIC_MASTER:
                return false;
            case TOPIC_ADULTS1:
                return _staffProof.getStatusAdults1() < WorkflowStatus.Provided.getId();
            case TOPIC_KIDS1:
                return _staffProof.getStatusKids1() < WorkflowStatus.Provided.getId();
            case TOPIC_ADULTS2:
                return _staffProof.getStatusAdults2() < WorkflowStatus.Provided.getId();
            case TOPIC_KIDS2:
                return _staffProof.getStatusKids2() < WorkflowStatus.Provided.getId();
            default:
                return false;
        }
    }

    public boolean isClosedState() {
        switch (getActiveTopicKey()) {
            case TOPIC_MASTER:
                return false;
            case TOPIC_ADULTS1:
                return _staffProof.getStatusAdults1() >= WorkflowStatus.Provided.getId();
            case TOPIC_KIDS1:
                return _staffProof.getStatusKids1() >= WorkflowStatus.Provided.getId();
            case TOPIC_ADULTS2:
                return _staffProof.getStatusAdults2() >= WorkflowStatus.Provided.getId();
            case TOPIC_KIDS2:
                return _staffProof.getStatusKids2() >= WorkflowStatus.Provided.getId();
            default:
                return false;
        }
    }

    public boolean isClosedStateActionEnabled() {
        if (!isSendEnabled()) {
            return false;
        }
        if (!_cooperationTools.isSealedEnabled(Feature.PSYCH_STAFF, _staffProof.getStatus(), _staffProof.getAccountId())) {
            return false;
        }
        return isClosedState();
    }

    public String close() {
        if (updateStatus(WorkflowStatus.Provided)) {
            save();
        }
        return null;
    }

    private boolean updateStatus(WorkflowStatus newStatus) {
        switch (getActiveTopicKey()) {
            case TOPIC_MASTER:
                return false;
            case TOPIC_ADULTS1:
                _staffProof.setStatusAdults1(newStatus.getId());
                _staffProof.setStatusChangedAdults1(new Date());
                break;
            case TOPIC_KIDS1:
                _staffProof.setStatusKids1(newStatus.getId());
                _staffProof.setStatusChangedKids1(new Date());
                break;
            case TOPIC_ADULTS2:
                _staffProof.setStatusAdults2(newStatus.getId());
                _staffProof.setStatusChangedAdults2(new Date());
                break;
            case TOPIC_KIDS2:
                _staffProof.setStatusKids2(newStatus.getId());
                _staffProof.setStatusChangedKids2(new Date());
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean isReopenEnabled() {
        if (!isSendEnabled()) {
            return false;
        }
        if (!_cooperationTools.isSealedEnabled(Feature.PSYCH_STAFF, _staffProof.getStatus(), _staffProof.getAccountId())) {
            return false;
        }
        return isClosedStateActionEnabled();
    }

    public String reopen() {
        if (updateStatus(WorkflowStatus.CorrectionRequested)) {
            save();
        }
        return null;
    }

    private boolean isSendEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsPsychStaffSendEnabled)) {
            return false;
        }
        if (_staffProof == null) {
            return false;
        }
        return true;
    }

    public boolean isRequestCorrectionEnabled() {
        return isSendEnabled()
                && (_staffProof.getStatus() == WorkflowStatus.Provided || _staffProof.getStatus() == WorkflowStatus.ReProvided)
                && _sessionController.isInekUser(Feature.PSYCH_STAFF, true);
    }

    public String requestCorrection() {
        if (!isRequestCorrectionEnabled()) {
            return "";
        }
        setModifiedInfo();
        // set as retired
        _staffProof.setStatus(WorkflowStatus.Retired);
        _psychStaffFacade.saveStaffProof(_staffProof);

        // create copy to edit (persist detached object with default Ids)
        _staffProof.setStatus(WorkflowStatus.CorrectionRequested);
        _staffProof.setId(-1);
        // todo: copy lists
//        for (RequestProjectedCenter requestProjectedCenter : _staffProof.getRequestProjectedCenters()) {
//            requestProjectedCenter.setId(-1);
//            requestProjectedCenter.setRequestMasterId(-1);
//        }
        _psychStaffFacade.saveStaffProof(_staffProof);
        sendMessage("BA Konkretisierung");

        return Pages.SpecificFunctionSummary.URL();
    }

    public boolean isTakeEnabled() {
        return _cooperationTools != null
                && _staffProof != null
                && _cooperationTools.isTakeEnabled(Feature.PSYCH_STAFF, _staffProof.getStatus(), _staffProof.getAccountId());
    }

    /**
     * This function seals a statement od participance if possible. Sealing is possible, if all mandatory fields are
     * fulfilled. After sealing, the statement od participance can not be edited anymore and is available for the InEK.
     *
     * @return
     */
    public String seal() {
        if (!staffProofIsComplete()) {
            return null;
        }
        _staffProof.setStatus(WorkflowStatus.Provided);
        setModifiedInfo();
        if (_staffProof.getSealed().equals(Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC)))) {
            // set seal date for the first time sealing only
            _staffProof.setSealed(Calendar.getInstance().getTime());
        }
        _staffProof = _psychStaffFacade.saveStaffProof(_staffProof);

        if (isValidId(_staffProof.getId())) {
            sendNotification();
            Utils.getFlash().put("headLine", Utils.getMessage("namePSYCH_STAFF"));
            Utils.getFlash().put("targetPage", Pages.SpecificFunctionSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_staffProof));
            return Pages.PrintView.URL();
        }
        return "";
    }

    @Inject private InekRoleFacade _inekRoleFacade;

    public void sendNotification() {
        List<Account> inekAccounts = _inekRoleFacade.findForFeature(Feature.PSYCH_STAFF);
        String receipients = inekAccounts.stream().map(a -> a.getEmail()).collect(Collectors.joining(";"));
        _mailer.sendMail(receipients, "Psych-Personalnachweis-Verorsnung", "Es wurde ein Datensatz an das InEK gesendet.");
    }

    private boolean staffProofIsComplete() {
        MessageContainer message = composeMissingFieldsMessage(_staffProof);

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

    public MessageContainer composeMissingFieldsMessage(StaffProof request) {
        MessageContainer message = new MessageContainer();

        String ik = request.getIk() <= 0 ? "" : "" + request.getIk();
        checkField(message, ik, "lblIK", "psychStaff:ikMulti", TOPIC_MASTER);
        checkField(message, _staffProof.getYear(), 2016, 2020, "lblAgreementYear", "psychStaff:year", TOPIC_MASTER);
        if (!_staffProof.isForAdults() && !_staffProof.isForKids()) {
            String msg = "Bitte angeben, ob die Einrichtung für Erwachsene und / oder Kinder und Jugendliche ist.";
            applyMessageValues(message, msg, "psychStaff:adults", TOPIC_MASTER);
        }

        if (_staffProof.isForAdults()) {
            checkField(message, _staffProof.getAdultsAgreedDays(), 1, Integer.MIN_VALUE, "lblAgreedDays", "psychStaff:agreedDays", TOPIC_ADULTS1);
            checkAgreedItems(message, PsychType.Adults, TOPIC_ADULTS1);
        }

        if (_staffProof.isForKids()) {
            checkField(message, _staffProof.getKidsAgreedDays(), 1, Integer.MIN_VALUE, "lblAgreedDays", "psychStaff:agreedDays", TOPIC_KIDS1);
            checkAgreedItems(message, PsychType.Kids, TOPIC_KIDS1);
        }

        return message;
    }

    private void checkAgreedItems(MessageContainer message, PsychType psychType, String topic) {
        for (StaffProofAgreed item : _staffProof.getStaffProofsAgreed(psychType)) {
            String msg = "Bitte Stellenbesetzung für " + item.getOccupationalCatagory().getName() + " angeben.";
            checkField(message, item.getStaffingComplete(), 0.1, null, msg, "", topic);
            checkField(message, item.getStaffingBudget(), 0.1, null, msg, "", topic);
            msg = "Bitte Durchschnittskosten für " + item.getOccupationalCatagory().getName() + " angeben.";
            checkField(message, item.getAvgCost(), 0.1, null, msg, "", topic);
        }
    }

    private void checkField(MessageContainer message, String value, String msgKey, String elementId, String topic) {
        if (Utils.isNullOrEmpty(value)) {
            applyMessageValues(message, msgKey, elementId, topic);
        }
    }

    private void checkField(
            MessageContainer message,
            Integer value,
            Integer minValue,
            Integer maxValue,
            String msgKey,
            String elementId,
            String topic) {
        if (value == null
                || minValue != null && value < minValue
                || maxValue != null && value > maxValue) {
            applyMessageValues(message, msgKey, elementId, topic);
        }
    }

    private void checkField(
            MessageContainer message,
            Double value,
            Double minValue,
            Double maxValue,
            String msgKey,
            String elementId,
            String topic) {
        if (value == null
                || minValue != null && value < minValue
                || maxValue != null && value > maxValue) {
            applyMessageValues(message, msgKey, elementId, topic);
        }
    }

    private void applyMessageValues(MessageContainer message, String msgKey, String elementId, String topic) {
        message.setMessage(message.getMessage() + "\\r\\n" + Utils.getMessageOrKey(msgKey));
        if (message.getTopic().isEmpty()) {
            message.setTopic(topic);
            message.setElementId(elementId);
        }
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        _staffProof.setAccountId(_sessionController.getAccountId());
        setModifiedInfo();
        _staffProof = _psychStaffFacade.saveStaffProof(_staffProof);
        return "";
    }

    public List<SelectItem> getIks() {
        Set<Integer> iks = new HashSet<>();
        if (_staffProof != null && _staffProof.getIk() > 0) {
            iks.add(_staffProof.getIk());
        }
        Account account = _sessionController.getAccount();
        if (account.getIK() != null && account.getIK() > 0) {
            iks.add(account.getIK());
        }
        for (AccountAdditionalIK additionalIK : account.getAdditionalIKs()) {
            iks.add(additionalIK.getIK());
        }
        List<SelectItem> items = new ArrayList<>();
        for (int ik : iks) {
            items.add(new SelectItem(ik));
        }
        return items;
    }
    // </editor-fold>

    public String determineFactor(StaffProofEffective effective) {
        StaffProofAgreed agreed = _staffProof.getStaffProofsAgreed(effective.getPsychType())
                .stream()
                .filter(a -> a.getOccupationalCatagoryId() == effective.getOccupationalCatagoryId())
                .findFirst().orElse(new StaffProofAgreed());
        double denominator = agreed.getStaffingComplete();
        if (denominator == 0) {
            return "";
        }
        double factor = 100 * effective.getStaffingComplete() / denominator;
        return String.format("%.1f", factor) + " %";
    }

    public String sumAgreedStaffingComplete(PsychType type) {
        double sum = _staffProof.getStaffProofsAgreed(type).stream().mapToDouble(i -> i.getStaffingComplete()).sum();
        return String.format("%.1f", sum);
    }

    public String sumAgreedStaffingBudget(PsychType type) {
        double sum = _staffProof.getStaffProofsAgreed(type).stream().mapToDouble(i -> i.getStaffingBudget()).sum();
        return String.format("%.1f", sum);
    }

    public String sumEffectiveStaffingComplete(PsychType type) {
        double sum = _staffProof.getStaffProofsEffective(type).stream().mapToDouble(i -> i.getStaffingComplete()).sum();
        return String.format("%.1f", sum);
    }

    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    public void uploadFile() {
        PsychType type;
        int appendix;
        String signature;

        switch (getActiveTopicKey()) {
            case TOPIC_MASTER:
                return;
            case TOPIC_ADULTS1:
                type = PsychType.Adults;
                appendix = 1;
                signature = _staffProof.getChecksumAgreement(type);
                break;
            case TOPIC_KIDS1:
                type = PsychType.Kids;
                appendix = 1;
                signature = _staffProof.getChecksumAgreement(type);
                break;
            case TOPIC_ADULTS2:
                type = PsychType.Adults;
                appendix = 2;
                signature = _staffProof.getChecksumEffective(type);
                break;
            case TOPIC_KIDS2:
                type = PsychType.Kids;
                appendix = 2;
                signature = _staffProof.getChecksumEffective(type);
                break;
            default:
                return;
        }
        try {
            if (_file != null) {
                InputStream is = _file.getInputStream();
                byte[] content = StreamUtils.stream2blob(is);
                StaffProofDocument document = new StaffProofDocument(_file.getSubmittedFileName());
                document.setContent(content);
                document.setPsychType(type);
                document.setAppendix(appendix);
                document.setSignature(signature);
                _staffProof.addStaffProofDocument(document);
                _file = null;
            }
        } catch (IOException | NoSuchElementException e) {
        }
    }

}
