package org.inek.dataportal.entities.certification;

import java.io.Serializable;

/**
 *
 * @author muellermi
 */
public class MapSystemAccount implements Serializable {

    int _systemId;
    int _accountId;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this._systemId;
        hash = 89 * hash + this._accountId;
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
        return _systemId == other._systemId && _accountId == other._accountId;
    }

}
