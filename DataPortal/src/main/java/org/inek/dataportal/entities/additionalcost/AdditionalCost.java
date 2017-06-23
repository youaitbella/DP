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
import org.inek.dataportal.enums.WorkflowStatus;

/**
 *
 * @author aitbellayo
 */
@Entity
@Table(name="AdditionalCost", schema = "add")
public class AdditionalCost implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acId")
    private Integer _id;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer _id) {
        this._id = _id;
    }
    
    @Column(name="acAcountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int _accountId) {
        this._accountId = _accountId;
    }
    
    @Column(name="acIk")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int _acIk) {
        this._ik = _acIk;
    }
    
    @Column(name="acHoslital")
    private String _hospital;

    public String getHospital() {
        return _hospital;
    }

    public void setHospital(String _hospital) {
        this._hospital = _hospital;
    }
    
    @Column(name="acStreet")
    private String _street;

    public String getStreet() {
        return _street;
    }

    public void setStreet(String _street) {
        this._street = _street;
    }
    
    @Column(name="acZip")
    private String _zip;

    public String getZip() {
        return _zip;
    }

    public void setZip(String _zip) {
        this._zip = _zip;
    }
    
    @Column(name="acCity")
    private String _city;

    public String getCity() {
        return _city;
    }

    public void setCity(String _city) {
        this._city = _city;
    }
    
    @Column(name="acContactFirstName")
    private String _contactFirstName;

    public String getContactFirstName() {
        return _contactFirstName;
    }

    public void setContactFirstName(String _contacFirstName) {
        this._contactFirstName = _contacFirstName;
    }
    
    @Column(name="acContactLastName")
    private String _contactLastName;

    public String getContactLastName() {
        return _contactLastName;
    }

    public void setContactLastName(String _contactLastName) {
        this._contactLastName = _contactLastName;
    }
    
    @Column(name = "acContactPhone")
    private String _contactPhone;

    public String getContactPhone() {
        return _contactPhone;
    }

    public void setContactPhone(String _contactPhone) {
        this._contactPhone = _contactPhone;
    }
    
    @Column(name = "acContactEmail")
    private String _contactEmail;

    public String getContactEmail() {
        return _contactEmail;
    }

    public void setContactEmail(String _contactEmail) {
        this._contactEmail = _contactEmail;
    }
    
    @Column(name = "acStatus")
    private WorkflowStatus _status = WorkflowStatus.New;

    public WorkflowStatus getStatus() {
        return _status;
    }

    public void setStatus(WorkflowStatus _status) {
        this._status = _status;
    }
    
    @Column(name = "acGender")
    private int _gender;

    public int getGender() {
        return _gender;
    }

    public void setGender(int _gender) {
        this._gender = _gender;
    }
    
    @Column(name="acPerinatalcentreLevel")//
    private int _perinatalcentreLevel;

    public int getPerinatalcentreLevel() {
        return _perinatalcentreLevel;
    }

    public void setPerinatalcentreLevel(int _perinatalcentreLevel) {
        this._perinatalcentreLevel = _perinatalcentreLevel;
    }
    
    @Column(name="acBudgetYear")//String od. int
    private int _budgetYear;

    public int getBudgetYear() {
        return _budgetYear;
    }

    public void setBudgetYear(int _budgetYear) {
        this._budgetYear = _budgetYear;
    }
        
    @Column(name = "acEffectivCaseMix")
    private double _effectivCaseMix;

    public double getEffectivCaseMix() {
        return _effectivCaseMix;
    }

    public void setEffectivCaseMix(double _effectivCaseMix) {
        this._effectivCaseMix = _effectivCaseMix;
    }
    
    @Column(name="acExtraChargeA")
     private double _extraChargeA;

    public double getExtraChargeA() {
        return _extraChargeA;
    }

    public void setExtraChargeA(double _extraChargeA) {
        this._extraChargeA = _extraChargeA;
    }
     
    @Column(name="acExtraChargeB")
     private double _extraChargeB;

    public double getExtraChargeB() {
        return _extraChargeB;
    }

    public void setExtraChargeB(double _extraChargeB) {
        this._extraChargeB = _extraChargeB;
    }

    @Column(name="acExtraChargeC")
     private double _extraChargeC;

    public double getExtraChargeC() {
        return _extraChargeC;
    }

    public void setExtraChargeC(double _extraChargeC) {
        this._extraChargeC = _extraChargeC;
    }
    
    @Column(name="acAgreedAmountExtraCharge")
     private double _agreedAmountExtraCharge;

    public double getAgreedAmountExtraCharge() {
        return _agreedAmountExtraCharge;
    }

    public void setAgreedAmountExtraCharge(double _agreedAmountExtraCharge) {
        this._agreedAmountExtraCharge = _agreedAmountExtraCharge;
    }

    
     
    @Column(name="acHospitalIndividualExtraCharge")
    private double _hospitalIndividualExtraCharge;

    public double getHospitalIndividualExtraCharge() {
        return _hospitalIndividualExtraCharge;
    }

    public void setHospitalIndividualExtraCharge(double _hospitalIndividualExtraCharge) {
        this._hospitalIndividualExtraCharge = _hospitalIndividualExtraCharge;
    }
    
    @Column(name = "acIsAdditionalCostAgreementAgreed")
    private boolean _isAdditionalCostAgreementAgreed;

    public boolean isIsAdditionalCostAgreementAgreed() {
        return _isAdditionalCostAgreementAgreed;
    }

    public void setIsAdditionalCostAgreementAgreed(boolean _isAdditionalCostAgreementAgreed) {
        this._isAdditionalCostAgreementAgreed = _isAdditionalCostAgreementAgreed;
    }
    
    
    @Column(name = "acComplianceRateAmount")
    private double _complianceRateAmount;

    public double getComplianceRateAmount() {
        return _complianceRateAmount;
    }

    public void setComplianceRateAmount(double _complianceRateAmount) {
        this._complianceRateAmount = _complianceRateAmount;
    }
    
    @Column(name = "acComplianceRate")
    private double _complianceRate;

    public double getComplianceRate() {
        return _complianceRate;
    }

    public void setComplianceRate(double _complianceRate) {
        this._complianceRate = _complianceRate;
    }
    
}
