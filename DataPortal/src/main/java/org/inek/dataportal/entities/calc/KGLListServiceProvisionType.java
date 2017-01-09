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
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListServiceProvisionType", schema = "calc")
@XmlRootElement
public class KGLListServiceProvisionType implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="ID">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sptID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Text">
    @Size(max = 200)
    @Column(name = "sptText")
    private String _text = "";

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FirstYear">
    @Column(name = "sptFirstYear")
    private int _firstYear;

    public int getFirstYear() {
        return _firstYear;
    }

    public void setFirstYear(int firstYear) {
        this._firstYear = firstYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="LastYear">
    @Column(name = "sptLastYear")
    private int _lastYear;

    public int getLastYear() {
        return _lastYear;
    }

    public void setLastYear(int lastYear) {
        this._lastYear = lastYear;
    }
    //</editor-fold>

    public KGLListServiceProvisionType() {
    }

    public KGLListServiceProvisionType(int sptID) {
        this._id = sptID;
    }

    public KGLListServiceProvisionType(int sptID, String sptText, int sptFirstYear, int sptLastYear) {
        this._id = sptID;
        this._text = sptText;
        this._firstYear = sptFirstYear;
        this._lastYear = sptLastYear;
    }

    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListServiceProvisionType)) {
            return false;
        }
        KGLListServiceProvisionType other = (KGLListServiceProvisionType) object;
        return this._id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListServiceProvisionType[ sptID=" + _id + " ]";
    }

}
