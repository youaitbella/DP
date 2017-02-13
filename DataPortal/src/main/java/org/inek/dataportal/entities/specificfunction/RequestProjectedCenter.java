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
@Table(name = "RequestProjectedCenter", schema = "spf")
public class RequestProjectedCenter implements Serializable {
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
    @Column(name = "rpcCenter")
    private String _center = "";

    @Size(max = 250)
    public String getCenter() {
        return _center;
    }

    public void setCenter(String center) {
        _center = center;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Location">
    @Column(name = "rpcLocation")
    private String _location = "";

    @Size(max = 250)
    public String getLocation() {
        return _location;
    }

    public void setLocation(String location) {
        _location = location;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SpecialFunction">
    @Column(name = "rpcSpecialFunction")
    private String _specialFunction = "";

    @Size(max = 500)
    public String getSpecialFunction() {
        return _specialFunction;
    }

    public void setSpecialFunction(String specialFunction) {
        _specialFunction = specialFunction;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Type">
    @Column(name = "rpcType")
    private int _type;

    @Min(0)
    public int getType() {
        return _type;
    }

    public void setType(int type) {
        _type = type;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property EstimatedPatientCount">
    @Column(name = "rpcEstimatedPatientCount")
    private int _estimatedPatientCount;

    @Min(0)
    public int getEstimatedPatientCount() {
        return _estimatedPatientCount;
    }

    public void setEstimatedPatientCount(int estimatedPatientCount) {
        _estimatedPatientCount = estimatedPatientCount;
    }
    // </editor-fold>

    @Override
    public int hashCode() {
        if (_id > 0){
            return _id;
        }
        int hash = 7;
        hash = 97 * hash + this._id;
        hash = 97 * hash + this._requestMasterId;
        hash = 97 * hash + Objects.hashCode(this._center);
        hash = 97 * hash + Objects.hashCode(this._location);
        hash = 97 * hash + Objects.hashCode(this._specialFunction);
        hash = 97 * hash + this._type;
        hash = 97 * hash + this._estimatedPatientCount;
        return hash;
    }

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
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
        final RequestProjectedCenter other = (RequestProjectedCenter) obj;
        if (_id > 0 || other._id > 0){
            return _id == other._id;
        }
        if (this._requestMasterId != other._requestMasterId) {
            return false;
        }
        if (this._type != other._type) {
            return false;
        }
        if (this._estimatedPatientCount != other._estimatedPatientCount) {
            return false;
        }
        if (!Objects.equals(this._center, other._center)) {
            return false;
        }
        if (!Objects.equals(this._location, other._location)) {
            return false;
        }
        if (!Objects.equals(this._specialFunction, other._specialFunction)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RequestProjectedCenter[id=" + _id + "]";
    }
    // </editor-fold>
    
}


