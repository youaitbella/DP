package org.inek.dataportal.psy.khcomparison.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "documents", schema = "psy")
public class PsyDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doId")
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
    @JoinColumn(name = "doBaseInformationId")
    private AEBBaseInformation _baseInformation;

    public AEBBaseInformation getBaseInformation() {
        return _baseInformation;
    }

    public void setBaseInformation(AEBBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "doName")
    private String _name = "";

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Description">
    @Column(name = "doDescription")
    private String _description = "";

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CaseCount">
    @Lob
    @Column(name = "doContent")
    private byte[] _content;

    public byte[] getContent() {
        return _content;
    }

    public void setContent(byte[] content) {
        this._content = content;
    }
    //</editor-fold>

    public String getContentTyp() {
        String[] content = _name.split("\\.");
        return content[content.length - 1];
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this._baseInformation);
        hash = 37 * hash + Objects.hashCode(this._name);
        hash = 37 * hash + Objects.hashCode(this._description);
        hash = 37 * hash + Arrays.hashCode(this._content);
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
        final PsyDocument other = (PsyDocument) obj;
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
        if (!Arrays.equals(this._content, other._content)) {
            return false;
        }
        return true;
    }

}
