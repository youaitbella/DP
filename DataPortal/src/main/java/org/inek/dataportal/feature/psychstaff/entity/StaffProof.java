package org.inek.dataportal.feature.psychstaff.entity;

import org.inek.dataportal.feature.specificfunction.entity.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.inek.dataportal.entities.iface.StatusEntity;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "StaffProofMaster", schema = "psy")
public class StaffProof implements Serializable, StatusEntity {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spmId")
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
    @Column(name = "spmYear")
    @Documentation(key = "lblYear")
    private int _year;

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        this._year = year;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "spmIK")
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
    @Column(name = "spmAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Created">
    @Column(name = "spmCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _created = new Date();

    public Date getCreated() {
        return _created;
    }

    public void setCreated(Date created) {
        this._created = created;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "spmLastChanged")
    @Documentation(name = "Stand")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastChanged = new Date();

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        this._lastChanged = lastChanged;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sealed">
    @Column(name = "spmSealed")
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
    @Column(name = "spmStatusId")
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

    // <editor-fold defaultstate="collapsed" desc="Property ForAdults">
    @Column(name = "spfIsForAdults")
    private boolean _forAdults;

    public boolean isForAdults() {
        return _forAdults;
    }

    public void setForAdults(boolean forAdults) {
        _forAdults = forAdults;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ForKids">
    @Column(name = "spfIsForKids")
    private boolean _forKids;

    public boolean isForKids() {
        return _forKids;
    }

    public void setForKids(boolean forKids) {
        _forKids = forKids;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CalculationType">
    @Column(name = "spfCalculationType")
    private int _calculationType;

    public int getCalculationType() {
        return _calculationType;
    }

    public void setCalculationType(int calculationType) {
        _calculationType = calculationType;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AdultsAgreedDays">
    @Column(name = "spfAdultsAgreedDays")
    private int _adultsAgreedDays;

    public int getAdultsAgreedDays() {
        return _adultsAgreedDays;
    }

    public void setAdultsAgreedDays(int adultsAgreedDays) {
        _adultsAgreedDays = adultsAgreedDays;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AdultsActualDays">
    @Column(name = "spfAdultsActualDays")
    private int _adultsActualDays;

    public int getAdultsActualDays() {
        return _adultsActualDays;
    }

    public void setAdultsActualDays(int adultsActualDays) {
        _adultsActualDays = adultsActualDays;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AdultsActualCosts">
    @Column(name = "spfAdultsActualCosts")
    private double _adultsActualCosts;

    public double getAdultsActualCosts() {
        return _adultsActualCosts;
    }

    public void setAdultsActualCosts(double adultsActualCosts) {
        _adultsActualCosts = adultsActualCosts;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property KidsAgreedDays">
    @Column(name = "spfKidsAgreedDays")
    private int _kidsAgreedDays;

    public int getKidsAgreedDays() {
        return _kidsAgreedDays;
    }

    public void setKidsAgreedDays(int kidsAgreedDays) {
        _kidsAgreedDays = kidsAgreedDays;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property KidsActualDays">
    @Column(name = "spfKidsActualDays")
    private int _kidsActualDays;

    public int getKidsActualDays() {
        return _kidsActualDays;
    }

    public void setKidsActualDays(int kidsActualDays) {
        _kidsActualDays = kidsActualDays;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property KidsActualCosts">
    @Column(name = "spfKidsActualCosts")
    private double _kidsActualCosts;

    public double getKidsActualCosts() {
        return _kidsActualCosts;
    }

    public void setKidsActualCosts(double kidsActualCosts) {
        _kidsActualCosts = kidsActualCosts;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StaffProof)) {
            return false;
        }
        StaffProof other = (StaffProof) object;

        return _id == other._id;
    }

    @Override
    public String toString() {
        return "StaffProof [id=" + _id + "]";
    }
    // </editor-fold>

}
