package org.inek.dataportal.insurance.specificfunction.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "AgreedRemunerationKeys", schema = "spf")
public class AgreedRemunerationKeys implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "arkId")
    private Integer _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>
    
    @Column(name = "arkNumber")
    private String _number = "";

    @Size(max = 8)
    public String getNumber() {
        return _number;
    }

    public void setNumber(String number) {
        this._number = number;
    }
    
    @Column(name = "arkText")
    private String _text = "";
    
    @Size(max = 255)
    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    
    @Column(name = "arkAmount")
    private float _amount;

    public float getAmount() {
        return _amount;
    }

    public void setAmount(float amount) {
        this._amount = amount;
    }
    
    @Column(name = "arkSpecificFunctionAgreementId")
    private int _specificFunctionAgreementId = -1;

    public int getSpecificFunctionAgreementId() {
        return _specificFunctionAgreementId;
    }

    public void setSpecificFunctionAgreementId(int specificFunctionAgreementId) {
        this._specificFunctionAgreementId = specificFunctionAgreementId;
    }
    
    // none = -1, all centres = 0 or sequence
    @Column(name = "arkScope")
    private int _scope = -1;

    public int getScope() {
        return _scope;
    }

    public void setScope(int scope) {
        this._scope = scope;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this._id);
        hash = 97 * hash + Objects.hashCode(this._number);
        hash = 97 * hash + Objects.hashCode(this._text);
        hash = 97 * hash + Float.floatToIntBits(this._amount);
        hash = 97 * hash + Objects.hashCode(this._specificFunctionAgreementId);
        hash = 97 * hash + this._scope;
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
        final AgreedRemunerationKeys other = (AgreedRemunerationKeys) obj;
        if (Float.floatToIntBits(this._amount) != Float.floatToIntBits(other._amount)) {
            return false;
        }
        if (this._scope != other._scope) {
            return false;
        }
        if (!Objects.equals(this._number, other._number)) {
            return false;
        }
        if (!Objects.equals(this._text, other._text)) {
            return false;
        }
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        if (!Objects.equals(this._specificFunctionAgreementId, other._specificFunctionAgreementId)) {
            return false;
        }
        return true;
    }
    
}
