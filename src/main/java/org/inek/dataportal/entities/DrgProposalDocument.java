package org.inek.dataportal.entities;
//import org.inek.dataportal.entities.Document;
import java.io.Serializable;
import javax.persistence.*;
import org.inek.dataportal.entities.Document;
import org.inek.dataportal.entities.Request;

/**
 *
 * @author muellermi
 */
@Entity
public class DrgProposalDocument implements Serializable,Document {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drgdId")
    private Integer _drgProposalDocumentId;
    @Column(name = "drgdName")
    private String _name = "";
    @Lob
    @Column(name = "drgdContent")
    private byte[] _content;

    
    
    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Integer getDrgProposalDocumentId() {
        return _drgProposalDocumentId;
    }

    public void setDrgProposalDocumentId(Integer id) {
        _drgProposalDocumentId = id;
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
        hash += (_drgProposalDocumentId != null ? _drgProposalDocumentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Request)) {
            return false;
        }
        Request other = (Request) object;
        if ((_drgProposalDocumentId == null && other.getRequestId() != null) || (_drgProposalDocumentId != null && !_drgProposalDocumentId.equals(other.getRequestId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.DrgProposalDocument[id=" + _drgProposalDocumentId + "]";
    }
    // </editor-fold>
}
