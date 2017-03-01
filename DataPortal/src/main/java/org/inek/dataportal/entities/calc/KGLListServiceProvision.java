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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Sequence">
    @Column(name = "spSequence")
    private int _sequence;

    public int getSequence() {
        return _sequence;
    }

    public void setSequence(int sequence) {
        this._sequence = sequence;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PartitionExternalAssignment">
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
    @Column(name = "spAmount")
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BaseInformationId">
//    @JoinColumn(name = "spBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
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
    @Column(name = "spProvidedTypeID")
    private int _providedTypeId;

    public int getProvidedTypeId() {
        return _providedTypeId;
    }

    public void setProvidedTypeId(int providedTypeId) {
        this._providedTypeId = providedTypeId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceProvisionTypeID">
//    @JoinColumn(name = "spServiceProvisionTypeID", referencedColumnName = "sptID")
//    @ManyToOne(optional = false)
    @Column(name = "spServiceProvisionTypeID")
    private int _serviceProvisionTypeId;

    public int getServiceProvisionTypeId() {
        return _serviceProvisionTypeId;
    }

    public void setServiceProvisionTypeId(int serviceProvisionTypeId) {
        this._serviceProvisionTypeId = serviceProvisionTypeId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property KGLListServiceProvisionType">
    @OneToOne
    @PrimaryKeyJoinColumn(name = "spServiceProvisionTypeID")
    private KGLListServiceProvisionType _serviceProvisionType;

    public KGLListServiceProvisionType getServiceProvisionType() {
        return _serviceProvisionType;
    }

    public void setServiceProvisionType(KGLListServiceProvisionType serviceProvisionType) {
        _serviceProvisionType = serviceProvisionType;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Transient property Domain">
    @Transient
    private String _domain;
    
    public String getDomain() {
        return _domain;
    }
    
    public void setDomain(String domain) {
        this._domain = domain;
    }
    //</editor-fold>
    
    public boolean isEmpty(){
        return _partitionExternalAssignment.isEmpty() && _note.isEmpty() && _amount <= 0 && _providedTypeId <=0;
                
    }
    public KGLListServiceProvision() {
    }

    public KGLListServiceProvision(int spID) {
        this._id = spID;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 37 * hash + this._sequence;
        hash = 37 * hash + Objects.hashCode(this._partitionExternalAssignment);
        hash = 37 * hash + Objects.hashCode(this._note);
        hash = 37 * hash + this._amount;
        hash = 37 * hash + this._baseInformationId;
        hash = 37 * hash + this._providedTypeId;
        hash = 37 * hash + this._serviceProvisionTypeId;
        hash = 37 * hash + Objects.hashCode(this._domain);
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
        final KGLListServiceProvision other = (KGLListServiceProvision) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._sequence != other._sequence) {
            return false;
        }
        if (this._amount != other._amount) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (this._providedTypeId != other._providedTypeId) {
            return false;
        }
        if (this._serviceProvisionTypeId != other._serviceProvisionTypeId) {
            return false;
        }
        if (!Objects.equals(this._partitionExternalAssignment, other._partitionExternalAssignment)) {
            return false;
        }
        if (!Objects.equals(this._note, other._note)) {
            return false;
        }
        if (!Objects.equals(this._domain, other._domain)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListServiceProvision[ spID=" + _id + " ]";
    }

}
