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
@Table(name = "Remuneration", schema = "mvh")
public class Remuneration implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Properties">
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reId")
    private Long _id;

    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="modelIntentionId">
    @Column(name = "reModelIntentionId")
    private int _modelIntentionId;

    public int getModelIntentionId() {
        return _modelIntentionId;
    }

    public void setModelIntentionId(int modelIntentionId) {
        _modelIntentionId = modelIntentionId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="code">
    @Column(name = "reCode")
    private String _code = "";

    public String getCode() {
        return _code;
    }

    public void setCode(String code) {
        _code = code;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="text">
    @Column(name = "reText")
    private String _text = "";

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="amaount">
    @Column(name = "reAmount")
    private BigDecimal _amount = new BigDecimal(0);

    public BigDecimal getAmount() {
        return _amount;
    }

    public void setAmount(BigDecimal amount) {
        _amount = amount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="UUID">
    @Transient
    private final String _uuid = UUID.randomUUID().toString().replace("-", "");

    public String getUUID() {
        return _uuid;
    }
    // </editor-fold>
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
        if (!(object instanceof Remuneration)) {
            return false;
        }
        Remuneration other = (Remuneration) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        if (this._id == null && other._id == null){
            return this._uuid.equals(other._uuid);
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + _id + " ]";
    }
    // </editor-fold>

}
