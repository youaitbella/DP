package org.inek.dataportal.calc.entities.psy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.calc.ListUtil;
import org.inek.dataportal.common.data.iface.StatusEntity;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.Documentation;
import org.inek.dataportal.common.utils.IgnoreOnCompare;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "KGPBaseInformation", schema = "calc")
@SuppressWarnings("MethodCount")
public class PeppCalcBasics implements Serializable, StatusEntity {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "biId", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Version">
    @Column(name = "biVersion")
    @Version
    @IgnoreOnCompare
    private int _version;

    @JsonIgnore
    public int getVersion(){
        return _version;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DataYear">
    @Column(name = "biDataYear")
    @Documentation(key = "lblYearData", headline = "Allgemeine Informationen", rank = 20)
    private int _dataYear;

    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        this._dataYear = dataYear;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IK">
    @Column(name = "biIk")
    @Documentation(key = "lblIK")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
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

    // <editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "biLastChanged")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Documentation(name = "Stand")
    private Date _lastChanged = Calendar.getInstance().getTime();

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        _lastChanged = lastChanged;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accountIdLastChange">
    @Column(name = "biLastChangedBy")
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

    // <editor-fold defaultstate="collapsed" desc="Property StatusId / Status">
    @Column(name = "biStatusId")
    @IgnoreOnCompare
    private int _statusId;

    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        _statusId = statusId;
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _correctionNote">
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Fälle vollstationär">
    @Column(name = "biCaseInStationCnt")
    @Documentation(name = "Fälle vollstationär", rank = 1020)
    private int _caseInStationCnt;

    public int getCaseInStationCnt() {
        return _caseInStationCnt;
    }

    public void setCaseInStationCnt(int caseInStationCnt) {
        this._caseInStationCnt = caseInStationCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Fälle vollstationär Psychosomatik">
    @Column(name = "biCaseInStationCntPsy")
    @Documentation(name = "Fälle vollstationär Psychosomatik", rank = 1021)
    private int _caseInStationCntPsy;

    public int getCaseInStationCntPsy() {
        return _caseInStationCntPsy;
    }

    public void setCaseInStationCntPsy(int caseInStationCntPsy) {
        this._caseInStationCntPsy = caseInStationCntPsy;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Pflegetage vollstationär">
    @Column(name = "biCareDaysInStationCnt")
    @Documentation(name = "Pflegetage vollstationär", rank = 1030)
    private int _careDaysInStationCnt;

    public int getCareDaysInStationCnt() {
        return _careDaysInStationCnt;
    }

    public void setCareDaysInStationCnt(int careDaysInStationCnt) {
        this._careDaysInStationCnt = careDaysInStationCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Pflegetage vollstationär Psychosomatik">
    @Column(name = "biCareDaysInStationCntPsy")
    @Documentation(name = "Pflegetage vollstationär Psychosomatik", rank = 1031)
    private int _careDaysInStationCntPsy;

    public int getCareDaysInStationCntPsy() {
        return _careDaysInStationCntPsy;
    }

    public void setCareDaysInStationCntPsy(int careDaysInStationCntPsy) {
        this._careDaysInStationCntPsy = careDaysInStationCntPsy;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Fälle teilstationär">
    @Column(name = "biCasePartialStationCnt")
    @Documentation(name = "Fälle teilstationär", rank = 1040)
    private int _casePartialStationCnt;

    public int getCasePartialStationCnt() {
        return _casePartialStationCnt;
    }

    public void setCasePartialStationCnt(int casePartialStationCnt) {
        this._casePartialStationCnt = casePartialStationCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Fälle teilstationär Psychosomatik">
    @Column(name = "biCasePartialStationCntPsy")
    @Documentation(name = "Fälle teilstationär Psychosomatik", rank = 1041)
    private int _casePartialStationCntPsy;

    public int getCasePartialStationCntPsy() {
        return _casePartialStationCntPsy;
    }

    public void setCasePartialStationCntPsy(int casePartialStationCntPsy) {
        this._casePartialStationCntPsy = casePartialStationCntPsy;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Berechnungstage teilstationär">
    @Column(name = "biDaysPartialStation")
    @Documentation(name = "Berechnungstage teilstationär", rank = 1050)
    private int _daysPartialStation;

    public int getDaysPartialStation() {
        return _daysPartialStation;
    }

    public void setDaysPartialStation(int daysPartialStation) {
        this._daysPartialStation = daysPartialStation;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Berechnungstage teilstationär Psychosomatik">
    @Column(name = "biDaysPartialStationPsy")
    @Documentation(name = "Berechnungstage teilstationär Psychosomatik", rank = 1051)
    private int _daysPartialStationPsy;

    public int getDaysPartialStationPsy() {
        return _daysPartialStationPsy;
    }

    public void setDaysPartialStationPsy(int daysPartialStationPsy) {
        this._daysPartialStationPsy = daysPartialStationPsy;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Begleitpersonen">
    @Column(name = "biPatientEscort")
    @Documentation(name = "Begleitpersonen", rank = 1060)
    private int _patientEscort;

    public int getPatientEscort() {
        return _patientEscort;
    }

    public void setPatientEscort(int patientEscort) {
        this._patientEscort = patientEscort;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Fälle rein vorstationär">
    @Column(name = "biPreStation")
    @Documentation(name = "Fälle rein vorstationär", rank = 1070)
    private int _preStation;

    public int getPreStation() {
        return _preStation;
    }

    public void setPreStation(int preStation) {
        this._preStation = preStation;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Anzahl Betten (PSY)">
    @Column(name = "biBeds")
    @Documentation(name = "Anzahl Betten (PSY)", rank = 1080)
    private int _beds;

    public int getBeds() {
        return _beds;
    }

    public void setBeds(int beds) {
        this._beds = beds;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Anzahl teilstationäre Plätze (PSY)">
    @Column(name = "biPartialCnt")
    @Documentation(name = "Anzahl teilstationäre Plätze (PSY)")
    private int _partialCnt;

    public int getPartialCnt() {
        return _partialCnt;
    }

    public void setPartialCnt(int partialCnt) {
        this._partialCnt = partialCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Erklärung zur Kalkulationsgrundlage">
    @Column(name = "biSumCalcCost")
    @Documentation(key = "lblSumCalcCost", rank = 1010, headline = "Erklärung zur Kalkulationsgrundlage")
    private int _sumCalcCost;

    public int getSumCalcCost() {
        return _sumCalcCost;
    }

    public void setSumCalcCost(int sumCalcCost) {
        this._sumCalcCost = sumCalcCost;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Anzahl Standorte">
    @Column(name = "biLocationCnt")
    @Documentation(name = "Anzahl Standorte", rank = 1090)
    private int _locationCnt;

    public int getLocationCnt() {
        return _locationCnt;
    }

    public void setLocationCnt(int locationCnt) {
        this._locationCnt = locationCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Differenzierter Versorgungsauftrag">
    @Column(name = "biDifLocationSupply")
    @Documentation(name = "Differenzierter Versorgungsauftrag", rank = 1100)
    private boolean _difLocationSupply;

    public boolean isDifLocationSupply() {
        return _difLocationSupply;
    }

    public void setDifLocationSupply(boolean difLocationSupply) {
        this._difLocationSupply = difLocationSupply;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Wird eine Einzelkostenzuordnung für Arzneimittel vorgenommen?">
    @Column(name = "biMedicineCostMapping")
    @Documentation(name = "Wird eine Einzelkostenzuordnung für Arzneimittel vorgenommen?", rank = 1120)
    private boolean _medicineCostMapping;

    public boolean isMedicineCostMapping() {
        return _medicineCostMapping;
    }

    public void setMedicineCostMapping(boolean medicineCostMapping) {
        this._medicineCostMapping = medicineCostMapping;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="In der Einrichtung wurden im aktuellen Datenjahr Patienten gerichtlich untergebracht">
    @Column(name = "biCourtPlacement")
    @Documentation(name = "In der Einrichtung wurden im aktuellen Datenjahr Patienten gerichtlich untergebracht", rank = 1130)
    private boolean _courtPlacement;

    public boolean isCourtPlacement() {
        return _courtPlacement;
    }

    public void setCourtPlacement(boolean courtPlacement) {
        this._courtPlacement = courtPlacement;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _additionalDataAllocation">
    @Column(name = "biAdditionalDataAllocation")
    @Documentation(name = "Ist es Ihnen möglich, die Daten in der von der ergänzenden Datenbereitstellung vorgesehenen "
            + "Form aufzubereiten und zu übermitteln?", rank = 1140)
    private boolean _additionalDataAllocation;

    public boolean isAdditionalDataAllocation() {
        return _additionalDataAllocation;
    }

    public void setAdditionalDataAllocation(boolean additionalDataAllocation) {
        this._additionalDataAllocation = additionalDataAllocation;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Ergänzende Angaben zum stationären Bereich">
    @Column(name = "biBimAll")
    @Documentation(name = "Leistungsdokumentation gem. BIM", rank = 9010, headline = "Ergänzende Angaben zum stationären Bereich")
    private boolean _bimAll;

    public boolean isBimAll() {
        return _bimAll;
    }

    public void setBimAll(boolean bimAll) {
        this._bimAll = bimAll;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Ausnahmegenehmigung für das Datenjahr (Kommentar)">
    @Column(name = "biIntensiveExceptionalPermission")
    @Documentation(name = "Ausnahmegenehmigung für das Datenjahr (Kommentar)", rank = 9020)
    private String _intensiveExceptionalPermission = "";

    public String getIntensiveExceptionalPermission() {
        return _intensiveExceptionalPermission;
    }

    public void setIntensiveExceptionalPermission(String intensiveExceptionalPermission) {
        this._intensiveExceptionalPermission = intensiveExceptionalPermission;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Stichwortartig Kriterien Pflegetage">
    @Column(name = "biIntensiveCriteriaBullets")
    @Documentation(name = "Stichwortartig Kriterien Pflegetage", rank = 9030)
    private String _intensiveCriteriaBullets = "";

    public String getIntensiveCriteriaBullets() {
        return _intensiveCriteriaBullets;
    }

    public void setIntensiveCriteriaBullets(String intensiveCriteriaBullets) {
        this._intensiveCriteriaBullets = intensiveCriteriaBullets;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Stichwortartig Verfahren zur Verrechnung der Kosten">
    @Column(name = "biIntensiveMethodBullets")
    @Documentation(name = "Stichwortartig Verfahren zur Verrechnung der Kosten", rank = 9040)
    private String _intensiveMethodBullets = "";

    public String getIntensiveMethodBullets() {
        return _intensiveMethodBullets;
    }

    public void setIntensiveMethodBullets(String intensiveMethodBullets) {
        this._intensiveMethodBullets = intensiveMethodBullets;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Gewähltes Verfahren bei Durchführung der IBLV">
    @Column(name = "biIBLVMethodMedInfra")
    @Documentation(name = "Gewähltes Verfahren bei Durchführung der IBLV", rank = 10010,
            translateValue = "0= ;1=Gleichungsverfahren;2=Stufenleiterverfahren;3=Anbauverfahren;4=Sonstige Vorgehensweise")
    private int _iblvMethodMedInfra;

    public int getIblvMethodMedInfra() {
        return _iblvMethodMedInfra;
    }

    public void setIblvMethodMedInfra(int iblvMethodMedInfra) {
        this._iblvMethodMedInfra = iblvMethodMedInfra;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Gewähltes Verfahren bei Durchführung der IBLV">
    @Column(name = "biOtherMethodMedInfra")
    @Documentation(name = "Gewähltes Verfahren bei Durchführung der IBLV", rank = 11010,
            translateValue = "0= ;1=Gleichungsverfahren;2=Stufenleiterverfahren;3=Anbauverfahren;4=Sonstige Vorgehensweise")
    private String _otherMethodMedInfra = "";

    public String getOtherMethodMedInfra() {
        return _otherMethodMedInfra;
    }

    public void setOtherMethodMedInfra(String otherMethodMedInfra) {
        this._otherMethodMedInfra = otherMethodMedInfra;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Stationsäquivalent">
    @Column(name = "biStationEquivalentCnt")
    @Documentation(name = "")
    private int _stationEquivalentCnt;

    public int getStationEquivalentCnt() {
        return _stationEquivalentCnt;
    }

    public void setStationEquivalentCnt(int stationEquivalentCnt) {
        this._stationEquivalentCnt = stationEquivalentCnt;
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Property StationEquivalentCntPsy">
    @Column(name = "biStationEquivalentCntPsy")
    private int _stationEquivalentCntPsy;

    public int getStationEquivalentCntPsy() {
        return _stationEquivalentCntPsy;
    }

    public void setStationEquivalentCntPsy(int stationEquivalentCntPsy) {
        this._stationEquivalentCntPsy = stationEquivalentCntPsy;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DaysStationEquivalentCnt">
    @Column(name = "biDaysStationEquivalentCnt")
    private int _daysStationEquivalentCnt;

    public int getDaysStationEquivalentCnt() {
        return _daysStationEquivalentCnt;
    }

    public void setDaysStationEquivalentCnt(int daysStationEquivalentCnt) {
        this._daysStationEquivalentCnt = daysStationEquivalentCnt;
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Property DaysStationEquivalentCntPsy">
    @Column(name = "biDaysStationEquivalentCntPsy")
    private int _daysStationEquivalentCntPsy;

    public int getDaysStationEquivalentCntPsy() {
        return _daysStationEquivalentCntPsy;
    }

    public void setDaysStationEquivalentCntPsy(int daysStationEquivalentCntPsy) {
        this._daysStationEquivalentCntPsy = daysStationEquivalentCntPsy;
    }
    // </editor-fold>

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

    //<editor-fold defaultstate="collapsed" desc="otherMethodMedInfraDesc">
    @Transient
    private boolean _hasDescMedicalInfra = false;

    public boolean isDescMedicalInfra() {
        return _hasDescMedicalInfra;
    }

    public void setDescMedicalInfra(boolean _hasDesc) {
        this._hasDescMedicalInfra = _hasDesc;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="otherMethodNonMedInfraDesc">
    @Transient
    private boolean _hasDescNonMedicalInfra = false;

    public boolean isDescNonMedicalInfra() {
        return _hasDescNonMedicalInfra;
    }

    public void setDescNonMedicalInfra(boolean _hasDesc) {
        this._hasDescNonMedicalInfra = _hasDesc;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List _kgpMedInfraList">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "miBaseInformationId", referencedColumnName = "biID")
    //@Documentation(name = "Unbekannte Liste (A)", rank = 20000)
    private List<KgpListMedInfra> _kgpMedInfraList = new Vector<>();

    public List<KgpListMedInfra> getKgpMedInfraList() {
        return _kgpMedInfraList;
    }

    @Documentation(name = "Verrechnungsschlüssel und Kostenvolumen der Kostenstellen der medizinischen Infrastruktur",
            headline = "Ergänzende Angaben zur innerbetrieblichen Leistungsverrechnung (medizinische Infrastruktur)", rank = 10020)
    @JsonIgnore
    public List<KgpListMedInfra> getCostCenter170() {
        return _kgpMedInfraList.stream().filter(c -> c.getCostTypeId()== 170).collect(Collectors.toList());
    }

    @Documentation(name = "Verrechnungsschlüssel und Kostenvolumen der Kostenstellen der nicht medizinischen Infrastruktur",
            headline = "Ergänzende Angaben zur innerbetrieblichen Leistungsverrechnung (nicht medizinische Infrastruktur)", rank = 11020)
    @JsonIgnore
    public List<KgpListMedInfra> getCostCenter180() {
        return _kgpMedInfraList.stream().filter(c -> c.getCostTypeId()== 180).collect(Collectors.toList());
    }

    public void deleteKgpMedInfraList(int costTypeId){
        _kgpMedInfraList.removeIf(c -> c.getCostTypeId()== costTypeId);
    }

    public void setKgpMedInfraList(List<KgpListMedInfra> kgpMedInfraList) {
        this._kgpMedInfraList = kgpMedInfraList;
    }

    public void addMedInfraItem(KgpListMedInfra item) {
        KgpListMedInfra foundItem = ListUtil.findItem(_kgpMedInfraList, item, (a, b) ->
                a.getCostCenterNumber().equals(b.getCostCenterNumber())
                        && a.getCostCenterText().equals(b.getCostCenterText())
                        && a.getKeyUsed().equals(b.getKeyUsed()));
        if (foundItem != null) {
            foundItem.setAmount(item.getAmount());
        } else {
            _kgpMedInfraList.add(item);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Erläuterung bzgl. Datengrundlage, Vorgehen und Kostenvolumen">
    @Column(name = "biPersonalAccountingDescription")
    @Documentation(name = "Erläuterung bzgl. Datengrundlage, Vorgehen und Kostenvolumen", rank = 12030)
    private String _personalAccountingDescription = "";

    public String getPersonalAccountingDescription() {
        return _personalAccountingDescription;
    }

    public void setPersonalAccountingDescription(String personalAccountingDescription) {
        this._personalAccountingDescription = personalAccountingDescription;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List _personalAccountings">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "paBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "paBaseInformationId", referencedColumnName = "biID")
    @Documentation(name = "Gewähltes Verfahren für die Durchführung der Personalkostenverrechnung", rank = 12010,
            headline = "Ergänzende Angaben zur Personalkostenverrechnung")
    @OrderBy(value = "_costTypeId")
    private List<KGPPersonalAccounting> _personalAccountings = new ArrayList<>();

    public List<KGPPersonalAccounting> getPersonalAccountings() {
        return _personalAccountings;
    }

    public void setPersonalAccountings(List<KGPPersonalAccounting> kgpPersonalAccountingList) {
        this._personalAccountings = kgpPersonalAccountingList;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Leistungen">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "spBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "spBaseInformationId", referencedColumnName = "biID")
    @Documentation(name = "Leistungen", rank = 2000, headline = "(Externe) Leistungserbringung / Fremdvergabe")
    @OrderBy(value = "_sequence")
    private List<KGPListServiceProvision> _serviceProvisions = new Vector<>();

    public List<KGPListServiceProvision> getServiceProvisions() {
        return _serviceProvisions;
        
    }

    public void setServiceProvisions(List<KGPListServiceProvision> serviceProvisions) {
        _serviceProvisions = serviceProvisions;
    }

    public void removeEmptyServiceProvisions(){
        _serviceProvisions.removeIf(p -> p.getServiceProvisionTypeId() < 0  && p.getDomain().isEmpty());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List _costCenters">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "ccBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ccBaseInformationId", referencedColumnName = "biID")
    private List<KGPListCostCenter> _costCenters = new Vector<>();

    public List<KGPListCostCenter> getCostCenters() {
        return _costCenters;
    }

    @Documentation(name = "Kostenstellen",headline = "Kostenstellengruppe 11 (Diagnostische Bereiche)", rank = 7000)
    @JsonIgnore
    public List<KGPListCostCenter> getCostCenter11() {
        return _costCenters.stream().filter(c -> c.getCostCenterId() == 11).collect(Collectors.toList());
    }

    @Documentation(name = "Kostenstellen",headline = "Kostenstellengruppe 12 (Therapeutische Verfahren)", rank = 8000)
    @JsonIgnore
    public List<KGPListCostCenter> getCostCenter12() {
        return _costCenters.stream().filter(c -> c.getCostCenterId() == 12).collect(Collectors.toList());
    }

    @Documentation(name = "Kostenstellen",headline = "Kostenstellengruppe 13 (Patientenaufnahme)", rank = 9000)
    @JsonIgnore
    public List<KGPListCostCenter> getCostCenter13() {
        return _costCenters.stream().filter(c -> c.getCostCenterId() == 13).collect(Collectors.toList());
    }

    public void setCostCenters(List<KGPListCostCenter> costCenters) {
        this._costCenters = costCenters;
    }

    public void addCostCenter(KGPListCostCenter item) {
        KGPListCostCenter foundItem = ListUtil.findItem(_costCenters, item, (a, b) ->
                a.getCostCenterId() == b.getCostCenterId() &&
                        a.getCostCenterNumber().equals(b.getCostCenterNumber()) &&
                        a.getCostCenterText().equalsIgnoreCase(b.getCostCenterText()));
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

    public void deleteCostCenters() {
        _costCenters.clear();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List _stationServiceCosts">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "sscBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sscBaseInformationId", referencedColumnName = "biID")
    @Documentation(name = "Stationen", rank = 9050)
    private List<KGPListStationServiceCost> _stationServiceCosts = new Vector<>();

    public List<KGPListStationServiceCost> getStationServiceCosts() {
        return _stationServiceCosts;
    }

    public void setStationServiceCosts(List<KGPListStationServiceCost> kgpStationServiceCostList) {
        this._stationServiceCosts = kgpStationServiceCostList;
    }

    public void clearStationServiceCosts(){
        _stationServiceCosts.clear();
    }

    public void addStationServiceCost(KGPListStationServiceCost item) {
        KGPListStationServiceCost foundItem = ListUtil.findItem(_stationServiceCosts, item, (a, b) ->
                        a.getCostCenterNumber().equalsIgnoreCase(b.getCostCenterNumber())
                                && a.getStation().equalsIgnoreCase(b.getStation()));

        if (foundItem != null) {
            foundItem.copyStationServiceCost(item);
        } else {
            _stationServiceCosts.add(item);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List _therapies">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "thBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "thBaseInformationId", referencedColumnName = "biID")
    @Documentation(name = "Kostenstellen", rank = 3010, headline = "Therapeutischer Bereich (Kostenstellengruppen 23 bis 26)")
    private List<KGPListTherapy> _therapies = new Vector<>();

    public List<KGPListTherapy> getTherapies() {
        return _therapies;
    }

    public void setTherapies(List<KGPListTherapy> kgpTherapyList) {
        this._therapies = kgpTherapyList;
    }

    public void addTherapy(KGPListTherapy item) {
        KGPListTherapy foundItem = ListUtil.findItem(_therapies, item, (a, b) ->
                        a.getCostCenterId() == b.getCostCenterId()
                                && a.getCostCenterText().equalsIgnoreCase(b.getCostCenterText())
                                && a.getKeyUsed().equalsIgnoreCase(b.getKeyUsed()));

        if (foundItem != null) {
            foundItem.copyTherapy(item);
        } else {
            _therapies.add(item);
        }
    }

    public void clearTheapies() {
        _therapies.clear();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List _kgpStationDepartmentList">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "seBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "saBaseInformationId", referencedColumnName = "biID")
    @Documentation(name = "ListStationAlternative", rank = 20005)
    private List<KGPListStationAlternative> _kgpStationDepartmentList = new Vector<>();

    public List<KGPListStationAlternative> getKgpStationDepartmentList() {
        return _kgpStationDepartmentList;
    }

    public void setKgpStationDepartmentList(List<KGPListStationAlternative> kgpStationDepartmentList) {
        this._kgpStationDepartmentList = kgpStationDepartmentList;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List _radiologyLaboratories">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "rlBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "rlBaseInformationId", referencedColumnName = "biID")
   // @Documentation(name = "Kostenstellen", rank = 5010, headline = "Kostenstellengruppe 9 (Radiologie)")
    private List<KGPListRadiologyLaboratory> _radiologyLaboratories = new Vector<>();

    public List<KGPListRadiologyLaboratory> getRadiologyLaboratories() {
        return _radiologyLaboratories;
    }

    @Documentation(name = "Kostenstellen",headline = "Kostenstellengruppe 9 (Radiologie)", rank = 5000)
    @JsonIgnore
    public List<KGPListRadiologyLaboratory> getRadiologyLaboratories9() {
        return _radiologyLaboratories.stream().filter(c -> c.getCostCenterId() == 9).collect(Collectors.toList());
    }

    @Documentation(name = "Kostenstellen",headline = "Kostenstellengruppe 10 (Laboratorien)", rank = 6000)
    @JsonIgnore
    public List<KGPListRadiologyLaboratory> getRadiologyLaboratories10() {
        return _radiologyLaboratories.stream().filter(c -> c.getCostCenterId() == 10).collect(Collectors.toList());
    }

    public void setRadiologyLaboratories(List<KGPListRadiologyLaboratory> radiologyLaboratories) {
        this._radiologyLaboratories = radiologyLaboratories;
    }

    public void addRadiologyLaboratory(KGPListRadiologyLaboratory item) {
        KGPListRadiologyLaboratory foundItem = ListUtil.findItem(_radiologyLaboratories, item, (a, b) ->
                a.getCostCenterId() == b.getCostCenterId()
                        && a.getCostCenterNumber().equals(b.getCostCenterNumber())
                        && a.getCostCenterText().equals(b.getCostCenterText()));
        if (foundItem != null) {
            foundItem.setDescription(item.getDescription());
            foundItem.setServiceDocType(item.getServiceDocType());
        } else {
            _radiologyLaboratories.add(item);
        }
    }

    public void clearRadiologyLaboratory(int costCenter) {
        Iterator<KGPListRadiologyLaboratory> it = _radiologyLaboratories.iterator();
        while (it.hasNext()) {
            KGPListRadiologyLaboratory next = it.next();
            if (next.getCostCenterId() == costCenter) {
                it.remove();
            }
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List _kgpDelimitationFactList">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "dfBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dfBaseInformationId", referencedColumnName = "biID")
    @OrderBy(value = "_contentTextId")
    @Documentation(name = "Berücksichtigte Abgrenzungstatbestände", rank = 1200)
    private List<KGPListDelimitationFact> _delimitationFacts = new Vector<>();

    public List<KGPListDelimitationFact> getDelimitationFacts() {
        return _delimitationFacts;
    }

    @Documentation(name = "Berücksichtigte Abgrenzungstatbestände", rank = 1120)
    @JsonIgnore
    public List<KGPListDelimitationFact> getDelimitationFactsInUse() {
        return _delimitationFacts.stream().filter(i -> i.isUsed() == true).collect(Collectors.toList());
    }

    public void setDelimitationFacts(List<KGPListDelimitationFact> delimitationFacts) {
        this._delimitationFacts = delimitationFacts;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Liste Dokumente">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "doBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "doBaseInformationId", referencedColumnName = "biID")
    @Documentation(name = "Liste Dokumente", rank = 20000)
    private List<KGPDocuments> _kgpDocumentsList = new Vector<>();

    public List<KGPDocuments> getKgpDocumentsList() {
        return _kgpDocumentsList;
    }

    public void setKgpDocumentsList(List<KGPDocuments> kgpDocumentsList) {
        this._kgpDocumentsList = kgpDocumentsList;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Liste Standorte">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lBaseInformationId", referencedColumnName = "biID")
    private List<KGPListLocation> _locations = new Vector<>();
    @Documentation(name = "Liste Standorte", rank = 20001)
    public List<KGPListLocation> getLocations() {
        return _locations;
    }

    public void setLocations(List<KGPListLocation> locations) {
        this._locations = locations;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Übersicht Personal">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "opBaseInformationId", referencedColumnName = "biId")
    @Documentation(name = "Übersicht Personal", headline = "Übersicht Personal", rank = 10)
    private List<KGPListOverviewPersonal> _overviewPersonal = new Vector<>();

    public List<KGPListOverviewPersonal> getOverviewPersonals() {
        return _overviewPersonal.stream()
                .sorted((e1, e2) -> Integer.compare(e1.getOverviewPersonalType().getSequence(),
                        e2.getOverviewPersonalType().getSequence()))
                .collect(Collectors.toList());
    }

    public void addOverviewPersonal(KGPListOverviewPersonal overviewPersonal){
        this._overviewPersonal.add(overviewPersonal);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 17 * hash + this._dataYear;
        hash = 17 * hash + this._ik;
        hash = 17 * hash + this._accountId;
        hash = 17 * hash + this._statusId;
        hash = 17 * hash + Objects.hashCode(this._lastChanged);
        return hash;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
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
        final PeppCalcBasics other = (PeppCalcBasics) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._dataYear != other._dataYear) {
            return false;
        }
        if (this._ik != other._ik) {
            return false;
        }
        if (this._accountId != other._accountId) {
            return false;
        }
        if (this._statusId != other._statusId) {
            return false;
        }
        if (!Objects.equals(this._lastChanged, other._lastChanged)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.BasicsPepp[ id=" + _id + " ]";
    }
    // </editor-fold>

    @PrePersist
    @PreUpdate
    public void tagModifiedDate() {
        _lastChanged = Calendar.getInstance().getTime();
    }

    public PeppCalcBasics() {
    }

    @JsonIgnore
    public boolean isSealed() {
        return getStatus().getId() >= WorkflowStatus.Provided.getId();
    }
}
