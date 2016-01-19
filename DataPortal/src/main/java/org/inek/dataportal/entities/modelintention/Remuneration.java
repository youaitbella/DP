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
import javax.validation.constraints.Pattern;
import org.inek.dataportal.utils.Documentation;

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
    @Documentation(key = "lblRemunerationCode", omitOnEmpty = true)
    @Column(name = "reCode")
    private String _code = "";

    @Pattern(regexp = "[A-Z0-9]{8}", message = "Genau 8 Buchstaben/Ziffern erforderlich")
    public String getCode() {
        return _code;
    }

    public void setCode(String code) {
        _code = code;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="text">
    @Documentation(key = "lblRemunerationType", omitOnEmpty = true)
    @Column(name = "reText")
    private String _text = "";

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="amount">
    @Documentation(key = "lblRemunerationAmount", isMoneyFormat = true)
    @Column(name = "reAmount")
    private BigDecimal _amount = new BigDecimal(0d);

    public BigDecimal getAmount() {
        return _amount;
    }

    public void setAmount(BigDecimal amount) {
        _amount = amount;
    }
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Remuneration)) {
            return false;
        }
        Remuneration other = (Remuneration) object;
        return Objects.equals(_id, other._id)
                && (_id != null
                || Objects.equals(_code, other._code)
                && Objects.equals(_text, other._text));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this._id);
        if (_id == null) {
            hash = 67 * hash + Objects.hashCode(this._code);
            hash = 67 * hash + Objects.hashCode(this._text);
        }
        return hash;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + _id + " ]";
    }
    // </editor-fold>

}
