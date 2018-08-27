package org.inek.dataportal.cert.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.inek.dataportal.cert.enums.CertStatus;
import org.inek.dataportal.common.data.account.entities.Account;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "Grouper", schema = "crt")
@SuppressWarnings("JavaNCSS")
public class Grouper implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grId")
    private Integer _id;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Version">
    @Column(name = "grVersion")
    @Version
    private int _version;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SystemId">
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grSystemId")
    private RemunerationSystem _system;

    public RemunerationSystem getSystem() {
        return _system;
    }

    public void setSystem(RemunerationSystem system) {
        this._system = system;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "grAccountId")
    private int _accountId = -1;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Account">
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "grAccountId", updatable = false, insertable = false)
    private Account _account;

    public Account getAccount() {
        return _account;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CertStatus">
    @Column(name = "grCertStatus")
    private int _certStatus = -1;

    public CertStatus getCertStatus() {
        return CertStatus.fromStatus(_certStatus);
    }

    public void setCertStatus(CertStatus certStatus) {
        _certStatus = certStatus.getStatus();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "grName")
    private String _name = "";

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name.trim();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property EmailCopy">
    @Column(name = "grEmailCopy")
    private String _emailCopy = "";

    public String getEmailCopy() {
        return _emailCopy;
    }

    public void setEmailCopy(String emailCopy) {
        _emailCopy = emailCopy;
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

    // <editor-fold defaultstate="collapsed" desc="Property DownloadSpec">
    @Column(name = "grDownloadSpec")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _downloadSpec;

    public Date getDownloadSpec() {
        return _downloadSpec;
    }

    public void setDownloadSpec(Date downloadSpec) {
        _downloadSpec = downloadSpec;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DownloadTest">
    @Column(name = "grDownloadTest")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _downloadTest;

    public Date getDownloadTest() {
        return _downloadTest;
    }

    public void setDownloadTest(Date downloadTest) {
        _downloadTest = downloadTest;
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
    private int _testError1 = -1;

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
    private int _testError2 = -1;

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
    private int _testError3 = -1;

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

    // <editor-fold defaultstate="collapsed" desc="Property DownloadCert">
    @Column(name = "grDownloadCert")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _downloadCert;

    public Date getDownloadCert() {
        return _downloadCert;
    }

    public void setDownloadCert(Date downloadCert) {
        _downloadCert = downloadCert;
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
    private int _certError1 = -1;

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
    private int _certError2 = -1;

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

    @Column(name = "grWebsiteRelease")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _websiteRelease;

    public Date getWebsiteRelease() {
        return _websiteRelease;
    }

    public void setWebsiteRelease(Date websiteRelease) {
        _websiteRelease = websiteRelease;
    }

    @Column(name = "grApprovedUntil")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _approvedUntil;

    public Date getApprovedUntil() {
        return _approvedUntil;
    }

    public void setApprovedUntil(Date approvedUntil) {
        this._approvedUntil = approvedUntil;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this._version;
        hash = 83 * hash + Objects.hashCode(this._system);
        hash = 83 * hash + this._accountId;
        hash = 83 * hash + Objects.hashCode(this._account);
        hash = 83 * hash + this._certStatus;
        hash = 83 * hash + Objects.hashCode(this._name);
        hash = 83 * hash + Objects.hashCode(this._emailCopy);
        hash = 83 * hash + Objects.hashCode(this._passwordRequest);
        hash = 83 * hash + Objects.hashCode(this._downloadSpec);
        hash = 83 * hash + Objects.hashCode(this._downloadTest);
        hash = 83 * hash + Objects.hashCode(this._testUpload1);
        hash = 83 * hash + Objects.hashCode(this._testCheck1);
        hash = 83 * hash + this._testError1;
        hash = 83 * hash + Objects.hashCode(this._testUpload2);
        hash = 83 * hash + Objects.hashCode(this._testCheck2);
        hash = 83 * hash + this._testError2;
        hash = 83 * hash + Objects.hashCode(this._testUpload3);
        hash = 83 * hash + Objects.hashCode(this._testCheck3);
        hash = 83 * hash + this._testError3;
        hash = 83 * hash + Objects.hashCode(this._certUpload1);
        hash = 83 * hash + Objects.hashCode(this._downloadCert);
        hash = 83 * hash + Objects.hashCode(this._certCheck1);
        hash = 83 * hash + this._certError1;
        hash = 83 * hash + Objects.hashCode(this._certUpload2);
        hash = 83 * hash + Objects.hashCode(this._certCheck2);
        hash = 83 * hash + this._certError2;
        hash = 83 * hash + Objects.hashCode(this._certification);
        hash = 83 * hash + Objects.hashCode(this._websiteRelease);
        hash = 83 * hash + Objects.hashCode(this._approvedUntil);
        return hash;
    }

    @SuppressWarnings("CyclomaticComplexity")
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
        final Grouper other = (Grouper) obj;
        if (this._version != other._version) {
            return false;
        }
        if (this._accountId != other._accountId) {
            return false;
        }
        if (this._certStatus != other._certStatus) {
            return false;
        }
        if (this._testError1 != other._testError1) {
            return false;
        }
        if (this._testError2 != other._testError2) {
            return false;
        }
        if (this._testError3 != other._testError3) {
            return false;
        }
        if (this._certError1 != other._certError1) {
            return false;
        }
        if (this._certError2 != other._certError2) {
            return false;
        }
        if (!Objects.equals(this._name, other._name)) {
            return false;
        }
        if (!Objects.equals(this._emailCopy, other._emailCopy)) {
            return false;
        }
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        if (!Objects.equals(this._system, other._system)) {
            return false;
        }
        if (!Objects.equals(this._account, other._account)) {
            return false;
        }
        if (!Objects.equals(this._passwordRequest, other._passwordRequest)) {
            return false;
        }
        if (!Objects.equals(this._downloadSpec, other._downloadSpec)) {
            return false;
        }
        if (!Objects.equals(this._downloadTest, other._downloadTest)) {
            return false;
        }
        if (!Objects.equals(this._testUpload1, other._testUpload1)) {
            return false;
        }
        if (!Objects.equals(this._testCheck1, other._testCheck1)) {
            return false;
        }
        if (!Objects.equals(this._testUpload2, other._testUpload2)) {
            return false;
        }
        if (!Objects.equals(this._testCheck2, other._testCheck2)) {
            return false;
        }
        if (!Objects.equals(this._testUpload3, other._testUpload3)) {
            return false;
        }
        if (!Objects.equals(this._testCheck3, other._testCheck3)) {
            return false;
        }
        if (!Objects.equals(this._certUpload1, other._certUpload1)) {
            return false;
        }
        if (!Objects.equals(this._downloadCert, other._downloadCert)) {
            return false;
        }
        if (!Objects.equals(this._certCheck1, other._certCheck1)) {
            return false;
        }
        if (!Objects.equals(this._certUpload2, other._certUpload2)) {
            return false;
        }
        if (!Objects.equals(this._certCheck2, other._certCheck2)) {
            return false;
        }
        if (!Objects.equals(this._certification, other._certification)) {
            return false;
        }
        if (!Objects.equals(this._websiteRelease, other._websiteRelease)) {
            return false;
        }
        if (!Objects.equals(this._approvedUntil, other._approvedUntil)) {
            return false;
        }
        return true;
    }

}
