package org.inek.dataportal.common.specificfunction.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.common.utils.Documentation;

@Entity
@Table(name = "listSpecificFunction", schema = "spf")
public class SpecificFunction implements Serializable {

    private static final long serialVersionUID = 1L;

    public SpecificFunction() {
    }
    
    public SpecificFunction(int id, String text) {
        _id = id;
        _text = text;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sfId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Text">
    @Column(name = "sfText")
    @Documentation(key = "lblNotation")
    private String _text = "";

    @Size(max = 200)
    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode, equals, toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this._id;
        hash = 97 * hash + Objects.hashCode(this._text);
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
        final SpecificFunction other = (SpecificFunction) obj;
        if (this._id != other._id) {
            return false;
        }
        if (!Objects.equals(this._text, other._text)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return _text;
    }
    //</editor-fold>

}
