package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "DistributionModelMaster", schema = "calc")
public class DistributionModel implements Serializable {
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dmmId")
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastChanged;

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        this._lastChanged = lastChanged;
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

    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    public void setStatus(WorkflowStatus status) {
        _statusId = status.getValue();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property List Details">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dmdMasterId", referencedColumnName = "dmmID")
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
