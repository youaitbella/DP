package org.inek.dataportal.calc.entities.drg;

import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "KGLListExternalMedStaff", schema = "calc")
@XmlRootElement
public class KGLListExternalMedStaff implements Serializable {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emsID", updatable = false, nullable = false)
    private int _id = -1;

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
    private String _divisionMedStaff="";

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
    private double _costStGr;

    public double getCostKoStGr() {
        return _costStGr;
    }

    public void setCostKoStGr(double costKoStGr) {
        this._costStGr = costKoStGr;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoArtGr">
    @Column(name = "emsKoArtGr")
    private double _costKoArtGr;

    public double getCostKoArtGr() {
        return _costKoArtGr;
    }

    public void setCostKoArtGr(double costKoArtGr) {
        this._costKoArtGr = costKoArtGr;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Erläuterungsfeld">
    @Column(name = "emsExplanationField")
    private String _explanationField="";

    public String getExplanationField() {
        return _explanationField;
    }

    public void setExplanationField(String explanationField) {
        this._explanationField = explanationField;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="baseInformationId">
    @Column(name = "emsBaseInformationId")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int _baseInformationId) {
        this._baseInformationId = _baseInformationId;
    }
    // </editor-fold>

    public KGLListExternalMedStaff() {
    }

    public KGLListExternalMedStaff(int _baseInformationId) {
        this._baseInformationId = _baseInformationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KGLListExternalMedStaff)) return false;
        KGLListExternalMedStaff that = (KGLListExternalMedStaff) o;
        return getId() == that.getId() &&
                Double.compare(that.getCount(), getCount()) == 0 &&
                Double.compare(that.getCostVolume(), getCostVolume()) == 0 &&
                Double.compare(that.getCostKoStGr(), getCostKoStGr()) == 0 &&
                Double.compare(that.getCostKoArtGr(), getCostKoArtGr()) == 0 &&
                getBaseInformationId() == that.getBaseInformationId() &&
                Objects.equals(getDivisionMedStaff(), that.getDivisionMedStaff()) &&
                Objects.equals(getExplanationField(), that.getExplanationField());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDivisionMedStaff(), getCount(), getCostVolume(), getCostKoStGr(), getCostKoArtGr(),
                getExplanationField(), getBaseInformationId());
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListExternalMedStaff[emsId" +
                 _id + ']';
    }
}
