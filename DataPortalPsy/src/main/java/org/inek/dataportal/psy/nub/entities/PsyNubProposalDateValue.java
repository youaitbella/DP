package org.inek.dataportal.psy.nub.entities;

import org.inek.dataportal.psy.nub.converter.PsyNubDateFieldsConverter;
import org.inek.dataportal.psy.nub.enums.PsyNubDateFields;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "PsyNubProposalDateValue", schema = "psy")
public class PsyNubProposalDateValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dvId")
    private int _id;

    @ManyToOne
    @JoinColumn(name = "dvPsyNubProposalId")
    private PsyNubProposal _psyNubProposal;

    @Column(name = "dvDate")
    @Temporal(TemporalType.DATE)
    private Date _date = new Date();

    @Column(name = "dvComment")
    private String _comment = "";

    @Column(name = "dvField")
    @Convert(converter = PsyNubDateFieldsConverter.class)
    private PsyNubDateFields _field;

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

    public Date getDate() {
        return _date;
    }

    public void setDate(Date date) {
        this._date = date;
    }

    public String getComment() {
        return _comment;
    }

    public void setComment(String comment) {
        this._comment = comment;
    }

    public PsyNubDateFields getField() {
        return _field;
    }

    public void setField(PsyNubDateFields field) {
        this._field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsyNubProposalDateValue that = (PsyNubProposalDateValue) o;
        return _id == that._id &&
                Objects.equals(_psyNubProposal, that._psyNubProposal) &&
                Objects.equals(_date, that._date) &&
                Objects.equals(_comment, that._comment) &&
                _field == that._field;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _psyNubProposal, _date, _comment, _field);
    }
}
