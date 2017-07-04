package org.inek.dataportal.feature.admin.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "Config", schema = "adm")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Id
    @Column(name = "cfKey")
    private String _key;
    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        _key = key;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Value">
    @Column(name = "cfValue")
    private String _value;
    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        _value = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _key.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Config)) {
            return false;
        }
        Config other = (Config) object;
        return Objects.equals(_key, other._key);
    }

    @Override
    public String toString() {
        return "org.inek.entities.Config[name=" + _key + "]";
    }
    // </editor-fold>

}
