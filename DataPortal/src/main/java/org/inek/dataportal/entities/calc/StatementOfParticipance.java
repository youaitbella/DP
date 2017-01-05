/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "StatementOfParticipance", schema = "calc")
public class StatementOfParticipance implements Serializable{
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sopId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DataYear">
    @Column(name = "sopDataYear")
    private int _dataYear = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        this._dataYear = dataYear;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IK">
    @Column(name = "sopIK")
    private int _ik;
    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="hospitalName">
    @NotNull
    @Size(max = 150)
    @Column(name = "sopHospitalName")
    private String sopHospitalName = "";
    
    public String getSopHospitalName() {
        return sopHospitalName;
    }
    
    public void setSopHospitalName(String sopHospitalName) {
        this.sopHospitalName = sopHospitalName;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "sopAccountId")
    private int _accountId;
    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property StatusId / Status">
    @Column(name = "sopStatusId")
    private int _statusId;
    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        _statusId = statusId;
    }
    
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    public void setStatus(WorkflowStatus status) {
        _statusId = status.getValue();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "sopLastChanged")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastChanged =  Calendar.getInstance().getTime();
    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        _lastChanged = lastChanged;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property IsDrgCalc">
    @Column(name = "sopIsDrg")
    private boolean _drgCalc = true;
    public boolean isDrgCalc() {
        return _drgCalc;
    }

    public void setDrgCalc(boolean drgCalc) {
        _drgCalc = drgCalc;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsPsyCalc">
    @Column(name = "sopIsPsy")
    private boolean _psyCalc = true;
    public boolean isPsyCalc() {
        return _psyCalc;
    }

    public void setPsyCalc(boolean psyCalc) {
        _psyCalc = psyCalc;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsInvCalc">
    @Column(name = "sopIsInv")
    private boolean _invCalc;
    public boolean isInvCalc() {
        return _invCalc;
    }

    public void setInvCalc(boolean invCalc) {
        _invCalc = invCalc;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsTpgCalc">
    @Column(name = "sopIsTpg")
    private boolean _tpgCalc;
    public boolean isTpgCalc() {
        return _tpgCalc;
    }

    public void setTpgCalc(boolean tpgCalc) {
        _tpgCalc = tpgCalc;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ClinicalDistributionModelDrg">
    @Column(name = "sopCdmDrg")
    private int _clinicalDistributionModelDrg = -1;
    public int getClinicalDistributionModelDrg() {
        return _clinicalDistributionModelDrg;
    }

    public void setClinicalDistributionModelDrg(int clinicalDistributionModelDrg) {
        _clinicalDistributionModelDrg = clinicalDistributionModelDrg;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ClinicalDistributionModelPsy">
    @Column(name = "sopCdmPsy")
    private int _clinicalDistributionModelPsy = -1;
    public int getClinicalDistributionModelPsy() {
        return _clinicalDistributionModelPsy;
    }

    public void setClinicalDistributionModelPsy(int clinicalDistributionModelPsy) {
        _clinicalDistributionModelPsy = clinicalDistributionModelPsy;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property MultiyearDrg">
    @Column(name = "sopMultiyearDrg")
    private String _multiyearDrg = "";
    public String getMultiyearDrg() {
        return _multiyearDrg;
    }

    public void setMultiyearDrg(String multiyearDrg) {
        _multiyearDrg = multiyearDrg;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property MultiyearDrgText">
    @Column(name = "sopMultiyearDrgText")
    private String _multiyearDrgText = "";
    public String getMultiyearDrgText() {
        return _multiyearDrgText;
    }

    public void setMultiyearDrgText(String multiyearDrgText) {
        _multiyearDrgText = multiyearDrgText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property MultiyearPsy">
    @Column(name = "sopMultiyearPsy")
    private String _multiyearPsy = "";
    public String getMultiyearPsy() {
        return _multiyearPsy;
    }

    public void setMultiyearPsy(String multiyearPsy) {
        _multiyearPsy = multiyearPsy;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property MultiyearPsyText">
    @Column(name = "sopMultiyearPsyText")
    private String _multiyearPsyText = "";
    public String getMultiyearPsyText() {
        return _multiyearPsyText;
    }

    public void setMultiyearPsyText(String multiyearPsyText) {
        _multiyearPsyText = multiyearPsyText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Section">
    @Column(name = "sopSection")
    private boolean _section;
    public boolean isSection() {
        return _section;
    }

    public void setSection(boolean section) {
        _section = section;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SectionExtern">
    @Column(name = "sopSectionExtern")
    private boolean _sectionExtern;
    public boolean isSectionExtern() {
        return _sectionExtern;
    }

    public void setSectionExtern(boolean sectionExtern) {
        _sectionExtern = sectionExtern;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SectionCount">
    @Column(name = "sopSectionCount")
    private int _sectionCount;
    public int isSectionCount() {
        return _sectionCount;
    }

    public void setSectionCount(int sectionCount) {
        _sectionCount = sectionCount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DrgCare">
    @Column(name = "sopDrgCare")
    private String _drgCare = "";
    public String getDrgCare() {
        return _drgCare;
    }

    public void setDrgCare(String drgCare) {
        _drgCare = drgCare;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PsyDataIntensive">
    @Column(name = "sopPsyDataIntensive")
    private boolean _psyDataIntensive;
    public boolean isPsyDataIntensive() {
        return _psyDataIntensive;
    }

    public void setPsyDataIntensive(boolean psyDataIntensive) {
        _psyDataIntensive = psyDataIntensive;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property WithConsultant">
    @Column(name = "sopIsWithConsultant")
    private boolean _withConsultant;
    public boolean isWithConsultant() {
        return _withConsultant;
    }

    public void setWithConsultant(boolean withConsultant) {
        _withConsultant = withConsultant;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ConsultantCompany">
    @Column(name = "sopConsultantCompany")
    private String _consultantCompany = "";
    public String getConsultantCompany() {
        return _consultantCompany;
    }

    public void setConsultantCompany(String consultantCompany) {
        _consultantCompany = consultantCompany;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ConsultantSendMail">
    @Column(name = "sopConsultantSendMail")
    private boolean _consultantSendMail;
    public boolean isConsultantSendMail() {
        return _consultantSendMail;
    }

    public void setConsultantSendMail(boolean consultantSendMail) {
        _consultantSendMail = consultantSendMail;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Contacts">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "coStatementOfParticipanceId", referencedColumnName = "sopId")
    private List<CalcContact> _contacts = new Vector<>();

    public List<CalcContact> getContacts() {
        return _contacts;
    }

    public void setContacts(List<CalcContact> contacts) {
        _contacts = contacts;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this._id;
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
        final StatementOfParticipance other = (StatementOfParticipance) obj;
        return _id == other._id;
    }
    
    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.StatementOfParticipance[ id=" + _id + " ]";
    }
    
    // </editor-fold>

    @PrePersist
    @PreUpdate
    public void tagModifiedDate() {
        _lastChanged = Calendar.getInstance().getTime();
    }
    
}
