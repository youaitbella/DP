package org.inek.dataportal.drg.drgproposal.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
public class DrgProposalComment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drgcId")
    private Integer _drgProposalCommentId;
    @Column(name = "drgcDrgProposalId")
    private Integer _drgProposalId;
    @Column(name = "drgcAccountId")
    private Integer _accountId;
    @Column(name = "drgcCreationDate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate;
    @Transient
    private String _initials;

    @Column(name = "drgcComment")
    private String _comment = "";

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Integer getDrgProposalCommentId() {
        return _drgProposalCommentId;
    }

    public void setDrgProposalCommentId(Integer id) {
        _drgProposalCommentId = id;
    }

    public Integer getDrgProposalId() {
        return _drgProposalId;
    }

    public void setDrgProposalId(Integer drgProposalId) {
        this._drgProposalId = drgProposalId;
    }

    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer accountId) {
        this._accountId = accountId;
    }

    public String getComment() {
        return _comment;
    }

    public void setComment(String comment) {
        this._comment = comment;
    }

    public Date getCreationDate() {
        return _creationDate;
    }
 
    public void setCreationDate(Date creationDate) {
        // method is only used to force creation of stub for WSDL
    }

    @PrePersist
    public void setCreationDate() {
        this._creationDate = Calendar.getInstance().getTime();
    }

    public String getInitials() {
        return _initials;
    }

    public void setInitials(String initials) {
        this._initials = initials;
    }
    
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_drgProposalCommentId != null ? _drgProposalCommentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DrgProposalComment)) {
            return false;
        }
        DrgProposalComment other = (DrgProposalComment) object;
        if ((_drgProposalCommentId == null && other.getDrgProposalCommentId()!= null) 
                || _drgProposalCommentId != null && !_drgProposalCommentId.equals(other.getDrgProposalCommentId())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.DrgProposalDocument[id=" + _drgProposalCommentId + "]";
    }
    // </editor-fold>
}
