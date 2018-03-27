/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.psy;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.inek.dataportal.common.data.iface.BaseIdValue;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPDocuments", schema = "calc")
@XmlRootElement
public class KGPDocuments implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doID", updatable = false, nullable = false)
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _name">
    @Column(name = "doName")
    private String _name = "";

    @Size(max = 250)
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _content">
    @Lob
    @Column(name = "doContent")
    private byte[] _content = new byte[0];

    public byte[] getContent() {
        return _content;
    }

    public void setContent(byte[] content) {
        this._content = content;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "doBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "doBaseInformationId")
    //private PeppCalcBasics _baseInformationId;
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property _sheetId">
//    @JoinColumn(name = "doSheetID", referencedColumnName = "sID")
//    @ManyToOne(optional = false)
    @Column(name = "doSheetID")
    //private KGPListSheet _sheetId;
    private int _sheetId;

    public int getSheetId() {
        return _sheetId;
    }

    public void setSheetId(int sheetId) {
        this._sheetId = sheetId;
    }
    // </editor-fold>

    public KGPDocuments() {
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + this._id;
        if (_id != -1) {
            return hash;
        }
        hash = 73 * hash + Objects.hashCode(this._name);
        hash = 73 * hash + this._baseInformationId;
        hash = 73 * hash + this._sheetId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPDocuments)) {
            return false;
        }
        final KGPDocuments other = (KGPDocuments) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (this._sheetId != other._sheetId) {
            return false;
        }
        if (!Objects.equals(this._name, other._name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPDocuments[ _ID=" + _id + " ]";
    }
    //</editor-fold>

}
