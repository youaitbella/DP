/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Basic;
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
@Table(catalog = "dataportaldev", schema = "calc")
@XmlRootElement
public class KGLListMedInfra implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @Basic(optional = false)
    @NotNull
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
    private int _costTypeID;

    public int getCostTypeID() {
        return _costTypeID;
    }

    public void setCostTypeID(int costTypeID) {
        this._costTypeID = costTypeID;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _costCenter">
    @Basic(optional = false)
    @NotNull
    @Size(max = 20)
    private String _costCenter = "";

    public String getCostCenter() {
        return _costCenter;
    }

    public void setCostCenter(String costCenter) {
        this._costCenter = costCenter;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _costCenterText">
    @Basic(optional = false)
    @NotNull
    @Size(max = 100)
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
    private double _amount;

    public double getAmount() {
        return _amount;
    }

    public void setAmount(double amount) {
        this._amount = amount;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationID">
//    @JoinColumn(name = "miBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    private int _baseInformationID;

    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int baseInformationID) {
        this._baseInformationID = baseInformationID;
    }
    //</editor-fold>

    public KGLListMedInfra() {
    }

    public KGLListMedInfra(Integer miID) {
        this._id = miID;
    }

    public KGLListMedInfra(Integer miID, int miCostTypeID, String miCostCenter, String miCostCenterText, String miKeyUsed, double miAmount) {
        this._id = miID;
        this._costTypeID = miCostTypeID;
        this._costCenter = miCostCenter;
        this._costCenterText = miCostCenterText;
        this._keyUsed = miKeyUsed;
        this._amount = miAmount;
    }


    @Override
    public int hashCode() {
        
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListMedInfra)) {
            return false;
        }
        KGLListMedInfra other = (KGLListMedInfra) object;
        return (this._id == other._id);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListMedInfra[ miID=" + _id + " ]";
    }
    
}
