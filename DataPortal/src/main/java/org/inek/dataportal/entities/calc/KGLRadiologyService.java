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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.inek.dataportal.entities.iface.BaseIdValue;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLRadiologyService", schema = "calc")
@XmlRootElement
public class KGLRadiologyService implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rsID", updatable = false, nullable = false)
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

    //<editor-fold defaultstate="collapsed" desc="Property _opsCode">
    @Column(name = "rsOpsCode")
    @Documentation(name = "OPS", rank = 10)
    private String _opsCode = "";

    @Size(max = 10)
    public String getOpsCode() {
        return _opsCode;
    }

    public void setOpsCode(String opsCode) {
        this._opsCode = opsCode;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceCost">
    @Column(name = "rsServiceCost")
    @Documentation(name = "Kosten pro Leistung", rank = 20)
    private int _serviceCost;

    public int getServiceCost() {
        return _serviceCost;
    }

    public void setServiceCost(int serviceCost) {
        this._serviceCost = serviceCost;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _caseCntStationary">
    @Column(name = "rsCaseCntStationary")
    @Documentation(name = "Fallzahl stationär", rank = 30)
    private int _caseCntStationary;

    public int getCaseCntStationary() {
        return _caseCntStationary;
    }

    public void setCaseCntStationary(int caseCntStationary) {
        this._caseCntStationary = caseCntStationary;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _caseCntAmbulant">
    @Column(name = "rsCaseCntAmbulant")
    @Documentation(name = "Fallzahl ambulant", rank = 40)
    private int _caseCntAmbulant;

    public int getCaseCntAmbulant() {
        return _caseCntAmbulant;
    }

    public void setCaseCntAmbulant(int caseCntAmbulant) {
        this._caseCntAmbulant = caseCntAmbulant;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _ambulantAmount">
    @Column(name = "rsAmbulantAmount")
    @Documentation(name = "abgegr. Kostenvolumen ambulant", rank = 50)
    private int _ambulantAmount;

    public int getAmbulantAmount() {
        return _ambulantAmount;
    }

    public void setAmbulantAmount(int abulantAmount) {
        this._ambulantAmount = abulantAmount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "_baseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "rsBaseInformationId")
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

    //<editor-fold defaultstate="collapsed" desc="Property rsContentTextID">
//    @JoinColumn(name = "rsContentTextID", referencedColumnName = "ctID")
//    @ManyToOne(optional = false)
    @Column(name = "rsContentTextID")
    private int rsContentTextID;

    public int getRsContentTextID() {
        return rsContentTextID;
    }

    public void setRsContentTextID(int rsContentTextID) {
        this.rsContentTextID = rsContentTextID;
    }
    //</editor-fold>

    public KGLRadiologyService() {
    }

    public KGLRadiologyService(Integer rsID) {
        this._id = rsID;
    }

    public KGLRadiologyService(int rsID, String rsOpsCode, int rsServiceCost, int rsCaseCntStationary, int rsCaseCntAmbulant, int rsAbulantAmount) {
        this._id = rsID;
        this._opsCode = rsOpsCode;
        this._serviceCost = rsServiceCost;
        this._caseCntStationary = rsCaseCntStationary;
        this._caseCntAmbulant = rsCaseCntAmbulant;
        this._ambulantAmount = rsAbulantAmount;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 71 * hash + Objects.hashCode(this._opsCode);
        hash = 71 * hash + this._serviceCost;
        hash = 71 * hash + this._caseCntStationary;
        hash = 71 * hash + this._caseCntAmbulant;
        hash = 71 * hash + this._ambulantAmount;
        hash = 71 * hash + this._baseInformationId;
        hash = 71 * hash + this.rsContentTextID;
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
        final KGLRadiologyService other = (KGLRadiologyService) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (Double.doubleToLongBits(this._serviceCost) != Double.doubleToLongBits(other._serviceCost)) {
            return false;
        }
        if (this._caseCntStationary != other._caseCntStationary) {
            return false;
        }
        if (this._caseCntAmbulant != other._caseCntAmbulant) {
            return false;
        }
        if (Double.doubleToLongBits(this._ambulantAmount) != Double.doubleToLongBits(other._ambulantAmount)) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (this.rsContentTextID != other.rsContentTextID) {
            return false;
        }
        if (!Objects.equals(this._opsCode, other._opsCode)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLRadiologyService[ rsID=" + _id + " ]";
    }
    //</editor-fold>

}
