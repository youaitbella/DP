package org.inek.dataportal.psy.psychstaff.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;
import org.inek.dataportal.common.data.KhComparison.entities.OccupationalCategory;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "StaffProofExplanation", schema = "psy")
public class StaffProofExplanation implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spxId")
    private int _id;

    public StaffProofExplanation() {
        this._psychType = PsychType.Unknown;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StaffProofMasterId">
    @Column(name = "spxStaffProofMasterId")
    private int _staffProofMasterId;

    public int getStaffProofMasterId() {
        return _staffProofMasterId;
    }

    public void setStaffProofMasterId(int staffProofMasterId) {
        _staffProofMasterId = staffProofMasterId;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PsychType">
    @Column(name = "spxPsychType")
    private PsychType _psychType;

    public PsychType getPsychType() {
        return _psychType;
    }

    public void setPsychType(PsychType psychType) {
        _psychType = psychType;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property OccupationalCategoryId">
    @Column(name = "spxOccupationalCategoryId")
    private int _occupationalCategoryId;

    public int getOccupationalCategoryId() {
        return _occupationalCategoryId;
    }

    public void setOccupationalCategoryId(int occupationalCategoryId) {
        _occupationalCategoryId = occupationalCategoryId;
    }

    @OneToOne()
    @PrimaryKeyJoinColumn(name = "spxOccupationalCategoryId")
    private OccupationalCategory _occupationalCategory;

    public OccupationalCategory getOccupationalCategory() {
        return _occupationalCategory;
    }

    public void setOccupationalCategory(OccupationalCategory occupationalCategory) {
        _occupationalCategory = occupationalCategory;
        _occupationalCategoryId = occupationalCategory.getId();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DeductedSpecialist">
    @Column(name = "spxDeductedSpecialist")
    private int _deductedSpecialistId;

    public int getDeductedSpecialistId() {
        return _deductedSpecialistId;
    }

    public void setDeductedSpecialistId(int deductedSpecialistId) {
        _deductedSpecialistId = deductedSpecialistId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property EffectiveOccupationalCategory">
    @Column(name = "spxEffectiveOccupationalCategory")
    private String _effectiveOccupationalCategory = "";

    @Size(max = 250)
    public String getEffectiveOccupationalCategory() {
        return _effectiveOccupationalCategory;
    }

    public void setEffectiveOccupationalCategory(String effectiveOccupationalCategory) {
        _effectiveOccupationalCategory = effectiveOccupationalCategory;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DeductedFullVigor">
    @Column(name = "spxDeductedFullVigor")
    private double _deductedFullVigor;

    public double getDeductedFullVigor() {
        return _deductedFullVigor;
    }

    public void setDeductedFullVigor(double deductedFullVigor) {
        _deductedFullVigor = deductedFullVigor;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Explanation">
    @Column(name = "spxExplanation")
    private String _explanation = "";

    @Size(max = 2000)
    public String getExplanation() {
        return _explanation;
    }

    public void setExplanation(String explanation) {
        _explanation = explanation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode, equals, toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this._id;
        hash = 37 * hash + Objects.hashCode(this._psychType);
        hash = 37 * hash + this._occupationalCategoryId;
        hash = 37 * hash + this._deductedSpecialistId;
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
        final StaffProofExplanation other = (StaffProofExplanation) obj;
        if (this._id != other._id) {
            return false;
        }
        if (this._occupationalCategoryId != other._occupationalCategoryId) {
            return false;
        }
        if (this._deductedSpecialistId != other._deductedSpecialistId) {
            return false;
        }
        if (this._psychType != other._psychType) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return StaffProofExplanation.class.getName() + " ID [" + _id + "]";
    }
    //</editor-fold>
}
