package org.inek.dataportal.common.data.KhComparison.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "PersonalAgreed", schema = "psy")
public class PersonalAgreed implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paId")
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
    @JoinColumn(name = "paBaseInformationId")
    private AEBBaseInformation _baseInformation;

    public AEBBaseInformation getBaseInformation() {
        return _baseInformation;
    }

    public void setBaseInformation(AEBBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="OccupationalCategory">
    @Column(name = "paOccupationalCategoryId")
    private int _occupationalCategoryId;

    public int getOccupationalCategoryId() {
        return _occupationalCategoryId;
    }

    public void setOccupationalCategoryId(int occupationalCategoryId) {
        _occupationalCategoryId = occupationalCategoryId;
    }

    @OneToOne()
    @PrimaryKeyJoinColumn(name = "paOccupationalCategoryId")
    private OccupationalCategory _occupationalCategory;

    public OccupationalCategory getOccupationalCategory() {
        return _occupationalCategory;
    }

    public void setOccupationalCategory(OccupationalCategory occupationalCategory) {
        _occupationalCategory = occupationalCategory;
        _occupationalCategoryId = occupationalCategory.getId();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Complete">
    @Column(name = "paComplete")
    private double _complete;

    public double getComplete() {
        return _complete;
    }

    public void setComplete(double complete) {
        _complete = complete;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AverageCost">
    @Column(name = "paAverageCost")
    private double _averageCost;

    public double getAverageCost() {
        return _averageCost;
    }

    public void setAverageCost(double averageCost) {
        _averageCost = averageCost;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this._baseInformation);
        hash = 43 * hash + Objects.hashCode(this._occupationalCategory);
        hash = 43 * hash + (int) (Double.doubleToLongBits(this._complete) ^ (Double.doubleToLongBits(this._complete) >>> 32));
        hash = 43 * hash + (int) (Double.doubleToLongBits(this._averageCost) ^ (Double.doubleToLongBits(this._averageCost) >>> 32));
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
        final PersonalAgreed other = (PersonalAgreed) obj;
        if (Double.doubleToLongBits(this._complete) != Double.doubleToLongBits(other._complete)) {
            return false;
        }
        if (Double.doubleToLongBits(this._averageCost) != Double.doubleToLongBits(other._averageCost)) {
            return false;
        }
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        if (!Objects.equals(this._baseInformation, other._baseInformation)) {
            return false;
        }
        if (!Objects.equals(this._occupationalCategory, other._occupationalCategory)) {
            return false;
        }
        return true;
    }

}
