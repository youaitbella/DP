/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.inek.dataportal.entities.calc.iface.BaseIdValue;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListIntensivStroke", schema = "calc")
@XmlRootElement
public class KGLListIntensivStroke implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "isID", updatable = false, nullable = false)
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

    // <editor-fold defaultstate="collapsed" desc="IntensiveType">
    @Column(name = "isIntensiveType")
    private int _intensiveType;

    public int getIntensiveType() {
        return _intensiveType;
    }

    public void setIntensiveType(int intensiveType) {
        this._intensiveType = intensiveType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterText">
    @Column(name = "isCostCenterText")
    @Documentation (name = "Intensivstation", rank = 10)
    private String _costCenterText = "";

    @Size(max = 300, message = "Für Bezeichnung Intensivstation sind max. {max} Zeichen zulässig.")
    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="DepartmentAssignment">
    @Column(name = "isDepartmentAssignment")
    @Documentation (name = "FAB", rank = 20)
    private String _departmentAssignment = "";

    @Size(max = 200, message = "Für FAB sind max. {max} Zeichen zulässig.")
    public String getDepartmentAssignment() {
        return _departmentAssignment;
    }

    public void setDepartmentAssignment(String departmentAssignment) {
        this._departmentAssignment = departmentAssignment;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BedCnt">
    @Column(name = "isBedCnt")
    @Documentation (name = "Anzahl Betten", rank = 30)
    private int _bedCnt;

    public int getBedCnt() {
        return _bedCnt;
    }

    public void setBedCnt(int bedCnt) {
        this._bedCnt = bedCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CaseCnt">
    @Column(name = "isCaseCnt")
    @Documentation (name = "Anzahl Fälle", rank = 40)
    private int _caseCnt;

    public int getCaseCnt() {
        return _caseCnt;
    }

    public void setCaseCnt(int caseCnt) {
        this._caseCnt = caseCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OPS8980">
    @Column(name = "isOPS8980")
    @Documentation (name = "Mindestmerkmale OPS 8-980 erfüllt", omitOnValues = "false", rank = 50)
    private boolean _ops8980;

    public boolean getOps8980() {
        return _ops8980;
    }

    public void setOps8980(boolean ops8980) {
        this._ops8980 = ops8980;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OPS898f">
    @Column(name = "isOPS898f")
    @Documentation (name = "Mindestmerkmale OPS 8-98f erfüllt", omitOnValues = "false", rank = 60)
    private boolean _ops898f;

    public boolean getOps898f() {
        return _ops898f;
    }

    public void setOps898f(boolean ops898f) {
        this._ops898f = ops898f;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OPS8981">
    @Column(name = "isOPS8981")
    @Documentation (name = "Mindestmerkmale OPS 8-981 erfüllt", omitOnValues = "false", rank = 70)
    private boolean _ops8981;

    public boolean getOps8981() {
        return _ops8981;
    }

    public void setOps8981(boolean ops8981) {
        this._ops8981 = ops8981;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OPS898b">
    @Column(name = "isOPS898b")
    @Documentation (name = "Mindestmerkmale OPS 8-98b erfüllt", omitOnValues = "false", rank = 80)
    private boolean _ops898b;

    public boolean getOps898b() {
        return _ops898b;
    }

    public void setOps898b(boolean ops898b) {
        this._ops898b = ops898b;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MinimumPeriod">
    @Column(name = "isMinimumCriteriaPeriod")
    @Documentation (name = "Mindestmerkmale nur erfüllt im Zeitabschnitt:", rank = 90)
    private String _minimumCriteriaPeriod = "";

    @Size(max = 300, message = "Für Mindestmerkmale sind max. {max} Zeichen zulässig.")
    public String getMinimumCriteriaPeriod() {
        return _minimumCriteriaPeriod;
    }

    public void setMinimumCriteriaPeriod(String minimumPeriod) {
        this._minimumCriteriaPeriod = minimumPeriod;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="IntensivHoursWeighted">
    @Column(name = "isIntensivHoursWeighted")
    @Documentation (name = "Summe gewichtete Intensivstunden:", rank = 100)
    private int _intensivHoursWeighted;

    public int getIntensivHoursWeighted() {
        return _intensivHoursWeighted;
    }

    public void setIntensivHoursWeighted(int intensivHoursWeighted) {
        this._intensivHoursWeighted = intensivHoursWeighted;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="IntensivHoursNotweighted">
    @Column(name = "isIntensivHoursNotweighted")
    @Documentation (name = "Summe ungewichtete Intensivstunden:", rank = 110)
    private int _intensivHoursNotweighted;

    public int getIntensivHoursNotweighted() {
        return _intensivHoursNotweighted;
    }

    public void setIntensivHoursNotweighted(int intensivHoursNotweighted) {
        this._intensivHoursNotweighted = intensivHoursNotweighted;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="WeightMinimum">
    @Column(name = "isWeightMinimum")
    @Documentation (name = "Gewichtung Minimum:", rank = 120)
    private double _weightMinimum;

    public double getWeightMinimum() {
        return _weightMinimum;
    }

    public void setWeightMinimum(double weightMinimum) {
        this._weightMinimum = weightMinimum;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="WeightMaximum">
    @Column(name = "isWeightMaximum")
    @Documentation (name = "Gewichtung Maximum:", rank = 130)
    private double _weightMaximum;

    public double getWeightMaximum() {
        return _weightMaximum;
    }

    public void setWeightMaximum(double weightMaximum) {
        this._weightMaximum = weightMaximum;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="WeightDescription">
    @Column(name = "isWeightDescription")
    @Documentation (name = "Gewichtung Erläuterung:", rank = 140)
    private String _weightDescription = "";

    @Size(max = 300)
    public String getWeightDescription() {
        return _weightDescription;
    }

    public void setWeightDescription(String weightDescription) {
        this._weightDescription = weightDescription;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property medicalServiceCnt">
    @Column(name = "isMedicalServiceCnt")
    @Documentation (name = "VK ÄD:", rank = 150)
    private double _medicalServiceCnt;

    public double getMedicalServiceCnt() {
        return _medicalServiceCnt;
    }

    public void setMedicalServiceCnt(double medicalServiceCnt) {
        this._medicalServiceCnt = medicalServiceCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property nursingServiceCnt">
    @Column(name = "isNursingServiceCnt")
    @Documentation (name = "VK PD:", rank = 160)
    private double _nursingServiceCnt;

    public double getNursingServiceCnt() {
        return _nursingServiceCnt;
    }

    public void setNursingServiceCnt(double nursingServiceCnt) {
        this._nursingServiceCnt = nursingServiceCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property functionalServiceCnt">
    @Column(name = "isFunctionalServiceCnt")
    @Documentation (name = "FK FD:", rank = 170)
    private double _functionalServiceCnt;

    public double getFunctionalServiceCnt() {
        return _functionalServiceCnt;
    }

    public void setFunctionalServiceCnt(double functionalServiceCnt) {
        this._functionalServiceCnt = functionalServiceCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property medicalServiceCost">
    @Column(name = "isMedicalServiceCost")
    @Documentation (name = "Kosten ÄD:", rank = 180)
    private int _medicalServiceCost;

    public int getMedicalServiceCost() {
        return _medicalServiceCost;
    }

    public void setMedicalServiceCost(int medicalServiceCost) {
        this._medicalServiceCost = medicalServiceCost;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property nursingServiceCost">
    @Column(name = "isNursingServiceCost")
    @Documentation (name = "Kosten PD", rank = 190, isMoneyFormat = true)
    private int _nursingServiceCost;

    public int getNursingServiceCost() {
        return _nursingServiceCost;
    }

    public void setNursingServiceCost(int nursingServiceCost) {
        this._nursingServiceCost = nursingServiceCost;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property functionalServiceCost">
    @Column(name = "isFunctionalServiceCost")
    @Documentation (name = "Kosten FD", rank = 200)
    private int _functionalServiceCost;

    public int getFunctionalServiceCost() {
        return _functionalServiceCost;
    }

    public void setFunctionalServiceCost(int functionalServiceCost) {
        this._functionalServiceCost = functionalServiceCost;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property overheadsMedicine">
    @Column(name = "isOverheadsMedicine")
    @Documentation (name = "Kosten GK Arzneimittel", rank = 210)
    private int _overheadsMedicine;

    public int getOverheadsMedicine() {
        return _overheadsMedicine;
    }

    public void setOverheadsMedicine(int overheadsMedicine) {
        this._overheadsMedicine = overheadsMedicine;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property overheadMedicalGoods">
    @Column(name = "isOverheadsMedicalGoods")
    @Documentation (name = "Kosten GK med. Sachbedarf", rank = 220)
    private int _overheadMedicalGoods;

    public int getOverheadMedicalGoods() {
        return _overheadMedicalGoods;
    }

    public void setOverheadMedicalGoods(int overheadMedicalGoods) {
        this._overheadMedicalGoods = overheadMedicalGoods;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property medicalInfrastructureCost">
    @Column(name = "isMedicalInfrastructureCost")
    @Documentation (name = "Kosten med. Infra.", rank = 230)
    private int _medicalInfrastructureCost;

    public int getMedicalInfrastructureCost() {
        return _medicalInfrastructureCost;
    }

    public void setMedicalInfrastructureCost(int medicalInfrastructureCost) {
        this._medicalInfrastructureCost = medicalInfrastructureCost;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property nonMedicalInfrastructureCost">
    @Column(name = "isNonMedicalInfrastructureCost")
    @Documentation (name = "Kosten nicht med. Infra.", rank = 240)
    private int _nonMedicalInfrastructureCost;

    public int getNonMedicalInfrastructureCost() {
        return _nonMedicalInfrastructureCost;
    }

    public void setNonMedicalInfrastructureCost(int nonMedicalInfrastructureCost) {
        this._nonMedicalInfrastructureCost = nonMedicalInfrastructureCost;
    }
    //</editor-fold>

    public int getOverallCost() {
        return _medicalServiceCost + _nursingServiceCost + _functionalServiceCost + _overheadsMedicine
                + _overheadMedicalGoods + _medicalInfrastructureCost + _nonMedicalInfrastructureCost;
    }

    public Integer getCostPerMedicalVK() {
        if (_medicalServiceCnt == 0) {
            return null;
        }
        return (int) ((double) _medicalServiceCost / _medicalServiceCnt);
    }

    public Integer getCostPerNursingVK() {
        if (_nursingServiceCnt == 0) {
            return null;
        }
        return (int) ((double) _nursingServiceCost / _nursingServiceCnt);
    }

    // <editor-fold defaultstate="collapsed" desc="BaseInformationId">
//    @JoinColumn(name = "isBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "isBaseInformationId")
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    // </editor-fold>

    public String calcUtilization() {
        if (getBedCnt() == 0) {
            return "";
        }
        return Math.round(1000d * getIntensivHoursNotweighted() / (365 * 24 * getBedCnt())) / 10d + "%";
    }

    public KGLListIntensivStroke() {
    }

    public KGLListIntensivStroke(int baseInformationId, int intensiveType) {
        this._baseInformationId = baseInformationId;
        this._intensiveType = intensiveType;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this._id;

        if (this._id != -1) {
            return hash;
        }

        hash = 79 * hash + this._intensiveType;
        hash = 79 * hash + Objects.hashCode(this._costCenterText);
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

        if (this._id >= 0) {
            return this._id == other._id;
        }

        if (this._intensiveType != other._intensiveType) {
            return false;
        }
//        if (this._costCenterID != other._costCenterID) {
//            return false;
//        }
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
