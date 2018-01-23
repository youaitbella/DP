/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc.drg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.entities.iface.BaseIdValue;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListKstTop", schema = "calc")
public class KGLListKstTop implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ktID", updatable = false, nullable = false)
    private int _id = -1;
    
    @Override
    public int getId() {
        return _id;
    }
    
    @Override
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property baseInformationID">
    @Column(name = "ktBaseInformationId")
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property ktCostCenterId">
    @Column(name = "ktCostCenterId")
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
    @Documentation(key = "lblNotation")
    private String _text = "";

    @Size(max = 100, message = "Für Bezeichnung sind max. {max} Zeichen zulässig.")
    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property caseCount">
    @Column(name = "ktCaseCnt")
    @Documentation(key = "lblCaseCount")
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
    @Documentation(name = "Erlösvolumen")
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property medicalService">
    @Column(name = "ktMedicalService")
    @Documentation(name = "Ärtzlicher Dienst")
    private int _medicalService;

    public int getMedicalService() {
        return _medicalService;
    }

    public void setMedicalService(int medicalService) {
        this._medicalService = medicalService;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property functionalService">
    @Column(name = "ktFunctionalService")
    @Documentation(name = "Funktinaler Dienst")
    private int _functionalService;

    public int getFunctionalService() {
        return _functionalService;
    }

    public void setFunctionalService(int functionalService) {
        this._functionalService = functionalService;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property delimitationAmount">
    @Column(name = "ktDelimitationAmount")
    @Documentation(name = "abgegr. Kostenvolumen")
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

    public KGLListKstTop(Integer ktID, int ktCostCenterId, String ktText, int ktCaseCnt, int ktAmount, int ktDelimitationAmount, int ktRank) {
        this._id = ktID;
        this._ktCostCenterId = ktCostCenterId;
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
    @SuppressWarnings("CyclomaticComplexity")
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

    @JsonIgnore
    public boolean isEmpty(){
        return _text.isEmpty() && _caseCount <= 0 && _amount <= 0 && _delimitationAmount <= 0;
    }
}
