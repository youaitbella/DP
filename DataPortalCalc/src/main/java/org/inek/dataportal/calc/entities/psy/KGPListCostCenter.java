/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.psy;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListCostCenter", schema = "calc")
public class KGPListCostCenter implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ccID", updatable = false, nullable = false)
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
    @Column(name = "ccCostCenterID")
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
    @Column(name = "ccCostCenterNumber")
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
    @Column(name = "ccCostCenterText")
    @Documentation(name = "Name der Kostenstelle", rank = 20)
    private String _costCenterText = "";

    @Size(max = 50)
    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _amount">
    @Column(name = "ccAmount")
    @Documentation(name = "Kostenvolumen", rank = 30)
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _fullVigorCnt">
    @Column(name = "ccFullVigorCnt")
    @Documentation(name = "Anzahl zugeordenter Vollkräfte…", rank = 40)
    private double _fullVigorCnt;

    public double getFullVigorCnt() {
        return _fullVigorCnt;
    }

    public void setFullVigorCnt(double fullVigorCnt) {
        this._fullVigorCnt = fullVigorCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceKey">
    @Column(name = "ccServiceKey")
    @Documentation(name = "Leistungsschlüssel", rank = 50)
    private String _serviceKey = "";

    @Size(max = 50)
    public String getServiceKey() {
        return _serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this._serviceKey = serviceKey;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceKeyDescription">
    @Column(name = "ccServiceKeyDescription")
    @Documentation(name = "Beschreibung Leistungsschlüssel", rank = 60)
    private String _serviceKeyDescription = "";

    public String getServiceKeyDescription() {
        return _serviceKeyDescription;
    }

    public void setServiceKeyDescription(String serviceKeyDescription) {
        this._serviceKeyDescription = serviceKeyDescription;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceSum">
    @Column(name = "ccServiceSum")
    @Documentation(name = "Summe Leistungseinheiten", rank = 70)
    private double _serviceSum;

    public double getServiceSum() {
        return _serviceSum;
    }

    public void setServiceSum(double serviceSum) {
        this._serviceSum = serviceSum;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
    @Column(name = "ccBaseInformationId")
    private int _baseInformationId = -1;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ccCountMedStaffPre">
    @Column(name = "ccCountMedStaffPre")
    private double _countMedStaffPre;

    public double getCountMedStaffPre() {
        return _countMedStaffAfter;
    }

    public void setCountMedStaffPre(double countMedStaffPre) {
        this._countMedStaffAfter = countMedStaffPre;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ccCountMedStaffAfter">
    @Column(name = "ccCountMedStaffAfter")
    private double _countMedStaffAfter;

    public double getCountMedStaffAfter() {
        return _countMedStaffAfter;
    }

    public void setCountMedStaffAfter(double countMedStaffAfter) {
        this._countMedStaffAfter = countMedStaffAfter;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ccCostVolumeMedStaffPre">
    @Column(name = "ccCostVolumeMedStaffPre")
    private double _costVolumeMedStaffPre;

    public double getCostVolumeMedStaffPre() {
        return _costVolumeMedStaffPre;
    }

    public void set_costVolumeMedStaffPre(double costVolumeMedStaffPre) {
        this._costVolumeMedStaffPre = costVolumeMedStaffPre;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ccCostVolumeMedStaffAfter">
    @Column(name = "ccCostVolumeMedStaffAfter")
    private double _costVolumeMedStaffAfter;

    public double getCostVolumeMedStaffAfter() {
        return _costVolumeMedStaffAfter;
    }

    public void setCostVolumeMedStaffAfter(double costVolumeMedStaffAfter) {
        this._costVolumeMedStaffAfter = costVolumeMedStaffAfter;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ccCountFunctionalServicePre">
    @Column(name = "ccCountFunctionalServicePre")
    private double _countFunctionalServicePre;

    public double getCountFunctionalServicePre() {
        return _countFunctionalServicePre;
    }

    public void setCountFunctionalServicePre(double countFunctionalServicePre) {
        this._countFunctionalServicePre = countFunctionalServicePre;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ccCountFunctionalServiceAfter">
    @Column(name = "ccCountFunctionalServiceAfter")
    private double _countFunctionalServiceAfter;

    public double getCountFunctionalServiceAfter() {
        return _countFunctionalServiceAfter;
    }

    public void setCountFunctionalServiceAfter(double countFunctionalServiceAfter) {
        this._countFunctionalServiceAfter = countFunctionalServiceAfter;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ccCostVolumeFunctionalServicePre">
    @Column(name = "ccCostVolumeFunctionalServicePre")
    private double _costVolumeFunctionalServicePre;

    public double getCostVolumeFunctionalServicePre() {
        return _costVolumeFunctionalServicePre;
    }

    public void setCostVolumeFunctionalServicePre(double costVolumeFunctionalServicePre) {
        this._costVolumeFunctionalServicePre = costVolumeFunctionalServicePre;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ccCostVolumeFunctionalServiceAfter">
    @Column(name = "ccCostVolumeFunctionalServiceAfter")
    private double _costVolumeFunctionalServiceAfter;

    public double getCostVolumeFunctionalServiceAfter() {
        return _costVolumeFunctionalServiceAfter;
    }

    public void setCostVolumeFunctionalServiceAfter(double costVolumeFunctionalServiceAfter) {
        this._costVolumeFunctionalServiceAfter = costVolumeFunctionalServiceAfter;
    }
    // </editor-fold>

    public KGPListCostCenter() {
    }

    public KGPListCostCenter(Integer ccID) {
        this._id = ccID;
    }

    public KGPListCostCenter(int baseInformationId, int costCenterId) {
        _baseInformationId = baseInformationId;
        _costCenterId = costCenterId;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 79 * hash + this._costCenterId;
        hash = 79 * hash +_costCenterNumber.hashCode();
        hash = 79 * hash + Objects.hashCode(this._costCenterText);
        hash = 79 * hash + this._amount;
        hash = 79 * hash + (int) (Double.doubleToLongBits(this._fullVigorCnt) ^ (Double.doubleToLongBits(this._fullVigorCnt) >>> 32));
        hash = 79 * hash + Objects.hashCode(this._serviceKey);
        hash = 79 * hash + Objects.hashCode(this._serviceKeyDescription);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this._serviceSum) ^ (Double.doubleToLongBits(this._serviceSum) >>> 32));
        hash = 79 * hash + this._baseInformationId;
        return hash;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListCostCenter)) {
            return false;
        }
        final KGPListCostCenter other = (KGPListCostCenter) obj;
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
        if (this._amount != other._amount) {
            return false;
        }
        if (Double.doubleToLongBits(this._fullVigorCnt) != Double.doubleToLongBits(other._fullVigorCnt)) {
            return false;
        }
        if (Double.doubleToLongBits(this._serviceSum) != Double.doubleToLongBits(other._serviceSum)) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (!Objects.equals(this._costCenterText, other._costCenterText)) {
            return false;
        }
        if (!Objects.equals(this._serviceKey, other._serviceKey)) {
            return false;
        }
        if (!Objects.equals(this._serviceKeyDescription, other._serviceKeyDescription)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListCostCenter[ ccID=" + _id + " ]";
    }
    //</editor-fold>

    void copyCostCenter(KGPListCostCenter item) {
        this._costCenterId = item._costCenterId;
        this._costCenterNumber = item._costCenterNumber;
        this._costCenterText = item._costCenterText;
        this._amount = item._amount;
        this._fullVigorCnt = item._fullVigorCnt;
        this._serviceKey = item._serviceKey;
        this._serviceKeyDescription = item._serviceKeyDescription;
        this._serviceSum = item._serviceSum;
        this._baseInformationId = item._baseInformationId;
    }

}
