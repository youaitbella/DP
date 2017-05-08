/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc.psy;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import org.inek.dataportal.entities.iface.BaseIdValue;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListRadiologyLaboratory", schema = "calc")
public class KGPListRadiologyLaboratory implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rlID", updatable = false, nullable = false)
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

    //<editor-fold defaultstate="collapsed" desc="Property _costCenterId">
    @Column(name = "rlCostCenterID")
    @Documentation(name = "Kostenstelle", rank = 10)
    private int _costCenterId;

    public int getCostCenterId() {
        return _costCenterId;
    }

    public void setCostCenterId(int costCenterId) {
        this._costCenterId = costCenterId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costCenterNumber">
    @Column(name = "rlCostCenterNumber")
    @Documentation(name = "Nummer der Kostenstelle", rank = 20)
    private String _costCenterNumber = "";

    @Size(max = 20)
    public String getCostCenterNumber() {
        return _costCenterNumber;
    }

    public void setCostCenterNumber(String costCenterNumber) {
        this._costCenterNumber = costCenterNumber;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costCenterText">
    @Column(name = "rlCostCenterText")
    @Documentation(name = "Name Kostenstelle", rank = 30)
    private String _costCenterText = "";

    @Size(max = 100)
    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceDocHome">
    @Column(name = "rlServiceDocHome")
    @Documentation(name = "Leistungsdokumentation Hauskatalog", rank = 40)
    private boolean _serviceDocHome;

    public boolean isServiceDocHome() {
        return _serviceDocHome;
    }

    public void setServiceDocHome(boolean serviceDocHome) {
        this._serviceDocHome = serviceDocHome;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceDocDKG">
    @Column(name = "rlServiceDocDKG")
    @Documentation(name = "Leistungsdokumentation DKG-NT", rank = 50)
    private boolean _serviceDocDKG;

    public boolean isServiceDocDKG() {
        return _serviceDocDKG;
    }

    public void setServiceDocDKG(boolean serviceDocDKG) {
        this._serviceDocDKG = serviceDocDKG;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceDocEBM">
    @Column(name = "rlServiceDocEBM")
    @Documentation(name = "Leistungsdokumentation EBM", rank = 60)
    private boolean _serviceDocEBM;

    public boolean isServiceDocEBM() {
        return _serviceDocEBM;
    }

    public void setServiceDocEBM(boolean serviceDocEBM) {
        this._serviceDocEBM = serviceDocEBM;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceDocGOA">
    @Column(name = "rlServiceDocGOA")
    @Documentation(name = "Leistungsdokumentation GOÄ", rank = 70)
    private boolean _serviceDocGOA;

    public boolean isServiceDocGOA() {
        return _serviceDocGOA;
    }

    public void setServiceDocGOA(boolean serviceDocGOA) {
        this._serviceDocGOA = serviceDocGOA;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceDocDif">
    @Column(name = "rlServiceDocDif")
    @Documentation(name = "Leistungsdokumentation sonstige", rank = 80)
    private boolean _serviceDocDif;

    public boolean isServiceDocDif() {
        return _serviceDocDif;
    }

    public void setServiceDocDif(boolean serviceDocDif) {
        this._serviceDocDif = serviceDocDif;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceDocType">
    @Transient
    private int _serviceDocType;

    public int getServiceDocType() {
        int result = 0;
        if (_serviceDocHome) {
            return 1;
        }
        if (_serviceDocDKG) {
            return 2;
        }
        if (_serviceDocEBM) {
            return 3;
        }
        if (_serviceDocGOA) {
            return 4;
        }
        if (_serviceDocDif) {
            return 5;
        }

        return result;
    }

    public void setServiceDocType(int type) {
        _serviceDocHome = _serviceDocDKG = _serviceDocEBM = _serviceDocGOA = _serviceDocDif = false;
        switch (type) {
            case 0:
                break;
            case 1:
                _serviceDocHome = true;
                break;
            case 2:
                _serviceDocDKG = true;
                break;
            case 3:
                _serviceDocEBM = true;
                break;
            case 4:
                _serviceDocGOA = true;
                break;
            case 5:
                _serviceDocDif = true;
                break;
            default:
        }

        this._serviceDocType = type;
    }

    public String getServiceDocTypeAsString() {
        switch (getServiceDocType()) {
            case 0: return "fehlerhafte Angabe";
            case 1: return "Hauskatalog";
            case 2: return "DKG-NT";
            case 3: return "EBM";
            case 4: return "GOÄ";
            case 5: return "sonstiges";
            default: throw new IllegalArgumentException("unknown or unhandled service doc type " + getServiceDocType());
        }
    }

    public void setServiceDocTypeFromString(String value) {
        int type = 0;
        switch (value.trim().toLowerCase()) {
            case "hauskatalog": type = 1; break;
            case "dkg_nt": type = 2; break;
            case "ebm": type = 3; break;
            case "goä": type = 4; break;
            case "sonstiges": type = 5; break;
            default: type = 0;
        }
        setServiceDocType(type);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _description">
    @Column(name = "rlDescription")
    @Documentation(name = "Beschreibung", rank = 90)
    private String _description = "";

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "rlBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "rlBaseInformationId")
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

    public KGPListRadiologyLaboratory() {
    }

    public KGPListRadiologyLaboratory(int baseInformationId) {
        _baseInformationId = baseInformationId;
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
        hash = 23 * hash + this._costCenterNumber.hashCode();
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
