package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


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
    
    @Column (name = "coIk")
    private int _ik = -1;
    
    @Column (name = "coRemunerationKey")
    private String _remunerationKey = "";
    
    @Column (name = "coCostCenterId")
    private int _costCenterId = -1;
    
    @Column (name = "coCostTypeId")
    private int _costTypeId = -1;
    
    @Column (name = "coAmount")
    private BigDecimal _amount;
    
  
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

    public int getIk() {
        return _ik;
    }

    public void setIk(int _ik) {
        _ik = _ik;
    }

    public String getRemunerationKey() {
        return _remunerationKey;
    }

    public void setRemunerationKey(String remunerationKey) {
        _remunerationKey = remunerationKey;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cost)) {
            return false;
        }
        Cost other = (Cost) object;
        if ((_id == null && other.getId()!= null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.Cost[id=" + _id + "]";
    }

    // </editor-fold>
}
