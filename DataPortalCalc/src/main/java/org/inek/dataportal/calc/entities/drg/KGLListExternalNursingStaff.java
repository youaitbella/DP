package org.inek.dataportal.calc.entities.drg;

import javax.persistence.*;
import javax.validation.constraints.Min;
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

    // <editor-fold defaultstate="collapsed" desc="Division">
    @Column(name = "ensDivision")
    private String _divisionNursingStaff="";

    public String getDivisionNursingStaff() {
        return _divisionNursingStaff;
    }

    public void setDivisionNursingStaff(String divisionNursingStaff) {
        this._divisionNursingStaff = divisionNursingStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Type">
    @Column(name = "ensExternalStaffType")
    private int _externalStaffType;

    public int getExternalStaffType() {
        return _externalStaffType;
    }

    public void setExternalStaffType(int externalStaffType) {
        this._externalStaffType = externalStaffType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AgreedAverageWorkingHoursNursingStaff">
    @Column(name = "ensAgreedAverageWorkingHoursNursingStaff")
    private double _agreedAverageWorkingHoursNursingStaff;

    public double getAgreedAverageWorkingHoursNursingStaff() {
        return _agreedAverageWorkingHoursNursingStaff;
    }

    public void setAgreedAverageWorkingHoursNursingStaff(double agreedAverageWorkingHoursNursingStaff) {
        this._agreedAverageWorkingHoursNursingStaff = agreedAverageWorkingHoursNursingStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="NetAverageWorkingHoursNursingStaff">
    @Column(name = "ensNetAverageWorkingHoursNursingStaff")
    private double _netAverageWorkingHoursNursingStaff;

    public double getNetAverageWorkingHoursNursingStaff() {
        return _netAverageWorkingHoursNursingStaff;
    }

    public void setNetAverageWorkingHoursNursingStaff(double netAverageWorkingHoursNursingStaff) {
        this._netAverageWorkingHoursNursingStaff = netAverageWorkingHoursNursingStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CalculatedCountNursingStaff">
    @Column(name = "ensCalculatedCountNursingStaff")
    private double _calculatedCountNursingStaff;

    public double getCalculatedCountNursingStaff() {
        return _calculatedCountNursingStaff;
    }

    public void setCalculatedCountNursingStaff(double calculatedCountNursingStaff) {
        this._calculatedCountNursingStaff = calculatedCountNursingStaff;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AverageAnnualEmployerCostsNursingStaff">
    @Column(name = "ensAverageAnnualEmployerCostsNursingStaff")
    private double _averageAnnualEmployerCostsNursingStaff;

    public double getAverageAnnualEmployerCostsNursingStaff() {
        return _averageAnnualEmployerCostsNursingStaff;
    }

    public void setAverageAnnualEmployerCostsNursingStaff(double averageAnnualEmployerCostsNursingStaff) {
        this._averageAnnualEmployerCostsNursingStaff = averageAnnualEmployerCostsNursingStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AmountTemporaryEmploymentNursingStaff">
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
    private int _costStGr;

    public int getCostKoStGr() {
        return _costStGr;
    }

    public void setCostKoStGr(int costKoStGr) {
        this._costStGr = costKoStGr;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoArtGr">
    @Column(name = "ensKoArtGr")
    private int _costArtGr;

    public int getCostKoArtGr() {
        return _costArtGr;
    }

    public void setCostKoArtGr(int costKoArtGr) {
        this._costArtGr = costKoArtGr;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ExclusivelyCareAtBedNursingStaff">
    @Column(name = "ensExclusivelyCareAtBedNursingStaff")
    private int _exclusivelyCareAtBedNursingStaff = -1;

    @Min(0)
    public int getExclusivelyCareAtBedNursingStaff() {
        return _exclusivelyCareAtBedNursingStaff;
    }

    public void setExclusivelyCareAtBedNursingStaff(int exclusivelyCareAtBedNursingStaff) {
        this._exclusivelyCareAtBedNursingStaff = exclusivelyCareAtBedNursingStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PartOfCostVolumeBedNursingStaff">
    @Column(name = "ensPartOfCostVolumeBedNursingStaff")
    private double _partOfCostVolumeBedNursingStaff;

    public double getPartOfCostVolumeBedNursingStaff() {
        return _partOfCostVolumeBedNursingStaff;
    }

    public void setPartOfCostVolumeBedNursingStaff(double partOfCostVolumeBedNursingStaff) {
        this._partOfCostVolumeBedNursingStaff = partOfCostVolumeBedNursingStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ExplanationField">
    @Column(name = "ensExplanationField")
    private String _explanationFieldNursingStaff="";

    public String getExplanationFieldNursingStaff() {
        return _explanationFieldNursingStaff;
    }

    public void setExplanationFieldNursingStaff(String explanationFieldNursingStaff) {
        this._explanationFieldNursingStaff = explanationFieldNursingStaff;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DataYear">
    @Column(name = "ensDataYear")
    private int _dataYear;

    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        this._dataYear = dataYear;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BaseInformationId">
    @Column(name = "ensBaseInformationId")
    private int _baseInformationIdNursingStaff;

    public int getBaseInformationIdNursingStaff() {
        return _baseInformationIdNursingStaff;
    }

    public void setBaseInformationId(int baseInformationIdNursingStaff) {
        this._baseInformationIdNursingStaff = baseInformationIdNursingStaff;
    }
    // </editor-fold>

    public KGLListExternalNursingStaff() {
    }

    public KGLListExternalNursingStaff(int baseInformationIdNursingStaff) {
        this._baseInformationIdNursingStaff = baseInformationIdNursingStaff;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KGLListExternalNursingStaff)) return false;
        KGLListExternalNursingStaff that = (KGLListExternalNursingStaff) o;
        return _id == that._id &&
                _externalStaffType == that._externalStaffType &&
                Double.compare(that._agreedAverageWorkingHoursNursingStaff, _agreedAverageWorkingHoursNursingStaff) == 0 &&
                Double.compare(that._netAverageWorkingHoursNursingStaff, _netAverageWorkingHoursNursingStaff) == 0 &&
                Double.compare(that._calculatedCountNursingStaff, _calculatedCountNursingStaff) == 0 &&
                Double.compare(that._averageAnnualEmployerCostsNursingStaff, _averageAnnualEmployerCostsNursingStaff) == 0 &&
                Double.compare(that._amountTemporaryEmploymentNursingStaff, _amountTemporaryEmploymentNursingStaff) == 0 &&
                _costStGr == that._costStGr &&
                _costArtGr == that._costArtGr &&
                _exclusivelyCareAtBedNursingStaff == that._exclusivelyCareAtBedNursingStaff &&
                Double.compare(that._partOfCostVolumeBedNursingStaff, _partOfCostVolumeBedNursingStaff) == 0 &&
                _dataYear == that._dataYear &&
                _baseInformationIdNursingStaff == that._baseInformationIdNursingStaff &&
                Objects.equals(_divisionNursingStaff, that._divisionNursingStaff) &&
                Objects.equals(_explanationFieldNursingStaff, that._explanationFieldNursingStaff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _divisionNursingStaff, _externalStaffType, _agreedAverageWorkingHoursNursingStaff,
                _netAverageWorkingHoursNursingStaff, _calculatedCountNursingStaff, _averageAnnualEmployerCostsNursingStaff,
                _amountTemporaryEmploymentNursingStaff, _costStGr, _costArtGr, _exclusivelyCareAtBedNursingStaff,
                _partOfCostVolumeBedNursingStaff, _explanationFieldNursingStaff, _dataYear, _baseInformationIdNursingStaff);
    }


    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListExternalNursingStaff[ensId" +
                _id + ']';
    }
}
