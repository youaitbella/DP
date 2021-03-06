package org.inek.dataportal.psy.nub.entities;

import org.inek.dataportal.psy.nub.converter.PsyNubDateFieldsConverter;
import org.inek.dataportal.psy.nub.enums.PsyNubDateFields;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "NubRequestDateValue", schema = "psy")
public class PsyNubRequestDateValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dvId")
    private int _id;

    @ManyToOne
    @JoinColumn(name = "dvNubRequestId")
    private PsyNubRequest _psyNubRequest;

    @Column(name = "dvDate")
    private String _date = "";

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

    public PsyNubRequest getPsyNubRequest() {
        return _psyNubRequest;
    }

    public void setPsyNubRequest(PsyNubRequest psyNubRequest) {
        this._psyNubRequest = psyNubRequest;
    }

    public String getDate() {
        return _date;
    }

    public void setDate(String date) {
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
        PsyNubRequestDateValue that = (PsyNubRequestDateValue) o;
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
