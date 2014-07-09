package org.inek.dataportal.entities.admin;

import java.io.Serializable;

/**
 *
 * @author muellermi
 */
public class MapAccountInekRoleId  implements Serializable {
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
        final MapAccountInekRoleId other = (MapAccountInekRoleId) obj;
        if (this._accountId != other._accountId) {
            return false;
        }
        if (this._inekRoleId != other._inekRoleId) {
            return false;
        }
        return true;
    }
    
}
