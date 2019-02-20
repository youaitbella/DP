package org.inek.dataportal.calc.entities.drg;

import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "KGLListCostCenter", schema = "calc")
@SuppressWarnings("JavaNCSS")
public class KGLListCostCenter implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    public KGLListCostCenter() {
    }

    public KGLListCostCenter(Integer ccID) {
        this._id = ccID;
    }

    public KGLListCostCenter(int baseInformationId, int costCenterId) {
        _baseInformationId = baseInformationId;
        _costCenterId = costCenterId;
    }

    // <editor-fold defaultstate="collapsed" desc="id">
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterId">
    @Column(name = "ccCostCenterID")
    @Documentation(name = "Kostenstelle", rank = 10)
    private int _costCenterId;

    public int getCostCenterId() {
        return _costCenterId;
    }

    public void setCostCenterId(int costCenterId) {
        this._costCenterId = costCenterId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterNumber">
    @Column(name = "ccCostCenterNumber")
    @Documentation(name = "Nummer:", rank = 10)
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
    @Column(name = "ccCostCenterText")
    @Documentation(name = "Name der Kostenstelle", rank = 20)
    private String _costCenterText = "";

    @Size(max = 200, message = "Für Bezeichnung sind max. {max} Zeichen zulässig.")
    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Amount">
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ccAmount")
    @Documentation(name = "Kostenvolumen", rank = 40)
    private int _amount;

    @Min(0)
    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="FullVigorCnt">
    @Column(name = "ccFullVigorCnt")
    @Documentation(name = "Anzahl zugeordenter Vollkräfte…", rank = 60)
    private double _fullVigorCnt;

    @Min(0)
    public double getFullVigorCnt() {
        return _fullVigorCnt;
    }

    public void setFullVigorCnt(double fullVigorCnt) {
        this._fullVigorCnt = fullVigorCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceKey">
    @Column(name = "ccServiceKey")
    @Documentation(name = "Leistungsschlüssel", rank = 30)
    private String _serviceKey = "";

    @Size(max = 100, message = "Für Leistungsschlüssel sind max. {max} Zeichen zulässig.")
    public String getServiceKey() {
        return _serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this._serviceKey = serviceKey;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceKeyDescription">
    @Column(name = "ccServiceKeyDescription")
    @Documentation(name = "Beschreibung Schlüssel", rank = 50)
    private String _serviceKeyDescription = "";

    public String getServiceKeyDescription() {
        return _serviceKeyDescription;
    }

    public void setServiceKeyDescription(String serviceKeyDescription) {
        this._serviceKeyDescription = serviceKeyDescription;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceSum">
    @Column(name = "ccServiceSum")
    @Documentation(name = "Summe Leistungsschlüssel", rank = 70)
    private double _serviceSum;

    public double getServiceSum() {
        return _serviceSum;
    }

    public void setServiceSum(double serviceSum) {
        this._serviceSum = serviceSum;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BaseInformation">
//    @JoinColumn(name = "ccBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "ccBaseInformationId")
    //  @Documentation (name = "ccBaseInformationId", rank = 80)
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ccCountMedStaffPre">
    @Column(name = "ccCountMedStaffPre")
    private double _countMedStaffPre;

    public double getCountMedStaffPre() {
        return _countMedStaffPre;
    }

    public void setCountMedStaffPre(double countMedStaffPre) {
        this._countMedStaffPre = countMedStaffPre;
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

    public void setCostVolumeMedStaffPre(double costVolumeMedStaffPre) {
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

    // <editor-fold defaultstate="collapsed" desc="ccCountCareServicePre">
    @Column(name = "ccCountCareServicePre")
    private double _countCareServicePre;

    public double getCountCareServicePre() {
        return _countCareServicePre;
    }

    public void setCountCareServicePre(double countCareServicePre) {
        this._countCareServicePre = countCareServicePre;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ccCountCareServiceAfter">
    @Column(name = "ccCountCareServiceAfter")
    private double _countCareServiceAfter;

    public double getCountCareServiceAfter() {
        return _countCareServiceAfter;
    }

    public void setCountCareServiceAfter(double countCareServiceAfter) {
        this._countCareServiceAfter = countCareServiceAfter;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ccCostVolumeCareServicePre">
    @Column(name = "ccCostVolumeCareServicePre")
    private double _costVolumeCareServicePre;

    public double getCostVolumeCareServicePre() {
        return _costVolumeCareServicePre;
    }

    public void setCostVolumeCareServicePre(double costVolumeCareServicePre) {
        this._costVolumeCareServicePre = costVolumeCareServicePre;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ccCostVolumeCareServiceAfter">
    @Column(name = "ccCostVolumeCareServiceAfter")
    private double _costVolumeCareServiceAfter;

    public double getCostVolumeCareServiceAfter() {
        return _costVolumeCareServiceAfter;
    }

    public void setCostVolumeCareServiceAfter(double costVolumeCareServiceAfter) {
        this._costVolumeCareServiceAfter = costVolumeCareServiceAfter;
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
    }    // </editor-fold>

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

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        return 2441;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KGLListCostCenter that = (KGLListCostCenter) o;

        if (_id != that._id) return false;
        if (_id > 0 && _id == that._id) return true;
        if (_costCenterId != that._costCenterId) return false;
        if (_amount != that._amount) return false;
        if (Double.compare(that._fullVigorCnt, _fullVigorCnt) != 0) return false;
        if (Double.compare(that._serviceSum, _serviceSum) != 0) return false;
        if (_baseInformationId != that._baseInformationId) return false;
        if (Double.compare(that._countMedStaffPre, _countMedStaffPre) != 0) return false;
        if (Double.compare(that._countMedStaffAfter, _countMedStaffAfter) != 0) return false;
        if (Double.compare(that._costVolumeMedStaffPre, _costVolumeMedStaffPre) != 0) return false;
        if (Double.compare(that._costVolumeMedStaffAfter, _costVolumeMedStaffAfter) != 0) return false;
        if (Double.compare(that._countCareServicePre, _countCareServicePre) != 0) return false;
        if (Double.compare(that._countCareServiceAfter, _countCareServiceAfter) != 0) return false;
        if (Double.compare(that._costVolumeCareServicePre, _costVolumeCareServicePre) != 0) return false;
        if (Double.compare(that._costVolumeCareServiceAfter, _costVolumeCareServiceAfter) != 0) return false;
        if (Double.compare(that._countFunctionalServicePre, _countFunctionalServicePre) != 0) return false;
        if (Double.compare(that._countFunctionalServiceAfter, _countFunctionalServiceAfter) != 0) return false;
        if (Double.compare(that._costVolumeFunctionalServicePre, _costVolumeFunctionalServicePre) != 0) return false;
        if (Double.compare(that._costVolumeFunctionalServiceAfter, _costVolumeFunctionalServiceAfter) != 0)
            return false;
        if (!_costCenterNumber.equals(that._costCenterNumber)) return false;
        if (!_costCenterText.equals(that._costCenterText)) return false;
        if (!_serviceKey.equals(that._serviceKey)) return false;
        return _serviceKeyDescription.equals(that._serviceKeyDescription);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListCostCenter[ ccID=" + _id + " ]";
    }
    //</editor-fold>

    void copyCostCenter(KGLListCostCenter item) {
        this._costCenterId = item._costCenterId;
        this._costCenterNumber = item._costCenterNumber;
        this._costCenterText = item._costCenterText;
        this._amount = item._amount;
        this._fullVigorCnt = item._fullVigorCnt;
        this._serviceKey = item._serviceKey;
        this._serviceKeyDescription = item._serviceKeyDescription;
        this._serviceSum = item._serviceSum;
        this._baseInformationId = item._baseInformationId;
        this._countMedStaffPre = item._countMedStaffPre;
        this._countMedStaffAfter = item._countMedStaffAfter;
        this._costVolumeMedStaffPre = item._costVolumeMedStaffPre;
        this._costVolumeMedStaffAfter = item._costVolumeMedStaffAfter;
        this._countCareServicePre = item._countCareServicePre;
        this._countCareServiceAfter = item._countCareServiceAfter;
        this._costVolumeCareServicePre = item._costVolumeCareServicePre;
        this._costVolumeCareServiceAfter = item._costVolumeCareServiceAfter;
        this._countFunctionalServicePre = item._countFunctionalServicePre;
        this._countFunctionalServiceAfter = item._countFunctionalServiceAfter;
        this._costVolumeFunctionalServicePre = item._costVolumeFunctionalServicePre;
        this._costVolumeFunctionalServiceAfter = item._costVolumeFunctionalServiceAfter;
    }

}
