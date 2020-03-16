package org.inek.dataportal.base.feature.approval.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "listConfState", schema = "conf")
public class ConfState {
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "csId")
    private String id = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "csName")
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
        ConfState confType = (ConfState) o;
        return id.equals(confType.id) &&
                name.equals(confType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
    //</editor-fold>
}
