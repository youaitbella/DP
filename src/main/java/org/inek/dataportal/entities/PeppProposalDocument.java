package org.inek.dataportal.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
public class PeppProposalDocument implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ppdId")
    private Integer _peppProposalDocumentId;
    @Column(name = "ppdName")
    private String _name = "";
    @Lob
    @Column(name = "ppdContent")
    private byte[] _content;

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Integer getPeppProposalDocumentId() {
        return _peppProposalDocumentId;
    }

    public void setPeppProposalDocumentId(Integer id) {
        _peppProposalDocumentId = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    
    public byte[] getContent() {
        return _content;
    }

    public void setContent(byte[] content) {
        _content = content;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_peppProposalDocumentId != null ? _peppProposalDocumentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Request)) {
            return false;
        }
        Request other = (Request) object;
        if ((_peppProposalDocumentId == null && other.getRequestId() != null) || (_peppProposalDocumentId != null && !_peppProposalDocumentId.equals(other.getRequestId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.PeppProposalDocument[id=" + _peppProposalDocumentId + "]";
    }
    // </editor-fold>
}
