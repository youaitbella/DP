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
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "ModelIntention", schema = "mvh")
public class ModelIntention implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "miId")
    private Integer _id;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer miId) {
        this._id = miId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "miAccountId")
    private int _accountId;

    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property code">
    @Documentation(key = "lblModelIntentionCode")
    @Column(name = "miCode")
    private String _code = "";

    @Size(max = 8)
    @Pattern(regexp = "(^$)|([ME](0\\d|1[0-7])A[A-K]\\d{3})", message = "Die Eingabe entspricht nicht der Syntax des Kennzeichens.")
    public String getCode() {
        return _code;
    }

    public void setCode(String code) {
        _code = code;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property description">
    @Documentation(key = "lblAppellation")
    @Column(name = "miDescription")
    //@Size(min = 1, max = 100)
    private String _description = "";

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AgeYearsFrom">
    @Documentation(key = "lblAgeFrom", omitOnValues = "-1")
    @Column(name = "miAgeYearsFrom")
    private int _ageYearsFrom = -1;

    @Min(-1)
    @Max(124)
    public Integer getAgeYearsFrom() {
        return _ageYearsFrom == -1 ? null : _ageYearsFrom;
    }

    public void setAgeYearsFrom(Integer ageYearsFrom) {
        this._ageYearsFrom = ageYearsFrom == null ? -1 : ageYearsFrom;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AgeYearsTo">
    @Documentation(key = "lblAgeTo", omitOnValues = "-1")
    @Column(name = "miAgeYearsTo")
    private int _ageYearsTo = -1;

    @Min(-1)
    @Max(124)
    public Integer getAgeYearsTo() {
        return _ageYearsTo == -1 ? null : _ageYearsTo;
    }

    public void setAgeYearsTo(Integer ageYearsTo) {
        ageYearsTo = ageYearsTo == null ? -1 : ageYearsTo;
        this._ageYearsTo = ageYearsTo;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property sex">
    @Documentation(key = "lblGender", translateValue = "-1=empty;0=enmGenderBoth;1=enmGenderMale;2=enmGenderFemale")
    @Column(name = "miSex")
    private int _sex = 0;

    // @Size(min = 1, max = 50)
    public int getSex() {
        return _sex;
    }

    public void setSex(int sex) {
        this._sex = sex;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MiscPatient">
    @Documentation(key = "lblPersonalMisc", omitOnEmpty = true)
    @Column(name = "miMiscPatient")
    private String _miscPatient = "";

    public String getMiscPatient() {
        return _miscPatient;
    }

    public void setMiscPatient(String miscPatient) {
        this._miscPatient = miscPatient;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property RegionType">
    @Documentation(key = "lblRegionalFeatures", translateValue = "-1=empty;0=enmRegionGer;1=enmRegionState;2=enmRegionMisc")
    @Column(name = "miRegionType")
    private Integer _regionType = -1;

    public Integer getRegionType() {
        return _regionType;
    }

    public void setRegionType(Integer regionType) {
        this._regionType = regionType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property region">
    @Documentation(key = "empty", omitOnEmpty = true)
    @Column(name = "miRegion")
    private String _region = "";

    public void setRegion(String region) {
        this._region = region;
    }

    public String getRegion() {
        return _region;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property medicalAttributesType">
    @Documentation(key = "lblMedicalFeature", translateValue = "0=enmMedAttrMainDia;1=enmMedAttrPracticeArea;2=enmMedAttrMisc")
    @Column(name = "miMedicalAttributesType")
    private int _medicalAttributesType;

    public int getMedicalAttributesType() {
        return _medicalAttributesType;
    }

    public void setMedicalAttributesType(int medicalAttributesType) {
        this._medicalAttributesType = medicalAttributesType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property MedicalSpecification">
    @Documentation(key = "empty", omitOnEmpty = true)
    @Column(name = "miMedicalSpecification")
    private String _medicalSpecification = "";

    public String getMedicalSpecification() {
        return _medicalSpecification;
    }

    public void setMedicalSpecification(String medicalSpecification) {
        this._medicalSpecification = medicalSpecification;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property insuranceAffiliation">
    @Column(name = "miInsuranceAffiliation")

    @Documentation(key = "lblModelIntentionHealthInsurance", translateValue = "0=enmNoAttribute;1=enmInsuranceAffiliationNamed")
    private int _insuranceAffiliation = 0;
    public int getInsuranceAffiliation() {
        return _insuranceAffiliation;
    }

    public void setInsuranceAffiliation(int insuranceAffiliation) {
        _insuranceAffiliation = insuranceAffiliation;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property miscAttribute">
    @Column(name = "miMiscAttribute")
    @Documentation(key = "lblMiscAttr", omitOnEmpty = true)
    private String _miscAttribute = "";

    public String getMiscAttribute() {
        return _miscAttribute;
    }

    public void setMiscAttribute(String miscAttribute) {
        this._miscAttribute = miscAttribute;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Agreement">
    @Column(name = "miAgreement")
    private boolean _agreement = false;

    @NotNull
    public boolean isAgreement() {
        return _agreement;
    }

    public void setAgreement(boolean agreement) {
        this._agreement = agreement;
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SettleMedicType">
    @Documentation(key = "lblSettledDocs", translateValue = "0=enmSettleTypeImpDep;1=enmSettleTypeDepDocs;2=enmSettleTypeMiscDocs")
    @Column(name = "miSettleMedicType")
    private int _settleMedicType = 0;

    public int getSettleMedicType() {
        return _settleMedicType;
    }

    public void setSettleMedicType(int settleMedicType) {
        this._settleMedicType = settleMedicType;
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SettleMedicText">
    @Documentation(key = "empty", omitOnEmpty = true)
    @Column(name = "miSettleMedicText")
    private String _settleMedicText = "";

    public String getSettleMedicText() {
        return _settleMedicText;
    }

    public void setSettleMedicText(String settleMedicText) {
        this._settleMedicText = settleMedicText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PIAType">
    @Documentation(key = "lblPia", translateValue = "0=enmPiaTypeAny;1=enmPiaTypeIntegrated;2=enmPiaTypeContract;3=enmPiaTypeSpecific")
    @Column(name = "miPIAType")
    private int _piaType = 0;

    public int getPiaType() {
        return _piaType;
    }

    public void setPiaType(int piaType) {
        this._piaType = piaType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PIAText">
    @Documentation(key = "empty", omitOnEmpty = true)
    @Column(name = "miPIAText")
    private String _piaText = "";

    public String getPiaText() {
        return _piaText;
    }

    public void setPiaText(String piaText) {
        this._piaText = piaText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property HospitalType">
    @Documentation(key = "lblHospital", 
            translateValue = "-1=lblNo;0=enmHospitalTypeAny;1=enmHospitalTypeModelProject;2=enmHospitalTypeSpecific;3=enmHospitalTypeOther")
    @Column(name = "miHospitalType")
    private int _hospitalType;

    public int getHospitalType() {
        return _hospitalType;
    }

    public void setHospitalType(int hospitalType) {
        this._hospitalType = hospitalType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property HospitalText">
    @Documentation(key = "empty", omitOnEmpty = true)
    @Column(name = "miHospitalText")
    private String _hospitalText = "";

    public String getHospitalText() {
        return _hospitalText;
    }

    public void setHospitalText(String hospitalText) {
        this._hospitalText = hospitalText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SelfHospitalisationType">
    @Documentation(key = "lblSelfHospitalisation", 
            translateValue = "0=enmSelfHospitalisationTypePossible;1=enmSelfHospitalisationTypeEmergency;2=enmSelfHospitalisationTypeImpossible")
    @Column(name = "miSelfHospitalisationType")
    private int _selfHospitalisationType = 0;

    public int getSelfHospitalisationType() {
        return _selfHospitalisationType;
    }

    public void setSelfHospitalisationType(int selfHospitalisationType) {
        this._selfHospitalisationType = selfHospitalisationType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property MiscHospitalisation">
    @Documentation(key = "empty", omitOnEmpty = true)
    @Column(name = "miMiscHospitalisation")
    private String _miscHospitalisation = "";

    public String getMiscHospitalisation() {
        return _miscHospitalisation;
    }

    public void setMiscHospitalisation(String miscHospitalisation) {
        this._miscHospitalisation = miscHospitalisation;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="list AgreedPatiens">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "apModelIntentionId", referencedColumnName = "miId")
    @OrderBy("_id")
    @Documentation(key = "lblAgreedPatiens")
    @Valid
    private List<AgreedPatients> _agreedPatients;

    public List<AgreedPatients> getAgreedPatients() {
        if (_agreedPatients == null) {
            _agreedPatients = new ArrayList<>();
        }
        return _agreedPatients;
    }

    public void setAgreedPatients(List<AgreedPatients> agreedPatients) {
        _agreedPatients = agreedPatients;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PrimaryGoals">
    @Documentation(key = "lblModelIntentionHigherGoals", omitOnEmpty = true)
    @Column(name = "miPrimaryGoals")
    private String _primaryGoals = "";

    public String getPrimaryGoals() {
        return _primaryGoals;
    }

    public void setPrimaryGoals(String primaryGoals) {
        this._primaryGoals = primaryGoals;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PatientGoals">
    @Documentation(key = "lblModelIntentionPatientGoals", omitOnEmpty = true)
    @Column(name = "miPatientGoals")
    private String _patientGoals = "";

    public String getPatientGoals() {
        return _patientGoals;
    }

    public void setPatientGoals(String patientGoals) {
        this._patientGoals = patientGoals;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ProviderGoals">
    @Documentation(key = "lblModelIntentionProviderGoals", omitOnEmpty = true)
    @Column(name = "miProviderGoals")
    private String _providerGoals = "";

    public String getProviderGoals() {
        return _providerGoals;
    }

    public void setProviderGoals(String providerGoals) {
        this._providerGoals = providerGoals;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SponsorGoals">
    @Documentation(key = "lblModelIntentionSponsorGoals", omitOnEmpty = true)
    @Column(name = "miSponsorGoals")
    private String _sponsorGoals = "";

    public String getSponsorGoals() {
        return _sponsorGoals;
    }

    public void setSponsorGoals(String sponsorGoals) {
        this._sponsorGoals = sponsorGoals;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InvolvedGoals">
    @Documentation(key = "lblModelIntentionInvolvedGoals", omitOnEmpty = true)
    @Column(name = "miInvolvedGoals")
    private String _involvedGoals = "";

    public String getInvolvedGoals() {
        return _involvedGoals;
    }

    public void setInvolvedGoals(String involvedGoals) {
        this._involvedGoals = involvedGoals;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property inpatientTreatment">
    @Documentation(key = "lblInpatientTreatment", translateValue = "0=enmTreatmentTypeNo;1=enmTreatmentTypeGeneral;2=enmTreatmentTypeSpecial")
    @Column(name = "miInpatientType")
    private int _inpatientTreatmentType = 0;
    public int getInpatientTreatmentType() {
        return _inpatientTreatmentType;
    }

    public void setInpatientTreatmentType(int inpatientType) {
        _inpatientTreatmentType = inpatientType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InpatientText">
    @Documentation(key = "empty", omitOnEmpty = true)
    @Column(name = "miInpatientText")
    private String _inpatientTreatmentText = "";
    public String getInpatientTreatmentText() {
        return _inpatientTreatmentText;
    }

    public void setInpatientTreatmentText(String inpatientText) {
        _inpatientTreatmentText = inpatientText;
    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property dayPatient">
    @Documentation(key = "lblPartialStationaryTreatment", 
            translateValue = "0=enmTreatmentTypeNo;1=enmTreatmentTypeGeneral;2=enmTreatmentTypeSpecial")
    @Column(name = "miDayPatientType")
    private int _dayPatientTreatmentType;
    public int getDayPatientTreatmentType() {
        return _dayPatientTreatmentType;
    }

    public void setDayPatientTreatmentType(int dayPatientType) {
        _dayPatientTreatmentType = dayPatientType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DayPatientText">
    @Documentation(key = "empty", omitOnEmpty = true)
    @Column(name = "miDayPatientText")
    private String _dayPatientTreatmentText = "";
    public String getDayPatientTreatmentText() {
        return _dayPatientTreatmentText;
    }

    public void setDayPatientTreatmentText(String dayPatientText) {
        _dayPatientTreatmentText = dayPatientText;
    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property inpatientCompensationTreatment">
    @Documentation(key = "lblInpatientCompensationTreatment", 
            translateValue = "0=enmTreatmentTypeNo;1=enmTreatmentTypeGeneral;2=enmTreatmentTypeSpecial")
    @Column(name = "miInpatientCompensationType")
    private int _inpatientCompensationTreatmentType = 0;

    public int getInpatientCompensationTreatmentType() {
        return _inpatientCompensationTreatmentType;
    }

    public void setInpatientCompensationTreatmentType(int inpatientCompensationTreatmentType) {
        _inpatientCompensationTreatmentType = inpatientCompensationTreatmentType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InpatientCompensationText">
    @Documentation(key = "empty", omitOnEmpty = true)
    @Column(name = "miInpatientCompensationText")
    private String _inpatientCompensationTreatmentText = "";

    public String getInpatientCompensationTreatmentText() {
        return _inpatientCompensationTreatmentText;
    }

    public void setInpatientCompensationTreatmentText(String inpatientCompensationTreatmentText) {
        _inpatientCompensationTreatmentText = inpatientCompensationTreatmentText;
    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  inpatientCompensationHomeTreatment">
    @Documentation(key = "lblInpatientCompensationHomeTreatment", 
            translateValue = "0=enmTreatmentTypeNo;1=enmTreatmentTypeGeneral;2=enmTreatmentTypeSpecial")
    @Column(name = "miInpatientCompensationHomeType")
    private int _inpatientCompensationHomeTreatmentType;

    public int getInpatientCompensationHomeTreatmentType() {
        return _inpatientCompensationHomeTreatmentType;
    }

    public void setInpatientCompensationHomeTreatmentType(int inpatientCompensationHomeTreatmentType) {
        _inpatientCompensationHomeTreatmentType = inpatientCompensationHomeTreatmentType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  InpatientCompensationHomeText">
    @Documentation(key = "empty", omitOnEmpty = true)
    @Column(name = "miInpatientCompensationHomeText")
    private String _inpatientCompensationHomeTreatmentText = "";

    public String getInpatientCompensationHomeTreatmentText() {
        return _inpatientCompensationHomeTreatmentText;
    }

    public void setInpatientCompensationHomeTreatmentText(String inpatientCompensationHomeTreatmentText) {
        _inpatientCompensationHomeTreatmentText = inpatientCompensationHomeTreatmentText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property outpatientTreatment">
    @Documentation(key = "lblOutpatientTreatment", translateValue = "0=enmTreatmentTypeNo;1=enmTreatmentTypeGeneral;2=enmTreatmentTypeSpecial")
    @Column(name = "miOutpatientTreatmentType")
    private int _outpatientTreatmentType = 0;

    public int getOutpatientTreatmentType() {
        return _outpatientTreatmentType;
    }

    public void setOutpatientTreatmentType(int outpatientTreatmentType) {
        _outpatientTreatmentType = outpatientTreatmentType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property outpatientTreatmentText">
    @Documentation(key = "empty", omitOnEmpty = true)
    @Column(name = "miOutpatientTreatmentText")
    private String _outpatientTreatmentText = "";

    public String getOutpatientTreatmentText() {
        return _outpatientTreatmentText;
    }

    public void setOutpatientTreatmentText(String outPatientText) {
        _outpatientTreatmentText = outPatientText;
    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  outpatientHomeTreatment">
    @Documentation(key = "lblOutpatientHomeTreatment", translateValue = "0=enmTreatmentTypeNo;1=enmTreatmentTypeGeneral;2=enmTreatmentTypeSpecial")
    @Column(name = "miOutpatientHomeTreatmentType")
    private int _outpatientHomeTreatmentType;

    public int getOutpatientHomeTreatmentType() {
        return _outpatientHomeTreatmentType;
    }

    public void setOutpatientHomeTreatmentType(int outpatientHomeTreatmentType) {
        _outpatientHomeTreatmentType = outpatientHomeTreatmentType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  outpatientHomeTreatmentText">
    @Documentation(key = "empty", omitOnEmpty = true)
    @Column(name = "miOutpatientHomeTreatmentText")
    private String _outpatientHomeTreatmentText = "";

    public String getOutpatientHomeTreatmentText() {
        return _outpatientHomeTreatmentText;
    }

    public void setOutpatientHomeTreatmentText(String outPatientText) {
        _outpatientHomeTreatmentText = outPatientText;
    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  miscTreatment">
    @Documentation(key = "lblMisc", omitOnEmpty = true)
    @Column(name = "miMiscTreatment")
    private String _miscTreatment = "";
    public String getMiscTreatment() {
        return _miscTreatment;
    }

    public void setMiscTreatment(String miscTreatment) {
        this._miscTreatment = miscTreatment;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  CaseManagement">
    @Documentation(key = "lblCaseManagement", omitOnEmpty = true)
    @Column(name = "miCaseManagement")
    private String _caseManagement = "";

    public String getCaseManagement() {
        return _caseManagement;
    }

    public void setCaseManagement(String caseManagement) {
        this._caseManagement = caseManagement;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  TeamBasedInnovations">
    @Documentation(key = "lblTeamBasedInnovations", omitOnEmpty = true)
    @Column(name = "miTeamBasedInnovations")
    private String _teamBasedInnovations = "";

    public String getTeamBasedInnovations() {
        return _teamBasedInnovations;
    }

    public void setTeamBasedInnovations(String teamBasedInnovations) {
        this._teamBasedInnovations = teamBasedInnovations;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  CrossSectoralSupply">
    @Documentation(key = "lblCrossSectoralSupply", omitOnEmpty = true)
    @Column(name = "miCrossSectoralSupply")
    private String _crossSectoralSupply = "";

    public String getCrossSectoralSupply() {
        return _crossSectoralSupply;
    }

    public void setCrossSectoralSupply(String crossSectoralSupply) {
        this._crossSectoralSupply = crossSectoralSupply;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  HomeTreatment">
    @Documentation(key = "lblHomeTreatment", omitOnEmpty = true)
    @Column(name = "miHomeTreatment")
    private String _homeTreatment = "";

    public String getHomeTreatment() {
        return _homeTreatment;
    }

    public void setHomeTreatment(String homeTreatment) {
        this._homeTreatment = homeTreatment;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  MiscSpecialPatientConcept">
    @Documentation(key = "lblMisc", omitOnEmpty = true)
    @Column(name = "miMiscSpecialPatientConcept")
    private String _miscSpecialPatientConcept = "";

    public String getMiscSpecialPatientConcept() {
        return _miscSpecialPatientConcept;
    }

    public void setMiscSpecialPatientConcept(String miscSpecialPatientConcept) {
        this._miscSpecialPatientConcept = miscSpecialPatientConcept;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  SpecialPsyTherapy">
    @Documentation(key = "lblSpecialPsyTherapy", omitOnEmpty = true)
    @Column(name = "miSpecialPsyTherapy")
    private String _specialPsyTherapy = "";

    public String getSpecialPsyTherapy() {
        return _specialPsyTherapy;
    }

    public void setSpecialPsyTherapy(String specialPsyTherapy) {
        this._specialPsyTherapy = specialPsyTherapy;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  SpecialMedicalMethod">
    @Documentation(key = "lblSpecialMedicalMethod", omitOnEmpty = true)
    @Column(name = "miSpecialMedicalMethod")
    private String _specialMedicalMethod = "";

    public String getSpecialMedicalMethod() {
        return _specialMedicalMethod;
    }

    public void setSpecialMedicalMethod(String specialMedicalMethod) {
        this._specialMedicalMethod = specialMedicalMethod;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  OtherSpecialTherapyMethod">
    @Documentation(key = "lblOtherSpecialMedicalMethod", omitOnEmpty = true)
    @Column(name = "miOtherSpecialTherapyMethod")
    private String _otherSpecialTherapyMethod = "";

    public String getOtherSpecialTherapyMethod() {
        return _otherSpecialTherapyMethod;
    }

    public void setOtherSpecialTherapyMethod(String otherSpecialTherapyMethod) {
        this._otherSpecialTherapyMethod = otherSpecialTherapyMethod;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  MiscSpecificActivityContent">
    @Documentation(key = "lblMisc", omitOnEmpty = true)
    @Column(name = "miMiscSpecificActivityContent")
    private String _miscSpecificActivityContent = "";

    public String getMiscSpecificActivityContent() {
        return _miscSpecificActivityContent;
    }

    public void setMiscSpecificActivityContent(String miscSpecificActivityContent) {
        this._miscSpecificActivityContent = miscSpecificActivityContent;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  SingleRemuneration">
    @Documentation(key = "lblSingleRefund", omitOnEmpty = true)
    @Column(name = "miSingleRemuneration")
    private String _singleRemuneration = "";

    public String getSingleRemuneration() {
        return _singleRemuneration;
    }

    public void setSingleRemuneration(String singleRemuneration) {
        this._singleRemuneration = singleRemuneration;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  DayPackage">
    @Documentation(key = "lblDayPackage", omitOnEmpty = true)
    @Column(name = "miDayPackage")
    private String _dayPackage = "";

    public String getDayPackage() {
        return _dayPackage;
    }

    public void setDayPackage(String dayPackage) {
        this._dayPackage = dayPackage;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  CasePackage">
    @Documentation(key = "lblCasePackage", omitOnEmpty = true)
    @Column(name = "miCasePackage")
    private String _casePackage = "";

    public String getCasePackage() {
        return _casePackage;
    }

    public void setCasePackage(String casePackage) {
        this._casePackage = casePackage;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  QuarterPackage">
    @Documentation(key = "lblQuarterPackage", omitOnEmpty = true)
    @Column(name = "miQuarterPackage")
    private String _quarterPackage = "";

    public String getQuarterPackage() {
        return _quarterPackage;
    }

    public void setQuarterPackage(String quarterPackage) {
        this._quarterPackage = quarterPackage;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  ComplexPackage">
    @Documentation(key = "lblComplexPackage", omitOnEmpty = true)
    @Column(name = "miComplexPackage")
    private String _complexPackage = "";

    public String getComplexPackage() {
        return _complexPackage;
    }

    public void setComplexPackage(String complexPackage) {
        this._complexPackage = complexPackage;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  PEPPRemuneration">
    @Documentation(key = "lblPeppRemuneration", omitOnEmpty = true)
    @Column(name = "miPEPPRemuneration")
    private String _peppRemuneration = "";

    public String getPeppRemuneration() {
        return _peppRemuneration;
    }

    public void setPeppRemuneration(String peppRemuneration) {
        this._peppRemuneration = peppRemuneration;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  RegionalBudget">
    @Documentation(key = "lblReginalBudget", omitOnEmpty = true)
    @Column(name = "miRegionalBudget")
    private String _regionalBudget = "";

    public String getRegionalBudget() {
        return _regionalBudget;
    }

    public void setRegionalBudget(String regionalBudget) {
        this._regionalBudget = regionalBudget;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property  MiscRemuneration">
    @Documentation(key = "lblMiscRefund", omitOnEmpty = true)
    @Column(name = "miMiscRemuneration")
    private String _miscRemuneration = "";

    public String getMiscRemuneration() {
        return _miscRemuneration;
    }

    public void setMiscRemuneration(String miscRemuneration) {
        this._miscRemuneration = miscRemuneration;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property status">
    @Column(name = "miStatus")
    private int _status = 0;

    @Documentation(name = "Bearbeitungsstatus", rank = 10)
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_status);
    }

    public void setStatus(int value) {
        _status = value;
    }

    public void setStatus(WorkflowStatus status) {
        _status = status.getId();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="list remuneration">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reModelIntentionId", referencedColumnName = "miId")
    @OrderBy("_id")
    @Documentation(key = "headerRemuneration")
    @Valid
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

    // <editor-fold defaultstate="collapsed" desc="list costs">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "coModelIntentionId", referencedColumnName = "miId")
    @OrderBy("_id")
    @Documentation(key = "headerModelIntentionCost")
    @Valid
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

    // <editor-fold defaultstate="collapsed" desc="list adjustments">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "adModelIntentionId", referencedColumnName = "miId")
    @OrderBy("_id")
    @Documentation(key = "headerModelIntentionAdjustment")
    @Valid
    private List<Adjustment> _adjustments;

    public List<Adjustment> getAdjustments() {
        if (_adjustments == null) {
            _adjustments = new ArrayList<>();
        }
        return _adjustments;
    }

    public void setAdjustments(List<Adjustment> adjustments) {
        _adjustments = adjustments;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="list ModelLife">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mlModelIntentionId", referencedColumnName = "miId")
    @OrderBy("_startDate")
    @Documentation(key = "headerModelIntentionLifetime")
    @Valid
    private List<ModelLife> _modelLifes;

    public List<ModelLife> getModelLifes() {
        if (_modelLifes == null) {
            _modelLifes = new ArrayList<>();
        }
        return _modelLifes;
    }

    public void setModelLifes(List<ModelLife> modelLifes) {
        _modelLifes = modelLifes;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ModelIntentionContact">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "csModelIntentionId", referencedColumnName = "miId")
    @OrderBy("_id")
    @Documentation(key = "headerModelIntentionContract")
    @Valid
    private List<ModelIntentionContact> _contacts;

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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AcademicSupervision">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "asModelIntentionId", referencedColumnName = "miId")
    @OrderBy("_id")
    @Documentation(key = "headerModelIntentionScientific")
    @Valid
    private List<AcademicSupervision> _academicSupervisions;

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

    // <editor-fold defaultstate="collapsed" desc="InternalQuality">
    @Column(name = "miInternalQuality")
    //@Documentation(key = "headerModelIntentionInternQuality")
    private int _internalQuality;

    public int getInternalQuality() {
        return _internalQuality;
    }

    public void setInternalQuality(int internalQuality) {
        _internalQuality = internalQuality;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ExternalQuality">
    @Column(name = "miExternalQuality")
    //@Documentation(key = "headerModelIntentionExternQuality")
    private int _externalQuality;

    public int getExternalQuality() {
        return _externalQuality;
    }

    public void setExternalQuality(int externalQuality) {
        _externalQuality = externalQuality;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Quality">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "qyModelIntentionId", referencedColumnName = "miId")
    @OrderBy("_id")
    @Documentation(key = "headerModelIntentionQuality")
    @Valid
    private List<Quality> _qualities;

    public List<Quality> getQualities() {
        if (_qualities == null) {
            _qualities = new ArrayList<>();
        }
        return _qualities;
    }

    public void setQualities(List<Quality> qualitys) {
        _qualities = qualitys;
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
