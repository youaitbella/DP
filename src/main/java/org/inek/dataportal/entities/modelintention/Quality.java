/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author vohldo/schlappajo
 */
@Entity
@Table(name = "Quality", schema = "mvh")
public class Quality implements Serializable {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qyId")
    private Integer _id;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="modelIntentionId">
    @Column(name = "qyModelIntentionId")
    private int _modelIntentionId;

    public int getModelIntentionId() {
        return _modelIntentionId;
    }

    public void setModelIntentionId(int miId) {
        _modelIntentionId = miId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="type">
    @Column(name = "qyType")
    private int _typeId;

    public int getTypeId() {
        return _typeId;
    }

    public void setTypeId(int typeId) {
        _typeId = typeId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="indicator">
    @Column(name = "qyIndicator")
    private String _indicator = "";

    public String getIndicator() {
        return _indicator;
    }

    public void setIndicator(String indicator) {
        _indicator = indicator;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="description">
    @Column(name = "qyDescription")
    private String _description = "";

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Quality)) {
            return false;
        }
        Quality other = (Quality) object;
        return Objects.equals(_id, other._id)
                && (_id != null
                || _typeId == other._typeId
                && Objects.equals(_indicator, other._indicator));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this._id);
        if (_id == null) {
            hash = 41 * hash + this._typeId;
            hash = 41 * hash + Objects.hashCode(this._indicator);
        }
        return hash;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + _id + " ]";
    }

    // </editor-fold>
}
