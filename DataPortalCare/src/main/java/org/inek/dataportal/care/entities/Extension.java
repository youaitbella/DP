package org.inek.dataportal.care.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@NamedQuery(name = "Extension.findByCoordinates",
        query = "select e from Extension e where e.ik = :ik and e.year = :year and e.quarter = :quarter")
@Entity
@Table(name = "Extension", schema = "care")
public class Extension {

    //<editor-fold desc="Property Id">
    @Id
    @Column(name = "exId")
    Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    //</editor-fold>

    //<editor-fold desc="Property Ik">
    @Column(name = "exIk")
    int ik;

    public int getIk() {
        return ik;
    }

    public void setIk(int ik) {
        this.ik = ik;
    }
    //</editor-fold>

    //<editor-fold desc="Property Year">
    @Column(name = "exYear")
    int year;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    //</editor-fold>

    //<editor-fold desc="Property Quarter">
    @Column(name = "exQuarter")
    int quarter;

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }
    //</editor-fold>

    //<editor-fold desc="Property Date">
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "exDate")
    Date date = new Date();
    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Extension extension = (Extension) o;
        return ik == extension.ik &&
                year == extension.year &&
                quarter == extension.quarter &&
                id.equals(extension.id) &&
                date.equals(extension.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ik, year, quarter, date);
    }
}
