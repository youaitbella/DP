package org.inek.dataportal.base.feature.approval.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "listConfType", schema = "conf")
public class ConfType {
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ctId")
    private Integer id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "ctName")
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    // </editor-fold>

    //<editor-fold desc="equals / hasCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfType confType = (ConfType) o;
        return id.equals(confType.id) &&
                name.equals(confType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
    //</editor-fold>
}
