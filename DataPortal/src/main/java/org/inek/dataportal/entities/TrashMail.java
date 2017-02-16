/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "listTrashMail")
public class TrashMail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tmID")
    private int _id;

    @Column(name = "tmDomain")
    private String _domain;

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public String getDomain() {
        return _domain;
    }

    public void setDomain(String domain) {
        _domain = domain;
    }
    
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TrashMail)) {
            return false;
        }
        TrashMail other = (TrashMail) object;
        
        return _id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.entities.TrashMail[id=" + _id + "]";
    }
    // </editor-fold>
    
}
