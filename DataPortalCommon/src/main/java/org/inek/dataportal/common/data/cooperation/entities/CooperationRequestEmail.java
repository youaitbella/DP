package org.inek.dataportal.common.data.cooperation.entities;

import java.io.Serializable;
import java.util.Date;
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
    
    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creId")
    private Integer _id;
    public Integer getId() {
        return _id;
    }
    
    public void setId(Integer id) {
        _id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CreatorId">
    @Column(name = "creCreatorId")
    private int _creatorId;

    public int getCreatorId() {
        return _creatorId;
    }

    public void setCreatorId(int creatorId) {
        _creatorId = creatorId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="RequestEmail">
    @Column(name = "creRequestEmail")
    private String _requestEmail;

    public String getRequestEmail() {
        return _requestEmail;
    }

    public void setRequestEmail(String requestEmail) {
        _requestEmail = requestEmail;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CreationDate">
    @Column(name = "creCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _creationDate = new Date();

    public Date getCreationDate() {
        return _creationDate;
    }

    public void setCreationDate(Date value) {
        _creationDate = value;
    }
    // </editor-fold>
    
    
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
