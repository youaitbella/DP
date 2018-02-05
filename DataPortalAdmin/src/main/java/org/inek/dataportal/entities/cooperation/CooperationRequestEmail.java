package org.inek.dataportal.entities.cooperation;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This entity is used to signal a cooperation request
 * It will be deleted after confirmation of the cooperation 
 * or after a defined amount of time, whichever occurs first
 * @author muellermi
 */
@Entity
@Table(name = "CooperationRequestEmail", schema = "usr")
public class CooperationRequestEmail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static CooperationRequestEmail create(int creatorId, String requestEmail) {
        CooperationRequestEmail cre = new CooperationRequestEmail();
        cre.setCreatorId(creatorId);
        cre.setRequestEmail(requestEmail);
        return cre;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creId")
    private Integer _id;

    @Column(name = "creCreatorId")
    private int _creatorId;

    @Column(name = "creRequestEmail")
    private String _requestEmail;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        this._id = id;
    }

    public int getCreatorId() {
        return _creatorId;
    }

    public void setCreatorId(int creatorId) {
        this._creatorId = creatorId;
    }

    public String getRequestEmail() {
        return _requestEmail;
    }

    public void setRequestEmail(String requestEmail) {
        this._requestEmail = requestEmail;
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
        if (!(object instanceof CooperationRequestEmail)) {
            return false;
        }
        CooperationRequestEmail other = (CooperationRequestEmail) object;
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
