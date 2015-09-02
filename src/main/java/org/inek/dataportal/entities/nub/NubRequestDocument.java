package org.inek.dataportal.entities.nub;

import java.io.Serializable;
import javax.persistence.*;
import org.inek.dataportal.entities.Document;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "NubProposalDocument")
public class NubRequestDocument implements Serializable, Document {

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

    // <editor-fold defaultstate="collapsed" desc="Property NubRequestId">
    @Column(name = "npdNubProposalId")
    private int _nubRequestId = -1;

    public int getNubRequestId() {
        return _nubRequestId;
    }

    public void setNubRequestId(int nubRequestId) {
        _nubRequestId = nubRequestId;
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
        if (_id >= 0) {
            return _id;
        }
        return _name.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NubRequestDocument)) {
            return false;
        }
        NubRequestDocument other = (NubRequestDocument) object;
        if (_id >= 0) {
            return _id == other._id;
        }
        return _name.equals(other._name);
    }

    @Override
    public String toString() {
        return "org.inek.entities.NubRequestDocument[id=" + _id + "]";
    }
    // </editor-fold>

}
