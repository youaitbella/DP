/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListMedInfra", schema = "calc")
@XmlRootElement
public class KGPListMedInfra implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "miID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _costTypeID">
    @Basic(optional = false)
    @NotNull
    @Column(name = "miCostTypeID")
    private int _costTypeId;

    public int getCostTypeId() {
        return _costTypeId;
    }

    public void setCostTypeId(int costTypeId) {
        this._costTypeId = costTypeId;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _costCenterNumber">
    @Basic(optional = false)
    @NotNull
    @Size(max = 20)
    @Column(name = "miCostCenterNumber")
    private String _costCenterNumber = "";

    public String getCostCenterNumber() {
        return _costCenterNumber;
    }

    public void setCostCenterNumber(String costCenterNumber) {
        this._costCenterNumber = costCenterNumber;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _costCenterText">
    @Basic(optional = false)
    @NotNull
    @Size(max = 100)
    @Column(name = "miCostCenterText")
    private String _costCenterText = "";
    
    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _keyUsed">
    @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "miKeyUsed")
    private String _keyUsed = "";

    public String getKeyUsed() {
        return _keyUsed;
    }

    public void setKeyUsed(String keyUsed) {
        this._keyUsed = keyUsed;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _amount">
    @Basic(optional = false)
    @NotNull
    @Column(name = "miAmount")
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "miBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "miBaseInformationID")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    public KGPListMedInfra() {
    }

    public KGPListMedInfra(int miID) {
        this._id = miID;
    }

    public KGPListMedInfra(int miID, int miCostTypeID, String miCostCenterNumber, String miCostCenterText, String miKeyUsed, int miAmount) {
        this._id = miID;
        this._costTypeId = miCostTypeID;
        this._costCenterNumber = miCostCenterNumber;
        this._costCenterText = miCostCenterText;
        this._keyUsed = miKeyUsed;
        this._amount = miAmount;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 97 * hash + this._costTypeId;
        hash = 97 * hash + Objects.hashCode(this._costCenterNumber);
        hash = 97 * hash + Objects.hashCode(this._costCenterText);
        hash = 97 * hash + Objects.hashCode(this._keyUsed);
        hash = 97 * hash + this._amount;
        hash = 97 * hash + this._baseInformationId;
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListMedInfra)) {
            return false;
        }
        final KGPListMedInfra other = (KGPListMedInfra) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._costTypeId != other._costTypeId) {
            return false;
        }
        if (this._amount != other._amount) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (!Objects.equals(this._costCenterNumber, other._costCenterNumber)) {
            return false;
        }
        if (!Objects.equals(this._costCenterText, other._costCenterText)) {
            return false;
        }
        if (!Objects.equals(this._keyUsed, other._keyUsed)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListMedInfra[ miID=" + _id + " ]";
    }
    //</editor-fold>
    
}
