package org.inek.dataportal.calc.entities.drg;

import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "KGLListExternalCareStaffOther", schema = "calc")
@XmlRootElement
public class KGLListExternalCareStaffOther implements Serializable {
    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecsoID", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Bereich/Station">
    @Column(name = "ecsoDivision")
    @Documentation(name = "Bereich", rank = 10)
    private String _divisionCareStaffOther ="";

    public String getDivisionCareStaffOther() {
        return _divisionCareStaffOther;
    }

    public void setDivisionCareStaffOther(String divisionCareStaffOther) {
        this._divisionCareStaffOther = divisionCareStaffOther;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Anzahl VK">
    @Column(name = "ecsoCount")
    private double _countCareStaffOther;

    public double getCountCareStaffOther() {
        return _countCareStaffOther;
    }

    public void setCountCareStaffOther(double countCareStaffOther) {
        this._countCareStaffOther = countCareStaffOther;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Kostenvolumen,
    // welches gem. §6a Abs. 2 KHEntgG zur Ermittlung des Pflegebudgets berücksichtigt werden darf:">
    @Column(name = "ecsoConsideredCostVolume")
    private double _consideredCostVolumeCareStaffOther;

    public double getConsideredCostVolumeCareStaffOther() {
        return _consideredCostVolumeCareStaffOther;
    }

    public void setConsideredCostVolumeCareStaffOther(double consideredCostVolumeCareStaffOther) {
        this._consideredCostVolumeCareStaffOther = consideredCostVolumeCareStaffOther;
    }

    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Kostenvolumen,
    // welches nicht gem. §6a Abs. 2 KHEntgG im Pflegebudget berücksichtigt werden darf (i):">
    @Column(name = "ecsoNotConsideredCostVolume")
    private double _notConsideredCostVolumeNursingAssistant;

    public double getNotConsideredCostVolumeNursingAssistant() {
        return _notConsideredCostVolumeNursingAssistant;
    }

    public void setNotConsideredCostVolumeNursingAssistant(double notConsideredCostVolumeNursingAssistant) {
        this._notConsideredCostVolumeNursingAssistant = notConsideredCostVolumeNursingAssistant;
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="KoStGr">
    @Column(name = "ecsoKoStGr")
    private double _costKoStGrCareStaffOther;

    public double getCostKoStGrCareStaffOther() {
        return _costKoStGrCareStaffOther;
    }

    public void setCostKoStGrCareStaffOther(double costKoStGrCareStaffOther) {
        this._costKoStGrCareStaffOther = costKoStGrCareStaffOther;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoArtGr">
    @Column(name = "ecsoKoArtGr")
    private double _costKoArtGrNursingAssistant;

    public double getCostKoArtGrNursingAssistant() {
        return _costKoArtGrNursingAssistant;
    }

    public void setCostKoArtGrNursingAssistant(double costKoArtGrNursingAssistant) {
        this._costKoArtGrNursingAssistant = costKoArtGrNursingAssistant;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Erläuterungsfeld">
    @Column(name = "ecsoExplanationField")
    private String _explanationFieldNursingAssistant ="";

    public String getExplanationFieldNursingAssistant() {
        return _explanationFieldNursingAssistant;
    }

    public void setExplanationFieldNursingAssistant(String explanationFieldNursingAssistant) {
        this._explanationFieldNursingAssistant = explanationFieldNursingAssistant;
    }
    // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="_baseInformationId">
    @Column(name = "ecsoBaseInformationId")
    private int _baseInformationId;

    public int get_baseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int _baseInformationId) {
        this._baseInformationId = _baseInformationId;
    }
    // </editor-fold>


    public KGLListExternalCareStaffOther() {
    }

    public KGLListExternalCareStaffOther(int _baseInformationId) {
        this._baseInformationId = _baseInformationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KGLListExternalCareStaffOther)) return false;
        KGLListExternalCareStaffOther that = (KGLListExternalCareStaffOther) o;
        return _id == that._id &&
                Double.compare(that.getCountCareStaffOther(), getCountCareStaffOther()) == 0 &&
                Double.compare(that.getConsideredCostVolumeCareStaffOther(),
                        getConsideredCostVolumeCareStaffOther()) == 0 &&
                Double.compare(that.getNotConsideredCostVolumeNursingAssistant(),
                        getNotConsideredCostVolumeNursingAssistant()) == 0 &&
                Double.compare(that.getCostKoStGrCareStaffOther(),
                        getCostKoStGrCareStaffOther()) == 0 &&
                Double.compare(that.getCostKoArtGrNursingAssistant(),
                        getCostKoArtGrNursingAssistant()) == 0 &&
                get_baseInformationId() == that.get_baseInformationId() &&
                Objects.equals(getDivisionCareStaffOther(), that.getDivisionCareStaffOther()) &&
                Objects.equals(getExplanationFieldNursingAssistant(), that.getExplanationFieldNursingAssistant());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, getDivisionCareStaffOther(), getCountCareStaffOther(), getConsideredCostVolumeCareStaffOther(),
                getNotConsideredCostVolumeNursingAssistant(), getCostKoStGrCareStaffOther(), getCostKoArtGrNursingAssistant(),
                getExplanationFieldNursingAssistant(), get_baseInformationId());
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListExternalCareStaffOther{" +
                _id + ']';
    }
}
