/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.base.feature.dropbox.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "listDropboxType")
public class DropBoxType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "dbtId")
    private Integer _id;
    @Column(name = "dbtName")
    private String _name;
    @Column(name = "dbtNeedsIK")
    private boolean _needsIK;
    @Column(name = "dbtValidity")
    private Integer _validity;
    @Column(name = "dbtFolder")
    private String _folder;

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public boolean isNeedsIK() {
        return _needsIK;
    }

    public void setNeedsIK(boolean needsIK) {
        _needsIK = needsIK;
    }

    public Integer getValidity() {
        return _validity;
    }

    public void setValidity(Integer validity) {
        this._validity = validity;
    }
// </editor-fold>

    public String getFolder() {
        return _folder;
    }

    public void setFolder(String folder) {
        this._folder = folder;
    }

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DropBoxType)) {
            return false;
        }
        DropBoxType other = (DropBoxType) object;
        if ((_id == null && other.getId() != null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.DropBoxType[ id=" + _id + " ]";
    }
    // </editor-fold>
}
