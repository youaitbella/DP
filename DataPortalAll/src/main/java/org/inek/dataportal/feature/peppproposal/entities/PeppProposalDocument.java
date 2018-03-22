package org.inek.dataportal.feature.peppproposal.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.inek.dataportal.common.data.account.iface.Document;

/**
 *
 * @author muellermi
 */
@Entity
public class PeppProposalDocument implements Serializable, Document {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ppdId")
    private int _peppProposalDocumentId;
    @Column(name = "ppdName")
    private String _name = "";
    @Lob
    @Column(name = "ppdContent")
    private byte[] _content;

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public int getPeppProposalDocumentId() {
        return _peppProposalDocumentId;
    }

    public void setPeppProposalDocumentId(int id) {
        _peppProposalDocumentId = id;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }

    @Override
    public byte[] getContent() {
        return _content;
    }

    @Override
    public void setContent(byte[] content) {
        _content = content;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        if (_peppProposalDocumentId > 0){return _peppProposalDocumentId;}
        return _name.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeppProposalDocument)) {
            return false;
        }
        PeppProposalDocument other = (PeppProposalDocument) object;
        if (_peppProposalDocumentId > 0){return _peppProposalDocumentId == other._peppProposalDocumentId;}
        return _name.equals(other._name);
    }

    @Override
    public String toString() {
        return "org.inek.entities.PeppProposalDocument[id=" + _peppProposalDocumentId + "]";
    }
    // </editor-fold>

}
