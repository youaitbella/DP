/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
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
import javax.xml.bind.annotation.XmlTransient;
import org.inek.dataportal.enums.WorkflowStatus;

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "KGLBaseInformation", schema = "calc")
public class DrgCalcBasics implements Serializable {
    
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
    
    // <editor-fold defaultstate="collapsed" desc="Delivery Type">
    @Column(name = "biDeliveryType")
    private byte _deliveryType;

    public byte getDeliveryType() {
        return _deliveryType;
    }

    public void setDeliveryType(byte _deliveryType) {
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
    
    // <editor-fold defaultstate="collapsed" desc="Property SumCalcCost">
    @Column(name = "biSumCalcCost")
    private float _sumCalcCost;

    public float getSumCalcCost() {
        return _sumCalcCost;
    }

    public void setSumCalcCost(float _sumCalcCost) {
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
    @Column(name = "biMhi")
    private boolean _minimalValvularIntervention;

    public boolean isMinimalValvularIntervention() {
        return _minimalValvularIntervention;
    }

    public void setMinimalValvularIntervention(boolean minimalValvularIntervention) {
        this._minimalValvularIntervention = minimalValvularIntervention;
    }
    
    // </editor-fold>    
    
    // <editor-fold defaultstate="collapsed" desc="Property MhiAbsolute">
    @Column(name = "biMhiAbsolute")
    private boolean _mviAbsolute;

    public boolean isMviAbsolute() {
        return _mviAbsolute;
    }

    public void setMviAbsolute(boolean mviAbsolute) {
        this._mviAbsolute = mviAbsolute;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property MhiTransitionalArrangement">
    @Column(name = "biMhiTransitionalArrangement")
    private boolean _mviTransitionalArrangement;

    public boolean isMviTransitionalArrangement() {
        return _mviTransitionalArrangement;
    }

    public void setMviTransitionalArrangement(boolean mviTransitionalArrangement) {
        this._mviTransitionalArrangement = mviTransitionalArrangement;
    }    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property MhiTransitionalArrangement">
    @Column(name = "biMhiGuideline")
    private boolean _mviGuideline;

    public boolean isMviGuideline() {
        return _mviGuideline;
    }

    public void setMviGuideline(boolean mviGuideline) {
        this._mviGuideline = mviGuideline;
    }    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property MhiGuidelineAspired">
    @Column(name = "biMhiGuidelineAspired")
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

    // <editor-fold defaultstate="collapsed" desc="Property DeliveryRoomHours">
    @Column(name = "biDeliveryRoomHours")
    double _deliveryRoomHours;

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


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ccBaseInformationID")
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
    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spBaseInformationID")
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
    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "edBaseInformationID")
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

}
