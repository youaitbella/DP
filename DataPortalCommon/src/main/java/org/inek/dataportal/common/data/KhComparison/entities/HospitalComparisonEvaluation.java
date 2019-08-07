package org.inek.dataportal.common.data.KhComparison.entities;

import org.inek.dataportal.common.data.KhComparison.enums.PsyHosptalComparisonHospitalsType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "HospitalComparisonEvaluation", schema = "psy")
public class HospitalComparisonEvaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hceId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property hceEvaluationTypeId">
    @Column(name = "hceEvaluationTypeId")
    private int _evaluationTypeId;

    public int getEvaluationTypeId() {
        return _evaluationTypeId;
    }

    public void setEvaluationTypeId(int evaluationTypeId) {
        this._evaluationTypeId = evaluationTypeId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property hceHospitalComparisonInfoId">
    @ManyToOne
    @JoinColumn(name = "hceHospitalComparisonInfoId")
    private HospitalComparisonInfo _hospitalComparisonInfo;

    public HospitalComparisonInfo getHospitalComparisonInfo() {
        return _hospitalComparisonInfo;
    }

    public void setHospitalComparisonInfo(HospitalComparisonInfo hospitalComparisonInfo) {
        this._hospitalComparisonInfo = hospitalComparisonInfo;
    }
    //</editor-fold>

    @OneToMany(mappedBy = "_hospitalComparisonEvaluations", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hchHospitalComparisonEvaluationsId")
    private List<HospitalComparisonHospitals> _hospitalComparisonHospitals = new ArrayList<>();

    public List<HospitalComparisonHospitals> getHospitalComparisonHospitals() {
        return _hospitalComparisonHospitals;
    }

    public List<HospitalComparisonHospitals> getHospitalComparisonHospitalsGroup() {
        return _hospitalComparisonHospitals.stream()
                .filter(c -> c.getType().equals(PsyHosptalComparisonHospitalsType.Group))
                .collect(Collectors.toList());
    }

    public void setHospitalComparisonHospitals(List<HospitalComparisonHospitals> hospitalComparisonHospitals) {
        this._hospitalComparisonHospitals = hospitalComparisonHospitals;
    }

    public void addHospitalComparisonHospitals(HospitalComparisonHospitals hospital) {
        hospital.setHospitalComparisonEvaluations(this);
        _hospitalComparisonHospitals.add(hospital);
    }

    public void addHospitalComparisonHospitals(List<HospitalComparisonHospitals> hospitals) {
        for (HospitalComparisonHospitals hospital : hospitals) {
            hospital.setHospitalComparisonEvaluations(this);
            _hospitalComparisonHospitals.add(hospital);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HospitalComparisonEvaluation that = (HospitalComparisonEvaluation) o;
        return _id == that._id &&
                _evaluationTypeId == that._evaluationTypeId &&
                Objects.equals(_hospitalComparisonInfo, that._hospitalComparisonInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_evaluationTypeId, _hospitalComparisonInfo);
    }
}
