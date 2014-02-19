package org.inek.dataportal.feature.modelintention;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.AgreedPatients;
import org.inek.dataportal.entities.ModelIntention;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.ModelIntentionStatus;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.ModelIntentionFacade;
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
    private boolean _ageYearEnabled, _regionMiscEnabled;
    private String _conversationId;
    private ModelIntention _modelIntention;
    private AgreedPatients _agreedPatients;
    
    public boolean isAgeYearsEnabled() {
        return !_ageYearEnabled;
    }
    
    public SelectItem[] getGenders() {
        List<SelectItem> l = new ArrayList<>();
        Genders[] genders = Genders.values();
        for(Genders g : genders) {
            l.add(new SelectItem(g.id(), g.gender()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }
    
    public enum Genders {
        Both(0, "Beide"),
        Male(1, "Männlich"),
        Female(2, "Weiblich");
        
        private int _id;
        private String _gender;
        
        private Genders(int id, String gender) {
            _id = id;
            _gender = gender;
        }
        
        public int id() {
            return _id;
        }
        
        public String gender() {
            return _gender;
        }
    }
    
    public SelectItem[] getRegions() {
        List<SelectItem> l = new ArrayList<>();
        Regions[] regions = Regions.values();
        for(Regions r : regions) {
            l.add(new SelectItem(r.id(), r.region()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }
    
    public enum Regions {
        Germany(0, "Deutschland"),
        State(1, "Bundesland"),
        Misc(2, "Sonstige");
        
        private int _id;
        private String _region;
        
        private Regions(int id, String region) {
            _id = id;
            _region = region;
        }

        public int id() {
            return _id;
        }

        public String region() {
            return _region;
        }
    }
    
    public SelectItem[] getMedicalAttributes() {
        List<SelectItem> l = new ArrayList<>();
        MedicalAttributes[] attrs = MedicalAttributes.values();
        for(MedicalAttributes ma : attrs) {
            l.add(new SelectItem(ma.id(), ma.attribute()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }
    
    public enum MedicalAttributes {
        MainDiagnosis(0, "Hauptdiagnose(n)"),
        PracticeAreas(1, "behandelnde Fachgebiete"),
        Misc(2, "andere Spezifizierung");
        
        private int _id;
        private String _attribute;
        
        private MedicalAttributes(int id, String attribute) {
            _id = id;
            _attribute = attribute;
        }

        public int id() {
            return _id;
        }

        public String attribute() {
            return _attribute;
        }
    }
    
    public SelectItem[] getSettledTypes() {
        List<SelectItem> l = new ArrayList<>();
        SettleTypes[] types = SettleTypes.values();
        for(SettleTypes t : types) {
            l.add(new SelectItem(t.id(), t.type()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }
    
    public enum SettleTypes {
        ImpartialDepartment(0, "fachgebietsunabhängig"),
        DepartmentDocs(1, "nur Fachärzte"),
        MiscMedics(2, "sonstige bestimmte Ärzte");
        
        private int _id;
        private String _type;
        
        private SettleTypes(int id, String type) {
            _id = id;
            _type = type;
        }

        public int id() {
            return _id;
        }

        public String type() {
            return _type;
        }
    }

    @Override
    protected void addTopics() {
        addTopic(ModelIntentionTabs.tabModelIntTypeAndNumberOfPatients.name(), Pages.ModelIntentionTypeAndNumPat.URL());
        addTopic(ModelIntentionTabs.tabModelIntGoals.name(), Pages.ModelIntentionGoals.URL());
        addTopic(ModelIntentionTabs.tabModelIntTreatmentAreasAndCosts.name(), Pages.ModelIntentionTreatAreaAndCosts.URL());
        addTopic(ModelIntentionTabs.tabModelIntStructures.name(), Pages.ModelIntentionStructure.URL());
        addTopic(ModelIntentionTabs.tabModelIntQualityAndSupervision.name(), Pages.ModelIntentionQuality.URL());
    }

    public boolean isAgeYearEnabled() {
        clearAgeYearTextfields();
        return _ageYearEnabled;
    }

    private void clearAgeYearTextfields() {
        if(_modelIntention.getAgeYearsFrom() != null && !_ageYearEnabled) {
            _modelIntention.setAgeYearsFrom(null);
        } else if(_modelIntention.getAgeYearsTo() != null && !_ageYearEnabled) {
            _modelIntention.setAgeYearsTo(null);
        }
    }

    public void setAgeYearEnabled(boolean ageYearEnabled) {
        this._ageYearEnabled = ageYearEnabled;
    }

    public boolean isRegionMiscEnabled() {
        return _regionMiscEnabled;
    }

    public void setRegionMiscEnabled(boolean regionMiscEnabled) {
        this._regionMiscEnabled = regionMiscEnabled;
    }
    
    public String getSettleText() {
        if(_modelIntention.getSettleMedicType() == SettleTypes.ImpartialDepartment.id())
            return "";
        return _modelIntention.getSettleMedicText();
    }
    
    public void setSettleText(String text) {
        if(_modelIntention.getSettleMedicType() == SettleTypes.ImpartialDepartment.id())
            _modelIntention.setSettleMedicText("");
        else
            _modelIntention.setSettleMedicText(text);
    }
    
    public Integer getRegion() {
        int index = 0;
        boolean listItem = false;
        Regions[] regions = Regions.values();
        for(Regions r : regions) {
            if(r.region().equals(_modelIntention.getRegion())) {
                index = r.id();
                listItem = true;
            }
        }
        if(!listItem)
            index = Regions.Misc.id();
        return index;
    }
    
    public void setRegion(Integer index) {
        Regions[] regions = Regions.values();
        for(Regions r : regions) {
            if(index == r.id()) {
                _regionMiscEnabled = r.region().equals(Regions.Misc.region());
                if(_regionMiscEnabled) {
                    _modelIntention.setRegion(null);
                } else {
                    _modelIntention.setRegion(r.region());
                }
            }
        }
    }
    
    public boolean isSettleTextEnabled() {
        if(_modelIntention.getSettleMedicType() == SettleTypes.ImpartialDepartment.id())
            return false;
        return true;
    }
    
    public SelectItem[] getPiaTypes() {
        List<SelectItem> l = new ArrayList<>();
        PiaTypes[] types = PiaTypes.values();
        for(PiaTypes p : types) {
            l.add(new SelectItem(p.id(), p.type()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }
    
    public enum PiaTypes {
        AnyPIA(0, "jede PIA"),
        IntegratedPIA(1, "nur PIA der im Modellvorhaben integrierten Krankenhäuser"),
        ContractPIA(2, "nur PIA, die auch Vertragspartner im Modellvorhaben sind"),
        SpecificPIA(3, "nur bestimmte PIA");
        
        
        private int _id;
        private String _type;
        
        private PiaTypes(int id, String type) {
            _id = id;
            _type = type;
        }

        public int id() {
            return _id;
        }

        public String type() {
            return _type;
        }
    }
    
    public String getPIAText() {
        if(_modelIntention.getPiaType()!= PiaTypes.SpecificPIA.id())
            return "";
        return _modelIntention.getPiaText();
    }
    
    public void setPIAText(String text) {
        if(_modelIntention.getPiaType() != PiaTypes.SpecificPIA.id())
            _modelIntention.setPiaText("");
        else
            _modelIntention.setPiaText(text);
    }
    
    public boolean isPIATextEnabled() {
        if(_modelIntention.getPiaType() != PiaTypes.SpecificPIA.id())
            return false;
        return true;
    }

    public SelectItem[] getHospitalTypes() {
        List<SelectItem> l = new ArrayList<>();
        HospitalTypes[] types = HospitalTypes.values();
        for(HospitalTypes h : types) {
            l.add(new SelectItem(h.id(), h.type()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }
    
    public enum HospitalTypes {
        AnyHospital(0, "jedes Krankenhaus"),
        ModelProjectHospital(1, "nur am Modellprojekt beteiligte Krankenhäuser"),
        SpecificHospital(2, "nur Fachkrankenhäuser"),
        OtherHospital(3, "sonstige bestimmte Krankenhäuser");
        
        
        private int _id;
        private String _type;
        
        private HospitalTypes(int id, String type) {
            _id = id;
            _type = type;
        }

        public int id() {
            return _id;
        }

        public String type() {
            return _type;
        }
    }
    
    public String getHospitalText() {
        if(_modelIntention.getHospitalType()!= HospitalTypes.SpecificHospital.id() && _modelIntention.getHospitalType()!= HospitalTypes.OtherHospital.id())
            return "";
        return _modelIntention.getHospitalText();
    }
    
    public void setHospitalText(String text) {
        if(_modelIntention.getHospitalType()!= HospitalTypes.SpecificHospital.id() && _modelIntention.getHospitalType()!= HospitalTypes.OtherHospital.id())
            _modelIntention.setHospitalText("");
        else
            _modelIntention.setHospitalText(text);
    }
    
    public boolean isHospitalTextEnabled() {
        if(_modelIntention.getHospitalType()!= HospitalTypes.SpecificHospital.id() && _modelIntention.getHospitalType()!= HospitalTypes.OtherHospital.id())
            return false;
        return true;
    }
    
    
        public SelectItem[] getSelfHospitalisationTypes() {
        List<SelectItem> l = new ArrayList<>();
        SelfHospitalisationTypes[] types = SelfHospitalisationTypes.values();
        for(SelfHospitalisationTypes sh : types) {
            l.add(new SelectItem(sh.id(), sh.type()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }
    
    
    public enum SelfHospitalisationTypes {
        Possible(0, "grundsätzlich möglich"),
        EmergencyPossible(1, "nur als Notfall möglich"),
        NotPossible(2, "grundsätzlich nicht möglich");
        
        
        private int _id;
        private String _type;
        
        private SelfHospitalisationTypes(int id, String type) {
            _id = id;
            _type = type;
        }

        public int id() {
            return _id;
        }

        public String type() {
            return _type;
        }
    }
    
    
    public SelectItem[] getTreatmentTypes() {
        List<SelectItem> l = new ArrayList<>();
        TreatmentTypes[] types = TreatmentTypes.values();
        for(TreatmentTypes tt : types) {
            l.add(new SelectItem(tt.id(), tt.type()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }
        
    public enum TreatmentTypes {
        No(0, "nein"),
        Generally(1, "grundsätzlich"),
        SpecialSetting(2, "nur spezielles Setting");
        
        
        private int _id;
        private String _type;
        
        private TreatmentTypes(int id, String type) {
            _id = id;
            _type = type;
        }

        public int id() {
            return _id;
        }

        public String type() {
            return _type;
        }
    }
    
     public boolean isTreatmentTextEnabled() {
        if(_modelIntention.getStationaryType()== TreatmentTypes.Generally.id() || _modelIntention.getStationaryType()== TreatmentTypes.No.id())
            return false;
        return true;
    }
     
      public boolean isPartialHospitalisationTextEnabled() {
        if(_modelIntention.getPartialHospitalisationType()== TreatmentTypes.Generally.id() || _modelIntention.getStationaryType()== TreatmentTypes.No.id())
            return false;
        return true;
    }
    
    public boolean isHospitalAbulantTextEnabled() {
        if(_modelIntention.getHospitalAmbulantTreatmentType()== TreatmentTypes.Generally.id() || _modelIntention.getStationaryType()== TreatmentTypes.No.id())
            return false;
        return true;
    }
      
    public boolean isHomeVisitPIATextEnabled() {
        if(_modelIntention.getVisitPiaType()== TreatmentTypes.Generally.id() || _modelIntention.getStationaryType()== TreatmentTypes.No.id())
            return false;
        return true;
    }
    
          
    public boolean isAmbulantTreatmentTextEnabled() {
        if(_modelIntention.getAmbulantTreatmentType()== TreatmentTypes.Generally.id() || _modelIntention.getStationaryType()== TreatmentTypes.No.id())
            return false;
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
        _ageYearEnabled = false;
        _regionMiscEnabled = false;       
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public ModelIntention getModelIntention() {
        return _modelIntention;
    }

    public AgreedPatients getAgreedPatients() {
        return _agreedPatients;
    }
    
    public String getUserMaintenancePage() {
        return Pages.UserMaintenance.URL();
    }

    // </editor-fold>
    @PostConstruct
    private void init() {
        _conversationId = (String) Utils.getFlash().get("conversationId");
        Object miId = Utils.getFlash().get("miId");
        if (miId == null) {
            _modelIntention = newModelIntention();
            _agreedPatients = newAgreedPatients();
        } else {
            _modelIntention = loadModelIntention(miId);
        }if(_modelIntention.getAgeYearsFrom() != null || _modelIntention.getAgeYearsTo() != null)
            _ageYearEnabled = true;
        if(_modelIntention.getRegion() != null && _modelIntention.getRegion().equals(Regions.Misc.region()))
            _regionMiscEnabled = true;
        //ensureEmptyEntry(_peppProposal.getProcedures());
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
        modelIntention.setRegion(Regions.Germany.region());
        modelIntention.setSettleMedicType(SettleTypes.ImpartialDepartment.id());
        modelIntention.setPiaType(PiaTypes.AnyPIA.id());
        modelIntention.setHospitalType(HospitalTypes.AnyHospital.id());
        modelIntention.setStationaryType(TreatmentTypes.No.id());
        modelIntention.setPartialHospitalisationType(TreatmentTypes.No.id());
        modelIntention.setHospitalAmbulantTreatmentType(TreatmentTypes.No.id());
        modelIntention.setVisitPiaType(TreatmentTypes.No.id());
        modelIntention.setAmbulantTreatmentType(TreatmentTypes.No.id());
        return modelIntention;
    }
    
    private AgreedPatients newAgreedPatients() {
        AgreedPatients agreedPatients = new AgreedPatients();
        agreedPatients.setPatientsTo(null);
        agreedPatients.setPatientsFrom(null);
        agreedPatients.setPatientsCount(null);
        return agreedPatients;
    }
    
    public String getPatientsTo() {
        if(_agreedPatients.getPatientsTo() == null)
            return "";
        return _agreedPatients.getPatientsTo().toString();
    }
    
    public void setPatientsTo(String date) {
        try {
            _agreedPatients.setPatientsTo(SimpleDateFormat.getDateInstance().parse(date));
        } catch(Exception ex) {
            _agreedPatients.setPatientsTo(null);
        }
    }
    
    public String getPatientsFrom() {
        if(_agreedPatients.getPatientsFrom() == null)
            return "";
        return _agreedPatients.getPatientsFrom().toString();
    }
    
    public void setPatientsFrom(String date) {
        try {
            _agreedPatients.setPatientsFrom(SimpleDateFormat.getDateInstance().parse(date));
        } catch(Exception ex) {
            _agreedPatients.setPatientsFrom(null);
        }
    }
     
    private ModelIntentionController getModelIntentionController() {
        return (ModelIntentionController) _sessionController.getFeatureController(Feature.MODEL_INTENTION);
    }

    public String save() {
        if (!check4validSession()) {
            return Pages.InvalidConversation.URL();
        }
        
        _modelIntention = _modelIntentionFacade.saveModelIntention(_modelIntention);

        if (isValidId(_modelIntention.getMiId())) {
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
        return ModelIntentionStatus.Rejected.getValue() == _modelIntention.getStatus().intValue();
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
            return Pages.Error.URL();
        }
        if (_modelIntention.getStatus().intValue() >= 10){return Pages.Error.URL();}

        _modelIntention.setStatus(10 + _modelIntention.getStatus().intValue());
        _modelIntention = _modelIntentionFacade.saveModelIntention(_modelIntention);
        if (isValidId(_modelIntention.getMiId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameMODEL_INTENTION"));
            Utils.getFlash().put("targetPage", Pages.ModelIntentionSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_modelIntention));
            _sessionController.endConversation();
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
        if (_modelIntention.getStatus().intValue() >= 10){return Pages.Error.URL();}

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
        return _conversationId.equals(_sessionController.getConversationId());
    }

    // <editor-fold defaultstate="collapsed" desc="CheckElements">

    // </editor-fold>
}
