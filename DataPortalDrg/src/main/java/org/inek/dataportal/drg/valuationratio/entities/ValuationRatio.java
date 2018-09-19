package org.inek.dataportal.drg.valuationratio.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.converter.WorkflowStatusConverter;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "ValuationRatio", schema = "vr")
public class ValuationRatio implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vrId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int value) {
        _id = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CreationDate">
    @Column(name = "vrCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _creationDate;

    public Date getCreationDate() {
        return _creationDate;
    }

    public void setCreationDate(Date value) {
        _creationDate = value;
    }
    
    public String getCreationDateFormatted() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return df.format(_creationDate);
    }

    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastChange">
    @Column(name = "vrUpdated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastChange;

    public Date getLastChange() {
        return _lastChange;
    }

    public void setLastChange(Date value) {
        _lastChange = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "vrAccountId")
    private int _accountId = -1;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int value) {
        _accountId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Data Year">
    @Column(name = "vrDataYear")
    @Documentation(key = "lblYear")
    private int _dataYear = Utils.getTargetYear(Feature.VALUATION_RATIO);

    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int value) {
        _dataYear = value;
    }
    // </editor-fold>

    @Column(name = "vrIK")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    
    @Column(name = "vrContactFirstname")
    private String _contactFirstName = "";

    public String getContactFirstName() {
        return _contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this._contactFirstName = contactFirstName;
    }
    
    @Column(name = "vrContactLastname")
    private String _contactLastName = "";

    public String getContactLastName() {
        return _contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this._contactLastName = contactLastName;
    }
    
    @Column(name = "vrContactGender")
    private int _contactGender = -1;

    public int getContactGender() {
        return _contactGender;
    }

    public void setContactGender(int contactGender) {
        this._contactGender = contactGender;
    }
    
    @Column(name = "vrContactTitle")
    private String _contactTitle = "";

    public String getContactTitle() {
        return _contactTitle;
    }

    public void setContactTitle(String contactTitle) {
        this._contactTitle = contactTitle;
    }
    
    @Column(name = "vrContactPhone")
    private String _contactPhone = "";

    public String getContactPhone() {
        return _contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this._contactPhone = contactPhone;
    }
    
    @Column(name = "vrContactEmail")
    private String _contactEmail = "";

    public String getContactEmail() {
        return _contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this._contactEmail = contactEmail;
    }
    
    @Column(name = "vr68D")
    private int _i68d = 0;

    public int getI68d() {
        return _i68d;
    }

    public void setI68d(int i68d) {
        this._i68d = i68d;
    }
    
    @Column(name = "vr68E")
    private int _i68e = 0;

    public int getI68e() {
        return _i68e;
    }

    public void setI68e(int i68e) {
        this._i68e = i68e;
    }
    
    @Column(name = "vr68DList")
    private boolean _i68dList;
    
    public boolean getI68dList() {
        return _i68dList;
    }

    public void setI68dList(boolean i68dList) {
        this._i68dList = i68dList;
    }
    
    @Column(name = "vr68EList")
    private boolean _i68eList;

    public boolean getI68eList() {
        return _i68eList;
    }

    public void setI68eList(boolean i68eList) {
        this._i68eList = i68eList;
    }
    
    @Column(name = "vrDifferentIK")
    private int _differentIK = 0;

    public int getDifferentIK() {
        return _differentIK;
    }

    public void setDifferentIK(int differentIK) {
        this._differentIK = differentIK;
    }
    
    @Column(name = "vrValidFrom")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _validFrom;

    public Date getValidFrom() {
        return _validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this._validFrom = validFrom;
    }
    
    @Column(name = "vrStatus")
    @Convert(converter = WorkflowStatusConverter.class)
    private WorkflowStatus _status = WorkflowStatus.New;

    public WorkflowStatus getStatus() {
        return _status;
    }
    
    public void setStatus(WorkflowStatus status) {
        _status = status;
    }
    
    public int getStatusId() {
        return _status.getId();
    }

    @PrePersist
    private void tagCreationDate() {
        _creationDate = Calendar.getInstance().getTime();
        tagLastChange();
    }
    
    @PreUpdate
    private void tagLastChange() {
        _lastChange = Calendar.getInstance().getTime();
    }
    
    public int getDrgValue(String drg) {
        if("I68D".equals(drg))
            return getI68d();
        if("I68E".equals(drg))
            return getI68e();
        return 0;
    }
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this._id;
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
        final ValuationRatio other = (ValuationRatio) obj;
        if (this._id != other._id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.InsuranceNubNotice[id=" + _id + "]";
    }
    // </editor-fold>

}
