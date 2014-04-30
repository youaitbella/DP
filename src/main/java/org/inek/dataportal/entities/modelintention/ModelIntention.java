package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "ModelIntention", schema = "mvh")
public class ModelIntention implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "miId")
    private Integer _id;

    @Column(name = "miAccountId")
    private int _accountId;

    @Column(name = "miAgeYearsFrom")
    private int _ageYearsFrom = -1;

    @Column(name = "miAgeYearsTo")
    private int _ageYearsTo = -1;

    @Column(name = "miSex")
    private int _sex = -1;

    @Column(name = "miMiscPatient")
    private String _miscPatient = "";

    @Column(name = "miRegion")
    private String _region = "";

    @Column(name = "miMedicalAttributesType")
    private int _medicalAttributesType;

    @Column(name = "miMedicalSpecification")
    private String _medicalSpecification = "";

    // <editor-fold defaultstate="collapsed" desc="insuranceAffiliation">
    @Column(name = "miInsuranceAffiliation")
    private String _insuranceAffiliation = "";
    public String getInsuranceAffiliation() {
        return _insuranceAffiliation;
    }

    public void setInsuranceAffiliation(String insuranceAffiliation) {
        _insuranceAffiliation = insuranceAffiliation;
    }
    // </editor-fold>

    @Column(name = "miMiscAttribute")
    private String _miscAttribute = "";

    @Column(name = "miAgreement")
    private boolean _agreement;

    @Column(name = "miSettleMedicType")
    private int _settleMedicType;

    @Column(name = "miSettleMedicText")
    private String _settleMedicText = "";

    @Column(name = "miPIAType")
    private int _piaType;

    @Column(name = "miPIAText")
    private String _piaText = "";

    @Column(name = "miHospitalType")
    private int _hospitalType;

    @Column(name = "miHospitalText")
    private String _hospitalText = "";

    @Column(name = "miSelfHospitalisationType")
    private int _selfHospitalisationType;

    @Column(name = "miMiscHospitalisation")
    private String _miscHospitalisation = "";

    @Column(name = "miPrimaryGoals")
    private String _primaryGoals = "";

    @Column(name = "miPatientGoals")
    private String _patientGoals = "";

    @Column(name = "miProviderGoals")
    private String _providerGoals = "";

    @Column(name = "miSponsorGoals")
    private String _sponsorGoals = "";

    @Column(name = "miInvolvedGoals")
    private String _involvedGoals = "";

    // <editor-fold defaultstate="collapsed" desc="inpatientTreatment">
    @Column(name = "miInpatientType")
    private int _inpatientTreatmentType;
    public int getInpatientTreatmentType() {
        return _inpatientTreatmentType;
    }

    public void setInpatientTreatmentType(int inpatientType) {
        _inpatientTreatmentType = inpatientType;
    }

    @Column(name = "miInpatientText")
    private String _inpatientTreatmentText = "";
    public String getInpatientTreatmentText() {
        return _inpatientTreatmentText;
    }

    public void setInpatientTreatmentText(String inpatientText) {
        _inpatientTreatmentText = inpatientText;
    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="dayPatient">
    @Column(name = "miDayPatientType")
    private int _dayPatientTreatmentType;
    public int getDayPatientTreatmentType() {
        return _dayPatientTreatmentType;
    }

    public void setDayPatientTreatmentType(int dayPatientType) {
        _dayPatientTreatmentType = dayPatientType;
    }

    @Column(name = "miDayPatientText")
    private String _dayPatientTreatmentText = "";
    public String getDayPatientTreatmentText() {
        return _dayPatientTreatmentText;
    }

    public void setDayPatientTreatmentText(String dayPatientText) {
        _dayPatientTreatmentText = dayPatientText;
    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="InpatientCompensationTreatment">
    @Column(name = "miInpatientCompensationType")
    private int _inpatientCompensationTreatmentType;

    public int getInpatientCompensationTreatmentType() {
        return _inpatientCompensationTreatmentType;
    }

    public void setInpatientCompensationTreatmentType(int inpatientCompensationTreatmentType) {
        _inpatientCompensationTreatmentType = inpatientCompensationTreatmentType;
    }

    @Column(name = "miInpatientCompensationText")
    private String _inpatientCompensationTreatmentText = "";

    public String getInpatientCompensationTreatmentText() {
        return _inpatientCompensationTreatmentText;
    }

    public void setInpatientCompensationTreatmentText(String inpatientCompensationTreatmentText) {
        _inpatientCompensationTreatmentText = inpatientCompensationTreatmentText;
    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="InpatientCompensationHomeTreatment">
    @Column(name = "miInpatientCompensationHomeType")
    private int _inpatientCompensationHomeTreatmentType;

    public int getInpatientCompensationHomeTreatmentType() {
        return _inpatientCompensationHomeTreatmentType;
    }

    public void setInpatientCompensationHomeTreatmentType(int inpatientCompensationHomeTreatmentType) {
        _inpatientCompensationHomeTreatmentType = inpatientCompensationHomeTreatmentType;
    }

    @Column(name = "miInpatientCompensationHomeText")
    private String _inpatientCompensationHomeTreatmentText = "";

    public String getInpatientCompensationHomeTreatmentText() {
        return _inpatientCompensationHomeTreatmentText;
    }

    public void setInpatientCompensationHomeTreatmentText(String inpatientCompensationHomeTreatmentText) {
        _inpatientCompensationHomeTreatmentText = inpatientCompensationHomeTreatmentText;
    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OutpatientTreatment">
    @Column(name = "miOutpatientTreatmentType")
    private int _outpatientTreatmentType;
    
    public int getOutpatientTreatmentType() {
        return _outpatientTreatmentType;
    }

    public void setOutpatientTreatmentType(int outpatientTreatmentType) {
        _outpatientTreatmentType = outpatientTreatmentType;
    }

    @Column(name = "miOutpatientTreatmentText")
    private String _outpatientTreatmentText = "";

    public String getOutpatientTreatmentText() {
        return _outpatientTreatmentText;
    }

    public void setOutpatientTreatmentText(String outPatientText) {
        _outpatientTreatmentText = outPatientText;
    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OutpatientHomeTreatment">
    @Column(name = "miOutpatientHomeTreatmentType")
    private int _outpatientHomeTreatmentType;
    
    public int getOutpatientHomeTreatmentType() {
        return _outpatientHomeTreatmentType;
    }

    public void setOutpatientHomeTreatmentType(int outpatientHomeTreatmentType) {
        _outpatientHomeTreatmentType = outpatientHomeTreatmentType;
    }

    @Column(name = "miOutpatientHomeTreatmentText")
    private String _outpatientHomeTreatmentText = "";

    public String getOutpatientHomeTreatmentText() {
        return _outpatientHomeTreatmentText;
    }

    public void setOutpatientHomeTreatmentText(String outPatientText) {
        _outpatientHomeTreatmentText = outPatientText;
    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MiscTreatment">
    @Column(name = "miMiscTreatment")
    private String _miscTreatment = "";
    public String getMiscTreatment() {
        return _miscTreatment;
    }

    public void setMiscTreatment(String miscTreatment) {
        this._miscTreatment = miscTreatment;
    }
    // </editor-fold>

    @Column(name = "miCaseManagement")
    private String _caseManagement = "";

    @Column(name = "miTeamBasedInnovations")
    private String _teamBasedInnovations = "";

    @Column(name = "miCrossSectoralSupply")
    private String _crossSectoralSupply = "";

    @Column(name = "miHomeTreatment")
    private String _homeTreatment = "";

    @Column(name = "miMiscSpecialPatientConcept")
    private String _miscSpecialPatientConcept = "";

    @Column(name = "miSpecialPsyTherapy")
    private String _specialPsyTherapy = "";

    @Column(name = "miSpecialMedicalMethod")
    private String _specialMedicalMethod = "";

    @Column(name = "miOtherSpecialTherapyMethod")
    private String _otherSpecialTherapyMethod = "";

    @Column(name = "miMiscSpecificActivityContent")
    private String _miscSpecificActivityContent = "";

    @Column(name = "miSingleRemuneration")
    private String _singleRemuneration = "";

    @Column(name = "miDayPackage")
    private String _dayPackage = "";

    @Column(name = "miCasePackage")
    private String _casePackage = "";

    @Column(name = "miQuarterPackage")
    private String _quarterPackage = "";

    @Column(name = "miComplexPackage")
    private String _complexPackage = "";

    @Column(name = "miPEPPRemuneration")
    private String _peppRemuneration = "";

    @Column(name = "miRegionalBudget")
    private String _regionalBudget = "";

    @Column(name = "miMiscRemuneration")
    private String _miscRemuneration = "";

    @Column(name = "miStatus")
    private Integer _status = -1;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "apModelIntentionId", referencedColumnName = "miId")
    @OrderBy("_id")
    private List<AgreedPatients> _agreedPatients;

    // <editor-fold defaultstate="collapsed" desc="remuneration">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "reModelIntentionId", referencedColumnName = "miId")
    @OrderBy("_id")
    private List<Remuneration> _remunerations;

    public List<Remuneration> getRemunerations() {
        if (_remunerations == null) {
            _remunerations = new ArrayList<>();
        }
        return _remunerations;
    }

    public void setRemunerations(List<Remuneration> remunerations) {
        _remunerations = remunerations;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="cost">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "coModelIntentionId", referencedColumnName = "miId")
    @OrderBy("_id")
    private List<Cost> _costs;

    public List<Cost> getCosts() {
        if (_costs == null) {
            _costs = new ArrayList<>();
        }
        return _costs;
    }

    public void setCosts(List<Cost> costs) {
        _costs = costs;
    }
    // </editor-fold>

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "mlModelIntentionId", referencedColumnName = "miId")
    @OrderBy("_startDate")
    private List<ModelLife> _modelLifes;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "csModelIntentionId", referencedColumnName = "miId")
    @OrderBy("_id")
    private List<ModelIntentionContact> _contacts;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "asModelIntentionId", referencedColumnName = "miId")
    @OrderBy("_id")
    private List<AcademicSupervision> _academicSupervisions;

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer miId) {
        this._id = miId;
    }

    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }

    public Integer getAgeYearsFrom() {
        return _ageYearsFrom == -1 ? null : _ageYearsFrom;
    }

    public void setAgeYearsFrom(Integer ageYearsFrom) {
        this._ageYearsFrom = ageYearsFrom == null ? -1 : ageYearsFrom;
    }

    public Integer getAgeYearsTo() {
        return _ageYearsTo == -1 ? null : _ageYearsTo;
    }

    public void setAgeYearsTo(Integer ageYearsTo) {
        ageYearsTo = ageYearsTo == null ? -1 : ageYearsTo;
        this._ageYearsTo = ageYearsTo;
    }

    public int getSex() {
        return _sex;
    }

    public void setSex(int sex) {
        this._sex = sex;
    }

    public String getMiscPatient() {
        return _miscPatient;
    }

    public void setMiscPatient(String miscPatient) {
        this._miscPatient = miscPatient;
    }

    public String getRegion() {
        return _region;
    }

    public void setRegion(String region) {
        this._region = region;
    }

    public int getMedicalAttributesType() {
        return _medicalAttributesType;
    }

    public void setMedicalAttributesType(int medicalAttributesType) {
        this._medicalAttributesType = medicalAttributesType;
    }

    public String getMedicalSpecification() {
        return _medicalSpecification;
    }

    public void setMedicalSpecification(String medicalSpecification) {
        this._medicalSpecification = medicalSpecification;
    }

    public String getMiscAttribute() {
        return _miscAttribute;
    }

    public void setMiscAttribute(String miscAttribute) {
        this._miscAttribute = miscAttribute;
    }

    public boolean isAgreement() {
        return _agreement;
    }

    public void setAgreement(boolean agreement) {
        this._agreement = agreement;
    }

    public int getSettleMedicType() {
        return _settleMedicType;
    }

    public void setSettleMedicType(int settleMedicType) {
        this._settleMedicType = settleMedicType;
    }

    public String getSettleMedicText() {
        return _settleMedicText;
    }

    public void setSettleMedicText(String settleMedicText) {
        this._settleMedicText = settleMedicText;
    }

    public int getPiaType() {
        return _piaType;
    }

    public void setPiaType(int piaType) {
        this._piaType = piaType;
    }

    public String getPiaText() {
        return _piaText;
    }

    public void setPiaText(String piaText) {
        this._piaText = piaText;
    }

    public int getHospitalType() {
        return _hospitalType;
    }

    public void setHospitalType(int hospitalType) {
        this._hospitalType = hospitalType;
    }

    public String getHospitalText() {
        return _hospitalText;
    }

    public void setHospitalText(String hospitalText) {
        this._hospitalText = hospitalText;
    }

    public int getSelfHospitalisationType() {
        return _selfHospitalisationType;
    }

    public void setSelfHospitalisationType(int selfHospitalisationType) {
        this._selfHospitalisationType = selfHospitalisationType;
    }

    public String getMiscHospitalisation() {
        return _miscHospitalisation;
    }

    public void setMiscHospitalisation(String miscHospitalisation) {
        this._miscHospitalisation = miscHospitalisation;
    }

    public String getPrimaryGoals() {
        return _primaryGoals;
    }

    public void setPrimaryGoals(String primaryGoals) {
        this._primaryGoals = primaryGoals;
    }

    public String getPatientGoals() {
        return _patientGoals;
    }

    public void setPatientGoals(String patientGoals) {
        this._patientGoals = patientGoals;
    }

    public String getProviderGoals() {
        return _providerGoals;
    }

    public void setProviderGoals(String providerGoals) {
        this._providerGoals = providerGoals;
    }

    public String getSponsorGoals() {
        return _sponsorGoals;
    }

    public void setSponsorGoals(String sponsorGoals) {
        this._sponsorGoals = sponsorGoals;
    }

    public String getInvolvedGoals() {
        return _involvedGoals;
    }

    public void setInvolvedGoals(String involvedGoals) {
        this._involvedGoals = involvedGoals;
    }

    public String getCaseManagement() {
        return _caseManagement;
    }

    public void setCaseManagement(String caseManagement) {
        this._caseManagement = caseManagement;
    }

    public String getTeamBasedInnovations() {
        return _teamBasedInnovations;
    }

    public void setTeamBasedInnovations(String teamBasedInnovations) {
        this._teamBasedInnovations = teamBasedInnovations;
    }

    public String getCrossSectoralSupply() {
        return _crossSectoralSupply;
    }

    public void setCrossSectoralSupply(String crossSectoralSupply) {
        this._crossSectoralSupply = crossSectoralSupply;
    }

    public String getHomeTreatment() {
        return _homeTreatment;
    }

    public void setHomeTreatment(String homeTreatment) {
        this._homeTreatment = homeTreatment;
    }

    public String getMiscSpecialPatientConcept() {
        return _miscSpecialPatientConcept;
    }

    public void setMiscSpecialPatientConcept(String miscSpecialPatientConcept) {
        this._miscSpecialPatientConcept = miscSpecialPatientConcept;
    }

    public String getSpecialPsyTherapy() {
        return _specialPsyTherapy;
    }

    public void setSpecialPsyTherapy(String specialPsyTherapy) {
        this._specialPsyTherapy = specialPsyTherapy;
    }

    public String getSpecialMedicalMethod() {
        return _specialMedicalMethod;
    }

    public void setSpecialMedicalMethod(String specialMedicalMethod) {
        this._specialMedicalMethod = specialMedicalMethod;
    }

    public String getOtherSpecialTherapyMethod() {
        return _otherSpecialTherapyMethod;
    }

    public void setOtherSpecialTherapyMethod(String otherSpecialTherapyMethod) {
        this._otherSpecialTherapyMethod = otherSpecialTherapyMethod;
    }

    public String getMiscSpecificActivityContent() {
        return _miscSpecificActivityContent;
    }

    public void setMiscSpecificActivityContent(String miscSpecificActivityContent) {
        this._miscSpecificActivityContent = miscSpecificActivityContent;
    }

    public String getSingleRemuneration() {
        return _singleRemuneration;
    }

    public void setSingleRemuneration(String singleRemuneration) {
        this._singleRemuneration = singleRemuneration;
    }

    public String getDayPackage() {
        return _dayPackage;
    }

    public void setDayPackage(String dayPackage) {
        this._dayPackage = dayPackage;
    }

    public String getCasePackage() {
        return _casePackage;
    }

    public void setCasePackage(String casePackage) {
        this._casePackage = casePackage;
    }

    public String getQuarterPackage() {
        return _quarterPackage;
    }

    public void setQuarterPackage(String quarterPackage) {
        this._quarterPackage = quarterPackage;
    }

    public String getComplexPackage() {
        return _complexPackage;
    }

    public void setComplexPackage(String complexPackage) {
        this._complexPackage = complexPackage;
    }

    public String getPeppRemuneration() {
        return _peppRemuneration;
    }

    public void setPeppRemuneration(String peppRemuneration) {
        this._peppRemuneration = peppRemuneration;
    }

    public String getRegionalBudget() {
        return _regionalBudget;
    }

    public void setRegionalBudget(String regionalBudget) {
        this._regionalBudget = regionalBudget;
    }

    public String getMiscRemuneration() {
        return _miscRemuneration;
    }

    public void setMiscRemuneration(String miscRemuneration) {
        this._miscRemuneration = miscRemuneration;
    }

    public int getStatus() {
        return _status;
    }

    public void setStatus(int status) {
        this._status = status;
    }

    public List<AgreedPatients> getAgreedPatients() {
        if (_agreedPatients == null) {
            _agreedPatients = new ArrayList<>();
        }
        return _agreedPatients;
    }

    public void setAgreedPatients(List<AgreedPatients> agreedPatients) {
        _agreedPatients = agreedPatients;
    }

    public List<ModelIntentionContact> getContacts() {
        if (_contacts == null) {
            _contacts = new ArrayList<>();
            // at least, there must be two partners:
            _contacts.add(new ModelIntentionContact(1));
            _contacts.add(new ModelIntentionContact(1));
        }
        return _contacts;
    }

    public void setContacts(List<ModelIntentionContact> contacts) {
        _contacts = contacts;
    }

    public List<ModelLife> getModelLifes() {
        if (_modelLifes == null) {
            _modelLifes = new ArrayList<>();
        }
        return _modelLifes;
    }

    public void setModelLifes(List<ModelLife> modelLifes) {
        _modelLifes = modelLifes;
    }

    public List<AcademicSupervision> getAcademicSupervisions() {
        if (_academicSupervisions == null) {
            _academicSupervisions = new ArrayList<>();
            _academicSupervisions.add(new AcademicSupervision());
        }
        return _academicSupervisions;
    }

    public void setAcademicSupervisions(List<AcademicSupervision> academicSupervisions) {
        _academicSupervisions = academicSupervisions;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModelIntention)) {
            return false;
        }
        ModelIntention other = (ModelIntention) object;
        if ((_id == null && other.getId() != null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.ModelIntention[id=" + _id + "]";
    }

    // </editor-fold>
}
