package org.inek.dataportal.feature.modelintention;

import java.util.ArrayList;
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
import org.inek.dataportal.enums.Genders;
import org.inek.dataportal.enums.HospitalType;
import org.inek.dataportal.enums.InsuranceAffiliation;
import org.inek.dataportal.enums.MedicalAttribute;
import org.inek.dataportal.enums.ModelIntentionStatus;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.PiaType;
import org.inek.dataportal.enums.Region;
import org.inek.dataportal.enums.SelfHospitalisationType;
import org.inek.dataportal.enums.SettleType;
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

    public SelectItem[] getGenders() {
        List<SelectItem> l = new ArrayList<>();
        Genders[] genders = Genders.values();
        for (Genders g : genders) {
            l.add(new SelectItem(g.id(), g.gender()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }

    public SelectItem[] getRegions() {
        List<SelectItem> l = new ArrayList<>();
        Region[] regions = Region.values();
        for (Region r : regions) {
            l.add(new SelectItem(r.id(), r.region()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }

    public SelectItem[] getMedicalAttributes() {
        List<SelectItem> l = new ArrayList<>();
        MedicalAttribute[] attrs = MedicalAttribute.values();
        for (MedicalAttribute ma : attrs) {
            l.add(new SelectItem(ma.id(), ma.attribute()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }

    public SelectItem[] getSettledTypes() {
        List<SelectItem> l = new ArrayList<>();
        SettleType[] types = SettleType.values();
        for (SettleType t : types) {
            l.add(new SelectItem(t.id(), t.type()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }

    public List<SelectItem> getInsuranceAffiliation() {
        List<SelectItem> l = new ArrayList<>();
        for (InsuranceAffiliation t : InsuranceAffiliation.values()) {
            l.add(new SelectItem(t.id(), t.type()));
        }
        return l;
    }

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

    public String getSettleText() {
        if (_modelIntention.getSettleMedicType() == SettleType.ImpartialDepartment.id()) {
            return "";
        }
        return _modelIntention.getSettleMedicText();
    }

    public void setSettleText(String text) {
        if (_modelIntention.getSettleMedicType() == SettleType.ImpartialDepartment.id()) {
            _modelIntention.setSettleMedicText("");
        } else {
            _modelIntention.setSettleMedicText(text);
        }
    }

    public Integer getRegion() {
        int index = 0;
        boolean listItem = false;
        Region[] regions = Region.values();
        for (Region r : regions) {
            if (r.region().equals(_modelIntention.getRegion())) {
                index = r.id();
                listItem = true;
            }
        }
        if (!listItem) {
            index = Region.Misc.id();
        }
        return index;
    }

    public void setRegion(Integer index) {
        Region[] regions = Region.values();
        for (Region r : regions) {
            if (index == r.id()) {
                _regionMiscEnabled = r.region().equals(Region.Misc.region());
                if (_regionMiscEnabled) {
                    _modelIntention.setRegion(null);
                } else {
                    _modelIntention.setRegion(r.region());
                }
            }
        }
    }

    public boolean isSettleTextEnabled() {
        if (_modelIntention.getSettleMedicType() == SettleType.ImpartialDepartment.id()) {
            return false;
        }
        return true;
    }

    public SelectItem[] getPiaTypes() {
        List<SelectItem> l = new ArrayList<>();
        PiaType[] types = PiaType.values();
        for (PiaType p : types) {
            l.add(new SelectItem(p.id(), p.type()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }

    public String getPIAText() {
        if (_modelIntention.getPiaType() != PiaType.SpecificPIA.id()) {
            return "";
        }
        return _modelIntention.getPiaText();
    }

    public void setPIAText(String text) {
        if (_modelIntention.getPiaType() != PiaType.SpecificPIA.id()) {
            _modelIntention.setPiaText("");
        } else {
            _modelIntention.setPiaText(text);
        }
    }

    public boolean isPIATextEnabled() {
        if (_modelIntention.getPiaType() != PiaType.SpecificPIA.id()) {
            return false;
        }
        return true;
    }

    public SelectItem[] getHospitalTypes() {
        List<SelectItem> l = new ArrayList<>();
        HospitalType[] types = HospitalType.values();
        for (HospitalType h : types) {
            l.add(new SelectItem(h.id(), h.type()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }

    public String getHospitalText() {
        if (_modelIntention.getHospitalType() != HospitalType.SpecificHospital.id() && _modelIntention.getHospitalType() != HospitalType.OtherHospital.id()) {
            return "";
        }
        return _modelIntention.getHospitalText();
    }

    public void setHospitalText(String text) {
        if (_modelIntention.getHospitalType() != HospitalType.SpecificHospital.id() && _modelIntention.getHospitalType() != HospitalType.OtherHospital.id()) {
            _modelIntention.setHospitalText("");
        } else {
            _modelIntention.setHospitalText(text);
        }
    }

    public boolean isHospitalTextEnabled() {
        if (_modelIntention.getHospitalType() != HospitalType.SpecificHospital.id() && _modelIntention.getHospitalType() != HospitalType.OtherHospital.id()) {
            return false;
        }
        return true;
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
            _modelIntention = newModelIntention();
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
        _modelIntention.setStatus(1);

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

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isReadOnly() {
        return false;
    }

    public boolean isReadOnly(boolean laxCheck) {
        return false;
    }

    public boolean isRejectedModelIntention() {
        return ModelIntentionStatus.Rejected.getValue() == _modelIntention.getStatus();
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
//TODO:        if (!requestIsComplete()) {
//            return getActiveTopic().getOutcome();
//        }
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
        if (_modelIntention.getStatus() >= 10) {
            return Pages.Error.URL();
        }

        _modelIntention.setStatus(10 + _modelIntention.getStatus());
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
//TODO:        if (!requestIsComplete()) {
//            return getActiveTopic().getOutcome();
//        }
        String script = "if (confirm ('" + Utils.getMessage("msgRequestApproval") + "')) {document.getElementById('form:confirmApprovalRequest').click();}";
        _sessionController.setScript(script);
        return null;
    }

    public String confirmApprovalRequest() {
        if (!check4validSession() /*TODO: || !requestIsComplete()*/) {
            return Pages.Error.URL();
        }
        if (_modelIntention.getStatus() >= 10) {
            return Pages.Error.URL();
        }

        _modelIntention.setStatus(ModelIntentionStatus.ApprovalRequested.getValue());
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

    // <editor-fold defaultstate="collapsed" desc="tab patients">
    private AgreedPatientsDynamicTable _agreedPatiensTable;

    public DynamicTable getAgreedPatientsTable() {
        if (_agreedPatiensTable == null) {
            _agreedPatiensTable = new AgreedPatientsDynamicTable(getModelIntention());
        }
        return _agreedPatiensTable;
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

    private CostDynamicTable _costTable;

    public DynamicTable getCostTable() {
        if (_costTable == null) {
            _costTable = new CostDynamicTable(getModelIntention());
        }
        return _costTable;
    }

    public void addCost(Cost cost) {
        _costTable.removeEmptyEntries();
        _costTable.addEntry(cost);
        _costTable.ensureEmptyEntry();
    }
    
    private AdjustmentDynamicTable _adjustmentTable;

    public DynamicTable getAdjustmentTable() {
        if (_adjustmentTable == null) {
            _adjustmentTable = new AdjustmentDynamicTable(getModelIntention());
        }
        return _adjustmentTable;
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="tab structure">
    public void addNewContact(int id) {
        ModelIntentionContact contact = new ModelIntentionContact();
        contact.setContactTypeId(id);
        addContact(contact);
    }

    public void addContact(ModelIntentionContact contact) {
        contact.setModelIntentionId(_modelIntention.getId());
        getModelIntention().getContacts().add(contact);
    }

    private ModelLifeDynamicTable _modelLifeTable;

    public DynamicTable getModelLifeTable() {
        if (_modelLifeTable == null) {
            _modelLifeTable = new ModelLifeDynamicTable(getModelIntention());
        }
        return _modelLifeTable;
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
    }

    private void ensureEmptyEntries() {
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
