package org.inek.dataportal.calc.entities.drg;

import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

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

    // <editor-fold defaultstate="collapsed" desc="Erläuterungsfeld">
    @Column(name = "etfsExplanationField")
    private String _explanationFieldTechFunctService="";

    public String get_explanationFieldTechFunctService() {
        return _explanationFieldTechFunctService;
    }

    public void set_explanationFieldTechFunctService(String _explanationFieldTechFunctService) {
        this._explanationFieldTechFunctService = _explanationFieldTechFunctService;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="_baseInformationId">
    @Column(name = "etfsBaseInformationId")
    private int _baseInformationId;

    public int get_baseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int _baseInformationId) {
        this._baseInformationId = _baseInformationId;
    }
    // </editor-fold>


    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListExternalTechFunctService[etfsId" +
                _id + ']';
    }
}
