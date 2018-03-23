package org.inek.dataportal.insurance.specificfunction.entity;

import java.io.Serializable;
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
    private int _id = -1;

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
    private int _specificFunctionAgreementId;

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
}
