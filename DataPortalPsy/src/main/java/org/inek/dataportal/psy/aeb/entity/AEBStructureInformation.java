package org.inek.dataportal.psy.aeb.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "AEBStructureInformation", schema = "psy")
public class AEBStructureInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "siId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BaseInformation">
    @OneToOne
    @JoinColumn(name = "siBaseInformationId")
    private AEBBaseInformation _baseInformation;

    public AEBBaseInformation getBaseInformation() {
        return _baseInformation;
    }

    public void setBaseInformation(AEBBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property BedCount">
    @Column(name = "siBedCount")
    private int _bedCount;

    public int getBedCount() {
        return _bedCount;
    }

    public void setBedCount(int bedCount) {
        _bedCount = bedCount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property TherapyPartCount">
    @Column(name = "siTherapyPartCount")
    private int _therapyPartCount;

    public int getTherapyPartCount() {
        return _therapyPartCount;
    }

    public void setTherapyPartCount(int therapyPartCount) {
        _therapyPartCount = therapyPartCount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property RegionalCare">
    @Column(name = "siRegionalCare")
    private String _regionalCare;

    public String getRegionalCare() {
        return _regionalCare;
    }

    public void setRegionalCare(String regionalCare) {
        _regionalCare = regionalCare;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property SPCenterText">
    @Column(name = "siSPCenterText")
    private String _spCenterText;

    public String getSPCenterText() {
        return _spCenterText;
    }

    public void setSPCenterText(String spCenterText) {
        _spCenterText = spCenterText;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property SPCenter">
    @Column(name = "siSPCenter")
    private Boolean _spCenter;

    public Boolean getSPCenter() {
        return _spCenter;
    }

    public void setSPCenter(Boolean spCenter) {
        _spCenter = spCenter;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccommodationText">
    @Column(name = "siAccommodationText")
    private String _accommodationText;

    public String getAccommodationText() {
        return _accommodationText;
    }

    public void setAccommodationText(String accommodationText) {
        _accommodationText = accommodationText;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccommodationId">
    @Column(name = "siAccommodationId")
    private int _accommodationId;

    public int getAccommodationId() {
        return _accommodationId;
    }

    public void setAccommodationId(int accommodationId) {
        _accommodationId = accommodationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property PsychiatricCombine">
    @Column(name = "siPsychiatricCombine")
    private Boolean _psychiatricCombine;

    public Boolean getPsychiatricCombine() {
        return _psychiatricCombine;
    }

    public void setPsychiatricCombine(Boolean psychiatricCombine) {
        _psychiatricCombine = psychiatricCombine;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CareProvider">
    @Column(name = "siCareProvider")
    private String _careProvider;

    public String getCareProvider() {
        return _careProvider;
    }

    public void setCareProvider(String careProvider) {
        _careProvider = careProvider;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property PerformanceAreas">
    @Column(name = "siPerformanceAreas")
    private String _performanceAreas;

    public String getPerformanceAreas() {
        return _performanceAreas;
    }

    public void setPerformanceAreas(String performanceAreas) {
        _performanceAreas = performanceAreas;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property SocialPsychiatryService">
    @Column(name = "siSocialPsychiatryService")
    private String _socialPsychiatryService;

    public String getSocialPsychiatryService() {
        return _socialPsychiatryService;
    }

    public void setSocialPsychiatryService(String performanceAreas) {
        _socialPsychiatryService = performanceAreas;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AmbulantPerformanceId">
    @Column(name = "siAmbulantPerformanceId")
    private int _ambulantPerformanceId;

    public int getAmbulantPerformanceId() {
        return _ambulantPerformanceId;
    }

    public void setAmbulantPerformanceId(int ambulantPerformanceId) {
        _ambulantPerformanceId = ambulantPerformanceId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AmbulantPerformanceMain">
    @Column(name = "siAmbulantPerformanceMain")
    private String _ambulantPerformanceMain;

    public String getAmbulantPerformanceMain() {
        return _ambulantPerformanceMain;
    }

    public void setAmbulantPerformanceMain(String ambulantPerformanceMain) {
        _ambulantPerformanceMain = ambulantPerformanceMain;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AmbulantStructure">
    @Column(name = "siAmbulantStructure")
    private String _ambulantStructure;

    public String getAmbulantStructure() {
        return _ambulantStructure;
    }

    public void setAmbulantStructure(String ambulantStructure) {
        _ambulantStructure = ambulantStructure;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DismissManagement">
    @Column(name = "siDismissManagement")
    private String _dismissManagement;

    public String getDismissManagement() {
        return _dismissManagement;
    }

    public void setDismissManagement(String dismissManagement) {
        _dismissManagement = dismissManagement;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this._bedCount;
        hash = 29 * hash + this._therapyPartCount;
        hash = 29 * hash + Objects.hashCode(this._regionalCare);
        hash = 29 * hash + Objects.hashCode(this._spCenterText);
        hash = 29 * hash + Objects.hashCode(this._spCenter);
        hash = 29 * hash + Objects.hashCode(this._accommodationText);
        hash = 29 * hash + this._accommodationId;
        hash = 29 * hash + Objects.hashCode(this._psychiatricCombine);
        hash = 29 * hash + Objects.hashCode(this._careProvider);
        hash = 29 * hash + Objects.hashCode(this._performanceAreas);
        hash = 29 * hash + Objects.hashCode(this._socialPsychiatryService);
        hash = 29 * hash + this._ambulantPerformanceId;
        hash = 29 * hash + Objects.hashCode(this._ambulantPerformanceMain);
        hash = 29 * hash + Objects.hashCode(this._dismissManagement);
        return hash;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AEBStructureInformation other = (AEBStructureInformation) obj;
        if (this._bedCount != other._bedCount) {
            return false;
        }
        if (this._therapyPartCount != other._therapyPartCount) {
            return false;
        }
        if (this._accommodationId != other._accommodationId) {
            return false;
        }
        if (this._ambulantPerformanceId != other._ambulantPerformanceId) {
            return false;
        }
        if (!Objects.equals(this._regionalCare, other._regionalCare)) {
            return false;
        }
        if (!Objects.equals(this._spCenterText, other._spCenterText)) {
            return false;
        }
        if (!Objects.equals(this._accommodationText, other._accommodationText)) {
            return false;
        }
        if (!Objects.equals(this._careProvider, other._careProvider)) {
            return false;
        }
        if (!Objects.equals(this._performanceAreas, other._performanceAreas)) {
            return false;
        }
        if (!Objects.equals(this._socialPsychiatryService, other._socialPsychiatryService)) {
            return false;
        }
        if (!Objects.equals(this._ambulantPerformanceMain, other._ambulantPerformanceMain)) {
            return false;
        }
        if (!Objects.equals(this._dismissManagement, other._dismissManagement)) {
            return false;
        }
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        if (!Objects.equals(this._spCenter, other._spCenter)) {
            return false;
        }
        if (!Objects.equals(this._psychiatricCombine, other._psychiatricCombine)) {
            return false;
        }
        return true;
    }

}
