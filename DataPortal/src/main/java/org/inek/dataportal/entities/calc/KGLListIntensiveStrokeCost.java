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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListIntensiveStrokeCost", schema = "calc")
@XmlRootElement
public class KGLListIntensiveStrokeCost implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscID")
    private int _id = -1;
    
    public int getId() {
        return _id;
    }
    
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property intensiveType">
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscIntensiveType")
    private int _intensiveType;

    public int getIntensiveType() {
        return _intensiveType;
    }

    public void setIntensiveType(int intensiveType) {
        this._intensiveType = intensiveType;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property costCenterID">
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscCostCenterID")
    private int _costCenterID;

    public int getCostCenterID() {
        return _costCenterID;
    }

    public void setCostCenterID(int costCenterID) {
        this._costCenterID = costCenterID;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property costCenterText">
    @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "iscCostCenterText")
    private String _costCenterText = "";

    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property departmentKey">
    @Basic(optional = false)
    @NotNull
    @Size(max = 4)
    @Column(name = "iscDepartmentKey")
    private String _departmentKey = "";

    public String getDepartmentKey() {
        return _departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this._departmentKey = departmentKey;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property departmentAssignment">
    @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "iscDepartmentAssignment")
    private String _departmentAssignment = "";

    public String getDepartmentAssignment() {
        return _departmentAssignment;
    }

    public void setDepartmentAssignment(String departmentAssignment) {
        this._departmentAssignment = departmentAssignment;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property medicalServiceCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscMedicalServiceCnt")
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
    @Column(name = "iscNursingServiceCnt")
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
    @Column(name = "iscFunctionalServiceCnt")
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
    @Column(name = "iscMedicalServiceCost")
    private double _medicalServiceCost;

    public double getMedicalServiceCost() {
        return _medicalServiceCost;
    }

    public void setMedicalServiceCost(double medicalServiceCost) {
        this._medicalServiceCost = medicalServiceCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property nursingServiceCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscNursingServiceCost")
    private double _nursingServiceCost;

    public double getNursingServiceCost() {
        return _nursingServiceCost;
    }

    public void setNursingServiceCost(double nursingServiceCost) {
        this._nursingServiceCost = nursingServiceCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property functionalServiceCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscFunctionalServiceCost")
    private double _functionalServiceCost;

    public double getFunctionalServiceCost() {
        return _functionalServiceCost;
    }

    public void setFunctionalServiceCost(double functionalServiceCost) {
        this._functionalServiceCost = functionalServiceCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property overheadsMedicine">
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscOverheadsMedicine")
    private double _overheadsMedicine;

    public double getOverheadsMedicine() {
        return _overheadsMedicine;
    }

    public void setOverheadsMedicine(double overheadsMedicine) {
        this._overheadsMedicine = overheadsMedicine;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property overheadMedicalGoods">
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscOverheadMedicalGoods")
    private double _overheadMedicalGoods;

    public double getOverheadMedicalGoods() {
        return _overheadMedicalGoods;
    }

    public void setOverheadMedicalGoods(double overheadMedicalGoods) {
        this._overheadMedicalGoods = overheadMedicalGoods;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property medicalInfrastructureCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscMedicalInfrastructureCost")
    private double _medicalInfrastructureCost;

    public double getMedicalInfrastructureCost() {
        return _medicalInfrastructureCost;
    }

    public void setMedicalInfrastructureCost(double medicalInfrastructureCost) {
        this._medicalInfrastructureCost = medicalInfrastructureCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property nonMedicalInfrastructureCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscNonMedicalInfrastructureCost")
    private double _nonMedicalInfrastructureCost;

    public double getNonMedicalInfrastructureCost() {
        return _nonMedicalInfrastructureCost;
    }

    public void setNonMedicalInfrastructureCost(double nonMedicalInfrastructureCost) {
        this._nonMedicalInfrastructureCost = nonMedicalInfrastructureCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property baseInformationID">
//    @JoinColumn(name = "iscBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscBaseInformationID")
    private DrgCalcBasics _baseInformationID;

    public DrgCalcBasics getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(DrgCalcBasics baseInformationID) {
        this._baseInformationID = baseInformationID;
    }
    //</editor-fold>
    
    public KGLListIntensiveStrokeCost() {
    }

    public KGLListIntensiveStrokeCost(Integer iscID) {
        this._id = iscID;
    }

    public KGLListIntensiveStrokeCost(Integer iscID, int iscIntensiveType, int iscCostCenterID, String iscCostCenterText, String iscDepartmentKey, String iscDepartmentAssignment, double iscMedicalServiceCnt, double iscNursingServiceCnt, double iscFunctionalServiceCnt, double iscMedicalServiceCost, double iscNursingServiceCost, double iscFunctionalServiceCost, double iscOverheadsMedicine, double iscOverheadMedicalGoods, double iscMedicalInfrastructureCost, double iscNonMedicalInfrastructureCost) {
        this._id = iscID;
        this._intensiveType = iscIntensiveType;
        this._costCenterID = iscCostCenterID;
        this._costCenterText = iscCostCenterText;
        this._departmentKey = iscDepartmentKey;
        this._departmentAssignment = iscDepartmentAssignment;
        this._medicalServiceCnt = iscMedicalServiceCnt;
        this._nursingServiceCnt = iscNursingServiceCnt;
        this._functionalServiceCnt = iscFunctionalServiceCnt;
        this._medicalServiceCost = iscMedicalServiceCost;
        this._nursingServiceCost = iscNursingServiceCost;
        this._functionalServiceCost = iscFunctionalServiceCost;
        this._overheadsMedicine = iscOverheadsMedicine;
        this._overheadMedicalGoods = iscOverheadMedicalGoods;
        this._medicalInfrastructureCost = iscMedicalInfrastructureCost;
        this._nonMedicalInfrastructureCost = iscNonMedicalInfrastructureCost;
    }

    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListIntensiveStrokeCost)) {
            return false;
        }
        KGLListIntensiveStrokeCost other = (KGLListIntensiveStrokeCost) object;
        return (this._id == other._id);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListIntensiveStrokeCost[ iscID=" + _id + " ]";
    }
    
}
