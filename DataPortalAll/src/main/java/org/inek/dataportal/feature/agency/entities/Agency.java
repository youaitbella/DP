package org.inek.dataportal.feature.agency.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "listAgency")
public class Agency implements Serializable {
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agID")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "agName")
    private String _name;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Agency)) {
            return false;
        }
        Agency other = (Agency) object;
        
        return _id == other._id;
    }

    @Override
    public String toString() {
        return "Agency[id=" + _id + "; name=" + _name + "]";
    }
    // </editor-fold>
    
}
