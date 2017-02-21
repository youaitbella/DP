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

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListRadiologyLaboratory", schema = "calc")
@XmlRootElement
public class KGLListRadiologyLaboratory implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rlID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int rlId) {
        this._id = rlId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BaseInformationID">
//    @JoinColumn(name = "rlBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "rlBaseInformationID")
    private int _baseInformationID;

    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int rlBaseInformationID) {
        this._baseInformationID = rlBaseInformationID;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="costCenterID">
    @Column(name = "rlCostCenterID")
    private int _costCenterID;

    public int getCostCenterID() {
        return _costCenterID;
    }

    public void setCostCenterID(int rlCostCenterID) {
        this._costCenterID = rlCostCenterID;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="costCenterNumber">
    @Column(name = "rlCostCenterNumber")
    private int _costCenterNumber;

    public int getCostCenterNumber() {
        return _costCenterNumber;
    }

    public void setCostCenterNumber(int rlCostCenterNumber) {
        this._costCenterNumber = rlCostCenterNumber;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterText">
    @Size(max = 200)
    @Column(name = "rlCostCenterText")
    private String _costCenterText = "";

    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String rlCostCenterText) {
        this._costCenterText = rlCostCenterText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="serviceDocHome">
    @Column(name = "rlServiceDocHome")
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
    private boolean _serviceDocDif;

    public boolean isServiceDocDif() {
        return _serviceDocDif;
    }

    public void setServiceDocDif(boolean serviceDocDif) {
        this._serviceDocDif = serviceDocDif;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Description">
    @Size(max = 2147483647)
    @Column(name = "rlDescription")
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

    /**
     * copy all values but id and baseInformationId will default to -1
     *
     * @param source value to copy
     */
    public KGLListRadiologyLaboratory(KGLListRadiologyLaboratory source) {
        _id = -1;
        _baseInformationID = -1;
        this._costCenterID = source._costCenterID;
        this._costCenterNumber = source._costCenterNumber;
        this._serviceDocHome = source._serviceDocHome;
        this._serviceDocDKG = source._serviceDocDKG;
        this._serviceDocEBM = source._serviceDocEBM;
        this._serviceDocGOA = source._serviceDocGOA;
        this._serviceDocDif = source._serviceDocDif;
        this._serviceVolumePre = source._serviceVolumePre;
        this._amountPre = source._amountPre;
        this._serviceVolumePost = source._serviceVolumePost;
        this._amountPost = source._amountPost;
        this._costCenterText = source._costCenterText;
        this._description = source._description;
    }

    public KGLListRadiologyLaboratory(int rlID) {
        this._id = rlID;
    }

    public KGLListRadiologyLaboratory(int id, int baseInformationID, int costTypeID, int costCenterID, boolean serviceDocHome, boolean serviceDocDKG, boolean serviceDocEBM, boolean serviceDocGOA, boolean serviceDocDif, int serviceVolumePre, int amountPre, int serviceVolumePost, int amountPost) {
        this._id = id;
        this._baseInformationID = baseInformationID;
        this._costCenterID = costTypeID;
        this._costCenterNumber = costCenterID;
        this._serviceDocHome = serviceDocHome;
        this._serviceDocDKG = serviceDocDKG;
        this._serviceDocEBM = serviceDocEBM;
        this._serviceDocGOA = serviceDocGOA;
        this._serviceDocDif = serviceDocDif;
        this._serviceVolumePre = serviceVolumePre;
        this._amountPre = amountPre;
        this._serviceVolumePost = serviceVolumePost;
        this._amountPost = amountPost;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this._id;

        if (this._id != -1) {
            return hash;
        }

        hash = 43 * hash + this._baseInformationID;
        hash = 43 * hash + this._costCenterID;
        hash = 43 * hash + this._costCenterNumber;
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
        if (this._baseInformationID != other._baseInformationID) {
            return false;
        }
        if (this._costCenterID != other._costCenterID) {
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
