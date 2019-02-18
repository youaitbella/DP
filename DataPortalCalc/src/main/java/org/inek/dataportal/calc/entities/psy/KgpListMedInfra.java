/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.psy;

import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KgpListMedInfra", schema = "calc")
public class KgpListMedInfra implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @Column(name = "miID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    //<editor-fold defaultstate="collapsed" desc="Property _costTypeId">
    @Column(name = "miCostTypeID")
  //  @Documentation(name = "Kostenstellenart", rank = 10)
    private int _costTypeId;

    public int getCostTypeId() {
        return _costTypeId;
    }

    public void setCostTypeId(int costTypeId) {
        this._costTypeId = costTypeId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costCenterNumber">
    @Column(name = "miCostCenterNumber")
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
    @Column(name = "miCostCenterText")
    @Documentation(name = "Name der Kostenstelle", rank = 30)
    private String _costCenterText = "";

    @Size(max = 100)
    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _keyUsed">
    @Column(name = "miKeyUsed")
    @Documentation(name = "Verwendeter Schlüssel", rank = 40)
    private String _keyUsed = "";

    @Size(max = 50)
    public String getKeyUsed() {
        return _keyUsed;
    }

    public void setKeyUsed(String keyUsed) {
        this._keyUsed = keyUsed;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _amount">
    @Column(name = "miAmount")
    @Documentation(name = "Kostenvolumen vor Durchführung der IBLV und vor Abgrenzung nicht kalkulationsrelevanter Kosten", rank = 50)
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "miBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "miBaseInformationId")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    public KgpListMedInfra() {
    }

    public KgpListMedInfra(int baseInformationId, int costTypeId) {
        _baseInformationId = baseInformationId;
        _costTypeId = costTypeId;
    }

    // <editor-fold defaultstate="collapsed" desc="miPartCostVolumeMedStaffAfter">
    @Column(name = "miPartCostVolumeMedStaffAfter")
    private double _partCostVolumeMedStaffAfter;

    public double getPartCostVolumeMedStaffAfter() {
        return _partCostVolumeMedStaffAfter;
    }

    public void setPartCostVolumeMedStaffAfter(double partCostVolumeMedStaffAfter) {
        this._partCostVolumeMedStaffAfter = partCostVolumeMedStaffAfter;
    }
    // </editor-fold>

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
        hash = 97 * hash
                + (int) (Double.doubleToLongBits(this._partCostVolumeMedStaffAfter)
                ^ (Double.doubleToLongBits(this._partCostVolumeMedStaffAfter) >>> 32));
        return hash;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KgpListMedInfra)) {
            return false;
        }
        final KgpListMedInfra other = (KgpListMedInfra) obj;
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

        if (Double.doubleToLongBits(this._partCostVolumeMedStaffAfter) != Double.doubleToLongBits(other._partCostVolumeMedStaffAfter)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KgpListMedInfra[ miID=" + _id + " ]";
    }
    //</editor-fold>

}
