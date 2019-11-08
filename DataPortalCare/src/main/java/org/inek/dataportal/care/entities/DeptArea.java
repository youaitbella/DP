package org.inek.dataportal.care.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "listDeptArea", schema = "care")
public class DeptArea {

    //<editor-fold desc="Property Id">
    @Id
    @Column(name = "daId")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    //</editor-fold>


    //<editor-fold desc="Property Text">
    @Column(name = "daText")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeptArea deptArea = (DeptArea) o;
        return id.equals(deptArea.id) &&
                text.equals(deptArea.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }
}
