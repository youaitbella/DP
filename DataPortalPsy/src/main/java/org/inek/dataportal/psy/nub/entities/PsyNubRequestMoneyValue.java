package org.inek.dataportal.psy.nub.entities;

import org.inek.dataportal.psy.nub.converter.PsyNubMoneyFieldsConverter;
import org.inek.dataportal.psy.nub.enums.PsyNubMoneyFields;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "NubRequestMoneyValue", schema = "psy")
public class PsyNubRequestMoneyValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mvId")
    private int _id;

    @ManyToOne
    @JoinColumn(name = "mvNubRequestId")
    private PsyNubRequest _psyNubRequest;

    @Column(name = "mvMoney")
    private double _money;

    @Column(name = "mvComment")
    private String _comment = "";

    @Column(name = "mvField")
    @Convert(converter = PsyNubMoneyFieldsConverter.class)
    private PsyNubMoneyFields _field;

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

    public double getMoney() {
        return _money;
    }

    public void setMoney(double money) {
        this._money = money;
    }

    public String getComment() {
        return _comment;
    }

    public void setComment(String comment) {
        this._comment = comment;
    }

    public PsyNubMoneyFields getField() {
        return _field;
    }

    public void setField(PsyNubMoneyFields field) {
        this._field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsyNubRequestMoneyValue that = (PsyNubRequestMoneyValue) o;
        return _id == that._id &&
                Double.compare(that._money, _money) == 0 &&
                Objects.equals(_psyNubRequest, that._psyNubRequest) &&
                Objects.equals(_comment, that._comment) &&
                _field == that._field;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _psyNubRequest, _money, _comment, _field);
    }
}
