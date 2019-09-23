package org.inek.dataportal.psy.nub.entities;

import org.inek.dataportal.psy.nub.converter.PsyNubNumberFieldsConverter;
import org.inek.dataportal.psy.nub.enums.PsyNubNumberFields;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "NubRequestHistoryNumberValue", schema = "psy")
public class PsyNubRequestHistoryNumberValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nvId")
    private int _id;

    @ManyToOne
    @JoinColumn(name = "nvNubRequestHistoryId")
    private PsyNubRequestHistory _psyNubRequest;

    @Column(name = "nvNumber")
    private int _number;

    @Column(name = "nvComment")
    private String _comment = "";

    @Column(name = "nvField")
    @Convert(converter = PsyNubNumberFieldsConverter.class)
    private PsyNubNumberFields _field;

    public PsyNubRequestHistory getPsyNubRequest() {
        return _psyNubRequest;
    }

    public int getNumber() {
        return _number;
    }

    public String getComment() {
        return _comment;
    }

    public PsyNubNumberFields getField() {
        return _field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsyNubRequestHistoryNumberValue that = (PsyNubRequestHistoryNumberValue) o;
        return _id == that._id &&
                _number == that._number &&
                Objects.equals(_psyNubRequest, that._psyNubRequest) &&
                Objects.equals(_comment, that._comment) &&
                _field == that._field;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _psyNubRequest, _number, _comment, _field);
    }
}
