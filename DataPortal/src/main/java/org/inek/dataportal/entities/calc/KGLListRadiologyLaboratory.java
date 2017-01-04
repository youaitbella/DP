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
@Table(name = "KGLListRadiologyLaboratory", schema = "calc")
@XmlRootElement
public class KGLListRadiologyLaboratory implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlID")
    private Integer _id;
    
    public Integer getId() {
        return _id;
    }

    public void setId(Integer rlId) {
        this._id = rlId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BaseInformationID">
//    @JoinColumn(name = "rlBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlBaseInformationID")
    private int _baseInformationID;
    
    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int rlBaseInformationID) {
        this._baseInformationID = rlBaseInformationID;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostTypeID">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlCostTypeID")
    private int _costTypeID;
    
    public int getCostTypeID() {
        return _costTypeID;
    }

    public void setCostTypeID(int rlCostTypeID) {
        this._costTypeID = rlCostTypeID;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="CostCenterID">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlCostCenterID")
    private int _costCenterID;
    
    public int getCostCenterID() {
        return _costCenterID;
    }

    public void setCostCenterID(int rlCostCenterID) {
        this._costCenterID = rlCostCenterID;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterText">
    @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "rlCostCenterText")
    private String _costCenterText = "";
    
    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String rlCostCenterText) {
        this._costCenterText = rlCostCenterText;
    }
    // </editor-fold>

    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocHome")
    private boolean _serviceDocHome;

    public boolean isServiceDocHome() {
        return _serviceDocHome;
    }

    public void setServiceDocHome(boolean serviceDocHome) {
        this._serviceDocHome = serviceDocHome;
    }
    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocDKG")
    private boolean _serviceDocDKG;

    public boolean isServiceDocDKG() {
        return _serviceDocDKG;
    }

    public void setServiceDocDKG(boolean serviceDocDKG) {
        this._serviceDocDKG = serviceDocDKG;
    }
    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocEBM")
    private boolean _serviceDocEBM;

    public boolean isServiceDocEBM() {
        return _serviceDocEBM;
    }

    public void setServiceDocEBM(boolean serviceDocEBM) {
        this._serviceDocEBM = serviceDocEBM;
    }
    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocGOA")
    private boolean _serviceDocGOA;

    public boolean isServiceDocGOA() {
        return _serviceDocGOA;
    }

    public void setServiceDocGOA(boolean serviceDocGOA) {
        this._serviceDocGOA = serviceDocGOA;
    }
    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocDif")
    private boolean _serviceDocDif;

    public boolean isServiceDocDif() {
        return _serviceDocDif;
    }

    public void setServiceDocDif(boolean serviceDocDif) {
        this._serviceDocDif = serviceDocDif;
    }
    
    
    
    // <editor-fold defaultstate="collapsed" desc="Description">
    @Basic(optional = false)
    @NotNull
    @Size(max = 2147483647)
    @Column(name = "rlDescription")
    private String _description = "";
    
    public String getDescription() {
        return _description;
    }

    public void setDescription(String rlDescription) {
        this._description = rlDescription;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceVolumePre">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceVolumePre")
    private double _serviceVolumePre;
    
    public double getServiceVolumePre() {
        return _serviceVolumePre;
    }

    public void setServiceVolumePre(double rlServiceVolumePre) {
        this._serviceVolumePre = rlServiceVolumePre;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AmountPre">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlAmountPre")
    private double _amountPre;
    
    public double getAmountPre() {
        return _amountPre;
    }

    public void setAmountPre(double rlAmountPre) {
        this._amountPre = rlAmountPre;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ServiceVolumePost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceVolumePost")
    private double _serviceVolumePost;
    
    public double getServiceVolumePost() {
        return _serviceVolumePost;
    }

    public void setServiceVolumePost(double rlServiceVolumePost) {
        this._serviceVolumePost = rlServiceVolumePost;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AmountPost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlAmountPost")
    private double _amountPost;
    
    public double getAmountPost() {
        return _amountPost;
    }

    public void setAmountPost(double rlAmountPost) {
        this._amountPost = rlAmountPost;
    }
    // </editor-fold>

    
    public KGLListRadiologyLaboratory() {
    }

    public KGLListRadiologyLaboratory(Integer rlID) {
        this._id = rlID;
    }

    public KGLListRadiologyLaboratory(Integer id, int baseInformationID, int costTypeID, int costCenterID, boolean serviceDocHome, boolean serviceDocDKG, boolean serviceDocEBM, boolean serviceDocGOA, boolean serviceDocDif, double serviceVolumePre, double amountPre, double serviceVolumePost, double amountPost) {
        this._id = id;
        this._baseInformationID = baseInformationID;
        this._costTypeID = costTypeID;
        this._costCenterID = costCenterID;
        this._serviceDocHome = serviceDocHome;
        this._serviceDocDKG = serviceDocDKG;
        this._serviceDocEBM = serviceDocEBM;
        this._serviceDocGOA = serviceDocGOA;
        this._serviceDocDif = serviceDocDif;
        this._serviceVolumePre = serviceVolumePre;
        this._amountPre = amountPre;
        this._serviceVolumePost = serviceVolumePost;
        this._amountPost = amountPost;
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
        if (!(object instanceof KGLListRadiologyLaboratory)) {
            return false;
        }
        KGLListRadiologyLaboratory other = (KGLListRadiologyLaboratory) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListRadiologyLaboratory[ rlID=" + _id + " ]";
    }
    
}
