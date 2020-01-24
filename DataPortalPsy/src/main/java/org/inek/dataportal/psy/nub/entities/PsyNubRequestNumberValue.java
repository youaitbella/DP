package org.inek.dataportal.psy.nub.entities;

import org.inek.dataportal.psy.nub.converter.PsyNubNumberFieldsConverter;
import org.inek.dataportal.psy.nub.enums.PsyNubNumberFields;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "NubRequestNumberValue", schema = "psy")
public class PsyNubRequestNumberValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nvId")
    private int _id;

    @ManyToOne
    @JoinColumn(name = "nvNubRequestId")
    private PsyNubRequest _psyNubRequest;

    @Column(name = "nvNumber")
    private int _number;

    @Column(name = "nvComment")
    private String _comment = "";

    @Column(name = "nvField")
    @Convert(converter = PsyNubNumberFieldsConverter.class)
    private PsyNubNumberFields _field;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public PsyNubRequest getPsyNubRequest() {
        return _psyNubRequest;
    }

    public void setPsyNubRequest(PsyNubRequest psyNubRequest) {
        this._psyNubRequest = psyNubRequest;
    }

    public int getNumber() {
        return _number;
    }

    public void setNumber(int number) {
        this._number = number;
    }

    public String getComment() {
        return _comment;
    }

    public void setComment(String comment) {
        this._comment = comment;
    }

    public PsyNubNumberFields getField() {
        return _field;
    }

    public void setField(PsyNubNumberFields field) {
        this._field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsyNubRequestNumberValue that = (PsyNubRequestNumberValue) o;
        return _id == that._id &&
                Double.compare(that._number, _number) == 0 &&
                Objects.equals(_psyNubRequest, that._psyNubRequest) &&
                Objects.equals(_comment, that._comment) &&
                _field == that._field;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _psyNubRequest, _number, _comment, _field);
    }
}
