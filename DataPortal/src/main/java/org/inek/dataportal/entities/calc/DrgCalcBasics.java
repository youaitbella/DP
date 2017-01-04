/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
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
@NamedQueries({
    @NamedQuery(name = "KGLBaseInformation.findAll", query = "SELECT k FROM DrgCalcBasics k")})
public class DrgCalcBasics implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "biID")
    private Integer _id;
    
    public Integer getId() {
        return _id;
    }
    
    public void setId(Integer id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="dataYear">
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "biIK")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="hospitalName">
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "biHospitalName")
    private String _hospitalName;

    public String getHospitalName() {
        return _hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this._hospitalName = hospitalName;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="accountID">
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "biStatusID")
    private int _statusId;

    public int getStatusID() {
        return _statusId;
    }

    public void setStatusID(int statusID) {
        this._statusId = statusID;
    }
    
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    public void setStatus(WorkflowStatus status) {
        _statusId = status.getValue();
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="deliveryType">
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "biCorrectionNote")
    private String _correctionNote;

    public String getCorrectionNote() {
        return _correctionNote;
    }

    public void setCorrectionNote(String correctionNote) {
        this._correctionNote = correctionNote;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="sumCalcCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "biSumCalcCost")
    private BigDecimal _sumCalcCost;

    public BigDecimal getSumCalcCost() {
        return _sumCalcCost;
    }

    public void setSumCalcCost(BigDecimal sumCalcCost) {
        this._sumCalcCost = sumCalcCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="caseInStationCnt">
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "biDeliveryRoomHours")
    private BigDecimal _deliveryRoomHours;

    public BigDecimal getDeliveryRoomHours() {
        return _deliveryRoomHours;
    }

    public void setDeliveryRoomHours(BigDecimal deliveryRoomHours) {
        this._deliveryRoomHours = deliveryRoomHours;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="deliveryRoomPreBirthHabitationCnt">
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "biDeliveryRoomOrganizationalStructure")
    private String _deliveryRoomOrganizationalStructure;

    public String getDeliveryRoomOrganizationalStructure() {
        return _deliveryRoomOrganizationalStructure;
    }

    public void setDeliveryRoomOrganizationalStructure(String deliveryRoomOrganizationalStructure) {
        this._deliveryRoomOrganizationalStructure = deliveryRoomOrganizationalStructure;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="cardiology">
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "biPKMSRecording")
    private boolean _pkmsRecording;

    public boolean isPkmsRecording() {
        return _pkmsRecording;
    }

    public void setPkmsRecording(boolean pkmsRecording) {
        this._pkmsRecording = pkmsRecording;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="pkmsCaseCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "biPKMSCaseCnt")
    private int _pkmsCaseCnt;

    public int getPkmsCaseCnt() {
        return _pkmsCaseCnt;
    }

    public void setPkmsCaseCnt(int pkmsCaseCnt) {
        this._pkmsCaseCnt = pkmsCaseCnt;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="kisIntegration">
    @Basic(optional = false)
    @NotNull
    @Column(name = "biKISIntegration")
    private boolean _kisIntegration;

    public boolean isKisIntegration() {
        return _kisIntegration;
    }

    public void setKisIntegration(boolean kisIntegration) {
        this._kisIntegration = kisIntegration;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="normalStationOther">
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "biNormalStationOther")
    private String _normalStationOther;
    
    public String getNormalStationOther() {
        return _normalStationOther;
    }

    public void setNormalStationOther(String normalStationOther) {
        this._normalStationOther = normalStationOther;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="approximationMethodMedInfra">
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "biOtherMethodMedInfra")
    private boolean _otherMethodMedInfra;

    public boolean isOtherMethodMedInfra() {
        return _otherMethodMedInfra;
    }

    public void setOtherMethodMedInfra(boolean otherMethodMedInfra) {
        this._otherMethodMedInfra = otherMethodMedInfra;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="approximationMethodNonMedInfra">
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "biOtherMethodNonMedInfra")
    private boolean _otherMethodNonMedInfra;

    public boolean isOtherMethodNonMedInfra() {
        return _otherMethodNonMedInfra;
    }

    public void setOtherMethodNonMedInfra(boolean otherMethodNonMedInfra) {
        this._otherMethodNonMedInfra = otherMethodNonMedInfra;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="intensiveBed">
    @Basic(optional = false)
    @NotNull
    @Column(name = "biIntensiveBed")
    private boolean _intensiveBed;

    public boolean isIntensiveBed() {
        return _intensiveBed;
    }

    public void setIntensiveBed(boolean intensiveBed) {
        this._intensiveBed = intensiveBed;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="intensiveStrokeBed">
    @Basic(optional = false)
    @NotNull
    @Column(name = "biIntensiveStrokeBed")
    private boolean _intensiveStrokeBed;

    public boolean isIntensiveStrokeBed() {
        return _intensiveStrokeBed;
    }

    public void setIntensiveStrokeBed(boolean intensiveStrokeBed) {
        this._intensiveStrokeBed = intensiveStrokeBed;
    }
    //</editor-fold>
    
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

    //<editor-fold defaultstate="collapsed" desc="kglOpAn">
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "_drgCalcBasics")
    private KGLOpAn _kglOpAn;

    public KGLOpAn getKglOpAn() {
        return _kglOpAn;
    }

    public void setKglOpAn(KGLOpAn kglOpAn) {
        this._kglOpAn = kglOpAn;
    }
    //</editor-fold>
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ccBaseInformationID", referencedColumnName = "biId")
    private List<KGLListCostCenter> kGLListCostCenterList;
    @XmlTransient
    public List<KGLListCostCenter> getKGLListCostCenterList() {
        return kGLListCostCenterList;
    }

    public void setKGLListCostCenterList(List<KGLListCostCenter> kGLListCostCenterList) {
        this.kGLListCostCenterList = kGLListCostCenterList;
    }
    

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "rlBaseInformationID")//, referencedColumnName = "rlID")
    private List<KGLListRadiologyLaboratory> kGLListRadiologyLaboratoryList;

    @XmlTransient
    public List<KGLListRadiologyLaboratory> getKGLListRadiologyLaboratoryList() {
        return kGLListRadiologyLaboratoryList;
    }

    public void setKGLListRadiologyLaboratoryList(List<KGLListRadiologyLaboratory> kGLListRadiologyLaboratoryList) {
        this.kGLListRadiologyLaboratoryList = kGLListRadiologyLaboratoryList;
    }
    

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ogBaseInformationID")//, referencedColumnName = "ogID")
    private List<KGLListObstetricsGynecology> kGLListObstetricsGynecologyList;

    @XmlTransient
    public List<KGLListObstetricsGynecology> getKGLListObstetricsGynecologyList() {
        return kGLListObstetricsGynecologyList;
    }

    public void setKGLListObstetricsGynecologyList(List<KGLListObstetricsGynecology> kGLListObstetricsGynecologyList) {
        this.kGLListObstetricsGynecologyList = kGLListObstetricsGynecologyList;
    }
    

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "rsBaseInformationID")//, referencedColumnName = "rsID")
    private List<KGLRadiologyService> kGLRadiologyServiceList;

    @XmlTransient
    public List<KGLRadiologyService> getKGLRadiologyServiceList() {
        return kGLRadiologyServiceList;
    }

    public void setKGLRadiologyServiceList(List<KGLRadiologyService> kGLRadiologyServiceList) {
        this.kGLRadiologyServiceList = kGLRadiologyServiceList;
    }
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "spBaseInformationID")//, referencedColumnName = "spID")
    private List<KGLListServiceProvision> kGLListServiceProvisionList;

    @XmlTransient
    public List<KGLListServiceProvision> getKGLListServiceProvisionList() {
        return kGLListServiceProvisionList;
    }

    public void setKGLListServiceProvisionList(List<KGLListServiceProvision> kGLListServiceProvisionList) {
        this.kGLListServiceProvisionList = kGLListServiceProvisionList;
    }
    

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ktBaseInformationID")//, referencedColumnName = "ktID")
    private List<KGLListKstTop> kGLListKstTopList;

    @XmlTransient
    public List<KGLListKstTop> getKGLListKstTopList() {
        return kGLListKstTopList;
    }

    public void setKGLListKstTopList(List<KGLListKstTop> kGLListKstTopList) {
        this.kGLListKstTopList = kGLListKstTopList;
    }
    

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "edBaseInformationID")//, referencedColumnName = "edID")
    private List<KGLListEndoscopyDifferential> kGLListEndoscopyDifferentialList;
    @XmlTransient
    public List<KGLListEndoscopyDifferential> getKGLListEndoscopyDifferentialList() {
        return kGLListEndoscopyDifferentialList;
    }

    public void setKGLListEndoscopyDifferentialList(List<KGLListEndoscopyDifferential> kGLListEndoscopyDifferentialList) {
        this.kGLListEndoscopyDifferentialList = kGLListEndoscopyDifferentialList;
    }


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nfcBaseInformationID")//, referencedColumnName = "nfcID")
    private List<KGLNormalFeeContract> kGLNormalFeeContractList;
    
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nfBaseInformationID")//, referencedColumnName = "nfID")
    private List<KGLNormalFreelancer> kGLNormalFreelancerList;
    
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "isBaseInformationID")//, referencedColumnName = "isID")
    private List<KGLListIntensivStroke> kGLListIntensivStrokeList;
    
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "miBaseInformationID")//, referencedColumnName = "miID")
    private List<KGLListMedInfra> kGLListMedInfraList;
    
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "paBaseInformationID")//, referencedColumnName = "paID")
    private List<KGLPersonalAccounting> kGLPersonalAccountingList;
    
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ndBaseInformationID")//, referencedColumnName = "ndID")
    private List<DrgNeonatData> _neonateData;

    public List<DrgNeonatData> getNeonateData() {
        return _neonateData;
    }

    public void setNeonateData(List<DrgNeonatData> neonateData) {
        this._neonateData = neonateData;
    }
        
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nssBaseInformationID")//, referencedColumnName = "nssID")
    private List<KGLNormalStationServiceDocumentation> kGLNormalStationServiceDocumentationList;
    
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "iscBaseInformationID")//, referencedColumnName = "iscID")
    private List<KGLListIntensiveStrokeCost> kGLListIntensiveStrokeCostList;
    
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cccBaseInformationID")//, referencedColumnName = "cccID")    
    private List<KGLListCostCenterCost> kGLListCostCenterCostList;
    
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "paBaseInformationID")//, referencedColumnName = "paID")    
    private List<KGLPKMSAlternative> kGLPKMSAlternativeList;
    
    
    
    public DrgCalcBasics() {
    }

    public DrgCalcBasics(Integer biID) {
        this._id = biID;
    }

    public DrgCalcBasics(Integer biID, int biDataYear, int biIK, String biHospitalName, int biAccountID, Date biLastChanged, int biStatusID, short biDeliveryType, String biCorrectionNote, BigDecimal biSumCalcCost, int biCaseInStationCnt, int biCasePartialStationCnt, int biDaysPartialStation, int biPatientEscort, int biPreStation, int biBeds, int biPartialCnt, int biLocationCnt, boolean biDifLocationSupply, boolean biSpecialUnit, boolean biCentralFocus, int biNeonatLvl, boolean biGynecology, boolean biObstetrical, BigDecimal biDeliveryRoomHours, int biDeliveryRoomPreBirthHabitationCnt, int biDeliveryRoomInstationBirthCnt, boolean biNoDeliveryRoomHabitation, String biDeliveryRoomOrganizationalStructure, boolean biCardiology, int biCardiologyRoomCnt, int biCardiologyCaseCnt, boolean biEndoscopy, int biEndoscopyRoomCnt, int biEndoscopyCaseCnt, boolean biMinimalValvularIntervention, int biMviFulfilled, boolean biMviGuidelineAspired, boolean biNormalFreelancing, boolean biFeeContract, boolean biPKMSRecording, int biPKMSCaseCnt, boolean biKISIntegration, String biNormalStationOther, boolean biApproximationMethodMedInfra, boolean biStepladderMethodMedInfra, boolean biExtensionMethodMedInfra, boolean biOtherMethodMedInfra, boolean biApproximationMethodNonMedInfra, boolean biStepladderMethodNonMedInfra, boolean biExtensionMethodNonMedInfra, boolean biOtherMethodNonMedInfra, boolean biIntensiveBed, boolean biIntensiveStrokeBed) {
        this._id = biID;
        this._dataYear = biDataYear;
        this._ik = biIK;
        this._hospitalName = biHospitalName;
        this._accountId = biAccountID;
        this._lastChanged = biLastChanged;
        this._statusId = biStatusID;
        this._deliveryType = biDeliveryType;
        this._correctionNote = biCorrectionNote;
        this._sumCalcCost = biSumCalcCost;
        this._caseInStationCnt = biCaseInStationCnt;
        this._casePartialStationCnt = biCasePartialStationCnt;
        this._daysPartialStation = biDaysPartialStation;
        this._patientEscort = biPatientEscort;
        this._preStation = biPreStation;
        this._beds = biBeds;
        this._partialCnt = biPartialCnt;
        this._locationCnt = biLocationCnt;
        this._difLocationSupply = biDifLocationSupply;
        this._specialUnit = biSpecialUnit;
        this._centralFocus = biCentralFocus;
        this._neonatLvl = biNeonatLvl;
        this._gynecology = biGynecology;
        this._obstetrical = biObstetrical;
        this._deliveryRoomHours = biDeliveryRoomHours;
        this._deliveryRoomPreBirthHabitationCnt = biDeliveryRoomPreBirthHabitationCnt;
        this._deliveryRoomInstationBirthCnt = biDeliveryRoomInstationBirthCnt;
        this._noDeliveryRoomHabitation = biNoDeliveryRoomHabitation;
        this._deliveryRoomOrganizationalStructure = biDeliveryRoomOrganizationalStructure;
        this._cardiology = biCardiology;
        this._cardiologyRoomCnt = biCardiologyRoomCnt;
        this._cardiologyCaseCnt = biCardiologyCaseCnt;
        this._endoscopy = biEndoscopy;
        this._endoscopyRoomCnt = biEndoscopyRoomCnt;
        this._endoscopyCaseCnt = biEndoscopyCaseCnt;
        this._minimalValvularIntervention = biMinimalValvularIntervention;
        this._mviFulfilled = biMviFulfilled;
        this._mviGuidelineAspired = biMviGuidelineAspired;
        this._normalFreelancing = biNormalFreelancing;
        this._feeContract = biFeeContract;
        this._pkmsRecording = biPKMSRecording;
        this._pkmsCaseCnt = biPKMSCaseCnt;
        this._kisIntegration = biKISIntegration;
        this._normalStationOther = biNormalStationOther;
        this._approximationMethodMedInfra = biApproximationMethodMedInfra;
        this._stepladderMethodMedInfra = biStepladderMethodMedInfra;
        this._extensionMethodMedInfra = biExtensionMethodMedInfra;
        this._otherMethodMedInfra = biOtherMethodMedInfra;
        this._approximationMethodNonMedInfra = biApproximationMethodNonMedInfra;
        this._stepladderMethodNonMedInfra = biStepladderMethodNonMedInfra;
        this._extensionMethodNonMedInfra = biExtensionMethodNonMedInfra;
        this._otherMethodNonMedInfra = biOtherMethodNonMedInfra;
        this._intensiveBed = biIntensiveBed;
        this._intensiveStrokeBed = biIntensiveStrokeBed;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DrgCalcBasics)) {
            return false;
        }
        DrgCalcBasics other = (DrgCalcBasics) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.DrgCalcBasics[ biID=" + _id + " ]";
    }
    
}
