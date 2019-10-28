package org.inek.dataportal.common.data.KhComparison.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "RegionStructurParticularities", schema = "psy")
public class RegionStructurParticularities implements Serializable {

    private static final long serialVersionUID = 1L;

    public RegionStructurParticularities() {
    }

    public RegionStructurParticularities(RegionStructurParticularities structure) {
        this._structureCategorieId = structure.getStructureCategorieId();
        this._name = structure.getName();
        this._description = structure.getDescription();
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rspId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BaseInformation">
    @ManyToOne
    @JoinColumn(name = "rspBaseInformationId")
    private AEBBaseInformation _baseInformation;

    @JsonIgnore
    public AEBBaseInformation getBaseInformation() {
        return _baseInformation;
    }
    @JsonIgnore
    public void setBaseInformation(AEBBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
    }

    //Using only for JSON Export
    public int getBaseInformationId() {
        return _baseInformation.getId();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StructureCategorieId">
    @Column(name = "rspStructureCategorieId")
    private int _structureCategorieId;

    public int getStructureCategorieId() {
        return _structureCategorieId;
    }

    public void setStructureCategorieId(int structureCategorieId) {
        this._structureCategorieId = structureCategorieId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "rspName")
    private String _name = "";

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Description">
    @Column(name = "rspDescription")
    private String _description = "";

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this._baseInformation);
        hash = 23 * hash + this._structureCategorieId;
        hash = 23 * hash + Objects.hashCode(this._name);
        hash = 23 * hash + Objects.hashCode(this._description);
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
        final RegionStructurParticularities other = (RegionStructurParticularities) obj;
        if (this._structureCategorieId != other._structureCategorieId) {
            return false;
        }
        if (!Objects.equals(this._name, other._name)) {
            return false;
        }
        if (!Objects.equals(this._description, other._description)) {
            return false;
        }
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        if (!Objects.equals(this._baseInformation, other._baseInformation)) {
            return false;
        }
        return true;
    }

}
