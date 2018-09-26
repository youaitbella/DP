package org.inek.dataportal.common.data.ikadmin.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.converter.FeatureConverter;

@Entity
@Table(name = "IkAdminFeature", schema = "ikadm")
public class IkAdminFeature implements Serializable {

    private static final long serialVersionUID = 1L;

    public IkAdminFeature(){}
    public IkAdminFeature(int AccountId, Feature feature){
        _ikAdminId = AccountId;
        _feature = feature;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iafId")
    private Integer _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property IkAdminId">
    @Id
    @Column(name = "iafIkAdminId")
    private int _ikAdminId = -1;

    public int getIkAdminId() {
        return _ikAdminId;
    }

    public void setIkAdminId(int id) {
        _ikAdminId = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Feature">
    @Id
    @Column(name = "iafFeatureId")
    @Convert(converter = FeatureConverter.class)
    private Feature _feature;

    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        _feature = feature;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode & equals">
    @Override
    public int hashCode() {
        return 101;
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
        final IkAdminFeature other = (IkAdminFeature) obj;
        if (this._ikAdminId != other._ikAdminId) {
            return false;
        }
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        if (this._feature != other._feature) {
            return false;
        }
        return true;
    }
    // </editor-fold>


}
