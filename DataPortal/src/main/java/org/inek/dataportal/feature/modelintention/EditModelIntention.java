package org.inek.dataportal.feature.modelintention;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.common.RemunerationType;
import org.inek.dataportal.entities.modelintention.Adjustment;
import org.inek.dataportal.entities.modelintention.AgreedPatients;
import org.inek.dataportal.entities.modelintention.Cost;
import org.inek.dataportal.entities.modelintention.ModelIntention;
import org.inek.dataportal.entities.modelintention.ModelIntentionContact;
import org.inek.dataportal.entities.modelintention.ModelLife;
import org.inek.dataportal.entities.modelintention.Quality;
import org.inek.dataportal.entities.modelintention.Remuneration;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.Region;
import org.inek.dataportal.enums.SelfHospitalisationType;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.common.RemunerationTypeFacade;
import org.inek.dataportal.facades.modelintention.ModelIntentionFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author vohldo/schlappajo
 */
@Named
@FeatureScoped
public class EditModelIntention extends AbstractEditController {

    private static final Logger LOGGER = Logger.getLogger("EditModelIntention");

    @Inject private SessionController _sessionController;
    @Inject private ModelIntentionFacade _modelIntentionFacade;
    private boolean _regionMiscEnabled;
    private ModelIntention _modelIntention;

    @Override
    protected void addTopics() {
        addTopic(ModelIntentionTabs.tabModelIntTypeAndNumberOfPatients.name(), Pages.ModelIntentionTypeAndNumPat.URL());
        addTopic(ModelIntentionTabs.tabModelIntGoals.name(), Pages.ModelIntentionGoals.URL());
        addTopic(ModelIntentionTabs.tabModelIntTreatmentAreasAndCosts.name(), Pages.ModelIntentionTreatAreaAndCosts.URL());
        addTopic(ModelIntentionTabs.tabModelIntStructures.name(), Pages.ModelIntentionStructure.URL());
        addTopic(ModelIntentionTabs.tabModelIntQualityAndSupervision.name(), Pages.ModelIntentionQuality.URL());
    }

    public boolean isRegionMiscEnabled() {
        return _regionMiscEnabled;
    }

    public void setRegionMiscEnabled(boolean regionMiscEnabled) {
        this._regionMiscEnabled = regionMiscEnabled;
    }

    public SelectItem[] getSelfHospitalisationTypes() {
        List<SelectItem> l = new ArrayList<>();
        SelfHospitalisationType[] types = SelfHospitalisationType.values();
        for (SelfHospitalisationType sh : types) {
            l.add(new SelectItem(sh.id(), sh.type()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }

    enum ModelIntentionTabs {

        tabModelIntTypeAndNumberOfPatients,
        tabModelIntGoals,
        tabModelIntTreatmentAreasAndCosts,
        tabModelIntStructures,
        tabModelIntQualityAndSupervision;
    }

    public EditModelIntention() {
        _regionMiscEnabled = false;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public ModelIntention getModelIntention() {
        return _modelIntention;
    }
    // </editor-fold>

    @PostConstruct
    private void init() {
        Object id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
        } else if (id.toString().equals("new")) {
            if (_sessionController.isInekUser(Feature.MODEL_INTENTION)) {
                _modelIntention = newModelIntention();
            } else {
                Utils.navigate(Pages.MainApp.URL());
                return;
            }
        } else {
            _modelIntention = loadModelIntention(id);
        }
        
        if (_modelIntention.getRegion() != null && _modelIntention.getRegion().equals(Region.Misc.region())) {
            _regionMiscEnabled = true;
        }
        ensureEmptyEntries();
    }

    private ModelIntention loadModelIntention(Object ppId) {
        try {
            int id = Integer.parseInt("" + ppId);
            ModelIntention modelIntention = _modelIntentionFacade.find(id);
            return modelIntention;
        } catch (NumberFormatException ex) {
            LOGGER.info(ex.getMessage());
        }
        return newModelIntention();
    }

    private ModelIntention newModelIntention() {
        ModelIntention modelIntention = getModelIntentionController().createModelIntention();
        modelIntention.setAccountId(_sessionController.getAccountId());
        return modelIntention;
    }

    private ModelIntentionController getModelIntentionController() {
        return (ModelIntentionController) _sessionController.getFeatureController(Feature.MODEL_INTENTION);
    }

    @Override
    protected void topicChanged() {
        if (_sessionController.getAccount().isAutoSave() && !isReadOnly()) {
            saveToDatabase();
        }
    }
    
    public String save() {
        if (saveToDatabase()) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            ensureEmptyEntries();
            return "";
        }
        return Pages.Error.URL();
    }

    private boolean saveToDatabase() {
        removeEmptyEntries();
        _modelIntention.setQualities(getInternalQualityTable().getList());
        _modelIntention.getQualities().addAll(getExternalQualityTable().getList());
        removeObsolteTexts();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ModelIntention>> violations = validator.validate(_modelIntention);
        //violations.forEach(v -> LOGGER.log(Level.WARNING, v.getMessage()));
        for (ConstraintViolation<ModelIntention> v : violations) {
            LOGGER.log(Level.WARNING, v.getMessage());
        }
        _modelIntention = _modelIntentionFacade.saveModelIntention(_modelIntention);
        resetDynamicTables();
        return isValidId(_modelIntention.getId());
    }

    private void removeObsolteTexts() {
        // removes texts of corresponding to selectboxes, if text is not needed
        if (_modelIntention.getRegionType() < 2) {
            _modelIntention.setRegion("");
        }
        if (_modelIntention.getMedicalAttributesType() < 0) {
            _modelIntention.setMedicalSpecification("");
        }
        if (_modelIntention.getSettleMedicType() < 1) {
            _modelIntention.setSettleMedicText("");
        }
        if (_modelIntention.getPiaType() < 2) {
            _modelIntention.setPiaText("");
        }
        if (_modelIntention.getHospitalType() < 2) {
            _modelIntention.setHospitalText("");
        }
        if (_modelIntention.getInpatientTreatmentType() < 2) {
            _modelIntention.setInpatientTreatmentText("");
        }
        if (_modelIntention.getDayPatientTreatmentType() < 2) {
            _modelIntention.setDayPatientTreatmentText("");
        }
        if (_modelIntention.getInpatientCompensationTreatmentType() < 2) {
            _modelIntention.setInpatientCompensationTreatmentText("");
        }
        if (_modelIntention.getInpatientCompensationHomeTreatmentType() < 2) {
            _modelIntention.setInpatientCompensationHomeTreatmentText("");
        }
        if (_modelIntention.getOutpatientTreatmentType() < 2) {
            _modelIntention.setOutpatientTreatmentText("");
        }
        if (_modelIntention.getOutpatientHomeTreatmentType() < 2) {
            _modelIntention.setOutpatientHomeTreatmentText("");
        }
        if (_modelIntention.getInternalQuality() < 2) {
            removeQuality(1);
        }
        if (_modelIntention.getExternalQuality() < 2) {
            removeQuality(2);
        }
    }

    private void removeQuality(int type) {
        for (Iterator<Quality> itr = _modelIntention.getQualities().iterator(); itr.hasNext();) {
            Quality entry = itr.next();
            if (entry.getTypeId() == type) {
                itr.remove();
            }
        }
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isReadOnly() {
        return _modelIntention.getStatus().getId() >= WorkflowStatus.Provided.getId()
                || _modelIntention.getAccountId() != _sessionController.getAccountId();
    }

    public boolean isRejectedModelIntention() {
        return WorkflowStatus.Rejected == _modelIntention.getStatus();
    }

    /**
     * requests sealing of a formal request if the form is completely full
     * filled, this function displays a confirmation dialog confirming with "ok"
     * performs a call to seal
     *
     * @return
     */
    public String requestSeal() {
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
        if (_modelIntention.getStatus().getId() >= 10) {
            return Pages.Error.URL();
        }

        // try to save current state first
        if (!saveToDatabase()) {
            String script = "alert ('Fehler beim Speichern');";
            _sessionController.setScript(script);
            ensureEmptyEntries();
            return "";
        }

        if (!requestIsComplete()) {
            return getActiveTopic().getOutcome();
        }

        _modelIntention.setStatus(10 + _modelIntention.getStatus().getId());
        _modelIntention = _modelIntentionFacade.saveModelIntention(_modelIntention);

        if (isValidId(_modelIntention.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameMODEL_INTENTION"));
            Utils.getFlash().put("targetPage", Pages.ModelIntentionSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_modelIntention));
            return Pages.PrintView.URL();
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="CheckElements">
    private String _msg = "";
    private String _elementId = "";
    private String _nextTopic = "";

    private boolean requestIsComplete() {
        _msg = "";
        _nextTopic = "";
        checkFields();

        if (!_msg.isEmpty()) {
            _msg = Utils.getMessage("infoMissingFields") + "\\r\\n" + _msg;
            setActiveTopic(_nextTopic);
            String script = "alert ('" + _msg + "');";
            if (!_elementId.isEmpty()) {
                script += "\r\n document.getElementById('" + _elementId + "').focus();";
            }
            _sessionController.setScript(script);
        }
        return _msg.isEmpty();
    }

    private void checkFields() {
        // tab patient
        checkField(_modelIntention.getDescription(), 
                "lblAppellation", 
                "form:Appelation:idText", 
                ModelIntentionTabs.tabModelIntTypeAndNumberOfPatients);
        if (_modelIntention.getRegionType() == 2) {
            checkField(_modelIntention.getRegion(), 
                    "lblRegionalFeatures", 
                    "form:region:idText", 
                    ModelIntentionTabs.tabModelIntTypeAndNumberOfPatients);
        }
        if (_modelIntention.getMedicalAttributesType() >= 0) {
            checkField(_modelIntention.getMedicalSpecification(), 
                    "lblMedicalFeature", 
                    "form:medicalSpecification:idText", 
                    ModelIntentionTabs.tabModelIntTypeAndNumberOfPatients);
        }
        if (_modelIntention.getSettleMedicType() >= 1) {
            checkField(_modelIntention.getSettleMedicText(), 
                    "lblSettledDocs", 
                    "form:settleMedicText:idText", 
                    ModelIntentionTabs.tabModelIntTypeAndNumberOfPatients);
        }
        if (_modelIntention.getPiaType() >= 2) {
            checkField(_modelIntention.getPiaText(), 
                    "lblPiaText", 
                    "form:piaText:idText", 
                    ModelIntentionTabs.tabModelIntTypeAndNumberOfPatients);
        }
        if (_modelIntention.getHospitalType() >= 2) {
            checkField(_modelIntention.getHospitalText(), 
                    "lblHospital", 
                    "form:hospitalText:idText", 
                    ModelIntentionTabs.tabModelIntTypeAndNumberOfPatients);
        }
        checkAgreedPatients();
        // tab goals
        checkField(_modelIntention.getPrimaryGoals(), 
                "lblModelIntentionHigherGoals", 
                "form:txtHigherGoals", 
                ModelIntentionTabs.tabModelIntGoals);
        // tab cost
        if (_modelIntention.getInpatientTreatmentType() >= 2) {
            checkField(_modelIntention.getInpatientTreatmentText(), 
                    "lblInpatientTreatment", 
                    "form:inpatientTreatmentText:idText", 
                    ModelIntentionTabs.tabModelIntTreatmentAreasAndCosts);
        }
        if (_modelIntention.getDayPatientTreatmentType() >= 2) {
            checkField(_modelIntention.getDayPatientTreatmentText(), 
                    "lblDayPatientTreatment", 
                    "form:dayPatientTreatmentText:idText", 
                    ModelIntentionTabs.tabModelIntTreatmentAreasAndCosts);
        }
        if (_modelIntention.getInpatientCompensationTreatmentType() >= 2) {
            checkField(_modelIntention.getInpatientCompensationTreatmentText(), 
                    "lblInpatientCompensationTreatment", 
                    "form:inpatientCompensationTreatmentText:idText", 
                    ModelIntentionTabs.tabModelIntTreatmentAreasAndCosts);
        }
        if (_modelIntention.getInpatientCompensationHomeTreatmentType() >= 2) {
            checkField(_modelIntention.getInpatientCompensationHomeTreatmentText(), 
                    "lblInpatientCompensationHomeTreatment", 
                    "form:inpatientCompensationHomeTreatmentText:idText", 
                    ModelIntentionTabs.tabModelIntTreatmentAreasAndCosts);
        }
        if (_modelIntention.getOutpatientTreatmentType() >= 2) {
            checkField(_modelIntention.getOutpatientTreatmentText(), 
                    "lblOutpatientTreatment", 
                    "form:outpatientTreatmentText:idText", 
                    ModelIntentionTabs.tabModelIntTreatmentAreasAndCosts);
        }
        if (_modelIntention.getOutpatientHomeTreatmentType() >= 2) {
            checkField(_modelIntention.getOutpatientHomeTreatmentText(), 
                    "lblOutpatientHomeTreatment", 
                    "form:outpatientHomeTreatmentText:idText", 
                    ModelIntentionTabs.tabModelIntTreatmentAreasAndCosts);
        }
        checkRemunerations();
        checkCosts();
        checkAdjustments();
        // tab structure
        checkModelLifes();
        checkContacs();
        // tab quality
        if (_modelIntention.getInternalQuality() >= 2) {
            checkQuality(getInternalQualityTable().getList(), "headerModelIntentionInternQuality", "formQualityIntern:qualitySelector");
        }
        if (_modelIntention.getExternalQuality() >= 2) {
            checkQuality(getExternalQualityTable().getList(), "headerModelIntentionExternQuality", "formQualityExtern:qualitySelector");
        }
        checkAcademicSupervisions();
    }

    private void checkAgreedPatients() {
        boolean hasMissingField = _modelIntention.getAgreedPatients().stream()
                .anyMatch(a -> a.getPatientsCount() == null
                        || a.getPatientsFrom() == null
                        || a.getPatientsTo() == null
                        || a.getPatientsCount() <= 0);

        if (hasMissingField) {
            _msg += "\\r\\n" + Utils.getMessage("lblAgreedPatiens");
            setTopicAndElement(ModelIntentionTabs.tabModelIntTypeAndNumberOfPatients.name(), ":agreedPatients:addButton");
        }
    }

    private void checkRemunerations() {
        boolean isEmpty = _modelIntention.getRemunerations().isEmpty();
        boolean hasMissingField = _modelIntention.getRemunerations().stream()
                .anyMatch(r -> r.getCode().isEmpty() || r.getText().isEmpty() || r.getAmount().compareTo(BigDecimal.ZERO) <= 0);

        if (isEmpty || hasMissingField) {
            _msg += "\\r\\n" + Utils.getMessage("headerRemuneration");
            setTopicAndElement(ModelIntentionTabs.tabModelIntTreatmentAreasAndCosts.name(), ":remuneration:addButton");
        }
    }

    private void checkCosts() {
        Set<String> remunerationCodes = _modelIntention.getRemunerations().stream().map(Remuneration::getCode).collect(Collectors.toSet());
        boolean isEmpty = _modelIntention.getCosts().isEmpty();
        boolean hasMissingField = _modelIntention.getCosts().stream()
                .anyMatch(c -> c.getIk() == null
                        || c.getRemunerationCode().isEmpty()
                        || !remunerationCodes.contains(c.getRemunerationCode())
                        || c.getCostCenterId() < 0
                        || c.getCostTypeId() < 0
                        || c.getAmount().compareTo(BigDecimal.ZERO) <= 0);
        if (isEmpty || hasMissingField) {
            _msg += "\\r\\n" + Utils.getMessage("headerModelIntentionCost");
            setTopicAndElement(ModelIntentionTabs.tabModelIntTreatmentAreasAndCosts.name(), ":costForm:addButton");
        }
    }

    private void checkAdjustments() {
        boolean hasMissingField = _modelIntention.getAdjustments().stream()
                .anyMatch(a -> a.getAdjustmentTypeId() < 0
                        || a.getDateFrom() == null
                        || a.getDateTo() == null
                        || a.getDescription().isEmpty()
                        || a.getAmount().compareTo(BigDecimal.ZERO) <= 0);
        if (hasMissingField) {
            _msg += "\\r\\n" + Utils.getMessage("headerModelIntentionAdjustment");
            setTopicAndElement(ModelIntentionTabs.tabModelIntTreatmentAreasAndCosts.name(), ":adjustmentForm:addButton");
        }
    }

    private void checkModelLifes() {
        boolean hasMissingField = _modelIntention.getModelLifes().stream()
                .anyMatch(m -> m.getMonthDuration() <= 0
                        || m.getStartDate() == null);
        if (hasMissingField) {
            _msg += "\\r\\n" + Utils.getMessage("headerModelIntentionLifetime");
            setTopicAndElement(ModelIntentionTabs.tabModelIntStructures.name(), ":lifeTime:addButton");
        }
    }

    private void checkContacs() {
        boolean isTooLess = _modelIntention.getContacts().size() < 2;
        boolean hasMissingField = _modelIntention.getContacts().stream()
                .anyMatch(c -> c.getName().isEmpty()
                        || c.getStreet().isEmpty()
                        || c.getZip().isEmpty()
                        || c.getTown().isEmpty()
                        || c.getContactPerson().isEmpty()
                        || c.getPhone().isEmpty()
                        || c.getEmail().isEmpty());
        if (isTooLess || hasMissingField) {
            _msg += "\\r\\n" + Utils.getMessage("headerModelIntentionContract");
            setTopicAndElement(ModelIntentionTabs.tabModelIntStructures.name(), ":contractors:addButton");
        }
    }

    private void checkQuality(List<Quality> qualities, String msgKey, String elementId) {

        boolean hasMissingField = qualities.stream()
                .anyMatch(q -> q.getIndicator().isEmpty() || q.getDescription().isEmpty());
        if (qualities.isEmpty() || hasMissingField) {
            _msg += "\\r\\n" + Utils.getMessage(msgKey);
            setTopicAndElement(ModelIntentionTabs.tabModelIntQualityAndSupervision.name(), elementId);
        }
    }

    private void checkAcademicSupervisions() {
        boolean hasMissingField = _modelIntention.getAcademicSupervisions().stream()
                .anyMatch(a -> a.getContractor().isEmpty()
                        || a.getRemitter().isEmpty()
                        || a.getAcademicSupFrom() == null
                        || a.getAcademicSupTo() == null);
        if (hasMissingField) {
            _msg += "\\r\\n" + Utils.getMessage("headerModelIntentionScientific");
            setTopicAndElement(ModelIntentionTabs.tabModelIntQualityAndSupervision.name(), ":scientific:addButton");
        }
    }

    private void setTopicAndElement(String newTopic, String elementId) {
        if (_nextTopic.isEmpty()) {
            _nextTopic = newTopic;
            _elementId = elementId;
        }
    }

    private void checkField(String value, String msgKey, String elementId, ModelIntentionTabs tab) {
        if (Utils.isNullOrEmpty(value)) {
            _msg += "\\r\\n" + Utils.getMessage(msgKey);
            setTopicAndElement(tab.name(), elementId);
        }
    }

    private void checkField(Integer value, Integer minValue, Integer maxValue, String msgKey, String elementId, ModelIntentionTabs tab) {
        if (value == null
                || minValue != null && value < minValue
                || maxValue != null && value > maxValue) {
            _msg += "\\r\\n" + Utils.getMessage(msgKey);
            setTopicAndElement(tab.name(), elementId);
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="tab patients">
    private AgreedPatientsDynamicTable _agreedPatiensTable;

    public DynamicTable getAgreedPatientsTable() {
        if (_agreedPatiensTable == null) {
            _agreedPatiensTable = new AgreedPatientsDynamicTable(getModelIntention());
        }
        return _agreedPatiensTable;
    }

    public void addNewAgreedPatients() {
        _agreedPatiensTable.addNewEntry();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="tab costs">
    private RemunerationDynamicTable _remunarationTable;

    public DynamicTable getRemunerationTable() {
        if (_remunarationTable == null) {
            _remunarationTable = new RemunerationDynamicTable(getModelIntention());
        }
        return _remunarationTable;
    }

    @Inject private RemunerationTypeFacade _remunerationTypeFacade;

    public void loadRemunerationListener(Remuneration remuneration) {
        String code = remuneration.getCode();
        if (code.length() != 8 || remuneration.getText().length() > 0) {
            FacesContext.getCurrentInstance().responseComplete();
            return;
        }
        Optional<RemunerationType> optType = _remunerationTypeFacade.findByCode(code);
        if (optType.isPresent()) {
            remuneration.setText(optType.get().getText());
            return;
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void addNewRemuneration() {
        _remunarationTable.addNewEntry();
    }

    private CostDynamicTable _costTable;

    public DynamicTable getCostTable() {
        if (_costTable == null) {
            _costTable = new CostDynamicTable(getModelIntention());
        }
        return _costTable;
    }

    public boolean tryAddCost(Cost cost) {
        boolean remunerationFound = false;
        for (Remuneration remuneration : _modelIntention.getRemunerations()) {
            if (cost.getRemunerationCode().equals(remuneration.getCode())) {
                remunerationFound = true;
            }
        }
        if (!remunerationFound) {
            return false;
        }
        for (Object existing : _costTable.getList()) {
            if (existing instanceof Cost) {
                if (((Cost) existing).equalsFunctional(cost)) {
                    return false;
                }
            }
        }
        _costTable.addEntry(cost);
        return true;
    }

    public void addNewCost() {
        _costTable.addNewEntry();
    }

    private AdjustmentDynamicTable _adjustmentTable;

    public DynamicTable getAdjustmentTable() {
        if (_adjustmentTable == null) {
            _adjustmentTable = new AdjustmentDynamicTable(getModelIntention());
        }
        return _adjustmentTable;
    }

    public void addNewAdjustment() {
        _adjustmentTable.addNewEntry();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="tab structure">
    public void addNewContact(int id) {
        ModelIntentionContact contact = new ModelIntentionContact();
        contact.setContactTypeId(id);
        addContact(contact);
    }

    private void addContact(ModelIntentionContact contact) {
        contact.setModelIntentionId(_modelIntention.getId());
        getModelIntention().getContacts().add(contact);
    }

    public boolean tryAddContact(ModelIntentionContact contact) {
        for (ModelIntentionContact existing : _modelIntention.getContacts()) {
            if (contact.equalsFunctional(existing)) {
                return false;
            }
        }
        addContact(contact);
        return true;
    }

    public void deleteContact(ModelIntentionContact contact) {
        _modelIntention.getContacts().remove(contact);
    }

    private String _contactScript = "";

    public void setContactMessage(String msg) {
        _contactScript = "alert('" + msg + "');";
    }

    public String getContactScript() {
        String script = _contactScript;
        _contactScript = "";
        return script;
    }

    private ModelLifeDynamicTable _modelLifeTable;

    public DynamicTable getModelLifeTable() {
        if (_modelLifeTable == null) {
            _modelLifeTable = new ModelLifeDynamicTable(getModelIntention());
        }
        return _modelLifeTable;
    }

    public void addNewModelLife() {
        _modelLifeTable.addNewEntry();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="tab quality">
    private QualityDynamicTable _internalQualityTable;

    public DynamicTable<Quality> getInternalQualityTable() {
        final int typeId = 1;
        if (_internalQualityTable == null) {
            List<Quality> list = getQualities(typeId);
            _internalQualityTable = new QualityDynamicTable(getModelIntention(), list, typeId);
        }
        return _internalQualityTable;
    }

    public void addNewQuality(boolean isIntern) {
        if (isIntern) {
            _internalQualityTable.addNewEntry();
        } else {
            _externalQualityTable.addNewEntry();
        }
    }

    private QualityDynamicTable _externalQualityTable;

    public DynamicTable<Quality> getExternalQualityTable() {
        final int typeId = 2;
        if (_externalQualityTable == null) {
            List<Quality> list = getQualities(typeId);
            _externalQualityTable = new QualityDynamicTable(getModelIntention(), list, typeId);
        }
        return _externalQualityTable;
    }

    private List<Quality> getQualities(int typeId) {
        List<Quality> list = new ArrayList<>();
        for (Quality quality : _modelIntention.getQualities()) {
            if (quality.getTypeId() == typeId) {
                list.add(quality);
            }
        }
        return list;
    }

    private AcademicSupervisionDynamicTable _academicSupervisionTable;

    public DynamicTable getSupervisionTable() {
        if (_academicSupervisionTable == null) {
            _academicSupervisionTable = new AcademicSupervisionDynamicTable(getModelIntention());
        }
        return _academicSupervisionTable;
    }

    public void addNewAcademicSupervision() {
        _academicSupervisionTable.addNewEntry();
    }
    // </editor-fold>

    private void resetDynamicTables() {
        _agreedPatiensTable = null;
        _modelLifeTable = null;
        _remunarationTable = null;
        _costTable = null;
        _adjustmentTable = null;
        _internalQualityTable = null;
        _externalQualityTable = null;
        _academicSupervisionTable = null;
    }

    private void removeEmptyEntries() {
        getAgreedPatientsTable().removeEmptyEntries();
        getModelLifeTable().removeEmptyEntries();
        getRemunerationTable().removeEmptyEntries();
        getCostTable().removeEmptyEntries();
        getAdjustmentTable().removeEmptyEntries();
        getInternalQualityTable().removeEmptyEntries();
        getExternalQualityTable().removeEmptyEntries();
        getSupervisionTable().removeEmptyEntries();
        removeEmptyContacts();
    }

    private void removeEmptyContacts() {
        for (Iterator<ModelIntentionContact> itr = _modelIntention.getContacts().iterator(); itr.hasNext();) {
            ModelIntentionContact entry = itr.next();
            if (entry.isEmpty()) {
                itr.remove();
            }
        }
    }

    private void ensureEmptyEntries() {
        if (isReadOnly()) {
            return;
        }
        boolean weDontNeedEmptyEntriesUntilEventHandlingIsFine = true;
        if (weDontNeedEmptyEntriesUntilEventHandlingIsFine) {
            return;
        }
        getAgreedPatientsTable().ensureEmptyEntry();
        getModelLifeTable().ensureEmptyEntry();
        getRemunerationTable().ensureEmptyEntry();
        getCostTable().ensureEmptyEntry();
        getAdjustmentTable().ensureEmptyEntry();
        getInternalQualityTable().ensureEmptyEntry();
        getExternalQualityTable().ensureEmptyEntry();
        getSupervisionTable().ensureEmptyEntry();
    }
}
