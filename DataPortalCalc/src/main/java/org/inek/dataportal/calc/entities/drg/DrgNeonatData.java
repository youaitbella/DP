/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.drg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;
import org.inek.dataportal.common.utils.IgnoreOnCompare;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "KGLNeonatData", schema = "calc")
public class DrgNeonatData implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ndId", updatable = false, nullable = false)
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property BaseInformationId">
    @Column(name = "ndBaseInformationId")
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        _baseInformationId = baseInformationId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ContentTextId">
    @Column(name = "ndContentTextId")
    private int _contentTextId;

    public int getContentTextId() {
        return _contentTextId;
    }

    public void setContentTextId(int contentTextId) {
        _contentTextId = contentTextId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ContentText">
    @OneToOne
    @PrimaryKeyJoinColumn(name = "ndContentTextId")
    @IgnoreOnCompare
    private DrgContentText _contentText;

    @Documentation(name = "Bereich")
    public DrgContentText getContentText() {
        return _contentText;
    }

    public void setContentText(DrgContentText contentText) {
        _contentText = contentText;
        _contentTextId = contentText.getId();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Data">
    @Column(name = "ndData", precision = 10, scale = 1)
    @Documentation(name = "Wert", rank = 200)
    private BigDecimal _data = new BigDecimal(0);

    public BigDecimal getData() {
        return _data;
    }

    public void setData(BigDecimal data) {
        if (data == null) {
            _data = new BigDecimal(0);
        } else {
            _data = data;
        }
    }

    // conveniance method for int values
    @JsonIgnore
    public int getIntValue() {
        return _data.intValueExact();
    }

    public void setIntValue(int val) {
        _data = new BigDecimal(val);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 53 * hash + this._baseInformationId;
        hash = 53 * hash + this._contentTextId;
        hash = 53 * hash + Objects.hashCode(this._contentText);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DrgNeonatData other = (DrgNeonatData) obj;

        if (this._id != -1 && other._id == this._id) {
            return true;
        }

        if (this._id != other._id) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (this._contentTextId != other._contentTextId) {
            return false;
        }
        return Objects.equals(this._contentText, other._contentText);
    }

    @Override
    public String toString() {
        return "DrgNeonateData[ id=" + _id + " ]";
    }

    // </editor-fold>
}
