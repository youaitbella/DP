package org.inek.dataportal.entities.i68;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "I68", schema = "i68")
public class I68 implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int value) {
        _id = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CreationDate">
    @Column(name = "iCreated")
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
    @Column(name = "iUpdated")
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
    @Column(name = "iAccountId")
    private int _accountId = -1;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int value) {
        _accountId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Data Year">
    @Column(name = "iDataYear")
    @Documentation(key = "lblYear")
    private int _dataYear = Calendar.getInstance().get(Calendar.YEAR);

    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int value) {
        _dataYear = value;
    }
    // </editor-fold>

    @Column(name = "iIK")
    private int _ik = -1;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    
    @Column(name = "iHospital")
    private String _hospital = "";

    public String getHospital() {
        return _hospital;
    }

    public void setHospital(String hospital) {
        this._hospital = hospital;
    }
    
    @Column(name = "iStreet")
    private String _street = "";

    public String getStreet() {
        return _street;
    }

    public void setStreet(String street) {
        this._street = street;
    }
    
    @Column(name = "iZip")
    private String _zip = "";

    public String getZip() {
        return _zip;
    }

    public void setZip(String zip) {
        this._zip = zip;
    }
    
    @Column(name = "iCity")
    private String _city = "";

    public String getCity() {
        return _city;
    }

    public void setCity(String city) {
        this._city = city;
    }
    
    @Column(name = "iContactFirstname")
    private String _contactFirstName = "";

    public String getContactFirstName() {
        return _contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this._contactFirstName = contactFirstName;
    }
    
    @Column(name = "iContactLastname")
    private String _contactLastName = "";

    public String getContactLastName() {
        return _contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this._contactLastName = contactLastName;
    }
    
    @Column(name = "iContactGender")
    private int _contactGender = -1;

    public int getContactGender() {
        return _contactGender;
    }

    public void setContactGender(int contactGender) {
        this._contactGender = contactGender;
    }
    
    @Column(name = "iContactTitle")
    private int _contactTitle = -1;

    public int getContactTitle() {
        return _contactTitle;
    }

    public void setContactTitle(int contactTitle) {
        this._contactTitle = contactTitle;
    }
    
    @Column(name = "iContactPhone")
    private String _contactPhone = "";

    public String getContactPhone() {
        return _contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this._contactPhone = contactPhone;
    }
    
    @Column(name = "iContactEmail")
    private String _contactEmail = "";

    public String getContactEmail() {
        return _contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this._contactEmail = contactEmail;
    }
    
    @Column(name = "i68D")
    private int _i68d = -1;

    public int getI68d() {
        return _i68d;
    }

    public void setI68d(int i68d) {
        this._i68d = i68d;
    }
    
    @Column(name = "i68E")
    private int _i68e = -1;

    public int getI68e() {
        return _i68e;
    }

    public void setI68e(int i68e) {
        this._i68e = i68e;
    }
    
    @Column(name = "i68DList")
    private boolean _i68dList;
    
    public boolean getI68dList() {
        return _i68dList;
    }

    public void setI68dList(boolean i68dList) {
        this._i68dList = i68dList;
    }
    
    @Column(name = "i68EList")
    private boolean _i68eList;

    public boolean getI68eList() {
        return _i68eList;
    }

    public void setI68eList(boolean i68eList) {
        this._i68eList = i68eList;
    }
    
    @Column(name = "iDifferentIK")
    private int _differentIK = -1;

    public int getDifferentIK() {
        return _differentIK;
    }

    public void setDifferentIK(int differentIK) {
        this._differentIK = differentIK;
    }
    
    @Column(name = "iValidFrom")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _validFrom = new Date(1970, 1, 1);

    public Date getValidFrom() {
        return _validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this._validFrom = validFrom;
    }
    
    @Column(name = "iStatus")
    private WorkflowStatus _status = WorkflowStatus.New;

    public WorkflowStatus getStatus() {
        return _status;
    }

    public void setStatus(WorkflowStatus status) {
        this._status = status;
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
        final I68 other = (I68) obj;
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
