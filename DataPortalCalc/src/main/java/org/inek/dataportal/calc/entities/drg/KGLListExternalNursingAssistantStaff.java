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
    private String _divisionNursingAssistantStaff="";

    public String getDivisionNursingAssistantStaff() {
        return _divisionNursingAssistantStaff;
    }

    public void setDivisionNursingAssistantStaff(String divisionNursingAssistantStaff) {
        this._divisionNursingAssistantStaff = divisionNursingAssistantStaff;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Durchschnittliche tariflich vereinbarte
    //Jahresarbeitszeit [in Std.] einer direkt angestellten Pflegefachkraft ">
    @Column(name = "enaAgreedAverageWorkingHoursNursingAssistantStaff")
    private double _agreedAverageWorkingHoursNursingAssistantStaff;

    public double getAgreedAverageWorkingHoursNursingAssistantStaff() {
        return _agreedAverageWorkingHoursNursingAssistantStaff;
    }

    public void setAgreedAverageWorkingHoursNursingAssistantStaff(double agreedAverageWorkingHoursNursingAssistantStaff) {
        this._agreedAverageWorkingHoursNursingAssistantStaff = agreedAverageWorkingHoursNursingAssistantStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Durchschnittliche tarifliche Netto Jahresarbeitszeit [in Std.]
    //(nach Abzug von Fehltagen) (i) einer direkt angestellten Pflegefachkraft">
    @Column(name = "enaNetAverageWorkingHoursNursingAssistantStaff")
    private double _netAverageWorkingHoursNursingAssistantStaff;

    public double getNetAverageWorkingHoursNursingAssistantStaff() {
        return _netAverageWorkingHoursNursingAssistantStaff;
    }

    public void setNetAverageWorkingHoursNursingAssistantStaff(double netAverageWorkingHoursNursingAssistantStaff) {
        this._netAverageWorkingHoursNursingAssistantStaff = netAverageWorkingHoursNursingAssistantStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Errechnete Anzahl VK(Arbeitsleistung des Fremdpersonals/Tarifliche Netto Jahresarbeitszeit)">
    @Column(name = "enaCalculatedCountNursingAssistantStaff")
    private double _calculatedCountNursingAssistantStaff;

    public double getCalculatedCountNursingAssistantStaff() {
        return _calculatedCountNursingAssistantStaff;
    }

    public void setCalculatedCountNursingAssistantStaff(double calculatedCountNursingAssistantStaff) {
        this._calculatedCountNursingAssistantStaff = calculatedCountNursingAssistantStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Durchschnittliche jährliche Arbeitgeberkosten einer tariflich angestellten Pflegefachkraft">
    @Column(name = "enaAverageAnnualEmployerCostsNursingAssistantStaff")
    private double _averageAnnualEmployerCostsNursingAssistantStaff;

    public double getAverageAnnualEmployerCostsNursingAssistantStaff() {
        return _averageAnnualEmployerCostsNursingAssistantStaff;
    }

    public void setAverageAnnualEmployerCostsNursingAssistantStaff(double averageAnnualEmployerCostsNursingAssistantStaff) {
        this._averageAnnualEmployerCostsNursingAssistantStaff = averageAnnualEmployerCostsNursingAssistantStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Rechnungsbetrag der Arbeitnehmerüberlassung für Pflegefachkräfte:">
    @Column(name ="enaAmountTemporaryEmploymentNursingAssistantStaff")
    private double _amountTemporaryEmploymentNursingAssistantStaff;

    public double getAmountTemporaryEmploymentNursingAssistantStaff() {
        return _amountTemporaryEmploymentNursingAssistantStaff;
    }

    public void setAmountTemporaryEmploymentNursingAssistantStaff(double amountTemporaryEmploymentNursingAssistantStaff) {
        this._amountTemporaryEmploymentNursingAssistantStaff = amountTemporaryEmploymentNursingAssistantStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoStGr">
    @Column(name = "enaKoStGr")
    private double _costStGrNursingAssistantStaff;

    public double getCostStGrNursingAssistantStaff() {
        return _costStGrNursingAssistantStaff;
    }

    public void setCostStGrNursingAssistantStaff(double costStGrNursingAssistantStaff) {
        this._costStGrNursingAssistantStaff = costStGrNursingAssistantStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoArtGr">
    @Column(name = "enaKoArtGr")
    private double _costKoArtGrNursingAssistantStaff;

    public double getCostKoArtGrNursingAssistantStaff() {
        return _costKoArtGrNursingAssistantStaff;
    }

    public void setCostKoArtGrNursingAssistantStaff(double costKoArtGrNursingAssistantStaff) {
        this._costKoArtGrNursingAssistantStaff = costKoArtGrNursingAssistantStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Wird das Fremdpersonal ausschließlich für die "Pflege am Bett" eingesetzt?">
    @Column(name ="enaExclusivelyCareAtBedNursingAssistantStaff")
    private boolean _exclusivelyCareAtBedNursingAssistantStaff;

    public boolean isExclusivelyCareAtBedNursingAssistantStaff() {
        return _exclusivelyCareAtBedNursingAssistantStaff;
    }

    public void setExclusivelyCareAtBedNursingAssistantStaff(boolean exclusivelyCareAtBedNursingAssistantStaff) {
        this._exclusivelyCareAtBedNursingAssistantStaff = exclusivelyCareAtBedNursingAssistantStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Bitte weisen Sie hier  den Teil des Kostenvolumens des Fremdpersonals aus,
    //welches nicht für die  "Pflege am Bett" eingesetzt wird und geben Sie im Erläuterungsfeld den entsprechenden Bereich (z.B. Funktionslabor) an.">
    @Column(name ="enaPartOfCostVolumeBedNursingAssistantStaff")
    private double _partOfCostVolumeBedNursingAssistantStaff;

    public double getPartOfCostVolumeBedNursingAssistantStaff() {
        return _partOfCostVolumeBedNursingAssistantStaff;
    }

    public void setPartOfCostVolumeBedNursingAssistantStaff(double partOfCostVolumeBedNursingAssistantStaff) {
        this._partOfCostVolumeBedNursingAssistantStaff = partOfCostVolumeBedNursingAssistantStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Erläuterungsfeld">
    @Column(name = "enaExplanationFieldAssistant")
    private String _explanationFieldNursingAssistantStaff="";

    public String getExplanationFieldNursingAssistantStaff() {
        return _explanationFieldNursingAssistantStaff;
    }

    public void setExplanationFieldNursingAssistantStaff(String explanationFieldNursingAssistantStaff) {
        this._explanationFieldNursingAssistantStaff = explanationFieldNursingAssistantStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="baseInformationId">
    @Column(name = "enaBaseInformationId")
    private int _baseInformationId;

    public int getBaseInformationId() {
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
                Double.compare(that._agreedAverageWorkingHoursNursingAssistantStaff, _agreedAverageWorkingHoursNursingAssistantStaff) == 0 &&
                Double.compare(that.getNetAverageWorkingHoursNursingAssistantStaff(), getNetAverageWorkingHoursNursingAssistantStaff()) == 0 &&
                Double.compare(that._calculatedCountNursingAssistantStaff, _calculatedCountNursingAssistantStaff) == 0 &&
                Double.compare(that.getAverageAnnualEmployerCostsNursingAssistantStaff(), getAverageAnnualEmployerCostsNursingAssistantStaff()) == 0 &&
                Double.compare(that._amountTemporaryEmploymentNursingAssistantStaff, _amountTemporaryEmploymentNursingAssistantStaff) == 0 &&
                Double.compare(that._costStGrNursingAssistantStaff, _costStGrNursingAssistantStaff) == 0 &&
                Double.compare(that._costKoArtGrNursingAssistantStaff, _costKoArtGrNursingAssistantStaff) == 0 &&
                _exclusivelyCareAtBedNursingAssistantStaff == that._exclusivelyCareAtBedNursingAssistantStaff &&
                Double.compare(that._partOfCostVolumeBedNursingAssistantStaff, _partOfCostVolumeBedNursingAssistantStaff) == 0 &&
                _baseInformationId == that._baseInformationId &&
                Objects.equals(_divisionNursingAssistantStaff, that._divisionNursingAssistantStaff) &&
                Objects.equals(_explanationFieldNursingAssistantStaff, that._explanationFieldNursingAssistantStaff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _divisionNursingAssistantStaff, _agreedAverageWorkingHoursNursingAssistantStaff, getNetAverageWorkingHoursNursingAssistantStaff(), _calculatedCountNursingAssistantStaff, getAverageAnnualEmployerCostsNursingAssistantStaff(), _amountTemporaryEmploymentNursingAssistantStaff, _costStGrNursingAssistantStaff, _costKoArtGrNursingAssistantStaff, _exclusivelyCareAtBedNursingAssistantStaff, _partOfCostVolumeBedNursingAssistantStaff, _explanationFieldNursingAssistantStaff, _baseInformationId);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListExternalNursingAssistantStaff{" +
                _id + ']';
    }
}
