/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.inek.dataportal.enums.WorkflowStatus;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLBaseInformation", schema = "calc")
@XmlRootElement
public class DrgCalcBasics implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "biID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dataYear">
    @Column(name = "biDataYear")
    private int _dataYear;

    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        this._dataYear = dataYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ik">
    @Column(name = "biIK")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accountID">
    @Column(name = "biAccountID")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="lastChanged">
    @Column(name = "biLastChanged")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastChanged;

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        this._lastChanged = lastChanged;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="statusID">
    @Column(name = "biStatusID")
    private int _statusID;

    public int getStatusID() {
        return _statusID;
    }

    public void setStatusID(int statusID) {
        this._statusID = statusID;
    }

    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusID);
    }

    public void setStatus(WorkflowStatus status) {
        _statusID = status.getValue();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="deliveryType">
    @Column(name = "biDeliveryType")
    private short _deliveryType;

    public short getDeliveryType() {
        return _deliveryType;
    }

    public void setDeliveryType(short deliveryType) {
        this._deliveryType = deliveryType;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="correctionNote">
    @Size(max = 500)
    @Column(name = "biCorrectionNote")
    private String _correctionNote = "";

    public String getCorrectionNote() {
        return _correctionNote;
    }

    public void setCorrectionNote(String correctionNote) {
        this._correctionNote = correctionNote;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="sumCalcCost">
    @Column(name = "biSumCalcCost")
    private double _sumCalcCost;

    public double getSumCalcCost() {
        return _sumCalcCost;
    }

    public void setSumCalcCost(double sumCalcCost) {
        this._sumCalcCost = sumCalcCost;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="caseInStationCnt">
    @Column(name = "biCaseInStationCnt")
    private int _caseInStationCnt;

    public int getCaseInStationCnt() {
        return _caseInStationCnt;
    }

    public void setCaseInStationCnt(int caseInStationCnt) {
        this._caseInStationCnt = caseInStationCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="casePartialStationCnt">
    @Column(name = "biCasePartialStationCnt")
    private int _casePartialStationCnt;

    public int getCasePartialStationCnt() {
        return _casePartialStationCnt;
    }

    public void setCasePartialStationCnt(int casePartialStationCnt) {
        this._casePartialStationCnt = casePartialStationCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="daysPartialStation">
    @Column(name = "biDaysPartialStation")
    private int _daysPartialStation;

    public int getDaysPartialStation() {
        return _daysPartialStation;
    }

    public void setDaysPartialStation(int daysPartialStation) {
        this._daysPartialStation = daysPartialStation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="patientEscort">
    @Column(name = "biPatientEscort")
    private int _patientEscort;

    public int getPatientEscort() {
        return _patientEscort;
    }

    public void setPatientEscort(int patientEscort) {
        this._patientEscort = patientEscort;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="preStation">
    @Column(name = "biPreStation")
    private int _preStation;

    public int getPreStation() {
        return _preStation;
    }

    public void setPreStation(int preStation) {
        this._preStation = preStation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="beds">
    @Column(name = "biBeds")
    private int _beds;

    public int getBeds() {
        return _beds;
    }

    public void setBeds(int beds) {
        this._beds = beds;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="partialCnt">
    @Column(name = "biPartialCnt")
    private int _partialCnt;

    public int getPartialCnt() {
        return _partialCnt;
    }

    public void setPartialCnt(int partialCnt) {
        this._partialCnt = partialCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="locationCnt">
    @Column(name = "biLocationCnt")
    private int _locationCnt;

    public int getLocationCnt() {
        return _locationCnt;
    }

    public void setLocationCnt(int locationCnt) {
        this._locationCnt = locationCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="difLocationSupply">
    @Column(name = "biDifLocationSupply")
    private boolean _difLocationSupply;

    public boolean isDifLocationSupply() {
        return _difLocationSupply;
    }

    public void setDifLocationSupply(boolean difLocationSupply) {
        this._difLocationSupply = difLocationSupply;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="specialUnit">
    @Column(name = "biSpecialUnit")
    private boolean _specialUnit;

    public boolean isSpecialUnit() {
        return _specialUnit;
    }

    public void setSpecialUnit(boolean specialUnit) {
        this._specialUnit = specialUnit;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="centralFocus">
    @Column(name = "biCentralFocus")
    private boolean _centralFocus;

    public boolean isCentralFocus() {
        return _centralFocus;
    }

    public void setCentralFocus(boolean centralFocus) {
        this._centralFocus = centralFocus;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="neonatLvl">
    @Column(name = "biNeonatLvl")
    private int _neonatLvl;

    public int getNeonatLvl() {
        return _neonatLvl;
    }

    public void setNeonatLvl(int neonatLvl) {
        this._neonatLvl = neonatLvl;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gynecology">
    @Column(name = "biGynecology")
    private boolean _gynecology;

    public boolean isGynecology() {
        return _gynecology;
    }

    public void setGynecology(boolean gynecology) {
        this._gynecology = gynecology;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="obstetrical">
    @Column(name = "biObstetrical")
    private boolean _obstetrical;

    public boolean isObstetrical() {
        return _obstetrical;
    }

    public void setObstetrical(boolean obstetrical) {
        this._obstetrical = obstetrical;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="deliveryRoomHours">
    @Column(name = "biDeliveryRoomHours")
    private double _deliveryRoomHours;

    public double getDeliveryRoomHours() {
        return _deliveryRoomHours;
    }

    public void setDeliveryRoomHours(double deliveryRoomHours) {
        this._deliveryRoomHours = deliveryRoomHours;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="deliveryRoomPreBirthHabitationCnt">
    @Column(name = "biDeliveryRoomPreBirthHabitationCnt")
    private int _deliveryRoomPreBirthHabitationCnt;

    public int getDeliveryRoomPreBirthHabitationCnt() {
        return _deliveryRoomPreBirthHabitationCnt;
    }

    public void setDeliveryRoomPreBirthHabitationCnt(int deliveryRoomPreBirthHabitationCnt) {
        this._deliveryRoomPreBirthHabitationCnt = deliveryRoomPreBirthHabitationCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="deliveryRoomInstationBirthCnt">
    @Column(name = "biDeliveryRoomInstationBirthCnt")
    private int _deliveryRoomInstationBirthCnt;

    public int getDeliveryRoomInstationBirthCnt() {
        return _deliveryRoomInstationBirthCnt;
    }

    public void setDeliveryRoomInstationBirthCnt(int deliveryRoomInstationBirthCnt) {
        this._deliveryRoomInstationBirthCnt = deliveryRoomInstationBirthCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="noDeliveryRoomHabitation">
    @Column(name = "biNoDeliveryRoomHabitation")
    private boolean _noDeliveryRoomHabitation;

    public boolean isNoDeliveryRoomHabitation() {
        return _noDeliveryRoomHabitation;
    }

    public void setNoDeliveryRoomHabitation(boolean noDeliveryRoomHabitation) {
        this._noDeliveryRoomHabitation = noDeliveryRoomHabitation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="deliveryRoomOrganizationalStructure">
    @Column(name = "biDeliveryRoomOrganizationalStructure")
    private String _deliveryRoomOrganizationalStructure = "";

    public String getDeliveryRoomOrganizationalStructure() {
        return _deliveryRoomOrganizationalStructure;
    }

    public void setDeliveryRoomOrganizationalStructure(String deliveryRoomOrganizationalStructure) {
        this._deliveryRoomOrganizationalStructure = deliveryRoomOrganizationalStructure;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cardiology">
    @Column(name = "biCardiology")
    private boolean _cardiology;

    public boolean isCardiology() {
        return _cardiology;
    }

    public void setCardiology(boolean cardiology) {
        this._cardiology = cardiology;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cardiologyRoomCnt">
    @Column(name = "biCardiologyRoomCnt")
    private int _cardiologyRoomCnt;

    public int getCardiologyRoomCnt() {
        return _cardiologyRoomCnt;
    }

    public void setCardiologyRoomCnt(int cardiologyRoomCnt) {
        this._cardiologyRoomCnt = cardiologyRoomCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cardiologyCaseCnt">
    @Column(name = "biCardiologyCaseCnt")
    private int _cardiologyCaseCnt;

    public int getCardiologyCaseCnt() {
        return _cardiologyCaseCnt;
    }

    public void setCardiologyCaseCnt(int cardiologyCaseCnt) {
        this._cardiologyCaseCnt = cardiologyCaseCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="endoscopy">
    @Column(name = "biEndoscopy")
    private boolean _endoscopy;

    public boolean isEndoscopy() {
        return _endoscopy;
    }

    public void setEndoscopy(boolean endoscopy) {
        this._endoscopy = endoscopy;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="endoscopyRoomCnt">
    @Column(name = "biEndoscopyRoomCnt")
    private int _endoscopyRoomCnt;

    public int getEndoscopyRoomCnt() {
        return _endoscopyRoomCnt;
    }

    public void setEndoscopyRoomCnt(int endoscopyRoomCnt) {
        this._endoscopyRoomCnt = endoscopyRoomCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="endoscopyCaseCnt">
    @Column(name = "biEndoscopyCaseCnt")
    private int _endoscopyCaseCnt;

    public int getEndoscopyCaseCnt() {
        return _endoscopyCaseCnt;
    }

    public void setEndoscopyCaseCnt(int endoscopyCaseCnt) {
        this._endoscopyCaseCnt = endoscopyCaseCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="minimalValvularIntervention">
    @Column(name = "biMinimalValvularIntervention")
    private boolean _minimalValvularIntervention;

    public boolean isMinimalValvularIntervention() {
        return _minimalValvularIntervention;
    }

    public void setMinimalValvularIntervention(boolean minimalValvularIntervention) {
        this._minimalValvularIntervention = minimalValvularIntervention;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="mviFulfilled">
    @Column(name = "biMviFulfilled")
    private int _mviFulfilled;

    public int getMviFulfilled() {
        return _mviFulfilled;
    }

    public void setMviFulfilled(int mviFulfilled) {
        this._mviFulfilled = mviFulfilled;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="mviGuidelineAspired">
    @Column(name = "biMviGuidelineAspired")
    private boolean _mviGuidelineAspired;

    public boolean isMviGuidelineAspired() {
        return _mviGuidelineAspired;
    }

    public void setMviGuidelineAspired(boolean mviGuidelineAspired) {
        this._mviGuidelineAspired = mviGuidelineAspired;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="normalFreelancing">
    @Column(name = "biNormalFreelancing")
    private boolean _normalFreelancing;

    public boolean isNormalFreelancing() {
        return _normalFreelancing;
    }

    public void setNormalFreelancing(boolean normalFreelancing) {
        this._normalFreelancing = normalFreelancing;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="feeContract">
    @Column(name = "biFeeContract")
    private boolean _feeContract;

    public boolean isFeeContract() {
        return _feeContract;
    }

    public void setFeeContract(boolean feeContract) {
        this._feeContract = feeContract;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="pkmsRecording">
    @Column(name = "biPKMSRecording")
    private int _pkmsRecording;

    public int isPkmsRecording() {
        return _pkmsRecording;
    }

    public void setPkmsRecording(int pkmsRecording) {
        this._pkmsRecording = pkmsRecording;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="pkmsCaseCnt">
    @Column(name = "biPKMSCaseCnt")
    private int _pkmsCaseCnt;

    public int getPkmsCaseCnt() {
        return _pkmsCaseCnt;
    }

    public void setPkmsCaseCnt(int pkmsCaseCnt) {
        this._pkmsCaseCnt = pkmsCaseCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PkmsOther">
    @Column(name = "biPKMSOther")
    private String _pkmsOther = "";

    public String getPkmsOther() {
        return _pkmsOther;
    }

    public void setPkmsOther(String pkmsOther) {
        this._pkmsOther = pkmsOther;
    }    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="approximationMethodMedInfra">
    @Column(name = "biApproximationMethodMedInfra")
    private boolean _approximationMethodMedInfra;

    public boolean isApproximationMethodMedInfra() {
        return _approximationMethodMedInfra;
    }

    public void setApproximationMethodMedInfra(boolean approximationMethodMedInfra) {
        this._approximationMethodMedInfra = approximationMethodMedInfra;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="stepladderMethodMedInfra">
    @Column(name = "biStepladderMethodMedInfra")
    private boolean _stepladderMethodMedInfra;

    public boolean isStepladderMethodMedInfra() {
        return _stepladderMethodMedInfra;
    }

    public void setStepladderMethodMedInfra(boolean stepladderMethodMedInfra) {
        this._stepladderMethodMedInfra = stepladderMethodMedInfra;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="extensionMethodMedInfra">
    @Column(name = "biExtensionMethodMedInfra")
    private boolean _extensionMethodMedInfra;

    public boolean isExtensionMethodMedInfra() {
        return _extensionMethodMedInfra;
    }

    public void setExtensionMethodMedInfra(boolean extensionMethodMedInfra) {
        this._extensionMethodMedInfra = extensionMethodMedInfra;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="otherMethodMedInfra">
    @Column(name = "biOtherMethodMedInfra")
    private String _otherMethodMedInfra = "";

    public String getOtherMethodMedInfra() {
        return _otherMethodMedInfra;
    }

    public void setOtherMethodMedInfra(String otherMethodMedInfra) {
        this._otherMethodMedInfra = otherMethodMedInfra;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="approximationMethodNonMedInfra">
    @Column(name = "biApproximationMethodNonMedInfra")
    private boolean _approximationMethodNonMedInfra;

    public boolean isApproximationMethodNonMedInfra() {
        return _approximationMethodNonMedInfra;
    }

    public void setApproximationMethodNonMedInfra(boolean approximationMethodNonMedInfra) {
        this._approximationMethodNonMedInfra = approximationMethodNonMedInfra;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="stepladderMethodNonMedInfra">
    @Column(name = "biStepladderMethodNonMedInfra")
    private boolean _stepladderMethodNonMedInfra;

    public boolean isStepladderMethodNonMedInfra() {
        return _stepladderMethodNonMedInfra;
    }

    public void setStepladderMethodNonMedInfra(boolean stepladderMethodNonMedInfra) {
        this._stepladderMethodNonMedInfra = stepladderMethodNonMedInfra;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="extensionMethodNonMedInfra">
    @Column(name = "biExtensionMethodNonMedInfra")
    private boolean _extensionMethodNonMedInfra;

    public boolean isExtensionMethodNonMedInfra() {
        return _extensionMethodNonMedInfra;
    }

    public void setExtensionMethodNonMedInfra(boolean extensionMethodNonMedInfra) {
        this._extensionMethodNonMedInfra = extensionMethodNonMedInfra;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="otherMethodNonMedInfra">
    @Column(name = "biOtherMethodNonMedInfra")
    private String _otherMethodNonMedInfra = "";

    public String getOtherMethodNonMedInfra() {
        return _otherMethodNonMedInfra;
    }

    public void setOtherMethodNonMedInfra(String otherMethodNonMedInfra) {
        this._otherMethodNonMedInfra = otherMethodNonMedInfra;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="intensiveBed">
    @Column(name = "biIntensiveBed")
    private boolean _intensiveBed;

    public boolean isIntensiveBed() {
        return _intensiveBed;
    }

    public void setIntensiveBed(boolean intensiveBed) {
        this._intensiveBed = intensiveBed;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="strokeBed">
    @Column(name = "biStrokeBed")
    private boolean _strokeBed;

    public boolean isStrokeBed() {
        return _strokeBed;
    }

    public void setStrokeBed(boolean intensiveStrokeBed) {
        this._strokeBed = intensiveStrokeBed;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="_intensiveHoursWeighted">
    @Column(name = "biIntensiveHoursWeighted")
    private int _intensiveHoursWeighted;

    public int getIntensiveHoursWeighted() {
        return _intensiveHoursWeighted;
    }

    public void setIntensiveHoursWeighted(int intensiveHoursWeighted) {
        this._intensiveHoursWeighted = intensiveHoursWeighted;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="_intensiveHoursNotWeighted">
    @Column(name = "biIntensiveHoursNotWeighted")
    private int _intensiveHoursNotWeighted;

    public int getIntensiveHoursNotWeighted() {
        return _intensiveHoursNotWeighted;
    }

    public void setIntensiveHoursNotWeighted(int intensiveHoursNotWeighted) {
        this._intensiveHoursNotWeighted = intensiveHoursNotWeighted;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="_strokeHoursWeighted">
    @Column(name = "biStrokeHoursWeighted")
    private int _strokeHoursWeighted;

    public int getStrokeHoursWeighted() {
        return _strokeHoursWeighted;
    }

    public void setStrokeHoursWeighted(int strokeHoursWeighted) {
        this._strokeHoursWeighted = strokeHoursWeighted;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="_strokeHoursNotWeighted">
    @Column(name = "biStrokeHoursNotWeighted")
    private int _strokeHoursNotWeighted;
    
    public int getStrokeHoursNotWeighted() {
        return _strokeHoursNotWeighted;
    }

    public void setStrokeHoursNotWeighted(int strokeHoursNotWeighted) {
        this._strokeHoursNotWeighted = strokeHoursNotWeighted;
    }
    //</editor-fold>
    

    // <editor-fold defaultstate="collapsed" desc="Property List DelimitationFacts">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dfBaseInformationId", referencedColumnName = "biId")
    private List<DrgDelimitationFact> _delimitationFacts = new Vector<>();

    public List<DrgDelimitationFact> getDelimitationFacts() {
        return _delimitationFacts;
    }

    public void setDelimitationFacts(List<DrgDelimitationFact> _delimitationFacts) {
        this._delimitationFacts = _delimitationFacts;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property List CostCenters">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ccBaseInformationID", referencedColumnName = "biId")
    private List<KGLListCostCenter> _costCenters = new Vector<>();

    @XmlTransient
    public List<KGLListCostCenter> getCostCenters() {
        return _costCenters;
    }

    public void setCostCenters(List<KGLListCostCenter> costCenter) {
        this._costCenters = costCenter;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List RadiologyLaboratories">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "rlBaseInformationID", referencedColumnName = "biID")
    private List<KGLListRadiologyLaboratory> _radiologyLaboratories = new Vector<>();

    @XmlTransient
    public List<KGLListRadiologyLaboratory> getRadiologyLaboratories() {
        return _radiologyLaboratories;
    }

    public void setRadiologyLaboratories(List<KGLListRadiologyLaboratory> radiologyLaboratory) {
        this._radiologyLaboratories = radiologyLaboratory;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List ObstetricsGynecologies">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ogBaseInformationID", referencedColumnName = "biID")
    private List<KGLListObstetricsGynecology> _obstetricsGynecologies  = new Vector<>();
    
    @XmlTransient
    public List<KGLListObstetricsGynecology> getObstetricsGynecologies() {
        return _obstetricsGynecologies;
    }
    
    public void setObstetricsGynecologies(List<KGLListObstetricsGynecology> obstetricsGynecology) {
        this._obstetricsGynecologies = obstetricsGynecology;
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="Property List RadiologyServices">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "rsBaseInformationID", referencedColumnName = "biID")
    private List<KGLRadiologyService> _radiologyServices = new Vector<>();

    @XmlTransient
    public List<KGLRadiologyService> getRadiologyServices() {
        return _radiologyServices;
    }

    public void setRadiologyServices(List<KGLRadiologyService> radiologyService) {
        this._radiologyServices = radiologyService;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property OpAn">
    @OneToOne
    @PrimaryKeyJoinColumn(name = "biID", referencedColumnName = "oaBaseInformationID")
    private KGLOpAn _opAn;

    public KGLOpAn getOpAn() {
        if (_opAn == null) {
            _opAn = new KGLOpAn(_id);
        }
        return _opAn;
    }

    public void setOpAn(KGLOpAn opAn) {
        this._opAn = opAn;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List ServiceProvisions">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "spBaseInformationId", referencedColumnName = "biId")
    @OrderBy("_sequence")
    private List<KGLListServiceProvision> _serviceProvisions = new Vector<>();
    
    @XmlTransient
    public List<KGLListServiceProvision> getServiceProvisions() {
        return _serviceProvisions;
    }
    
    public void setServiceProvisions(List<KGLListServiceProvision> serviceProvision) {
        this._serviceProvisions = serviceProvision;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List KstTop">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ktBaseInformationID", referencedColumnName = "biId")
    private List<KGLListKstTop> _kstTop = new Vector<>();
    
    @XmlTransient
    public List<KGLListKstTop> getKstTop() {
        return _kstTop;
    }
    
    public void setKstTop(List<KGLListKstTop> kstTop) {
        this._kstTop = kstTop;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List EndoscopyDifferentials">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "edBaseInformationID", referencedColumnName = "biId")
    private List<KGLListEndoscopyDifferential> _endoscopyDifferentials = new Vector<>();
    
    @XmlTransient
    public List<KGLListEndoscopyDifferential> getEndoscopyDifferentials() {
        return _endoscopyDifferentials;
    }
    
    public void setEndoscopyDifferentials(List<KGLListEndoscopyDifferential> endoscopyDifferential) {
        this._endoscopyDifferentials = endoscopyDifferential;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List normalFeeContracts">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nfcBaseInformationID", referencedColumnName = "biID")
    private List<KGLNormalFeeContract> _normalFeeContracts = new Vector<>();
    
    @XmlTransient
    public List<KGLNormalFeeContract> getNormalFeeContracts() {
        return _normalFeeContracts;
    }
    
    public void setNormalFeeContracts(List<KGLNormalFeeContract> normalFeeContracts) {
        this._normalFeeContracts = normalFeeContracts;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List NormalFreelancers">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nfBaseInformationID", referencedColumnName = "biID")
    private List<KGLNormalFreelancer> _normalFreelancers = new Vector<>();
    
    public List<KGLNormalFreelancer> getNormalFreelancers() {
        return _normalFreelancers;
    }
    
    public void setNormalFreelancers(List<KGLNormalFreelancer> normalFreelancers) {
        this._normalFreelancers = normalFreelancers;
    }
    //</editor-fold>
    
    

    //<editor-fold defaultstate="collapsed" desc="Property List IntensivStrokes">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "isBaseInformationID", referencedColumnName = "biID")
    private List<KGLListIntensivStroke> _intensivStrokes = new Vector<>();
    
    public List<KGLListIntensivStroke> getIntensivStrokes() {
        return _intensivStrokes;
    }
    
    public void setIntensivStrokes(List<KGLListIntensivStroke> intensivStrokes) {
        this._intensivStrokes = intensivStrokes;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List medInfras">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "miBaseInformationID", referencedColumnName = "biID")
    private List<KGLListMedInfra> _medInfras = new Vector<>();
    
    public List<KGLListMedInfra> getMedInfras() {
        return _medInfras;
    }
    
    public void setMedInfras(List<KGLListMedInfra> medInfras) {
        this._medInfras = medInfras;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List PersonalAccountings">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "paBaseInformationID", referencedColumnName = "biID")
    private List<KGLPersonalAccounting> _personalAccountings = new Vector<>();
    
    public List<KGLPersonalAccounting> getPersonalAccountings() {
        return _personalAccountings;
    }
    
    public void setPersonalAccountings(List<KGLPersonalAccounting> personalAccountings) {
        this._personalAccountings = personalAccountings;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List NeonateDatas">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ndBaseInformationID", referencedColumnName = "biID")
    private List<DrgNeonatData> _neonateData = new Vector<>();

    public List<DrgNeonatData> getNeonateData() {
        return _neonateData;
    }

    public void setNeonateData(List<DrgNeonatData> neonateData) {
        this._neonateData = neonateData;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List NormalStationServiceDocumentations">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nssBaseInformationID", referencedColumnName = "biID")
    private List<KGLNormalStationServiceDocumentation> _normalStationServiceDocumentations = new Vector<>();
    
    public List<KGLNormalStationServiceDocumentation> getNormalStationServiceDocumentations() {
        return _normalStationServiceDocumentations;
    }
    
    public void setNormalStationServiceDocumentations(List<KGLNormalStationServiceDocumentation> normalStationServiceDocumentations) {
        this._normalStationServiceDocumentations = normalStationServiceDocumentations;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List _costCenterCosts">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cccBaseInformationID", referencedColumnName = "biID")
    private List<KGLListCostCenterCost> _costCenterCosts = new Vector<>();
    
    public List<KGLListCostCenterCost> getCostCenterCosts() {
        return _costCenterCosts;
    }
    
    public void setCostCenterCosts(List<KGLListCostCenterCost> cCostCenterCosts) {
        this._costCenterCosts = cCostCenterCosts;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List pkmsAlternatives">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "paBaseInformationID", referencedColumnName = "biID")
    private List<KGLPKMSAlternative> _pkmsAlternatives = new Vector<>();
    
    public List<KGLPKMSAlternative> getPkmsAlternatives() {
        return _pkmsAlternatives;
    }
    
    public void setPkmsAlternatives(List<KGLPKMSAlternative> pkmsAlternatives) {
        this._pkmsAlternatives = pkmsAlternatives;
    }
    //</editor-fold>
                
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DrgCalcBasics)) {
            return false;
        }
        DrgCalcBasics other = (DrgCalcBasics) object;
        return (this._id == other._id);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.DrgCalcBasics[ biID=" + _id + " ]";
    }

}
