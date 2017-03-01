/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Objects;
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
    @Column(name = "ktBaseInformationId")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property ktCostCenterID">
    @Column(name = "ktCostCenterID")
    private int _ktCostCenterId;

    public int getKtCostCenterId() {
        return _ktCostCenterId;
    }

    public void setKtCostCenterId(int ktCostCenterId) {
        this._ktCostCenterId = ktCostCenterId;
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
        this._ktCostCenterId = ktCostCenterID;
        this._text = ktText;
        this._caseCount = ktCaseCnt;
        this._amount = ktAmount;
        this._delimitationAmount = ktDelimitationAmount;
        this._rank = ktRank;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this._id;
        
        if (this._id != -1) return hash;
        
        hash = 23 * hash + this._baseInformationId;
        hash = 23 * hash + this._ktCostCenterId;
        hash = 23 * hash + Objects.hashCode(this._text);
        hash = 23 * hash + this._caseCount;
        hash = 23 * hash + this._amount;
        hash = 23 * hash + this._delimitationAmount;
        hash = 23 * hash + this._rank;
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
        final KGLListKstTop other = (KGLListKstTop) obj;
        
        if (this._id != -1 && this._id == other._id) return true;
         
        if (this._id != other._id) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (this._ktCostCenterId != other._ktCostCenterId) {
            return false;
        }
        if (this._caseCount != other._caseCount) {
            return false;
        }
        if (this._amount != other._amount) {
            return false;
        }
        if (this._delimitationAmount != other._delimitationAmount) {
            return false;
        }
        if (this._rank != other._rank) {
            return false;
        }
        if (!Objects.equals(this._text, other._text)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListKstTop[ ktID=" + _id + " ]";
    }

}
