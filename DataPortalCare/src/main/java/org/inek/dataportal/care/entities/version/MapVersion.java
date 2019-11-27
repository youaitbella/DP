package org.inek.dataportal.care.entities.version;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "listVersion", schema = "adm")
public class MapVersion implements Serializable {

    public MapVersion() {
    }

    public MapVersion(int accountId) {
        this.accountId = accountId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verId")
    private Integer _id = -1;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        this._id = id;
    }

    @Column(name = "verCreatedAt")
    private Date _createdAt = new Date();

    public Date getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this._createdAt = createdAt;
    }

    @Column(name = "verAccountId")
    private int accountId;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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
