package org.inek.dataportal.care.proof.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.care.entities.SensitiveDomain;
import org.inek.dataportal.care.enums.Months;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.common.utils.DateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
        this._proofWard = proof.getProofWard();
        this._shift = proof.getShift().getId();
        this.validFrom = proof.validFrom;
        this.validTo = proof.validTo;
        this._month = proof.getMonth().getId();
        this.deptNumbers = proof.deptNumbers;
        this.deptNames = proof.deptNames;
        this.sensitiveDomains = proof.sensitiveDomains;
        this.significantSensitiveDomain = proof.significantSensitiveDomain;
        this.beds = proof.getBeds();
        this._countShift = proof.getCountShift();
        this._occupancyDays = proof.getOccupancyDays();
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
    private Integer id;

    public int getId() {
        return id == null ? -1 : id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    //<editor-fold desc="Property ValidFrom">
    @Column(name = "prValidFrom")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validFrom;

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }
    //</editor-fold>

    //<editor-fold desc="Property ValidTo">
    @Column(name = "prValidTo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validTo;

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
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

    //<editor-fold defaultstate="collapsed" desc="Property DeptNumbers">
    @Column(name = "prDeptNumbers")
    private String deptNumbers = "";

    public String getDeptNumbers() {
        return deptNumbers;
    }

    public void setDeptNumbers(String deptNumbers) {
        this.deptNumbers = deptNumbers;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DeptNames">
    @Column(name = "prDeptNames")
    private String deptNames = "";

    public String getDeptNames() {
        return deptNames;
    }

    public void setDeptNames(String deptNames) {
        this.deptNames = deptNames;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property SensitiveDomains">
    @Column(name = "prSensitiveDomains")
    private String sensitiveDomains = "";

    public String getSensitiveDomains() {
        return sensitiveDomains;
    }

    public void setSensitiveDomains(String sensitiveDomains) {
        this.sensitiveDomains = sensitiveDomains;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SignificantSensitiveDomain">
    @ManyToOne
    @JoinColumn(name = "prSignificantSensitiveDomainId")
    private SensitiveDomain significantSensitiveDomain;

    public SensitiveDomain getSignificantSensitiveDomain() {
        return significantSensitiveDomain;
    }

    public void setSignificantSensitiveDomain(SensitiveDomain significantSensitiveDomain) {
        this.significantSensitiveDomain = significantSensitiveDomain;
    }
    // </editor-fold>

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

    //<editor-fold defaultstate="collapsed" desc="Property OccupancyDays">
    @Column(name = "prOccupancyDays")
    private int _occupancyDays;

    public int getOccupancyDays() {
        return _occupancyDays;
    }

    public void setOccupancyDays(int occupancyDays) {
        this._occupancyDays = occupancyDays;
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

    //<editor-fold desc="Property ExceptionFacts">
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
    //</editor-fold>


    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proof proof = (Proof) o;
        return _month == proof._month &&
                _shift == proof._shift &&
                id.equals(proof.id) &&
                _baseInformation.equals(proof._baseInformation) &&
                _proofWard.equals(proof._proofWard) &&
                validFrom.equals(proof.validFrom) &&
                validTo.equals(proof.validTo) &&
                deptNumbers.equals(proof.deptNumbers) &&
                deptNames.equals(proof.deptNames) &&
                significantSensitiveDomain.equals(proof.significantSensitiveDomain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, _proofWard, validFrom, validTo, _month, deptNumbers,
                deptNames, significantSensitiveDomain, _shift);
    }

    public int duration() {
        return DateUtils.duration(validFrom, validTo);
    }
}
