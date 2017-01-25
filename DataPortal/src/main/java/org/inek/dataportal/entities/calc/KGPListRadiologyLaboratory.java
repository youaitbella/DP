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
@Table(name = "KGPListRadiologyLaboratory", schema = "calc")
@XmlRootElement
public class KGPListRadiologyLaboratory implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _costCenterId">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlCostCenterID")
    private int _costCenterId;

    public int getCostCenterId() {
        return _costCenterId;
    }

    public void setCostCenterId(int costCenterId) {
        this._costCenterId = costCenterId;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _costCenterNumber">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlCostCenterNumber")
    private int _costCenterNumber;

    public int getCostCenterNumber() {
        return _costCenterNumber;
    }

    public void setCostCenterNumber(int costCenterNumber) {
        this._costCenterNumber = costCenterNumber;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _costCenterText">
    @Basic(optional = false)
    @NotNull
    @Size(max = 100)
    @Column(name = "rlCostCenterText")
    private String _costCenterText = "";

    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _serviceDocHome">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocHome")
    private boolean _serviceDocHome;

    public boolean isServiceDocHome() {
        return _serviceDocHome;
    }

    public void setServiceDocHome(boolean serviceDocHome) {
        this._serviceDocHome = serviceDocHome;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _serviceDocDKG">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocDKG")
    private boolean _serviceDocDKG;

    public boolean isServiceDocDKG() {
        return _serviceDocDKG;
    }

    public void setServiceDocDKG(boolean serviceDocDKG) {
        this._serviceDocDKG = serviceDocDKG;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _serviceDocEBM">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocEBM")
    private boolean _serviceDocEBM;

    public boolean isServiceDocEBM() {
        return _serviceDocEBM;
    }

    public void setServiceDocEBM(boolean serviceDocEBM) {
        this._serviceDocEBM = serviceDocEBM;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _serviceDocGOA">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocGOA")
    private boolean _serviceDocGOA;

    public boolean isServiceDocGOA() {
        return _serviceDocGOA;
    }

    public void setServiceDocGOA(boolean serviceDocGOA) {
        this._serviceDocGOA = serviceDocGOA;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _serviceDocDif">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocDif")
    private boolean _serviceDocDif;

    public boolean isServiceDocDif() {
        return _serviceDocDif;
    }

    public void setServiceDocDif(boolean serviceDocDif) {
        this._serviceDocDif = serviceDocDif;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _description">
    @Basic(optional = false)
    @NotNull
    @Size(max = 2147483647)
    @Column(name = "rlDescription")
    private String _description = "";

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "rlBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "rlBaseInformationID")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    public KGPListRadiologyLaboratory() {
    }

    public KGPListRadiologyLaboratory(int rlID) {
        this._id = rlID;
    }

    public KGPListRadiologyLaboratory(int rlID, int rlCostCenterID, int rlCostCenterNumber, String rlCostCenterText, boolean rlServiceDocHome, boolean rlServiceDocDKG, boolean rlServiceDocEBM, boolean rlServiceDocGOA, boolean rlServiceDocDif, String rlDescription) {
        this._id = rlID;
        this._costCenterId = rlCostCenterID;
        this._costCenterNumber = rlCostCenterNumber;
        this._costCenterText = rlCostCenterText;
        this._serviceDocHome = rlServiceDocHome;
        this._serviceDocDKG = rlServiceDocDKG;
        this._serviceDocEBM = rlServiceDocEBM;
        this._serviceDocGOA = rlServiceDocGOA;
        this._serviceDocDif = rlServiceDocDif;
        this._description = rlDescription;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 23 * hash + this._costCenterId;
        hash = 23 * hash + this._costCenterNumber;
        hash = 23 * hash + Objects.hashCode(this._costCenterText);
        hash = 23 * hash + (this._serviceDocHome ? 1 : 0);
        hash = 23 * hash + (this._serviceDocDKG ? 1 : 0);
        hash = 23 * hash + (this._serviceDocEBM ? 1 : 0);
        hash = 23 * hash + (this._serviceDocGOA ? 1 : 0);
        hash = 23 * hash + (this._serviceDocDif ? 1 : 0);
        hash = 23 * hash + Objects.hashCode(this._description);
        hash = 23 * hash + this._baseInformationId;
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListRadiologyLaboratory)) {
            return false;
        }
        final KGPListRadiologyLaboratory other = (KGPListRadiologyLaboratory) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._costCenterId != other._costCenterId) {
            return false;
        }
        if (this._costCenterNumber != other._costCenterNumber) {
            return false;
        }
        if (this._serviceDocHome != other._serviceDocHome) {
            return false;
        }
        if (this._serviceDocDKG != other._serviceDocDKG) {
            return false;
        }
        if (this._serviceDocEBM != other._serviceDocEBM) {
            return false;
        }
        if (this._serviceDocGOA != other._serviceDocGOA) {
            return false;
        }
        if (this._serviceDocDif != other._serviceDocDif) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (!Objects.equals(this._costCenterText, other._costCenterText)) {
            return false;
        }
        return Objects.equals(this._description, other._description);
    }
    
    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListRadiologyLaboratory[ rlID=" + _id + " ]";
    }
    //</editor-fold>
    
}
