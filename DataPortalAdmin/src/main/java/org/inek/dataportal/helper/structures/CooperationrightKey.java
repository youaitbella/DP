package org.inek.dataportal.helper.structures;

import java.util.Objects;
import org.inek.dataportal.common.enums.Feature;

/**
 *
 * @author muellermi
 */
public class CooperationrightKey {
    private Feature _feature;
    private Integer _ownerId;
    private Integer _partnerId;
    private Integer _ik;
    
    public CooperationrightKey() {
    }

    public CooperationrightKey(final Feature feature, final Integer ownerId, final Integer partnerId, final Integer ik) {
        _feature = feature;
        _ownerId = ownerId;
        _partnerId = partnerId;
        _ik = ik;
    }
    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        _feature = feature;
    }

    public Integer getPartnerId() {
        return _partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        _partnerId = partnerId;
    }

    public Integer getOwnerId() {
        return _ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        _ownerId = ownerId;
    }

    public Integer getIk() {
        return _ik;
    }

    public void setIk(Integer ik) {
        _ik = ik;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this._feature);
        hash = 71 * hash + Objects.hashCode(this._ownerId);
        hash = 71 * hash + Objects.hashCode(this._partnerId);
        hash = 71 * hash + Objects.hashCode(this._ik);
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
        final CooperationrightKey other = (CooperationrightKey) obj;
        if (_feature != other._feature) {
            return false;
        }
        if (!Objects.equals(_ownerId, other._ownerId)) {
            return false;
        }
        if (!Objects.equals(_partnerId, other._partnerId)) {
            return false;
        }
        return Objects.equals(_ik, other._ik);
    }
    
}
