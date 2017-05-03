/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc.drg;

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
@Table(name = "KGLListRadiologyLaboratory", schema = "calc")
@XmlRootElement
public class KGLListRadiologyLaboratory implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rlID", updatable = false, nullable = false)
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int rlId) {
        this._id = rlId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BaseInformationId">
//    @JoinColumn(name = "rlBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "rlBaseInformationId")
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int rlBaseInformationId) {
        this._baseInformationId = rlBaseInformationId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="costCenterID">
    @Column(name = "rlCostCenterID")
    @Documentation (name = "Kostenstelle", rank = 10)
    private int _costCenterId;

    public int getCostCenterId() {
        return _costCenterId;
    }

    public void setCostCenterId(int rlCostCenterID) {
        this._costCenterId = rlCostCenterID;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="costCenterNumber">
    @Column(name = "rlCostCenterNumber")
    @Documentation (name = "Kostenstellen Nummer:", rank = 10)
    private String _costCenterNumber = "";

    @Size(max = 20)
    public String getCostCenterNumber() {
        return _costCenterNumber;
    }

    public void setCostCenterNumber(String costCenterNumber) {
        this._costCenterNumber = costCenterNumber;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterText">
    @Column(name = "rlCostCenterText")
    @Documentation (name = "Name Kostenstelle", rank = 20)
    private String _costCenterText = "";

    @Size(max = 200)
    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String rlCostCenterText) {
        this._costCenterText = rlCostCenterText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="serviceDocHome">
    @Column(name = "rlServiceDocHome")
    @Documentation (name = "Leistungsdokumentation Hauskatalog", rank = 30)
    private boolean _serviceDocHome;

    public boolean isServiceDocHome() {
        return _serviceDocHome;
    }

    public void setServiceDocHome(boolean serviceDocHome) {
        this._serviceDocHome = serviceDocHome;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="serviceDocDKG">
    @Column(name = "rlServiceDocDKG")
    @Documentation (name = "Leistungsdokumentation DKG-NT", rank = 40)
    private boolean _serviceDocDKG;

    public boolean isServiceDocDKG() {
        return _serviceDocDKG;
    }

    public void setServiceDocDKG(boolean serviceDocDKG) {
        this._serviceDocDKG = serviceDocDKG;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="serviceDocEBM">
    @Column(name = "rlServiceDocEBM")
    @Documentation (name = "Leistungsdokumentation EBM", rank = 50)
    private boolean _serviceDocEBM;

    public boolean isServiceDocEBM() {
        return _serviceDocEBM;
    }

    public void setServiceDocEBM(boolean serviceDocEBM) {
        this._serviceDocEBM = serviceDocEBM;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="serviceDocGOA">
    @Column(name = "rlServiceDocGOA")
    @Documentation (name = "Leistungsdokumentation GOÄ", rank = 60)
    private boolean _serviceDocGOA;

    public boolean isServiceDocGOA() {
        return _serviceDocGOA;
    }

    public void setServiceDocGOA(boolean serviceDocGOA) {
        this._serviceDocGOA = serviceDocGOA;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="serviceDocDif">
    @Column(name = "rlServiceDocDif")
    @Documentation (name = "Leistungsdokumentation sonstige", rank = 70)
    private boolean _serviceDocDif;

    public boolean isServiceDocDif() {
        return _serviceDocDif;
    }

    public void setServiceDocDif(boolean serviceDocDif) {
        this._serviceDocDif = serviceDocDif;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Description">
    @Column(name = "rlDescription")
    @Documentation (name = "Beschreibung", rank = 80)
    private String _description = "";

    public String getDescription() {
        return _description;
    }

    public void setDescription(String rlDescription) {
        this._description = rlDescription;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceVolumePre">
    @Column(name = "rlServiceVolumePre")
    @Documentation (name = "Leistungsvolumen vor Abgrenzung", rank = 90)
    private int _serviceVolumePre;

    public int getServiceVolumePre() {
        return _serviceVolumePre;
    }

    public void setServiceVolumePre(int rlServiceVolumePre) {
        this._serviceVolumePre = rlServiceVolumePre;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AmountPre">
    @Column(name = "rlAmountPre")
    @Documentation (name = "Kostenvolumen vor Abgrenzung", rank = 100)
    private int _amountPre;

    public int getAmountPre() {
        return _amountPre;
    }

    public void setAmountPre(int rlAmountPre) {
        this._amountPre = rlAmountPre;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceVolumePost">
    @Column(name = "rlServiceVolumePost")
    @Documentation (name = "Leistungsvolumen nach Abgrenzung", rank = 110)
    private int _serviceVolumePost;

    public int getServiceVolumePost() {
        return _serviceVolumePost;
    }

    public void setServiceVolumePost(int rlServiceVolumePost) {
        this._serviceVolumePost = rlServiceVolumePost;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AmountPost">
    @Column(name = "rlAmountPost")
    @Documentation (name = "Kostenvolumen nach Abgrenzung", rank = 120)
    private int _amountPost;

    public int getAmountPost() {
        return _amountPost;
    }

    public void setAmountPost(int rlAmountPost) {
        this._amountPost = rlAmountPost;
    }
    // </editor-fold>

    public KGLListRadiologyLaboratory() {
    }

    public KGLListRadiologyLaboratory(int baseInformationID, int costCenterId) {
        _baseInformationId = baseInformationID;
        _costCenterId = costCenterId;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this._id;

        if (this._id != -1) {
            return hash;
        }

        hash = 43 * hash + this._baseInformationId;
        hash = 43 * hash + this._costCenterId;
        hash = 43 * hash + _costCenterNumber.hashCode();
        hash = 43 * hash + Objects.hashCode(this._costCenterText);
        hash = 43 * hash + (this._serviceDocHome ? 1 : 0);
        hash = 43 * hash + (this._serviceDocDKG ? 1 : 0);
        hash = 43 * hash + (this._serviceDocEBM ? 1 : 0);
        hash = 43 * hash + (this._serviceDocGOA ? 1 : 0);
        hash = 43 * hash + (this._serviceDocDif ? 1 : 0);
        hash = 43 * hash + Objects.hashCode(this._description);
        hash = 43 * hash + this._serviceVolumePre;
        hash = 43 * hash + this._amountPre;
        hash = 43 * hash + this._serviceVolumePost;
        hash = 43 * hash + this._amountPost;
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
        final KGLListRadiologyLaboratory other = (KGLListRadiologyLaboratory) obj;

        if (this._id != -1 && this._id == other._id) {
            return true;
        }

        if (this._id != other._id) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
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
        if (this._serviceVolumePre != other._serviceVolumePre) {
            return false;
        }
        if (this._amountPre != other._amountPre) {
            return false;
        }
        if (this._serviceVolumePost != other._serviceVolumePost) {
            return false;
        }
        if (this._amountPost != other._amountPost) {
            return false;
        }
        if (!Objects.equals(this._costCenterText, other._costCenterText)) {
            return false;
        }
        if (!Objects.equals(this._description, other._description)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListRadiologyLaboratory[ rlID=" + _id + " ]";
    }
    //</editor-fold>

}
