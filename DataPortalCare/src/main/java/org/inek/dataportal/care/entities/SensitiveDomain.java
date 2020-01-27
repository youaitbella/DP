package org.inek.dataportal.care.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "listSensitiveDomain", schema = "care")
public class SensitiveDomain {

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sdId")
    private Integer id;

    public int getId() {
        return id;
    }

    public void setId(Integer val) {
        id = val;
    }
    // </editor-fold>

    //<editor-fold desc="Property CharId">
    @Column(name = "sdCharId")
    private String charId;

    public String getCharId() {
        return charId;
    }

    public void setCharId(String val) {
        charId = val;
    }
    //</editor-fold>

    //<editor-fold desc="Property Name">
    @Column(name = "sdName")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String val) {
        name = val;
    }
    //</editor-fold>

    //<editor-fold desc="Property IsPartOf">
    @Column(name = "sdIsPartOf")
    private int isPartOf;

    public int getIsPartOf() {
        return isPartOf;
    }

    public void setIsPartOf(int isPartOf) {
        this.isPartOf = isPartOf;
    }
    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensitiveDomain that = (SensitiveDomain) o;
        return isPartOf == that.isPartOf &&
                id.equals(that.id) &&
                charId.equals(that.charId) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, charId, name, isPartOf);
    }
}
