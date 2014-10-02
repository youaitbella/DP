package org.inek.dataportal.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "DropBox")
public class DropBox implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dbId")
    private Integer _dropBoxId;
    @JoinColumn(name = "dbTypeId")
    @OneToOne
    private DropBoxType _dropboxType;
    @Column(name = "dbAccountId")
    private Integer _accountId;
    @Column(name = "dbIk")
    private Integer _IK;
    @Column(name = "dbIsComplete")
    private boolean _isComplete;
    @Column(name = "dbDirectory")
    private String _directory;
    @Column(name = "dbDescription")
    private String _description;
    @Column(name = "dbValidUntil")
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date _validUntil;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dbiDropBoxId", referencedColumnName = "dbId")
    @OrderBy("_name")
    private List<DropBoxItem> _items;

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Integer getDropBoxId() {
        return _dropBoxId;
    }

    public void setDropBoxId(Integer id) {
        _dropBoxId = id;
    }

    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer accountId) {
        _accountId = accountId;
    }

    public DropBoxType getDropboxType() {
        return _dropboxType;
    }

    public void setDropboxType(DropBoxType dropboxType) {
        _dropboxType = dropboxType;
    }

    public Integer getIK() {
        return _IK;
    }

    public void setIK(Integer IK) {
        _IK = IK;
    }

    public boolean isComplete() {
        return _isComplete;
    }

    public void setComplete(boolean isComplete) {
        _isComplete = isComplete;
    }

    public String getDirectory() {
        return _directory;
    }

    public void setDirectory(String directory) {
        _directory = directory == null ? "" : directory;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public Date getValidUntil() {
        return _validUntil;
    }

    public void setValidUntil(Date creationDate) {
        _validUntil = creationDate;
    }

    public List<DropBoxItem> getItems() {
        if (_items == null) {
            _items = new ArrayList<>();
        }
        return _items;
    }

    public void setItems(List<DropBoxItem> items) {
        _items = items;
    }

    // </editor-fold>
    @PrePersist
    private void setValidUntil() {
        Calendar cal = Calendar.getInstance();
        if (_dropboxType.getValidity() <= 0) {
            // set to defined hour of next day, e.g -3 -> 3:00
            int hour = Math.min(-_dropboxType.getValidity(), 23);
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.add(Calendar.DAY_OF_YEAR, 1);
        } else {
            cal.add(Calendar.HOUR, _dropboxType.getValidity());
        }
        _validUntil = cal.getTime();
    }

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_dropBoxId != null ? _dropBoxId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DropBox)) {
            return false;
        }
        DropBox other = (DropBox) object;
        if ((_dropBoxId == null && other.getDropBoxId() != null) || (_dropBoxId != null && !_dropBoxId.equals(other.getDropBoxId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.DropBox[id=" + _dropBoxId + "]";
    }
    // </editor-fold>

}
