/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.additionalcost;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.inek.dataportal.enums.WorkflowStatus;

/**
 *
 * @author aitbellayo
 */
@Entity
@Table(name = "AdditionalCost", schema = "add")
public class AdditionalCost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acId")
    private Integer _id;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        this._id = id;
    }

    @Column(name = "acAcountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }

    @Column(name = "acIk")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int acIk) {
        this._ik = acIk;
    }

    @Column(name = "acHospital")
    private String _hospital;

    public String getHospital() {
        return _hospital;
    }

    public void setHospital(String hospital) {
        this._hospital = hospital;
    }

    @Column(name = "acStreet")
    private String _street;

    public String getStreet() {
        return _street;
    }

    public void setStreet(String street) {
        this._street = street;
    }

    @Column(name = "acZip")
    private String _zip;

    public String getZip() {
        return _zip;
    }

    public void setZip(String zip) {
        this._zip = zip;
    }

    @Column(name = "acCity")
    private String _city;

    public String getCity() {
        return _city;
    }

    public void setCity(String city) {
        this._city = city;
    }

    @Column(name = "acContactFirstName")
    private String _contactFirstName;

    public String getContactFirstName() {
        return _contactFirstName;
    }

    public void setContactFirstName(String contacFirstName) {
        this._contactFirstName = contacFirstName;
    }

    @Column(name = "acContactLastName")
    private String _contactLastName;

    public String getContactLastName() {
        return _contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this._contactLastName = contactLastName;
    }

    @Column(name = "acContactPhone")
    private String _contactPhone;

    public String getContactPhone() {
        return _contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this._contactPhone = contactPhone;
    }

    @Column(name = "acContactEmail")
    private String _contactEmail;

    public String getContactEmail() {
        return _contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this._contactEmail = contactEmail;
    }

    @Column(name = "acStatus")
    private WorkflowStatus _status = WorkflowStatus.New;

    public WorkflowStatus getStatus() {
        return _status;
    }

    public void setStatus(WorkflowStatus status) {
        this._status = status;
    }

    @Column(name = "acGender")
    private int _gender;

    public int getGender() {
        return _gender;
    }

    public void setGender(int gender) {
        this._gender = gender;
    }

    @Column(name = "acPerinatalcentreLevel")
    private int _perinatalcentreLevel;

    public int getPerinatalcentreLevel() {
        return _perinatalcentreLevel;
    }

    public void setPerinatalcentreLevel(int perinatalcentreLevel) {
        this._perinatalcentreLevel = perinatalcentreLevel;
    }
    
    @Column(name = "acPeriodTo")
    private int periodTo;

    public int getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(int periodTo) {
        this.periodTo = periodTo;
    }
    
    @Column(name = "acPeriodFrom")
    private int periodFrom;

    public int getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(int periodFrom) {
        this.periodFrom = periodFrom;
    }
    
    @Column(name = "acEffectivCaseMix")
    private double _effectivCaseMix;

    public double getEffectivCaseMix() {
        return _effectivCaseMix;
    }

    public void setEffectivCaseMix(double effectivCaseMix) {
        this._effectivCaseMix = effectivCaseMix;
    }

    @Column(name = "acExtraChargeA")
    private double _extraChargeA;

    public double getExtraChargeA() {
        return _extraChargeA;
    }

    public void setExtraChargeA(double extraChargeA) {
        this._extraChargeA = extraChargeA;
    }

    @Column(name = "acExtraChargeB")
    private double _extraChargeB;

    public double getExtraChargeB() {
        return _extraChargeB;
    }

    public void setExtraChargeB(double extraChargeB) {
        this._extraChargeB = extraChargeB;
    }

    @Column(name = "acExtraChargeC")
    private double _extraChargeC;

    public double getExtraChargeC() {
        return _extraChargeC;
    }

    public void setExtraChargeC(double extraChargeC) {
        this._extraChargeC = extraChargeC;
    }

    @Column(name = "acAgreedHospitalIndividualExtraCharge")
    private double _agreedHospitalIndividualExtraCharge;

    public double getAgreedHospitalIndividualExtraCharge() {
        return _agreedHospitalIndividualExtraCharge;
    }

    public void setAgreedHospitalIndividualExtraCharge(double agreedHospitalIndividualExtraCharge) {
        this._agreedHospitalIndividualExtraCharge = agreedHospitalIndividualExtraCharge;
    }

    @Column(name="acAgreedRepaymentAdditionalCost")
    private double agreedRepaymentAdditionalCost;

    public double getAgreedRepaymentAdditionalCost() {
        return agreedRepaymentAdditionalCost;
    }

    public void setAgreedRepaymentAdditionalCost(double agreedRepaymentAdditionalCost) {
        this.agreedRepaymentAdditionalCost = agreedRepaymentAdditionalCost;
    }
    
    @Column(name="acRepaymentPeriodFrom")
    private int _repaymentPeriodFrom;

    public int getRepaymentPeriodFrom() {
        return _repaymentPeriodFrom;
    }

    public void setRepaymentPeriodFrom(int repaymentPeriodFrom) {
        this._repaymentPeriodFrom = repaymentPeriodFrom;
    }

    @Column(name="acRepaymentPeriodTo")
    private int _repaymentPeriodTo;

    public int getRepaymentPeriodTo() {
        return _repaymentPeriodTo;
    }

    public void setRepaymentPeriodTo(int repaymentPeriodTo) {
        this._repaymentPeriodTo = repaymentPeriodTo;
    }

    @Column(name="acCalenderYear")
    private int calenderYear;

    public int getCalenderYear() {
        return calenderYear;
    }

    public void setCalenderYear(int calenderYear) {
        this.calenderYear = calenderYear;
    }
    
    @Column(name = "acHospitalIndividualExtraCharge")
    private double _hospitalIndividualExtraCharge;

    @Min(0)
    @Max(100)
    public double getHospitalIndividualExtraCharge() {
        return _hospitalIndividualExtraCharge;
    }

    public void setHospitalIndividualExtraCharge(double hospitalIndividualExtraCharge) {
        this._hospitalIndividualExtraCharge = hospitalIndividualExtraCharge;
    }

    @Column(name = "acIsAdditionalCostAgreementAgreed")
    private boolean _isAdditionalCostAgreementAgreed;

    public boolean isIsAdditionalCostAgreementAgreed() {
        return _isAdditionalCostAgreementAgreed;
    }

    public void setIsAdditionalCostAgreementAgreed(boolean isAdditionalCostAgreementAgreed) {
        this._isAdditionalCostAgreementAgreed = isAdditionalCostAgreementAgreed;
    }

    @Column(name = "acComplianceRateAmount")
    private double _complianceRateAmount;

    public double getComplianceRateAmount() {
        return _complianceRateAmount;
    }

    public void setComplianceRateAmount(double complianceRateAmount) {
        this._complianceRateAmount = complianceRateAmount;
    }

    @Column(name = "acComplianceRate")
    private double _complianceRate;

    public double getComplianceRate() {
        return _complianceRate;
    }

    public void setComplianceRate(double complianceRate) {
        this._complianceRate = complianceRate;
    }
}
