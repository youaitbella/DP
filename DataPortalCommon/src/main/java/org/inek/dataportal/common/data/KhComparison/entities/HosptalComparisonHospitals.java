package org.inek.dataportal.common.data.KhComparison.entities;

import org.inek.dataportal.common.data.KhComparison.enums.PsyHosptalComparisonHospitalsType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "HosptalComparisonHospitals", schema = "psy")
public class HosptalComparisonHospitals implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hchId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property hchAebBaseInformationId">
    @Column(name = "hchAebBaseInformationId")
    private int _aebBaseInformationId;

    public int getAebBaseInformationId() {
        return _aebBaseInformationId;
    }

    public void setAebBaseInformationId(int aebBaseInformationId) {
        this._aebBaseInformationId = aebBaseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property hchHospitalComparisonEvaluationsId">
    @ManyToOne
    @JoinColumn(name = "hchHospitalComparisonEvaluationsId")
    private HosptalComparisonEvaluations _hosptalComparisonEvaluations;

    public HosptalComparisonEvaluations getHosptalComparisonEvaluations() {
        return _hosptalComparisonEvaluations;
    }

    public void setHosptalComparisonEvaluations(HosptalComparisonEvaluations hosptalComparisonEvaluations) {
        this._hosptalComparisonEvaluations = hosptalComparisonEvaluations;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property hchType">
    @Column(name = "hchType")
    private String _type;

    public PsyHosptalComparisonHospitalsType getType() {
        return PsyHosptalComparisonHospitalsType.getByValue(_type);
    }

    public void setType(PsyHosptalComparisonHospitalsType type) {
        this._type = type.getValue();
    }

    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HosptalComparisonHospitals that = (HosptalComparisonHospitals) o;
        return _id == that._id &&
                _aebBaseInformationId == that._aebBaseInformationId &&
                Objects.equals(_hosptalComparisonEvaluations, that._hosptalComparisonEvaluations) &&
                Objects.equals(_type, that._type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _aebBaseInformationId, _hosptalComparisonEvaluations, _type);
    }
}
