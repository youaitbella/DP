package org.inek.dataportal.entities.insurance;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "InsuranceNubNotice")
public class InsuranceNubNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "innID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int value) {
        _id = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CreationDate">
    @Column(name = "innCreationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _creationDate;

    public Date getCreationDate() {
        return _creationDate;
    }

    public void setCreationDate(Date value) {
        _creationDate = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastChange">
    @Column(name = "innLastChange")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastChange;

    public Date getLastChange() {
        return _lastChange;
    }

    public void setLastChange(Date value) {
        _lastChange = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "innAccountId")
    private int _accountId = -1;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int value) {
        _accountId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Year">
    @Column(name = "innYear")
    private int _year = -1;

    public int getYear() {
        return _year;
    }

    public void setYear(int value) {
        _year = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InsuranceName">
    @Column(name = "innInsuranceName")
    private String _insuranceName = "";

    public String getInsuranceName() {
        return _insuranceName;
    }

    public void setInsuranceName(String name) {
        _insuranceName = name;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property InsuranceIk">
    @Column(name = "innInsuranceIk")
    private int _insuranceIk = -1;

    public int getInsuranceIk() {
        return _insuranceIk;
    }

    public void setInsuranceIk(int value) {
        _insuranceIk = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property HospitalIk">
    @Column(name = "innHospitalIk")
    private int _hospitalIk = -1;

    public int getHospitalIk() {
        return _hospitalIk;
    }

    public void setHospitalIk(int value) {
        _hospitalIk = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InsuranceNubNotice)) {
            return false;
        }
        InsuranceNubNotice other = (InsuranceNubNotice) object;
        return _id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.entities.InsuranceNubNotice[id=" + _id + "]";
    }
    // </editor-fold>

}
