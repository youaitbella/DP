/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.entities.account;

import java.io.Serializable;
import javax.persistence.*;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.FeatureState;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "AccountFeature")
public class AccountFeature implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "afId")
    private Integer _id;
    @Column(name = "afSequence")
    private Integer _sequence = 0;
    @Column(name = "afFeature")
    @Enumerated(EnumType.STRING)
    private Feature _feature;
    @Column(name = "afFeatureState")
    @Enumerated(EnumType.STRING)
    private FeatureState _featureState;

    public AccountFeature() {
    }

    public AccountFeature(final Feature feature) {
        _feature = feature;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer Id) {
        this._id = Id;
    }

    public Integer getSequence() {
        return _sequence;
    }

    public void setSequence(Integer sequence) {
        _sequence = sequence;
    }

    public FeatureState getFeatureState() {
        return _featureState;
    }

    public void setFeatureState(FeatureState featureState) {
        _featureState = featureState;
    }

    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        _feature = feature;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccountFeature)) {
            return false;
        }
        AccountFeature other = (AccountFeature) object;
        if ((_id == null && other.getId() != null) || (_id != null && !_id.equals(other.getId()))) {
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
