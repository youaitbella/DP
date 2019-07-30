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
public class HosptalComparisonEvaluation implements Serializable {

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

    //<editor-fold defaultstate="collapsed" desc="Property hceEvaluationTypeId">
    @ManyToOne
    @JoinColumn(name = "hceHosptalComparisonInfoId")
    private HosptalComparisonInfo _hosptalComparisonInfo;

    public HosptalComparisonInfo getHosptalComparisonInfo() {
        return _hosptalComparisonInfo;
    }

    public void setHosptalComparisonInfo(HosptalComparisonInfo hosptalComparisonInfo) {
        this._hosptalComparisonInfo = hosptalComparisonInfo;
    }
    //</editor-fold>

    @OneToMany(mappedBy = "_hosptalComparisonEvaluations", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hchHospitalComparisonEvaluationsId")
    private List<HosptalComparisonHospitals> _hosptalComparisonHospitals  = new ArrayList<>();

    public List<HosptalComparisonHospitals> getHosptalComparisonHospitals() {
        return _hosptalComparisonHospitals;
    }

    public List<HosptalComparisonHospitals> getHosptalComparisonHospitalsGroup() {
        return _hosptalComparisonHospitals.stream()
                .filter(c -> c.getType().equals(PsyHosptalComparisonHospitalsType.Group))
                .collect(Collectors.toList());
    }

    public void setHosptalComparisonHospitals(List<HosptalComparisonHospitals> hosptalComparisonHospitals) {
        this._hosptalComparisonHospitals = hosptalComparisonHospitals;
    }

    public void addHosptalComparisonHospitals(HosptalComparisonHospitals hospital) {
        hospital.setHosptalComparisonEvaluations(this);
        _hosptalComparisonHospitals.add(hospital);
    }

    public void addHosptalComparisonHospitals(List<HosptalComparisonHospitals> hospitals) {
        for (HosptalComparisonHospitals hospital : hospitals) {
            hospital.setHosptalComparisonEvaluations(this);
            _hosptalComparisonHospitals.add(hospital);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HosptalComparisonEvaluation that = (HosptalComparisonEvaluation) o;
        return _id == that._id &&
                _evaluationTypeId == that._evaluationTypeId &&
                Objects.equals(_hosptalComparisonInfo, that._hosptalComparisonInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_evaluationTypeId, _hosptalComparisonInfo);
    }
}
