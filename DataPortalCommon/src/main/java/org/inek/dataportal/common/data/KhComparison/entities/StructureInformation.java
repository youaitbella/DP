package org.inek.dataportal.common.data.KhComparison.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.common.data.converter.StructureInformationCategorieConverter;
import org.inek.dataportal.common.enums.StructureInformationCategorie;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "StructureInformation", schema = "psy")
public class StructureInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "siId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BaseInformation">
    @ManyToOne
    @JoinColumn(name = "siStructureBaseInformationId")
    private StructureBaseInformation _baseInformation;

    @JsonIgnore
    public StructureBaseInformation getBaseInformation() {
        return _baseInformation;
    }

    @JsonIgnore
    public void setBaseInformation(StructureBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
    }

    //Using only for JSON Export
    public int getBaseInformationId() {
        return _baseInformation.getId();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ValidFrom">
    @Column(name = "siValidFrom")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _validFrom = new Date();

    public Date getValidFrom() {
        return _validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this._validFrom = validFrom;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Content">
    @Column(name = "siContent")
    private String _content = "";

    public String getContent() {
        return _content;
    }

    public void setContent(String content) {
        this._content = content;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property siComment">
    @Column(name = "siComment")
    private String _comment = "";

    public String getComment() {
        return _comment;
    }

    public void setComment(String comment) {
        this._comment = comment;
    }

    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Structur Categorie Categorie">
    @Column(name = "siStructureCategorie")
    @Convert(converter = StructureInformationCategorieConverter.class)
    private StructureInformationCategorie _structureCategorie;

    public StructureInformationCategorie getStructureCategorie() {
        return _structureCategorie;
    }

    public void setStructureCategorie(StructureInformationCategorie structureCategorie) {
        this._structureCategorie = structureCategorie;
    }
    // </editor-fold>

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this._id;
        hash = 29 * hash + Objects.hashCode(this._baseInformation);
        hash = 29 * hash + Objects.hashCode(this._validFrom);
        hash = 29 * hash + Objects.hashCode(this._content);
        hash = 29 * hash + Objects.hashCode(this._comment);
        hash = 29 * hash + Objects.hashCode(this._structureCategorie);
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
        final StructureInformation other = (StructureInformation) obj;
        if (this._id != other._id) {
            return false;
        }
        if (!Objects.equals(this._content, other._content)) {
            return false;
        }
        if (!Objects.equals(this._comment, other._comment)) {
            return false;
        }
        if (!Objects.equals(this._baseInformation, other._baseInformation)) {
            return false;
        }
        if (!Objects.equals(this._validFrom, other._validFrom)) {
            return false;
        }
        if (this._structureCategorie != other._structureCategorie) {
            return false;
        }
        return true;
    }

}
