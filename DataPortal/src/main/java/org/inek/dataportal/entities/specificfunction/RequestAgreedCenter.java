package org.inek.dataportal.entities.specificfunction;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "RequestAgreedCenter", schema = "calc")
public class RequestAgreedCenter implements Serializable {
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "racId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property RequestMasterId">
    @Column(name = "racRequestMasterId")
    private int _requestMasterId = -1;
    public int getRequestMasterId() {
        return _requestMasterId;
    }

    public void setRequestMasterId(int requestMasterId) {
        _requestMasterId = requestMasterId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Center">
    @Column(name = "racCenter")
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
    @Min(1)
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        _amount = amount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        if (_id > 0){
            return _id;
        }
        int hash = 7;
        hash = 11 * hash + this._id;
        hash = 11 * hash + this._requestMasterId;
        hash = 11 * hash + Objects.hashCode(this._center);
        hash = 11 * hash + Objects.hashCode(this._remunerationKey);
        hash = 11 * hash + Objects.hashCode(this._amount);
        return hash;
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
        if (_id > 0 || other._id > 0){
            return _id == other._id;
        }
        if (this._requestMasterId != other._requestMasterId) {
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
    
}
