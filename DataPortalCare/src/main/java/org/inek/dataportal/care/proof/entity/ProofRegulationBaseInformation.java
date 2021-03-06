package org.inek.dataportal.care.proof.entity;

import org.inek.dataportal.common.data.iface.StatusEntity;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.DateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.*;

@Entity
@Table(name = "ProofRegulationBaseInformation", schema = "care")
public class ProofRegulationBaseInformation implements Serializable, StatusEntity {

    private static final long serialVersionUID = 1L;

    public ProofRegulationBaseInformation() {
    }

    public ProofRegulationBaseInformation(ProofRegulationBaseInformation baseInformation) {
        this._created = baseInformation.getCreated();
        this._createdBy = baseInformation.getCreatedBy();
        this._ik = baseInformation.getIk();
        this._year = baseInformation.getYear();
        this._quarter = baseInformation.getQuarter();
        this._signature = baseInformation.getSignature();
        this._statusId = baseInformation.getStatusId();
        this._send = baseInformation.getSend();
        this._lastChangeBy = baseInformation.getLastChangeBy();
        this._lastChanged = baseInformation.getLastChanged();
        extensionRequestedBy = baseInformation.getExtensionRequestedBy();
        extensionRequestedAt = baseInformation.getExtensionRequestedAt();

        for (Proof proof : baseInformation.getProofs()) {
            Proof newProof = new Proof(proof);
            newProof.setBaseInformation(this);
            addProof(newProof);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prbiId")
    private Integer _id;

    public int getId() {
        return _id == null ? -1 : _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Version">
    @Column(name = "prbiVersion")
    @Version
    private int _version;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dataYear">
    @Column(name = "prbiYear")
    private int _year;

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        _year = year;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Quarter">
    @Column(name = "prbiQuarter")
    private int _quarter;

    public int getQuarter() {
        return _quarter;
    }

    public void setQuarter(int quarter) {
        _quarter = quarter;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Signature">
    @Column(name = "prbiSignature")
    private String _signature = "";

    public String getSignature() {
        return _signature;
    }

    public void setSignature(String signature) {
        _signature = signature;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "prbiIK")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        _ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CreatedBy">
    @Column(name = "prbiCreatedBy")
    private int _createdBy;

    public int getCreatedBy() {
        return _createdBy;
    }

    public void setCreatedBy(int createdBy) {
        _createdBy = createdBy;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Last Change">
    @Column(name = "prbiLastChangeBy")
    private int _lastChangeBy;

    public int getLastChangeBy() {
        return _lastChangeBy;
    }

    public void setLastChangeBy(int lastChangeBy) {
        _lastChangeBy = lastChangeBy;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Created">
    @Column(name = "prbiCreated")
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
    @Column(name = "prbiLastChanged")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastChanged = new Date();

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        _lastChanged = lastChanged;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Send">
    @Column(name = "prbiSend")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _send = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    public Date getSend() {
        return _send;
    }

    public void setSend(Date send) {
        _send = send;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StatusId">
    @Column(name = "prbiStatusId")
    private int _statusId;

    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        _statusId = statusId;
    }

    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    public void setStatus(WorkflowStatus status) {
        _statusId = status.getId();
    }
    //</editor-fold>

    //<editor-fold desc="Property ExtensionRequestedBy">
    @Column(name = "prbiExtensionRequestedBy")
    private int extensionRequestedBy;

    public int getExtensionRequestedBy() {
        return extensionRequestedBy;
    }

    public void setExtensionRequestedBy(int extensionRequestedBy) {
        this.extensionRequestedBy = extensionRequestedBy;
    }
    //</editor-fold>

    //<editor-fold desc="Property ExtensionRequestedAt">
    @Column(name = "prbiExtensionRequestedAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date extensionRequestedAt = DateUtils.MIN_DATE;

    public Date getExtensionRequestedAt() {
        return extensionRequestedAt;
    }

    public void setExtensionRequestedAt(Date extensionRequestedAt) {
        this.extensionRequestedAt = extensionRequestedAt;
    }
    //</editor-fold>

    //<editor-fold desc="Property Proofs">
    @OneToMany(mappedBy = "_baseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "prProofRegulationBaseInformationId")
    @OrderBy(value = "significantSensitiveDomain, deptNumbers, deptNames, _proofWard, _month, _shift DESC")
    private List<Proof> _proofs = new ArrayList<>();

    public List<Proof> getProofs() {
        return Collections.unmodifiableList(_proofs);
    }

    public void removeProof(Proof proof) {
        _proofs.remove(proof);
    }

    public void removeProofs(Collection<Proof> proofs) {
        _proofs.removeAll(proofs);
    }

    public void addProof(Proof proof) {
        _proofs.add(proof);
    }
    //</editor-fold>

    @SuppressWarnings("CyclomaticComplexity")
    public boolean contentEquals(ProofRegulationBaseInformation that) {
        if (this == that) return true;
        if (that == null) return false;
        if (_year != that._year
                || _quarter != that._quarter
                || _ik != that._ik
                || _statusId != that._statusId
                || !_signature.equals(that._signature)
                || !extensionRequestedAt.equals(that.extensionRequestedAt)
                || _proofs.size() != that._proofs.size()) {
            return false;
        }

        return _proofs.stream().allMatch(p -> that._proofs.stream().anyMatch(op -> op.contentEquals(p)));
    }


    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProofRegulationBaseInformation that = (ProofRegulationBaseInformation) o;
        return _version == that._version &&
                _year == that._year &&
                _quarter == that._quarter &&
                _ik == that._ik &&
                _createdBy == that._createdBy &&
                _lastChangeBy == that._lastChangeBy &&
                _statusId == that._statusId &&
                extensionRequestedBy == that.extensionRequestedBy &&
                _id.equals(that._id) &&
                _signature.equals(that._signature) &&
                _created.equals(that._created) &&
                _lastChanged.equals(that._lastChanged) &&
                _send.equals(that._send) &&
                extensionRequestedAt.equals(that.extensionRequestedAt) &&
                _proofs.equals(that._proofs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _version, _year, _quarter, _signature, _ik, _createdBy, _lastChangeBy, _created,
                _lastChanged, _send, _statusId, extensionRequestedBy, extensionRequestedAt, _proofs);
    }
}
