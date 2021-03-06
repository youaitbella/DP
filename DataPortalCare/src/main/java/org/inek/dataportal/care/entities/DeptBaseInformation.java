package org.inek.dataportal.care.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.care.entities.version.MapVersion;
import org.inek.dataportal.common.data.iface.StatusEntity;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.DateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lautenti
 */
@Entity
@Table(name = "DeptBaseInformation", schema = "care")
public class DeptBaseInformation implements Serializable, StatusEntity {

    private static final long serialVersionUID = 1L;

    public DeptBaseInformation() {

    }

    public DeptBaseInformation(DeptBaseInformation deptBaseInformation, int accountId) {
        this._created = new Date();
        this._createdBy = accountId;
        this._ik = deptBaseInformation.getIk();
        this._year = deptBaseInformation.getYear();
        this._statusId = deptBaseInformation.getStatusId();
        this._send = deptBaseInformation.getSend();
        this._lastChangeBy = deptBaseInformation.getLastChangeBy();
        this._lastChanged = deptBaseInformation.getLastChanged();
        this.currentVersion = deptBaseInformation.getCurrentVersion();
        this.setExtensionRequested(deptBaseInformation.getExtensionRequested());

        for (Dept dept : deptBaseInformation.getDepts()) {
            Dept newDept = new Dept(dept);
            newDept.setBaseInformation(this);
            addDept(newDept);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dbiId")
    private Integer _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Version">
    @Column(name = "dbiVersion")
    @Version
    private int _version;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dataYear">
    @Column(name = "dbiYear")
    private int _year;

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        _year = year;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "dbiIK")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        _ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CreatedBy">
    @Column(name = "dbiCreatedBy")
    private int _createdBy;

    public int getCreatedBy() {
        return _createdBy;
    }

    public void setCreatedBy(int createdBy) {
        _createdBy = createdBy;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Last Change">
    @Column(name = "dbiLastChangeBy")
    private int _lastChangeBy;

    public int getLastChangeBy() {
        return _lastChangeBy;
    }

    public void setLastChangeBy(int lastChangeBy) {
        _lastChangeBy = lastChangeBy;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Created">
    @Column(name = "dbiCreated")
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
    @Column(name = "dbiLastChanged")
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
    @Column(name = "dbiSend")
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
    @Column(name = "dbiStatusId")
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

    //<editor-fold desc="Property CurrentVersion">
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "dbiCurrentVersionId", referencedColumnName = "verId")
    @JsonIgnore
    private MapVersion currentVersion;

    @JsonIgnore
    public MapVersion getCurrentVersion() {
        return currentVersion;
    }

    // for Json only - do not delete
    public int getCurrentVersionId() {
        return currentVersion.getId();
    }

    public void setCurrentVersion(MapVersion mapVersion) {
        this.currentVersion = mapVersion;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ExtensionRequested">
    @Column(name = "dbiExtensionRequested")
    @Temporal(TemporalType.TIMESTAMP)
    private Date extensionRequested = DateUtils.MIN_DATE;

    public Date getExtensionRequested() {
        return extensionRequested;
    }

    public void setExtensionRequested(Date extensionRequested) {
        this.extensionRequested = extensionRequested;
    }
    //</editor-fold>


    @OneToMany(mappedBy = "_baseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "deBaseInformationId")
    @OrderBy("_deptNumber")
    private List<Dept> _depts = new ArrayList<>();

    public List<Dept> getDepts() {
        return _depts;
    }

    public void setDepts(List<Dept> depts) {
        this._depts = depts;
    }

    public void addNewDept() {
        Dept dept = new Dept();
        dept.setBaseInformation(this);
        _depts.add(dept);
    }

    public List<DeptWard> obtainCurrentWards() {
        int versionId = getCurrentVersionId();
        return obtainWardsByVersion(versionId);
    }

    public List<DeptWard> obtainWardsByVersion(int versionId) {
        List<DeptWard> wards = new ArrayList<>();
        for (Dept dept : _depts) {
            wards.addAll(dept.getDeptWards()
                    .stream()
                    .filter(w -> w.getMapVersionId() == versionId)
                    .collect(Collectors.toList()));
        }
        return wards;
    }

    public void removeDept(Dept dept) {
        _depts.remove(dept);
    }

    public void addDept(Dept dept) {
        _depts.add(dept);
    }

}
