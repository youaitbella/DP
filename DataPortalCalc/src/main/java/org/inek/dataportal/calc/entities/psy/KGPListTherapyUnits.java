package org.inek.dataportal.calc.entities.psy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "KGPListTherapyUnits", schema = "calc")
public class KGPListTherapyUnits  implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tuId", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
    @Column(name = "tuBaseInformationId")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _contentTextId">
    @Column(name = "tuContentTextId")
    private int _contentTextId;

    public int getContentTextId() {
        return _contentTextId;
    }

    public void setContentTextId(int contentTextId) {
        this._contentTextId = contentTextId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _value">
    @Column(name = "tuValue")
    private boolean _value;

    public boolean getValue() {
        return _value;
    }

    public void setValue(boolean value) {
        this._value = value;
    }
    //</editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KGPListTherapyUnits that = (KGPListTherapyUnits) o;
        return _id == that._id &&
                _baseInformationId == that._baseInformationId &&
                _contentTextId == that._contentTextId &&
                _value == that._value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _baseInformationId, _contentTextId, _value);
    }
}
