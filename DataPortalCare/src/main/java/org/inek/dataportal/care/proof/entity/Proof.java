package org.inek.dataportal.care.proof.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.care.enums.Months;
import org.inek.dataportal.care.enums.Shift;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Proof", schema = "care")
public class Proof implements Serializable {

    private static final long serialVersionUID = 1L;

    public Proof() {
    }

    public Proof(ProofRegulationBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
    }


    public Proof(Proof proof) {
        // old: this._proofRegulationStation = proof.getProofRegulationStation();
        this._proofWard = proof.getProofWard();
        this._shift = proof.getShift().getId();
        this._month = proof.getMonth().getId();
        this.beds = proof.getBeds()
        this.maxShiftCount = proof.getMaxShiftCount();
        this._countShift = proof.getCountShift();
        this._nurse = proof.getNurse();
        this._helpeNurse = proof.getHelpNurse();
        this._patientOccupancy = proof.getPatientOccupancy();
        this._countShiftNotRespected = proof.getCountShiftNotRespected();
        this._patientPerNurse = proof.getPatientPerNurse();
        this._countHelpeNurseChargeable = proof.getCountHelpeNurseChargeable();

        for (ProofExceptionFact exceptionFact : proof.getExceptionFact()) {
            ProofExceptionFact newExceptionFact = new ProofExceptionFact(exceptionFact);
            newExceptionFact.setProof(this);
            addExceptionFact(newExceptionFact);
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prId")
    private Integer _id;

    public int getId() {
        return _id == null ? -1 : _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BaseInformation">
    @ManyToOne
    @JoinColumn(name = "prProofRegulationBaseInformationId")
    @JsonIgnore
    private ProofRegulationBaseInformation _baseInformation;

    @JsonIgnore
    public ProofRegulationBaseInformation getBaseInformation() {
        return _baseInformation;
    }

    @JsonIgnore
    public void setBaseInformation(ProofRegulationBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
    }

    //Using only for JSON Export
    public int getBaseInformationId() {
        return _baseInformation.getId();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Proof Station">
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "prProofRegulationStationId")
    private ProofRegulationStation _proofRegulationStation;

    @JsonIgnore
    public ProofRegulationStation getProofRegulationStation() {
        return _proofRegulationStation;
    }

    public void setProofRegulationStation(ProofRegulationStation proofRegulationStation) {
        this._proofRegulationStation = proofRegulationStation;
    }

    //Using only for JSON Export and to distinguish between old and new format
    public int getProofRegulationStationId() {
        return _proofRegulationStation.getId();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ProofWard">
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "prProofWardId")
    private ProofWard _proofWard;

    @JsonIgnore
    public ProofWard getProofWard() {
        return _proofWard;
    }

    public void setProofWard(ProofWard proofWard) {
        this._proofWard = proofWard;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Month">
    @Column(name = "prMonth")
    private int _month;

    public Months getMonth() {
        return Months.getById(_month);
    }

    public void setMonth(Months month) {
        this._month = month.getId();
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Shift">
    @Column(name = "prShift")
    private int _shift;

    public Shift getShift() {
        return Shift.getById(_shift);
    }

    public void setShift(Shift shift) {
        this._shift = shift.getId();
    }
    //</editor-fold>

    //<editor-fold desc="Property Beds">
    @Column(name = "prBeds")
    private double beds;

    public double getBeds() {
        return beds;
    }

    public void setBeds(double beds) {
        this.beds = beds;
    }
    //</editor-fold>

    //<editor-fold desc="Property MaxShiftCount">
    @Column(name = "prMaxShiftCount")
    private int maxShiftCount;

    public int getMaxShiftCount() {
        return maxShiftCount;
    }

    public void setMaxShiftCount(int maxShiftCount) {
        this.maxShiftCount = maxShiftCount;
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

    //<editor-fold defaultstate="collapsed" desc="Property Comment">
    @Column(name = "prComment")
    private String _comment = "";

    public String getComment() {
        return _comment;
    }

    public void setComment(String comment) {
        this._comment = comment;
    }
    //</editor-fold>

    @OneToMany(mappedBy = "_proof", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pefProofId")
    private List<ProofExceptionFact> _proofExceptionFact = new ArrayList<>();

    public List<ProofExceptionFact> getExceptionFact() {
        return _proofExceptionFact;
    }

    public void setExceptionFact(List<ProofExceptionFact> proofExceptionFact) {
        this._proofExceptionFact = proofExceptionFact;
    }

    public void addExceptionFact(ProofExceptionFact fact) {
        _proofExceptionFact.add(fact);
    }

    public void removeExceptionFact(ProofExceptionFact fact) {
        _proofExceptionFact.remove(fact);
    }

    @Transient
    @JsonIgnore
    private double _ppug;

    @Transient
    @JsonIgnore
    private double _part;

    @JsonIgnore
    public double getPpug() {
        return _ppug;
    }

    @JsonIgnore
    public void setPpug(double ppug) {
        this._ppug = ppug;
    }

    @JsonIgnore
    public double getPart() {
        return _part;
    }

    @JsonIgnore
    public void setPart(double part) {
        this._part = part;
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
                Objects.equals(_proofRegulationStation, proof._proofRegulationStation) &&
                Objects.equals(_baseInformation, proof._baseInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_proofRegulationStation, _baseInformation, _shift, _month, _countShift, _nurse, _helpeNurse, _patientOccupancy,
                _countShiftNotRespected, _patientPerNurse, _countHelpeNurseChargeable);
    }

    public Double getNewPpug() {
        return 10.0;
    }
}
