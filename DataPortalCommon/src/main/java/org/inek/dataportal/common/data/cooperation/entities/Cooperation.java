package org.inek.dataportal.common.data.cooperation.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;

/**
 * This entity is used to signal a cooperation request
 * It will be deleted after confirmation of the cooperation 
 * or after a defined amount of time, whichever occurs first
 * @author muellermi
 */
@Entity
@Table(name = "Cooperation", schema = "usr")
public class Cooperation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static Cooperation create(int accountId1, int accountId2) {
        Cooperation request = new Cooperation();
        request.setAccountId1(accountId1);
        request.setAccountId2(accountId2);
        return request;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coId")
    private Integer _id;

    @Column(name = "coCreationDate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate = null;

    @Column(name = "coAccountId1")
    private int _accountId1;

    @Column(name = "coAccountId2")
    private int _accountId2;

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Date getCreationTS() {
        return _creationDate;
    }

    public int getAccountId1() {
        return _accountId1;
    }

    public void setAccountId1(int accountId) {
        _accountId1 = accountId;
    }

    public int getAccountId2() {
        return _accountId2;
    }

    public void setAccountId2(int accountId) {
        _accountId2 = accountId;
    }

    
    // </editor-fold>

    @PrePersist
    public void tagModifiedDate() {
        _creationDate = Calendar.getInstance().getTime();
    }

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cooperation)) {
            return false;
        }
        Cooperation other = (Cooperation) object;
        if ((_id == null && other.getId() != null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.CooperationRequest[id=" + _id + "]";
    }
    // </editor-fold>
    
}
