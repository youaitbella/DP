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
public class KGLListServiceProvision implements Serializable {
    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "spID")
    private int _id = -1;
    
    public int getId() {
        return _id;
    }

    public void setId(int id) {
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

    public KGLListServiceProvision(int spID) {
        this._id = spID;
    }

    public KGLListServiceProvision(int id, String partitionExternalAssignment, String note, double amount) {
        this._id = id;
        this._partitionExternalAssignment = partitionExternalAssignment;
        this._note = note;
        this._amount = amount;
    }


    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListServiceProvision)) {
            return false;
        }
        KGLListServiceProvision other = (KGLListServiceProvision) object;
        return this._id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListServiceProvision[ spID=" + _id + " ]";
    }
}
