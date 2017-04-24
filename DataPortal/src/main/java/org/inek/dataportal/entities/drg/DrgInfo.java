/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.drg;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author muellermi
 */
@Entity
@Table(schema = "dbo", name = "listDrg")
public class DrgInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drId")
    private Integer _id;
    @Column(name = "drYear")
    private int _year;
    @Column(name = "drDrg")
    private String _code;
    @Column(name = "drText")
    private String _text;
    @Column(name = "drSearchWords")
    private String _searchWords;

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        this._id = id;
    }

    public String getCode() {
        return _code;
    }

    public void setCode(String code) {
        this._code = code;
    }

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        this._year = year;
    }

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }

    public String getSearchWords() {
        return _searchWords;
    }

    public void setSearchWords(String searchWords) {
        this._searchWords = searchWords;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DrgInfo)) {
            return false;
        }
        DrgInfo other = (DrgInfo) object;
        if ((_id == null && other.getId() != null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return _id + " (" + getYear() + ") " + _text.substring(0, 30) + (_text.length() > 30 ? "..." : "");
    }
    // </editor-fold>
}
