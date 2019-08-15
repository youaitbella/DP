package org.inek.dataportal.psy.nub.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "PsyNubProposalDocument", schema = "psy")
public class PsyNubProposalDocument implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "npdId")
    private int _id;

    @ManyToOne
    @JoinColumn(name = "npdPsyNubProposalId")
    private PsyNubProposal _psyNubProposal;

    @Column(name = "npdName")
    private String _name = "";

    @Lob
    @Column(name = "npdContent")
    private byte[] _content;


    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public PsyNubProposal getPsyNubProposal() {
        return _psyNubProposal;
    }

    public void setPsyNubProposal(PsyNubProposal psyNubProposal) {
        this._psyNubProposal = psyNubProposal;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public byte[] getContent() {
        return _content;
    }

    public void setContent(byte[] content) {
        this._content = content;
    }

    public String getContentTyp() {
        String[] content = _name.split("\\.");
        return content[content.length - 1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsyNubProposalDocument that = (PsyNubProposalDocument) o;
        return _id == that._id &&
                Objects.equals(_psyNubProposal, that._psyNubProposal) &&
                Objects.equals(_name, that._name) &&
                Arrays.equals(_content, that._content);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(_id, _psyNubProposal, _name);
        result = 31 * result + Arrays.hashCode(_content);
        return result;
    }
}
