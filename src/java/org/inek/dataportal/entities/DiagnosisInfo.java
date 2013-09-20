package org.inek.dataportal.entities;

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
@Table(name = "listIcd")
public class DiagnosisInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "icId")
    private Integer _id;
    @Column(name = "icCode")
    private String _code;
    @Column(name = "icCodeCompact")
    private String _codeShort;
    @Column(name = "icName")
    private String _name;
    @Column(name = "icFirstYear")
    private int _firstYear;
    @Column(name = "icLastYear")
    private int _lastYear;
    @Column(name = "icSearchWords")
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

    public String getCodeShort() {
        return _codeShort;
    }

    public void setCodeShort(String codeShort) {
        this._codeShort = codeShort;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public int getFirstYear() {
        return _firstYear;
    }

    public void setFirstYear(int year) {
        this._firstYear = year;
    }

    public int getLastYear() {
        return _lastYear;
    }

    public void setLastYear(int year) {
        this._lastYear = year;
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
        if (!(object instanceof DiagnosisInfo)) {
            return false;
        }
        DiagnosisInfo other = (DiagnosisInfo) object;
        if ((_id == null && other.getId() != null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return _id + " (" + getFirstYear() + " - " + getLastYear() + ") " + _name.substring(0, 30) + (_name.length() > 30 ? "..." : "");
    }

    // </editor-fold>

}
