package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.inek.dataportal.utils.Documentation;

@Entity
@Table(name = "Cost", schema = "mvh")
public class Cost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coId")
    private Integer _id;

    @Column(name = "coModelIntentionId")
    private Integer _modelIntentionId;

    // <editor-fold defaultstate="collapsed" desc="IK">
    @Column(name = "coIk")
    @Documentation(key = "lblIK")
    private int _ik = -1;

    public Integer getIk() {
        return _ik < 0 ? null : _ik;
    }

    public void setIk(Integer ik) {
        _ik = ik == null ? -1 : ik;
    }
    // </editor-fold>

    @Column(name = "coRemunerationKey")
    @Documentation(key = "lblRemunerationCode")
    private String _remunerationCode = "";

    @Column(name = "coCostCenterId")
    
    @Documentation(key = "lblCostCenter", translateValue = "1=enmCostCenter1;2=enmCostCenter2;3=enmCostCenter3"
            + ";4=enmCostCenter4;5=enmCostCenter5;6=enmCostCenter6;7=enmCostCenter7;8=enmCostCenter8"
            + ";9=enmCostCenter9;10=enmCostCenter10;11=enmCostCenter11;12=enmCostCenter21;13=enmCostCenter22"
            + ";14=enmCostCenter23;15=enmCostCenter24;16=enmCostCenter25;17=enmCostCenter26;18=enmCostCenter30"
            + ";19=enmCostCenter40;20=enmCostCenter99")
    private int _costCenterId = -1;

    @Column(name = "coCostTypeId")
    
    @Documentation(key = "lblCostType", translateValue = "0=enmCostType1;100=enmCostType2;110=enmCostType3;120=enmCostType4"
            + ";130=enmCostType5;131=enmCostType6;132=enmCostType7;133=enmCostType8;141=enmCostType9;142=enmCostType10"
            + ";150=enmCostType11;161=enmCostType12;162=enmCostType13;170=enmCostType14;180=enmCostType15")
    private int _costTypeId = -1;

    @Column(name = "coAmount")
    @Documentation(key = "lblCostAmount", isMoneyFormat = true)
    private BigDecimal _amount = new BigDecimal(0d);

    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getModelIntentionId() {
        return _modelIntentionId;
    }

    public void setModelIntentionId(Integer modelIntentionId) {
        _modelIntentionId = modelIntentionId;
    }

    public String getRemunerationCode() {
        return _remunerationCode;
    }

    public void setRemunerationCode(String remunerationCode) {
        _remunerationCode = remunerationCode;
    }

    public int getCostCenterId() {
        return _costCenterId;
    }

    public void setCostCenterId(int costCenterId) {
        _costCenterId = costCenterId;
    }

    public int getCostTypeId() {
        return _costTypeId;
    }

    public void setCostTypeId(int costTypeId) {
        _costTypeId = costTypeId;
    }

    public BigDecimal getAmount() {
        return _amount;
    }

    public void setAmount(BigDecimal amount) {
        _amount = amount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cost)) {
            return false;
        }
        Cost other = (Cost) object;
        return Objects.equals(_id, other._id)
                && (_id != null
                || _ik == other._ik
                && this._remunerationCode.equals(other._remunerationCode)
                && this._costCenterId == other._costCenterId
                && this._costTypeId == other._costTypeId);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this._id);
        if (_id == null) {
            hash = 67 * hash + this._ik;
            hash = 67 * hash + Objects.hashCode(this._remunerationCode);
            hash = 67 * hash + this._costCenterId;
            hash = 67 * hash + this._costTypeId;
        }
        return hash;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + _id + " ]";
    }

    // </editor-fold>
    /**
     * Two cost are functional equal, if they define the same module. This check
     * is independent from id and amount!
     *
     * @param other
     * @return
     */
    public boolean equalsFunctional(Cost other) {
        if (other == null) {
            return false;
        }
        return this._ik == other._ik
                && this._remunerationCode.equals(other._remunerationCode)
                && this._costCenterId == other._costCenterId
                && this._costTypeId == other._costTypeId;
    }

}
