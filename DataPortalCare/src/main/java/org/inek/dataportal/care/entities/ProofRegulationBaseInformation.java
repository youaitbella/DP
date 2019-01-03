package org.inek.dataportal.care.entities;

import org.inek.dataportal.common.data.iface.StatusEntity;
import org.inek.dataportal.common.enums.WorkflowStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lautenti
 */
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

    @OneToMany(mappedBy = "_baseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "prProofRegulationBaseInformationId")
    private List<Proof> _proofs = new ArrayList<>();

    public List<Proof> getProofs() {
        return _proofs;
    }

    public void setProofs(List<Proof> proofs) {
        this._proofs = proofs;
    }

    public void addNewProof() {
        Proof proof = new Proof();
        proof.setBaseInformation(this);
        _proofs.add(proof);
    }

    public void removeProof(Proof proof) {
        _proofs.remove(proof);
    }

    public void addProof(Proof proof) {
        _proofs.add(proof);
    }
}
