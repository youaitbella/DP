package org.inek.dataportal.entities.account;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "WeakPassword")
public class WeakPassword implements Serializable {
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Passord">
    @Id
    @Column(name = "wpPassword")
    private String _password;
    
    public String getPassword() {
        return _password;
    }

    public void setPassword(String id) {
        _password = id;
    }
    // </editor-fold>
    

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this._password);
        return hash;
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
        final WeakPassword other = (WeakPassword) obj;
        if (!Objects.equals(this._password, other._password)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "WeakPassword{" + "_password=" + _password + '}';
    }
    // </editor-fold>

  
}
