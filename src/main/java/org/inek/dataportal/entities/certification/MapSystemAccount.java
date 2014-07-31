package org.inek.dataportal.entities.certification;

import java.io.Serializable;

/**
 *
 * @author muellermi
 */
public class MapSystemAccount implements Serializable {

    int _accountId;
    int _inekRoleId;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this._accountId;
        hash = 89 * hash + this._inekRoleId;
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
        final MapSystemAccount other = (MapSystemAccount) obj;
        return _accountId == other._accountId && _inekRoleId == other._inekRoleId;
    }

}
