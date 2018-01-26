package org.inek.dataportal.entities.pepp;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
public class PeppProposalComment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ppcId")
    private Integer _peppProposalCommentId;
    @Column(name = "ppcPeppProposalId")
    private Integer _peppProposalId;
    @Column(name = "ppcAccountId")
    private Integer _accountId;
    @Column(name = "ppcCreationDate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate;
    @Transient
    private String _initials;

    @Column(name = "ppcComment")
    private String _comment = "";

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Integer getPeppProposalCommentId() {
        return _peppProposalCommentId;
    }

    public void setPeppProposalCommentId(Integer id) {
        _peppProposalCommentId = id;
    }

    public Integer getPeppProposalId() {
        return _peppProposalId;
    }

    public void setPeppProposalId(Integer peppProposalId) {
        this._peppProposalId = peppProposalId;
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
        hash += (_peppProposalCommentId != null ? _peppProposalCommentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeppProposalComment)) {
            return false;
        }
        PeppProposalComment other = (PeppProposalComment) object;
        if (_peppProposalCommentId == null && other._peppProposalCommentId != null
                || _peppProposalCommentId != null && !_peppProposalCommentId.equals(other._peppProposalCommentId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.PeppProposalDocument[id=" + _peppProposalCommentId + "]";
    }
    // </editor-fold>
}
