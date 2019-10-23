package org.inek.dataportal.care.entities.version;

import org.inek.dataportal.care.entities.DeptStation;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "mapVersion", schema = "adm")
public class MapVersion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mvVersion")
    private Integer _id;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        this._id = id;
    }

    @Column(name = "mvDate")
    private Date _createdAt = new Date();

    public Date getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this._createdAt = createdAt;
    }

    @OneToOne(mappedBy = "_mapVersion")
    private DeptStation _deptStation;

    public DeptStation getDeptStation() {
        return _deptStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapVersion that = (MapVersion) o;
        return Objects.equals(_id, that._id) &&
                Objects.equals(_createdAt, that._createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _createdAt);
    }
}
