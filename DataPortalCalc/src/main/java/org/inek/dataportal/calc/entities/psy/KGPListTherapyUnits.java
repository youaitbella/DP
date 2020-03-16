package org.inek.dataportal.calc.entities.psy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "KGPListTherapyUnits", schema = "calc")
public class KGPListTherapyUnits  implements Serializable {
    public KGPListTherapyUnits() {
    }

    public KGPListTherapyUnits(KGPListContentText kgpListContentText) {
        this._contentText = kgpListContentText;
    }

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tuId", updatable = false, nullable = false)
    private Integer _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
    @ManyToOne
    @JoinColumn(name = "tuBaseInformationId")
    private PeppCalcBasics _baseInformation;

    public PeppCalcBasics getBaseInformation() {
        return _baseInformation;
    }

    public void setBaseInformationId(PeppCalcBasics baseInformationId) {
        this._baseInformation = baseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _contentText">
    @ManyToOne
    @JoinColumn(name = "tuContentTextId")
    private KGPListContentText _contentText;

    public KGPListContentText getContentText() {
        return _contentText;
    }

    public void setContentText(KGPListContentText contentTextId) {
        this._contentText = contentTextId;
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
                _baseInformation == that._baseInformation &&
                _contentText == that._contentText &&
                _value == that._value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _baseInformation, _contentText, _value);
    }
}
