package org.inek.dataportal.psy.khcomparison.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.inek.dataportal.psy.psychstaff.entity.OccupationalCategory;

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
    @OneToOne()
    @PrimaryKeyJoinColumn(name = "paOccupationalCategoryId")
    private OccupationalCategory _occupationalCategory;

    public OccupationalCategory getOccupationalCategory() {
        return _occupationalCategory;
    }

    public void setOccupationalCategory(OccupationalCategory occupationalCategory) {
        _occupationalCategory = occupationalCategory;
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

    //<editor-fold defaultstate="collapsed" desc="Property Budget">
    @Column(name = "paBudget")
    private double _budget;

    public double getBudget() {
        return _budget;
    }

    public void setBudget(double Budget) {
        _budget = Budget;
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

}
