package org.inek.dataportal.feature.calculationhospital.entities.cdm;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.*;
import org.inek.dataportal.common.data.iface.StatusEntity;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "DistributionModelMaster", schema = "calc")
public class DistributionModel implements Serializable, StatusEntity {
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dmmId", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Type">
    @Column(name = "dmmType")
    @Documentation(name = "KVM", translateValue = "0=DRG;1=PEPP")
    private int _type;

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        _type = type;
    }
    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="dataYear">
    @Column(name = "dmmDataYear")
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
    @Column(name = "dmmIK")
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
    @Column(name = "dmmAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "dmmLastChanged")
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
    @Column(name = "dmmLastChangedBy")
    private int _accountIdLastChange;

    public int getAccountIdLastChange() {
        return _accountIdLastChange;
    }

    public void setAccountIdLastChange(int accountId) {
        this._accountIdLastChange = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sealed">
    @Column(name = "dmmSealed")
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
    @Column(name = "dmmStatusId")
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

    // <editor-fold defaultstate="collapsed" desc="Property ApprovalNote">
    @Column(name = "dmmNoteInek")
    @Documentation(name = "Bemerkung InEK", rank = 175, omitOnEmpty = true)
    private String _noteInek = "";

    public String getNoteInek() {
        return _noteInek;
    }

    public void setNoteInek(String noteInek) {
        _noteInek = noteInek;
    }
    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List Details">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dmdMasterId", referencedColumnName = "dmmID")
    @OrderBy(value = "_article, _costCenterId, _costTypeId")
    @Documentation(name = "Verteilungsmodell")
    private List<DistributionModelDetail> _details = new Vector<>();

    public List<DistributionModelDetail> getDetails() {
        return _details;
    }

    public void setDetails(List<DistributionModelDetail> details) {
        this._details = details;
    }
    //</editor-fold>
        
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DistributionModel)) {
            return false;
        }
        DistributionModel other = (DistributionModel) object;
        
        return _id == other._id;
    }

    @Override
    public String toString() {
        return "DistributionModel[id=" + _id + "]";
    }
    // </editor-fold>
    
}
