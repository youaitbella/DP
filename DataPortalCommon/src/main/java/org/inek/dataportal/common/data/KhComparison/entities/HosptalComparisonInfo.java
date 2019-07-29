package org.inek.dataportal.common.data.KhComparison.entities;

import org.inek.dataportal.common.data.KhComparison.enums.PsyGroup;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "HosptalComparisonInfo", schema = "psy")
public class HosptalComparisonInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hciId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property HospitalComparisonId">
    @Column(name = "hciHcId")
    private String _hospitalComparisonId;

    public String getHospitalComparisonId() {
        return _hospitalComparisonId;
    }

    public void setHospitalComparisonId(String hospitalComparisonId) {
        this._hospitalComparisonId = hospitalComparisonId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AgreementYear">
    @Column(name = "hciAgreementYear")
    private int _agreementYear;

    public int getAgreementYear() {
        return _agreementYear;
    }

    public void setAgreementYear(int agreementYear) {
        _agreementYear = agreementYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CreatedAt">
    @Column(name = "hciCreatedAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _createdAt;

    public Date getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        _createdAt = createdAt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "hciAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccountFirstName">
    @Column(name = "hciAccountFirstName")
    private String _accountFirstName;

    public String getAccountFirstName() {
        return _accountFirstName;
    }

    public void setAccountFirstName(String accountFirstName) {
        this._accountFirstName = accountFirstName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccountLastName">
    @Column(name = "hciAccountLastName")
    private String _accountLastName;

    public String getAccountLastName() {
        return _accountLastName;
    }

    public void setAccountLastName(String accountLastName) {
        this._accountLastName = accountLastName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property hciHospitalPsyGroupId">
    @Column(name = "hciHospitalPsyGroupId")
    private int _hospitalPsyGroupId;

    public PsyGroup getHospitalPsyGroup() {
        return PsyGroup.getById(_hospitalPsyGroupId);
    }

    public void setHospitalPsyGroup(PsyGroup psyGroup) {
        this._hospitalPsyGroupId = psyGroup.getId();
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property HospitalStateId">
    @Column(name = "hciHospitalStateId")
    private int _hospitalStateId;

    public int getHospitalStateId() {
        return _hospitalStateId;
    }

    public void setHospitalStateId(int hospitalStateId) {
        this._hospitalStateId = hospitalStateId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property HospitalIk">
    @Column(name = "hciHospitalIk")
    private int _hospitalIk;

    public int getHospitalIk() {
        return _hospitalIk;
    }

    public void setHospitalIk(int hospitalIk) {
        this._hospitalIk = hospitalIk;
    }
    //</editor-fold>

    @OneToMany(mappedBy = "_hosptalComparisonInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hceHosptalComparisonInfoId")
    private List<HosptalComparisonEvaluation> _hosptalComparisonEvaluation  = new ArrayList<>();

    public List<HosptalComparisonEvaluation> getHosptalComparisonEvaluation() {
        return _hosptalComparisonEvaluation;
    }

    public void setHosptalComparisonEvaluation(List<HosptalComparisonEvaluation> hosptalComparisonEvaluation) {
        this._hosptalComparisonEvaluation = hosptalComparisonEvaluation;
    }

    public void addHosptalComparisonEvaluation(HosptalComparisonEvaluation evaluation) {
        evaluation.setHosptalComparisonInfo(this);
        _hosptalComparisonEvaluation.add(evaluation);
    }


    @OneToOne(mappedBy = "_hosptalComparisonInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private HosptalComparisonJob _hosptalComparisonJob;

    public HosptalComparisonJob getHosptalComparisonJob() {
        return _hosptalComparisonJob;
    }

    public void setHosptalComparisonJob(HosptalComparisonJob hosptalComparisonJob) {
        hosptalComparisonJob.setHosptalComparisonInfo(this);
        this._hosptalComparisonJob = hosptalComparisonJob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HosptalComparisonInfo that = (HosptalComparisonInfo) o;
        return _agreementYear == that._agreementYear &&
                _accountId == that._accountId &&
                _hospitalPsyGroupId == that._hospitalPsyGroupId &&
                _hospitalStateId == that._hospitalStateId &&
                _hospitalIk == that._hospitalIk &&
                Objects.equals(_hospitalComparisonId, that._hospitalComparisonId) &&
                Objects.equals(_createdAt, that._createdAt) &&
                Objects.equals(_accountFirstName, that._accountFirstName) &&
                Objects.equals(_accountLastName, that._accountLastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_hospitalComparisonId, _agreementYear, _createdAt, _accountId, _accountFirstName, _accountLastName,
                _hospitalPsyGroupId, _hospitalStateId, _hospitalIk);
    }
}
