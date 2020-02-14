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
    private String _division ="";

    public String getDivision() {
        return _division;
    }

    public void setDivision(String division) {
        this._division = division;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Durchschnittliche tariflich vereinbarte
    //Jahresarbeitszeit [in Std.] einer direkt angestellten Pflegefachkraft ">
    @Column(name = "ecsoAgreedAverageWorkingHours")
    private double _agreedAverageWorkingHours;

    public double getAgreedAverageWorkingHours() {
        return _agreedAverageWorkingHours;
    }

    public void setAgreedAverageWorkingHours(double agreedAverageWorkingHours) {
        this._agreedAverageWorkingHours = agreedAverageWorkingHours;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Durchschnittliche tarifliche Netto Jahresarbeitszeit [in Std.]
    // (nach Abzug von Fehltagen) (i) einer direkt angestellten Pflegefachkraft">
    @Column(name = "ecsoNetAverageWorkingHours")
    private double _netAverageWorkingHours;

    public double getNetAverageWorkingHours() {
        return _netAverageWorkingHours;
    }

    public void setNetAverageWorkingHours(double netAverageWorkingHours) {
        this._netAverageWorkingHours = netAverageWorkingHours;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Errechnete Anzahl VK(Arbeitsleistung des Fremdpersonals/Tarifliche Netto Jahresarbeitszeit)">
    @Column(name = "ecsoCalculatedCount")
    private double _calculatedCount;

    public double getCalculatedCount() {
        return _calculatedCount;
    }

    public void setCalculatedCount(double calculatedCount) {
        this._calculatedCount = calculatedCount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Durchschnittliche jährliche Arbeitgeberkosten einer tariflich angestellten Pflegefachkraft">
    @Column(name = "ecsoAverageAnnualEmployerCosts")
    private double _averageAnnualEmployerCosts;

    public double getAverageAnnualEmployerCosts() {
        return _averageAnnualEmployerCosts;
    }

    public void setAverageAnnualEmployerCosts(double averageAnnualEmployerCosts) {
        this._averageAnnualEmployerCosts = averageAnnualEmployerCosts;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Rechnungsbetrag der Arbeitnehmerüberlassung für Pflegefachkräfte:">
    @Column(name ="ecsoAmountTemporaryEmployment")
    private double _amountTemporaryEmployment;

    public double getAmountTemporaryEmployment() {
        return _amountTemporaryEmployment;
    }

    public void setAmountTemporaryEmployment(double amountTemporaryEmployment) {
        this._amountTemporaryEmployment = amountTemporaryEmployment;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoStGr">
    @Column(name = "ecsoKoStGr")
    private double _costKoStGr;

    public double getCostKoStGr() {
        return _costKoStGr;
    }

    public void setCostKoStGr(double costKoStGr) {
        this._costKoStGr = costKoStGr;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoArtGr">
    @Column(name = "ecsoKoArtGr")
    private double _costKoArtGr;

    public double getCostKoArtGr() {
        return _costKoArtGr;
    }

    public void setCostKoArtGr(double costKoArtGr) {
        this._costKoArtGr = costKoArtGr;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Wird das Fremdpersonal ausschließlich für die "Pflege am Bett" eingesetzt?">
    @Column(name ="ecsoExclusivelyCareAtBed")
    private boolean _exclusivelyCareAtBed;

    public boolean isExclusivelyCareAtBed() {
        return _exclusivelyCareAtBed;
    }

    public void setExclusivelyCareAtBed(boolean exclusivelyCareAtBed) {
        this._exclusivelyCareAtBed = exclusivelyCareAtBed;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Bitte weisen Sie hier  den Teil des Kostenvolumens des Fremdpersonals aus,
    //welches nicht für die  "Pflege am Bett" eingesetzt wird und geben Sie im Erläuterungsfeld den entsprechenden Bereich (z.B. Funktionslabor) an.">
    @Column(name ="ecsoPartOfCostVolumeBed")
    private double _partOfCostVolumeBed;

    public double getPartOfCostVolumeBed() {
        return _partOfCostVolumeBed;
    }

    public void setPartOfCostVolumeBed(double partOfCostVolumeBed) {
        this._partOfCostVolumeBed = partOfCostVolumeBed;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Erläuterungsfeld">
    @Column(name = "ecsoExplanationField")
    private String _explanationField ="";

    public String getExplanationField() {
        return _explanationField;
    }

    public void setExplanationField(String explanationField) {
        this._explanationField = explanationField;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="baseInformationId">
    @Column(name = "ecsoBaseInformationId")
    private int _baseInformationId;

    public int getBaseInformationId() {
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
                Double.compare(that._agreedAverageWorkingHours, _agreedAverageWorkingHours) == 0 &&
                Double.compare(that._netAverageWorkingHours, _netAverageWorkingHours) == 0 &&
                Double.compare(that._calculatedCount, _calculatedCount) == 0 &&
                Double.compare(that._averageAnnualEmployerCosts, _averageAnnualEmployerCosts) == 0 &&
                Double.compare(that._amountTemporaryEmployment, _amountTemporaryEmployment) == 0 &&
                Double.compare(that._costKoStGr, _costKoStGr) == 0 &&
                Double.compare(that.getCostKoArtGr(), getCostKoArtGr()) == 0 &&
                _exclusivelyCareAtBed == that._exclusivelyCareAtBed &&
                Double.compare(that.getPartOfCostVolumeBed(), getPartOfCostVolumeBed()) == 0 &&
                getBaseInformationId() == that.getBaseInformationId() &&
                Objects.equals(_division, that._division) &&
                Objects.equals(_explanationField, that._explanationField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _division, _agreedAverageWorkingHours, _netAverageWorkingHours, _calculatedCount, _averageAnnualEmployerCosts, _amountTemporaryEmployment, _costKoStGr, getCostKoArtGr(), _exclusivelyCareAtBed, getPartOfCostVolumeBed(), _explanationField, getBaseInformationId());
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListExternalCareStaffOther{" +
                _id + ']';
    }
}
