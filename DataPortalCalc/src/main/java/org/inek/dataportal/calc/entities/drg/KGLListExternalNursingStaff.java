package org.inek.dataportal.calc.entities.drg;

import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "KGLListExternalNursingStaff", schema = "calc")
@XmlRootElement
public class KGLListExternalNursingStaff implements Serializable {
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ensID", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Bereich/Station">
    @Column(name = "ensDivision")
    @Documentation(name = "Bereich", rank = 10)
    private String _divisionNursingStaff="";

    public String getDivisionNursingStaff() {
        return _divisionNursingStaff;
    }

    public void setDivisionNursingStaff(String divisionNursingStaff) {
        this._divisionNursingStaff = divisionNursingStaff;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Anzahl VK">
    @Column(name = "ensCount")
    private double _countNursingStaff;

    public double getCountNursingStaff() {
        return _countNursingStaff;
    }

    public void setCountNursingStaff(double countNursingStaff) {
        this._countNursingStaff = countNursingStaff;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Kostenvolumen, welches gem. §6a Abs. 2 KHEntgG zur Ermittlung des Pflegebudgets berücksichtigt werden darf:">
    @Column(name = "ensConsideredCostVolume")
    private double _consideredCostVolumeNursingStaff;

    public double getConsideredCostVolumeNursingStaff() {
        return _consideredCostVolumeNursingStaff;
    }

    public void setConsideredCostVolumeNursingStaff(double consideredCostVolumeNursingStaff) {
        this._consideredCostVolumeNursingStaff = consideredCostVolumeNursingStaff;
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Kostenvolumen, welches nicht gem. §6a Abs. 2 KHEntgG im Pflegebudget berücksichtigt werden darf (i):">
    @Column(name = "ensNotConsideredCostVolume")
    private double _notConsideredCostVolumeNursingStaff;

    public double getNotConsideredCostVolumeNursingStaff() {
        return _notConsideredCostVolumeNursingStaff;
    }

    public void setNotConsideredCostVolumeNursingStaff(double NotConsideredCostVolumeNursingStaff) {
        this._notConsideredCostVolumeNursingStaff = NotConsideredCostVolumeNursingStaff;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Kosten je VK">
    @Column(name = "ensCostPerFullTime")
    private double _costPerFullTimeNursingStaff;

    public double getCostPerFullTimeNursingStaff() {
        return _costPerFullTimeNursingStaff;
    }

    public void setCostPerFullTimeNursingStaff(double costPerFullTimeNursingStaff) {
        this._costPerFullTimeNursingStaff = costPerFullTimeNursingStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoStGr">
    @Column(name = "ensKoStGr")
    private double _costStGrNursingStaff;

    public double getCostStGrNursingStaff() {
        return _costStGrNursingStaff;
    }

    public void setCostStGrNursingStaff(double costStGrNursingStaff) {
        this._costStGrNursingStaff = costStGrNursingStaff;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoArtGr">
    @Column(name = "ensKoArtGr")
    private double _costKoArtGrNursingStaff;

    public double getCostKoArtGrNursingStaff() {
        return _costKoArtGrNursingStaff;
    }

    public void setCostKoArtGrNursingStaff(double costKoArtGrNursingStaff) {
        this._costKoArtGrNursingStaff = costKoArtGrNursingStaff;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Erläuterungsfeld">
    @Column(name = "ensExplanationField")
    private String _explanationFieldNursingStaff="";

    public String getExplanationFieldNursingStaff() {
        return _explanationFieldNursingStaff;
    }

    public void setExplanationFieldNursingStaff(String explanationFieldNursingStaff) {
        this._explanationFieldNursingStaff = explanationFieldNursingStaff;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="_baseInformationId">
    @Column(name = "ensBaseInformationId")
    private int _baseInformationIdNursingStaff;

    public int get_baseInformationIdNursingStaff() {
        return _baseInformationIdNursingStaff;
    }

    public void set_baseInformationIdNursingStaff(int _baseInformationIdNursingStaff) {
        this._baseInformationIdNursingStaff = _baseInformationIdNursingStaff;
    }
    // </editor-fold>


    public KGLListExternalNursingStaff() {
    }

    public KGLListExternalNursingStaff(int _baseInformationIdNursingStaff) {
        this._baseInformationIdNursingStaff = _baseInformationIdNursingStaff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KGLListExternalNursingStaff)) return false;
        KGLListExternalNursingStaff that = (KGLListExternalNursingStaff) o;
        return _id == that._id &&
                Double.compare(that._countNursingStaff, _countNursingStaff) == 0 &&
                Double.compare(that.getConsideredCostVolumeNursingStaff(), getConsideredCostVolumeNursingStaff()) == 0 &&
                Double.compare(that._notConsideredCostVolumeNursingStaff, _notConsideredCostVolumeNursingStaff) == 0 &&
                Double.compare(that._costPerFullTimeNursingStaff, _costPerFullTimeNursingStaff) == 0 &&
                Double.compare(that._costStGrNursingStaff, _costStGrNursingStaff) == 0 &&
                Double.compare(that._costKoArtGrNursingStaff, _costKoArtGrNursingStaff) == 0 &&
                get_baseInformationIdNursingStaff() == that.get_baseInformationIdNursingStaff() &&
                Objects.equals(_divisionNursingStaff, that._divisionNursingStaff) &&
                Objects.equals(_explanationFieldNursingStaff, that._explanationFieldNursingStaff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _divisionNursingStaff, _countNursingStaff, getConsideredCostVolumeNursingStaff(), _notConsideredCostVolumeNursingStaff, _costPerFullTimeNursingStaff, _costStGrNursingStaff, _costKoArtGrNursingStaff, _explanationFieldNursingStaff, get_baseInformationIdNursingStaff());
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListExternalNursingStaff[ensId" +
                _id + ']';
    }
}
