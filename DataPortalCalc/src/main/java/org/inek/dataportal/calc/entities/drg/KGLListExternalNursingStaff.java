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

    // <editor-fold defaultstate="collapsed" desc="Durchschnittliche tariflich vereinbarte
    //Jahresarbeitszeit [in Std.] einer direkt angestellten Pflegefachkraft ">
    @Column(name = "ensAgreedAverageWorkingHoursNursingStaff")
    private double _agreedAverageWorkingHoursNursingStaff;

    public double getAgreedAverageWorkingHoursNursingStaff() {
        return _agreedAverageWorkingHoursNursingStaff;
    }

    public void setAgreedAverageWorkingHoursNursingStaff(double agreedAverageWorkingHoursNursingStaff) {
        this._agreedAverageWorkingHoursNursingStaff = agreedAverageWorkingHoursNursingStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Durchschnittliche tarifliche Netto Jahresarbeitszeit [in Std.]
    //(nach Abzug von Fehltagen) (i) einer direkt angestellten Pflegefachkraft">
    @Column(name = "ensNetAverageWorkingHoursNursingStaff")
    private double _netAverageWorkingHoursNursingStaff;

    public double getNetAverageWorkingHoursNursingStaff() {
        return _netAverageWorkingHoursNursingStaff;
    }

    public void setNetAverageWorkingHoursNursingStaff(double netAverageWorkingHoursNursingStaff) {
        this._netAverageWorkingHoursNursingStaff = netAverageWorkingHoursNursingStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Errechnete Anzahl VK(Arbeitsleistung des Fremdpersonals/Tarifliche Netto Jahresarbeitszeit)">
    @Column(name = "ensCalculatedCountNursingStaff")
    private double _calculatedCountNursingStaff;

    public double getCalculatedCountNursingStaff() {
        return _calculatedCountNursingStaff;
    }

    public void setCalculatedCountNursingStaff(double calculatedCountNursingStaff) {
        this._calculatedCountNursingStaff = calculatedCountNursingStaff;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Durchschnittliche jährliche Arbeitgeberkosten einer tariflich angestellten Pflegefachkraft">
    @Column(name = "ensAverageAnnualEmployerCostsNursingStaff")
    private double _averageAnnualEmployerCostsNursingStaff;

    public double getAverageAnnualEmployerCostsNursingStaff() {
        return _averageAnnualEmployerCostsNursingStaff;
    }

    public void setAverageAnnualEmployerCostsNursingStaff(double averageAnnualEmployerCostsNursingStaff) {
        this._averageAnnualEmployerCostsNursingStaff = averageAnnualEmployerCostsNursingStaff;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Rechnungsbetrag der Arbeitnehmerüberlassung für Pflegefachkräfte:">
    @Column(name ="ensAmountTemporaryEmploymentNursingStaff")
    private double _amountTemporaryEmploymentNursingStaff;

    public double getAmountTemporaryEmploymentNursingStaff() {
        return _amountTemporaryEmploymentNursingStaff;
    }

    public void setAmountTemporaryEmploymentNursingStaff(double amountTemporaryEmploymentNursingStaff) {
        this._amountTemporaryEmploymentNursingStaff = amountTemporaryEmploymentNursingStaff;
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

    // <editor-fold defaultstate="collapsed" desc="Wird das Fremdpersonal ausschließlich für die "Pflege am Bett" eingesetzt?">
    @Column(name ="ensExclusivelyCareAtBedNursingStaff")
    private boolean _exclusivelyCareAtBedNursingStaff;

    public boolean isExclusivelyCareAtBedNursingStaff() {
        return _exclusivelyCareAtBedNursingStaff;
    }

    public void setExclusivelyCareAtBedNursingStaff(boolean exclusivelyCareAtBedNursingStaff) {
        this._exclusivelyCareAtBedNursingStaff = exclusivelyCareAtBedNursingStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Bitte weisen Sie hier  den Teil des Kostenvolumens des Fremdpersonals aus,
    //welches nicht für die  "Pflege am Bett" eingesetzt wird und geben Sie im Erläuterungsfeld den entsprechenden Bereich (z.B. Funktionslabor) an.">
    @Column(name ="ensPartOfCostVolumeBedNursingStaff")
    private double _partOfCostVolumeBedNursingStaff;

    public double getPartOfCostVolumeBedNursingStaff() {
        return _partOfCostVolumeBedNursingStaff;
    }

    public void setPartOfCostVolumeBedNursingStaff(double partOfCostVolumeBedNursingStaff) {
        this._partOfCostVolumeBedNursingStaff = partOfCostVolumeBedNursingStaff;
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

    // <editor-fold defaultstate="collapsed" desc="baseInformationId">
    @Column(name = "ensBaseInformationId")
    private int _baseInformationIdNursingStaff;

    public int getBaseInformationIdNursingStaff() {
        return _baseInformationIdNursingStaff;
    }

    public void setBaseInformationId(int _baseInformationIdNursingStaff) {
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
                Double.compare(that.getAgreedAverageWorkingHoursNursingStaff(), getAgreedAverageWorkingHoursNursingStaff()) == 0 &&
                Double.compare(that.getNetAverageWorkingHoursNursingStaff(), getNetAverageWorkingHoursNursingStaff()) == 0 &&
                Double.compare(that.getCalculatedCountNursingStaff(), getCalculatedCountNursingStaff()) == 0 &&
                Double.compare(that.getAverageAnnualEmployerCostsNursingStaff(), getAverageAnnualEmployerCostsNursingStaff()) == 0 &&
                Double.compare(that.getAmountTemporaryEmploymentNursingStaff(), getAmountTemporaryEmploymentNursingStaff()) == 0 &&
                Double.compare(that._costStGrNursingStaff, _costStGrNursingStaff) == 0 &&
                Double.compare(that._costKoArtGrNursingStaff, _costKoArtGrNursingStaff) == 0 &&
                isExclusivelyCareAtBedNursingStaff() == that.isExclusivelyCareAtBedNursingStaff() &&
                Double.compare(that.getPartOfCostVolumeBedNursingStaff(), getPartOfCostVolumeBedNursingStaff()) == 0 &&
                _baseInformationIdNursingStaff == that._baseInformationIdNursingStaff &&
                Objects.equals(_divisionNursingStaff, that._divisionNursingStaff) &&
                Objects.equals(_explanationFieldNursingStaff, that._explanationFieldNursingStaff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _divisionNursingStaff, getAgreedAverageWorkingHoursNursingStaff(), getNetAverageWorkingHoursNursingStaff(), getCalculatedCountNursingStaff(), getAverageAnnualEmployerCostsNursingStaff(), getAmountTemporaryEmploymentNursingStaff(), _costStGrNursingStaff, _costKoArtGrNursingStaff, isExclusivelyCareAtBedNursingStaff(), getPartOfCostVolumeBedNursingStaff(), _explanationFieldNursingStaff, _baseInformationIdNursingStaff);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListExternalNursingStaff[ensId" +
                _id + ']';
    }
}
