package org.inek.dataportal.psy.modelintention.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.inek.dataportal.common.utils.Documentation;

@Entity
@Table(name = "Adjustment", schema = "mvh")
public class Adjustment implements Serializable {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adId")
    private Integer _id;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
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
    @Documentation(key = "lblFor", translateValue = "1=enmAdjustmentType1;2=enmAdjustmentType2;3=enmAdjustmentType3;4=enmAdjustmentType4")
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
    @Documentation(key = "lblFrom", omitOnEmpty = true, dateFormat = "dd.MM.yyyy")
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
    @Documentation(key = "lblTo", omitOnEmpty = true, dateFormat = "dd.MM.yyyy")
    private Date _dateTo;

    public Date getDateTo() {
        return _dateTo;
    }

    public void setDateTo(Date dateTo) {
        _dateTo = dateTo;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="amount">
    @Column(name = "adAmount")
    @Documentation(key = "lblCostAmount", omitOnEmpty = true, isMoneyFormat = true)
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
    @Documentation(key = "tabNubPage1", omitOnEmpty = true)
    private String _description = "";

    public String getDescription() {
        return _description;
    }

    public void setDescription(String text) {
        _description = text;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Adjustment)) {
            return false;
        }
        Adjustment other = (Adjustment) object;
        return Objects.equals(_id, other._id)
                && (_id != null
                || _adjustmentTypeId == other._adjustmentTypeId
                && Objects.equals(_dateFrom, other._dateFrom)
                && Objects.equals(_dateTo, other._dateTo));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this._id);
        if (_id == null) {
            hash = 23 * hash + this._adjustmentTypeId;
            hash = 23 * hash + Objects.hashCode(this._dateFrom);
            hash = 23 * hash + Objects.hashCode(this._dateTo);
        }
        return hash;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + _id + " ]";
    }
    // </editor-fold>

}
