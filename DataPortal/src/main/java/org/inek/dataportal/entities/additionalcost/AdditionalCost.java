/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.additionalcost;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import org.inek.dataportal.entities.iface.StatusEntity;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author aitbellayo
 */
@Entity
@Table(name = "AdditionalCost", schema = "adc")
public class AdditionalCost implements Serializable, StatusEntity {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adcId")
    private int _id;

    @Override
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AcountId">
    @Column(name = "adcAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "adcLastChanged")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _adcLastChanged;

    public Date getAdcLastChanged() {
        return _adcLastChanged;
    }

    public void setAdcLastChanged(Date adcLastChanged) {
        this._adcLastChanged = adcLastChanged;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Prperty Ik">
    @Column(name = "adcIk")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int acIk) {
        this._ik = acIk;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Hospital">
    @Column(name = "adcHospital")
    private String _hospital = "";

    public String getHospital() {
        return _hospital;
    }

    public void setHospital(String hospital) {
        this._hospital = hospital;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Street">
    @Column(name = "adcStreet")
    private String _street = "";

    public String getStreet() {
        return _street;
    }

    public void setStreet(String street) {
        this._street = street;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Zip">
    @Column(name = "adcZip")
    private String _zip = "";

    public String getZip() {
        return _zip;
    }

    public void setZip(String zip) {
        this._zip = zip;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="City">
    @Column(name = "adcCity")
    private String _city = "";

    public String getCity() {
        return _city;
    }

    public void setCity(String city) {
        this._city = city;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ContactFirstName">
    @Column(name = "adcContactFirstName")
    private String _contactFirstName = "";

    public String getContactFirstName() {
        return _contactFirstName;
    }

    public void setContactFirstName(String contacFirstName) {
        this._contactFirstName = contacFirstName;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ContactLastName">
    @Column(name = "adcContactLastName")
    private String _contactLastName = "";

    public String getContactLastName() {
        return _contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this._contactLastName = contactLastName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ContactPhone">
    @Column(name = "adcContactPhone")
    private String _contactPhone = "";

    public String getContactPhone() {
        return _contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this._contactPhone = contactPhone;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ContactEmail">
    @Column(name = "adcContactEmail")
    private String _contactEmail = "";

    public String getContactEmail() {
        return _contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this._contactEmail = contactEmail;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Status">
    @Column(name = "adcStatus")
    private int _statusId;

    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        this._statusId = statusId;
    }

    @Documentation(key = "lblWorkstate", rank = 10)
    @Override
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    @Override
    public void setStatus(WorkflowStatus status) {
        _statusId = status.getId();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Gender">
    @Column(name = "adcGender")
    private int _gender;

    public int getGender() {
        return _gender;
    }

    public void setGender(int gender) {
        this._gender = gender;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PerinatalcentreLevel">
    @Column(name = "adcPerinatalcentreLevel")
    private int _perinatalcentreLevel;

    public int getPerinatalcentreLevel() {
        return _perinatalcentreLevel;
    }

    public void setPerinatalcentreLevel(int perinatalcentreLevel) {
        this._perinatalcentreLevel = perinatalcentreLevel;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PeriodFrom">
    @Column(name = "adcPeriodFrom")
    private int _periodFrom;

    public int getPeriodFrom() {
        return _periodFrom;
    }

    public void setPeriodFrom(int periodFrom) {
        this._periodFrom = periodFrom;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PeriodTo">
    @Column(name = "adcPeriodTo")
    private int _periodTo;

    public int getPeriodTo() {
        return _periodTo;
    }

    public void setPeriodTo(int periodTo) {
        this._periodTo = periodTo;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EffectivCaseMix">
    @Column(name = "adcEffectivCaseMix")
    private double _effectivCaseMix;

    public double getEffectivCaseMix() {
        return _effectivCaseMix;
    }

    public void setEffectivCaseMix(double effectivCaseMix) {
        this._effectivCaseMix = effectivCaseMix;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ExtraChargeA">
    @Column(name = "adcExtraChargeA")
    private double _extraChargeA;

    public double getExtraChargeA() {
        return _extraChargeA;
    }

    public void setExtraChargeA(double extraChargeA) {
        this._extraChargeA = extraChargeA;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ExtraChargeB">
    @Column(name = "adcExtraChargeB")
    private double _extraChargeB;

    public double getExtraChargeB() {
        return _extraChargeB;
    }

    public void setExtraChargeB(double extraChargeB) {
        this._extraChargeB = extraChargeB;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ExtraChargeC">
    @Column(name = "adcExtraChargeC")
    private double _extraChargeC;

    public double getExtraChargeC() {
        return _extraChargeC;
    }

    public void setExtraChargeC(double extraChargeC) {
        this._extraChargeC = extraChargeC;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="AgreedHospitalIndividualExtraCharge">
    @Column(name = "adcAgreedHospitalIndividualExtraCharge")
    private double _agreedHospitalIndividualExtraCharge;

    public double getAgreedHospitalIndividualExtraCharge() {
        return _agreedHospitalIndividualExtraCharge;
    }

    public void setAgreedHospitalIndividualExtraCharge(double agreedHospitalIndividualExtraCharge) {
        this._agreedHospitalIndividualExtraCharge = agreedHospitalIndividualExtraCharge;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="AgreedRepaymentAdditionalCost">
    @Column(name = "adcAgreedRepaymentAdditionalCost")
    private double _agreedRepaymentAdditionalCost;

    public double getAgreedRepaymentAdditionalCost() {
        return _agreedRepaymentAdditionalCost;
    }

    public void setAgreedRepaymentAdditionalCost(double agreedRepaymentAdditionalCost) {
        this._agreedRepaymentAdditionalCost = agreedRepaymentAdditionalCost;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="RepaymentPeriodFrom">
    @Column(name = "adcRepaymentPeriodFrom")
    private int _repaymentPeriodFrom;

    public int getRepaymentPeriodFrom() {
        return _repaymentPeriodFrom;
    }

    public void setRepaymentPeriodFrom(int repaymentPeriodFrom) {
        this._repaymentPeriodFrom = repaymentPeriodFrom;
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="RepaymentPeriodTo">
    @Column(name = "adcRepaymentPeriodTo")
    private int _repaymentPeriodTo;

    public int getRepaymentPeriodTo() {
        return _repaymentPeriodTo;
    }

    public void setRepaymentPeriodTo(int repaymentPeriodTo) {
        this._repaymentPeriodTo = repaymentPeriodTo;
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="CalenderYear">
    @Column(name = "adcCalenderYear")
    private int _calenderYear;

    public int getCalenderYear() {
        return _calenderYear;
    }

    public void setCalenderYear(int calenderYear) {
        this._calenderYear = calenderYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="HospitalIndividualExtraCharge">
    @Column(name = "adcHospitalIndividualExtraCharge")
    private double _hospitalIndividualExtraCharge;

    @Min(0)
    @Max(100)
    public double getHospitalIndividualExtraCharge() {
        return _hospitalIndividualExtraCharge;
    }

    public void setHospitalIndividualExtraCharge(double hospitalIndividualExtraCharge) {
        this._hospitalIndividualExtraCharge = hospitalIndividualExtraCharge;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="IsAdditionalCostAgreementAgreed">
    @Column(name = "adcIsAdditionalCostAgreementAgreed")
    private boolean _isAdditionalCostAgreementAgreed;
    
    public boolean isIsAdditionalCostAgreementAgreed() {
        return _isAdditionalCostAgreementAgreed;
    }
    
    public void setIsAdditionalCostAgreementAgreed(boolean isAdditionalCostAgreementAgreed) {
        this._isAdditionalCostAgreementAgreed = isAdditionalCostAgreementAgreed;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ComplianceRate">
    @Column(name = "adcComplianceRate")
    private double _complianceRate;

    @Min(0)
    @Max(100)
    public double getComplianceRate() {
        return _complianceRate;
    }

    public void setComplianceRate(double complianceRate) {
        this._complianceRate = complianceRate;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Code">
    @Column(name = "adcCode")
    @Documentation(key = "lblContractKey")
    private String _code = "";

    @Size(max = 10)
    public String getCode() {
        return _code;
    }

    public void setCode(String code) {
        _code = code == null ? "" : code;
    }
    //</editor-fold>
}
