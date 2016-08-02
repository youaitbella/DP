package org.inek.dataportal.entities.insurance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.*;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "InsuranceNubNoticeItem")
public class InsuranceNubNoticeItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    private final UUID _uuid = UUID.randomUUID();

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inniID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int value) {
        _id = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InsuranceNubNoticeId">
    @Column(name = "inniInsuranceNubNoticeId")
    private Integer _insuranceNubNoticeId;

    public Integer getInsuranceNubNoticeId() {
        return _insuranceNubNoticeId;
    }

    public void setInsuranceNubNoticeId(Integer value) {
        _insuranceNubNoticeId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property NubRequestId">
    @Column(name = "inniNubRequestId")
    private int _nubRequestId;

    public int getNubRequestId() {
        return _nubRequestId;
    }

    public void setNubRequestId(int value) {
        _nubRequestId = value;
    }

    public String getExternalId() {
        return "N" + _nubRequestId;
    }

    public void setExternalId(String id) {
        if (id.startsWith("N")) {
            try {
                _nubRequestId = Integer.parseInt(id.substring(1));
            } catch (NumberFormatException ex) {
                _nubRequestId = 0;
            }
        } else {
            _nubRequestId = 0;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Procedures">
    @Column(name = "inniProcedures")
    @Documentation
    private String _procedures = "";
    // this is defined as a string, not mapped to the procedure table
    // because combined entries are allowed:
    // code1, code2, ...
    // code1, code2+code3, code4, ...

    public String getProcedures() {
        return _procedures;
    }

    public void setProcedures(String name) {
        _procedures = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DosageFormId">
    @Column(name = "inniDosageFormId")
    @Documentation
    private int _dosageFormId;

    public int getDosageFormId() {
        return _dosageFormId;
    }

    public void setDosageFormId(int value) {
        _dosageFormId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Amount">
    @Column(name = "inniAmount")
    private BigDecimal _amount = new BigDecimal(0d);

    public BigDecimal getAmount() {
        return _amount;
    }

    public void setAmount(BigDecimal value) {
        _amount = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property UnitId">
    @Column(name = "inniUnitId")
    private int _unitId;

    public int getUnitId() {
        return _unitId;
    }

    public void setUnitId(int value) {
        _unitId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Price">
    @Column(name = "inniPrice")
    private BigDecimal _price = new BigDecimal(0d);

    public BigDecimal getPrice() {
        return _price;
    }

    public void setPrice(BigDecimal value) {
        _price = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Quantity">
    @Column(name = "inniQuantity")
    private int _quantity = 1;

    public int getQuantity() {
        return _quantity;
    }

    public void setQuantity(int value) {
        _quantity = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property RemunerationTypeCharId">
    @Column(name = "inniRemunerationTypeCharId")
    private int _remunerationTypeCharId;

    public int getRemunerationTypeCharId() {
        return _remunerationTypeCharId;
    }

    public void setRemunerationTypeCharId(int value) {
        _remunerationTypeCharId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Note">
    @Column(name = "inniNote")
    private String _note = "";

    public String getNote() {
        return _note;
    }

    public void setNote(String value) {
        _note = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        if (_id > 0) {
            return _id;
        }
        //return _uuid.hashCode();
        int hash = 7;
        hash = 67 * hash + this._insuranceNubNoticeId;
        hash = 67 * hash + Objects.hashCode(this._procedures);
        hash = 67 * hash + this._dosageFormId;
        hash = 67 * hash + Objects.hashCode(this._amount);
        hash = 67 * hash + this._unitId;
        hash = 67 * hash + Objects.hashCode(this._price);
        hash = 67 * hash + this._quantity;
        hash = 67 * hash + this._remunerationTypeCharId;
        hash = 67 * hash + Objects.hashCode(this._note);
        hash = 67 * hash + this._nubRequestId;
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
        final InsuranceNubNoticeItem other = (InsuranceNubNoticeItem) obj;
        if (_id > 0 || other._id > 0) {
            return _id == other._id;
        }

        //return _uuid.equals(other._uuid);
        if (!Objects.equals(this._insuranceNubNoticeId, other._insuranceNubNoticeId)) {
            return false;
        }
        if (this._dosageFormId != other._dosageFormId) {
            return false;
        }
        if (this._unitId != other._unitId) {
            return false;
        }
        if (this._quantity != other._quantity) {
            return false;
        }
        if (this._remunerationTypeCharId != other._remunerationTypeCharId) {
            return false;
        }
        if (this._nubRequestId != other._nubRequestId) {
            return false;
        }
        if (!Objects.equals(this._procedures, other._procedures)) {
            return false;
        }
        if (!Objects.equals(this._note, other._note)) {
            return false;
        }
        if (!Objects.equals(this._amount, other._amount)) {
            return false;
        }
        if (!Objects.equals(this._price, other._price)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.InsuranceNubNoticeItem[id=" + _id + "]";
    }
    // </editor-fold>

}
