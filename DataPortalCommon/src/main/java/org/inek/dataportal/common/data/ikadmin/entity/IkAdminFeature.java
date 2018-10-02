package org.inek.dataportal.common.data.ikadmin.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.converter.FeatureConverter;

@Entity
@Table(name = "IkAdminFeature", schema = "ikadm")
public class IkAdminFeature implements Serializable {

    private static final long serialVersionUID = 1L;

    public IkAdminFeature() {
    }

    public IkAdminFeature(IkAdmin ikAdmin, Feature feature) {
        _ikAdmin = ikAdmin;
        _feature = feature;
    }

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iafId")
    private Integer _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IkAdminId">
    @ManyToOne
    @JoinColumn(name = "iafIkAdminId")
    private IkAdmin _ikAdmin;

    public IkAdmin getIkAdmin() {
        return _ikAdmin;
    }

    public void setIkAdmin(IkAdmin ikAdmin) {
        this._ikAdmin = ikAdmin;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Feature">
    @Id
    @Column(name = "iafFeatureId")
    @Convert(converter = FeatureConverter.class)
    private Feature _feature;

    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        _feature = feature;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode & equals">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this._id);
        hash = 97 * hash + Objects.hashCode(this._ikAdmin);
        hash = 97 * hash + Objects.hashCode(this._feature);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IkAdminFeature other = (IkAdminFeature) obj;
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        if (!Objects.equals(this._ikAdmin, other._ikAdmin)) {
            return false;
        }
        if (this._feature != other._feature) {
            return false;
        }
        return true;
    }
    // </editor-fold>
}
