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
import org.inek.dataportal.entities.calc.iface.IdValue;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListServiceProvision", schema = "calc")
public class KGPListServiceProvision implements Serializable, IdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spID")
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _partitionExternalAssignment">
    @Size(max = 200)
    @Column(name = "spPartitionExternalAssignment")
    private String _partitionExternalAssignment = "";

    public String getPartitionExternalAssignment() {
        return _partitionExternalAssignment;
    }

    public void setPartitionExternalAssignment(String partitionExternalAssignment) {
        this._partitionExternalAssignment = partitionExternalAssignment;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _note">
    @Size(max = 2147483647)
    @Column(name = "spNote")
    private String _note = "";

    public String getNote() {
        return _note;
    }

    public void setNote(String note) {
        this._note = note;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _amount">
    @Column(name = "spAmount")
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "spBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "spBaseInformationId")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _providedTypeId">
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceProvisionTypeId">
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
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property KGLListServiceProvisionType">
    @OneToOne
    @PrimaryKeyJoinColumn(name = "spServiceProvisionTypeID")
    private KGPListServiceProvisionType _serviceProvisionType;

    public KGPListServiceProvisionType getServiceProvisionType() {
        return _serviceProvisionType;
    }

    public void setServiceProvisionType(KGPListServiceProvisionType serviceProvisionType) {
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

    //<editor-fold defaultstate="collapsed" desc="Transient property Domain">
    @Column(name = "spSequence")
    private int _sequence;

    public int getSequence() {
        return _sequence;
    }

    public void setSequence(int sequence) {
        this._sequence = sequence;
    }

    //</editor-fold>
    public KGPListServiceProvision() {
    }

    public KGPListServiceProvision(int spID) {
        this._id = spID;
    }

    public KGPListServiceProvision(int spID, String spPartitionExternalAssignment, String spNote, int spAmount) {
        this._id = spID;
        this._partitionExternalAssignment = spPartitionExternalAssignment;
        this._note = spNote;
        this._amount = spAmount;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 97 * hash + Objects.hashCode(this._partitionExternalAssignment);
        hash = 97 * hash + Objects.hashCode(this._note);
        hash = 97 * hash + this._amount;
        hash = 97 * hash + this._baseInformationId;
        hash = 97 * hash + this._providedTypeId;
        hash = 97 * hash + this._serviceProvisionTypeId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListServiceProvision)) {
            return false;
        }
        final KGPListServiceProvision other = (KGPListServiceProvision) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
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
        return Objects.equals(this._note, other._note);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListServiceProvision[ spID=" + _id + " ]";
    }
    //</editor-fold>

    public boolean isEmpty(){
        return _partitionExternalAssignment.isEmpty() && _note.isEmpty() && _amount <= 0 && _providedTypeId <=0;
                
    }

}
