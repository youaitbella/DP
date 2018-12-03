package org.inek.dataportal.care.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lautenti
 */
@Entity
@Table(name = "Proof", schema = "care")
public class Proof implements Serializable {

    private static final long serialVersionUID = 1L;

    public Proof() {
    }


    public Proof(Proof proof) {
        this._deptStation = new DeptStation(proof.getDeptStation());
        this._shift = proof.getShift();
        this._month = proof.getMonth();
        this._countShift = proof.getCountShift();
        this._nurse = proof.getNurse();
        this._helpeNurse = proof.getHelpNurse();
        this._patientOccupancy = proof.getPatientOccupancy();
        this._countShiftNotRespected = proof.getCountShiftNotRespected();
        this._patientPerNurse = proof.getPatientPerNurse();
        this._countHelpeNurseChargeable = proof.getCountHelpeNurseChargeable();
    }


    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Dept Station">
    @ManyToOne
    @JoinColumn(name = "prDeptStationId")
    private DeptStation _deptStation;

    public DeptStation getDeptStation() {
        return _deptStation;
    }

    public void setDeptStation(DeptStation deptStation) {
        this._deptStation = deptStation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BaseInformation">
    @ManyToOne
    @JoinColumn(name = "prProofRegulationBaseInformationId")
    private ProofRegulationBaseInformation _baseInformation;

    public ProofRegulationBaseInformation getBaseInformation() {
        return _baseInformation;
    }

    public void setBaseInformation(ProofRegulationBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Shift">
    @Column(name = "prShift")
    private int _shift;

    public int getShift() {
        return _shift;
    }

    public void setShift(int shift) {
        this._shift = shift;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Month">
    @Column(name = "prMonth")
    private int _month;

    public int getMonth() {
        return _month;
    }

    public void setMonth(int month) {
        this._month = month;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CountShift">
    @Column(name = "prCountShift")
    private int _countShift;

    public int getCountShift() {
        return _countShift;
    }

    public void setCountShift(int countShift) {
        this._countShift = countShift;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Nurse">
    @Column(name = "prNurse")
    private double _nurse;

    public double getNurse() {
        return _nurse;
    }

    public void setNurse(double nurse) {
        this._nurse = nurse;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property HelpeNurse">
    @Column(name = "prHelpeNurse")
    private double _helpeNurse;

    public double getHelpNurse() {
        return _helpeNurse;
    }

    public void setHelpNurse(double helpeNurse) {
        this._helpeNurse = helpeNurse;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property PatientOccupancy">
    @Column(name = "prPatientOccupancy")
    private double _patientOccupancy;

    public double getPatientOccupancy() {
        return _patientOccupancy;
    }

    public void setPatientOccupancy(double patientOccupancy) {
        this._patientOccupancy = patientOccupancy;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CountShiftNotRespected">
    @Column(name = "prCountShiftNotRespected")
    private double _countShiftNotRespected;

    public double getCountShiftNotRespected() {
        return _countShiftNotRespected;
    }

    public void setCountShiftNotRespected(double countShiftNotRespected) {
        this._countShiftNotRespected = countShiftNotRespected;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property PatientPerNurse">
    @Column(name = "prPatientPerNurse")
    private double _patientPerNurse;

    public double getPatientPerNurse() {
        return _patientPerNurse;
    }

    public void setPatientPerNurse(double patientPerNurse) {
        this._patientPerNurse = patientPerNurse;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CountHelpeNurseChargeable">
    @Column(name = "prCountHelpeNurseChargeable")
    private double _countHelpeNurseChargeable;

    public double getCountHelpeNurseChargeable() {
        return _countHelpeNurseChargeable;
    }

    public void setCountHelpeNurseChargeable(double countHelpeNurseChargeable) {
        this._countHelpeNurseChargeable = countHelpeNurseChargeable;
    }
    //</editor-fold>

    @OneToOne(mappedBy = "_proof", cascade = CascadeType.ALL)
    private ProofObjection _proofObjection;

    public ProofObjection getProofObjection() {
        return _proofObjection;
    }

    public void setProofObjection(ProofObjection proofObjection) {
        this._proofObjection = proofObjection;
    }


    @SuppressWarnings("CyclomaticComplexity")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proof proof = (Proof) o;
        return _shift == proof._shift &&
                _month == proof._month &&
                _countShift == proof._countShift &&
                Double.compare(proof._nurse, _nurse) == 0 &&
                Double.compare(proof._helpeNurse, _helpeNurse) == 0 &&
                Double.compare(proof._patientOccupancy, _patientOccupancy) == 0 &&
                Double.compare(proof._countShiftNotRespected, _countShiftNotRespected) == 0 &&
                Double.compare(proof._patientPerNurse, _patientPerNurse) == 0 &&
                Double.compare(proof._countHelpeNurseChargeable, _countHelpeNurseChargeable) == 0 &&
                Objects.equals(_deptStation, proof._deptStation) &&
                Objects.equals(_baseInformation, proof._baseInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_deptStation, _baseInformation, _shift, _month, _countShift, _nurse, _helpeNurse, _patientOccupancy,
                _countShiftNotRespected, _patientPerNurse, _countHelpeNurseChargeable);
    }
}
