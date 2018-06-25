/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.infotext.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "listInfoText", schema = "adm")
public class InfoText implements Serializable {

    private static final long serialVersionUID = 1L;

    public InfoText() {
    }

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Key">
    @Column(name = "itKey")
    private String _key;

    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        _key = key;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Text">
    @Column(name = "itText")
    private String _text;

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Language">
    @Column(name = "itLanguage")
    private String _language;

    public String getLanguage() {
        return _language;
    }

    public void setLanguage(String language) {
        _language = language;
    }
    // </editor-fold>

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this._key);
        hash = 83 * hash + Objects.hashCode(this._text);
        hash = 83 * hash + Objects.hashCode(this._language);
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
        final InfoText other = (InfoText) obj;
        if (!Objects.equals(this._key, other._key)) {
            return false;
        }
        if (!Objects.equals(this._text, other._text)) {
            return false;
        }
        if (!Objects.equals(this._language, other._language)) {
            return false;
        }
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        return true;
    }

}
