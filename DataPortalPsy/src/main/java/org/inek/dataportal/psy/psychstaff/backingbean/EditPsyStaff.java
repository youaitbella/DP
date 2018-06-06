/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.psychstaff.backingbean;

import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.servlet.http.Part;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.psy.psychstaff.entity.ExclusionFact;
import org.inek.dataportal.psy.psychstaff.entity.OccupationalCategory;
import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofDocument;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofEffective;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofExplanation;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;
import org.inek.dataportal.psy.psychstaff.facade.PsychStaffFacade;
import org.inek.dataportal.psy.psychstaff.pdf.PdfBuilder;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.utils.DateUtils;
import org.inek.dataportal.common.controller.ReportController;

/**
 *
 * @author muellermi
 */
@SuppressWarnings("JavaNCSS")
@Named
@FeatureScoped
public class EditPsyStaff extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger LOGGER = Logger.getLogger("EditPsyStaff");
    private static final String TOPIC_BASE = "topicBaseData";
    private static final String TOPIC_ADULTS1 = "topicAppendix1Adults";
    private static final String TOPIC_KIDS1 = "topicAppendix1Kids";
    private static final String TOPIC_ADULTS2 = "topicAppendix2Adults";
    private static final String TOPIC_KIDS2 = "topicAppendix2Kids";
    private static final String PDF_DOCUMENT_APX1 = "PsychPersonalNachweis_Anlage1.pdf";
    private static final String PDF_DOCUMENT_APX2 = "PsychPersonalNachweis_Anlage2.pdf";
    private static final String EXCEL_DOCUMENT = "PsychPersonalNachweis.xlsx";

    private AccessManager _accessManager;
    private SessionController _sessionController;
    private ReportController _reportController;
    private PsychStaffFacade _psychStaffFacade;
    private ApplicationTools _appTools;

    public EditPsyStaff() {
    }

    @Inject
    public EditPsyStaff(AccessManager accessManager,
            SessionController sessionController,
            ReportController reportController,
            PsychStaffFacade psychStaffFacade,
            ApplicationTools appTools) {
        _accessManager = accessManager;
        _sessionController = sessionController;
        _reportController = reportController;
        _psychStaffFacade = psychStaffFacade;
        _appTools = appTools;
    }

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
        if (!isReadOnly()) {
            save(false);
        }
    }

//    @Override
//    protected String getOutcome() {
//        return "";
//    }
    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        String id = "" + params.get("id");
        if ("new".equals(id)) {
            _staffProof = newStaffProof();
            setTopicVisibility();
            return;
        }
        if (!loadStaffProof(id)) {
            Utils.navigate(Pages.MainApp.RedirectURL());
            return;
        }
        setTopicVisibility();
    }

    private boolean loadStaffProof(String idString) {
        try {
            int id = Integer.parseInt(idString);
            StaffProof staffProof = _psychStaffFacade.findStaffProof(id);
            if (staffProof != null && hasSufficientRights(staffProof)) {
                ensureStaffProofsAgreed(staffProof);
                ensureStaffProofsEffective(staffProof);
                _staffProof = staffProof;
                return true;
            }
        } catch (NumberFormatException ex) {
            LOGGER.log(Level.WARNING, "EditPsyStaff called with non-numeric id: {0}", idString);
        }
        // assgin _staffproof to prevent from null exceptions, 
        // coz some methodes like isReadOnly are queried before navigation to error page took place
        _staffProof = new StaffProof();
        return false;
    }

    private boolean hasSufficientRights(StaffProof staffProof) {
        return _accessManager.isAccessAllowed(Feature.PSYCH_STAFF, staffProof.getStatus(),
                staffProof.getAccountId(), staffProof.getIk());
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
        ExclusionFact noneFact = obtainExclusionFacts()
                .stream()
                .filter(f -> f.getId() == 0)
                .findFirst()
                .get();
        staffProof.setExclusionFact1(noneFact);
        staffProof.setExclusionFact2(noneFact);
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

    public void exclusionFactChanged1() {
        // since a exclusionFactConverter would create a new object (can be solved with JSF 2.3), 
        // we only use the id and retrieve the appropriate ExclusionFact
        ExclusionFact ef = obtainExclusionFacts()
                .stream()
                .filter(f -> f.getId() == _staffProof.getExclusionFactId1())
                .findFirst()
                .get();
        _staffProof.setExclusionFact1(ef);
    }

    public void exclusionFactChanged2() {
        // since a exclusionFactConverter would create a new object (can be solved with JSF 2.3), 
        // we only use the id and retrieve the appropriate ExclusionFact
        ExclusionFact ef = obtainExclusionFacts()
                .stream()
                .filter(f -> f.getId() == _staffProof.getExclusionFactId2())
                .findFirst()
                .get();
        _staffProof.setExclusionFact2(ef);
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
        if (staffProof.getId() > 0) {
            LOGGER.log(Level.INFO, "Load StaffProofAgreed for existing data. Id: {0} Type: {1}",
                    new Object[]{staffProof.getId(), type.getShortName()});
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
        if (staffProof.getId() > 0) {
            LOGGER.log(Level.INFO, "Load StaffProofEffective for existing data. Id: {0} Type: {1}",
                    new Object[]{staffProof.getId(), type.getShortName()});
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
//        if (_sessionController.isInekUser(Feature.PSYCH_STAFF)
//                && _staffProof.getAccountId() != _sessionController.getAccountId()) {
//            return true;
//        }
        if (isCompleteSinceMoreThan3Days()) {
            return true;
        }
        return _accessManager.isReadOnly(Feature.PSYCH_STAFF, _staffProof.getStatus(),
                _staffProof.getAccountId(), _staffProof.getIk());
    }

    public String save() {
        return save(true);
    }

    private String save(boolean showMessage) {
        if (IsReasonMissing()) {
            String script = "alert ('Bitte geben Sie zum Ausnahmetatbestand eine Begründung an.');";
            _sessionController.setScript(script);
            return null;
        }
        if (_staffProof.getYear() < 2016) {
            String script = "alert ('Ohne Angabe eines Datenjahrs können Sie nicht speichern.\\r\\n"
                    + "Je IK kann jedes Datenjahr nur einmal gewählt werden.');";
            _sessionController.setScript(script);
            return null;
        }
        cleanUneededReason();
        setModifiedInfo();
        try {
            _staffProof = _psychStaffFacade.saveStaffProof(_staffProof);
            if (_staffProof.isForAdults()) {
                adjustAllLines(PsychType.Adults);
            }
            if (_staffProof.isForKids()) {
                adjustAllLines(PsychType.Kids);
            }

            if (_staffProof.getId() >= 0) {
                if (showMessage) {
                    // CR+LF or LF only will be replaced by "\r\n"
                    String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").
                            replace("\n", "\\r\\n") + "');";
                    _sessionController.setScript(script);
                }
                return null;
            } else {
                LOGGER.log(Level.WARNING, "EditPsyStaff with invalid id after save. IK = {0}", _staffProof.getIk());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception during save PsychStaff", ex);
            if (ex.getCause() instanceof OptimisticLockException) {
                LOGGER.log(Level.WARNING, "Optimistic Lock Exception during save PsychStaff");
                return null;
            }
        }

        return Pages.Error.URL();
    }

    private boolean IsReasonMissing() {
        return _staffProof.getExclusionFact1().isNeedReason() && _staffProof.getExclusionReason1().isEmpty()
                || _staffProof.getExclusionFact2().isNeedReason() && _staffProof.getExclusionReason2().isEmpty();
    }

    private void cleanUneededReason() {
        if (!_staffProof.getExclusionFact1().isNeedReason()) {
            _staffProof.setExclusionReason1("");
        }
        if (!_staffProof.getExclusionFact2().isNeedReason()) {
            _staffProof.setExclusionReason2("");
        }
    }

    private void setModifiedInfo() {
        _staffProof.setLastChanged(Calendar.getInstance().getTime());
    }

    public boolean isCloseEnabled() {
        if (!isSendEnabled()) {
            return false;
        }
        if (!_accessManager.isSealedEnabled(Feature.PSYCH_STAFF, _staffProof.getStatus(),
                _staffProof.getAccountId(), _staffProof.getIk())) {
            return false;
        }
        switch (getActiveTopicKey()) {
            case TOPIC_BASE:
                return false;
            case TOPIC_ADULTS1:
            case TOPIC_KIDS1:
                return _staffProof.getStatusApx1() < WorkflowStatus.Provided.getId()
                        && _staffProof.getExclusionFact1().isCanSeal();
            case TOPIC_ADULTS2:
            case TOPIC_KIDS2:
                return _staffProof.getStatusApx2() < WorkflowStatus.Provided.getId()
                        && _staffProof.getExclusionFact2().isCanSeal();
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
        if (!_accessManager.isSealedEnabled(Feature.PSYCH_STAFF, _staffProof.getStatus(),
                _staffProof.getAccountId(), _staffProof.getIk())) {
            return false;
        }
        return isClosedState();
    }

    public boolean isPdfExportEnabled() {
        switch (getActiveTopicKey()) {
            case TOPIC_BASE:
                return false;
            case TOPIC_ADULTS1:
            case TOPIC_KIDS1:
                return _staffProof.getStatusApx1() >= WorkflowStatus.Provided.getId() && _staffProof.
                        getExclusionFactId1() == 0;
            case TOPIC_ADULTS2:
            case TOPIC_KIDS2:
                return _staffProof.getStatusApx2() >= WorkflowStatus.Provided.getId() && _staffProof.
                        getExclusionFactId2() == 0;
            default:
                return false;
        }
    }

    public boolean isPdfImportEnabled() {
        return isPdfExportEnabled() && !isCompleteSinceMoreThan3Days();
    }

    public boolean isCompleteSinceMoreThan3Days() {
        return isComplete() && (_staffProof.getLastChanged().getTime() < DateUtils.getDateWithDayOffset(-3).getTime());
    }

    public boolean isComplete() {
        boolean apx1Ready = _staffProof.getStatusApx1() == WorkflowStatus.Provided.getId()
                && (_staffProof.getExclusionFactId1() > 0 || _staffProof.hasStaffProofDocument(1));
        boolean apx2Ready = _staffProof.getStatusApx2() == WorkflowStatus.Provided.getId()
                && (_staffProof.getExclusionFactId2() > 0 || _staffProof.hasStaffProofDocument(2));
        return apx1Ready && apx2Ready;
    }

    public boolean isDataExportEnabled() {
        if (_staffProof == null) {
            // shall not be null!
            LOGGER.log(Level.SEVERE, "isDataExportEnabled: _staffProof is null");
            return false;
        }
        // ready, if provided and (exclusion fact or document exists)
        return isComplete() && _reportController.reportTemplateExists(EXCEL_DOCUMENT);
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
                return "Eingabe Anlage 1 " + type + " abschließen";
            case TOPIC_ADULTS2:
            case TOPIC_KIDS2:
                return "Eingabe Anlage 2 " + type + " abschließen";
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
        if (isCompleteSinceMoreThan3Days()) {
            return false;
        }
        if (!_accessManager.isSealedEnabled(Feature.PSYCH_STAFF, _staffProof.getStatus(),
                _staffProof.getAccountId(), _staffProof.getIk())) {
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
        return _staffProof != null;
    }

    public boolean isTakeEnabled() {
        return _accessManager != null
                && _staffProof != null
                && _accessManager.isTakeEnabled(Feature.PSYCH_STAFF, _staffProof.getStatus(),
                        _staffProof.getAccountId(), _staffProof.getIk());
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
        Account account = _sessionController.getAccount();
        Set<Integer> iks = account.getFullIkSet();
        if (_staffProof != null && _staffProof.getIk() > 0) {
            iks.add(_staffProof.getIk());
        }
        List<SelectItem> items = new ArrayList<>();
        for (int ik : iks) {
            items.add(new SelectItem(ik));
        }
        return items;
    }
    // </editor-fold>

    public String determineFactor(StaffProofEffective effective) {
        if (_staffProof.getExclusionFactId1() > 0) {
            return "";
        }
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

    public double sumAgreedStaffingComplete(PsychType type) {
        return _staffProof.getStaffProofsAgreed(type).stream().mapToDouble(i -> i.getStaffingComplete()).sum();
    }

    public double sumAgreedStaffingBudget(PsychType type) {
        return _staffProof.getStaffProofsAgreed(type).stream().mapToDouble(i -> i.getStaffingBudget()).sum();
    }

    public double sumEffectiveStaffingComplete(PsychType type) {
        return _staffProof.getStaffProofsEffective(type).stream().mapToDouble(i -> i.getStaffingComplete()).sum();
    }

    public double sumEffectiveStaffingDeductionPsych(PsychType type) {
        return _staffProof.getStaffProofsEffective(type).stream().mapToDouble(i -> i.getStaffingDeductionPsych()).sum();
    }

    public double sumEffectiveStaffingDeductionNonPsych(PsychType type) {
        return _staffProof.getStaffProofsEffective(type).stream().mapToDouble(i -> i.getStaffingDeductionNonPsych()).
                sum();
    }

    public double sumEffectiveStaffingDeductionOther(PsychType type) {
        return _staffProof.getStaffProofsEffective(type).stream().mapToDouble(i -> i.getStaffingDeductionOther()).sum();
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

    public void putDocument(String fileName, byte[] content) {
        int appendix;
        String signature;

        switch (getActiveTopicKey()) {
            case TOPIC_BASE:
                return;
            case TOPIC_ADULTS1:
            case TOPIC_KIDS1:
                appendix = 1;
                signature = _staffProof.getSignatureAgreement();
                break;
            case TOPIC_ADULTS2:
            case TOPIC_KIDS2:
                appendix = 2;
                signature = _staffProof.getSignatureEffective();
                break;
            default:
                return;
        }

        int pos = fileName.lastIndexOf(".");
        String extension = pos < 0 ? "" : fileName.toLowerCase().substring(pos);
        if (allowedFileExtensions().contains(extension)) {
            StaffProofDocument document = new StaffProofDocument(fileName);
            document.setContent(content);
            document.setAppendix(appendix);
            document.setSignature(signature);
            _staffProof.addStaffProofDocument(document);
            save(true);
        }
    }

    public String refresh() {
        return "";
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

    public void importExplanation() {
        if (_file == null) {
            return;
        }

        String msg = "";
        switch (getActiveTopicKey()) {
            case TOPIC_BASE:
                return;
            case TOPIC_ADULTS1:
                return;
            case TOPIC_KIDS1:
                return;
            case TOPIC_ADULTS2:
                msg = PsychStaffImporter.importExplanation(_file, _staffProof, PsychType.Adults);
                break;
            case TOPIC_KIDS2:
                msg = PsychStaffImporter.importExplanation(_file, _staffProof, PsychType.Kids);
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
        adjustLine(staffProofEffective, key);
    }

    public void adjustAllLines(String typeString) {
        PsychType type = PsychType.valueOf(typeString);
        adjustAllLines(type);
    }

    public void adjustAllLines(PsychType type) {
        for (StaffProofEffective staffProofEffective : _staffProof.getStaffProofsEffective(type)) {
            for (int key = 4; key <= 6; key++) {
                adjustLine(staffProofEffective, key);
            }
        }
    }

    private void adjustLine(StaffProofEffective staffProofEffective, int key) {
        OccupationalCategory occupationalCategory = staffProofEffective.getOccupationalCategory();
        PsychType type = staffProofEffective.getPsychType();
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
                .filter(e -> e.getOccupationalCategory().getId() == occupationalCategory.getId() && e.
                getDeductedSpecialistId() == key)
                .mapToDouble(e -> e.getDeductedFullVigor())
                .sum();
        if (deductionCount - sum > 0) {
            boolean existsEmpty = _staffProof
                    .getStaffProofExplanations(type)
                    .stream()
                    .anyMatch(e -> e.getOccupationalCategory().getId() == occupationalCategory.getId()
                    && e.getDeductedSpecialistId() == key
                    && e.getDeductedFullVigor() == 0);
            if (!existsEmpty) {
                _staffProof.addStaffProofExplanation(type, occupationalCategory, key);
            }
        }
    }

    public List<ExclusionFact> getExclusionFacts() {
        return obtainExclusionFacts()
                .stream()
                .filter(f -> f.getYearFrom() <= _staffProof.getYear() && f.getYearTo() >= _staffProof.getYear())
                .collect(Collectors.toList());
    }

    public List<ExclusionFact> getExclusionFacts1() {
        return obtainExclusionFacts()
                .stream()
                .filter(f -> f.getYearFrom() <= _staffProof.getYear() && f.getYearTo() >= _staffProof.getYear() && f.
                isAffectsApx1())
                .collect(Collectors.toList());
    }

    public List<ExclusionFact> getExclusionFacts2() {
        return obtainExclusionFacts()
                .stream()
                .filter(f -> f.getYearFrom() <= _staffProof.getYear() && f.getYearTo() >= _staffProof.getYear() && f.
                isAffectsApx2())
                .collect(Collectors.toList());
    }

    public void createDocument() {
        // just for testing. Need to determin current apx
        //_reportController.createSingleDocument(PDF_DOCUMENT_APX1, _staffProof.getId());
        try {
            new PdfBuilder(this).createDocument();
        } catch (DocumentException | IOException | NoSuchAlgorithmException ex) {
            LOGGER.log(Level.WARNING, "Error during PDF creation: {0}", ex.getMessage());
        }
    }

    public void exportData() {
        String fileName = EXCEL_DOCUMENT.substring(0, EXCEL_DOCUMENT.lastIndexOf(".")) + "_" + _staffProof.getIk() + ".xlsx";
        _reportController.createSingleDocument(EXCEL_DOCUMENT, _staffProof.getId(), fileName);
    }

    public void deleteExplanation(StaffProofExplanation item) {
        _staffProof.removeStaffProofExplanation(item);
    }

    public void exportAllData() {
        PsyStaffExport exporter = new PsyStaffExport(_reportController);
        exporter.exportAllData(EXCEL_DOCUMENT, _staffProof);
    }

    public String retrieveHospitalInfo() {
        return _appTools.retrieveHospitalInfo(_staffProof.getIk());
    }

    private List<ExclusionFact> _exclusionFacts;

    public List<ExclusionFact> obtainExclusionFacts() {
        if (_exclusionFacts == null) {
            _exclusionFacts = _psychStaffFacade.findAllExclusionFacts();
        }
        return _exclusionFacts;
    }
    
    public void setLastChangeAtNow() {
        _staffProof.setLastChanged(Calendar.getInstance().getTime());
        _staffProof = _psychStaffFacade.saveStaffProof(_staffProof);
    }
}
