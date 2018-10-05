package org.inek.dataportal.common.data.KhComparison.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import org.inek.dataportal.common.enums.StructureInformationCategorie;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "StructureBaseInformation", schema = "psy")
public class StructureBaseInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sbiId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Version">
    @Column(name = "sbiVersion")
    @Version
    private int _version;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "sbiIk")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        _ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CreatedBy">
    @Column(name = "sbiCreatedBy")
    private int _createdBy;

    public int getCreatedBy() {
        return _createdBy;
    }

    public void setCreatedBy(int createdBy) {
        _createdBy = createdBy;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Last Change By">
    @Column(name = "sbiLastChangeBy")
    private int _lastChangeBy;

    public int getLastChangeBy() {
        return _lastChangeBy;
    }

    public void setLastChangeBy(int lastChangeBy) {
        _lastChangeBy = lastChangeBy;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "sbiLastChanged")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastChanged = new Date();

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        _lastChanged = lastChanged;
    }
    //</editor-fold>

    @OneToMany(mappedBy = "_baseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "siStructureBaseInformationId")
    private List<StructureInformation> _structureInformations = new ArrayList<>();

    public List<StructureInformation> getStructureInformations() {
        return _structureInformations;
    }

    public void setStructureInformations(List<StructureInformation> structureInformations) {
        this._structureInformations = structureInformations;
    }

    public void addNewStructureInformation(StructureInformationCategorie cat) {
        StructureInformation info = new StructureInformation();
        info.setBaseInformation(this);
        info.setStructureCategorie(cat);
        _structureInformations.add(info);
    }

    public void addStructureInformation(StructureInformation info) {
        info.setBaseInformation(this);
        _structureInformations.add(info);
    }

}
