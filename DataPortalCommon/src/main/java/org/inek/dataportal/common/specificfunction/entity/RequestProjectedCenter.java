package org.inek.dataportal.common.specificfunction.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "RequestProjectedCenter", schema = "spf")
public class RequestProjectedCenter implements Serializable {

    private static final long serialVersionUID = 1L;

    public RequestProjectedCenter() {
    }

    public RequestProjectedCenter(SpecificFunctionRequest master) {
        _requestMaster = master;
    }

    public RequestProjectedCenter(RequestProjectedCenter template) {
        _centerId = template._centerId;
        _centerName = template._centerName;
        _otherCenterName = template._otherCenterName;
        _location = template._location;
        _specificFunctions.addAll(template._specificFunctions);
        _otherSpecificFunction = template._otherSpecificFunction;
        _typeId = template._typeId;
        _estimatedPatientCount = template._estimatedPatientCount;
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rpcId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property RequestMasterId">
    @ManyToOne
    @JoinColumn(name = "rpcRequestMasterId")
    private SpecificFunctionRequest _requestMaster;

    public SpecificFunctionRequest getRequestMaster() {
        return _requestMaster;
    }

    public void setRequestMaster(SpecificFunctionRequest requestMaster) {
        _requestMaster = requestMaster;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CenterId">
    @Column(name = "rpcCenterId")
    private int _centerId;

    public int getCenterId() {
        return _centerId;
    }

    public void setCenterId(int value) {
        _centerId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CenterName">
    @OneToOne
    @PrimaryKeyJoinColumn(name = "rpcCenterId")
    @Documentation(name = "Zentrum")
    private CenterName _centerName;

    public CenterName getCenterName() {
        return _centerName;
    }

    public void setCenterName(CenterName value) {
        _centerName = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property OtherCenterName">
    @Column(name = "rpcOtherCenterName")
    @Documentation(name = "Sonstige Art des Zentrums", omitOnEmpty = true)
    private String _otherCenterName = "";

    @Size(max = 250)
    public String getOtherCenterName() {
        return _otherCenterName;
    }

    public void setOtherCenterName(String otherCenterName) {
        _otherCenterName = otherCenterName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Location">
    @Column(name = "rpcLocation")
    @Documentation(name = "Standort", omitOnEmpty = true)
    private String _location = "";

    @Size(max = 250)
    public String getLocation() {
        return _location;
    }

    public void setLocation(String location) {
        _location = location;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SpecificFunction">
    @OneToMany
    @JoinTable(
            name = "mapProjectedCenterSpecificFunction",
            schema = "spf",
            joinColumns = {
                @JoinColumn(name = "pcsfProjectedCenterId", referencedColumnName = "rpcId")},
            inverseJoinColumns = {
                @JoinColumn(name = "pcsfSpecificFunctionId", referencedColumnName = "sfId", unique = true)}
            )
    @Documentation(name = "Besondere Aufgaben")
    private List<SpecificFunction> _specificFunctions = new Vector<>();

    public List<SpecificFunction> getSpecificFunctions() {
        return _specificFunctions;
    }

    public void setSpecificFunctions(List<SpecificFunction> specificFunctions) {
        _specificFunctions = specificFunctions;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property OtherSpecificFunction">
    @Column(name = "rpcOtherSpecificFunction")
    @Documentation(name = "Sonstige Besondere Aufgaben", omitOnEmpty = true)
    private String _otherSpecificFunction = "";

    @Size(max = 500)
    public String getOtherSpecificFunction() {
        return _otherSpecificFunction;
    }

    public void setOtherSpecificFunction(String otherSpecificFunction) {
        _otherSpecificFunction = otherSpecificFunction;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property TypeId">
    @Column(name = "rpcTypeId")
    @Documentation(name = "Ausweisung und Festsetzung",
            translateValue = "1=im Krankenhausplan des Landes;2=durch gleichartige Festlegung der zuständigen Landesbehörde")
    private int _typeId;

    @Min(0)
    public int getTypeId() {
        return _typeId;
    }

    public void setTypeId(int typeId) {
        _typeId = typeId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property EstimatedPatientCount">
    @Column(name = "rpcEstimatedPatientCount")
    @Documentation(name = "Anzahl Patienten (ca.)")
    private int _estimatedPatientCount;

    @Min(0)
    public int getEstimatedPatientCount() {
        return _estimatedPatientCount;
    }

    public void setEstimatedPatientCount(int estimatedPatientCount) {
        _estimatedPatientCount = estimatedPatientCount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return 2447;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
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
        if (_id != null) {
            return Objects.equals(_id, other._id);
        }
        if (other._id != null){
            return false;
        }
        if (!Objects.equals(this._requestMaster, other._requestMaster)) {
            return false;
        }
        if (this._typeId != other._typeId) {
            return false;
        }
        if (this._estimatedPatientCount != other._estimatedPatientCount) {
            return false;
        }
        if (!Objects.equals(this._otherCenterName, other._otherCenterName)) {
            return false;
        }
        if (!Objects.equals(this._location, other._location)) {
            return false;
        }
        if (!Objects.equals(this._otherSpecificFunction, other._otherSpecificFunction)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RequestProjectedCenter[id=" + _id + "]";
    }
    // </editor-fold>

    public boolean isEmpty() {
        return _id == null
                && _centerId == 0
                && _otherCenterName.isEmpty()
                && _location.isEmpty()
                && _specificFunctions.isEmpty()
                && _otherSpecificFunction.isEmpty()
                && _typeId == 0
                && _estimatedPatientCount == 0;
    }

}
