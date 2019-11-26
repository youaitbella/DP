package org.inek.dataportal.care.entities.StructuralChanges;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.common.enums.WorkflowStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    public void setIk(int ik) {
        _ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StatusId">
    @Column(name = "scStatusId")
    private int _statusId;

    @JsonIgnore
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    @JsonIgnore
    public void setStatus(WorkflowStatus status) {
        this._statusId = status.getId();
    }

    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        this._statusId = statusId;
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

    //<editor-fold desc="Property BasedOnVersionId">
    @Column(name = "scBasedOnVersionId")
    private int basedOnVersionId;

    public int getBasedOnVersionId() {
        return basedOnVersionId;
    }

    public void setBasedOnVersionId(int basedOnVersionId) {
        this.basedOnVersionId = basedOnVersionId;
    }
    //</editor-fold>

    @OneToMany(mappedBy = "_structuralChangesBaseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "scStructuralChangesBaseInformationId")
    @JsonIgnore
    private List<StructuralChanges> _structuralChanges = new ArrayList<>();

    @JsonIgnore
    public List<StructuralChanges> getStructuralChanges() {
        return _structuralChanges;
    }

    @JsonIgnore
    public void setStructuralChanges(List<StructuralChanges> structuralChanges) {
        this._structuralChanges = structuralChanges;
    }

    @JsonIgnore
    public void addStructuralChanges(StructuralChanges sc) {
        sc.setStructuralChangesBaseInformation(this);
        _structuralChanges.add(sc);
    }

    @JsonIgnore
    public void removeStructuralChanges(StructuralChanges change) {
        _structuralChanges.remove(change);
    }

    @JsonIgnore
    public String getStatusText() {
        /*List<WorkflowStatus> states = _structuralChanges.stream().map(StructuralChanges::getStatus).collect(Collectors.toList());

        if (states.stream().allMatch(c -> c.equals(WorkflowStatus.New))) {
            return "In Erfassung durch Krankenhaus";
        }

        if (states.stream().allMatch(c -> c.equals(WorkflowStatus.Provided))) {
            return "In Prüfung";
        }*/

        if (getStatus().equals(WorkflowStatus.New)) {
            return "In Erfassung durch Krankenhaus";
        } else if (getStatus().equals(WorkflowStatus.Provided)) {
            return "in Bearbeitung durch das InEK";
        }
        return "Unbekannt";

    }
}
