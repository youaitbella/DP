package org.inek.dataportal.entities.nub;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "NubInsuranceMessage")
public class NubInsuranceMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nimID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int value) {
        _id = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Year">
    @Column(name = "nimYear")
    private int _year = -1;

    public int getYear() {
        return _year;
    }

    public void setYear(int value) {
        _year = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "nimAccountId")
    private int _accountId = -1;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int value) {
        _accountId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IK">
    @Column(name = "nimIK")
    private int _ik = -1;

    public int getIK() {
        return _ik;
    }

    public void setIK(int value) {
        _ik = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Date">
    @Column(name = "nimDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _date;

    public Date getDate() {
        return _date;
    }

    public void setDate(Date value) {
        _date = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InekMethodId">
    @Column(name = "nimInekMethodId")
    private int _inekMethodId = -1;

    public int getInekMethodId() {
        return _inekMethodId;
    }

    public void setInekMethodId(int value) {
        _inekMethodId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Procedures">
    @Column(name = "nimProcedures")
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

    // <editor-fold defaultstate="collapsed" desc="Property Amount">
    @Column(name = "nimAmount")
    private BigDecimal _amount = new BigDecimal(0d);

    public BigDecimal getAmount() {
        return _amount;
    }

    public void setAmount(BigDecimal value) {
        _amount = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property UnitId">
    @Column(name = "nimUnitId")
    private int _unitId = -1;

    public int getUnitId() {
        return _unitId;
    }

    public void setUnitId(int value) {
        _unitId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Price">
    @Column(name = "nimPrice")
    private BigDecimal _price = new BigDecimal(0d);

    public BigDecimal getPrice() {
        return _price;
    }

    public void setPrice(BigDecimal value) {
        _price = value;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Quantity">
    @Column(name = "nimQuantity")
    private int _quantity = -1;

    public int getQuantity() {
        return _quantity;
    }

    public void setQuantity(int value) {
        _quantity = value;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property RemunerationTypeId">
    @Column(name = "nimRemunerationTypeId")
    private int _remunerationTypeId = -1;

    public int getRemunerationTypeId() {
        return _remunerationTypeId;
    }

    public void setRemunerationTypeId(int value) {
        _remunerationTypeId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Note">
    @Column(name = "nimNote")
    private String _note = "";

    public String getNote() {
        return _note;
    }

    public void setNote(String value) {
        _note = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property NubRequestId">
    @Column(name = "nimNubRequestId")
    private int _nubRequestId = -1;

    public int getNubRequestId() {
        return _nubRequestId;
    }

    public void setNubRequestId(int value) {
        _nubRequestId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NubInsuranceMessage)) {
            return false;
        }
        NubInsuranceMessage other = (NubInsuranceMessage) object;
        return _id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.entities.NubInsuranceMessage[id=" + _id + "]";
    }
    // </editor-fold>

}
