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

    public InfoText(String key){
        _key = key;
        _modified = true;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itId")
    private Integer _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Key">
    @Column(name = "itKey")
    private String _key = "";

    public String getKey() {
        return _key;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ShortText">
    @Column(name = "itShortText")
    private String _shortText = "";

    public String getShortText() {
        return _shortText;
    }

    public void setShortText(String shortText) {
        if (!_shortText.equals(shortText)) {
            setModified();
            _shortText = shortText;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Description">
    @Column(name = "itDescription")
    private String _description = "";

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        if (!_description.equals(description)) {
            setModified();
            _description = description;
        }
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Property Modified">
    @Transient
    private boolean _modified;

    public boolean getModified() {
        return _modified;
    }

    public void setModified() {
        _modified = true;
    }

    // </editor-fold>    
    // <editor-fold defaultstate="collapsed" desc="Property Language">
    @Column(name = "itLanguage")
    private String _language = "DE";

    public String getLanguage() {
        return _language;
    }

    public void setLanguage(String language) {
        if (!_language.equals(language)) {
            setModified();
            _language = language;
        }
    }
    // </editor-fold>

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this._key);
        hash = 83 * hash + Objects.hashCode(this._shortText);
        hash = 83 * hash + Objects.hashCode(this._description);
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
        if (!Objects.equals(this._shortText, other._shortText)) {
            return false;
        }
        if (!Objects.equals(this._description, other._description)) {
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
