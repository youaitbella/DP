package org.inek.dataportal.calc.entities.drg;

import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "KGLListExternalTechFunctService", schema = "calc")
@XmlRootElement
public class KGLListExternalTechFunctService implements Serializable {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "etfsID", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Bereich/Station">
    @Column(name = "etfsDivision")
    @Documentation(name = "Bereich", rank = 10)
    private String _divisionTechFunctService="";

    public String getDivisionTechFunctService() {
        return _divisionTechFunctService;
    }

    public void setDivisionTechFunctService(String divisionTechFunctService) {
        this._divisionTechFunctService = divisionTechFunctService;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Anzahl VK">
    @Column(name = "etfsCount")
    private double _countTechFunctService;

    public double getCountTechFunctService() {
        return _countTechFunctService;
    }

    public void setCountTechFunctService(double countTechFunctService) {
        this._countTechFunctService = countTechFunctService;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Kostenvolumen">
    @Column(name = "etfsCostVolume")
    private double _costVolumeTechFunctService;

    public double getCostVolumeTechFunctService() {
        return _costVolumeTechFunctService;
    }

    public void setCostVolumeTechFunctService(double costVolumeTechFunctService) {
        this._costVolumeTechFunctService = costVolumeTechFunctService;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoStGr">
    @Column(name = "etfsKoStGr")
    private double _costStGrTechFunctService;

    public double getCostStGrTechFunctService() {
        return _costStGrTechFunctService;
    }

    public void setCostStGrTechFunctService(double costStGrTechFunctService) {
        this._costStGrTechFunctService = costStGrTechFunctService;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoArtGr">
    @Column(name = "etfsKoArtGr")
    private double _costKoArtGrTechFunctService;

    public double getCostKoArtGrTechFunctService() {
        return _costKoArtGrTechFunctService;
    }

    public void setCostKoArtGrTechFunctService(double costKoArtGrTechFunctService) {
        this._costKoArtGrTechFunctService = costKoArtGrTechFunctService;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KoArtGr">
    @Column(name = "etfsOccupationalGroup")
    private int _occupationalGroup;

    public int getOccupationalGroup() {
        return _occupationalGroup;
    }

    public void setOccupationalGroup(int occupationalGroup) {
        this._occupationalGroup = occupationalGroup;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Erläuterungsfeld">
    @Column(name = "etfsExplanationField")
    private String _explanationFieldTechFunctService="";

    public String getExplanationFieldTechFunctService() {
        return _explanationFieldTechFunctService;
    }

    public void setExplanationFieldTechFunctService(String explanationFieldTechFunctService) {
        this._explanationFieldTechFunctService = explanationFieldTechFunctService;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="baseInformationId">
    @Column(name = "etfsBaseInformationId")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int _baseInformationId) {
        this._baseInformationId = _baseInformationId;
    }
    // </editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KGLListExternalTechFunctService)) return false;
        KGLListExternalTechFunctService that = (KGLListExternalTechFunctService) o;
        return _id == that._id &&
                Double.compare(that._countTechFunctService, _countTechFunctService) == 0 &&
                Double.compare(that._costVolumeTechFunctService, _costVolumeTechFunctService) == 0 &&
                Double.compare(that._costStGrTechFunctService, _costStGrTechFunctService) == 0 &&
                Double.compare(that._costKoArtGrTechFunctService, _costKoArtGrTechFunctService) == 0 &&
                getOccupationalGroup() == that.getOccupationalGroup() &&
                getBaseInformationId() == that.getBaseInformationId() &&
                Objects.equals(_divisionTechFunctService, that._divisionTechFunctService) &&
                Objects.equals(getExplanationFieldTechFunctService(), that.getExplanationFieldTechFunctService());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _divisionTechFunctService, _countTechFunctService, _costVolumeTechFunctService, _costStGrTechFunctService, _costKoArtGrTechFunctService, getOccupationalGroup(), getExplanationFieldTechFunctService(), getBaseInformationId());
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListExternalTechFunctService[etfsId" +
                _id + ']';
    }
}
