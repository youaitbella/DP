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
@Table(name = "KGLListServiceProvision", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLListServiceProvision.findAll", query = "SELECT k FROM KGLListServiceProvision k")
    , @NamedQuery(name = "KGLListServiceProvision.findBySpID", query = "SELECT k FROM KGLListServiceProvision k WHERE k._id = :spID")
    , @NamedQuery(name = "KGLListServiceProvision.findBySpPartitionExternalAssignment", query = "SELECT k FROM KGLListServiceProvision k WHERE k._partitionExternalAssignment = :spPartitionExternalAssignment")
    , @NamedQuery(name = "KGLListServiceProvision.findBySpNote", query = "SELECT k FROM KGLListServiceProvision k WHERE k._note = :spNote")
    , @NamedQuery(name = "KGLListServiceProvision.findBySpAmount", query = "SELECT k FROM KGLListServiceProvision k WHERE k._amount = :spAmount")})
public class KGLListServiceProvision implements Serializable {
    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "spID")
    private Integer _id;
    
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PartitionExternalAssignment">
    @Basic(optional = false)
    @NotNull
    @Size(max = 200)
    @Column(name = "spPartitionExternalAssignment")
    private String _partitionExternalAssignment = "";
    
    public String getPartitionExternalAssignment() {
        return _partitionExternalAssignment;
    }

    public void setPartitionExternalAssignment(String partitionExternalAssignment) {
        this._partitionExternalAssignment = partitionExternalAssignment;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Note">
    @Basic(optional = false)
    @NotNull
    @Size(max = 2147483647)
    @Column(name = "spNote")
    private String _note = "";
    
    public String getNote() {
        return _note;
    }

    public void setNote(String spNote) {
        this._note = spNote;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Amount">
    @Basic(optional = false)
    @NotNull
    @Column(name = "spAmount")
    private double _amount;

    public double getAmount() {
        return _amount;
    }

    public void setAmount(double amount) {
        this._amount = amount;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="BaseInformationID">
//    @JoinColumn(name = "spBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "spBaseInformationID")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ProvidedTypeID">
//    @JoinColumn(name = "spProvidedTypeID", referencedColumnName = "ptID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "spProvidedTypeID")
    private int _providedTypeID;

    public int getProvidedTypeID() {
        return _providedTypeID;
    }

    public void setProvidedTypeID(int providedTypeID) {
        this._providedTypeID = providedTypeID;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ServiceProvisionTypeID">
//    @JoinColumn(name = "spServiceProvisionTypeID", referencedColumnName = "sptID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "spServiceProvisionTypeID")
    private int _serviceProvisionTypeID;

    public int getServiceProvisionTypeID() {
        return _serviceProvisionTypeID;
    }

    public void setServiceProvisionTypeID(int serviceProvisionTypeID) {
        this._serviceProvisionTypeID = serviceProvisionTypeID;
    }
    // </editor-fold>
    

    
    public KGLListServiceProvision() {
    }

    public KGLListServiceProvision(Integer spID) {
        this._id = spID;
    }

    public KGLListServiceProvision(Integer id, String partitionExternalAssignment, String note, double amount) {
        this._id = id;
        this._partitionExternalAssignment = partitionExternalAssignment;
        this._note = note;
        this._amount = amount;
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
        if (!(object instanceof KGLListServiceProvision)) {
            return false;
        }
        KGLListServiceProvision other = (KGLListServiceProvision) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListServiceProvision[ spID=" + _id + " ]";
    }
}
