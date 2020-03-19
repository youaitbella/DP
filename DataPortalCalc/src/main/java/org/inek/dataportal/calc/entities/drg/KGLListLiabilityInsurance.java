package org.inek.dataportal.calc.entities.drg;

import org.inek.dataportal.common.data.iface.BaseIdValue;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "KGLListLiabilityInsurance", schema = "calc")
@XmlRootElement
public class KGLListLiabilityInsurance implements Serializable, BaseIdValue {
    private static final long serialVersionUID = 1L;

    public KGLListLiabilityInsurance() {
    }

    public KGLListLiabilityInsurance(DrgCalcBasics calcBasics) {
        this.calcBasics = calcBasics;
    }

    // <editor-fold defaultstate="collapsed" desc="Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liID", updatable = false, nullable = false)
    private Integer _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterGroup">
    @Column(name = "liCostCenterGroup")
    private String costCenterGroup = "";

    public String getCostCenterGroup() {
        return costCenterGroup;
    }

    public void setCostCenterGroup(String costCenterGroup) {
        this.costCenterGroup = costCenterGroup;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="liCostCenterName">
    @Column(name = "liCostCenterName")
    private String costCenterName = "";

    public String getCostCenterName() {
        return costCenterName;
    }

    public void setCostCenterName(String costCenterName) {
        this.costCenterName = costCenterName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostKoArtGrp8">
    @Column(name = "liCostKoArtGrp8")
    private double costKoArtGrp8;

    public double getCostKoArtGrp8() {
        return costKoArtGrp8;
    }

    public void setCostKoArtGrp8(double costKoArtGrp8) {
        this.costKoArtGrp8 = costKoArtGrp8;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BaseInformation">
    @ManyToOne
    @JoinColumn(name = "liBaseInformationId")
    private DrgCalcBasics calcBasics;

    public DrgCalcBasics getDrgCalcBasics() {
        return calcBasics;
    }

    public int getBaseInformationId() {
        return calcBasics.getId();
    }

    public void setBaseInformationId(int dummy) {
    }
    // </editor-fold>

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
