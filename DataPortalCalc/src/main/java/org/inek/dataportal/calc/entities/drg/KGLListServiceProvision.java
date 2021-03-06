/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.drg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.calc.backingbean.CalcBasicsStaticData;
import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;

import javax.faces.model.SelectItem;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListServiceProvision", schema = "calc")
@XmlRootElement
public class KGLListServiceProvision implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Id">
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PartitionExternalAssignment">
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Note">
    @Column(name = "spNote")
    @Documentation(name = "Anmerkung", rank = 20)
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
    @Documentation(name = "Kostenvolumen", rank = 30)
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BaseInformationId">
//    @JoinColumn(name = "spBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "spBaseInformationId")
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ProvidedTypeId">
//    @JoinColumn(name = "spProvidedTypeId", referencedColumnName = "ptID")
//    @ManyToOne(optional = false)
    @Column(name = "spProvidedTypeId")
    //@Documentation(name = "Erbringungsart", rank = 40)
    private int _providedTypeId = 1;

    public int getProvidedTypeId() {
        return _providedTypeId;
    }
    
    @Documentation(name = "Erbringungsart", rank = 40)
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceProvisionTypeId">
    @Column(name = "spServiceProvisionTypeId")
    @Documentation(name = "Fremdvergebene Teilbereiche", rank = 50)
    private int _serviceProvisionTypeId = -1;

    public int getServiceProvisionTypeId() {
        return _serviceProvisionTypeId;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property KGLListServiceProvisionType">
    @OneToOne
    @PrimaryKeyJoinColumn(name = "spServiceProvisionTypeId")
    private KGLListServiceProvisionType _serviceProvisionType;

    public KGLListServiceProvisionType getServiceProvisionType() {
        return _serviceProvisionType;
    }

    public void setServiceProvisionType(KGLListServiceProvisionType serviceProvisionType) {
        _serviceProvisionType = serviceProvisionType;
        _serviceProvisionTypeId = serviceProvisionType == null ? -1 : serviceProvisionType.getId();
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Transient property Domain">
    @Transient
    private String _domain = "";
    
    public String getDomain() {
        return _domain;
    }
    
    public void setDomain(String domain) {
        this._domain = domain;
    }
    //</editor-fold>
    
    @JsonIgnore
    public boolean isEmpty(){
        return _partitionExternalAssignment.isEmpty() && _note.isEmpty() && _amount <= 0 && _providedTypeId <=0;
    }

    public KGLListServiceProvision() {
    }

    public KGLListServiceProvision(int baseInformationId) {
        _baseInformationId = baseInformationId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
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
    @SuppressWarnings("CyclomaticComplexity")
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
