package org.inek.dataportal.calc.entities.drg;

import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "KGLListExternalMedStaff", schema = "calc")
@XmlRootElement
public class KGLListExternalMedStaff implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emsID", updatable = false, nullable = false)
    private int _id = -1;

    public KGLListExternalMedStaff() {
    }

    public KGLListExternalMedStaff(DrgCalcBasics calcBasics) {
        this.calcBasics = calcBasics;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Bereich/Station">
    @Column(name = "emsDivision")
    @Documentation(name = "Bereich", rank = 10)
    private String _divisionMedStaff = "";

    public String getDivisionMedStaff() {
        return _divisionMedStaff;
    }

    public void setDivisionMedStaff(String divisionMedStaff) {
        this._divisionMedStaff = divisionMedStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Anzahl VK">
    @Column(name = "emsCount")
    private double _countMedStaff;

    public double getCount() {
        return _countMedStaff;
    }

    public void setCount(double count) {
        this._countMedStaff = count;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Kostenvolumen">
    @Column(name = "emsCostVolume")
    private double _costVolumeMedStaff;

    public double getCostVolume() {
        return _costVolumeMedStaff;
    }

    public void setCostVolume(double costVolume) {
        this._costVolumeMedStaff = costVolume;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoStGr">
    @Column(name = "emsKoStGr")
    private int _costStGr;

    public int getCostKoStGr() {
        return _costStGr;
    }

    public void setCostKoStGr(int costKoStGr) {
        this._costStGr = costKoStGr;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoArtGr">
    @Column(name = "emsKoArtGr")
    private int _costKoArtGr;

    public int getCostKoArtGr() {
        return _costKoArtGr;
    }

    public void setCostKoArtGr(int costKoArtGr) {
        this._costKoArtGr = costKoArtGr;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Erläuterungsfeld">
    @Column(name = "emsExplanationField")
    private String _explanationField = "";

    public String getExplanationField() {
        return _explanationField;
    }

    public void setExplanationField(String explanationField) {
        this._explanationField = explanationField;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DataYear">
    @Column(name = "emsDataYear")
    private int _dataYear;

    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        this._dataYear = dataYear;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="baseInformationId">
    @ManyToOne
    @JoinColumn(name = "emsBaseInformationId")
    private DrgCalcBasics calcBasics;

    public DrgCalcBasics getDrgCalcBasics() {
        return calcBasics;
    }

    public int getBaseInformationId() {
        return calcBasics.getId();
    }

    public void setBaseInformationId(int id) {
    }
    // </editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KGLListExternalMedStaff)) return false;
        KGLListExternalMedStaff that = (KGLListExternalMedStaff) o;
        return _id == that._id &&
                Double.compare(that._countMedStaff, _countMedStaff) == 0 &&
                Double.compare(that._costVolumeMedStaff, _costVolumeMedStaff) == 0 &&
                _costStGr == that._costStGr &&
                _costKoArtGr == that._costKoArtGr &&
                _dataYear == that._dataYear &&
                Objects.equals(_divisionMedStaff, that._divisionMedStaff) &&
                Objects.equals(_explanationField, that._explanationField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _divisionMedStaff, _countMedStaff, _costVolumeMedStaff, _costStGr, _costKoArtGr,
                _explanationField, _dataYear);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListExternalMedStaff[emsId" +
                _id + ']';
    }
}
