/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListIntensivStroke", schema = "calc")
@XmlRootElement
public class KGLListIntensivStroke implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "isID")
    private int _id = -1;
    
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="IntensiveType">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isIntensiveType")
    private int _intensiveType;
    
    public int getIntensiveType() {
        return _intensiveType;
    }

    public void setIntensiveType(int intensiveType) {
        this._intensiveType = intensiveType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterID">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isCostCenterID")
    private int _costCenterID;
    
    public int getCostCenterID() {
        return _costCenterID;
    }

    public void setCostCenterID(int costCenterID) {
        this._costCenterID = costCenterID;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterText">
    @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "isCostCenterText")
    private String _costCenterText = "";
    
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
    @Column(name = "isDepartmentKey")
    private String _departmentKey = "";
    
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
    @Size(max = 50)
    @Column(name = "isDepartmentAssignment")
    private String _departmentAssignment = "";
    
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
    @Column(name = "isBedCnt")
    private int _bedCnt;
    
    public int getBedCnt() {
        return _bedCnt;
    }

    public void setBedCnt(int bedCnt) {
        this._bedCnt = bedCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CaseCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isCaseCnt")
    private int _caseCnt;
    
    public int getCaseCnt() {
        return _caseCnt;
    }

    public void setCaseCnt(int caseCnt) {
        this._caseCnt = caseCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OPS8980">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isOPS8980")
    private boolean _ops8980;
    
    public boolean getOps8980() {
        return _ops8980;
    }

    public void setOps8980(boolean ops8980) {
        this._ops8980 = ops8980;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OPS898f">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isOPS898f")
    private boolean _ops898f;
    
    public boolean getOps898f() {
        return _ops898f;
    }

    public void setOps898f(boolean ops898f) {
        this._ops898f = ops898f;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="OPS8981">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isOPS8981")
    private boolean _ops8981;
    
    public boolean getOps8981() {
        return _ops8981;
    }

    public void setOps8981(boolean ops8981) {
        this._ops8981 = ops8981;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OPS898b">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isOPS898b")
    private boolean _ops898b;
    
    public boolean getOps898b() {
        return _ops898b;
    }

    public void setOps898b(boolean ops898b) {
        this._ops898b = ops898b;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MinimumPeriod">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isMinimumCriteriaPeriod")
    @Size(max = 100)
    private String _minimumCriteriaPeriod;
    
    public String getMinimumCriteriaPeriod() {
        return _minimumCriteriaPeriod;
    }

    public void setMinimumCriteriaPeriod(String minimumPeriod) {
        this._minimumCriteriaPeriod = minimumPeriod;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="IntensivHoursWeighted">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isIntensivHoursWeighted")
    private int _intensivHoursWeighted;
    
    public int getIntensivHoursWeighted() {
        return _intensivHoursWeighted;
    }

    public void setIntensivHoursWeighted(int intensivHoursWeighted) {
        this._intensivHoursWeighted = intensivHoursWeighted;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="IntensivHoursNotweighted">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isIntensivHoursNotweighted")
    private int _intensivHoursNotweighted;
    
    public int getIntensivHoursNotweighted() {
        return _intensivHoursNotweighted;
    }

    public void setIntensivHoursNotweighted(int intensivHoursNotweighted) {
        this._intensivHoursNotweighted = intensivHoursNotweighted;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="WeightMinimum">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isWeightMinimum")
    private double _weightMinimum;
    
    public double getWeightMinimum() {
        return _weightMinimum;
    }

    public void setWeightMinimum(double weightMinimum) {
        this._weightMinimum = weightMinimum;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="WeightMaximum">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isWeightMaximum")
    private double _weightMaximum;
    
    public double getWeightMaximum() {
        return _weightMaximum;
    }

    public void setWeightMaximum(double weightMaximum) {
        this._weightMaximum = weightMaximum;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="WeightDescription">
    @Basic(optional = false)
    @NotNull
    @Size(max = 300)
    @Column(name = "isWeightDescription")
    private String _weightDescription = "";
    
    public String getWeightDescription() {
        return _weightDescription;
    }

    public void setWeightDescription(String weightDescription) {
        this._weightDescription = weightDescription;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property medicalServiceCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isMedicalServiceCnt")
    private double _medicalServiceCnt;

    public double getMedicalServiceCnt() {
        return _medicalServiceCnt;
    }

    public void setMedicalServiceCnt(double medicalServiceCnt) {
        this._medicalServiceCnt = medicalServiceCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property nursingServiceCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isNursingServiceCnt")
    private double _nursingServiceCnt;

    public double getNursingServiceCnt() {
        return _nursingServiceCnt;
    }

    public void setNursingServiceCnt(double nursingServiceCnt) {
        this._nursingServiceCnt = nursingServiceCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property functionalServiceCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isFunctionalServiceCnt")
    private double _functionalServiceCnt;

    public double getFunctionalServiceCnt() {
        return _functionalServiceCnt;
    }

    public void setFunctionalServiceCnt(double functionalServiceCnt) {
        this._functionalServiceCnt = functionalServiceCnt;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property medicalServiceCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isMedicalServiceCost")
    private int _medicalServiceCost;

    public int getMedicalServiceCost() {
        return _medicalServiceCost;
    }

    public void setMedicalServiceCost(int medicalServiceCost) {
        this._medicalServiceCost = medicalServiceCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property nursingServiceCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isNursingServiceCost")
    private int _nursingServiceCost;

    public int getNursingServiceCost() {
        return _nursingServiceCost;
    }

    public void setNursingServiceCost(int nursingServiceCost) {
        this._nursingServiceCost = nursingServiceCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property functionalServiceCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isFunctionalServiceCost")
    private int _functionalServiceCost;

    public int getFunctionalServiceCost() {
        return _functionalServiceCost;
    }

    public void setFunctionalServiceCost(int functionalServiceCost) {
        this._functionalServiceCost = functionalServiceCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property overheadsMedicine">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isOverheadsMedicine")
    private int _overheadsMedicine;

    public int getOverheadsMedicine() {
        return _overheadsMedicine;
    }

    public void setOverheadsMedicine(int overheadsMedicine) {
        this._overheadsMedicine = overheadsMedicine;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property overheadMedicalGoods">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isOverheadsMedicalGoods")
    private int _overheadMedicalGoods;

    public int getOverheadMedicalGoods() {
        return _overheadMedicalGoods;
    }

    public void setOverheadMedicalGoods(int overheadMedicalGoods) {
        this._overheadMedicalGoods = overheadMedicalGoods;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property medicalInfrastructureCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isMedicalInfrastructureCost")
    private int _medicalInfrastructureCost;

    public int getMedicalInfrastructureCost() {
        return _medicalInfrastructureCost;
    }

    public void setMedicalInfrastructureCost(int medicalInfrastructureCost) {
        this._medicalInfrastructureCost = medicalInfrastructureCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property nonMedicalInfrastructureCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "isNonMedicalInfrastructureCost")
    private int _nonMedicalInfrastructureCost;

    public int getNonMedicalInfrastructureCost() {
        return _nonMedicalInfrastructureCost;
    }

    public void setNonMedicalInfrastructureCost(int nonMedicalInfrastructureCost) {
        this._nonMedicalInfrastructureCost = nonMedicalInfrastructureCost;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BaseInformationID">
//    @JoinColumn(name = "isBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "isBaseInformationID")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    // </editor-fold>
    
    public KGLListIntensivStroke() {
    }

    public KGLListIntensivStroke(int isID) {
        this._id = isID;
    }

    public KGLListIntensivStroke(int intensiveType, int costCenterID, int bedCnt, int caseCnt, boolean ops8980, boolean ops898f, boolean ops8981, boolean ops898b, String minimumCriteriaPeriod, int intensivHoursWeighted, int intensivHoursNotweighted, double weightMinimum, double weightMaximum, int medicalServiceCost, int nursingServiceCost, int functionalServiceCost, int overheadsMedicine, int overheadMedicalGoods, int medicalInfrastructureCost, int nonMedicalInfrastructureCost, int baseInformationId) {
        this._intensiveType = intensiveType;
        this._costCenterID = costCenterID;
        this._bedCnt = bedCnt;
        this._caseCnt = caseCnt;
        this._ops8980 = ops8980;
        this._ops898f = ops898f;
        this._ops8981 = ops8981;
        this._ops898b = ops898b;
        this._minimumCriteriaPeriod = minimumCriteriaPeriod;
        this._intensivHoursWeighted = intensivHoursWeighted;
        this._intensivHoursNotweighted = intensivHoursNotweighted;
        this._weightMinimum = weightMinimum;
        this._weightMaximum = weightMaximum;
        this._medicalServiceCost = medicalServiceCost;
        this._nursingServiceCost = nursingServiceCost;
        this._functionalServiceCost = functionalServiceCost;
        this._overheadsMedicine = overheadsMedicine;
        this._overheadMedicalGoods = overheadMedicalGoods;
        this._medicalInfrastructureCost = medicalInfrastructureCost;
        this._nonMedicalInfrastructureCost = nonMedicalInfrastructureCost;
        this._baseInformationId = baseInformationId;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this._id;
        
        if (this._id != -1) return hash;
        
        hash = 79 * hash + this._intensiveType;
        hash = 79 * hash + this._costCenterID;
        hash = 79 * hash + Objects.hashCode(this._costCenterText);
        hash = 79 * hash + Objects.hashCode(this._departmentKey);
        hash = 79 * hash + Objects.hashCode(this._departmentAssignment);
        hash = 79 * hash + this._bedCnt;
        hash = 79 * hash + this._caseCnt;
        hash = 79 * hash + (this._ops8980 ? 1 : 0);
        hash = 79 * hash + (this._ops898f ? 1 : 0);
        hash = 79 * hash + (this._ops8981 ? 1 : 0);
        hash = 79 * hash + (this._ops898b ? 1 : 0);
        hash = 79 * hash + Objects.hashCode(this._minimumCriteriaPeriod);
        hash = 79 * hash + this._intensivHoursWeighted;
        hash = 79 * hash + this._intensivHoursNotweighted;
        hash = 79 * hash + (int) (Double.doubleToLongBits(this._weightMinimum) ^ (Double.doubleToLongBits(this._weightMinimum) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this._weightMaximum) ^ (Double.doubleToLongBits(this._weightMaximum) >>> 32));
        hash = 79 * hash + Objects.hashCode(this._weightDescription);
        hash = 79 * hash + this._medicalServiceCost;
        hash = 79 * hash + this._nursingServiceCost;
        hash = 79 * hash + this._functionalServiceCost;
        hash = 79 * hash + this._overheadsMedicine;
        hash = 79 * hash + this._overheadMedicalGoods;
        hash = 79 * hash + this._medicalInfrastructureCost;
        hash = 79 * hash + this._nonMedicalInfrastructureCost;
        hash = 79 * hash + Objects.hashCode(this._baseInformationId);
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
        final KGLListIntensivStroke other = (KGLListIntensivStroke) obj;
        
        if (this._id != -1 && this._id == other._id) return true;
        
        if (this._id != other._id) {
            return false;
        }
        if (this._intensiveType != other._intensiveType) {
            return false;
        }
        if (this._costCenterID != other._costCenterID) {
            return false;
        }
        if (this._bedCnt != other._bedCnt) {
            return false;
        }
        if (this._caseCnt != other._caseCnt) {
            return false;
        }
        if (this._ops8980 != other._ops8980) {
            return false;
        }
        if (this._ops898f != other._ops898f) {
            return false;
        }
        if (this._ops8981 != other._ops8981) {
            return false;
        }
        if (this._ops898b != other._ops898b) {
            return false;
        }
        if (this._intensivHoursWeighted != other._intensivHoursWeighted) {
            return false;
        }
        if (this._intensivHoursNotweighted != other._intensivHoursNotweighted) {
            return false;
        }
        if (Double.doubleToLongBits(this._weightMinimum) != Double.doubleToLongBits(other._weightMinimum)) {
            return false;
        }
        if (Double.doubleToLongBits(this._weightMaximum) != Double.doubleToLongBits(other._weightMaximum)) {
            return false;
        }
        if (this._medicalServiceCost != other._medicalServiceCost) {
            return false;
        }
        if (this._nursingServiceCost != other._nursingServiceCost) {
            return false;
        }
        if (this._functionalServiceCost != other._functionalServiceCost) {
            return false;
        }
        if (this._overheadsMedicine != other._overheadsMedicine) {
            return false;
        }
        if (this._overheadMedicalGoods != other._overheadMedicalGoods) {
            return false;
        }
        if (this._medicalInfrastructureCost != other._medicalInfrastructureCost) {
            return false;
        }
        if (this._nonMedicalInfrastructureCost != other._nonMedicalInfrastructureCost) {
            return false;
        }
        if (!Objects.equals(this._costCenterText, other._costCenterText)) {
            return false;
        }
        if (!Objects.equals(this._departmentKey, other._departmentKey)) {
            return false;
        }
        if (!Objects.equals(this._departmentAssignment, other._departmentAssignment)) {
            return false;
        }
        if (!Objects.equals(this._minimumCriteriaPeriod, other._minimumCriteriaPeriod)) {
            return false;
        }
        if (!Objects.equals(this._weightDescription, other._weightDescription)) {
            return false;
        }
        if (!Objects.equals(this._baseInformationId, other._baseInformationId)) {
            return false;
        }
        return true;
    }
    
    
    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListIntensivStroke[ isID=" + _id + " ]";
    }
    //</editor-fold>
    
}
