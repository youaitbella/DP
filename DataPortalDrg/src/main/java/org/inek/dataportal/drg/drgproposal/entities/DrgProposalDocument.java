package org.inek.dataportal.drg.drgproposal.entities;
//import org.inek.dataportal.entities.Document;

import java.io.Serializable;
import javax.persistence.*;
import org.inek.dataportal.common.data.account.iface.Document;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
public class DrgProposalDocument implements Serializable, Document {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drgdId")
    private int _drgProposalDocumentId;

    @Documentation(key = "lblName", omitOnEmpty = true)
    @Column(name = "drgdName")
    private String _name = "";

    @Lob
    @Column(name = "drgdContent")
    private byte[] _content;

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public int getDrgProposalDocumentId() {
        return _drgProposalDocumentId;
    }

    public void setDrgProposalDocumentId(int id) {
        _drgProposalDocumentId = id;
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
        if (_drgProposalDocumentId > 0){return _drgProposalDocumentId;}
        return _name.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DrgProposalDocument)) {
            return false;
        }
        DrgProposalDocument other = (DrgProposalDocument) object;
        if (_drgProposalDocumentId > 0){return _drgProposalDocumentId == other._drgProposalDocumentId;}
        return _name.equals(other._name);
    }

    @Override
    public String toString() {
        return "org.inek.entities.DrgProposalDocument[id=" + _drgProposalDocumentId + "]";
    }
    // </editor-fold>
}
