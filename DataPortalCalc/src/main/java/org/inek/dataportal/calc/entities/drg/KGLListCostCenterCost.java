/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.drg;

import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;
import org.inek.dataportal.common.utils.IgnoreOnCompare;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListCostCenterCost", schema = "calc")
public class KGLListCostCenterCost implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    public KGLListCostCenterCost() {
    }

    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cccID", updatable = false, nullable = false)
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenter">
    @Column(name = "cccCostCenterID")
    @Documentation(name = "Zugeordnete Kostenstellengruppe", rank = 10)
    private int _costCenterId;

    public int getCostCenter() {
        return _costCenterId;
    }

    public void setCostCenter(int costCenterId) {
        this._costCenterId = costCenterId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterText">
    @Column(name = "cccCostCenterText")
    @Documentation(name = "Name der Kostenstelle", rank = 20)
    private String _costCenterText = "";

    @Size(max = 200, message = "Für Kostenstellenbezeichnung sind max. {max} Zeichen zulässig.")
    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DepartmentKey">
    @Column(name = "cccDepartmentKey")
    @Documentation(name = "FAB-Schlüssel 301:", rank = 30)
    private String _departmentKey = "";

    @Size(max = 4, message = "Für FAB-Schlüssel sind max. {max} Zeichen zulässig.")
    public String getDepartmentKey() {
        return _departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this._departmentKey = departmentKey;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DepartmentAssignment">
    @Column(name = "cccDepartmentAssignment")
    @Documentation(name = "Belegung/FAB", rank = 40)
    private String _departmentAssignment = "";

    @Size(max = 100, message = "Für Belegung/FAB sind max. {max} Zeichen zulässig.")
    public String getDepartmentAssignment() {
        return _departmentAssignment;
    }

    public void setDepartmentAssignment(String departmentAssignment) {
        this._departmentAssignment = departmentAssignment;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BedCnt">
    @Column(name = "cccBedCnt")
    @Documentation(name = "Anzahl Betten", rank = 50)
    private int _bedCnt;

    public int getBedCnt() {
        return _bedCnt;
    }

    public void setBedCnt(int bedCnt) {
        this._bedCnt = bedCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CareDays">
    @Column(name = "cccCareDays")
    @Documentation(name = "Pfelegetage:", rank = 60)
    private int _careDays;

    public int getCareDays() {
        return _careDays;
    }

    public void setCareDays(int careDays) {
        this._careDays = careDays;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PPRMinutes">
    @Column(name = "cccPPRMinutes")
    @Documentation(name = "PPR-Minuten", rank = 70)
    private int _pprMinutes;

    public int getPprMinutes() {
        return _pprMinutes;
    }

    public void setPprMinutes(int pprMinutes) {
        this._pprMinutes = pprMinutes;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PPRWeight">
    @Column(name = "cccPPRWeight")
    @Documentation(name = "zusätzliche Gewichtung PPR", rank = 80)
    private int _pprWeight;

    public int getPprWeight() {
        return _pprWeight;
    }

    public void setPprWeight(int pprWeight) {
        this._pprWeight = pprWeight;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MedicalServiceCnt">
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cccMedicalServiceCnt")
    @Documentation(name = "Anz. VK ÄD", rank = 90)
    private double _medicalServiceCnt;

    public double getMedicalServiceCnt() {
        return _medicalServiceCnt;
    }

    public void setMedicalServiceCnt(double medicalServiceCnt) {
        this._medicalServiceCnt = medicalServiceCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="NursingServiceCnt">
    @Column(name = "cccNursingServiceCnt")
    @Documentation(name = "Anz. VK PD", rank = 100)
    private double _nursingServiceCnt;

    public double getNursingServiceCnt() {
        return _nursingServiceCnt;
    }

    public void setNursingServiceCnt(double nursingServiceCnt) {
        this._nursingServiceCnt = nursingServiceCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="FunctionalServiceCnt">
    @Column(name = "cccFunctionalServiceCnt")
    @Documentation(name = "Anz. VK FD", rank = 110)
    private double _functionalServiceCnt;

    public double getFunctionalServiceCnt() {
        return _functionalServiceCnt;
    }

    public void setFunctionalServiceCnt(double functionalServiceCnt) {
        this._functionalServiceCnt = functionalServiceCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MedicalServiceAmount">
    @Column(name = "cccMedicalServiceAmount")
    @Documentation(name = "Kosten ÄD", rank = 120)
    private int _medicalServiceAmount;

    public int getMedicalServiceAmount() {
        return _medicalServiceAmount;
    }

    public void setMedicalServiceAmount(int medicalServiceAmount) {
        this._medicalServiceAmount = medicalServiceAmount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="NursingServiceAmount">
    @Column(name = "cccNursingServiceAmount")
    @Documentation(name = "Kosten PD", rank = 130)
    private int _nursingServiceAmount;

    public int getNursingServiceAmount() {
        return _nursingServiceAmount;
    }

    public void setNursingServiceAmount(int nursingServiceAmount) {
        this._nursingServiceAmount = nursingServiceAmount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="FunctionalServiceAmount">
    @Column(name = "cccFunctionalServiceAmount")
    @Documentation(name = "Kosten FD", rank = 140)
    private int _functionalServiceAmount;

    public int getFunctionalServiceAmount() {
        return _functionalServiceAmount;
    }

    public void setFunctionalServiceAmount(int functionalServiceAmount) {
        this._functionalServiceAmount = functionalServiceAmount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OverheadsMedicine">
    @Column(name = "cccOverheadsMedicine")
    @Documentation(name = "Gemeinkosten Arzneimittel", rank = 150)
    private int _overheadsMedicine;

    public int getOverheadsMedicine() {
        return _overheadsMedicine;
    }

    public void setOverheadsMedicine(int overheadsMedicine) {
        this._overheadsMedicine = overheadsMedicine;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OverheadsMedicalGoods">
    @Column(name = "cccOverheadsMedicalGoods")
    @Documentation(name = "Gemeinkosten Sachbedarf", rank = 160)
    private int _overheadsMedicalGoods;

    public int getOverheadsMedicalGoods() {
        return _overheadsMedicalGoods;
    }

    public void setOverheadsMedicalGoods(int overheadsMedicalGoods) {
        this._overheadsMedicalGoods = overheadsMedicalGoods;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MedicalInfrastructureCost">
    @Column(name = "cccMedicalInfrastructureCost")
    @Documentation(name = "Kosten MedInfra", rank = 170)
    private int _medicalInfrastructureCost;

    public int getMedicalInfrastructureCost() {
        return _medicalInfrastructureCost;
    }

    public void setMedicalInfrastructureCost(int medicalInfrastructureCost) {
        this._medicalInfrastructureCost = medicalInfrastructureCost;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="NonMedicalInfrastructureCost">
    @Column(name = "cccNonMedicalInfrastructureCost")
    @Documentation(name = "Kosten nicht MedInfra", rank = 180)
    private int _nonMedicalInfrastructureCost;

    public int getNonMedicalInfrastructureCost() {
        return _nonMedicalInfrastructureCost;
    }

    public void setNonMedicalInfrastructureCost(int nonMedicalInfrastructureCost) {
        this._nonMedicalInfrastructureCost = nonMedicalInfrastructureCost;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="cccBaseInformationId">
//    @JoinColumn(name = "cccBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "cccBaseInformationId")
    private int _baseInformationId = -1;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="costTypeId">
    @Column(name = "cccCostCenterNumber")
    @Documentation(name = "Nummer der Kostenstelle", rank = 190)
    private String _costCenterNumber = "";

    @Size(max = 20, message = "Für Kostenstellennummer sind max. {max} Zeichen zulässig.")
    public String getCostCenterNumber() {
        return _costCenterNumber;
    }

    public void setCostCenterNumber(String costCenterNumber) {
        this._costCenterNumber = costCenterNumber;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="cccAcceptPlausibility">
    @Column(name = "cccAcceptPlausibility")
    private Boolean _acceptPlausibility = false;

    public Boolean getAcceptPlausibility() {
        return _acceptPlausibility;
    }

    public void setAcceptPlausibility(Boolean acceptPlausibility) {
        this._acceptPlausibility = acceptPlausibility;
    }

    // </editor-fold>

    @Column(name = "cccPriorID")
    private int _priorId = 0;

    public int getPriorId() {
        return _priorId;
    }

    public void setPriorId(int priorId) {
        this._priorId = priorId;
    }

    @Transient
    @IgnoreOnCompare
    private KGLListCostCenterCost _prior;

    public KGLListCostCenterCost getPrior() {
        return _prior;
    }

    public void setPrior(KGLListCostCenterCost prior) {
        this._prior = prior;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 59 * hash + this._costCenterId;
        hash = 59 * hash + Objects.hashCode(this._costCenterText);
        hash = 59 * hash + Objects.hashCode(this._departmentKey);
        hash = 59 * hash + Objects.hashCode(this._departmentAssignment);
        hash = 59 * hash + this._bedCnt;
        hash = 59 * hash + this._careDays;
        hash = 59 * hash + this._pprMinutes;
        hash = 59 * hash + Objects.hashCode(this._pprWeight);
        hash = 59 * hash + (int) (Double.doubleToLongBits(this._medicalServiceCnt) ^ (Double.doubleToLongBits(this._medicalServiceCnt) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this._nursingServiceCnt) ^ (Double.doubleToLongBits(this._nursingServiceCnt) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this._functionalServiceCnt) ^ (Double.doubleToLongBits(this._functionalServiceCnt) >>> 32));
        hash = 59 * hash + this._medicalServiceAmount;
        hash = 59 * hash + this._nursingServiceAmount;
        hash = 59 * hash + this._functionalServiceAmount;
        hash = 59 * hash + this._overheadsMedicine;
        hash = 59 * hash + this._overheadsMedicalGoods;
        hash = 59 * hash + this._medicalInfrastructureCost;
        hash = 59 * hash + this._nonMedicalInfrastructureCost;
        hash = 59 * hash + this._baseInformationId;
        return hash;
    }

    @SuppressWarnings("CyclomaticComplexity")
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
        final KGLListCostCenterCost other = (KGLListCostCenterCost) obj;

        if (this._id != -1 && this._id == other._id) {
            return _id == other._id;
        }

        return _costCenterId == other._costCenterId
                && _bedCnt == other._bedCnt
                && _careDays == other._careDays
                && _pprMinutes == other._pprMinutes
                && Double.doubleToLongBits(this._medicalServiceCnt) == Double.doubleToLongBits(other._medicalServiceCnt)
                && Double.doubleToLongBits(this._nursingServiceCnt) == Double.doubleToLongBits(other._nursingServiceCnt)
                && Double.doubleToLongBits(this._functionalServiceCnt) == Double.doubleToLongBits(other._functionalServiceCnt)
                && _medicalServiceAmount == other._medicalServiceAmount
                && _nursingServiceAmount == other._nursingServiceAmount
                && _functionalServiceAmount == other._functionalServiceAmount
                && _overheadsMedicine == other._overheadsMedicine
                && _overheadsMedicalGoods == other._overheadsMedicalGoods
                && _medicalInfrastructureCost == other._medicalInfrastructureCost
                && _nonMedicalInfrastructureCost == other._nonMedicalInfrastructureCost
                && _baseInformationId == other._baseInformationId
                && Objects.equals(this._costCenterText, other._costCenterText)
                && Objects.equals(this._departmentKey, other._departmentKey)
                && Objects.equals(this._departmentAssignment, other._departmentAssignment)
                && Objects.equals(this._pprWeight, other._pprWeight)
                && _costCenterNumber.equals(other._costCenterNumber);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListCostCenterCost[ cccID=" + _id + " ]";
    }
    //</editor-fold>

    public void copyCostCenterCost(KGLListCostCenterCost item) {
        this._costCenterId = item._costCenterId;
        this._costCenterText = item._costCenterText;
        this._departmentKey = item._departmentKey;
        this._departmentAssignment = item._departmentAssignment;
        this._bedCnt = item._bedCnt;
        this._careDays = item._careDays;
        this._pprMinutes = item._pprMinutes;
        this._pprWeight = item._pprWeight;
        this._medicalServiceCnt = item._medicalServiceCnt;
        this._nursingServiceCnt = item._nursingServiceCnt;
        this._functionalServiceCnt = item._functionalServiceCnt;
        this._medicalServiceAmount = item._medicalServiceAmount;
        this._nursingServiceAmount = item._nursingServiceAmount;
        this._functionalServiceAmount = item._functionalServiceAmount;
        this._overheadsMedicine = item._overheadsMedicine;
        this._overheadsMedicalGoods = item._overheadsMedicalGoods;
        this._medicalInfrastructureCost = item._medicalInfrastructureCost;
        this._nonMedicalInfrastructureCost = item._nonMedicalInfrastructureCost;
        this._baseInformationId = item._baseInformationId;
        this._costCenterNumber = item._costCenterNumber;
    }
}
