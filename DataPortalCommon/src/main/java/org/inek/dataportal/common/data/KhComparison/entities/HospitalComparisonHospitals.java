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
@Table(name = "HospitalComparisonHospitals", schema = "psy")
public class HospitalComparisonHospitals implements Serializable {

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
    private HospitalComparisonEvaluation _hospitalComparisonEvaluations;

    public HospitalComparisonEvaluation getHospitalComparisonEvaluations() {
        return _hospitalComparisonEvaluations;
    }

    public void setHospitalComparisonEvaluations(HospitalComparisonEvaluation hospitalComparisonEvaluations) {
        this._hospitalComparisonEvaluations = hospitalComparisonEvaluations;
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
        HospitalComparisonHospitals that = (HospitalComparisonHospitals) o;
        return _id == that._id &&
                _aebBaseInformationId == that._aebBaseInformationId &&
                Objects.equals(_hospitalComparisonEvaluations, that._hospitalComparisonEvaluations) &&
                Objects.equals(_type, that._type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _aebBaseInformationId, _hospitalComparisonEvaluations, _type);
    }
}
