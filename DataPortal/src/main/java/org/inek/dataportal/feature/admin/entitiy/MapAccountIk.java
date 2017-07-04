package org.inek.dataportal.feature.admin.entitiy;

import java.io.Serializable;

/**
 *
 * @author muellermi
 */
public class MapAccountIk implements Serializable {

    private int _accountId;
    private int _ik;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this._accountId;
        hash = 89 * hash + this._ik;
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
        final MapAccountIk other = (MapAccountIk) obj;
        return _accountId == other._accountId && _ik == other._ik;
    }

}
