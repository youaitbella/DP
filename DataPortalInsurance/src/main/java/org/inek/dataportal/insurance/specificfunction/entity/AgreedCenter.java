package org.inek.dataportal.insurance.specificfunction.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import org.inek.dataportal.common.utils.Documentation;
import org.inek.dataportal.common.specificfunction.entity.CenterName;
import org.inek.dataportal.common.specificfunction.entity.RelatedName;
import org.inek.dataportal.common.specificfunction.entity.SpecificFunction;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "AgreedCenter", schema = "spf")
public class AgreedCenter implements Serializable {

    private static final long serialVersionUID = 1L;

    public AgreedCenter() {
    }

    public AgreedCenter(int masterId) {
        _agreedMasterId = masterId;
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AgreedMasterId">
    @Column(name = "acAgreedMasterId")
    private int _agreedMasterId = -1;

    public int getAgreedMasterId() {
        return _agreedMasterId;
    }

    public void setAgreedMasterId(int agreedMasterId) {
        _agreedMasterId = agreedMasterId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CenterId">
    @Column(name = "acCenterId")
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
    @PrimaryKeyJoinColumn(name = "acCenterId")
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
    @Column(name = "acOtherCenterName")
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
    @Column(name = "acLocation")
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
            name = "mapAgreedCenterSpecificFunction",
            schema = "spf",
            joinColumns = {
                @JoinColumn(name = "acsfAgreedCenterId", referencedColumnName = "acId")},
            inverseJoinColumns = {
                @JoinColumn(name = "acsfSpecificFunctionId", referencedColumnName = "sfId", unique = true)}
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
    @Column(name = "acOtherSpecificFunction")
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

    // <editor-fold defaultstate="collapsed" desc="Property CostSum">
    @Column(name = "acCostSum")
    private double _costSum;

    @Min(0)
    public double getCostSum() {
        return _costSum;
    }

    public void setCostSum(double costSum) {
        _costSum = costSum;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property RelatedId">
    @Column(name = "acRelatedId")
    private int _relatedId;

    public int getRelatedId() {
        return _relatedId;
    }

    public void setRelatedId(int value) {
        _relatedId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CenterName">
    @OneToOne
    @PrimaryKeyJoinColumn(name = "acRelatedId")
    @Documentation(name = "Zentrum")
    private RelatedName _relatedName;

    public RelatedName getRelatedName() {
        return _relatedName;
    }

    public void setRelatedName(RelatedName value) {
        _relatedName = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property OtherRelatedText">
    @Column(name = "acOtherRelatedText")
    @Documentation(name = "Sonstige Art des Zentrums", omitOnEmpty = true)
    private String _otherRelatedText = "";

    @Size(max = 250)
    public String getOtherRelatedText() {
        return _otherRelatedText;
    }

    public void setOtherRelatedText(String otherRelatedText) {
        _otherRelatedText = otherRelatedText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ExtraAmount">
    @Column(name = "acExtraAmount")
    @Documentation(name = "Höhe des Zuschlags (€)")
    private double _extraAmount;

    @Min(0)
    public double getExtraAmount() {
        return _extraAmount;
    }

    public void setExtraAmount(double extraAmount) {
        _extraAmount = extraAmount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Sequence">
    @Column(name = "acSequence")
    @Documentation(name = "laufende Nummers")
    private int _sequence;

    public int getSequence() {
        return _sequence;
    }

    public void setSequence(int sequence) {
        this._sequence = sequence;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        if (_id != null) {
            return _id;
        }
        int hash = 7;
        hash = 97 * hash + this._agreedMasterId;
        hash = 97 * hash + Objects.hashCode(this._otherCenterName);
        hash = 97 * hash + Objects.hashCode(this._location);
        hash = 97 * hash + Objects.hashCode(this._otherSpecificFunction);

        return hash;
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
        final AgreedCenter other = (AgreedCenter) obj;
        if (_id != null) {
            return Objects.equals(_id, other._id);
        }
        if (other._id != null){
            return false;
        }
        if (!Objects.equals(this._agreedMasterId, other._agreedMasterId)) {
            return false;
        }
        if (this._costSum != other._costSum) {
            return false;
        }
        if (this._extraAmount != other._extraAmount) {
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
        return "AgreedCenter[id=" + _id + "]";
    }
    // </editor-fold>

    public boolean isEmpty() {
        return _id == null
                && _centerId == 0
                && _otherCenterName.isEmpty()
                && _location.isEmpty()
                && _specificFunctions.isEmpty()
                && _otherSpecificFunction.isEmpty()
                && _costSum == 0d
                && _extraAmount == 0d;
    }
}
