/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.psychstaff.backingbean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.feature.psychstaff.entity.ExclusionFact;
import org.inek.dataportal.feature.psychstaff.entity.OccupationalCategory;
import org.inek.dataportal.feature.psychstaff.entity.StaffProof;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofDocument;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofEffective;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofExplanation;
import org.inek.dataportal.feature.psychstaff.enums.PsychType;
import org.inek.dataportal.feature.psychstaff.facade.PsychStaffFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.MessageContainer;
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
    private static final String TOPIC_BASE = "topicBaseData";
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
        addTopic(TOPIC_BASE, Pages.PsychStaffBaseData.URL());
        addTopic(TOPIC_ADULTS1, Pages.PsychStaffAppendix1Adults.URL());
        addTopic(TOPIC_KIDS1, Pages.PsychStaffAppendix1Kids.URL());
        addTopic(TOPIC_ADULTS2, Pages.PsychStaffAppendix2Adults.URL());
        addTopic(TOPIC_KIDS2, Pages.PsychStaffAppendix2Kids.URL());
    }

    @Override
    protected void topicChanged() {
        if (_sessionController.getAccount().isAutoSave() && !isReadOnly()) {
            save(false);
        }
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
            setYearToFirstAvailable(staffProof);
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
        setYearToFirstAvailable(_staffProof);
    }

    private void setYearToFirstAvailable(StaffProof staffProof) {
        List<Integer> years = getYears(staffProof);
        if (years.size() > 0) {
            staffProof.setYear(years.get(0));
        }
    }

    public void domainChanged() {
        setTopicVisibility();
        ensureStaffProofsAgreed(_staffProof);
        ensureStaffProofsEffective(_staffProof);
    }

    public List<Integer> getYears() {
        return getYears(_staffProof);
    }

    public List<Integer> getYears(StaffProof staffProof) {
        if (staffProof == null) {
            return new ArrayList<>();
        }

        List<Integer> existingYears = _psychStaffFacade.getExistingYears(staffProof.getIk());

        List<Integer> availableYears = new ArrayList<>();
        IntStream.rangeClosed(2016, 2019) // as of the contract
                .filter(y -> y == staffProof.getYear() || !existingYears.contains(y))
                .forEach(y -> availableYears.add(y));

        return availableYears;
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
        for (OccupationalCategory cat : getOccupationalCategories()) {
            StaffProofAgreed agreed = new StaffProofAgreed();
            agreed.setStaffProofMasterId(staffProof.getId());
            agreed.setPsychType(type);
            agreed.setOccupationalCategory(cat);
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
        for (OccupationalCategory cat : getOccupationalCategories()) {
            StaffProofEffective effective = new StaffProofEffective();
            effective.setStaffProofMasterId(staffProof.getId());
            effective.setPsychType(type);
            effective.setOccupationalCategory(cat);
            staffProof.addStaffProofEffective(effective);
        }
    }

    public List<OccupationalCategory> getOccupationalCategories() {
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
        return save(true);
    }

    private String save(boolean showMessage) {
        setModifiedInfo();
        _staffProof = _psychStaffFacade.saveStaffProof(_staffProof);

        if (isValidId(_staffProof.getId())) {
            if (showMessage) {
                // CR+LF or LF only will be replaced by "\r\n"
                String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
                _sessionController.setScript(script);
            }
            return null;
        }
        return Pages.Error.URL();
    }

    private void setModifiedInfo() {
        _staffProof.setLastChanged(Calendar.getInstance().getTime());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isCloseEnabled() {
        if (!isSendEnabled()) {
            return false;
        }
        if (!_cooperationTools.isSealedEnabled(Feature.PSYCH_STAFF, _staffProof.getStatus(), _staffProof.getAccountId())) {
            return false;
        }
        switch (getActiveTopicKey()) {
            case TOPIC_BASE:
                return false;
            case TOPIC_ADULTS1:
            case TOPIC_KIDS1:
                return _staffProof.getStatusApx1() < WorkflowStatus.Provided.getId();
            case TOPIC_ADULTS2:
            case TOPIC_KIDS2:
                return _staffProof.getStatusApx2() < WorkflowStatus.Provided.getId();
            default:
                return false;
        }
    }

    public boolean isClosedState() {
        switch (getActiveTopicKey()) {
            case TOPIC_BASE:
                return false;
            case TOPIC_ADULTS1:
            case TOPIC_KIDS1:
                return _staffProof.getStatusApx1() >= WorkflowStatus.Provided.getId();
            case TOPIC_ADULTS2:
            case TOPIC_KIDS2:
                return _staffProof.getStatusApx2() >= WorkflowStatus.Provided.getId();
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

    public String getBtnClose() {
        String type = "("
                + (_staffProof.isForAdults() ? PsychType.Adults.getShortName() : "")
                + (_staffProof.isForAdults() && _staffProof.isForKids() ? " + " : "")
                + (_staffProof.isForKids() ? PsychType.Kids.getShortName() : "") 
                + ")";
        switch (getActiveTopicKey()) {
            case TOPIC_BASE:
                return "";
            case TOPIC_ADULTS1:
            case TOPIC_KIDS1:
                return "Eingabe Anhang 1 " + type + " abschließen";
            case TOPIC_ADULTS2:
            case TOPIC_KIDS2:
                return "Eingabe Anhang 2 " + type + " abschließen";
            default:
                return "";
        }

    }

    public String close() {
        if (updateStatus(WorkflowStatus.Provided)) {
            save(false);
        }
        return null;
    }

    private boolean updateStatus(WorkflowStatus newStatus) {
        switch (getActiveTopicKey()) {
            case TOPIC_BASE:
                return false;
            case TOPIC_ADULTS1:
            case TOPIC_KIDS1:
                _staffProof.setStatusApx1(newStatus.getId());
                _staffProof.setStatusApx1Changed(new Date());
                break;
            case TOPIC_ADULTS2:
            case TOPIC_KIDS2:
                _staffProof.setStatusApx2(newStatus.getId());
                _staffProof.setStatusApx2Changed(new Date());
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
            save(false);
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

    public boolean isTakeEnabled() {
        return _cooperationTools != null
                && _staffProof != null
                && _cooperationTools.isTakeEnabled(Feature.PSYCH_STAFF, _staffProof.getStatus(), _staffProof.getAccountId());
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
        checkField(message, ik, "lblIK", "psychStaff:ikMulti", TOPIC_BASE);
        checkField(message, _staffProof.getYear(), 2016, 2020, "lblAgreementYear", "psychStaff:year", TOPIC_BASE);
        if (!_staffProof.isForAdults() && !_staffProof.isForKids()) {
            String msg = "Bitte angeben, ob die Einrichtung für Erwachsene und / oder Kinder und Jugendliche ist.";
            applyMessageValues(message, msg, "psychStaff:adults", TOPIC_BASE);
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
            String msg = "Bitte Stellenbesetzung für " + item.getOccupationalCategory().getName() + " angeben.";
            checkField(message, item.getStaffingComplete(), 0.1, null, msg, "", topic);
            checkField(message, item.getStaffingBudget(), 0.1, null, msg, "", topic);
            msg = "Bitte Durchschnittskosten für " + item.getOccupationalCategory().getName() + " angeben.";
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
                .filter(a -> a.getOccupationalCategoryId() == effective.getOccupationalCategoryId())
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

    public String sumEffectiveStaffingDeductionPsych(PsychType type) {
        double sum = _staffProof.getStaffProofsEffective(type).stream().mapToDouble(i -> i.getStaffingDeductionPsych()).sum();
        return String.format("%.1f", sum);
    }

    public String sumEffectiveStaffingDeductionNonPsych(PsychType type) {
        double sum = _staffProof.getStaffProofsEffective(type).stream().mapToDouble(i -> i.getStaffingDeductionNonPsych()).sum();
        return String.format("%.1f", sum);
    }

    public String sumEffectiveStaffingDeductionOther(PsychType type) {
        double sum = _staffProof.getStaffProofsEffective(type).stream().mapToDouble(i -> i.getStaffingDeductionOther()).sum();
        return String.format("%.1f", sum);
    }

    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    public String getAllowedFileExtensions() {
        return allowedFileExtensions().stream().collect(Collectors.joining(", "));
    }

    private List<String> allowedFileExtensions() {
        return Arrays.asList(".pdf", ".jpg", ".jpeg", ".png", ".bmp", ".gif");
    }

    public void uploadFile() {
        if (_file == null) {
            return;
        }

        int appendix;
        String signature;

        switch (getActiveTopicKey()) {
            case TOPIC_BASE:
                return;
            case TOPIC_ADULTS1:
            case TOPIC_KIDS1:
                appendix = 1;
                signature = _staffProof.getSignatureEffective();
                break;
            case TOPIC_ADULTS2:
            case TOPIC_KIDS2:
                appendix = 2;
                signature = _staffProof.getSignatureEffective();
                break;
            default:
                return;
        }
        try {
            String fileName = _file.getSubmittedFileName();
            int pos = fileName.lastIndexOf(".");
            String extension = pos < 0 ? "" : fileName.toLowerCase().substring(pos);
            if (allowedFileExtensions().contains(extension)) {
                InputStream is = _file.getInputStream();
                byte[] content = StreamUtils.stream2blob(is);
                StaffProofDocument document = new StaffProofDocument(fileName);
                document.setContent(content);
                document.setAppendix(appendix);
                document.setSignature(signature);
                _staffProof.addStaffProofDocument(document);
            }
            _file = null;
        } catch (IOException | NoSuchElementException e) {
        }
        _file = null;
    }

    public String downloadDocument(String signature) {
        StaffProofDocument doc = _staffProof.getStaffProofDocument(signature);
        Utils.downloadDocument(doc);
        return "";
    }

    public void importData() {
        if (_file == null) {
            return;
        }

        String msg = "";
        switch (getActiveTopicKey()) {
            case TOPIC_BASE:
                return;
            case TOPIC_ADULTS1:
                msg = PsychStaffImporter.importAgreed(_file, _staffProof, PsychType.Adults);
                break;
            case TOPIC_KIDS1:
                msg = PsychStaffImporter.importAgreed(_file, _staffProof, PsychType.Kids);
                break;
            case TOPIC_ADULTS2:
                msg = PsychStaffImporter.importEffective(_file, _staffProof, PsychType.Adults);
                break;
            case TOPIC_KIDS2:
                msg = PsychStaffImporter.importEffective(_file, _staffProof, PsychType.Kids);
                break;
            default:
                return;
        }

        _sessionController.alertClient(msg);
    }

    public void countChanged(StaffProofEffective item, int key) {
        double count;
        switch (key) {
            case 4:
                count = item.getStaffingDeductionPsych();
                break;
            case 5:
                count = item.getStaffingDeductionNonPsych();
                break;
            case 6:
                count = item.getStaffingDeductionOther();
                break;
            default:
                throw new IllegalArgumentException("Unknown deduction key: " + key);
        }
        if (count == 0) {
            _staffProof.removeStaffProofExplanation(item.getPsychType(), item.getOccupationalCategory(), key);
            return;
        }
        _staffProof.addMissingStaffProofExplanation(item.getPsychType(), item.getOccupationalCategory(), key);

    }

    public void adjustLines(StaffProofExplanation explanation) {
        if (explanation.getDeductedFullVigor() == 0) {
            return;
        }
        PsychType type = explanation.getPsychType();
        OccupationalCategory occupationalCategory = explanation.getOccupationalCategory();
        int key = explanation.getDeductedSpecialistId();
        StaffProofEffective staffProofEffective = _staffProof
                .getStaffProofsEffective(type)
                .stream()
                .filter(e -> e.getOccupationalCategory().getId() == occupationalCategory.getId())
                .findAny()
                .get();
        double deductionCount;
        switch (key) {
            case 4:
                deductionCount = staffProofEffective.getStaffingDeductionPsych();
                break;
            case 5:
                deductionCount = staffProofEffective.getStaffingDeductionNonPsych();
                break;
            case 6:
                deductionCount = staffProofEffective.getStaffingDeductionOther();
                break;
            default:
                deductionCount = 0;
        }
        double sum = _staffProof
                .getStaffProofExplanations(type)
                .stream()
                .filter(e -> e.getOccupationalCategory().getId() == occupationalCategory.getId() && e.getDeductedSpecialistId() == key)
                .mapToDouble(e -> e.getDeductedFullVigor())
                .sum();
        if (deductionCount - sum > 0) {
            _staffProof.addStaffProofExplanation(type, occupationalCategory, key);
        }
    }
    
    private List<ExclusionFact> _exclusionFacts;
    public List<ExclusionFact> getExclusionFacts (){
        if (_exclusionFacts == null){
            _exclusionFacts = _psychStaffFacade.getExclusionFacts();
        }
        return _exclusionFacts
                .stream()
                .filter(f -> f.getYearFrom() <= _staffProof.getYear() && f.getYearTo() >= _staffProof.getYear())
                .collect(Collectors.toList());
    }
}
