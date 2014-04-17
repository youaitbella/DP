package org.inek.dataportal.feature.modelintention;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.modelintention.AcademicSupervision;
import org.inek.dataportal.entities.modelintention.ModelIntention;
import org.inek.dataportal.entities.modelintention.ModelIntentionContact;
import org.inek.dataportal.entities.modelintention.ModelIntentionQuality;
import org.inek.dataportal.entities.modelintention.ModelLife;
import org.inek.dataportal.entities.modelintention.RemunerationCode;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Genders;
import org.inek.dataportal.enums.HospitalType;
import org.inek.dataportal.enums.MedicalAttribute;
import org.inek.dataportal.enums.ModelIntentionStatus;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.PiaType;
import org.inek.dataportal.enums.Region;
import org.inek.dataportal.enums.SelfHospitalisationType;
import org.inek.dataportal.enums.SettleType;
import org.inek.dataportal.enums.TreatmentType;
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
    private ModelIntentionQuality _modelIntentionQuality;
    private AcademicSupervision _modelIntentionAcademicSupervision;

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

    public SelectItem[] getTreatmentTypes() {
        List<SelectItem> l = new ArrayList<>();
        TreatmentType[] types = TreatmentType.values();
        for (TreatmentType tt : types) {
            l.add(new SelectItem(tt.id(), tt.type()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }

    public boolean isTreatmentTextEnabled() {
        if (_modelIntention.getStationaryType() == TreatmentType.Generally.id() || _modelIntention.getStationaryType() == TreatmentType.No.id()) {
            return false;
        }
        return true;
    }

    public boolean isPartialHospitalisationTextEnabled() {
        if (_modelIntention.getPartialHospitalisationType() == TreatmentType.Generally.id() || _modelIntention.getStationaryType() == TreatmentType.No.id()) {
            return false;
        }
        return true;
    }

    public boolean isHospitalAbulantTextEnabled() {
        if (_modelIntention.getHospitalAmbulantTreatmentType() == TreatmentType.Generally.id() || _modelIntention.getStationaryType() == TreatmentType.No.id()) {
            return false;
        }
        return true;
    }

    public boolean isHomeVisitPIATextEnabled() {
        if (_modelIntention.getVisitPiaType() == TreatmentType.Generally.id() || _modelIntention.getStationaryType() == TreatmentType.No.id()) {
            return false;
        }
        return true;
    }

    public boolean isAmbulantTreatmentTextEnabled() {
        if (_modelIntention.getAmbulantTreatmentType() == TreatmentType.Generally.id() || _modelIntention.getStationaryType() == TreatmentType.No.id()) {
            return false;
        }
        return true;
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

    public ModelIntentionQuality getModelIntentionQuality() {
        return _modelIntentionQuality;
    }

    public AcademicSupervision getModelIntentionAcademicSupervision() {
        return _modelIntentionAcademicSupervision;
    }

    // </editor-fold>
    @PostConstruct
    private void init() {
        //_logger.log(Level.WARNING, "Init EditModelIntation");
        _sessionController.beginConversation(_conversation);
        Object miId = Utils.getFlash().get("modelId");
        if (miId == null) {
            _modelIntention = newModelIntention();
            _modelIntentionQuality = newModelIntentionQuality();
            _modelIntentionAcademicSupervision = newModelIntentionAcademicSupervision();
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
        modelIntention.setRegion(Region.Germany.region());
        modelIntention.setSettleMedicType(SettleType.ImpartialDepartment.id());
        modelIntention.setPiaType(PiaType.AnyPIA.id());
        modelIntention.setHospitalType(HospitalType.AnyHospital.id());
        modelIntention.setStationaryType(TreatmentType.No.id());
        modelIntention.setPartialHospitalisationType(TreatmentType.No.id());
        modelIntention.setHospitalAmbulantTreatmentType(TreatmentType.No.id());
        modelIntention.setVisitPiaType(TreatmentType.No.id());
        modelIntention.setAmbulantTreatmentType(TreatmentType.No.id());
        return modelIntention;
    }

    private ModelIntentionQuality newModelIntentionQuality() {
        ModelIntentionQuality modelIntentionQuality = new ModelIntentionQuality();
        modelIntentionQuality.setId(null);
        return modelIntentionQuality;
    }

    private AcademicSupervision newModelIntentionAcademicSupervision() {
        AcademicSupervision modelIntentionAcademicSupervision = new AcademicSupervision();
        modelIntentionAcademicSupervision.setId(null);
        return modelIntentionAcademicSupervision;
    }

    public String getAcademicSupTo() {
        if (_modelIntentionAcademicSupervision.getAcademicSupTo() == null) {
            return "";
        }
        return _modelIntentionAcademicSupervision.getAcademicSupTo().toString();
    }

    public void setAcademicSupTo(String date) {
        try {
            _modelIntentionAcademicSupervision.setAcademicSupTo(SimpleDateFormat.getDateInstance().parse(date));
        } catch (Exception ex) {
            _modelIntentionAcademicSupervision.setAcademicSupTo(null);
        }
    }

    public String getAcademicSupFrom() {
        if (_modelIntentionAcademicSupervision.getAcademicSupFrom() == null) {
            return "";
        }
        return _modelIntentionAcademicSupervision.getAcademicSupFrom().toString();
    }

    public void setAcademicSupFrom(String date) {
        try {
            _modelIntentionAcademicSupervision.setAcademicSupFrom(SimpleDateFormat.getDateInstance().parse(date));
        } catch (Exception ex) {
            _modelIntentionAcademicSupervision.setAcademicSupFrom(null);
        }
    }

    private ModelIntentionController getModelIntentionController() {
        return (ModelIntentionController) _sessionController.getFeatureController(Feature.MODEL_INTENTION);
    }

    public String save() {
        if (!check4validSession()) {
            return Pages.InvalidConversation.URL();
        }
        removeEmptyEntries();
        _modelIntention.setStatus(1);
        
        _modelIntention = _modelIntentionFacade.saveModelIntention(_modelIntention);
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

    // <editor-fold defaultstate="collapsed" desc="tab costs">
    private boolean ensureEmptyRemunerationCode() {
        List<RemunerationCode> remunerationCodes = getModelIntention().getRemunerationCodes();
        if (needEmptyCode(remunerationCodes)) {
            RemunerationCode remunerationCode = new RemunerationCode();
            if (getModelIntention().getId() != null){
                remunerationCode.setModelIntentionId(getModelIntention().getId());
            }
            remunerationCodes.add(remunerationCode);
            return true;
        }
        return false;
    }
    
    private boolean needEmptyCode(List<RemunerationCode> remunerationCodes) {
        if (remunerationCodes.isEmpty()){return true;}
        RemunerationCode remunerationCode = remunerationCodes.get(remunerationCodes.size() - 1);
        return remunerationCode.getCode().length() > 0 || remunerationCode.getText().length() > 0 ;
    }

    String _remunerationScript = "";

    public void checkRemunerationListener(AjaxBehaviorEvent event) {
        HtmlInputText t = (HtmlInputText) event.getSource();
        String currentId = t.getClientId();
        if (ensureEmptyRemunerationCode()) {
            _remunerationScript = "setCaretPosition('" + currentId + "', -1);";
        } else {
            _remunerationScript = "";
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public String getRemunerationScript() {
        String script = _remunerationScript;
        _remunerationScript = "";
        return script;
    }

    private void removeEmptyRemunerationCode() {
        List<RemunerationCode> remunerations = getModelIntention().getRemunerationCodes();
        for (Iterator<RemunerationCode> itr = remunerations.iterator(); itr.hasNext();) {
            RemunerationCode remuneration = itr.next();
            if (remuneration.getCode()== null || remuneration.getText()== null) {
                itr.remove();
            }
        }
    }

    public String deleteRemuneration(RemunerationCode remuneration){
        getModelIntention().getRemunerationCodes().remove(remuneration);
        ensureEmptyRemunerationCode();
        return "";
    }
       
// </editor-fold>    
    
    // <editor-fold defaultstate="collapsed" desc="tab structure">
    public void addContact(int id) {
        ModelIntentionContact contact = new ModelIntentionContact();
        contact.setContactTypeId(id);
        getModelIntention().getContacts().add(contact);
    }

    private boolean ensureEmptyModelLife() {
        List<ModelLife> lifes = getModelIntention().getModelLifes();
        if (lifes.isEmpty() 
                || lifes.get(lifes.size() - 1).getStartDate() != null 
                || lifes.get(lifes.size() - 1).getMonthDuration()!= null) {
            ModelLife life = new ModelLife();
            if (getModelIntention().getId() != null){
                life.setModelIntentionId(getModelIntention().getId());
            }
            lifes.add(life);
            return true;
        }
        return false;
    }

    String _modelLifeScript = "";

    public void checkModelLifeListener(AjaxBehaviorEvent event) {
        HtmlInputText t = (HtmlInputText) event.getSource();
        String currentId = t.getClientId();
        if (ensureEmptyModelLife()) {
            _modelLifeScript = "setCaretPosition('" + currentId + "', -1);";
        } else {
            _modelLifeScript = "";
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public String getModelLifeScript() {
        String script = _modelLifeScript;
        _modelLifeScript = "";
        return script;
    }

    private void removeEmptyModelLife() {
        List<ModelLife> modelLifes = getModelIntention().getModelLifes();
        for (Iterator<ModelLife> itr = modelLifes.iterator(); itr.hasNext();) {
            ModelLife life = itr.next();
            if (life.getStartDate() == null || life.getMonthDuration() == null) {
                itr.remove();
            }
        }
    }

    public String deleteModelLife(ModelLife life){
        getModelIntention().getModelLifes().remove(life);
        ensureEmptyModelLife();
        return "";
    }
       
    
    // </editor-fold>    
    private void removeEmptyEntries() {
        removeEmptyModelLife();
        removeEmptyRemunerationCode();
        // todo: remove other empty entries
    }

    private void ensureEmptyEntries() {
        ensureEmptyModelLife();
        ensureEmptyRemunerationCode();
        // todo: ensure other empty entries
    }

    // <editor-fold defaultstate="collapsed" desc="CheckElements">
    // </editor-fold>
}
