package org.inek.dataportal.entities.account;

import java.io.Serializable;
import java.util.Objects;

public class AccountActivationId implements Serializable {

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this._accountId);
        hash = 73 * hash + Objects.hashCode(this._guid);
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
        final AccountActivationId other = (AccountActivationId) obj;
        if (!Objects.equals(this._accountId, other._accountId)) {
            return false;
        }
        if (!Objects.equals(this._guid, other._guid)) {
            return false;
        }
        return true;
    }
    Integer _accountId;
    String _guid;
}
