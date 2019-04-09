package org.inek.dataportal.calc.entities.drg;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "listServiceArea", schema = "dbo")
public class KGLListServiceArea implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    @Column(name = "saName")
    private String _name ="";

    public String getName() {
        return _name;
    }

    public KGLListServiceArea() {
    }

    public KGLListServiceArea(String _name) {
        this._name = _name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KGLListServiceArea)) return false;
        KGLListServiceArea that = (KGLListServiceArea) o;
        return _id == that._id &&
                Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _name);
    }
}
