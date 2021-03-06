package org.inek.dataportal.common.data.adm;

import java.io.Serializable;

/**
 *
 * @author muellermi
 */
public class MapAccountInekRoleId implements Serializable {

    private static final long serialVersionUID = 1L;
    private int _accountId;
    private int _inekRoleId;

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
        return _accountId == other._accountId && _inekRoleId == other._inekRoleId;
    }

}
