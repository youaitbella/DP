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

    // <editor-fold defaultstate="collapsed" desc="BaseInformationID">
    @JoinColumn(name = "isBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics _baseInformation;

    public DrgCalcBasics getBaseInformation() {
        return _baseInformation;
    }

    public void setBaseInformation(DrgCalcBasics baseInformation) {
        this._baseInformation = baseInformation;
    }
    // </editor-fold>
    
    public KGLListIntensivStroke() {
    }

    public KGLListIntensivStroke(int isID) {
        this._id = isID;
    }

    public KGLListIntensivStroke(int id, int intensiveType, int costCenterID, String costCenterText, String departmentKey, String departmentAssignment, int bedCnt, int caseCnt, boolean ops8980, boolean ops898f, boolean ops8981, boolean ops898b, String minimumPeriod, int intensivHoursWeighted, int intensivHoursNotweighted, double weightMinimum, double weightMaximum, String weightDescription) {
        this._id = id;
        this._intensiveType = intensiveType;
        this._costCenterID = costCenterID;
        this._costCenterText = costCenterText;
        this._departmentKey = departmentKey;
        this._departmentAssignment = departmentAssignment;
        this._bedCnt = bedCnt;
        this._caseCnt = caseCnt;
        this._ops8980 = ops8980;
        this._ops898f = ops898f;
        this._ops8981 = ops8981;
        this._ops898b = ops898b;
        this._minimumCriteriaPeriod = minimumPeriod;
        this._intensivHoursWeighted = intensivHoursWeighted;
        this._intensivHoursNotweighted = intensivHoursNotweighted;
        this._weightMinimum = weightMinimum;
        this._weightMaximum = weightMaximum;
        this._weightDescription = weightDescription;
    }

    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListIntensivStroke)) {
            return false;
        }
        KGLListIntensivStroke other = (KGLListIntensivStroke) object;
        return this._id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListIntensivStroke[ isID=" + _id + " ]";
    }
    
}
