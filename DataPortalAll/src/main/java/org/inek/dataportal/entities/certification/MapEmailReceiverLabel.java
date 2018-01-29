/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.certification;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "mapEmailReceiverLabel", schema = "crt")
public class MapEmailReceiverLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "erlId")
    private int _erlId = -1;

    public int getEmailReceiverLabelId() {
        return _erlId;
    }

    public void setEmailReceiverLabelId(int id) {
        _erlId = id;
    }
    
    @Column(name = "erlLabel")
    private String _label = "";

    public String getLabel() {
        return _label;
    }

    public void setLabel(String label) {
        this._label = label;
    }
    
    @Override
    public int hashCode() {
        int hash = 9;
        hash = 5 * hash + this._erlId;
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
        final MapEmailReceiverLabel other = (MapEmailReceiverLabel) obj;
        return   _erlId == other._erlId && _label.equals(other._label);
    }

}
