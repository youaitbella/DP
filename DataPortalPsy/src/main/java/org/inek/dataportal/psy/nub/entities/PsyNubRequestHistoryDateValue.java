package org.inek.dataportal.psy.nub.entities;

import org.inek.dataportal.psy.nub.converter.PsyNubDateFieldsConverter;
import org.inek.dataportal.psy.nub.enums.PsyNubDateFields;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "NubRequestHistoryDateValue", schema = "psy")
public class PsyNubRequestHistoryDateValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dvId")
    private int _id;

    @ManyToOne
    @JoinColumn(name = "dvNubRequestHistoryId")
    private PsyNubRequestHistory _psyNubRequest;

    @Column(name = "dvDate")
    private String _date = "";

    @Column(name = "dvComment")
    private String _comment = "";

    @Column(name = "dvField")
    @Convert(converter = PsyNubDateFieldsConverter.class)
    private PsyNubDateFields _field;

    public PsyNubRequestHistory getPsyNubRequest() {
        return _psyNubRequest;
    }

    public String getDate() {
        return _date;
    }

    public String getComment() {
        return _comment;
    }

    public PsyNubDateFields getField() {
        return _field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsyNubRequestHistoryDateValue that = (PsyNubRequestHistoryDateValue) o;
        return _id == that._id &&
                Objects.equals(_psyNubRequest, that._psyNubRequest) &&
                Objects.equals(_date, that._date) &&
                Objects.equals(_comment, that._comment) &&
                _field == that._field;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _psyNubRequest, _date, _comment, _field);
    }
}
