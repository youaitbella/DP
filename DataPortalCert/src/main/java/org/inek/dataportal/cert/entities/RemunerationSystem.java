package org.inek.dataportal.cert.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.inek.dataportal.common.enums.RemunSystem;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "System", schema = "crt")
public class RemunerationSystem implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Properties">
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "syId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="RemunerationId">
    @Column(name = "syRemunerationId")
    private int _remunerationId;

    public int getRemunerationId() {
        return _remunerationId;
    }

    public void setRemunerationId(int remunerationId) {
        _remunerationId = remunerationId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="RemunerationSystem">
    public RemunSystem getRemunerationSystem() {
        return RemunSystem.fromId(_remunerationId);
    }

    public void setRemunerationSystem(RemunSystem remunerationSystem) {
        _remunerationId = remunerationSystem.getId();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="YearData">
    @Column(name = "syYearData")
    private int _yearData;

    @Min(value = 2010)
    @Max(2030)
    public int getYearData() {
        return _yearData;
    }

    public void setYearData(int value) {
        _yearData = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="YearSystem">
    @Column(name = "syYearSystem")
    private int _yearSystem;

    @Min(2010)
    @Max(2030)
    public int getYearSystem() {
        return _yearSystem;
    }

    public void setYearSystem(int value) {
        _yearSystem = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Approved">
    @Column(name = "syApproved")
    private boolean _approved = false;

    public boolean isApproved() {
        return _approved;
    }

    public void setApproved(boolean approved) {
        if (_checkList == true && _specManual == true) {
            _approved = approved;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Password">
    @Column(name = "syPassword")
    private String _password = "";

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SpecManual">
    @Column(name = "sySpecManual")
    private boolean _specManual = false;

    public boolean isSpecManual() {
        return _specManual;
    }

    public void setSpecManual(boolean specManual) {
        if (specManual == false && _approved == true) {
            return;
        }
        _specManual = specManual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CheckList">
    @Column(name = "syCheckList")
    private boolean _checkList = false;

    public boolean isCheckList() {
        return _checkList;
    }

    public void setCheckList(boolean checkList) {
        if (checkList == false && _approved == true) {
            return;
        }
        _checkList = checkList;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Archived">
    @Column(name = "syIsActive")
    private boolean _active = false;

    public boolean isActive() {
        return _active;
    }

    public void setActive(boolean active) {
        this._active = active;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="List Grouper">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "grSystemId")
    private List<Grouper> _grouperList = new ArrayList<>();

    public List<Grouper> getGrouperList() {
        return _grouperList;
    }

    public void setGrouperList(List<Grouper> grouperList) {
        _grouperList = grouperList;
    }

    public void removeGrouper(Grouper grouper) {
        _grouperList.remove(grouper);
    }

    public void addGrouper(Grouper grouper) {
        _grouperList.add(grouper);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="DisplayName">
    public String getDisplayName() {
        String yearInfo;
        if (_yearData == _yearSystem) {
            yearInfo = "" + _yearSystem;
        } else {
            yearInfo = _yearData + "/" + _yearSystem;
        }

        return getRemunerationSystem().getName() + " " + yearInfo;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="FileName">
    public String getFileName() {
        return getDisplayName().replace("/", "_");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Version">
    @Column(name = "syVersion")
    @Version
    private int _version;

    public int getVersion() {
        return _version;
    }

    public void setVersion(int version) {
        this._version = version;
    }

    // </editor-fold>
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RemunerationSystem)) {
            return false;
        }
        RemunerationSystem other = (RemunerationSystem) object;
        return _id == other._id;
    }

    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + _id + " ]";
    }
    // </editor-fold>

}
