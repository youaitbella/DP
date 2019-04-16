/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.drg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.calc.ListUtil;
import org.inek.dataportal.calc.backingbean.CalcBasicsStaticData;
import org.inek.dataportal.calc.entities.psy.KglPkmsAlternative;
import org.inek.dataportal.common.data.iface.StatusEntity;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.Documentation;
import org.inek.dataportal.common.utils.IgnoreOnCompare;

import javax.faces.model.SelectItem;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLBaseInformation", schema = "calc")
@XmlRootElement
@SuppressWarnings({"MethodCount", "JavaNCSS"})
public class DrgCalcBasics implements Serializable, StatusEntity {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "biID", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Version">
    @Column(name = "biVersion")
    @Version
    @IgnoreOnCompare
    private int _version;

    @JsonIgnore
    public int getVersion() {
        return _version;
    }
    // </editor-fold>

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

    //<editor-fold defaultstate="collapsed" desc="accountIdLastChange">
    @Column(name = "biLastChangedBy")
    @IgnoreOnCompare
    private int _accountIdLastChange;

    @JsonIgnore
    public int getAccountIdLastChange() {
        return _accountIdLastChange;
    }

    public void setAccountIdLastChange(int accountId) {
        this._accountIdLastChange = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sealed">
    @Column(name = "biSealed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _sealed = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    @JsonIgnore
    public Date getSealed() {
        return _sealed;
    }

    public void setSealed(Date sealed) {
        this._sealed = sealed;
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
    @IgnoreOnCompare
    private int _statusId;

    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        this._statusId = statusId;
    }

    @Documentation(key = "lblWorkstate", rank = 10)
    @Override
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    @Override
    public void setStatus(WorkflowStatus status) {
        _statusId = status.getId();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Erklärung zur Kalkulationsgrundlage">
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

    //<editor-fold defaultstate="collapsed" desc="Erlösvolumen">
    @Column(name = "biRedeemingVolume")
    @Documentation(name = "Erlösvolumen")
    private int _redeemingVolume;

    @Min(0)
    public int getRedeemingVolume() {
        return _redeemingVolume;
    }

    public void setRedeemingVolume(int redeemingVolume) {
        this._redeemingVolume = redeemingVolume;
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

    //<editor-fold defaultstate="collapsed" desc="casePartialStationCnt">
    @Column(name = "biCaseBaCnt")
    @Documentation(name = "Fälle Belegabteilung")
    private int _caseBaCnt;

    @Min(0)
    public int getCaseBaCnt() {
        return _caseBaCnt;
    }

    public void setCaseBaCnt(int caseBaCnt) {
        this._caseBaCnt = caseBaCnt;
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
    @Documentation(name = "Leistungen im Bereich der Gynäkologie", headline = "Kostenstellengruppe 6 (Geburtshilfe und Gynäkologie)", rank = 4000)
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
    @Documentation(name = "KH erbringt Leistungen in Kardiologie", rank = 5000, headline = "Kostenstellengruppe 7 (Kardiologie)")
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
    @Documentation(key = "lblCardiologyRoomCount", rank = 5000, omitOnValues = "0")
    private int _cardiologyRoomCnt;

    @Min(0)
    public int getCardiologyRoomCnt() {
        return _cardiologyRoomCnt;
    }

    public void setCardiologyRoomCnt(int cardiologyRoomCnt) {
        this._cardiologyRoomCnt = cardiologyRoomCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="biCardiologyLocationCnt">
    @Column(name = "biCardiologyLocationCnt")
    @Documentation(name = "Anzahl Standorte Kardiologie")
    private int _cardiologyLocationCnt;

    @Min(0)
    public int getCardiologyLocationCnt() {
        return _cardiologyLocationCnt;
    }

    public void setCardiologyLocationCnt(int cardiologyLocationCnt) {
        this._cardiologyLocationCnt = cardiologyLocationCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cardiologyCaseCnt">
    @Column(name = "biCardiologyCaseCnt")
    @Documentation(name = "Anzahl kalkulationsrelevante Fälle Kardiologie", rank = 5000, omitOnValues = "0")
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
    @Documentation(name = "Leistungen im Bereich der Endoskopie", rank = 6000, headline = "Kostenstellengruppe 8 (Endoskopie)")
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
    @Documentation(name = "Anzahl endoskopischer Eingriffsräume", rank = 6010, omitOnValues = "0")
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
    @Documentation(name = "Anzahl kalkulationsrelevanter endoskopischer Fälle", rank = 6010, omitOnValues = "0")
    private int _endoscopyCaseCnt;

    @Min(0)
    public int getEndoscopyCaseCnt() {
        return _endoscopyCaseCnt;
    }

    public void setEndoscopyCaseCnt(int endoscopyCaseCnt) {
        this._endoscopyCaseCnt = endoscopyCaseCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="biEndoscopyLocationCnt">
    @Column(name = "biEndoscopyLocationCnt")
    @Documentation(name = "Anzahl Standorte Endoskopie")
    private int _endoscopyLocationCnt;

    @Min(0)
    public int getEndoscopyLocationCnt() {
        return _endoscopyLocationCnt;
    }

    public void setEndoscopyLocationCnt(int endoscopyLocationCnt) {
        this._endoscopyLocationCnt = endoscopyLocationCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="minimalValvularIntervention">
    @Column(name = "biMinimalValvularIntervention")
    @Documentation(name = "KH führt minimalinvasiven Herzklappeninterventionen durch", rank = 17000,
            headline = "Ergänzende Angaben zur minimalinvasiven Herzklappeninterventionen")
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
    @Documentation(key = "lblFulfillmentDemand")
    private int _mviFulfilled;

    public int getMviFulfilled() {
        return _mviFulfilled;
    }

    public void setMviFulfilled(int mviFulfilled) {
        this._mviFulfilled = mviFulfilled;
    }

    @Documentation(name = "Erfüllung der Anforderungen", rank = 17010, omitOnEmpty = true)
    @JsonIgnore
    private String getMviFulfilledText() {
        return CalcBasicsStaticData.staticGetMviFulfillmentItems()
                .stream()
                .filter(i -> (int) i.getValue() == _mviFulfilled)
                .findAny().orElse(new SelectItem(-1, ""))
                .getLabel();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="mviGuidelineAspired">
    @Column(name = "biMviGuidelineAspired")
    @Documentation(name = "Das KH wird im kommenden Datenjahr die notwendigen Anforderungen erfüllen", rank = 17020)
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
    @Documentation(name = "Ärzte freie Mitarbeit", rank = 12000, headline = "Ergänzende Angaben zur Normalstation")
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
    @Documentation(name = "Honorarverträge", rank = 12020)
    private boolean _feeContract;

    public boolean isFeeContract() {
        return _feeContract;
    }

    public void setFeeContract(boolean feeContract) {
        this._feeContract = feeContract;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PKMSComplex">
    @Column(name = "biPKMSComplex")
    @Documentation(name = "Es liegen Patienten mit PKMS vor", rank = 12050)
    private boolean _pkmsComplex;

    public boolean isPkmsComplex() {
        return _pkmsComplex;
    }

    public void setPkmsComplex(boolean pkmsComplex) {
        this._pkmsComplex = pkmsComplex;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="pkmsCaseCnt">
    @Column(name = "biPKMSCaseCnt")
    @Documentation(name = "Anzahl der kalkulierten Fälle mit PKMS", rank = 12060)
    private int _pkmsCaseCnt;

    @Min(0)
    public int getPkmsCaseCnt() {
        return _pkmsCaseCnt;
    }

    public void setPkmsCaseCnt(int pkmsCaseCnt) {
        this._pkmsCaseCnt = pkmsCaseCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="pkmsRecording">
    @Column(name = "biPKMSRecording")
    @Documentation(name = "Erfassung des PKMS liegt in allen relevanten Normalstationen", rank = 12070,
            translateValue = "0=Nein;1=Ja;2=KIS-integriert;3=Manuell;4=Sonstiges")
    private int _pkmsRecording;

    public int getPkmsRecording() {
        return _pkmsRecording;
    }

    public void setPkmsRecording(int pkmsRecording) {
        this._pkmsRecording = pkmsRecording;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PkmsOther">
    @Column(name = "biPKMSOther")
    @Documentation(name = "Erläuterung PKMS sonstiges", rank = 12080)
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
    @Documentation(name = "Gewähltes Verfahren bei Durchführung der IBLV",
            headline = "Ergänzende Angaben zur innerbetrieblichen Leistungsverrechnung (medizinische Infrastruktur)", rank = 14000)
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
    @Documentation(name = "Erläuterung", rank = 14010)
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

    public void setDescMedicalInfra(boolean hasDesc) {
        this._hasDescMedicalInfra = hasDesc;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="intensiveBed">
    @Column(name = "biIntensiveBed")
    @Documentation(name = "Das Krankenhaus hat Intensivbetten", rank = 13010, headline = "Ergänzende Angaben zur Intensivbehandlung")
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
    @Documentation(name = "Das Krankenhaus hat Intensivbetten zur Behandlung des akuten Schlaganfalls",
            rank = 14000, translateValue = "0=Nein;1=Ja", headline = "Ergänzende Angaben zur Stroke Unit")
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
    @Documentation(name = "Abgrenzungstatbestände", rank = 1120)
    private List<DrgDelimitationFact> _delimitationFacts = new Vector<>();

    public List<DrgDelimitationFact> getDelimitationFacts() {
        return _delimitationFacts;
    }

    @Documentation(name = "Berücksichtigte Abgrenzungstatbestände", rank = 1120)
    public List<DrgDelimitationFact> getDelimitationFactsInUse() {
        return _delimitationFacts.stream().filter(i -> i.isUsed() == true).collect(Collectors.toList());
    }

    public void setDelimitationFacts(List<DrgDelimitationFact> delimitationFacts) {
        this._delimitationFacts = delimitationFacts;
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

    @Documentation(name = "Kostenstellen1", rank = 4040)
    @JsonIgnore
    public List<KGLListCostCenter> getCostCenters1() {
        return _costCenters.stream().filter(c -> c.getCostCenterId() == 1).collect(Collectors.toList());
    }

    @Documentation(name = "Kostenstellen6", rank = 4050)
    @JsonIgnore
    public List<KGLListCostCenter> getCostCenters6() {
        return _costCenters.stream().filter(c -> c.getCostCenterId() == 6).collect(Collectors.toList());
    }

    @Documentation(name = "Kostenstellen", headline = "Kostenstellengruppe 11 (Diagnostische Bereiche)", rank = 9000)
    @JsonIgnore
    public List<KGLListCostCenter> getCostCenters11() {
        return _costCenters.stream().filter(c -> c.getCostCenterId() == 11).collect(Collectors.toList());
    }

    @Documentation(name = "Kostenstellen", headline = "Kostenstellengruppe 12 (Therapeutische Verfahren)", rank = 10000)
    @JsonIgnore
    public List<KGLListCostCenter> getCostCenters12() {
        return _costCenters.stream().filter(c -> c.getCostCenterId() == 12).collect(Collectors.toList());
    }

    @Documentation(name = "Kostenstellen", headline = "Kostenstellengruppe 13 (Patientenaufnahme)", rank = 11000)
    @JsonIgnore
    public List<KGLListCostCenter> getCostCenters13() {
        return _costCenters.stream().filter(c -> c.getCostCenterId() == 13).collect(Collectors.toList());
    }

    public void addCostCenter(KGLListCostCenter item) {
        KGLListCostCenter foundItem = ListUtil.findItem(_costCenters, item, (a, b)
                -> a.getCostCenterId() == b.getCostCenterId()
                && a.getCostCenterNumber().equals(b.getCostCenterNumber())
                && a.getCostCenterText().equalsIgnoreCase(b.getCostCenterText()));
// check if the above is a key info for cost center the rest is for equality without saving information
//        &&
//                a.getAmount() == b.getAmount() &&
//                        a.getCostCenterText().equals(b.getCostCenterText()) &&
//                        a.getFullVigorCnt() == b.getFullVigorCnt() &&
//                        a.getServiceKeyDescription().equals(b.getServiceKeyDescription()) &&
//                        a.getServiceSum() == b.getServiceSum());

        if (foundItem != null) {
            foundItem.copyCostCenter(item);
        } else {
            _costCenters.add(item);
        }
    }

    public void deleteCostCenters(int costCenterId) {
        _costCenters.removeIf(center -> center.getCostCenterId() == costCenterId);
    }

    public void deleteCostCenters_11_12_13() {
        _costCenters.removeIf(center -> center.getCostCenterId() >= 11 && center.getCostCenterId() <= 13);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Property List RadiologyLaboratories">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "rlBaseInformationId", referencedColumnName = "biID")
    private List<KGLListRadiologyLaboratory> _radiologyLaboratories = new Vector<>();

    public List<KGLListRadiologyLaboratory> getRadiologyLaboratories() {
        return _radiologyLaboratories;
    }

    public void deleteLaboratories() {
        _radiologyLaboratories.removeAll(getLaboratories());
    }

    public void deleteRadiologies() {
        _radiologyLaboratories.removeAll(getRadiologies());
    }

    @Documentation(name = "Kostenstellen", headline = "Kostenstellengruppe 9 (Radiologie)", rank = 7000)
    public List<KGLListRadiologyLaboratory> getRadiologies() {
        return _radiologyLaboratories.stream().filter(c -> c.getCostCenterId() == 9).collect(Collectors.toList());
    }

    @Documentation(name = "Kostenstellen", headline = "Kostenstellengruppe 10 (Laboratorien)", rank = 8000)
    public List<KGLListRadiologyLaboratory> getLaboratories() {
        return _radiologyLaboratories.stream().filter(c -> c.getCostCenterId() == 10).collect(Collectors.toList());
    }

    public void setRadiologyLaboratories(List<KGLListRadiologyLaboratory> radiologyLaboratory) {
        this._radiologyLaboratories = radiologyLaboratory;
    }

    public void clearRadiologies() {
        _radiologyLaboratories.removeIf(laboratory -> laboratory.getCostCenterId() == 9);
    }

    public void clearLaboratories() {
        _radiologyLaboratories.removeIf(laboratory -> laboratory.getCostCenterId() == 10);
    }

    public void addRadiologyLaboratories(KGLListRadiologyLaboratory item, int ccId) {
        item.setCostCenterId(ccId);
        item.setBaseInformationId(_id);
        KGLListRadiologyLaboratory foundItem = ListUtil.findItem(_radiologyLaboratories, item, (a, b)
                -> a.getCostCenterId() == b.getCostCenterId()
                && a.getCostCenterNumber().equals(b.getCostCenterNumber())
                && a.getCostCenterText().equalsIgnoreCase(b.getCostCenterText())
        );
        if (foundItem != null) {
            if (item.getCostCenterId() == ccId) {
                item.setCostCenterId(ccId);
                item.setBaseInformationId(_id);
            }
        } else {
            _radiologyLaboratories.add(item);
        }

//        item.setCostCenterId(ccId);
//        item.setBaseInformationId(_id);
//        _radiologyLaboratories.add(item);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property List ObstetricsGynecologies">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ogBaseInformationId", referencedColumnName = "biID")
    @Documentation(name = "KGLListObstetricsGynecology", rank = 4050)
    private List<KGLListObstetricsGynecology> _obstetricsGynecologies = new Vector<>();

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
    @Documentation(name = "Ausgewählte Leistungen", rank = 7020)
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
    @Valid
    private KglOpAn _opAn;

    public KglOpAn getOpAn() {
        if (_opAn == null) {
            _opAn = new KglOpAn(_id);
        }
        return _opAn;
    }

    public void setOpAn(KglOpAn opAn) {
        this._opAn = opAn;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property List ServiceProvisions">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "spBaseInformationId", referencedColumnName = "biId")
    @OrderBy("_serviceProvisionTypeId")
    @Documentation(name = "Ext. Leistungen", headline = "(Externe) Leistungserbringung / Fremdvergabe", rank = 2010)
    private List<KGLListServiceProvision> _serviceProvisions = new Vector<>();

    public List<KGLListServiceProvision> getServiceProvisions() {
        return _serviceProvisions;
    }

    public void setServiceProvisions(List<KGLListServiceProvision> serviceProvision) {
        this._serviceProvisions = serviceProvision;
    }

    public void addServiceProvision(KGLListServiceProvision serviceProvision) {
        this._serviceProvisions.add(serviceProvision);
    }

    public void removeEmptyServiceProvisions() {
        _serviceProvisions.removeIf(p -> p.getServiceProvisionTypeId() < 0 && p.getDomain().isEmpty());
    }
    //</editor-fold>
    
        //<editor-fold defaultstate="collapsed" desc="Property List OverviewPersonal">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "opBaseInformationId", referencedColumnName = "biId")
    @Documentation(name = "Übersicht Personal", headline = "Übersicht Personal", rank = 10)
    private List<KGLListOverviewPersonal> _overviewPersonal = new Vector<>();

    public List<KGLListOverviewPersonal> getOverviewPersonals() {
        return _overviewPersonal.stream()
                .sorted((e1, e2) -> Integer.compare(e1.getOverviewPersonalType().getSequence(),
                    e2.getOverviewPersonalType().getSequence()))
                .collect(Collectors.toList());
    }

    public void setOverviewPersonals(List<KGLListOverviewPersonal> overviewPersonal) {
        this._overviewPersonal = overviewPersonal;
    }

    public void addOverviewPersonal(KGLListOverviewPersonal overviewPersonal) {
        this._overviewPersonal.add(overviewPersonal);
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

    @Documentation(name = "TOP 3 Leistungen", rank = 3100, omitOnOtherValues = "KGLOpAn._centralOPCnt=0")
    public List<KGLListKstTop> getKstTopOp() {
        ensureTopList();
        return _kstTop.stream().filter(i -> i.getKtCostCenterId() == 4).collect(Collectors.toList());
    }

    @Documentation(name = "TOP 5 Leistungen", rank = 4100)
    public List<KGLListKstTop> getKstTopMaternityRoom() {
        ensureTopList();
        return _kstTop.stream().filter(i -> i.getKtCostCenterId() == 6).collect(Collectors.toList());
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
    @Documentation(name = "Endoskopischer Bereich", rank = 6050)
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
    @Documentation(name = "Ambulante Leistungen im endoskopischen Bereich ", rank = 6060)
    private List<KGLListEndoscopyAmbulant> _endoscopyAmbulant = new Vector<>();

    public List<KGLListEndoscopyAmbulant> getEndoscopyAmbulant() {
        return _endoscopyAmbulant;
    }

    public void setEndoscopyAmbulant(List<KGLListEndoscopyAmbulant> endoscopyAmbulant) {
        this._endoscopyAmbulant = endoscopyAmbulant;
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
    @Documentation(name = "Intensiv", rank = 13050)
    private List<KGLListIntensivStroke> _intensivStrokes = new Vector<>();

    public List<KGLListIntensivStroke> getIntensivStrokes() {
        return _intensivStrokes;
    }

    public void setIntensivStrokes(List<KGLListIntensivStroke> intensivStrokes) {
        this._intensivStrokes = intensivStrokes;
    }

    @JsonIgnore
    public boolean addIntensive(KGLListIntensivStroke intensivStroke) {
        intensivStroke.setIntensiveType(1);
        if (_intensivStrokes.stream().anyMatch(i -> i.equalsContent(intensivStroke))) {
            return false;
        }
        this._intensivStrokes.add(intensivStroke);
        return true;
    }

    @JsonIgnore
    public boolean addStroke(KGLListIntensivStroke intensivStroke) {
        intensivStroke.setIntensiveType(2);
        if (_intensivStrokes.stream().anyMatch(i -> i.equalsContent(intensivStroke))) {
            return false;
        }
        this._intensivStrokes.add(intensivStroke);
        return true;
    }

    public void clearIntensive() {
        _intensivStrokes.removeIf(intStroke -> intStroke.getIntensiveType() == 1);
    }

    public void clearStroke() {
        _intensivStrokes.removeIf(intStroke -> intStroke.getIntensiveType() == 2);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property List medInfras">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "miBaseInformationId", referencedColumnName = "biID")
    //@Documentation (name = "Personal Intensiv", rank = 13020)
    private List<KGLListMedInfra> _medInfras = new Vector<>();

    public List<KGLListMedInfra> getMedInfras() {
        return _medInfras;
    }

    @Documentation(name = "Kostenstellen", rank = 14020)
    @JsonIgnore
    public List<KGLListMedInfra> getMedInfras170() {
        return _medInfras.stream().filter(c -> c.getCostTypeId() == 170).collect(Collectors.toList());
    }

    @Documentation(name = "Kostenstellen",
            headline = "Ergänzende Angaben zur innerbetrieblichen Leistungsverrechnung (nicht medizinische Infrastruktur)",
            rank = 15000)
    @JsonIgnore
    public List<KGLListMedInfra> getMedInfras180() {
        return _medInfras.stream().filter(c -> c.getCostTypeId() == 180).collect(Collectors.toList());
    }

    public void deleteMedInfraItems(int costTypeId) {
        _medInfras.removeIf(i -> i.getCostTypeId() == costTypeId);
    }

    public void setMedInfras(List<KGLListMedInfra> medInfras) {
        this._medInfras = medInfras;
    }

    @JsonIgnore
    public void addMedInfra(int costTypeId) {
        KGLListMedInfra medInfra = new KGLListMedInfra(_id, costTypeId);
        _medInfras.add(medInfra);
    }

    @JsonIgnore
    public boolean addMedInfra(KGLListMedInfra medInfra) {
        medInfra.setCostTypeId(170);
        if (_medInfras.stream().anyMatch(mi -> mi.equals(medInfra))) {
            return false;
        }
        _medInfras.add(medInfra);
        return true;
    }

    @JsonIgnore
    public boolean addNonMedInfra(KGLListMedInfra medInfra) {
        medInfra.setCostTypeId(180);
        if (_medInfras.stream().anyMatch(mi -> mi.equals(medInfra))) {
            return false;
        }
        _medInfras.add(medInfra);
        return true;
    }

    public void clearMedInfra() {
        _medInfras.removeIf(medInfra -> medInfra.getCostTypeId() == 170);
    }

    public void clearNonMedInfra() {
        _medInfras.removeIf(medInfra -> medInfra.getCostTypeId() == 180);
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property List PersonalAccountings">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "paBaseInformationId", referencedColumnName = "biID")
    @OrderBy(value = "_costTypeId")
    @Documentation(name = "Verfahren Personalkostenverrechnung", rank = 17000,
            headline = "Ergänzende Angaben zur Personalkostenverrechnung")
    private List<KGLPersonalAccounting> _personalAccountings = new Vector<>();

    public List<KGLPersonalAccounting> getPersonalAccountings() {
        return _personalAccountings;
    }

    public void setPersonalAccountings(List<KGLPersonalAccounting> personalAccountings) {
        this._personalAccountings = personalAccountings;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="personalAccountingDescription">
    @Column(name = "biPersonalAccountingDescription")
    @Documentation(name = "Erläuterung Personalkostenverrechnung", rank = 17010)
    private String _personalAccountingDescription = "";

    public String getPersonalAccountingDescription() {
        return _personalAccountingDescription;
    }

    public void setPersonalAccountingDescription(String personalAccountingDescription) {
        this._personalAccountingDescription = personalAccountingDescription;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="neonatLvl">
    @Column(name = "biNeonatLvl")
    @Documentation(name = "Versorgungsstufe des Perinatalzentrums",
            headline = "Ergänzende Angaben zur Neonatologischen Versorgung", rank = 19000)
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
    @Documentation(name = "Leistungsdokumentation für die Kostenartengruppen 2, 4a und 6a", rank = 12020)
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
    @Documentation(name = "Minutenwerte gem. PPR / Alternativverfahren", rank = 12020)
    private List<KGLNormalStationServiceDocumentationMinutes> _normalStationServiceDocumentationMinutes = new Vector<>();

    public List<KGLNormalStationServiceDocumentationMinutes> getNormalStationServiceDocumentationMinutes() {
        return _normalStationServiceDocumentationMinutes;
    }

    public void setNormalStationServiceDocumentationMinutes(
            List<KGLNormalStationServiceDocumentationMinutes> normalStationServiceDocumentationMinutes) {
        this._normalStationServiceDocumentationMinutes = normalStationServiceDocumentationMinutes;
    }

    //<editor-fold defaultstate="collapsed" desc="Property List _costCenterCosts">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cccBaseInformationId", referencedColumnName = "biID")
    @Documentation(name = "Kosten (Normalstation)", rank = 12120)
    private List<KGLListCostCenterCost> _costCenterCosts = new Vector<>();

    public List<KGLListCostCenterCost> getCostCenterCosts() {
        return _costCenterCosts;
    }

    public void setCostCenterCosts(List<KGLListCostCenterCost> cCostCenterCosts) {
        this._costCenterCosts = cCostCenterCosts;
    }

    public void addCostCenterCost(KGLListCostCenterCost item) {
        KGLListCostCenterCost foundItem = ListUtil.findItem(_costCenterCosts, item, (a, b)
                -> a.getCostCenterNumber().equalsIgnoreCase(b.getCostCenterNumber())
                && a.getCostCenterText().equalsIgnoreCase(b.getCostCenterText())
                && a.getDepartmentKey().equalsIgnoreCase(b.getDepartmentKey())
                && a.getDepartmentAssignment().equalsIgnoreCase(b.getDepartmentAssignment()));
        if (foundItem != null) {
            foundItem.copyCostCenterCost(item);
        } else {
            _costCenterCosts.add(item);
        }
    }

    public void clearCostCenterCosts() {
        _costCenterCosts.clear();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property List pkmsAlternatives">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "paBaseInformationId", referencedColumnName = "biID")
    @Documentation(name = "KGLPKMSAlternative", rank = 25041)
    private List<KglPkmsAlternative> _pkmsAlternatives = new Vector<>();

    public List<KglPkmsAlternative> getPkmsAlternatives() {
        return Collections.unmodifiableList(_pkmsAlternatives);
    }

    public void setPkmsAlternatives(List<KglPkmsAlternative> pkmsAlternatives) {
        _pkmsAlternatives = pkmsAlternatives;
    }

    public void addPkmsAlternative() {
        _pkmsAlternatives.add(new KglPkmsAlternative(_id));
    }

    public void removePkmsAlternative(KglPkmsAlternative item) {
        _pkmsAlternatives.remove(item);
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
    @Documentation(name = "Entlassender Standort", rank = 1020)
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

    //<editor-fold defaultstate="collapsed" desc="Property List KGLListCostCenterOpAn">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "oaBaseInformationId", referencedColumnName = "biID")
    private List<KGLListCostCenterOpAn> _costCenterOpAn = new Vector<>();

    public List<KGLListCostCenterOpAn> getCostCenterOpAn() {
        return _costCenterOpAn;
    }

    public void addCostCenterOpAn(KGLListCostCenterOpAn costCenterOpAn) {
        costCenterOpAn.setBaseInformationId(_id);
        _costCenterOpAn.add(costCenterOpAn);
    }

    public void removeCostCenterOpAn(KGLListCostCenterOpAn costCenterOpAn) {
        _costCenterOpAn.remove(costCenterOpAn);
    }

    public void clearCostCenterOpAn() {
        _costCenterOpAn.clear();
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property List RadiologyServices">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "rcBaseInformationId", referencedColumnName = "biID")
    private List<KglRoomCapability> _kglRoomCapability = new Vector<>();

    //Using for JSON
    public List<KglRoomCapability> getKglRoomCapability() {
        return _kglRoomCapability;
    }

    //Using for JSON
    public void setKglRoomCapability(List<KglRoomCapability> kglRoomCapability) {
        this._kglRoomCapability = kglRoomCapability;
    }

    public List<KglRoomCapability> getRoomCapabilities(int costCenterId) {
        return _kglRoomCapability.stream().filter(c -> c.getCostCenterId() == costCenterId).collect(Collectors.toList());
    }

    public void addRoomCapability(int costCenterId) {
        _kglRoomCapability.add(new KglRoomCapability(this, costCenterId));
    }

    public void deleteRoomCapability(KglRoomCapability roomCapability) {
        _kglRoomCapability.remove(roomCapability);
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

    @JsonIgnore
    public boolean isSealed() {
        return getStatus().getId() >= WorkflowStatus.Provided.getId();
    }

}
