
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
@Table(name = "RemunerationCode", schema = "mvh")
public class RemunerationCode implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "rcId")
    private Long _id;
    
    @Column (name = "rcModelIntentionId")
    private int _modelIntentionId;
    
    @Column (name = "rcCode")
    private String _code = "";
    
    @Column (name = "rcText")
    private String _text = "";
    
    @Column (name = "rcAmount")
    private BigDecimal _amount = new BigDecimal(0);
    
    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        _id = id;
    }
    
    public int getModelIntentionId() {
        return _modelIntentionId;
    }

    public void setModelIntentionId(int modelIntentionId) {
        _modelIntentionId = modelIntentionId;
    }
    
    public String getCode() {
        return _code;
    }

    public void setCode(String code) {
        _code = code;
    }
    
    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
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
        // TODO: Warning - this method won't work in the case the _id fields are not set
        if (!(object instanceof RemunerationCode)) {
            return false;
        }
        RemunerationCode other = (RemunerationCode) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + _id + " ]";
    }
    // </editor-fold>

}
