package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import java.math.BigDecimal;
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
    @Column (name = "coId")
    private Integer _id;
    
    @Column (name = "coModelIntentionId")
    private Integer _modelIntentionId;
    
    // <editor-fold defaultstate="collapsed" desc="IK">
    @Column (name = "coIk")
    @Documentation(key = "lblIK")
    private int _ik = -1;

    public Integer getIk() {
        return _ik < 0 ? null : _ik;
    }

    public void setIk(Integer ik) {
        _ik = ik == null ? -1 : ik;
    }
    // </editor-fold>
    
    @Column (name = "coRemunerationKey")
    @Documentation(key = "lblRemunerationCode")
    private String _remunerationCode = "";
    
    @Column (name = "coCostCenterId")
    @Documentation(key = "lblCostCenter")
    private int _costCenterId = -1;
    
    @Column (name = "coCostTypeId")
    @Documentation(key = "lblCostType")
    private int _costTypeId = -1;
    
    @Column (name = "coAmount")
    @Documentation(key = "lblCostAmount")
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
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cost)) {
            return false;
        }
        Cost other = (Cost) object;
        if ((_id == null && other.getId()!= null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return this._ik == other._ik
                && this._remunerationCode.equals(other._remunerationCode)
                && this._costCenterId == other._costCenterId
                && this._costTypeId == other._costTypeId
                && this._amount.equals(other._amount);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + _id + " ]";
    }

    // </editor-fold>
    
    /**
     * Two cost are functional equal, if they define the same module.
     * This check is independent from id and amount!
     * @param other
     * @return 
     */
    public boolean equalsFunctional(Cost other) {
        if (other == null){return false;}
        return this._ik == other._ik
                && this._remunerationCode.equals(other._remunerationCode)
                && this._costCenterId == other._costCenterId
                && this._costTypeId == other._costTypeId;
    }
}
