package org.inek.dataportal.entities.certification;

import java.io.Serializable;
import java.util.Objects;

public class GrouperId implements Serializable {
    int _systemId;
    int _accountId;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(_systemId);
        hash = 73 * hash + Objects.hashCode(_accountId);
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
        final GrouperId other = (GrouperId) obj;
        return _systemId == other._systemId && _accountId == other._accountId;
    }

}
