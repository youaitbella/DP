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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListKstTop", schema = "calc")
@XmlRootElement
public class KGLListKstTop implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ktID")
    private int _id = -1;
    
    public int getId() {
        return _id;
    }
    
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property baseInformationID">
    @Column(name = "ktBaseInformationID")
    private int _baseInformationID;

    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int baseInformationID) {
        this._baseInformationID = baseInformationID;
    }
    //</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="Property ktCostCenterID">
    @Column(name = "ktCostCenterID")
    private int _ktCostCenterID;

    public int getKtCostCenterID() {
        return _ktCostCenterID;
    }

    public void setKtCostCenterID(int ktCostCenterID) {
        this._ktCostCenterID = ktCostCenterID;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property text">
    @Column(name = "ktText")
    private String _text = "";

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property caseCount">
    @Column(name = "ktCaseCnt")
    private int _caseCount;

    public int getCaseCount() {
        return _caseCount;
    }

    public void setCaseCount(int caseCount) {
        this._caseCount = caseCount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property amount">
    @Column(name = "ktAmount")
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property delimitationAmount">
    @Column(name = "ktDelimitationAmount")
    private int _delimitationAmount;

    public int getDelimitationAmount() {
        return _delimitationAmount;
    }

    public void setDelimitationAmount(int delimitationAmount) {
        this._delimitationAmount = delimitationAmount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ">
    @Column(name = "ktRank")
    private int _rank;

    public int getRank() {
        return _rank;
    }

    public void setRank(int rank) {
        this._rank = rank;
    }
    //</editor-fold>

    public KGLListKstTop() {
    }

    public KGLListKstTop(Integer ktID) {
        this._id = ktID;
    }

    public KGLListKstTop(Integer ktID, int ktCostCenterID, String ktText, int ktCaseCnt, int ktAmount, int ktDelimitationAmount, int ktRank) {
        this._id = ktID;
        this._ktCostCenterID = ktCostCenterID;
        this._text = ktText;
        this._caseCount = ktCaseCnt;
        this._amount = ktAmount;
        this._delimitationAmount = ktDelimitationAmount;
        this._rank = ktRank;
    }

    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListKstTop)) {
            return false;
        }
        KGLListKstTop other = (KGLListKstTop) object;
        return this._id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListKstTop[ ktID=" + _id + " ]";
    }

}
