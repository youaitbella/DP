package org.inek.dataportal.calc.entities.drg;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "KGLListLiabilityInsurance", schema = "calc")
@XmlRootElement
public class KGLListLiabilityInsurance implements Serializable {
    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liID", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    /*
    // <editor-fold defaultstate="collapsed" desc="CostVolume">
    @Column(name = "liCostVolume")
    private double CostVolume;

    public double getCostVolume() {
        return CostVolume;
    }

    public void setCostVolume(double costVolume) {
        CostVolume = costVolume;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="HasTotalCostILBV">
    @Column(name = "liTotalCostILBV")
    private boolean HasTotalCostILBV;

    public boolean isHasTotalCostILBV() {
        return HasTotalCostILBV;
    }

    public void setHasTotalCostILBV(boolean hasTotalCostILBV) {
        HasTotalCostILBV = hasTotalCostILBV;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="HasCostCalculation2020">
    private boolean HasCostCalculation2020;

    public boolean isHasCostCalculation2020() {
        return HasCostCalculation2020;
    }

    public void setHasCostCalculation2020(boolean hasCostCalculation2020) {
        HasCostCalculation2020 = hasCostCalculation2020;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="liHasDistributionOfPremiumInfo">
    private boolean HasDistributionOfPremiumInfo;

    public boolean isHasDistributionOfPremiumInfo() {
        return HasDistributionOfPremiumInfo;
    }

    public void setHasDistributionOfPremiumInfo(boolean hasDistributionOfPremiumInfo) {
        HasDistributionOfPremiumInfo = hasDistributionOfPremiumInfo;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="liExplanationField">
    private String ExplanationField;

    public String getExplanationField() {
        return ExplanationField;
    }

    public void setExplanationField(String explanationField) {
        ExplanationField = explanationField;
    }
    // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="RemainingCosts">
    private double RemainingCosts;

    public double getRemainingCosts() {
        return RemainingCosts;
    }

    public void setRemainingCosts(double remainingCosts) {
        RemainingCosts = remainingCosts;
    }
    // </editor-fold>
    */

    // <editor-fold defaultstate="collapsed" desc="CostCenterGroup">
    private String CostCenterGroup;

    public String getCostCenterGroup() {
        return CostCenterGroup;
    }

    public void setCostCenterGroup(String costCenterGroup) {
        CostCenterGroup = costCenterGroup;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="liCostCenterName">
    private String CostCenterName;

    public String getCostCenterName() {
        return CostCenterName;
    }

    public void setCostCenterName(String costCenterName) {
        CostCenterName = costCenterName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostKoArtGrp8">
    private double CostKoArtGrp8;

    public double getCostKoArtGrp8() {
        return CostKoArtGrp8;
    }

    public void setCostKoArtGrp8(double costKoArtGrp8) {
        CostKoArtGrp8 = costKoArtGrp8;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="baseInformationId">
    @Column(name = "liBaseInformationId")
    private int baseInformationId;

    public int getBaseInformationId() {
        return baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this.baseInformationId = baseInformationId;
    }
    // </editor-fold>

    public KGLListLiabilityInsurance() {
    }

    public KGLListLiabilityInsurance(int baseInformationId) {
        this.baseInformationId = baseInformationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KGLListLiabilityInsurance)) return false;
        KGLListLiabilityInsurance that = (KGLListLiabilityInsurance) o;
        return _id == that._id &&
                Double.compare(that.getCostKoArtGrp8(), getCostKoArtGrp8()) == 0 &&
                getBaseInformationId() == that.getBaseInformationId() &&
                Objects.equals(getCostCenterGroup(), that.getCostCenterGroup()) &&
                Objects.equals(getCostCenterName(), that.getCostCenterName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, getCostCenterGroup(), getCostCenterName(), getCostKoArtGrp8(), getBaseInformationId());
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListLiabilityInsurance[liId" +
                _id + ']';
    }
}
