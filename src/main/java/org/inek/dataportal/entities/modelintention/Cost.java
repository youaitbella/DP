package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


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
    private int _ik = -1;

    public Integer getIk() {
        return _ik < 0 ? null : _ik;
    }

    public void setIk(Integer ik) {
        _ik = ik == null ? -1 : ik;
    }
    // </editor-fold>
    
    @Column (name = "coRemunerationKey")
    private String _remunerationCode = "";
    
    @Column (name = "coCostCenterId")
    private String _costCenterId = "";
    
    @Column (name = "coCostTypeId")
    private String _costTypeId = "";
    
    @Column (name = "coAmount")
    private BigDecimal _amount;
    
      // <editor-fold defaultstate="collapsed" desc="UUID">
    @Transient
    private final String _uuid = UUID.randomUUID().toString().replace("-", "");

    public String getUUID() {
        return _uuid;
    }

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

    public String getCostCenterId() {
        return _costCenterId;
    }

    public void setCostCenterId(String costCenterId) {
        _costCenterId = costCenterId;
    }

    public String getCostTypeId() {
        return _costTypeId;
    }

    public void setCostTypeId(String costTypeId) {
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
        if (this._id == null && other._id == null){
            return this._uuid.equals(other._uuid);
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.Cost[id=" + _id + "]";
    }

    // </editor-fold>
}
