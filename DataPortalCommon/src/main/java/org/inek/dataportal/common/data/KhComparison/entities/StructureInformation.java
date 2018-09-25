package org.inek.dataportal.common.data.KhComparison.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "StructureInformation", schema = "psy")
public class StructureInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "siId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Version">
    @Column(name = "siVersion")
    @Version
    private int _version;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "siIk")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        _ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CreatedFrom">
    @Column(name = "siCreatedFrom")
    private int _createdFrom;

    public int getCreatedFrom() {
        return _createdFrom;
    }

    public void setCreatedFrom(int createdFrom) {
        _createdFrom = createdFrom;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Last Change">
    @Column(name = "siLastChangeFrom")
    private int _lastChangeFrom;

    public int getLastChangeFrom() {
        return _lastChangeFrom;
    }

    public void setLastChangeFrom(int lastChangeFrom) {
        _lastChangeFrom = lastChangeFrom;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ValidFrom">
    @Column(name = "siValidFrom")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _validFrom = new Date();

    public Date getValidFrom() {
        return _validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this._validFrom = validFrom;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "siLastChanged")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastChanged = new Date();

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        _lastChanged = lastChanged;
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
    private String _regionalCare = "";

    public String getRegionalCare() {
        return _regionalCare;
    }

    public void setRegionalCare(String regionalCare) {
        _regionalCare = regionalCare;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property SPCenterText">
    @Column(name = "siSPCenterText")
    private String _spCenterText = "";

    public String getSPCenterText() {
        return _spCenterText;
    }

    public void setSPCenterText(String spCenterText) {
        _spCenterText = spCenterText;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccommodationText">
    @Column(name = "siAccommodationText")
    private String _accommodationText = "";

    public String getAccommodationText() {
        return _accommodationText;
    }

    public void setAccommodationText(String accommodationText) {
        _accommodationText = accommodationText;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CareProvider">
    @Column(name = "siCareProvider")
    private String _careProvider = "";

    public String getCareProvider() {
        return _careProvider;
    }

    public void setCareProvider(String careProvider) {
        _careProvider = careProvider;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property PerformanceAreas">
    @Column(name = "siPerformanceAreas")
    private String _performanceAreas = "";

    public String getPerformanceAreas() {
        return _performanceAreas;
    }

    public void setPerformanceAreas(String performanceAreas) {
        _performanceAreas = performanceAreas;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property SocialPsychiatryService">
    @Column(name = "siSocialPsychiatryService")
    private String _socialPsychiatryService = "";

    public String getSocialPsychiatryService() {
        return _socialPsychiatryService;
    }

    public void setSocialPsychiatryService(String performanceAreas) {
        _socialPsychiatryService = performanceAreas;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AmbulantPerformanceMain">
    @Column(name = "siAmbulantPerformanceMain")
    private String _ambulantPerformanceMain = "";

    public String getAmbulantPerformanceMain() {
        return _ambulantPerformanceMain;
    }

    public void setAmbulantPerformanceMain(String ambulantPerformanceMain) {
        _ambulantPerformanceMain = ambulantPerformanceMain;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AmbulantStructure">
    @Column(name = "siAmbulantStructure")
    private String _ambulantStructure = "";

    public String getAmbulantStructure() {
        return _ambulantStructure;
    }

    public void setAmbulantStructure(String ambulantStructure) {
        _ambulantStructure = ambulantStructure;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DismissManagement">
    @Column(name = "siDismissManagement")
    private String _dismissManagement = "";

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
        hash = 37 * hash + this._version;
        hash = 37 * hash + this._ik;
        hash = 37 * hash + this._createdFrom;
        hash = 37 * hash + this._lastChangeFrom;
        hash = 37 * hash + Objects.hashCode(this._validFrom);
        hash = 37 * hash + Objects.hashCode(this._lastChanged);
        hash = 37 * hash + this._bedCount;
        hash = 37 * hash + this._therapyPartCount;
        hash = 37 * hash + Objects.hashCode(this._regionalCare);
        hash = 37 * hash + Objects.hashCode(this._spCenterText);
        hash = 37 * hash + Objects.hashCode(this._accommodationText);
        hash = 37 * hash + Objects.hashCode(this._careProvider);
        hash = 37 * hash + Objects.hashCode(this._performanceAreas);
        hash = 37 * hash + Objects.hashCode(this._socialPsychiatryService);
        hash = 37 * hash + Objects.hashCode(this._ambulantPerformanceMain);
        hash = 37 * hash + Objects.hashCode(this._ambulantStructure);
        hash = 37 * hash + Objects.hashCode(this._dismissManagement);
        return hash;
    }

    @SuppressWarnings("CyclomaticComplexity")
    @Override
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
        final StructureInformation other = (StructureInformation) obj;
        if (this._version != other._version) {
            return false;
        }
        if (this._ik != other._ik) {
            return false;
        }
        if (this._createdFrom != other._createdFrom) {
            return false;
        }
        if (this._lastChangeFrom != other._lastChangeFrom) {
            return false;
        }
        if (this._bedCount != other._bedCount) {
            return false;
        }
        if (this._therapyPartCount != other._therapyPartCount) {
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
        if (!Objects.equals(this._ambulantStructure, other._ambulantStructure)) {
            return false;
        }
        if (!Objects.equals(this._dismissManagement, other._dismissManagement)) {
            return false;
        }
        if (!Objects.equals(this._validFrom, other._validFrom)) {
            return false;
        }
        if (!Objects.equals(this._lastChanged, other._lastChanged)) {
            return false;
        }
        return true;
    }

}
