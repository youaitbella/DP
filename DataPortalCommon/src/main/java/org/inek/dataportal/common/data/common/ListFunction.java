package org.inek.dataportal.common.data.common;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="listFunction")
public class ListFunction {

    private static final long serialVersionUID = 1L;
    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @Column(name = "fuId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "fuName")
    private String _name;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListFunction that = (ListFunction) o;
        return _id == that._id &&
                _name.equals(that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _name);
    }
}
