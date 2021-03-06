/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.sop;

import org.inek.dataportal.calc.entities.sop.CalcContact;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.inek.dataportal.common.data.iface.StatusEntity;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "StatementOfParticipance", schema = "calc")
public class StatementOfParticipance implements Serializable, StatusEntity{

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sopId", updatable = false, nullable = false)
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
    @Documentation(key = "lblYearData")
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
    @Documentation(key = "lblIK")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    // </editor-fold>

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

    @Documentation(key = "lblWorkstate", rank = 10)
    @Override
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    @Override
    public void setStatus(WorkflowStatus status) {
        _statusId = status.getId();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "sopLastChanged")
    @Documentation(name = "Stand")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastChanged = Calendar.getInstance().getTime();

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        _lastChanged = lastChanged;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accountIdLastChange">
    @Column(name = "sopLastChangedBy")
    private int _accountIdLastChange;

    public int getAccountIdLastChange() {
        return _accountIdLastChange;
    }

    public void setAccountIdLastChange(int accountId) {
        this._accountIdLastChange = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sealed">
    @Column(name = "sopSealed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _sealed = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    public Date getSealed() {
        return _sealed;
    }

    public void setSealed(Date sealed) {
        this._sealed = sealed;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Obligatory">
    @Column(name = "sopIsObligatory")
    private boolean _obligatory;

    public boolean isObligatory() {
        return _obligatory;
    }

    public void setObligatory(boolean obligatory) {
        _obligatory = obligatory;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Obligatory Following Year">
    @Column(name = "sopIsObligatoryFollowYears")
    private boolean _obligatoryFollowingYears;

    public boolean isObligatoryFollowingYears() {
        return _obligatoryFollowingYears;
    }

    public void setObligatoryFollowingYears(boolean obligatory) {
        _obligatoryFollowingYears = obligatory;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ObligatoryCalcType">
    @Column(name = "sopObligatoryCalcType")
    @Documentation(name = "Kalkulationstyp", omitOnValues = "0", translateValue = "1=vereinfachte Kalkulation;2=Probekalkulation")
    private int _obligatoryCalcType;

    public int getObligatoryCalcType() {
        return _obligatoryCalcType;
    }

    public void setObligatoryCalcType(int obligatoryCalcType) {
        _obligatoryCalcType = obligatoryCalcType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DrgCalc">
    @Column(name = "sopIsDrg")
    @Documentation(name = "Teilnahme DRG", omitOnValues = "false")
    private boolean _drgCalc;

    public boolean isDrgCalc() {
        return _drgCalc;
    }

    public void setDrgCalc(boolean drgCalc) {
        _drgCalc = drgCalc;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PsyCalc">
    @Column(name = "sopIsPsy")
    @Documentation(name = "Teilnahme PSY", omitOnValues = "false")
    private boolean _psyCalc;

    public boolean isPsyCalc() {
        return _psyCalc;
    }

    public void setPsyCalc(boolean psyCalc) {
        _psyCalc = psyCalc;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InvCalc">
    @Column(name = "sopIsInv")
    @Documentation(name = "Teilnahme INV", omitOnValues = "false")
    private boolean _invCalc;

    public boolean isInvCalc() {
        return _invCalc;
    }

    public void setInvCalc(boolean invCalc) {
        _invCalc = invCalc;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property TpgCalc">
    @Column(name = "sopIsTpg")
    @Documentation(name = "Teilnahme TPG", omitOnValues = "false")
    private boolean _tpgCalc;

    public boolean isTpgCalc() {
        return _tpgCalc;
    }

    public void setTpgCalc(boolean tpgCalc) {
        _tpgCalc = tpgCalc;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ObdCalc">
    @Column(name = "sopIsObd")
    @Documentation(name = "Teilnahme OBD", omitOnValues = "false")
    private boolean _obdCalc;

    public boolean isObdCalc() {
        return _obdCalc;
    }

    public void setObdCalc(boolean obdCalc) {
        _obdCalc = obdCalc;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ClinicalDistributionModelDrg">
    @Column(name = "sopCdmDrg")
    //@Documentation(name = "KVM DRG", omitOnValues = "-1;0", translateValue = "1=lblStatementSingleCostAttributionDrg")
    @Documentation(name = "KVM DRG", omitOnValues = "-1", translateValue = "0=Nein;1=Ja")
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
    @Documentation(name = "KVM PSY", omitOnValues = "-1", translateValue = "0=Nein;1=Ja")
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
    @Documentation(name = "Überlieger DRG", omitOnValues = "0", 
            translateValue = "4=chooseOverlayerAll;5=chooseOverlayerPartial;6=chooseOverlayerNone;15=chooseOverlayerAlternative")
    private int _multiyearDrg;

    public int getMultiyearDrg() {
        return _multiyearDrg;
    }

    public void setMultiyearDrg(int multiyearDrg) {
        _multiyearDrg = multiyearDrg;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property MultiyearDrgText">
    @Column(name = "sopMultiyearDrgText")
    @Documentation(name = "Beschreibung Alternative", omitOnEmpty = true)
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
    @Documentation(name = "Überlieger PSY", omitOnValues = "0", 
            translateValue = "4=chooseOverlayerAll;5=chooseOverlayerPartial;6=chooseOverlayerNone;15=chooseOverlayerAlternative")
    private int _multiyearPsy;

    public int getMultiyearPsy() {
        return _multiyearPsy;
    }

    public void setMultiyearPsy(int multiyearPsy) {
        _multiyearPsy = multiyearPsy;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property MultiyearPsyText">
    @Column(name = "sopMultiyearPsyText")
    @Documentation(name = "Beschreibung Alternative", omitOnEmpty = true)
    private String _multiyearPsyText = "";

    public String getMultiyearPsyText() {
        return _multiyearPsyText;
    }

    public void setMultiyearPsyText(String multiyearPsyText) {
        _multiyearPsyText = multiyearPsyText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property WithConsultant">
    @Column(name = "sopIsWithConsultant")
    @Documentation(name = "Mit externem Berater")
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
    @Documentation(key = "lblNameConsultant", omitOnEmpty = true)
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
    @Documentation(name = "Berater in Mailversand aufnehmen", omitOnValues = "false")
    private boolean _consultantSendMail;

    public boolean isConsultantSendMail() {
        return _consultantSendMail;
    }

    public void setConsultantSendMail(boolean consultantSendMail) {
        _consultantSendMail = consultantSendMail;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Contacts">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "coStatementOfParticipanceId", referencedColumnName = "sopId")
    @Documentation(key = "lblContactPerson")
    private List<CalcContact> _contacts = new Vector<>();

    public List<CalcContact> getContacts() {
        return _contacts;
    }

    public void addContact(CalcContact con) {
        _contacts.add(con);
    }

    public void setContacts(List<CalcContact> contacts) {
        _contacts = contacts;
    }
    // </editor-fold>

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 37 * hash + this._dataYear;
        hash = 37 * hash + this._ik;
        hash = 37 * hash + this._accountId;
        hash = 37 * hash + this._statusId;
        hash = 37 * hash + Objects.hashCode(this._lastChanged);
        hash = 37 * hash + (this._drgCalc ? 1 : 0);
        hash = 37 * hash + (this._psyCalc ? 1 : 0);
        hash = 37 * hash + (this._invCalc ? 1 : 0);
        hash = 37 * hash + (this._tpgCalc ? 1 : 0);
        hash = 37 * hash + this._clinicalDistributionModelDrg;
        hash = 37 * hash + this._clinicalDistributionModelPsy;
        hash = 37 * hash + Objects.hashCode(this._multiyearDrg);
        hash = 37 * hash + Objects.hashCode(this._multiyearDrgText);
        hash = 37 * hash + Objects.hashCode(this._multiyearPsy);
        hash = 37 * hash + Objects.hashCode(this._multiyearPsyText);
        hash = 37 * hash + (this._withConsultant ? 1 : 0);
        hash = 37 * hash + Objects.hashCode(this._consultantCompany);
        hash = 37 * hash + (this._consultantSendMail ? 1 : 0);
        return hash;
    }

    // <editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    @SuppressWarnings({"CyclomaticComplexity", "JavaNCSS"})
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
        final StatementOfParticipance other = (StatementOfParticipance) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._dataYear != other._dataYear) {
            return false;
        }
        if (this._ik != other._ik) {
            return false;
        }
        if (this._accountId != other._accountId) {
            return false;
        }
        if (this._statusId != other._statusId) {
            return false;
        }
        if (this._drgCalc != other._drgCalc) {
            return false;
        }
        if (this._psyCalc != other._psyCalc) {
            return false;
        }
        if (this._invCalc != other._invCalc) {
            return false;
        }
        if (this._tpgCalc != other._tpgCalc) {
            return false;
        }
        if (this._obdCalc != other._obdCalc) {
            return false;
        }
        if (this._clinicalDistributionModelDrg != other._clinicalDistributionModelDrg) {
            return false;
        }
        if (this._clinicalDistributionModelPsy != other._clinicalDistributionModelPsy) {
            return false;
        }
        if (this._withConsultant != other._withConsultant) {
            return false;
        }
        if (this._consultantSendMail != other._consultantSendMail) {
            return false;
        }
        if (!Objects.equals(this._multiyearDrg, other._multiyearDrg)) {
            return false;
        }
        if (!Objects.equals(this._multiyearDrgText, other._multiyearDrgText)) {
            return false;
        }
        if (!Objects.equals(this._multiyearPsy, other._multiyearPsy)) {
            return false;
        }
        if (!Objects.equals(this._multiyearPsyText, other._multiyearPsyText)) {
            return false;
        }
        if (!Objects.equals(this._consultantCompany, other._consultantCompany)) {
            return false;
        }
        if (!Objects.equals(this._lastChanged, other._lastChanged)) {
            return false;
        }
        return true;
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
