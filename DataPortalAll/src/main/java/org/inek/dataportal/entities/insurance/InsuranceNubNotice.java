package org.inek.dataportal.entities.insurance;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.Valid;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "InsuranceNubNotice")
public class InsuranceNubNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "innId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int value) {
        _id = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CreationDate">
    @Column(name = "innCreationDate")
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
    @Column(name = "innLastChange")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastChange;

    public Date getLastChange() {
        return _lastChange;
    }

    public void setLastChange(Date value) {
        _lastChange = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property WorkflowStatusId">
    @Column(name = "innWorkflowStatusId")
    private int _workflowStatusId = -1;

    public int getWorkflowStatusId() {
        return _workflowStatusId;
    }

    public void setWorkflowStatusId(int value) {
        _workflowStatusId = value;
    }
    
    public WorkflowStatus getWorkflowStatus() {
        return WorkflowStatus.fromValue(_workflowStatusId);
    }

    public void setWorkflowStatus(WorkflowStatus value) {
        _workflowStatusId = value.getId();
    }    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "innAccountId")
    private int _accountId = -1;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int value) {
        _accountId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Year">
    @Column(name = "innYear")
    @Documentation(key = "lblYear")
    private int _year = Calendar.getInstance().get(Calendar.YEAR);

    public int getYear() {
        return _year;
    }

    public void setYear(int value) {
        _year = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InsuranceName">
    @Column(name = "innInsuranceName")
    private String _insuranceName = "";

    public String getInsuranceName() {
        return _insuranceName;
    }

    public void setInsuranceName(String name) {
        _insuranceName = name;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property InsuranceIk">
    @Column(name = "innInsuranceIk")
    private int _insuranceIk = -1;

    public int getInsuranceIk() {
        return _insuranceIk;
    }

    public void setInsuranceIk(int value) {
        _insuranceIk = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property HospitalIk">
    @Column(name = "innHospitalIk")
    private int _hospitalIk = -1;

    public int getHospitalIk() {
        return _hospitalIk;
    }

    public void setHospitalIk(int value) {
        _hospitalIk = value;
    }
    // </editor-fold>

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "inniInsuranceNubNoticeId", referencedColumnName = "innId")
    @OrderBy("_id")
    @Valid
    @Documentation(key = "tabMessageList")
    private List<InsuranceNubNoticeItem> _items;

    public List<InsuranceNubNoticeItem> getItems() {
        if (_items == null){
            _items = new ArrayList<>();
        }
        return _items;
    }

    public void setItems(List<InsuranceNubNoticeItem> items) {
        _items = items;
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
        hash = 59 * hash + Objects.hashCode(this._items);
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
        final InsuranceNubNotice other = (InsuranceNubNotice) obj;
        if (this._id != other._id) {
            return false;
        }
        if (!Objects.equals(this._items, other._items)) {
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
