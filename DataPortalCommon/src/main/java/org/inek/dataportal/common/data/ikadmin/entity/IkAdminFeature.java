package org.inek.dataportal.common.data.ikadmin.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "mapIkAdminFeature", schema = "ikadm")
@IdClass(MapIkAdminFeature.class)
public class IkAdminFeature implements Serializable {

    private static final long serialVersionUID = 1L;

    public IkAdminFeature(){}
    public IkAdminFeature(int AccountId, int ik){
        _ikAdminId = AccountId;
        _featureId = ik;
    }
    
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

    // <editor-fold defaultstate="collapsed" desc="Property FeatureId">
    @Id
    @Column(name = "iafFeatureId")
    private int _featureId = -1;

    public int getFeatureId() {
        return _featureId;
    }

    public void setFeatureId(int id) {
        _featureId = id;
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
        if (this._featureId != other._featureId) {
            return false;
        }
        return true;
    }
    // </editor-fold>

}
