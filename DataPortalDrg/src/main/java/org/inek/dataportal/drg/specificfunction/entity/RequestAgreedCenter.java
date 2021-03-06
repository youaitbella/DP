package org.inek.dataportal.drg.specificfunction.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import org.inek.dataportal.common.utils.Documentation;

@Entity
@Table(name = "RequestAgreedCenter", schema = "spf")
public class RequestAgreedCenter implements Serializable {

    private static final long serialVersionUID = 1L;

    public RequestAgreedCenter() {
    }

    public RequestAgreedCenter(SpecificFunctionRequest master) {
        _requestMaster = master;
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "racId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property RequestMaster">
    @ManyToOne
    @JoinColumn(name = "racRequestMasterId")
    private SpecificFunctionRequest _requestMaster;

    public SpecificFunctionRequest getRequestMaster() {
        return _requestMaster;
    }

    public void setRequestMaster(SpecificFunctionRequest requestMaster) {
        _requestMaster = requestMaster;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Center">
    @Column(name = "racCenter")
    @Documentation(name = "Art des Zentrums")
    private String _center = "";

    @Size(max = 250)
    public String getCenter() {
        return _center;
    }

    public void setCenter(String center) {
        _center = center;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property RemunerationKey">
    @Column(name = "racRemunerationKey")
    @Documentation(key = "lblRemunerationCode")
    private String _remunerationKey = "";

    @Size(max = 10)
    public String getRemunerationKey() {
        return _remunerationKey;
    }

    public void setRemunerationKey(String remunerationKey) {
        _remunerationKey = remunerationKey;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Amount">
    @Column(name = "racAmount")
    @Documentation(name = "Betrag", omitOnValues = "0")
    private int _amount;

    @Min(0)
    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        _amount = amount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Percent">
    @Column(name = "racPercent")
    @Documentation(name = "Prozent", omitOnValues = "0")
    private double _percent;

    //@Min(0)
    // even thought Min is allowed to be used on double, this somtimes causes a validation failure
    // this seems to be a known bug - we use JSF validation instead
    public double getPercent() {
        return _percent;
    }

    public void setPercent(double percent) {
        _percent = percent;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return 2461;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RequestAgreedCenter other = (RequestAgreedCenter) obj;
        if (_id !=  null) {
            return Objects.equals(_id, other._id);
        }
        if (other._id != null) {
            return false;
        }
        if (!Objects.equals(this._requestMaster, other._requestMaster)) {
            return false;
        }
        if (!Objects.equals(this._center, other._center)) {
            return false;
        }
        if (!Objects.equals(this._remunerationKey, other._remunerationKey)) {
            return false;
        }
        return this._amount == other._amount;
    }

    @Override
    public String toString() {
        return "RequestAgreedCenter[id=" + _id + "]";
    }
    // </editor-fold>

    public boolean isEmpty() {
        return (_id == null || _id == -1)
                && _center.isEmpty()
                && _remunerationKey.isEmpty()
                && _amount == 0
                && _percent == 0d;
    }

}
