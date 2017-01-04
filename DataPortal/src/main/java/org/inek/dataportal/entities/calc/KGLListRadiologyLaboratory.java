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
@NamedQueries({
    @NamedQuery(name = "KGLListRadiologyLaboratory.findAll", query = "SELECT k FROM KGLListRadiologyLaboratory k")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByID", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k._id = :rlID")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByCostCenterID", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k._costCenterID = :rlCostCenterID")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByCostCenterText", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k._costCenterText = :rlCostCenterText")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByServiceDocumentation", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k._serviceDocumentation = :rlServiceDocumentation")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByDescription", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k._description = :rlDescription")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByServiceVolumePre", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k._serviceVolumePre = :rlServiceVolumePre")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByAmountPre", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k._amountPre = :rlAmountPre")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByServiceVolumePost", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k._serviceVolumePost = :rlServiceVolumePost")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByAmountPost", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k._amountPost = :rlAmountPost")})
public class KGLListRadiologyLaboratory implements Serializable {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlID")
    private Integer _id;
    
    public Integer getID() {
        return _id;
    }

    public void setID(Integer rlID) {
        this._id = rlID;
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

    // <editor-fold defaultstate="collapsed" desc="ServiceDocumentation">
    @Basic(optional = false)
    @NotNull
    @Size(max = 200)
    @Column(name = "rlServiceDocumentation")
    private String _serviceDocumentation = "";
    
    public String getServiceDocumentation() {
        return _serviceDocumentation;
    }

    public void setServiceDocumentation(String rlServiceDocumentation) {
        this._serviceDocumentation = rlServiceDocumentation;
    }
    // </editor-fold>

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

    public KGLListRadiologyLaboratory(Integer id, int costCenterID, String costCenterText, String serviceDocumentation, String description, double serviceVolumePre, double amountPre, double serviceVolumePost, double amountPost) {
        this._id = id;
        this._costCenterID = costCenterID;
        this._costCenterText = costCenterText;
        this._serviceDocumentation = serviceDocumentation;
        this._description = description;
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
