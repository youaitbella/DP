package org.inek.dataportal.feature.psychstaff.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.persistence.*;
import org.inek.dataportal.entities.iface.StatusEntity;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.feature.psychstaff.enums.PsychType;
import org.inek.dataportal.utils.Crypt;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "StaffProofMaster", schema = "psy")
public class StaffProof implements Serializable, StatusEntity {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spmId")
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dataYear">
    @Column(name = "spmYear")
    @Documentation(key = "lblYear")
    private int _year = 2017;

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        this._year = year;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "spmIK")
    @Documentation(key = "lblIK")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "spmAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Created">
    @Column(name = "spmCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _created = new Date();

    public Date getCreated() {
        return _created;
    }

    public void setCreated(Date created) {
        this._created = created;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "spmLastChanged")
    @Documentation(name = "Stand")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastChanged = new Date();

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        this._lastChanged = lastChanged;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Sealed">
    @Column(name = "spmSealed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _sealed = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    public Date getSealed() {
        return _sealed;
    }

    public void setSealed(Date sealed) {
        this._sealed = sealed;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StatusId">
    @Column(name = "spmStatusId")
    private int _statusId;

    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        this._statusId = statusId;
    }

    @Documentation(key = "lblWorkstate", rank = 10)
    @Override
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    @Override
    public void setStatus(WorkflowStatus status) {
        _statusId = status.getId();
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ForAdults">
    @Column(name = "spmIsForAdults")
    private boolean _forAdults;

    public boolean isForAdults() {
        return _forAdults;
    }

    public void setForAdults(boolean forAdults) {
        _forAdults = forAdults;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ForKids">
    @Column(name = "spmIsForKids")
    private boolean _forKids;

    public boolean isForKids() {
        return _forKids;
    }

    public void setForKids(boolean forKids) {
        _forKids = forKids;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CalculationType">
    @Column(name = "spmCalculationType")
    private int _calculationType = 1;

    public int getCalculationType() {
        return _calculationType;
    }

    public void setCalculationType(int calculationType) {
        _calculationType = calculationType;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AdultsAgreedDays">
    @Column(name = "spmAdultsAgreedDays")
    private int _adultsAgreedDays;

    public int getAdultsAgreedDays() {
        return _adultsAgreedDays;
    }

    public void setAdultsAgreedDays(int adultsAgreedDays) {
        _adultsAgreedDays = adultsAgreedDays;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AdultsEffectiveDays">
    @Column(name = "spmAdultsEffectiveDays")
    private int _adultsEffectiveDays;

    public int getAdultsEffectiveDays() {
        return _adultsEffectiveDays;
    }

    public void setAdultsEffectiveDays(int adultsEffectiveDays) {
        _adultsEffectiveDays = adultsEffectiveDays;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AdultsEffectiveCosts">
    @Column(name = "spmAdultsEffectiveCosts")
    private double _adultsEffectiveCosts;

    public double getAdultsEffectiveCosts() {
        return _adultsEffectiveCosts;
    }

    public void setAdultsEffectiveCosts(double adultsEffectiveCosts) {
        _adultsEffectiveCosts = adultsEffectiveCosts;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property KidsAgreedDays">
    @Column(name = "spmKidsAgreedDays")
    private int _kidsAgreedDays;

    public int getKidsAgreedDays() {
        return _kidsAgreedDays;
    }

    public void setKidsAgreedDays(int kidsAgreedDays) {
        _kidsAgreedDays = kidsAgreedDays;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property KidsEffectiveDays">
    @Column(name = "spmKidsEffectiveDays")
    private int _kidsEffectiveDays;

    public int getKidsEffectiveDays() {
        return _kidsEffectiveDays;
    }

    public void setKidsEffectiveDays(int kidsEffectiveDays) {
        _kidsEffectiveDays = kidsEffectiveDays;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property KidsEffectiveCosts">
    @Column(name = "spmKidsEffectiveCosts")
    private double _kidsEffectiveCosts;

    public double getKidsEffectiveCosts() {
        return _kidsEffectiveCosts;
    }

    public void setKidsEffectiveCosts(double kidsEffectiveCosts) {
        _kidsEffectiveCosts = kidsEffectiveCosts;
    }
    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property StaffProofsAgreed">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "spaStaffProofMasterId", referencedColumnName = "spmId")
    private List<StaffProofAgreed> _staffProofAgreed = new Vector<>();

    public List<StaffProofAgreed> getStaffProofsAgreed(PsychType type) {
        return _staffProofAgreed
                .stream()
                .filter(a -> a.getPsychType() == type)
                .sorted((a1, a2) -> a1.getOccupationalCatagoryId() - a2.getOccupationalCatagoryId())
                .collect(Collectors.toList());
    }

    /**
     * Add a StaffProofAgreed to the list
     * @param staffProofAgreed
     * @return true, if the new element could be added; false if the element existed before
     */
    public boolean addStaffProofAgreed(StaffProofAgreed staffProofAgreed) {
        if (staffProofAgreed.getPsychType() == PsychType.Unknown || staffProofAgreed.getOccupationalCatagory() == null){
            throw new IllegalArgumentException("StaffProofAgreed needs a valid PsychType as well as a valid OccupationalCatagory");
        }
        if (_staffProofAgreed.stream()
                .anyMatch(a -> a.getPsychType() == staffProofAgreed.getPsychType() 
                        && a.getOccupationalCatagory() ==  staffProofAgreed.getOccupationalCatagory())){
            return false;
        }
        _staffProofAgreed.add(staffProofAgreed);
        return true;
    }
    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property StaffProofsEffective">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "speStaffProofMasterId", referencedColumnName = "spmId")
    private List<StaffProofEffective> _staffProofEffective = new Vector<>();

    public List<StaffProofEffective> getStaffProofsEffective(PsychType type) {
        return _staffProofEffective
                .stream()
                .filter(a -> a.getPsychType() == type)
                .sorted((e1, e2) -> e1.getOccupationalCatagoryId() - e2.getOccupationalCatagoryId())
                .collect(Collectors.toList());
    }

    /**
     * Add a StaffProofEffective to the list
     * @param staffProofEffective
     * @return true, if the new element could be added; false if the element existed before
     */
    public boolean addStaffProofEffective(StaffProofEffective staffProofEffective) {
        if (staffProofEffective.getPsychType() == PsychType.Unknown || staffProofEffective.getOccupationalCatagory() == null){
            throw new IllegalArgumentException("StaffProofEffective needs a valid PsychType as well as a valid OccupationalCatagory");
        }
        if (_staffProofEffective.stream()
                .anyMatch(a -> a.getPsychType() == staffProofEffective.getPsychType() 
                        && a.getOccupationalCatagory() ==  staffProofEffective.getOccupationalCatagory())){
            return false;
        }
        _staffProofEffective.add(staffProofEffective);
        return true;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof StaffProof)) {
            return false;
        }
        StaffProof other = (StaffProof) obj;

        return _id == other._id;
    }

    @Override
    public String toString() {
        return "StaffProof [id=" + _id + "]";
    }
    // </editor-fold>

    public String getChecksumAgreement() {
        String data = ""
                + getId()
                + getAccountId()
                + getCreated()
                + isForAdults()
                + isForKids()
                + getCalculationType()
                + getAdultsAgreedDays()
                + getKidsAgreedDays()
                + getProofsAgreedData(PsychType.Adults)
                + getProofsAgreedData(PsychType.Kids);
        return Crypt.getHash("SHA1", data);
    }

    private String getProofsAgreedData(PsychType psychType) {
        String data = "" + psychType;
        data =  getStaffProofsAgreed(psychType)
                .stream()
                .map((item) -> ""
                + item.getOccupationalCatagoryId()
                + item.getStaffingComplete()
                + item.getStaffingBudget()
                + item.getAvgCost())
                .reduce(data, String::concat);
        return data;
    }
    
}
