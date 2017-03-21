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
import java.util.Objects;
import java.util.Vector;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "KGPBaseInformation", schema = "calc")
public class PeppCalcBasics implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "biId")
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
    @Documentation(key = "lblYearData")
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

    // <editor-fold defaultstate="collapsed" desc="Property StatusId / Status">
    @Column(name = "biStatusId")
    private int _statusId;

    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        _statusId = statusId;
    }

    @Documentation(key = "lblWorkstate", rank = 10, headline = "Diese Druckansicht steht Ihnen in Kürze zur Verfügung.")
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

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

    // <editor-fold defaultstate="collapsed" desc="Property _caseInStationCnt">
    @Column(name = "biCaseInStationCnt")
    @Documentation(name = "Fälle vollstationär")
    private int _caseInStationCnt;

    public int getCaseInStationCnt() {
        return _caseInStationCnt;
    }

    public void setCaseInStationCnt(int caseInStationCnt) {
        this._caseInStationCnt = caseInStationCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _caseInStationCntPsy">
    @Column(name = "biCaseInStationCntPsy")
    @Documentation(name = "Fälle vollstationär Psychosomatik")
    private int _caseInStationCntPsy;

    public int getCaseInStationCntPsy() {
        return _caseInStationCntPsy;
    }

    public void setCaseInStationCntPsy(int caseInStationCntPsy) {
        this._caseInStationCntPsy = caseInStationCntPsy;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _careDaysInStationCnt">
    @Column(name = "biCareDaysInStationCnt")
    @Documentation(name = "Pflegetage vollstationär")
    private int _careDaysInStationCnt;

    public int getCareDaysInStationCnt() {
        return _careDaysInStationCnt;
    }

    public void setCareDaysInStationCnt(int careDaysInStationCnt) {
        this._careDaysInStationCnt = careDaysInStationCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _careDaysInStationCntPsy">
    @Column(name = "biCareDaysInStationCntPsy")
    @Documentation(name = "Pflegetage vollstationär Psychosomatik")
    private int _careDaysInStationCntPsy;

    public int getCareDaysInStationCntPsy() {
        return _careDaysInStationCntPsy;
    }

    public void setCareDaysInStationCntPsy(int careDaysInStationCntPsy) {
        this._careDaysInStationCntPsy = careDaysInStationCntPsy;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _casePartialStationCnt">
    @Column(name = "biCasePartialStationCnt")
    @Documentation(name = "Fälle teilstationär")
    private int _casePartialStationCnt;

    public int getCasePartialStationCnt() {
        return _casePartialStationCnt;
    }

    public void setCasePartialStationCnt(int casePartialStationCnt) {
        this._casePartialStationCnt = casePartialStationCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _casePartialStationCntPsy">
    @Column(name = "biCasePartialStationCntPsy")
    @Documentation(name = "Fälle teilstationär Psychosomatik")
    private int _casePartialStationCntPsy;

    public int getCasePartialStationCntPsy() {
        return _casePartialStationCntPsy;
    }

    public void setCasePartialStationCntPsy(int casePartialStationCntPsy) {
        this._casePartialStationCntPsy = casePartialStationCntPsy;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _daysPartialStation">
    @Column(name = "biDaysPartialStation")
    @Documentation(name = "Berechnungstage teilstationär")
    private int _daysPartialStation;

    public int getDaysPartialStation() {
        return _daysPartialStation;
    }

    public void setDaysPartialStation(int daysPartialStation) {
        this._daysPartialStation = daysPartialStation;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _daysPartialStationPsy">
    @Column(name = "biDaysPartialStationPsy")
    @Documentation(name = "Berechnungstage teilstationär Psychosomatik")
    private int _daysPartialStationPsy;

    public int getDaysPartialStationPsy() {
        return _daysPartialStationPsy;
    }

    public void setDaysPartialStationPsy(int daysPartialStationPsy) {
        this._daysPartialStationPsy = daysPartialStationPsy;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _patientEscort">
    @Column(name = "biPatientEscort")
    @Documentation(name = "Begleitpersonen")
    private int _patientEscort;

    public int getPatientEscort() {
        return _patientEscort;
    }

    public void setPatientEscort(int patientEscort) {
        this._patientEscort = patientEscort;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _preStation">
    @Column(name = "biPreStation")
    @Documentation(name = "Fälle rein vorstationär")
    private int _preStation;

    public int getPreStation() {
        return _preStation;
    }

    public void setPreStation(int preStation) {
        this._preStation = preStation;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _beds">
    @Column(name = "biBeds")
    @Documentation(name = "Anzahl Betten (PSY)")
    private int _beds;

    public int getBeds() {
        return _beds;
    }

    public void setBeds(int beds) {
        this._beds = beds;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _partialCnt">
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

    // <editor-fold defaultstate="collapsed" desc="Property _sumCalcCost">
    @Column(name = "biSumCalcCost")
    @Documentation(key = "lblSumCalcCost")
    private int _sumCalcCost;

    public int getSumCalcCost() {
        return _sumCalcCost;
    }

    public void setSumCalcCost(int sumCalcCost) {
        this._sumCalcCost = sumCalcCost;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _locationCnt">
    @Column(name = "biLocationCnt")
    @Documentation(name = "Anzahl Standorte")
    private int _locationCnt;

    public int getLocationCnt() {
        return _locationCnt;
    }

    public void setLocationCnt(int locationCnt) {
        this._locationCnt = locationCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _difLocationSupply">
    @Column(name = "biDifLocationSupply")
    @Documentation(name = "Differenzierter Versorgungsauftrag")
    private boolean _difLocationSupply;

    public boolean isDifLocationSupply() {
        return _difLocationSupply;
    }

    public void setDifLocationSupply(boolean difLocationSupply) {
        this._difLocationSupply = difLocationSupply;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _medicineCostMapping">
    @Column(name = "biMedicineCostMapping")
    private boolean _medicineCostMapping;

    public boolean isMedicineCostMapping() {
        return _medicineCostMapping;
    }

    public void setMedicineCostMapping(boolean medicineCostMapping) {
        this._medicineCostMapping = medicineCostMapping;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _courtPlacement">
    @Column(name = "biCourtPlacement")
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
    private boolean _additionalDataAllocation;

    public boolean isAdditionalDataAllocation() {
        return _additionalDataAllocation;
    }

    public void setAdditionalDataAllocation(boolean additionalDataAllocation) {
        this._additionalDataAllocation = additionalDataAllocation;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _bimAll">
    @Column(name = "biBimAll")
    private boolean _bimAll;

    public boolean isBimAll() {
        return _bimAll;
    }

    public void setBimAll(boolean bimAll) {
        this._bimAll = bimAll;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _intensiveExceptionalPermission">
    @Column(name = "biIntensiveExceptionalPermission")
    private String _intensiveExceptionalPermission = "";

    public String getIntensiveExceptionalPermission() {
        return _intensiveExceptionalPermission;
    }

    public void setIntensiveExceptionalPermission(String intensiveExceptionalPermission) {
        this._intensiveExceptionalPermission = intensiveExceptionalPermission;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _intensiveCriteriaBullets">
    @Column(name = "biIntensiveCriteriaBullets")
    private String _intensiveCriteriaBullets = "";

    public String getIntensiveCriteriaBullets() {
        return _intensiveCriteriaBullets;
    }

    public void setIntensiveCriteriaBullets(String intensiveCriteriaBullets) {
        this._intensiveCriteriaBullets = intensiveCriteriaBullets;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _intensiveMethodBullets">
    @Column(name = "biIntensiveMethodBullets")
    private String _intensiveMethodBullets = "";

    public String getIntensiveMethodBullets() {
        return _intensiveMethodBullets;
    }

    public void setIntensiveMethodBullets(String intensiveMethodBullets) {
        this._intensiveMethodBullets = intensiveMethodBullets;
    }
    // </editor-fold>

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
    
    // <editor-fold defaultstate="collapsed" desc="Property _otherMethodMedInfra">
    @Column(name = "biOtherMethodMedInfra")
    private String _otherMethodMedInfra = "";

    public String getOtherMethodMedInfra() {
        return _otherMethodMedInfra;
    }

    public void setOtherMethodMedInfra(String otherMethodMedInfra) {
        this._otherMethodMedInfra = otherMethodMedInfra;
    }
    // </editor-fold>

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
    private List<KGPListMedInfra> _kgpMedInfraList = new Vector<>();

    public List<KGPListMedInfra> getKgpMedInfraList() {
        return _kgpMedInfraList;
    }

    public void setKgpMedInfraList(List<KGPListMedInfra> kgpMedInfraList) {
        this._kgpMedInfraList = kgpMedInfraList;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _personalAccountingDescription">
    @Column(name = "biPersonalAccountingDescription")
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
    @OrderBy(value = "_costTypeId")
    private List<KGPPersonalAccounting> _personalAccountings = new Vector<>();

    public List<KGPPersonalAccounting> getPersonalAccountings() {
        return _personalAccountings;
    }

    public void setPersonalAccountings(List<KGPPersonalAccounting> kgpPersonalAccountingList) {
        this._personalAccountings = kgpPersonalAccountingList;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List _serviceProvisions">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "spBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "spBaseInformationId", referencedColumnName = "biID")
    @OrderBy(value = "_sequence")
    private List<KGPListServiceProvision> _serviceProvisions = new Vector<>();

    public List<KGPListServiceProvision> getServiceProvisions() {
        return _serviceProvisions;
    }

    public void setServiceProvisions(List<KGPListServiceProvision> serviceProvisions) {
        this._serviceProvisions = serviceProvisions;
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

    public void setCostCenters(List<KGPListCostCenter> costCenters) {
        this._costCenters = costCenters;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List _stationServiceCosts">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "sscBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sscBaseInformationId", referencedColumnName = "biID")
    private List<KGPListStationServiceCost> _stationServiceCosts = new Vector<>();

    public List<KGPListStationServiceCost> getStationServiceCosts() {
        return _stationServiceCosts;
    }

    public void setStationServiceCosts(List<KGPListStationServiceCost> kgpStationServiceCostList) {
        this._stationServiceCosts = kgpStationServiceCostList;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List _therapies">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "thBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "thBaseInformationId", referencedColumnName = "biID")
    private List<KGPListTherapy> _therapies = new Vector<>();

    public List<KGPListTherapy> getTherapies() {
        return _therapies;
    }

    public void setTherapies(List<KGPListTherapy> kgpTherapyList) {
        this._therapies = kgpTherapyList;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List _kgpStationDepartmentList">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "seBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "saBaseInformationId", referencedColumnName = "biID")
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
    private List<KGPListRadiologyLaboratory> _radiologyLaboratories = new Vector<>();

    public List<KGPListRadiologyLaboratory> getRadiologyLaboratories() {
        return _radiologyLaboratories;
    }

    public void setRadiologyLaboratories(List<KGPListRadiologyLaboratory> radiologyLaboratories) {
        this._radiologyLaboratories = radiologyLaboratories;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List _kgpDelimitationFactList">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "dfBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dfBaseInformationId", referencedColumnName = "biID")
    @OrderBy(value = "_contentTextId")
    private List<KGPListDelimitationFact> _delimitationFacts = new Vector<>();

    public List<KGPListDelimitationFact> getDelimitationFacts() {
        return _delimitationFacts;
    }

    public void setDelimitationFacts(List<KGPListDelimitationFact> delimitationFacts) {
        this._delimitationFacts = delimitationFacts;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List _kgpDocumentsList">
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "doBaseInformationId")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "doBaseInformationId", referencedColumnName = "biID")
    private List<KGPDocuments> _kgpDocumentsList = new Vector<>();

    public List<KGPDocuments> getKgpDocumentsList() {
        return _kgpDocumentsList;
    }

    public void setKgpDocumentsList(List<KGPDocuments> kgpDocumentsList) {
        this._kgpDocumentsList = kgpDocumentsList;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property List locations">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lBaseInformationId", referencedColumnName = "biID")
    private List<KGPListLocation> _locations = new Vector<>();

    public List<KGPListLocation> getLocations() {
        return _locations;
    }

    public void setLocations(List<KGPListLocation> locations) {
        this._locations = locations;
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

}
