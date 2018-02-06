/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.entities.account;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.FeatureState;
import org.inek.dataportal.helper.converter.FeatureConverter;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "AccountFeature")
public class AccountFeature implements Serializable {

    private static final long serialVersionUID = 1L;

    public AccountFeature() {
    }

    public AccountFeature(final Feature feature) {
        _feature = feature;
    }

    public AccountFeature(int sequence, FeatureState featureState, final Feature feature) {
        _sequence = sequence;
        _featureState = featureState;
        _feature = feature;
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "afId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int Id) {
        _id = Id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Sequence">
    @Column(name = "afSequence")
    private int _sequence = 0;

    public int getSequence() {
        return _sequence;
    }

    public void setSequence(int sequence) {
        _sequence = sequence;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FeatureState">
    @Column(name = "afFeatureId")
    @Convert(converter = FeatureConverter.class)
    private Feature _feature;

    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        _feature = feature;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Feature">
    @Column(name = "afFeatureState")
    @Enumerated(EnumType.STRING)
    private FeatureState _featureState;

    public FeatureState getFeatureState() {
        return _featureState;
    }

    public void setFeatureState(FeatureState featureState) {
        _featureState = featureState;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this._id;
        hash = 79 * hash + Objects.hashCode(this._feature);
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
        final AccountFeature other = (AccountFeature) obj;
        if (this._id != other._id) {
            return false;
        }
        if (this._feature != other._feature) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.AccountFeature[id=" + _id + "]";
    }
    // </editor-fold>

}
