package org.inek.dataportal.common.data.ikadmin.entity;

import java.io.Serializable;

/**
 *
 * @author muellermi
 */
public class MapIkAdminFeature implements Serializable {

    private int _ikAdminId;
    private int _featureId;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this._ikAdminId;
        hash = 89 * hash + this._featureId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MapIkAdminFeature other = (MapIkAdminFeature) obj;
        return _ikAdminId == other._ikAdminId && _featureId == other._featureId;
    }

}
