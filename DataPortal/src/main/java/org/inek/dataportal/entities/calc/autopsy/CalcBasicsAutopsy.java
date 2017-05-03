/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc.autopsy;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import org.inek.dataportal.entities.iface.StatusEntity;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.helper.groupinterface.Seal;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "CalcBasicsAutopsy", schema = "calc")
@SuppressWarnings("Indentation")
public class CalcBasicsAutopsy implements Serializable, StatusEntity {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cbaId", updatable = false, nullable = false)
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dataYear">
    @Column(name = "cbaDataYear")
    @Documentation(key = "lblYearData")
    private int _dataYear;

    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        this._dataYear = dataYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "cbaIK")
    @Documentation(key = "lblIK")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "cbaAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "cbaLastChanged")
    @Documentation(name = "Stand")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastChanged;

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        this._lastChanged = lastChanged;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accountIdLastChange">
    @Column(name = "cbaLastChangedBy")
    private int _accountIdLastChange;

    public int getAccountIdLastChange() {
        return _accountIdLastChange;
    }

    public void setAccountIdLastChange(int accountId) {
        this._accountIdLastChange = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sealed">
    @Column(name = "cbaSealed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _sealed = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    public Date getSealed() {
        return _sealed;
    }

    public void setSealed(Date sealed) {
        this._sealed = sealed;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StatusId">
    @Column(name = "cbaStatusId")
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
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FullVigorMedicalBeforeAccrual">
    @Column(name = "cbaFullVigorMedicalBeforeAccrual")
    @Documentation(name="Anzahl Vollkräfte Ärztlicher Dienst vor Abgrenzung")
    private double _fullVigorMedicalBeforeAccrual;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public double getFullVigorMedicalBeforeAccrual() {
        return _fullVigorMedicalBeforeAccrual;
    }

    public void setFullVigorMedicalBeforeAccrual(double fullVigorMedicalBeforeAccrual) {
        _fullVigorMedicalBeforeAccrual = fullVigorMedicalBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FullVigorMedicalAfterAccrual">
    @Column(name = "cbaFullVigorMedicalAfterAccrual")
    @Documentation(name="Anzahl Vollkräfte Ärztlicher Dienst nach Abgrenzung")
    private double _fullVigorMedicalAfterAccrual;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public double getFullVigorMedicalAfterAccrual() {
        return _fullVigorMedicalAfterAccrual;
    }

    public void setFullVigorMedicalAfterAccrual(double fullVigorMedicalAfterAccrual) {
        _fullVigorMedicalAfterAccrual = fullVigorMedicalAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeMedicalBeforeAccrual">
    @Column(name = "cbaCostVolumeMedicalBeforeAccrual")
    @Documentation(name="Kostenvolumen Ärztlicher Dienst vor Abgrenzung")
    private int _costVolumeMedicalBeforeAccrual;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public int getCostVolumeMedicalBeforeAccrual() {
        return _costVolumeMedicalBeforeAccrual;
    }

    public void setCostVolumeMedicalBeforeAccrual(int costVolumeMedicalBeforeAccrual) {
        _costVolumeMedicalBeforeAccrual = costVolumeMedicalBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeMedicalAfterAccrual">
    @Column(name = "cbaCostVolumeMedicalAfterAccrual")
    @Documentation(name="Kostenvolumen Ärztlicher Dienst nach Abgrenzung")
    private int _costVolumeMedicalAfterAccrual;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public int getCostVolumeMedicalAfterAccrual() {
        return _costVolumeMedicalAfterAccrual;
    }

    public void setCostVolumeMedicalAfterAccrual(int costVolumeMedicalAfterAccrual) {
        _costVolumeMedicalAfterAccrual = costVolumeMedicalAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FullVigorOtherBeforeAccrual">
    @Column(name = "cbaFullVigorOtherBeforeAccrual")
    @Documentation(name="Anzahl Vollkräfte Sektionsgehilfen/Präparator etc. vor Abgrenzung" , headline = "Pathologie: Kosteninformationen")
    private double _fullVigorOtherBeforeAccrual;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public double getFullVigorOtherBeforeAccrual() {
        return _fullVigorOtherBeforeAccrual;
    }

    public void setFullVigorOtherBeforeAccrual(double fullVigorOtherBeforeAccrual) {
        _fullVigorOtherBeforeAccrual = fullVigorOtherBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FullVigorOtherAfterAccrual">
    @Column(name = "cbaFullVigorOtherAfterAccrual")
    @Documentation(name="Anzahl Vollkräfte Sektionsgehilfen/Präparator etc. nach Abgrenzung")
    private double _fullVigorOtherAfterAccrual;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public double getFullVigorOtherAfterAccrual() {
        return _fullVigorOtherAfterAccrual;
    }

    public void setFullVigorOtherAfterAccrual(double fullVigorOtherAfterAccrual) {
        _fullVigorOtherAfterAccrual = fullVigorOtherAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeOtherBeforeAccrual">
    @Column(name = "cbaCostVolumeOtherBeforeAccrual")
    @Documentation(name="Kostenvolumen Sektionsgehilfen/Präparator etc. vor Abgrenzung")
    private int _costVolumeOtherBeforeAccrual;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public int getCostVolumeOtherBeforeAccrual() {
        return _costVolumeOtherBeforeAccrual;
    }

    public void setCostVolumeOtherBeforeAccrual(int costVolumeOtherBeforeAccrual) {
        _costVolumeOtherBeforeAccrual = costVolumeOtherBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeOtherAfterAccrual">
    @Column(name = "cbaCostVolumeOtherAfterAccrual")
    @Documentation(name="Kostenvolumen Sektionsgehilfen/Präparator etc. nach Abgrenzung")
    private int _costVolumeOtherAfterAccrual;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public int getCostVolumeOtherAfterAccrual() {
        return _costVolumeOtherAfterAccrual;
    }

    public void setCostVolumeOtherAfterAccrual(int costVolumeOtherAfterAccrual) {
        _costVolumeOtherAfterAccrual = costVolumeOtherAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeMedicalInfraBeforeAccrual">
    @Column(name = "cbaCostVolumeMedicalInfraBeforeAccrual")
    @Documentation(name = "Kostenvolumen medizinische Infrastruktur vor Abgrenzung")
    private int _costVolumeMedicalInfraBeforeAccrual;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public int getCostVolumeMedicalInfraBeforeAccrual() {
        return _costVolumeMedicalInfraBeforeAccrual;
    }

    public void setCostVolumeMedicalInfraBeforeAccrual(int costVolumeMedicalInfraBeforeAccrual) {
        _costVolumeMedicalInfraBeforeAccrual = costVolumeMedicalInfraBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeMedicalInfraAfterAccrual">
    @Column(name = "cbaCostVolumeMedicalInfraAfterAccrual")
    @Documentation(name="Kostenvolumen medizinische Infrastruktur nach Abgrenzung")
    private int _costVolumeMedicalInfraAfterAccrual;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public int getCostVolumeMedicalInfraAfterAccrual() {
        return _costVolumeMedicalInfraAfterAccrual;
    }

    public void setCostVolumeMedicalInfraAfterAccrual(int costVolumeMedicalInfraAfterAccrual) {
        _costVolumeMedicalInfraAfterAccrual = costVolumeMedicalInfraAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeNonMedicalInfraBeforeAccrual">
    @Column(name = "cbaCostVolumeNonMedicalInfraBeforeAccrual")
    @Documentation(name="Kostenvolumen nicht medizinische Infrastruktur vor Abgrenzung")
    private int _costVolumeNonMedicalInfraBeforeAccrual;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public int getCostVolumeNonMedicalInfraBeforeAccrual() {
        return _costVolumeNonMedicalInfraBeforeAccrual;
    }

    public void setCostVolumeNonMedicalInfraBeforeAccrual(int costVolumeNonMedicalInfraBeforeAccrual) {
        _costVolumeNonMedicalInfraBeforeAccrual = costVolumeNonMedicalInfraBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeNonMedicalInfraAfterAccrual">
    @Column(name = "cbaCostVolumeNonMedicalInfraAfterAccrual")
    @Documentation(name="Kostenvolumen nicht medizinische Infrastruktur nach Abgrenzung")
    private int _costVolumeNonMedicalInfraAfterAccrual;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public int getCostVolumeNonMedicalInfraAfterAccrual() {
        return _costVolumeNonMedicalInfraAfterAccrual;
    }

    public void setCostVolumeNonMedicalInfraAfterAccrual(int costVolumeNonMedicalInfraAfterAccrual) {
        _costVolumeNonMedicalInfraAfterAccrual = costVolumeNonMedicalInfraAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property HasCostCenterForensic">
    @Column(name = "cbaHasCostCenterForensic")
    @Documentation(name = "Eigenständige Kostenstelle Rechtsmedizin vorhanden", headline = "Rechtsmedizin: Kosteninformationen")
    private boolean _hasCostCenterForensic;

    public boolean getHasCostCenterForensic() {
        return _hasCostCenterForensic;
    }

    public void setHasCostCenterForensic(boolean hasCostCenterForensic) {
        _hasCostCenterForensic = hasCostCenterForensic;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeForensic">
    @Column(name = "cbaCostVolumeForensic")
    @Documentation(name="Kostenvolumen Rechtsmedizin vor Abgrenzung")
    private int _costVolumeForensic;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public int getCostVolumeForensic() {
        return _costVolumeForensic;
    }

    public void setCostVolumeForensic(int costVolumeForensic) {
        _costVolumeForensic = costVolumeForensic;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeForensicAccural">
    @Column(name = "cbaCostVolumeForensicAccural")
    @Documentation(name="Kostenvolumen Rechtsmedizin nach Abgrenzung")
    private int _costVolumeForensicAccural;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public int getCostVolumeForensicAccural() {
        return _costVolumeForensicAccural;
    }

    public void setCostVolumeForensicAccural(int costVolumeForensicAccural) {
        _costVolumeForensicAccural = costVolumeForensicAccural;
    }
    // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Property TotalAutopsys">
    @Column(name = "cbaTotalAutopsys")
    @Documentation(name="Anzahl insgesamt durchgeführter klinischer Sektionen" , headline = "Pathologie: Leistungs- und Kosteninformationen")
    private int _totalAutopsys;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public int getTotalAutopsys() {
        return _totalAutopsys;
    }

    public void setTotalAutopsys(int value) {
        _totalAutopsys = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CalcAutopsys">
    @Column(name = "cbaCalcAutopsys")
    @Documentation(name="davon: kalkulationsrelevante Anzahl")
    private int _calcAutopsys;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public int getCalcAutopsys() {
        return _calcAutopsys;
    }

    public void setCalcAutopsys(int calcAutopsys) {
        _calcAutopsys = calcAutopsys;
    }
    // </editor-fold>

     // <editor-fold defaultstate="collapsed" desc="Property CostVolumeLaboratoryBeforeAccrual">
    @Column(name = "cbaCostVolumeLaboratoryBeforeAccrual")
    @Documentation(name="Kostenvolumen Kostenstellengruppe 10 (Labor) für klinische Sektionen vor Abgrenzung", rank = 200,
            headline = "Weitere Leistungsbereiche: Kosten- und Leistungsinformationen")
    private int _costVolumeLaboratoryBeforeAccrual;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public int getCostVolumeLaboratoryBeforeAccrual() {
        return _costVolumeLaboratoryBeforeAccrual;
    }

    public void setCostVolumeLaboratoryBeforeAccrual(int costVolumeLaboratoryBeforeAccrual) {
        _costVolumeLaboratoryBeforeAccrual = costVolumeLaboratoryBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeLaboratoryAfterAccrual">
    @Column(name = "cbaCostVolumeLaboratoryAfterAccrual")
    @Documentation(name="Kostenvolumen Kostenstellengruppe 10 (Labor) für klinische Sektionen nach Abgrenzung", rank=200)
    private int _costVolumeLaboratoryAfterAccrual;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte angeben")})
    public int getCostVolumeLaboratoryAfterAccrual() {
        return _costVolumeLaboratoryAfterAccrual;
    }

    public void setCostVolumeLaboratoryAfterAccrual(int costVolumeLaboratoryAfterAccrual) {
        _costVolumeLaboratoryAfterAccrual = costVolumeLaboratoryAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CaseCountXRay">
    @Column(name = "cbaCaseCountXRay")
    @Documentation(name = "Anzahl Fälle mit Röntgenleistungen für klinische Sektionen", rank=200)
    private int _caseCountXRay;

    public int getCaseCountXRay() {
        return _caseCountXRay;
    }

    public void setCaseCountXRay(int caseCountXRay) {
        _caseCountXRay = caseCountXRay;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property cbaCostVolumeXRayBeforeAccrual">
    @Column(name = "cbaCostVolumeXRayBeforeAccrual")
    @Documentation(name="Kostenvolumen Kostenstellengruppe 9 (Radiologie) für klinische Sektionen vor Abgrenzung", rank=200)
    private int _costVolumeXRayBeforeAccrual;

    public int getCostVolumeXRayBeforeAccrual() {
        return _costVolumeXRayBeforeAccrual;
    }

    public void setCostVolumeXRayBeforeAccrual(int costVolumeXRayBeforeAccrual) {
        _costVolumeXRayBeforeAccrual = costVolumeXRayBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeXRayAfterAccrual">
    @Column(name = "cbaCostVolumeXRayAfterAccrual")
    @Documentation(name="Kostenvolumen Kostenstellengruppe 9 (Radiologie) für klinische Sektionen nach Abgrenzung", rank=200)
    private int _costVolumeXRayAfterAccrual;

    public int getCostVolumeXRayAfterAccrual() {
        return _costVolumeXRayAfterAccrual;
    }

    public void setCostVolumeXRayAfterAccrual(int costVolumeXRayAfterAccrual) {
        _costVolumeXRayAfterAccrual = costVolumeXRayAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property List AutopsyItems">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cbaiCalcBasicsAutopsyId", referencedColumnName = "cbaId")
    @OrderBy("_autopsyServiceTextId")
    @Documentation(name = "Leistungs- und Kosteninformationen")
    private List<AutopsyItem> _autopsyItems = new Vector<>();

    public List<AutopsyItem> getAutopsyItems() {
        return Collections.unmodifiableList(_autopsyItems);
    }

    public void addAutopsyItem(AutopsyServiceText serviceText) {
        _autopsyItems.add(new AutopsyItem(serviceText));
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode, equals, toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this._id;
        return hash;
    }

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
        final CalcBasicsAutopsy other = (CalcBasicsAutopsy) obj;
        if (this._id != other._id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CalcBasicsAutopsy{" + "_id=" + _id + '}';
    }
    //</editor-fold>

}
