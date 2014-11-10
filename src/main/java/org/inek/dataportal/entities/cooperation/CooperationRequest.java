package org.inek.dataportal.entities.cooperation;

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
@Table(name = "CooperationRequest", schema = "usr")
public class CooperationRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    public static CooperationRequest create(int requestorId, int requestedId) {
        CooperationRequest request = new CooperationRequest();
        request.setRequestorId(requestorId);
        request.setRequestedId(requestedId);
        return request;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crId")
    private Integer _id;

    @Column(name = "crCreationDate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate = null;

    @Column(name = "crRequestorAccountId")
    private int _requestorId;

    @Column(name = "crRequestedAccountId")
    private int _requestedId;

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

    public int getRequestorId() {
        return _requestorId;
    }

    public void setRequestorId(int requestorId) {
        _requestorId = requestorId;
    }

    public int getRequestedId() {
        return _requestedId;
    }

    public void setRequestedId(int requestedId) {
        _requestedId = requestedId;
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
        if (!(object instanceof CooperationRequest)) {
            return false;
        }
        CooperationRequest other = (CooperationRequest) object;
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
