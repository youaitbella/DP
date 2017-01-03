/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "KGLOpAn", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLOpAn.findAll", query = "SELECT k FROM KGLOpAn k")
    , @NamedQuery(name = "KGLOpAn.findByOaID", query = "SELECT k FROM KGLOpAn k WHERE k._id = :oaID")
    , @NamedQuery(name = "KGLOpAn.findByOaHasCentralOP", query = "SELECT k FROM KGLOpAn k WHERE k._hasCentralOP = :oaHasCentralOP")
    , @NamedQuery(name = "KGLOpAn.findByOaCentralOPCnt", query = "SELECT k FROM KGLOpAn k WHERE k._centralOPCnt = :oaCentralOPCnt")
    , @NamedQuery(name = "KGLOpAn.findByOaMedicalService", query = "SELECT k FROM KGLOpAn k WHERE k._medicalService = :oaMedicalService")
    , @NamedQuery(name = "KGLOpAn.findByOaFunctionalService", query = "SELECT k FROM KGLOpAn k WHERE k._functionalService = :oaFunctionalService")
    , @NamedQuery(name = "KGLOpAn.findByOaDescription", query = "SELECT k FROM KGLOpAn k WHERE k._description = :oaDescription")
    , @NamedQuery(name = "KGLOpAn.findByOaMedicalServiceAmount", query = "SELECT k FROM KGLOpAn k WHERE k._medicalServiceAmount = :oaMedicalServiceAmount")
    , @NamedQuery(name = "KGLOpAn.findByOaFunctionalServiceAmount", query = "SELECT k FROM KGLOpAn k WHERE k._functionalServiceAmount = :oaFunctionalServiceAmount")})
public class KGLOpAn implements Serializable {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaID")
    private Integer _id;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        this._id = id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property HasCentralOP">
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaHasCentralOP")
    private boolean _hasCentralOP;

    public boolean isHasCentralOP() {
        return _hasCentralOP;
    }

    public void setHasCentralOP(boolean hasCentralOP) {
        this._hasCentralOP = hasCentralOP;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CentralOPCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaCentralOPCnt")
    private int _centralOPCnt;

    public int getCentralOPCnt() {
        return _centralOPCnt;
    }

    public void setCentralOPCnt(int centralOPCnt) {
        this._centralOPCnt = centralOPCnt;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property MedicalService">
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaMedicalService")
    private boolean _medicalService;

    public boolean isMedicalService() {
        return _medicalService;
    }

    public void setMedicalService(boolean medicalService) {
        this._medicalService = medicalService;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property FunctionalService">
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaFunctionalService")
    private boolean _functionalService;

    public boolean isFunctionalService() {
        return _functionalService;
    }

    public void setFunctionalService(boolean functionalService) {
        this._functionalService = functionalService;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Description">
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "oaDescription")
    private String _description;

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property MedicalServiceAmount">
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaMedicalServiceAmount")
    private BigDecimal _medicalServiceAmount;

    public BigDecimal getMedicalServiceAmount() {
        return _medicalServiceAmount;
    }

    public void setMedicalServiceAmount(BigDecimal medicalServiceAmount) {
        this._medicalServiceAmount = medicalServiceAmount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FunctionalServiceAmount">
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaFunctionalServiceAmount")
    private BigDecimal _functionalServiceAmount;
    
    public BigDecimal getFunctionalServiceAmount() {
        return _functionalServiceAmount;
    }

    public void setFunctionalServiceAmount(BigDecimal functionalServiceAmount) {
        this._functionalServiceAmount = functionalServiceAmount;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property BaseInformationID">
//    @JoinColumn(name = "oaBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaBaseInformationID")
    private int _baseInformationID;

    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int baseInformationID) {
        this._baseInformationID = baseInformationID;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property ContentTextID">
//    @JoinColumn(name = "oaContentTextID", referencedColumnName = "ctID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaContentTextID")
    private int _contentTextID;

    public int getContentTextID() {
        return _contentTextID;
    }

    public void setContentTextID(int contentTextID) {
        this._contentTextID = contentTextID;
    }
    // </editor-fold>
    
    public KGLOpAn() {
    }

    public KGLOpAn(Integer oaID) {
        this._id = oaID;
    }

    public KGLOpAn(Integer oaID, boolean oaHasCentralOP, int oaCentralOPCnt, boolean oaMedicalService, boolean oaFunctionalService, String oaDescription, BigDecimal oaMedicalServiceAmount, BigDecimal oaFunctionalServiceAmount) {
        this._id = oaID;
        this._hasCentralOP = oaHasCentralOP;
        this._centralOPCnt = oaCentralOPCnt;
        this._medicalService = oaMedicalService;
        this._functionalService = oaFunctionalService;
        this._description = oaDescription;
        this._medicalServiceAmount = oaMedicalServiceAmount;
        this._functionalServiceAmount = oaFunctionalServiceAmount;
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
        if (!(object instanceof KGLOpAn)) {
            return false;
        }
        KGLOpAn other = (KGLOpAn) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLOpAn[ oaID=" + _id + " ]";
    }
    
}
