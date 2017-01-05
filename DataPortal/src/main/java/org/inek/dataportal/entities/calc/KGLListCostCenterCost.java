/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListCostCenterCost", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLListCostCenterCost.findAll", query = "SELECT k FROM KGLListCostCenterCost k")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccID", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._id = :cccID")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccCostCenter", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._costCenter = :cccCostCenter")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccCostCenterText", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._costCenterText = :cccCostCenterText")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccDepartmentKey", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._departmentKey = :cccDepartmentKey")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccDepartmentAssignment", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._departmentAssignment = :cccDepartmentAssignment")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccBedCnt", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._bedCnt = :cccBedCnt")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccCareDays", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._careDays = :cccCareDays")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccPPRMinutes", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._pprMinutes = :cccPPRMinutes")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccPPRWeight", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._pprWeight = :cccPPRWeight")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccMedicalServiceCnt", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._medicalServiceCnt = :cccMedicalServiceCnt")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccNursingServiceCnt", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._nursingServiceCnt = :cccNursingServiceCnt")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccFunctionalServiceCnt", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._functionalServiceCnt = :cccFunctionalServiceCnt")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccMedicalServiceAmount", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._medicalServiceAmount = :cccMedicalServiceAmount")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccNursingServiceAmount", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._nursingServiceAmount = :cccNursingServiceAmount")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccFunctionalServiceAmount", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._functionalServiceAmount = :cccFunctionalServiceAmount")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccOverheadsMedicine", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._overheadsMedicine = :cccOverheadsMedicine")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccOverheadsMedicalGoods", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._overheadsMedicalGoods = :cccOverheadsMedicalGoods")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccMedicalInfrastructureCost", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._medicalInfrastructureCost = :cccMedicalInfrastructureCost")
    , @NamedQuery(name = "KGLListCostCenterCost.findByCccNonMedicalInfrastructureCost", query = "SELECT k FROM KGLListCostCenterCost k WHERE k._nonMedicalInfrastructureCost = :cccNonMedicalInfrastructureCost")})
public class KGLListCostCenterCost implements Serializable {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccID")
    private Integer _id;
    
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenter">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccCostCenter")
    private int _costCenter;
    
    public int getCostCenter() {
        return _costCenter;
    }

    public void setCostCenter(int costCenter) {
        this._costCenter = costCenter;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterText">
    @Basic(optional = false)
    @NotNull
    @Size(max = 100)
    @Column(name = "cccCostCenterText")
    private String _costCenterText;
    
    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DepartmentKey">
    @Basic(optional = false)
    @NotNull
    @Size(max = 4)
    @Column(name = "cccDepartmentKey")
    private String _departmentKey;
    
    public String getDepartmentKey() {
        return _departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this._departmentKey = departmentKey;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DepartmentAssignment">
    @Basic(optional = false)
    @NotNull
    @Size(max = 4)
    @Column(name = "cccDepartmentAssignment")
    private String _departmentAssignment;
    
    public String getDepartmentAssignment() {
        return _departmentAssignment;
    }

    public void setDepartmentAssignment(String departmentAssignment) {
        this._departmentAssignment = departmentAssignment;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BedCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccBedCnt")
    private int _bedCnt;
    
    public int getBedCnt() {
        return _bedCnt;
    }

    public void setBedCnt(int bedCnt) {
        this._bedCnt = bedCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CareDays">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccCareDays")
    private int _careDays;
    
    public int getCareDays() {
        return _careDays;
    }

    public void setCareDays(int careDays) {
        this._careDays = careDays;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PPRMinutes">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccPPRMinutes")
    private int _pprMinutes;
    
    public int getPPRMinutes() {
        return _pprMinutes;
    }

    public void setPPRMinutes(int pprMinutes) {
        this._pprMinutes = pprMinutes;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PPRWeight">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccPPRWeight")
    private String _pprWeight = "";
    
    public String getPPRWeight() {
        return _pprWeight;
    }

    public void setPPRWeight(String pprWeight) {
        this._pprWeight = pprWeight;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MedicalServiceCnt">
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccMedicalServiceCnt")
    private double _medicalServiceCnt;
    
    public double getMedicalServiceCnt() {
        return _medicalServiceCnt;
    }

    public void setMedicalServiceCnt(double medicalServiceCnt) {
        this._medicalServiceCnt = medicalServiceCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="NursingServiceCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccNursingServiceCnt")
    private double _nursingServiceCnt;
    
    public double getNursingServiceCnt() {
        return _nursingServiceCnt;
    }

    public void setNursingServiceCnt(double nursingServiceCnt) {
        this._nursingServiceCnt = nursingServiceCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="FunctionalServiceCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccFunctionalServiceCnt")
    private double _functionalServiceCnt;
    
    public double getFunctionalServiceCnt() {
        return _functionalServiceCnt;
    }

    public void setFunctionalServiceCnt(double functionalServiceCnt) {
        this._functionalServiceCnt = functionalServiceCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MedicalServiceAmount">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccMedicalServiceAmount")
    private double _medicalServiceAmount;
    
    public double getMedicalServiceAmount() {
        return _medicalServiceAmount;
    }

    public void setMedicalServiceAmount(double medicalServiceAmount) {
        this._medicalServiceAmount = medicalServiceAmount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="NursingServiceAmount">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccNursingServiceAmount")
    private double _nursingServiceAmount;
    
    public double getNursingServiceAmount() {
        return _nursingServiceAmount;
    }

    public void setNursingServiceAmount(double nursingServiceAmount) {
        this._nursingServiceAmount = nursingServiceAmount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="FunctionalServiceAmount">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccFunctionalServiceAmount")
    private double _functionalServiceAmount;
    
    public double getFunctionalServiceAmount() {
        return _functionalServiceAmount;
    }

    public void setFunctionalServiceAmount(double functionalServiceAmount) {
        this._functionalServiceAmount = functionalServiceAmount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OverheadsMedicine">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccOverheadsMedicine")
    private double _overheadsMedicine;
    
    public double getOverheadsMedicine() {
        return _overheadsMedicine;
    }

    public void setOverheadsMedicine(double overheadsMedicine) {
        this._overheadsMedicine = overheadsMedicine;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OverheadsMedicalGoods">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccOverheadsMedicalGoods")
    private double _overheadsMedicalGoods;
    
    public double getOverheadsMedicalGoods() {
        return _overheadsMedicalGoods;
    }

    public void setOverheadsMedicalGoods(double overheadsMedicalGoods) {
        this._overheadsMedicalGoods = overheadsMedicalGoods;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MedicalInfrastructureCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccMedicalInfrastructureCost")
    private double _medicalInfrastructureCost;
    
    public double getMedicalInfrastructureCost() {
        return _medicalInfrastructureCost;
    }

    public void setMedicalInfrastructureCost(double medicalInfrastructureCost) {
        this._medicalInfrastructureCost = medicalInfrastructureCost;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="NonMedicalInfrastructureCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cccNonMedicalInfrastructureCost")
    private double _nonMedicalInfrastructureCost;
    
    public double getNonMedicalInfrastructureCost() {
        return _nonMedicalInfrastructureCost;
    }

    public void setNonMedicalInfrastructureCost(double nonMedicalInfrastructureCost) {
        this._nonMedicalInfrastructureCost = nonMedicalInfrastructureCost;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="cccBaseInformationID">
    @JoinColumn(name = "cccBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics _baseInformation;

    public DrgCalcBasics getBaseInformationID() {
        return _baseInformation;
    }

    public void setBaseInformationID(DrgCalcBasics baseInformationID) {
        this._baseInformation = baseInformationID;
    }
    // </editor-fold>

    public KGLListCostCenterCost() {
    }

    public KGLListCostCenterCost(Integer cccID) {
        this._id = cccID;
    }

    public KGLListCostCenterCost(Integer cccID, int cccCostCenter, String cccCostCenterText, String cccDepartmentKey, String cccDepartmentAssignment, int cccBedCnt, int cccCareDays, int cccPPRMinutes, String cccPPRWeight, double cccMedicalServiceCnt, double cccNursingServiceCnt, double cccFunctionalServiceCnt, double cccMedicalServiceAmount, double cccNursingServiceAmount, double cccFunctionalServiceAmount, double cccOverheadsMedicine, double cccOverheadsMedicalGoods, double cccMedicalInfrastructureCost, double cccNonMedicalInfrastructureCost) {
        this._id = cccID;
        this._costCenter = cccCostCenter;
        this._costCenterText = cccCostCenterText;
        this._departmentKey = cccDepartmentKey;
        this._departmentAssignment = cccDepartmentAssignment;
        this._bedCnt = cccBedCnt;
        this._careDays = cccCareDays;
        this._pprMinutes = cccPPRMinutes;
        this._pprWeight = cccPPRWeight;
        this._medicalServiceCnt = cccMedicalServiceCnt;
        this._nursingServiceCnt = cccNursingServiceCnt;
        this._functionalServiceCnt = cccFunctionalServiceCnt;
        this._medicalServiceAmount = cccMedicalServiceAmount;
        this._nursingServiceAmount = cccNursingServiceAmount;
        this._functionalServiceAmount = cccFunctionalServiceAmount;
        this._overheadsMedicine = cccOverheadsMedicine;
        this._overheadsMedicalGoods = cccOverheadsMedicalGoods;
        this._medicalInfrastructureCost = cccMedicalInfrastructureCost;
        this._nonMedicalInfrastructureCost = cccNonMedicalInfrastructureCost;
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
        if (!(object instanceof KGLListCostCenterCost)) {
            return false;
        }
        KGLListCostCenterCost other = (KGLListCostCenterCost) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListCostCenterCost[ cccID=" + _id + " ]";
    }
    
}
