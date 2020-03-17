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
    private Integer _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    @Column(name = "liCostCenterGroup")
    // <editor-fold defaultstate="collapsed" desc="CostCenterGroup">
    private String CostCenterGroup="";

    public String getCostCenterGroup() {
        return CostCenterGroup;
    }

    public void setCostCenterGroup(String costCenterGroup) {
        CostCenterGroup = costCenterGroup;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="liCostCenterName">
    @Column(name = "liCostCenterName")
    private String CostCenterName="";

    public String getCostCenterName() {
        return CostCenterName;
    }

    public void setCostCenterName(String costCenterName) {
        CostCenterName = costCenterName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostKoArtGrp8">
    @Column(name = "liCostKoArtGrp8")
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
