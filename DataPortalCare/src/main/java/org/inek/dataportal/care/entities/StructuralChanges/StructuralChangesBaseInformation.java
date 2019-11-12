package org.inek.dataportal.care.entities.StructuralChanges;

import org.inek.dataportal.care.enums.StructuralChangesDetailType;
import org.inek.dataportal.care.enums.StructuralChangesType;
import org.inek.dataportal.common.enums.WorkflowStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "StructuralChangesBaseInformation", schema = "care")
public class StructuralChangesBaseInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    public StructuralChangesBaseInformation() {
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scId")
    private Integer _id;

    public Integer getId() {
        return _id == null ? -1 : _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "scIk")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(Integer ik) {
        _ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StatusId">
    @Column(name = "scStatusId")
    private int _statusId;

    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    public void setStatus(WorkflowStatus status) {
        this._statusId = status.getId();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property RequestedAt">
    @Column(name = "scRequestedAt")
    private Date _requestedAt;

    public Date getRequestedAt() {
        return _requestedAt;
    }

    public void setRequestedAt(Date requestedAt) {
        this._requestedAt = requestedAt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property RequestedAccountId">
    @Column(name = "scRequestedAccountId")
    private int _requestedAccountId;

    public int getRequestedAccountId() {
        return _requestedAccountId;
    }

    public void setRequestedAccountId(int requestedAccountId) {
        this._requestedAccountId = requestedAccountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AgentId">
    @Column(name = "scAgentId")
    private Integer _agentId;

    public Integer getAgendId() {
        return _agentId;
    }

    public void setAgendId(int agentId) {
        this._agentId = agentId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ProcessedAt">
    @Column(name = "scProcessedAt")
    private Date _processedAt = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    public Date getPocessedAt() {
        return _processedAt;
    }

    public void setProcessedAt(Date processedAt) {
        this._processedAt = processedAt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StructuralChangesType">
    @Column(name = "scType")
    private int _structuralChangesTypeId;

    public StructuralChangesType getStructuralChangesType() {
        return StructuralChangesType.getById(_structuralChangesTypeId);
    }

    public void setStructuralChangesType(StructuralChangesType structuralChangesType) {
        this._structuralChangesTypeId = structuralChangesType.getId();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StructuralChangesDetailType">
    @Column(name = "scStructuralChangesDetailTypeId")
    private Integer _structuralChangesDetailTypeId;

    public StructuralChangesDetailType getStructuralChangesDetailType() {
        return StructuralChangesDetailType.getById(_structuralChangesDetailTypeId);
    }

    public void setStructuralChangesDetailType(StructuralChangesDetailType structuralChangesDetailType) {
        this._structuralChangesDetailTypeId = structuralChangesDetailType.getId();
    }

    public Integer getStructuralChangesDetailTypeId() {
        return _structuralChangesDetailTypeId;
    }

    public void setStructuralChangesDetailTypeId(int structuralChangesDetailTypeId) {
        this._structuralChangesDetailTypeId = structuralChangesDetailTypeId;
    }

    //</editor-fold>


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "scWardsToChangeId", referencedColumnName = "wtcId")
    private WardsToChange _wardsToChange;

    public WardsToChange getWardsToChange() {
        return _wardsToChange;
    }

    public void setWardsToChange(WardsToChange wardsToChange) {
        wardsToChange.setStructuralChangesBaseInformation(this);
        this._wardsToChange = wardsToChange;
    }


    @OneToMany(mappedBy = "_structuralChangesBaseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "scwStructuralChangesId")
    private List<StructuralChangesWards> _structuralChangesWards = new ArrayList<>();

    public List<StructuralChangesWards> getStructuralChangesWards() {
        return _structuralChangesWards;
    }

    public void setStructuralChangesWards(List<StructuralChangesWards> structuralChangesWards) {
        this._structuralChangesWards = structuralChangesWards;
    }

    public void addStructuralChangesWards(StructuralChangesWards scw) {
        scw.setStructuralChangesBaseInformation(this);
        _structuralChangesWards.add(scw);
    }

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructuralChangesBaseInformation that = (StructuralChangesBaseInformation) o;
        return _ik == that._ik &&
                _statusId == that._statusId &&
                _requestedAccountId == that._requestedAccountId &&
                _agentId == that._agentId &&
                _structuralChangesTypeId == that._structuralChangesTypeId &&
                Objects.equals(_id, that._id) &&
                Objects.equals(_requestedAt, that._requestedAt) &&
                Objects.equals(_processedAt, that._processedAt) &&
                Objects.equals(_wardsToChange, that._wardsToChange) &&
                Objects.equals(_structuralChangesWards, that._structuralChangesWards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _ik, _statusId, _requestedAt, _requestedAccountId, _agentId, _processedAt,
                _structuralChangesTypeId);
    }
}
