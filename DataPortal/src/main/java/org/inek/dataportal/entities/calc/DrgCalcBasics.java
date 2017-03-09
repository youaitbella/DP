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
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.utils.Documentation;

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
    @Documentation(key = "lblYearData")
    private int _dataYear;

    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        this._dataYear = dataYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="lastChanged">
    @Column(name = "biLastChanged")
    @Temporal(TemporalType.TIMESTAMP)
    @Documentation(name = "Stand")
    private Date _lastChanged;

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        this._lastChanged = lastChanged;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ik">
    @Column(name = "biIK")
    @Documentation(key = "lblIK")
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

    //<editor-fold defaultstate="collapsed" desc="statusID">
    @Column(name = "biStatusID")
    private int _statusId;

    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        this._statusId = statusId;
    }

    @Documentation(key = "lblWorkstate", rank = 10)
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    public void setStatus(WorkflowStatus status) {
        _statusId = status.getValue();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="correctionNote">
    @Column(name = "biCorrectionNote")
    @Documentation(key = "lblComment")
    private String _correctionNote = "";

    @Size(max = 500)
    public String getCorrectionNote() {
        return _correctionNote;
    }

    public void setCorrectionNote(String correctionNote) {
        this._correctionNote = correctionNote;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="sumCalcCost">
    @Column(name = "biSumCalcCost")
    @Documentation(key = "lblSumCalcCost", headline = "Erklärung zur Kalkulationsgrundlage", rank = 1000)
    private int _sumCalcCost;

    @Min(0)
    public int getSumCalcCost() {
        return _sumCalcCost;
    }

    public void setSumCalcCost(int sumCalcCost) {
        this._sumCalcCost = sumCalcCost;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="caseInStationCnt">
    @Column(name = "biCaseInStationCnt")
    @Documentation(name = "Fälle vollstationär", rank = 1000)
    private int _caseInStationCnt;

    @Min(0)
    public int getCaseInStationCnt() {
        return _caseInStationCnt;
    }

    public void setCaseInStationCnt(int caseInStationCnt) {
        this._caseInStationCnt = caseInStationCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="casePartialStationCnt">
    @Column(name = "biCasePartialStationCnt")
    @Documentation(name = "Fälle teilstationär", rank = 1000)
    private int _casePartialStationCnt;

    @Min(0)
    public int getCasePartialStationCnt() {
        return _casePartialStationCnt;
    }

    public void setCasePartialStationCnt(int casePartialStationCnt) {
        this._casePartialStationCnt = casePartialStationCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="daysPartialStation">
    @Column(name = "biDaysPartialStation")
    @Documentation(name = "Berechnungstage teilstationär", rank = 1000)
    private int _daysPartialStation;

    @Min(0)
    public int getDaysPartialStation() {
        return _daysPartialStation;
    }

    public void setDaysPartialStation(int daysPartialStation) {
        this._daysPartialStation = daysPartialStation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="patientEscort">
    @Column(name = "biPatientEscort")
    @Documentation(name = "Begleitpersonen", rank = 1000)
    private int _patientEscort;

    @Min(0)
    public int getPatientEscort() {
        return _patientEscort;
    }

    public void setPatientEscort(int patientEscort) {
        this._patientEscort = patientEscort;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="preStation">
    @Column(name = "biPreStation")
    @Documentation(name = "Fälle rein vorstationär", rank = 1000)
    private int _preStation;

    @Min(0)
    public int getPreStation() {
        return _preStation;
    }

    public void setPreStation(int preStation) {
        this._preStation = preStation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="beds">
    @Column(name = "biBeds")
    @Documentation(name = "Anzahl Betten (DRG)", rank = 1000)
    private int _beds;

    @Min(0)
    public int getBeds() {
        return _beds;
    }

    public void setBeds(int beds) {
        this._beds = beds;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="partialCnt">
    @Column(name = "biPartialCnt")
    @Documentation(name = "Anzahl teilstationäre Plätze (DRG)", rank = 1000)
    private int _partialCnt;

    @Min(0)
    public int getPartialCnt() {
        return _partialCnt;
    }

    public void setPartialCnt(int partialCnt) {
        this._partialCnt = partialCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="locationCnt">
    @Column(name = "biLocationCnt")
    @Documentation(name = "Anzahl Standorte", rank = 1000)
    private int _locationCnt;

    @Min(0)
    public int getLocationCnt() {
        return _locationCnt;
    }

    public void setLocationCnt(int locationCnt) {
        this._locationCnt = locationCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="difLocationSupply">
    @Column(name = "biDifLocationSupply")
    @Documentation(name = "Differenzierter Versorgungsauftrag", rank = 1000)
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
    @Documentation(name = "Besondere Einrichtung", rank = 1050)
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
    @Documentation(name = "Zentren und Schwerpunkte", rank = 1100)
    private boolean _centralFocus;

    public boolean isCentralFocus() {
        return _centralFocus;
    }

    public void setCentralFocus(boolean centralFocus) {
        this._centralFocus = centralFocus;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gynecology">
    @Column(name = "biGynecology")
    @Documentation(name = "Leistungen im Bereich der Gynäkologie", headline = "Kreißsaal", rank = 4000)
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
    @Documentation(name = "Leistungen im Bereich Geburtshilfe", rank = 4000)
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
    @Documentation(name = "Aufenthaltszeit der Patientin im Kreißsaal (Std.)", omitOnEmpty = true, rank = 4000)
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
    @Documentation(name = "davon für vorgeburtliche Kreißsaalaufenthalte", omitOnEmpty = true, rank = 4000)
    private int _deliveryRoomPreBirthHabitationCnt;

    @Min(0)
    public int getDeliveryRoomPreBirthHabitationCnt() {
        return _deliveryRoomPreBirthHabitationCnt;
    }

    public void setDeliveryRoomPreBirthHabitationCnt(int deliveryRoomPreBirthHabitationCnt) {
        this._deliveryRoomPreBirthHabitationCnt = deliveryRoomPreBirthHabitationCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="deliveryRoomInstationBirthCnt">
    @Column(name = "biDeliveryRoomInstationBirthCnt")
    @Documentation(name = "Anzahl vollstationär geborener Kinder", omitOnEmpty = true, rank = 4000)
    private int _deliveryRoomInstationBirthCnt;

    @Min(0)
    public int getDeliveryRoomInstationBirthCnt() {
        return _deliveryRoomInstationBirthCnt;
    }

    public void setDeliveryRoomInstationBirthCnt(int deliveryRoomInstationBirthCnt) {
        this._deliveryRoomInstationBirthCnt = deliveryRoomInstationBirthCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="noDeliveryRoomHabitation">
    @Column(name = "biNoDeliveryRoomHabitation")
    @Documentation(name = "Bei vorgeburtlichen Fällen keine Aufenthaltszeiten der Patientin im Kreißsaal", omitOnEmpty = true, rank = 4000)
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
    @Documentation(name = "Organisationsstrukturen", omitOnEmpty = true, rank = 4000)
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

    //<editor-fold defaultstate="collapsed" desc="personalAccountingDescription">

    @Column(name = "biPersonalAccountingDescription")
    private String _personalAccountingDescription = "";

    public String getPersonalAccountingDescription() {
        return _personalAccountingDescription;
    }

    public void setPersonalAccountingDescription(String personalAccountingDescription) {
        this._personalAccountingDescription = personalAccountingDescription;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="cardiologyRoomCnt">
    @Column(name = "biCardiologyRoomCnt")
    private int _cardiologyRoomCnt;

    @Min(0)
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

    @Min(0)
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

    @Min(0)
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

    @Min(0)
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

    public int getPkmsRecording() {
        return _pkmsRecording;
    }

    public void setPkmsRecording(int pkmsRecording) {
        this._pkmsRecording = pkmsRecording;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="pkmsCaseCnt">
    @Column(name = "biPKMSCaseCnt")
    private int _pkmsCaseCnt;

    @Min(0)
    public int getPkmsCaseCnt() {
        return _pkmsCaseCnt;
    }

    public void setPkmsCaseCnt(int pkmsCaseCnt) {
        this._pkmsCaseCnt = pkmsCaseCnt;
    }
    //</editor-fold>
    
    @Column(name = "biPKMSComplex")
    private boolean _pkmsComplex;

    public boolean isPkmsComplex() {
        return _pkmsComplex;
    }

    public void setPkmsComplex(boolean _pkmsComplex) {
        this._pkmsComplex = _pkmsComplex;
    }

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

    //<editor-fold defaultstate="collapsed" desc="Property IBLVMethodMedInfra">
    @Column(name = "biIBLVMethodMedInfra")
    private int _iblvMethodMedInfra;

    public int getIblvMethodMedInfra() {
        return _iblvMethodMedInfra;
    }

    public void setIblvMethodMedInfra(int iblvMethodMedInfra) {
        this._iblvMethodMedInfra = iblvMethodMedInfra;
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

    //<editor-fold defaultstate="collapsed" desc="_hasDescMedicalInfra">
    @Transient
    private boolean _hasDescMedicalInfra = false;

    public boolean isDescMedicalInfra() {
        return _hasDescMedicalInfra;
    }

    public void setDescMedicalInfra(boolean _hasDesc) {
        this._hasDescMedicalInfra = _hasDesc;
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

    // <editor-fold defaultstate="collapsed" desc="Property List DelimitationFacts">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dfBaseInformationId", referencedColumnName = "biId")
    @OrderBy(value = "_contentTextId")
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
    @JoinColumn(name = "ccBaseInformationId", referencedColumnName = "biId")
    private List<KGLListCostCenter> _costCenters = new Vector<>();

    
    public List<KGLListCostCenter> getCostCenters() {
        return _costCenters;
    }

    public void setCostCenters(List<KGLListCostCenter> costCenter) {
        this._costCenters = costCenter;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List RadiologyLaboratories">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "rlBaseInformationId", referencedColumnName = "biID")
    private List<KGLListRadiologyLaboratory> _radiologyLaboratories = new Vector<>();

    
    public List<KGLListRadiologyLaboratory> getRadiologyLaboratories() {
        return _radiologyLaboratories;
    }

    public void setRadiologyLaboratories(List<KGLListRadiologyLaboratory> radiologyLaboratory) {
        this._radiologyLaboratories = radiologyLaboratory;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List ObstetricsGynecologies">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ogBaseInformationId", referencedColumnName = "biID")
    private List<KGLListObstetricsGynecology> _obstetricsGynecologies  = new Vector<>();
    
    
    public List<KGLListObstetricsGynecology> getObstetricsGynecologies() {
        return _obstetricsGynecologies;
    }
    
    public void setObstetricsGynecologies(List<KGLListObstetricsGynecology> obstetricsGynecology) {
        this._obstetricsGynecologies = obstetricsGynecology;
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="Property List RadiologyServices">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "rsBaseInformationId", referencedColumnName = "biID")
    private List<KGLRadiologyService> _radiologyServices = new Vector<>();

    
    public List<KGLRadiologyService> getRadiologyServices() {
        return _radiologyServices;
    }

    public void setRadiologyServices(List<KGLRadiologyService> radiologyService) {
        this._radiologyServices = radiologyService;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property OpAn">
    @OneToOne
    @PrimaryKeyJoinColumn(name = "biID", referencedColumnName = "oaBaseInformationId")
    @Documentation(include = true)
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
    
    
    public List<KGLListServiceProvision> getServiceProvisions() {
        return _serviceProvisions;
    }
    
    public void setServiceProvisions(List<KGLListServiceProvision> serviceProvision) {
        this._serviceProvisions = serviceProvision;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List KstTop">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ktBaseInformationId", referencedColumnName = "biId")
    private List<KGLListKstTop> _kstTop = new Vector<>();
    
    
    public List<KGLListKstTop> getKstTop() {
        ensureTopList();
        return _kstTop;
    }
    
    public void setKstTop(List<KGLListKstTop> kstTop) {
        this._kstTop = kstTop;
    }
    
    private void ensureTopList() {
        if (_kstTop == null) {
            _kstTop = new Vector<>();
        }
        ensureTopListCostCenter(4, 3);
        ensureTopListCostCenter(6, 5);
    }

    private void ensureTopListCostCenter(int costCenterId, int count) {
        if (_kstTop.stream().filter(e -> e.getKtCostCenterId() == costCenterId).count() == 0) {
            for (int i = 0; i < count; i++) {
                KGLListKstTop item = new KGLListKstTop();
                item.setBaseInformationId(_id);
                item.setKtCostCenterId(costCenterId);
                _kstTop.add(item);
            }
        }
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List EndoscopyDifferentials">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "edBaseInformationId", referencedColumnName = "biId")
    private List<KGLListEndoscopyDifferential> _endoscopyDifferentials = new Vector<>();
    
    
    public List<KGLListEndoscopyDifferential> getEndoscopyDifferentials() {
        return _endoscopyDifferentials;
    }
    
    public void setEndoscopyDifferentials(List<KGLListEndoscopyDifferential> endoscopyDifferential) {
        this._endoscopyDifferentials = endoscopyDifferential;
    }
    //</editor-fold>
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "leaBaseInformationId", referencedColumnName = "biId")
    private List<KGLListEndoscopyAmbulant> _endoscopyAmbulant = new Vector<>();

    public List<KGLListEndoscopyAmbulant> getEndoscopyAmbulant() {
        return _endoscopyAmbulant;
    }

    public void setEndoscopyAmbulant(List<KGLListEndoscopyAmbulant> _endoscopyAmbulant) {
        this._endoscopyAmbulant = _endoscopyAmbulant;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Property List normalFeeContracts">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nfcBaseInformationId", referencedColumnName = "biID")
    private List<KGLNormalFeeContract> _normalFeeContracts = new Vector<>();
    
    
    public List<KGLNormalFeeContract> getNormalFeeContracts() {
        return _normalFeeContracts;
    }
    
    public void setNormalFeeContracts(List<KGLNormalFeeContract> normalFeeContracts) {
        this._normalFeeContracts = normalFeeContracts;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List NormalFreelancers">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nfBaseInformationId", referencedColumnName = "biID")
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
    @JoinColumn(name = "isBaseInformationId", referencedColumnName = "biID")
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
    @JoinColumn(name = "miBaseInformationId", referencedColumnName = "biID")
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
    @JoinColumn(name = "paBaseInformationId", referencedColumnName = "biID")
    @OrderBy(value = "_costTypeId")
    private List<KGLPersonalAccounting> _personalAccountings = new Vector<>();
    
    public List<KGLPersonalAccounting> getPersonalAccountings() {
        return _personalAccountings;
    }
    
    public void setPersonalAccountings(List<KGLPersonalAccounting> personalAccountings) {
        this._personalAccountings = personalAccountings;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="neonatLvl">
    @Column(name = "biNeonatLvl")
    @Documentation(name = "Versorgungsstufe des Perinatalzentrums", headline = "Neonatologische Versorgung", rank = 19000)
    private int _neonatLvl;

    public int getNeonatLvl() {
        return _neonatLvl;
    }

    public void setNeonatLvl(int neonatLvl) {
        this._neonatLvl = neonatLvl;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List NeonateDatas">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ndBaseInformationId", referencedColumnName = "biID")
    @Documentation(name = "Neonatologische Versorgung", rank = 19000)
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
    @JoinColumn(name = "nssBaseInformationId", referencedColumnName = "biID")
    @OrderBy(value = "_contentTextId")
    private List<KGLNormalStationServiceDocumentation> _normalStationServiceDocumentations = new Vector<>();
    
    public List<KGLNormalStationServiceDocumentation> getNormalStationServiceDocumentations() {
        return _normalStationServiceDocumentations;
    }
    
    public void setNormalStationServiceDocumentations(List<KGLNormalStationServiceDocumentation> normalStationServiceDocumentations) {
        this._normalStationServiceDocumentations = normalStationServiceDocumentations;
    }
    //</editor-fold>
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nssdmBaseInformationId", referencedColumnName = "biID")
    private List<KGLNormalStationServiceDocumentationMinutes> _normalStationServiceDocumentationMinutes = new Vector<>();

    public List<KGLNormalStationServiceDocumentationMinutes> getNormalStationServiceDocumentationMinutes() {
        return _normalStationServiceDocumentationMinutes;
    }

    public void setNormalStationServiceDocumentationMinutes(List<KGLNormalStationServiceDocumentationMinutes> _normalStationServiceDocumentationMinutes) {
        this._normalStationServiceDocumentationMinutes = _normalStationServiceDocumentationMinutes;
    }    
    
    //<editor-fold defaultstate="collapsed" desc="Property List _costCenterCosts">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cccBaseInformationId", referencedColumnName = "biID")
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
    @JoinColumn(name = "paBaseInformationId", referencedColumnName = "biID")
    private List<KGLPKMSAlternative> _pkmsAlternatives = new Vector<>();
    
    public List<KGLPKMSAlternative> getPkmsAlternatives() {
        return _pkmsAlternatives;
    }
    
    public void setPkmsAlternatives(List<KGLPKMSAlternative> pkmsAlternatives) {
        this._pkmsAlternatives = pkmsAlternatives;
    }
    //</editor-fold>
              
    // <editor-fold defaultstate="collapsed" desc="Property Documents">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "doBaseInformationId", referencedColumnName = "biID")
    private List<KGLDocument> _documents = new Vector<>();

    public List<KGLDocument> getDocuments() {
        return _documents;
    }

    public void setDocuments(List<KGLDocument> documents) {
        _documents = documents;
    }
    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List centralFocuses">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cfBaseInformationId", referencedColumnName = "biID")
    private List<KGLListCentralFocus> _centralFocuses = new Vector<>();

    public List<KGLListCentralFocus> getCentralFocuses() {
        return _centralFocuses;
    }

    public void setCentralFocuses(List<KGLListCentralFocus> centralFocuses) {
        this._centralFocuses = centralFocuses;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property List locations">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lBaseInformationId", referencedColumnName = "biID")
    @OrderBy(value = "_locationNo")
    private List<KGLListLocation> _locations = new Vector<>();

    public List<KGLListLocation> getLocations() {
        return _locations;
    }

    public void setLocations(List<KGLListLocation> locations) {
        this._locations = locations;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List specialUnits">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "suBaseInformationId", referencedColumnName = "biID")
    private List<KGLListSpecialUnit> _specialUnits = new Vector<>();

    public List<KGLListSpecialUnit> getSpecialUnits() {
        return _specialUnits;
    }

    public void setSpecialUnits(List<KGLListSpecialUnit> specialUnits) {
        this._specialUnits = specialUnits;
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
