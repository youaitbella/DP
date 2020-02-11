package org.inek.dataportal.calc.entities.drg;

import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "KGLListExternalNursingAssistantStaff", schema = "calc")
@XmlRootElement
public class KGLListExternalNursingAssistantStaff implements Serializable {
    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enaID", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="Bereich/Station">
    @Column(name = "enaDivision")
    @Documentation(name = "Bereich", rank = 10)
    private String _divisionNursingAssistant ="";

    public String getDivisionNursingAssistant() {
        return _divisionNursingAssistant;
    }

    public void setDivisionNursingAssistant(String divisionNursingAssistant) {
        this._divisionNursingAssistant = divisionNursingAssistant;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Anzahl VK">
    @Column(name = "enaCount")
    private double _countNursingAssistant;

    public double getCountNursingAssistant() {
        return _countNursingAssistant;
    }

    public void setCountNursingAssistant(double countNursingAssistant) {
        this._countNursingAssistant = countNursingAssistant;
    }

// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Kostenvolumen,
    // welches gem. §6a Abs. 2 KHEntgG zur Ermittlung des Pflegebudgets berücksichtigt werden darf:">
    @Column(name = "enaConsideredCostVolume")
    private double _consideredCostVolumeNursingAssistant;

    public double getConsideredCostVolumeNursingAssistant() {
        return _consideredCostVolumeNursingAssistant;
    }

    public void setConsideredCostVolumeNursingAssistant(double consideredCostVolumeNursingAssistant) {
        this._consideredCostVolumeNursingAssistant = consideredCostVolumeNursingAssistant;
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Kostenvolumen,
    // welches nicht gem. §6a Abs. 2 KHEntgG im Pflegebudget berücksichtigt werden darf (i):">
    @Column(name = "enaNotConsideredCostVolume")
    private double _notConsideredCostVolumeNursingAssistant;

    public double getNotConsideredCostVolumeNursingAssistant() {
        return _notConsideredCostVolumeNursingAssistant;
    }

    public void setNotConsideredCostVolumeNursingAssistant(double notConsideredCostVolumeNursingAssistant) {
        this._notConsideredCostVolumeNursingAssistant = notConsideredCostVolumeNursingAssistant;
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="KoStGr">
    @Column(name = "enaKoStGr")
    private double _costKoStGrNursingAssistant;

    public double getCostKoStGrNursingAssistant() {
        return _costKoStGrNursingAssistant;
    }

    public void setCostKoStGrNursingAssistant(double costKoStGrNursingAssistant) {
        this._costKoStGrNursingAssistant = costKoStGrNursingAssistant;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoArtGr">
    @Column(name = "enaKoArtGr")
    private double _costKoArtGrNursingAssistant;

    public double getCostKoArtGrNursingAssistant() {
        return _costKoArtGrNursingAssistant;
    }

    public void setCostKoArtGrNursingAssistant(double costKoArtGrNursingAssistant) {
        this._costKoArtGrNursingAssistant = costKoArtGrNursingAssistant;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Erläuterungsfeld">
    @Column(name = "enaExplanationField")
    private String _explanationFieldNursingAssistant ="";

    public String getExplanationFieldNursingAssistant() {
        return _explanationFieldNursingAssistant;
    }

    public void setExplanationFieldNursingAssistant(String explanationFieldNursingAssistant) {
        this._explanationFieldNursingAssistant = explanationFieldNursingAssistant;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="_baseInformationId">
    @Column(name = "enaBaseInformationId")
    private int _baseInformationId;

    public int get_baseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int _baseInformationId) {
        this._baseInformationId = _baseInformationId;
    }
    // </editor-fold>


    public KGLListExternalNursingAssistantStaff() {
    }

    public KGLListExternalNursingAssistantStaff(int _baseInformationId) {
        this._baseInformationId = _baseInformationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KGLListExternalNursingAssistantStaff)) return false;
        KGLListExternalNursingAssistantStaff that = (KGLListExternalNursingAssistantStaff) o;
        return _id == that._id &&
                Double.compare(that._countNursingAssistant, _countNursingAssistant) == 0 &&
                Double.compare(that._consideredCostVolumeNursingAssistant, _consideredCostVolumeNursingAssistant) == 0 &&
                Double.compare(that._notConsideredCostVolumeNursingAssistant, _notConsideredCostVolumeNursingAssistant) == 0 &&
                Double.compare(that._costKoStGrNursingAssistant, _costKoStGrNursingAssistant) == 0 &&
                Double.compare(that._costKoArtGrNursingAssistant, _costKoArtGrNursingAssistant) == 0 &&
                get_baseInformationId() == that.get_baseInformationId() &&
                Objects.equals(_divisionNursingAssistant, that._divisionNursingAssistant) &&
                Objects.equals(_explanationFieldNursingAssistant, that._explanationFieldNursingAssistant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _divisionNursingAssistant, _countNursingAssistant, _consideredCostVolumeNursingAssistant,
                _notConsideredCostVolumeNursingAssistant, _costKoStGrNursingAssistant, _costKoArtGrNursingAssistant
                , _explanationFieldNursingAssistant, get_baseInformationId());
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListExternalNursingAssistantStaff{" +
                _id + ']';
    }
}
