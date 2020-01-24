package org.inek.dataportal.common.data.common;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "listOps")
public class ProcedureInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opId")
    private Integer _id;
    @Column(name = "opCode")
    private String _code;
    @Column(name = "opCodeCompact")
    private String _codeShort;
    @Column(name = "opName")
    private String _name;
    @Column(name = "opFirstYear")
    private int _firstYear;
    @Column(name = "opLastYear")
    private int _lastYear;
    @Column(name = "opSearchWords")
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
        if (!(object instanceof ProcedureInfo)) {
            return false;
        }
        ProcedureInfo other = (ProcedureInfo) object;
        if ((_id == null && other.getId() != null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        int maxLen = 30;
        return _id + " (" + getFirstYear() + " - " + getLastYear() + ") " +
                _name.substring(0, maxLen) + (_name.length() > maxLen ? "..." : "");
    }

    // </editor-fold>
}
