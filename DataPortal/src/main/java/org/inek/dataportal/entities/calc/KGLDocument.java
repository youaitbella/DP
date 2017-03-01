/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.inek.dataportal.entities.Document;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "KGLDocuments", schema = "calc")
public class KGLDocument implements Serializable, Document{
    

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property BaseInformationId">
    @Column(name = "doBaseInformationID")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        _baseInformationId = baseInformationId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SheetId">
    @Column(name = "doSheetID")
    private int _sheetId;

    public int getSheetId() {
        return _sheetId;
    }

    public void setSheetId(int sheetId) {
        _sheetId = sheetId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "doName")
    private String _name;
    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Content">
    @Lob
    @Column(name = "doContent")
    private byte[] _content;
    @Override
    public byte[] getContent() {
        return _content;
    }

    @Override
    public void setContent(byte[] content) {
        _content = content;
    }
    // </editor-fold>
    
    
    // <editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this._id;
        if (this._id != -1) return hash;
        hash = 53 * hash + this._baseInformationId;
        hash = 53 * hash + this._sheetId;
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
        final KGLDocument other = (KGLDocument) obj;
        
        if (this._id != -1 && other._id == this._id) return true;
        
        if (this._id != other._id) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        return this._sheetId == other._sheetId;
    }

    @Override
    public String toString() {
        return "KGLDocument[ id=" + _id + " ]";
    }
    // </editor-fold>
   
    
}
