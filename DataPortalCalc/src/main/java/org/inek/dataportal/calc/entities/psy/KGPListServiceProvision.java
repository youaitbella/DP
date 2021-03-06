/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.psy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.calc.backingbean.CalcBasicsStaticData;
import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;

import javax.faces.model.SelectItem;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListServiceProvision", schema = "calc")
public class KGPListServiceProvision implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spID", updatable = false, nullable = false)
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

    //<editor-fold defaultstate="collapsed" desc="Fremdvergebene Teilbereiche">
    @Column(name = "spPartitionExternalAssignment")
    @Documentation(name = "Fremdvergebene Teilbereiche", rank = 10)
    private String _partitionExternalAssignment = "";

    @Size(max = 200)
    public String getPartitionExternalAssignment() {
        return _partitionExternalAssignment;
    }

    public void setPartitionExternalAssignment(String partitionExternalAssignment) {
        this._partitionExternalAssignment = partitionExternalAssignment;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Anmerkung">
    @Column(name = "spNote")
    @Documentation(name = "Anmerkung", rank = 20)
    private String _note = "";

    public String getNote() {
        return _note;
    }

    public void setNote(String note) {
        this._note = note;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Kostenvolumen">
    @Column(name = "spAmount")
    @Documentation(name = "Kostenvolumen", rank = 30)
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
//    @JoinColumn(name = "spProvidedTypeId", referencedColumnName = "ptID")
//    @ManyToOne(optional = false)
    @Column(name = "spProvidedTypeId")
    //@Documentation(name = "Erbringungsart", rank = 30)
    private int _providedTypeId;

    public int getProvidedTypeId() {
        return _providedTypeId;
    }
    
    @Documentation(name = "Erbringungsart", rank = 30)
    @JsonIgnore
    public String getProvidedTypeText(){
        return CalcBasicsStaticData.staticGetProvidedTypeText()
                .stream()
                .filter(i -> (int)i.getValue() == _providedTypeId)
                .findAny().orElse(new SelectItem(-1, ""))
                .getLabel();
    }

    public void setProvidedTypeId(int providedTypeId) {
        this._providedTypeId = providedTypeId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceProvisionTypeId">
//    @JoinColumn(name = "spServiceProvisionTypeId", referencedColumnName = "sptID")
//    @ManyToOne(optional = false)
    @Column(name = "spServiceProvisionTypeId")
    
    private int _serviceProvisionTypeId;

    public int getServiceProvisionTypeId() {
        return _serviceProvisionTypeId;
    }
    
    @Documentation(name = "Bereich", rank = 40)
    @JsonIgnore
    public String getServiceProvisionTypeText(){
        return CalcBasicsStaticData.staticGetServiceProvivionItem()
                .stream()
                .filter(i -> (int)i.getValue() == _serviceProvisionTypeId)
                .findAny().orElse(new SelectItem(-1, ""))
                .getLabel();
    }

    public void setServiceProvisionTypeId(int serviceProvisionTypeId) {
        this._serviceProvisionTypeId = serviceProvisionTypeId;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property KGLListServiceProvisionType">
    @OneToOne
    @PrimaryKeyJoinColumn(name = "spServiceProvisionTypeId")
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
    private String _domain = "";

    public String getDomain() {
        return _domain;
    }

    public void setDomain(String domain) {
        _domain = domain;
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
    @SuppressWarnings("CyclomaticComplexity")
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
