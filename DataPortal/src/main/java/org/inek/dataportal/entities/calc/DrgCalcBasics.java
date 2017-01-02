/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import org.inek.dataportal.enums.WorkflowStatus;

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "KGLBaseInformation", schema = "calc")
public class DrgCalcBasics implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "biDeliveryType")
    private short _deliveryType;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "biSumCalcCost")
    private double _sumCalcCost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biDeliveryRoomHours")
    private double _deliveryRoomHours;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biNormalFreelancing")
    private boolean _normalFreelancing;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biFeeContract")
    private boolean _feeContract;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biPKMSRecording")
    private boolean _pkmsRecording;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biPKMSCaseCnt")
    private int _pkmsCaseCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biKISIntegration")
    private boolean _kisIntegration;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "biNormalStationOther")
    private String _normalStationOther;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biApproximationMethodMedInfra")
    private boolean _approximationMethodMedInfra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biStepladderMethodMedInfra")
    private boolean _stepladderMethodMedInfra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biExtensionMethodMedInfra")
    private boolean _extensionMethodMedInfra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biOtherMethodMedInfra")
    private boolean _otherMethodMedInfra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biApproximationMethodNonMedInfra")
    private boolean _approximationMethodNonMedInfra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biStepladderMethodNonMedInfra")
    private boolean _stepladderMethodNonMedInfra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biExtensionMethodNonMedInfra")
    private boolean _extensionMethodNonMedInfra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biOtherMethodNonMedInfra")
    private boolean _otherMethodNonMedInfra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biIntensiveBed")
    private boolean _intensiveBed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "biIntensiveStrokeBed")
    private boolean _intensiveStrokeBed;
    
    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "biID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property DataYear">
    @Column(name = "biDataYear")
    private int _dataYear;
    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        this._dataYear = dataYear;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property IK">
    @Column(name = "biIK")
    private int _ik;
    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Hospital Name">
    @Column(name = "biHospitalName")
    private String _hospitalName = "";

    public String getHospitalName() {
        return _hospitalName;
    }

    public void setHospitalName(String _hospitalName) {
        this._hospitalName = _hospitalName;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "biAccountId")
    private int _accountId;
    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property StatusId / Status">
    @Column(name = "biStatusId")
    private int _statusId;
    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        _statusId = statusId;
    }
    
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    public void setStatus(WorkflowStatus status) {
        _statusId = status.getValue();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "biLastChanged")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastChanged =  Calendar.getInstance().getTime();
    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        _lastChanged = lastChanged;
    }
    // </editor-fold>

    public short getDeliveryType() {
        return _deliveryType;
    }

    public void setDeliveryType(short _deliveryType) {
        this._deliveryType = _deliveryType;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Correction Note">
    @Column(name = "biCorrectionNote")
    private String _correctionNote = "";

    public String getCorrectionNote() {
        return _correctionNote;
    }

    public void setCorrectionNote(String _correctionNote) {
        this._correctionNote = _correctionNote;
    }
    // </editor-fold>

    public double getSumCalcCost() {
        return _sumCalcCost;
    }

    public void setSumCalcCost(double _sumCalcCost) {
        this._sumCalcCost = _sumCalcCost;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CaseInStationCount">
    @Column(name = "biCaseInStationCnt")
    private int _caseInStationCount;

    public int getCaseInStationCount() {
        return _caseInStationCount;
    }

    public void setCaseInStationCount(int caseInStationCount) {
        this._caseInStationCount = caseInStationCount;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CasePartialStationCount">
    @Column(name = "biCasePartialStationCnt")
    private int _casePartialStationCount;

    public int getCasePartialStationCount() {
        return _casePartialStationCount;
    }

    public void setCasePartialStationCount(int casePartialStationCount) {
        this._casePartialStationCount = casePartialStationCount;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property DaysPartialStation">
    @Column(name = "biDaysPartialStation")
    private int _daysPartialStation;

    public int getDaysPartialStation() {
        return _daysPartialStation;
    }

    public void setDaysPartialStation(int _daysPartialStation) {
        this._daysPartialStation = _daysPartialStation;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property PatientEscort">
    @Column(name = "biPatientEscort")
    private int _patientEscort;

    public int getPatientEscort() {
        return _patientEscort;
    }

    public void setPatientEscort(int _patientEscort) {
        this._patientEscort = _patientEscort;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property PreStation">
    @Column(name = "biPreStation")
    private int _preStation = -1;
    
    public int getPreStation() {
        return _preStation;
    }

    public void setPreStation(int _preStation) {
        this._preStation = _preStation;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Beds">
    @Column(name = "biBeds")
    private int _beds = -1;
    
    public int getBeds() {
        return _beds;
    }

    public void setBeds(int _beds) {
        this._beds = _beds;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CntPartial">
    @Column(name = "biPartialCnt")
    private int _cntPartial = -1;
    
    public int getCntPartial() {
        return _cntPartial;
    }

    public void setCntPartial(int _cntPartial) {
        this._cntPartial = _cntPartial;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CntLocation">
    @Column(name = "biLocationCnt")
    private int _cntLocation = -1;
    
    public int getCntLocation() {
        return _cntLocation;
    }

    public void setCntLocation(int _cntLocation) {
        this._cntLocation = _cntLocation;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DifLocationSupply">
    @Column(name = "biDifLocationSupply")
    private boolean _difLocationSupply;
    
    public boolean getDifLocationSupply() {
        return _difLocationSupply;
    }

    public void setDifLocationSupply(boolean _difLocationSupply) {
        this._difLocationSupply = _difLocationSupply;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property SpecialUnit">
    @Column(name = "biSpecialUnit")
    private boolean _specialUnit;
    
    public boolean getSpecialUnit() {
        return _specialUnit;
    }

    public void setSpecialUnit(boolean _specialUnit) {
        this._specialUnit = _specialUnit;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CentralFocus">
    @Column(name = "biCentralFocus")
    private boolean _centralFocus;
    
    public boolean getCentralFocus() {
        return _centralFocus;
    }

    public void setCentralFocus(boolean _centralFocus) {
        this._centralFocus = _centralFocus;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property gynecology">
    @Column(name = "biGynecology")
    private boolean _gynecology;

    public boolean isGynecology() {
        return _gynecology;
    }

    public void setGynecology(boolean _gynecology) {
        this._gynecology = _gynecology;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property obstetrical">
    @Column(name = "biObstetrical")
    private boolean _obstetrical;

    public boolean isObstetrical() {
        return _obstetrical;
    }

    public void setObstetrical(boolean _obstetrical) {
        this._obstetrical = _obstetrical;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property NoDeliveryRoomHabitation">
    @Column(name = "biNoDeliveryRoomHabitation")
    private boolean _noDeliveryRoomHabitation;

    public boolean isNoDeliveryRoomHabitation() {
        return _noDeliveryRoomHabitation;
    }

    public void setNoDeliveryRoomHabitation(boolean _noDeliveryRoomHabitation) {
        this._noDeliveryRoomHabitation = _noDeliveryRoomHabitation;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Cardiology">
    @Column(name = "biCardiology")
    private boolean _cardiology;

    public boolean isCardiology() {
        return _cardiology;
    }

    public void setCardiology(boolean _cardiology) {
        this._cardiology = _cardiology;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Mhi">
    @Column(name = "biMinimalValvularIntervention")
    private boolean _minimalValvularIntervention;

    public boolean isMinimalValvularIntervention() {
        return _minimalValvularIntervention;
    }

    public void setMinimalValvularIntervention(boolean minimalValvularIntervention) {
        this._minimalValvularIntervention = minimalValvularIntervention;
    }
    
    // </editor-fold>    
    
    // <editor-fold defaultstate="collapsed" desc="Property MviFulfilled">
    @Column(name = "biMviFulfilled")
    private int _mviFulfilled = -1;

    public int getMviFulfilled() {
        return _mviFulfilled;
    }

    public void setMviFulfilled(int mviFulfilled) {
        this._mviFulfilled = mviFulfilled;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property MviGuidelineAspired">
    @Column(name = "biMviGuidelineAspired")
    boolean _mviGuidelineAspired;

    public boolean isMviGuidelineAspired() {
        return _mviGuidelineAspired;
    }

    public void setMviGuidelineAspired(boolean mviGuidelineAspired) {
        this._mviGuidelineAspired = mviGuidelineAspired;
    }
    // </editor-fold>
            
    // <editor-fold defaultstate="collapsed" desc="Property Endoscopy">
    @Column(name = "biEndoscopy")
    boolean _endoscopy; 

    public boolean isEndoscopy() {
        return _endoscopy;
    }

    public void setEndoscopy(boolean _endoscopy) {
        this._endoscopy = _endoscopy;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property DeliveryRoomPreBirthHabitationCnt">
    @Column(name = "biDeliveryRoomPreBirthHabitationCnt")
    int _deliveryRoomPreBirthHabitationCnt;

    public int getDeliveryRoomPreBirthHabitationCnt() {
        return _deliveryRoomPreBirthHabitationCnt;
    }

    public void setDeliveryRoomPreBirthHabitationCnt(int _deliveryRoomPreBirthHabitationCnt) {
        this._deliveryRoomPreBirthHabitationCnt = _deliveryRoomPreBirthHabitationCnt;
    }
    
    // </editor-fold>
            
    // <editor-fold defaultstate="collapsed" desc="Property DeliveryRoomInstationBirthCnt">
    @Column(name = "biDeliveryRoomInstationBirthCnt")
    int _deliveryRoomInstationBirthCnt;

    public int getDeliveryRoomInstationBirthCnt() {
        return _deliveryRoomInstationBirthCnt;
    }

    public void setDeliveryRoomInstationBirthCnt(int _deliveryRoomInstationBirthCnt) {
        this._deliveryRoomInstationBirthCnt = _deliveryRoomInstationBirthCnt;
    }
            
    // </editor-fold>
            
    // <editor-fold defaultstate="collapsed" desc="Property CardiologyRoomCnt">
    @Column(name = "biCardiologyRoomCnt")
    int _cardiologyRoomCnt;

    public int getCardiologyRoomCnt() {
        return _cardiologyRoomCnt;
    }

    public void setCardiologyRoomCnt(int _cardiologyRoomCnt) {
        this._cardiologyRoomCnt = _cardiologyRoomCnt;
    }
    
    // </editor-fold>
            
    // <editor-fold defaultstate="collapsed" desc="Property CardiologyCaseCnt">
    @Column(name = "biCardiologyCaseCnt")
    int _cardiologyCaseCnt;

    public int getCardiologyCaseCnt() {
        return _cardiologyCaseCnt;
    }

    public void setCardiologyCaseCnt(int _cardiologyCaseCnt) {
        this._cardiologyCaseCnt = _cardiologyCaseCnt;
    }
    
    // </editor-fold>
            
    // <editor-fold defaultstate="collapsed" desc="Property EndoscopyRoomCnt">
    @Column(name = "biEndoscopyRoomCnt")
    int _endoscopyRoomCnt;

    public int getEndoscopyRoomCnt() {
        return _endoscopyRoomCnt;
    }

    public void setEndoscopyRoomCnt(int _endoscopyRoomCnt) {
        this._endoscopyRoomCnt = _endoscopyRoomCnt;
    }
    
    // </editor-fold>
            
    // <editor-fold defaultstate="collapsed" desc="Property EndoscopyCaseCnt">
    @Column(name = "biEndoscopyCaseCnt")
    int _endoscopyCaseCnt;

    public int getEndoscopyCaseCnt() {
        return _endoscopyCaseCnt;
    }

    public void setEndoscopyCaseCnt(int _endoscopyCaseCnt) {
        this._endoscopyCaseCnt = _endoscopyCaseCnt;
    }
    // </editor-fold>            

    public double getDeliveryRoomHours() {
        return _deliveryRoomHours;
    }

    public void setDeliveryRoomHours(double _deliveryRoomHours) {
        this._deliveryRoomHours = _deliveryRoomHours;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DeliveryRoomOrganizationalStructure">
    @Column(name = "biDeliveryRoomOrganizationalStructure")
    private String _deliveryRoomOrganizationalStructure = "";

    public String getDeliveryRoomOrganizationalStructure() {
        return _deliveryRoomOrganizationalStructure;
    }

    public void setDeliveryRoomOrganizationalStructure(String deliveryRoomOrganizationalStructure) {
        this._deliveryRoomOrganizationalStructure = deliveryRoomOrganizationalStructure;
    }
    // </editor-fold>


    
    // <editor-fold defaultstate="collapsed" desc="Property List DelimitationFacts">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dfBaseInformationId", referencedColumnName = "biId")
    private List<DrgDelimitationFact> _delimitationFacts = new Vector<>();

    public List<DrgDelimitationFact> getDelimitationFacts() {
        return _delimitationFacts;
    }

    public void setDelimitationFacts(List<DrgDelimitationFact> _delimitationFacts) {
        this._delimitationFacts = _delimitationFacts;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property NeonatLvl">
    @Column(name = "biNeonatLvl")
    private int _neonatLvl;
    
    public int getNeonatLvl() {
        return _neonatLvl;
    }

    public void setNeonatLvl(int _neonatLvl) {
        this._neonatLvl = _neonatLvl;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property List DrgNeonatData">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ndBaseInformationID", referencedColumnName = "biId")
    private List<DrgNeonatData> neonateData;

    public List<DrgNeonatData> getNeonateData() {
        return neonateData;
    }

    public void setNeonateData(List<DrgNeonatData> neonateData) {
        this.neonateData = neonateData;
    }
    // </editor-fold>


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "_baseInformation")
    private List<KGLListCostCenter> kGLListCostCenterList;
    @XmlTransient
    public List<KGLListCostCenter> getKGLListCostCenterList() {
        return kGLListCostCenterList;
    }

    public void setKGLListCostCenterList(List<KGLListCostCenter> kGLListCostCenterList) {
        this.kGLListCostCenterList = kGLListCostCenterList;
    }
    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rlBaseInformationID")
    private List<KGLListRadiologyLaboratory> kGLListRadiologyLaboratoryList;

    @XmlTransient
    public List<KGLListRadiologyLaboratory> getKGLListRadiologyLaboratoryList() {
        return kGLListRadiologyLaboratoryList;
    }

    public void setKGLListRadiologyLaboratoryList(List<KGLListRadiologyLaboratory> kGLListRadiologyLaboratoryList) {
        this.kGLListRadiologyLaboratoryList = kGLListRadiologyLaboratoryList;
    }
    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ogBaseInformationID")
    private List<KGLListObstetricsGynecology> kGLListObstetricsGynecologyList;

    @XmlTransient
    public List<KGLListObstetricsGynecology> getKGLListObstetricsGynecologyList() {
        return kGLListObstetricsGynecologyList;
    }

    public void setKGLListObstetricsGynecologyList(List<KGLListObstetricsGynecology> kGLListObstetricsGynecologyList) {
        this.kGLListObstetricsGynecologyList = kGLListObstetricsGynecologyList;
    }
    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rsBaseInformationID")
    private List<KGLRadiologyService> kGLRadiologyServiceList;

    @XmlTransient
    public List<KGLRadiologyService> getKGLRadiologyServiceList() {
        return kGLRadiologyServiceList;
    }

    public void setKGLRadiologyServiceList(List<KGLRadiologyService> kGLRadiologyServiceList) {
        this.kGLRadiologyServiceList = kGLRadiologyServiceList;
    }
    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "oaBaseInformationID")
    private List<KGLOpAn> kGLOpAnList;

    @XmlTransient
    public List<KGLOpAn> getKGLOpAnList() {
        return kGLOpAnList;
    }

    public void setKGLOpAnList(List<KGLOpAn> kGLOpAnList) {
        this.kGLOpAnList = kGLOpAnList;
    }
    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "_baseInformation")
    private List<KGLListServiceProvision> kGLListServiceProvisionList;

    @XmlTransient
    public List<KGLListServiceProvision> getKGLListServiceProvisionList() {
        return kGLListServiceProvisionList;
    }

    public void setKGLListServiceProvisionList(List<KGLListServiceProvision> kGLListServiceProvisionList) {
        this.kGLListServiceProvisionList = kGLListServiceProvisionList;
    }
    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ktBaseInformationID")
    private List<KGLListKstTop> kGLListKstTopList;

    @XmlTransient
    public List<KGLListKstTop> getKGLListKstTopList() {
        return kGLListKstTopList;
    }

    public void setKGLListKstTopList(List<KGLListKstTop> kGLListKstTopList) {
        this.kGLListKstTopList = kGLListKstTopList;
    }
    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "_baseInformation")
    private List<KGLListEndoscopyDifferential> kGLListEndoscopyDifferentialList;
    @XmlTransient
    public List<KGLListEndoscopyDifferential> getKGLListEndoscopyDifferentialList() {
        return kGLListEndoscopyDifferentialList;
    }

    public void setKGLListEndoscopyDifferentialList(List<KGLListEndoscopyDifferential> kGLListEndoscopyDifferentialList) {
        this.kGLListEndoscopyDifferentialList = kGLListEndoscopyDifferentialList;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this._id;
        hash = 89 * hash + this._dataYear;
        hash = 89 * hash + this._ik;
        return hash;
    }

    // <editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DrgCalcBasics other = (DrgCalcBasics) obj;
        if (this._id != other._id) {
            return false;
        }
        if (this._dataYear != other._dataYear) {
            return false;
        }
        if (this._ik != other._ik) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DrgCalcBasics{" + "_id=" + _id + '}';
    }
    
   
    
    // </editor-fold>
    
    @PrePersist
    @PreUpdate
    public void tagModifiedDate() {
        _lastChanged = Calendar.getInstance().getTime();
    }

    public DrgCalcBasics() {
    }

    public short getBiDeliveryType() {
        return _deliveryType;
    }

    public void setBiDeliveryType(short biDeliveryType) {
        this._deliveryType = biDeliveryType;
    }

    public double getBiSumCalcCost() {
        return _sumCalcCost;
    }

    public void setBiSumCalcCost(double biSumCalcCost) {
        this._sumCalcCost = biSumCalcCost;
    }

    public double getBiDeliveryRoomHours() {
        return _deliveryRoomHours;
    }

    public void setBiDeliveryRoomHours(double biDeliveryRoomHours) {
        this._deliveryRoomHours = biDeliveryRoomHours;
    }

    public boolean getBiNormalFreelancing() {
        return _normalFreelancing;
    }

    public void setBiNormalFreelancing(boolean biNormalFreelancing) {
        this._normalFreelancing = biNormalFreelancing;
    }

    public boolean getBiFeeContract() {
        return _feeContract;
    }

    public void setBiFeeContract(boolean biFeeContract) {
        this._feeContract = biFeeContract;
    }

    public boolean getBiPKMSRecording() {
        return _pkmsRecording;
    }

    public void setBiPKMSRecording(boolean biPKMSRecording) {
        this._pkmsRecording = biPKMSRecording;
    }

    public int getBiPKMSCaseCnt() {
        return _pkmsCaseCnt;
    }

    public void setBiPKMSCaseCnt(int biPKMSCaseCnt) {
        this._pkmsCaseCnt = biPKMSCaseCnt;
    }

    public boolean getBiKISIntegration() {
        return _kisIntegration;
    }

    public void setBiKISIntegration(boolean biKISIntegration) {
        this._kisIntegration = biKISIntegration;
    }

    public String getBiNormalStationOther() {
        return _normalStationOther;
    }

    public void setBiNormalStationOther(String biNormalStationOther) {
        this._normalStationOther = biNormalStationOther;
    }

    public boolean getBiApproximationMethodMedInfra() {
        return _approximationMethodMedInfra;
    }

    public void setBiApproximationMethodMedInfra(boolean biApproximationMethodMedInfra) {
        this._approximationMethodMedInfra = biApproximationMethodMedInfra;
    }

    public boolean getBiStepladderMethodMedInfra() {
        return _stepladderMethodMedInfra;
    }

    public void setBiStepladderMethodMedInfra(boolean biStepladderMethodMedInfra) {
        this._stepladderMethodMedInfra = biStepladderMethodMedInfra;
    }

    public boolean getBiExtensionMethodMedInfra() {
        return _extensionMethodMedInfra;
    }

    public void setBiExtensionMethodMedInfra(boolean biExtensionMethodMedInfra) {
        this._extensionMethodMedInfra = biExtensionMethodMedInfra;
    }

    public boolean getBiOtherMethodMedInfra() {
        return _otherMethodMedInfra;
    }

    public void setBiOtherMethodMedInfra(boolean biOtherMethodMedInfra) {
        this._otherMethodMedInfra = biOtherMethodMedInfra;
    }

    public boolean getBiApproximationMethodNonMedInfra() {
        return _approximationMethodNonMedInfra;
    }

    public void setBiApproximationMethodNonMedInfra(boolean biApproximationMethodNonMedInfra) {
        this._approximationMethodNonMedInfra = biApproximationMethodNonMedInfra;
    }

    public boolean getBiStepladderMethodNonMedInfra() {
        return _stepladderMethodNonMedInfra;
    }

    public void setBiStepladderMethodNonMedInfra(boolean biStepladderMethodNonMedInfra) {
        this._stepladderMethodNonMedInfra = biStepladderMethodNonMedInfra;
    }

    public boolean getBiExtensionMethodNonMedInfra() {
        return _extensionMethodNonMedInfra;
    }

    public void setBiExtensionMethodNonMedInfra(boolean biExtensionMethodNonMedInfra) {
        this._extensionMethodNonMedInfra = biExtensionMethodNonMedInfra;
    }

    public boolean getBiOtherMethodNonMedInfra() {
        return _otherMethodNonMedInfra;
    }

    public void setBiOtherMethodNonMedInfra(boolean biOtherMethodNonMedInfra) {
        this._otherMethodNonMedInfra = biOtherMethodNonMedInfra;
    }

    public boolean getBiIntensiveBed() {
        return _intensiveBed;
    }

    public void setBiIntensiveBed(boolean biIntensiveBed) {
        this._intensiveBed = biIntensiveBed;
    }

    public boolean getBiIntensiveStrokeBed() {
        return _intensiveStrokeBed;
    }

    public void setBiIntensiveStrokeBed(boolean biIntensiveStrokeBed) {
        this._intensiveStrokeBed = biIntensiveStrokeBed;
    }

}
