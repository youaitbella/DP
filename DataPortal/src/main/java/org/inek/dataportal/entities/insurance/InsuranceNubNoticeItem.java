package org.inek.dataportal.entities.insurance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "InsuranceNubNoticeItem")
public class InsuranceNubNoticeItem implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private int _insuranceNubNoticeId;

    public int getInsuranceNubNoticeId() {
        return _insuranceNubNoticeId;
    }

    public void setInsuranceNubNoticeId(int value) {
        _insuranceNubNoticeId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InekMethodId">
    @Column(name = "inniInekMethodId")
    private int _inekMethodId = -1;

    public int getInekMethodId() {
        return _inekMethodId;
    }

    public void setInekMethodId(int value) {
        _inekMethodId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Procedures">
    @Column(name = "inniProcedures")
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
    private int _unitId = -1;

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
    private int _quantity = -1;

    public int getQuantity() {
        return _quantity;
    }

    public void setQuantity(int value) {
        _quantity = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property RemunerationTypeId">
    @Column(name = "inniRemunerationTypeId")
    private int _remunerationTypeId = -1;

    public int getRemunerationTypeId() {
        return _remunerationTypeId;
    }

    public void setRemunerationTypeId(int value) {
        _remunerationTypeId = value;
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

    // <editor-fold defaultstate="collapsed" desc="Property NubRequestId">
    @Column(name = "inniNubRequestId")
    private int _nubRequestId = -1;

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

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InsuranceNubNoticeItem)) {
            return false;
        }
        InsuranceNubNoticeItem other = (InsuranceNubNoticeItem) object;
        return _id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.entities.InsuranceNubNoticeItem[id=" + _id + "]";
    }
    // </editor-fold>

}
