package org.inek.dataportal.feature.modelintention;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.common.RemunerationType;
import org.inek.dataportal.entities.modelintention.Cost;
import org.inek.dataportal.entities.modelintention.ModelIntention;
import org.inek.dataportal.entities.modelintention.ModelIntentionContact;
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
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author vohldo/schlappajo
 */
@Named
@ConversationScoped
public class EditModelIntention extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("EditModelIntention");

    @Inject private SessionController _sessionController;
    @Inject private ModelIntentionFacade _modelIntentionFacade;
    @Inject private Conversation _conversation;
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
        //System.out.println("EditModelIntention");
        _regionMiscEnabled = false;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public ModelIntention getModelIntention() {
        return _modelIntention;
    }

    public String getUserMaintenancePage() {
        return Pages.UserMaintenance.URL();
    }

    // </editor-fold>
    @PostConstruct
    private void init() {
        //_logger.log(Level.WARNING, "Init EditModelIntation");
        _sessionController.beginConversation(_conversation);
        Object miId = Utils.getFlash().get("modelId");
        if (miId == null) {
            if (_sessionController.isInekUser(Feature.MODEL_INTENTION)) {
                _modelIntention = newModelIntention();
            } else {
                Utils.navigate(Pages.MainApp.URL());
                return;
            }
        } else {
            _modelIntention = loadModelIntention(miId);
        }
        if (_modelIntention.getRegion() != null && _modelIntention.getRegion().equals(Region.Misc.region())) {
            _regionMiscEnabled = true;
        }
        ensureEmptyEntries();
    }

    @PreDestroy
    private void destroy() {
        //_logger.log(Level.WARNING, "Destroy EditModelIntation");
    }

    private ModelIntention loadModelIntention(Object ppId) {
        try {
            int id = Integer.parseInt("" + ppId);
            ModelIntention modelIntention = _modelIntentionFacade.find(id);
            return modelIntention;
        } catch (NumberFormatException ex) {
            _logger.info(ex.getMessage());
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

    public String save() {
        if (!check4validSession()) {
            return Pages.InvalidConversation.URL();
        }
        removeEmptyEntries();
        _modelIntention.setQualities(_internalQualityTable.getList());
        _modelIntention.getQualities().addAll(_externalQualityTable.getList());
        removeObsolteTexts();
        _modelIntention = _modelIntentionFacade.saveModelIntention(_modelIntention);
        resetDynamicTables();
        ensureEmptyEntries();
        if (isValidId(_modelIntention.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
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
        return _modelIntention.getAccountId() != _sessionController.getAccountId();
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
        if (!check4validSession() /*TODO: || !requestIsComplete()*/) {
            return Pages.InvalidConversation.URL();
        }
        if (!requestIsComplete()) {
            return getActiveTopic().getOutcome();
        }
        if (_modelIntention.getStatus().getValue() >= 10) {
            return Pages.Error.URL();
        }

        _modelIntention.setStatus(10 + _modelIntention.getStatus().getValue());
        removeEmptyEntries();
        _modelIntention = _modelIntentionFacade.saveModelIntention(_modelIntention);
        if (isValidId(_modelIntention.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameMODEL_INTENTION"));
            Utils.getFlash().put("targetPage", Pages.ModelIntentionSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_modelIntention));
            _sessionController.endConversation(_conversation);
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
        if (!check4validSession() /*TODO: || !requestIsComplete()*/) {
            return Pages.Error.URL();
        }
        if (!requestIsComplete()) {
            return getActiveTopic().getOutcome();
        }
        if (_modelIntention.getStatus().getValue() >= 10) {
            return Pages.Error.URL();
        }

        _modelIntention.setStatus(WorkflowStatus.ApprovalRequested.getValue());
        _modelIntention = _modelIntentionFacade.saveModelIntention(_modelIntention);
        return "";
    }

    /**
     * checks, whether the session is still valid
     *
     * @return
     */
    private boolean check4validSession() {
        return !_conversation.isTransient();
    }

     // <editor-fold defaultstate="collapsed" desc="CheckElements">
    String _msg = "";
    String _elementId = "";

    private boolean requestIsComplete() {
        _msg = "";
        String newTopic = "";
        newTopic = checkField(newTopic, _modelIntention.getDescription(), "lblAppellation", "form:Appelation:idText", ModelIntentionTabs.tabModelIntTypeAndNumberOfPatients);
        if (_modelIntention.getRegionType() == 2) {
            newTopic = checkField(newTopic, _modelIntention.getRegion(), "lblRegionalFeatures", "form:region:idText", ModelIntentionTabs.tabModelIntTypeAndNumberOfPatients);
        }
        if (_modelIntention.getMedicalAttributesType() >= 0) {
            newTopic = checkField(newTopic, _modelIntention.getMedicalSpecification(), "lblMedicalFeature", "form:medicalSpecification:idText", ModelIntentionTabs.tabModelIntTypeAndNumberOfPatients);
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

    private String checkField(String newTopic, String value, String msgKey, String elementId, ModelIntentionTabs tab) {
        if (Utils.isNullOrEmpty(value)) {
            _msg += "\\r\\n" + Utils.getMessage(msgKey);
            if (newTopic.isEmpty()) {
                newTopic = tab.name();
                _elementId = elementId;
            }
        }
        return newTopic;
    }

    private String checkField(String newTopic, Integer value, Integer minValue, Integer maxValue, String msgKey, String elementId, ModelIntentionTabs tab) {
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
        RemunerationType type = _remunerationTypeFacade.find(code);
        if (type == null) {
            FacesContext.getCurrentInstance().responseComplete();
            return;
        }
        remuneration.setText(type.getText());
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
        for (Object existing : _costTable.getList()) {
            if (existing instanceof Cost) {
                if (((Cost) existing).equalsFunctional(cost)) {
                    return false;
                }
            }
        }
        //_costTable.removeEmptyEntries();
        _costTable.addEntry(cost);
        //_costTable.ensureEmptyEntry();
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

    String _contactScript = "";

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

    public DynamicTable getInternalQualityTable() {
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

    public DynamicTable getExternalQualityTable() {
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

    // <editor-fold defaultstate="collapsed" desc="CheckElements">
    // </editor-fold>
}
