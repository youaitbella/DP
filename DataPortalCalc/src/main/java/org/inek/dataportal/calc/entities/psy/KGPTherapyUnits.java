package org.inek.dataportal.calc.entities.psy;

import org.inek.dataportal.common.data.iface.BaseIdValue;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "KGPTherapyUnits", schema = "calc")
public class KGPTherapyUnits implements Serializable, BaseIdValue {
    public KGPTherapyUnits() {
    }

    public KGPTherapyUnits(KGPListContentText kgpListContentText, PeppCalcBasics kgpBaseInformation) {
        this._contentText = kgpListContentText;
        this._baseInformation = kgpBaseInformation;
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

    public void setBaseInformation(PeppCalcBasics baseInformation) {
        this._baseInformation = baseInformation;
    }

    public int getBaseInformationId(){
        return _baseInformation.getId();
    }
    public void setBaseInformationId(int dummy) {}
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
        KGPTherapyUnits that = (KGPTherapyUnits) o;
        return _value == that._value &&
                Objects.equals(_id, that._id) &&
                Objects.equals(_baseInformation, that._baseInformation) &&
                Objects.equals(_contentText, that._contentText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _baseInformation, _contentText, _value);
    }
}
