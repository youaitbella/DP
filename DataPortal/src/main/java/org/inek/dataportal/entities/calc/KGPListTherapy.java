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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "KGPListTherapy", schema = "calc")
@XmlRootElement
public class KGPListTherapy implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "thID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _costCenterID">
    @Basic(optional = false)
    @NotNull
    @Column(name = "thCostCenterID")
    private int _costCenterId;

    public int getCostCenterId() {
        return _costCenterId;
    }

    public void setCostCenterId(int costCenterId) {
        this._costCenterId = costCenterId;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _costCenterText">
    @Basic(optional = false)
    @NotNull
    @Size(max = 100)
    @Column(name = "thCostCenterText")
    private String _costCenterText = "";

    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _externalService">
    @Basic(optional = false)
    @NotNull
    @Column(name = "thExternalService")
    private int _externalService;

    public int getExternalService() {
        return _externalService;
    }

    public void setExternalService(int externalService) {
        this._externalService = externalService;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _keyUsed">
    @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "thKeyUsed")
    private String _keyUsed = "";

    public String getKeyUsed() {
        return _keyUsed;
    }

    public void setKeyUsed(String keyUsed) {
        this._keyUsed = keyUsed;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _serviceUnitsCt1">
    @Basic(optional = false)
    @NotNull
    @Column(name = "thServiceUnitsCt1")
    private int _serviceUnitsCt1;

    public int getServiceUnitsCt1() {
        return _serviceUnitsCt1;
    }

    public void setServiceUnitsCt1(int serviceUnitsCt1) {
        this._serviceUnitsCt1 = serviceUnitsCt1;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _personalCostCt1">
    @Basic(optional = false)
    @NotNull
    @Column(name = "thPersonalCostCt1")
    private int _personalCostCt1;

    public int getPersonalCostCt1() {
        return _personalCostCt1;
    }

    public void setPersonalCostCt1(int personalCostCt1) {
        this._personalCostCt1 = personalCostCt1;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _serviceUnitsCt2">
    @Basic(optional = false)
    @NotNull
    @Column(name = "thServiceUnitsCt2")
    private int _serviceUnitsCt2;

    public int getServiceUnitsCt2() {
        return _serviceUnitsCt2;
    }

    public void setServiceUnitsCt2(int serviceUnitsCt2) {
        this._serviceUnitsCt2 = serviceUnitsCt2;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _personalCostCt2">
    @Basic(optional = false)
    @NotNull
    @Column(name = "thPersonalCostCt2")
    private int _personalCostCt2;

    public int getPersonalCostCt2() {
        return _personalCostCt2;
    }

    public void setPersonalCostCt2(int personalCostCt2) {
        this._personalCostCt2 = personalCostCt2;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _serviceUnitsCt3">
    @Basic(optional = false)
    @NotNull
    @Column(name = "thServiceUnitsCt3")
    private int _serviceUnitsCt3;

    public int getServiceUnitsCt3() {
        return _serviceUnitsCt3;
    }

    public void setServiceUnitsCt3(int serviceUnitsCt3) {
        this._serviceUnitsCt3 = serviceUnitsCt3;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _personalCostCt3">
    @Basic(optional = false)
    @NotNull
    @Column(name = "thPersonalCostCt3")
    private int _personalCostCt3;

    public int getPersonalCostCt3() {
        return _personalCostCt3;
    }

    public void setPersonalCostCt3(int personalCostCt3) {
        this._personalCostCt3 = personalCostCt3;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _serviceUnitsCt3a">
    @Basic(optional = false)
    @NotNull
    @Column(name = "thServiceUnitsCt3a")
    private int _serviceUnitsCt3a;

    public int getServiceUnitsCt3a() {
        return _serviceUnitsCt3a;
    }

    public void setServiceUnitsCt3a(int serviceUnitsCt3a) {
        this._serviceUnitsCt3a = serviceUnitsCt3a;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _personalCostCt3a">
    @Basic(optional = false)
    @NotNull
    @Column(name = "thPersonalCostCt3a")
    private int _personalCostCt3a;

    public int getPersonalCostCt3a() {
        return _personalCostCt3a;
    }

    public void setPersonalCostCt3a(int personalCostCt3a) {
        this._personalCostCt3a = personalCostCt3a;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _serviceUnitsCt3b">
    @Basic(optional = false)
    @NotNull
    @Column(name = "thServiceUnitsCt3b")
    private int _serviceUnitsCt3b;

    public int getServiceUnitsCt3b() {
        return _serviceUnitsCt3b;
    }

    public void setServiceUnitsCt3b(int serviceUnitsCt3b) {
        this._serviceUnitsCt3b = serviceUnitsCt3b;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _personalCostCt3b">
    @Basic(optional = false)
    @NotNull
    @Column(name = "thPersonalCostCt3b")
    private int _personalCostCt3b;

    public int getPersonalCostCt3b() {
        return _personalCostCt3b;
    }

    public void setPersonalCostCt3b(int personalCostCt3b) {
        this._personalCostCt3b = personalCostCt3b;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _serviceUnitsCt3c">
    @Basic(optional = false)
    @NotNull
    @Column(name = "thServiceUnitsCt3c")
    private int _serviceUnitsCt3c;

    public int getServiceUnitsCt3c() {
        return _serviceUnitsCt3c;
    }

    public void setServiceUnitsCt3c(int serviceUnitsCt3c) {
        this._serviceUnitsCt3c = serviceUnitsCt3c;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _personalCostCt3c">
    @Basic(optional = false)
    @NotNull
    @Column(name = "thPersonalCostCt3c")
    private int _personalCostCt3c;

    public int getPersonalCostCt3c() {
        return _personalCostCt3c;
    }

    public void setPersonalCostCt3c(int personalCostCt3c) {
        this._personalCostCt3c = personalCostCt3c;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "thBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "thBaseInformationID")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    public KGPListTherapy() {
    }

    public KGPListTherapy(int thID) {
        this._id = thID;
    }

    public KGPListTherapy(int thID, int thCostCenterID, String thCostCenterText, int thExternalService, String thKeyUsed, int thServiceUnitsCt1, int thPersonalCostCt1, int thServiceUnitsCt2, int thPersonalCostCt2, int thServiceUnitsCt3, int thPersonalCostCt3, int thServiceUnitsCt3a, int thPersonalCostCt3a, int thServiceUnitsCt3b, int thPersonalCostCt3b, int thServiceUnitsCt3c, int thPersonalCostCt3c) {
        this._id = thID;
        this._costCenterId = thCostCenterID;
        this._costCenterText = thCostCenterText;
        this._externalService = thExternalService;
        this._keyUsed = thKeyUsed;
        this._serviceUnitsCt1 = thServiceUnitsCt1;
        this._personalCostCt1 = thPersonalCostCt1;
        this._serviceUnitsCt2 = thServiceUnitsCt2;
        this._personalCostCt2 = thPersonalCostCt2;
        this._serviceUnitsCt3 = thServiceUnitsCt3;
        this._personalCostCt3 = thPersonalCostCt3;
        this._serviceUnitsCt3a = thServiceUnitsCt3a;
        this._personalCostCt3a = thPersonalCostCt3a;
        this._serviceUnitsCt3b = thServiceUnitsCt3b;
        this._personalCostCt3b = thPersonalCostCt3b;
        this._serviceUnitsCt3c = thServiceUnitsCt3c;
        this._personalCostCt3c = thPersonalCostCt3c;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 47 * hash + this._costCenterId;
        hash = 47 * hash + Objects.hashCode(this._costCenterText);
        hash = 47 * hash + this._externalService;
        hash = 47 * hash + Objects.hashCode(this._keyUsed);
        hash = 47 * hash + this._serviceUnitsCt1;
        hash = 47 * hash + this._personalCostCt1;
        hash = 47 * hash + this._serviceUnitsCt2;
        hash = 47 * hash + this._personalCostCt2;
        hash = 47 * hash + this._serviceUnitsCt3;
        hash = 47 * hash + this._personalCostCt3;
        hash = 47 * hash + this._serviceUnitsCt3a;
        hash = 47 * hash + this._personalCostCt3a;
        hash = 47 * hash + this._serviceUnitsCt3b;
        hash = 47 * hash + this._personalCostCt3b;
        hash = 47 * hash + this._serviceUnitsCt3c;
        hash = 47 * hash + this._personalCostCt3c;
        hash = 47 * hash + this._baseInformationId;
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListTherapy)) {
            return false;
        }
        final KGPListTherapy other = (KGPListTherapy) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._costCenterId != other._costCenterId) {
            return false;
        }
        if (this._externalService != other._externalService) {
            return false;
        }
        if (this._serviceUnitsCt1 != other._serviceUnitsCt1) {
            return false;
        }
        if (this._personalCostCt1 != other._personalCostCt1) {
            return false;
        }
        if (this._serviceUnitsCt2 != other._serviceUnitsCt2) {
            return false;
        }
        if (this._personalCostCt2 != other._personalCostCt2) {
            return false;
        }
        if (this._serviceUnitsCt3 != other._serviceUnitsCt3) {
            return false;
        }
        if (this._personalCostCt3 != other._personalCostCt3) {
            return false;
        }
        if (this._serviceUnitsCt3a != other._serviceUnitsCt3a) {
            return false;
        }
        if (this._personalCostCt3a != other._personalCostCt3a) {
            return false;
        }
        if (this._serviceUnitsCt3b != other._serviceUnitsCt3b) {
            return false;
        }
        if (this._personalCostCt3b != other._personalCostCt3b) {
            return false;
        }
        if (this._serviceUnitsCt3c != other._serviceUnitsCt3c) {
            return false;
        }
        if (this._personalCostCt3c != other._personalCostCt3c) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (!Objects.equals(this._costCenterText, other._costCenterText)) {
            return false;
        }
        return Objects.equals(this._keyUsed, other._keyUsed);
    }
    
    
    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListTherapy[ thID=" + _id + " ]";
    }
    //</editor-fold>
    
}
