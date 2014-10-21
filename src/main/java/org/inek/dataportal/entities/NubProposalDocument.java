package org.inek.dataportal.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
public class NubProposalDocument implements Serializable, Document {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "npdId")
    private int _id = -1;
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property NubProposalId">
    @Column(name = "npdNubProposalId")
    private int _nubProposalId = -1;
    public int getNubProposalId() {
        return _nubProposalId;
    }

    public void setNubProposalId(int nubProposalId) {
        _nubProposalId = nubProposalId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "npdName")
    private String _name = "";

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Content">
    @Lob
    @Column(name = "npdContent")
    private byte[] _content;

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
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Request)) {
            return false;
        }
        NubProposalDocument other = (NubProposalDocument) object;
        return _id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.entities.NubProposalDocument[id=" + _id + "]";
    }
    // </editor-fold>

}
