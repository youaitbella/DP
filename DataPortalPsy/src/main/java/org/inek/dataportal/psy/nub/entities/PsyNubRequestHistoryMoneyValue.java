package org.inek.dataportal.psy.nub.entities;

import org.inek.dataportal.psy.nub.converter.PsyNubMoneyFieldsConverter;
import org.inek.dataportal.psy.nub.enums.PsyNubMoneyFields;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "NubRequestHistoryMoneyValue", schema = "psy")
public class PsyNubRequestHistoryMoneyValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mvId")
    private int _id;

    @ManyToOne
    @JoinColumn(name = "mvNubRequestHistoryId")
    private PsyNubRequestHistory _psyNubRequest;

    @Column(name = "mvMoney")
    private double _money;

    @Column(name = "mvComment")
    private String _comment = "";

    @Column(name = "mvField")
    @Convert(converter = PsyNubMoneyFieldsConverter.class)
    private PsyNubMoneyFields _field;

    public PsyNubRequestHistory getPsyNubRequest() {
        return _psyNubRequest;
    }

    public double getMoney() {
        return _money;
    }

    public String getComment() {
        return _comment;
    }

    public PsyNubMoneyFields getField() {
        return _field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsyNubRequestHistoryMoneyValue that = (PsyNubRequestHistoryMoneyValue) o;
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
