package org.inek.dataportal.entities.certification;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "Grouper", schema = "crt")
@IdClass(GrouperId.class)
public class Grouper implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property SystemId">
    @Id
    @Column(name = "grSystemId")
    private int _systemId;
    public int getSystemId() {
        return _systemId;
    }

    public void setSystemId(int systemId) {
        _systemId = systemId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Id
    @Column(name = "grAccountId")
    private int _accountId;
    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "grName")
    private String _name;
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PasswordRequest">
    @Column(name = "grPasswordRequest")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _passwordRequest;
    public Date getPasswordRequest() {
        return _passwordRequest;
    }

    public void setPasswordRequest(Date passwordRequest) {
        _passwordRequest = passwordRequest;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property TestUpload1">
    @Column(name = "grTestUpload1")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _testUpload1;
    public Date getTestUpload1() {
        return _testUpload1;
    }

    public void setTestUpload1(Date date) {
        _testUpload1 = date;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property TestCheck1">
    @Column(name = "grTestCheck1")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _testCheck1;
    public Date getTestCheck1() {
        return _testCheck1;
    }

    public void setTestCheck1(Date date) {
        _testCheck1 = date;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property TestError1">
    @Column(name = "grTestError1")
    private int _testError1;
    public int getTestError1() {
        return _testError1;
    }

    public void setTestError1(int testError1) {
        _testError1 = testError1;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property TestUpload2">
    @Column(name = "grTestUpload2")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _testUpload2;
    public Date getTestUpload2() {
        return _testUpload2;
    }

    public void setTestUpload2(Date date) {
        _testUpload2 = date;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property TestCheck2">
    @Column(name = "grTestCheck2")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _testCheck2;
    public Date getTestCheck2() {
        return _testCheck2;
    }

    public void setTestCheck2(Date date) {
        _testCheck2 = date;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property TestError2">
    @Column(name = "grTestError2")
    private int _testError2;
    public int getTestError2() {
        return _testError2;
    }

    public void setTestError2(int testError2) {
        _testError2 = testError2;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property TestUpload3">
    @Column(name = "grTestUpload3")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _testUpload3;
    public Date getTestUpload3() {
        return _testUpload3;
    }

    public void setTestUpload3(Date date) {
        _testUpload3 = date;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property TestCheck3">
    @Column(name = "grTestCheck3")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _testCheck3;
    public Date getTestCheck3() {
        return _testCheck3;
    }

    public void setTestCheck3(Date date) {
        _testCheck3 = date;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property TestError3">
    @Column(name = "grTestError3")
    private int _testError3;
    public int getTestError3() {
        return _testError3;
    }

    public void setTestError3(int testError3) {
        _testError3 = testError3;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CertUpload1">
    @Column(name = "grCertUpload1")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _certUpload1;
    public Date getCertUpload1() {
        return _certUpload1;
    }

    public void setCertUpload1(Date date) {
        _certUpload1 = date;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CertCheck1">
    @Column(name = "grCertCheck1")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _certCheck1;
    public Date getCertCheck1() {
        return _certCheck1;
    }

    public void setCertCheck1(Date date) {
        _certCheck1 = date;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CertError1">
    @Column(name = "grCertError1")
    private int _certError1;
    public int getCertError1() {
        return _certError1;
    }

    public void setCertError1(int certError1) {
        _certError1 = certError1;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CertUpload2">
    @Column(name = "grCertUpload2")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _certUpload2;
    public Date getCertUpload2() {
        return _certUpload2;
    }

    public void setCertUpload2(Date date) {
        _certUpload2 = date;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CertCheck2">
    @Column(name = "grCertCheck2")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _certCheck2;
    public Date getCertCheck2() {
        return _certCheck2;
    }

    public void setCertCheck2(Date date) {
        _certCheck2 = date;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CertError2">
    @Column(name = "grCertError2")
    private int _certError2;
    public int getCertError2() {
        return _certError2;
    }

    public void setCertError2(int certError2) {
        _certError2 = certError2;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Certification">
    @Column(name = "grCertification")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _certification;
    public Date getCertification() {
        return _certification;
    }

    public void setCertification(Date certification) {
        _certification = certification;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode + equals">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(_systemId);
        hash = 73 * hash + Objects.hashCode(_accountId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrouperId other = (GrouperId) obj;
        return _systemId == other._systemId && _accountId == other._accountId;
    }
    // </editor-fold>

}
