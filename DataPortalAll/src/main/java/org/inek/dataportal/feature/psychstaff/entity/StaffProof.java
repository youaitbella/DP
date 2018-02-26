package org.inek.dataportal.feature.psychstaff.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.inek.dataportal.entities.iface.StatusEntity;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.feature.psychstaff.enums.PsychType;
import org.inek.dataportal.common.utils.Crypt;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "StaffProofMaster", schema = "psy")
@Cacheable(false)
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

    // <editor-fold defaultstate="collapsed" desc="Property Version">
    @Column(name = "spmVersion")
    @Version
    private int _version;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dataYear">
    @Column(name = "spmYear")
    @Documentation(key = "lblYear")
    private int _year;

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        _year = year;
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
        _ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "spmAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
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
        _created = created;
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
        _lastChanged = lastChanged;
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
        _sealed = sealed;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StatusId">
    @Column(name = "spmStatusId")
    private int _statusId;

    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        _statusId = statusId;
    }

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
    @Documentation(name = "Einrichtung für Erwachsene")
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
    @Documentation(name = "Einrichtung für Kinder- und Jugendpsychiatrie")
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

    // <editor-fold defaultstate="collapsed" desc="Property ExclusionFactId1">
    @Column(name = "spmExclusionFactId1")
    private int _exclusionFactId1 = 0;

    public int getExclusionFactId1() {
        return _exclusionFactId1;
    }

    public void setExclusionFactId1(int exclusionFactId1) {
        _exclusionFactId1 = exclusionFactId1;
    }

    @OneToOne()
    @PrimaryKeyJoinColumn(name = "spmExclusionFactId1")
    private ExclusionFact _exclusionFact1;

    public ExclusionFact getExclusionFact1() {
        return _exclusionFact1;
    }

    public void setExclusionFact1(ExclusionFact exclusionFact1) {
        _exclusionFact1 = exclusionFact1;
        _exclusionFactId1 = exclusionFact1.getId();
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ExclusionReason1">
    @Column(name = "spmExclusionReason1")
    private String _exclusionReason1 = "";

    @Size(max = 250)
    public String getExclusionReason1() {
        return _exclusionReason1;
    }

    public void setExclusionReason1(String exclusionReason1) {
        _exclusionReason1 = exclusionReason1;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ExclusionFactId2">
    @Column(name = "spmExclusionFactId2")
    private int _exclusionFactId2 = 0;

    public int getExclusionFactId2() {
        return _exclusionFactId2;
    }

    public void setExclusionFactId2(int exclusionFactId2) {
        _exclusionFactId2 = exclusionFactId2;
    }

    @OneToOne()
    @PrimaryKeyJoinColumn(name = "spmExclusionFactId2")
    private ExclusionFact _exclusionFact2;

    public ExclusionFact getExclusionFact2() {
        return _exclusionFact2;
    }

    public void setExclusionFact2(ExclusionFact exclusionFact2) {
        _exclusionFact2 = exclusionFact2;
        _exclusionFactId2 = exclusionFact2.getId();
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ExclusionReason2">
    @Column(name = "spmExclusionReason2")
    private String _exclusionReason2 = "";

    @Size(max = 250)
    public String getExclusionReason2() {
        return _exclusionReason2;
    }

    public void setExclusionReason2(String exclusionReason2) {
        _exclusionReason2 = exclusionReason2;
    }
    //</editor-fold>

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

    //<editor-fold defaultstate="collapsed" desc="Property StatusApx1Changed">
    @Column(name = "spmStatusApx1Changed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _statusApx1Changed = new Date();

    public Date getStatusApx1Changed() {
        return _statusApx1Changed;
    }

    public void setStatusApx1Changed(Date statusApx1Changed) {
        _statusApx1Changed = statusApx1Changed;
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="Property StatusApx1">
    @Column(name = "spmStatusApx1")
    private int _statusApx1;

    public int getStatusApx1() {
        return _statusApx1;
    }

    public void setStatusApx1(int statusApx1) {
        if (_statusApx1 != statusApx1) {
            _statusApx1 = statusApx1;
            setSignatureAgreement(calcSignatureAgreement());
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StatusApx2Changed">
    @Column(name = "spmStatusApx2Changed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _statusApx2Changed = new Date();

    public Date getStatusApx2Changed() {
        return _statusApx2Changed;
    }

    public void setStatusApx2Changed(Date statusApx2Changed) {
        _statusApx2Changed = statusApx2Changed;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StatusApx2">
    @Column(name = "spmStatusApx2")
    private int _statusApx2;

    public int getStatusApx2() {
        return _statusApx2;
    }

    public void setStatusApx2(int statusApx2) {
        if (_statusApx2 != statusApx2) {
            _statusApx2 = statusApx2;
            setSignatureEffective(calcSignatureEffective());
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property SignatureAgreement">
    @Column(name = "spmSignatureAgreement")
    @Documentation(name = "Signatur Anlage 1", omitOnEmpty = true)
    private String _signatureAgreement = "";

    public String getSignatureAgreement() {
        return _signatureAgreement;
    }

    private void setSignatureAgreement(String signatureAgreement) {
        _signatureAgreement = signatureAgreement;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property SignatureEffective">
    @Column(name = "spmSignatureEffective")
    @Documentation(name = "Signatur Anlage 2", omitOnEmpty = true)
    private String _signatureEffective = "";

    public String getSignatureEffective() {
        return _signatureEffective;
    }

    private void setSignatureEffective(String signatureEffective) {
        _signatureEffective = signatureEffective;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StaffProofsAgreed">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "spaStaffProofMasterId", referencedColumnName = "spmId")
    private List<StaffProofAgreed> _staffProofAgreed = new Vector<>();

    public List<StaffProofAgreed> getStaffProofsAgreed(PsychType type) {
        return _staffProofAgreed
                .stream()
                .filter(a -> a.getPsychType() == type)
                .sorted((a1, a2) -> a1.getOccupationalCategoryId() - a2.getOccupationalCategoryId())
                .collect(Collectors.toList());
    }

    /**
     * Add a StaffProofAgreed to the list
     *
     * @param staffProofAgreed
     * @return true, if the new element could be added; false if the element existed before
     */
    public boolean addStaffProofAgreed(StaffProofAgreed staffProofAgreed) {
        if (staffProofAgreed.getPsychType() == PsychType.Unknown || staffProofAgreed.getOccupationalCategory() == null) {
            throw new IllegalArgumentException("StaffProofAgreed needs a valid PsychType as well as a valid OccupationalCatagory");
        }
        if (_staffProofAgreed.stream()
                .anyMatch(a -> a.getPsychType() == staffProofAgreed.getPsychType()
                && a.getOccupationalCategory().getId() == staffProofAgreed.getOccupationalCategory().getId())) {
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
                .sorted((e1, e2) -> e1.getOccupationalCategoryId() - e2.getOccupationalCategoryId())
                .collect(Collectors.toList());
    }

    /**
     * Add a StaffProofEffective to the list
     *
     * @param staffProofEffective
     * @return true, if the new element could be added; false if the element existed before
     */
    public boolean addStaffProofEffective(StaffProofEffective staffProofEffective) {
        if (staffProofEffective.getPsychType() == PsychType.Unknown || staffProofEffective.getOccupationalCategory() == null) {
            throw new IllegalArgumentException("StaffProofEffective needs a valid PsychType as well as a valid OccupationalCatagory");
        }
        if (_staffProofEffective.stream()
                .anyMatch(a -> a.getPsychType() == staffProofEffective.getPsychType()
                && a.getOccupationalCategory().getId() == staffProofEffective.getOccupationalCategory().getId())) {
            return false;
        }
        _staffProofEffective.add(staffProofEffective);
        return true;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StaffProofExplanations">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "spxStaffProofMasterId", referencedColumnName = "spmId")
    private List<StaffProofExplanation> _staffProofExplanation = new Vector<>();

    public List<StaffProofExplanation> getStaffProofExplanations(PsychType type) {
        return _staffProofExplanation
                .stream()
                .filter(a -> a.getPsychType() == type)
                .sorted((e1, e2) -> e1.getOccupationalCategoryId() - e2.getOccupationalCategoryId())
                .collect(Collectors.toList());
    }

    /**
     * Add a StaffProofExplanation to the list
     *
     * @param type
     * @param occupationalCategory
     * @param deductedSpecialistId
     * @return true, if the new element could be added; false if the element existed before
     */
    public boolean addMissingStaffProofExplanation(PsychType type, OccupationalCategory occupationalCategory, int deductedSpecialistId) {
        if (_staffProofExplanation.stream().anyMatch(a -> a.getPsychType() == type
                && a.getOccupationalCategoryId() == occupationalCategory.getId()
                && a.getDeductedSpecialistId() == deductedSpecialistId)) {
            return false;
        }
        addStaffProofExplanation(type, occupationalCategory, deductedSpecialistId);
        return true;
    }

    public void addStaffProofExplanation(PsychType type, OccupationalCategory occupationalCategory, int deductedSpecialistId) {
        StaffProofExplanation explanation = new StaffProofExplanation();
        explanation.setStaffProofMasterId(_id);
        explanation.setPsychType(type);
        explanation.setOccupationalCategory(occupationalCategory);
        explanation.setDeductedSpecialistId(deductedSpecialistId);
        _staffProofExplanation.add(explanation);
    }

    public void addStaffProofExplanation(StaffProofExplanation explanation) {
        explanation.setStaffProofMasterId(_id);
        _staffProofExplanation.add(explanation);
    }

    public void removeStaffProofExplanation(PsychType type, OccupationalCategory occupationalCategory, int deductedSpecialistId) {
        _staffProofExplanation.removeIf(a -> a.getPsychType() == type
                && a.getOccupationalCategoryId() == occupationalCategory.getId()
                && a.getDeductedSpecialistId() == deductedSpecialistId);
    }

    public void removeStaffProofExplanation(StaffProofExplanation explanation) {
        _staffProofExplanation.remove(explanation);
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StaffProofDocuments">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "spdStaffProofMasterId", referencedColumnName = "spmId")
    private List<StaffProofDocument> _staffProofDocument = new Vector<>();

    public List<StaffProofDocument> getStaffProofDocuments() {
        return Collections.unmodifiableList(_staffProofDocument);
    }

    public boolean hasStaffProofDocument(int appendix) {
        String signature = appendix == 1 ? _signatureAgreement : _signatureEffective;
        return _staffProofDocument
                .stream()
                .anyMatch(d -> signature.length() > 0 && signature.equals(d.getSignature()));
    }

    public StaffProofDocument getStaffProofDocument(int appendix) {
        if (appendix == 1) {
            return getStaffProofDocument(_signatureAgreement);
        }
        if (appendix == 2) {
            return getStaffProofDocument(_signatureEffective);
        }
        return new StaffProofDocument();
    }

    public StaffProofDocument getStaffProofDocument(String signature) {
        return _staffProofDocument
                .stream()
                .filter(d -> signature.length() > 0 && signature.equals(d.getSignature()))
                .findAny()
                .orElse(new StaffProofDocument());
    }

    public String getStaffProofDocumentName(String signature) {
        return getStaffProofDocument(signature).getName();
    }

    /**
     * Add a StaffProofDocument to the list
     *
     * @param staffProofDocument
     * @return true, if the new element could be added; false if the element existed before
     */
    public boolean addStaffProofDocument(StaffProofDocument staffProofDocument) {
        Optional<StaffProofDocument> findAny = _staffProofDocument
                .stream()
                .filter(d -> staffProofDocument.getSignature().equals(d.getSignature()))
                .findAny();
        if (findAny.isPresent()) {
            StaffProofDocument existing = findAny.get();
            existing.setName(staffProofDocument.getName());
            existing.setContent(staffProofDocument.getContent());
            return false;
        }
        staffProofDocument.setStaffProofMasterId(_id);
        _staffProofDocument.add(staffProofDocument);
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

    public boolean isAnyClosed() {
        return _statusApx1 >= WorkflowStatus.Provided.getId()
                || _statusApx2 >= WorkflowStatus.Provided.getId();
    }

    public boolean isClosed() {
        return _statusApx1 >= WorkflowStatus.Provided.getId() && _statusApx2 >= WorkflowStatus.Provided.getId();
    }

    /**
     * Returns a checksum, if status is at least provided (closed): Otherwise the checksum is empty
     *
     * @return Base64 encoded checksum
     */
    public String calcSignatureAgreement() {
        if (_statusApx1 < WorkflowStatus.Provided.getId()) {
            return "";
        }
        // we use a delimitter to distinguish the concatenation of "cummutative" values
        // eg. id = 1; AccountId = 11, without deli: "111", with deli: "1^11"
        //     id = 11; AccountId = 1, without deli: "111" (as before!), with deli: "11^1"
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        String data = "^"
                + getId() + "^"
                + getAccountId() + "^"
                + getCreated() + "^"
                + getYear() + "^"
                + getCalculationType() + "^"
                + getAdultsAgreedDays() + "^"
                + getKidsAgreedDays() + "^"
                + df.format(getStatusApx1Changed()) + "^"
                + getProofsAgreedData(PsychType.Adults)
                + getProofsAgreedData(PsychType.Kids);
        return Crypt.getHash64("SHA-1", data);  // sha-1 is sufficiant for this purpose and keeps the result short
    }
    private static final String DATE_FORMAT = "yyyyMMddHHmmssSSS";

    private String getProofsAgreedData(PsychType psychType) {
        String data = psychType + "^";
        data = getStaffProofsAgreed(psychType)
                .stream()
                .map((item) -> ""
                + item.getOccupationalCategoryId() + "^"
                + item.getStaffingComplete() + "^"
                + item.getStaffingBudget() + "^"
                + item.getAvgCost() + "^")
                .reduce(data, String::concat);
        return data;
    }

    public String calcSignatureEffective() {
        if (_statusApx2 < WorkflowStatus.Provided.getId()) {
            return "";
        }
        // we use a delimitter to distinguish the concatenation of "cummutative" values
        // eg. id = 1; AccountId = 11, without deli: "111", with deli: "1^11"
        //     id = 11; AccountId = 1, without deli: "111" (as before!), with deli: "11^1"
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        String data = "^"
                + getId() + "^"
                + getAccountId() + "^"
                + getCreated() + "^"
                + getYear() + "^"
                + getCalculationType() + "^"
                + getAdultsEffectiveDays() + "^"
                + getAdultsEffectiveCosts() + "^"
                + getKidsEffectiveDays() + "^"
                + getKidsEffectiveCosts() + "^"
                + df.format(getStatusApx2Changed()) + "^"
                + getProofsEffectiveData(PsychType.Adults)
                + getExplanationData(PsychType.Adults)
                + getProofsEffectiveData(PsychType.Kids)
                + getExplanationData(PsychType.Kids);
        return Crypt.getHash64("SHA-1", data);  // sha-1 is sufficiant for this purpose and keeps the result short
    }

    private String getProofsEffectiveData(PsychType psychType) {
        String data = psychType + "^";
        data = getStaffProofsEffective(psychType)
                .stream()
                .map((item) -> ""
                + item.getOccupationalCategoryId() + "^"
                + item.getStaffingComplete() + "^"
                + item.getStaffingDeductionPsych() + "^"
                + item.getStaffingDeductionNonPsych() + "^"
                + item.getStaffingDeductionOther() + "^")
                .reduce(data, String::concat);
        return data;
    }

    private String getExplanationData(PsychType psychType) {
        String data = psychType + "^";
        data = getStaffProofExplanations(psychType)
                .stream()
                .map((item) -> ""
                + item.getOccupationalCategoryId() + "^"
                + item.getDeductedSpecialistId() + "^"
                + item.getEffectiveOccupationalCategory() + "^"
                + item.getDeductedFullVigor() + "^"
                + item.getExplanation() + "^")
                .reduce(data, String::concat);
        return data;
    }

}
