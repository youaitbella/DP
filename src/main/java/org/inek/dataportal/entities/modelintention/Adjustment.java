package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "Adjustment", schema = "mvh")
public class Adjustment implements Serializable {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="modelIntentionId">
    @Column(name = "adModelIntentionId")
    private Integer _modelIntentionId;

    public Integer getModelIntentionId() {
        return _modelIntentionId;
    }

    public void setModelIntentionId(Integer modelIntentionId) {
        _modelIntentionId = modelIntentionId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="adjustmentTypeId">
    @Column(name = "adAdjustmentTypeId")
    private int _adjustmentTypeId;

    public int getAdjustmentTypeId() {
        return _adjustmentTypeId;
    }

    public void setAdjustmentTypeId(int adjustmentTypeId) {
        _adjustmentTypeId = adjustmentTypeId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="dateFrom">
    @Column(name = "adDateFrom")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _dateFrom;

    public Date getDateFrom() {
        return _dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        _dateFrom = dateFrom;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="dateTo">
    @Column(name = "adDateTo")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _dateTo;

    public Date getDateTo() {
        return _dateTo;
    }

    public void setDateTo(Date dateTo) {
        _dateTo = dateTo;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="amount">
    @Column (name = "adAmount")
    private BigDecimal _amount = new BigDecimal(0d);
    
    public BigDecimal getAmount() {
        return _amount;
    }

    public void setAmount(BigDecimal amount) {
        _amount = amount;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="description">
    @Column(name = "adDescription")
    private String _description;

    public String getDescription() {
        return _description;
    }

    public void setDescription(String text) {
        _description = text;
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
        if (!(object instanceof Adjustment)) {
            return false;
        }
        Adjustment other = (Adjustment) object;
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
