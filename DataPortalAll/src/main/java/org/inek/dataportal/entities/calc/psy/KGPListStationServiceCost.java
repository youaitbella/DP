/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc.psy;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.entities.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListStationServiceCost", schema = "calc")
public class KGPListStationServiceCost implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sscID", updatable = false, nullable = false)
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costCenterID">
    @Column(name = "sscCostCenterID")
    @Documentation(name = "Kostenstelle", rank = 10)
    private int _costCenterID;

    public int getCostCenterID() {
        return _costCenterID;
    }

    public void setCostCenterID(int costCenterID) {
        this._costCenterID = costCenterID;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costCenterNumber">
    @Column(name = "sscCostCenterNumber")
    @Documentation(name = "Nummer der Kostenstelle", rank = 20)
    private String _costCenterNumber = "";

    public String getCostCenterNumber() {
        return _costCenterNumber;
    }

    public void setCostCenterNumber(String costCenterNumber) {
        this._costCenterNumber = costCenterNumber;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _station">
    @Column(name = "sscStation")
    @Documentation(name = "Station", rank = 30)
    private String _station = "";

    @Size(max = 200)
    public String getStation() {
        return _station;
    }

    public void setStation(String station) {
        this._station = station;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _bedCnt">
    @Column(name = "sscBedCnt")
    @Documentation(name = "Anzahl Betten", rank = 40)
    private int _bedCnt;

    public int getBedCnt() {
        return _bedCnt;
    }

    public void setBedCnt(int bedCnt) {
        this._bedCnt = bedCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _receivingStation">
    @Column(name = "sscReceivingStation")
    @Documentation(name = "Bettenführende Aufnahmestation", rank = 50)
    private boolean _receivingStation;

    public boolean isReceivingStation() {
        return _receivingStation;
    }

    public void setReceivingStation(boolean receivingStation) {
        this._receivingStation = receivingStation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _regularCareDays">
    @Column(name = "sscRegularCareDays")
    @Documentation(name = "Summe Pflegetage Regelbehandlung", rank = 60)
    private int _regularCareDays;

    public int getRegularCareDays() {
        return _regularCareDays;
    }

    public void setRegularCareDays(int regularCareDays) {
        this._regularCareDays = regularCareDays;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _regularWeight">
    @Column(name = "sscRegularWeight")
    @Documentation(name = "Summe Gewichtungspunkte Regelbehandlung", rank = 70)
    private int _regularWeight;

    public int getRegularWeight() {
        return _regularWeight;
    }

    public void setRegularWeight(int regularWeight) {
        this._regularWeight = regularWeight;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _intensiveCareDays">
    @Column(name = "sscIntensiveCareDays")
    @Documentation(name = "Summe Pflegetage Intensiv", rank = 80)
    private int _intensiveCareDays;

    public int getIntensiveCareDays() {
        return _intensiveCareDays;
    }

    public void setIntensiveCareDays(int intensiveCareDays) {
        this._intensiveCareDays = intensiveCareDays;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _intensiveWeight">
    @Column(name = "sscIntensiveWeight")
    @Documentation(name = "Summe Gewichtungspunkte Intensiv", rank = 90)
    private int _intensiveWeight;

    public int getIntensiveWeight() {
        return _intensiveWeight;
    }

    public void setIntensiveWeight(int intensiveWeight) {
        this._intensiveWeight = intensiveWeight;
    }
    //</editor-fold>

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    //<editor-fold defaultstate="collapsed" desc="Property _medicalServiceCnt">
    @Column(name = "sscMedicalServiceCnt")
    @Documentation(name = "Anzahl VK ÄD", rank = 100)
    private double _medicalServiceCnt;

    public double getMedicalServiceCnt() {
        return _medicalServiceCnt;
    }

    public void setMedicalServiceCnt(double medicalServiceCnt) {
        this._medicalServiceCnt = medicalServiceCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _nursingServiceCnt">
    @Column(name = "sscNursingServiceCnt")
    @Documentation(name = "Anzahl VK PD", rank = 110)
    private double _nursingServiceCnt;

    public double getNursingServiceCnt() {
        return _nursingServiceCnt;
    }

    public void setNursingServiceCnt(double nursingServiceCnt) {
        this._nursingServiceCnt = nursingServiceCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _psychologistCnt">
    @Column(name = "sscPsychologistCnt")
    @Documentation(name = "Anzahl VK Psychologen", rank = 120)
    private double _psychologistCnt;

    public double getPsychologistCnt() {
        return _psychologistCnt;
    }

    public void setPsychologistCnt(double psychologistCnt) {
        this._psychologistCnt = psychologistCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _socialWorkerCnt">
    @Column(name = "sscSocialWorkerCnt")
    @Documentation(name = "Anzahl VK Sozialarbeiter", rank = 130)
    private double _socialWorkerCnt;

    public double getSocialWorkerCnt() {
        return _socialWorkerCnt;
    }

    public void setSocialWorkerCnt(double socialWorkerCnt) {
        this._socialWorkerCnt = socialWorkerCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _specialTherapistCnt">
    @Column(name = "sscSpecialTherapistCnt")
    @Documentation(name = "Anzahl VK Spezialtherapeuten", rank = 140)
    private double _specialTherapistCnt;

    public double getSpecialTherapistCnt() {
        return _specialTherapistCnt;
    }

    public void setSpecialTherapistCnt(double specialTherapistCnt) {
        this._specialTherapistCnt = specialTherapistCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _functionalServiceCnt">
    @Column(name = "sscFunctionalServiceCnt")
    @Documentation(name = "Anzahl VK FD", rank = 150)
    private double _functionalServiceCnt;

    public double getFunctionalServiceCnt() {
        return _functionalServiceCnt;
    }

    public void setFunctionalServiceCnt(double functionalServiceCnt) {
        this._functionalServiceCnt = functionalServiceCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _medicalServiceAmount">
    @Column(name = "sscMedicalServiceAmount")
    @Documentation(name = "Kosten ÄD", rank = 160)
    private int _medicalServiceAmount;

    public int getMedicalServiceAmount() {
        return _medicalServiceAmount;
    }

    public void setMedicalServiceAmount(int medicalServiceAmount) {
        this._medicalServiceAmount = medicalServiceAmount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _nursingServiceAmount">
    @Column(name = "sscNursingServiceAmount")
    @Documentation(name = "Kosten PD", rank = 170)
    private int _nursingServiceAmount;

    public int getNursingServiceAmount() {
        return _nursingServiceAmount;
    }

    public void setNursingServiceAmount(int nursingServiceAmount) {
        this._nursingServiceAmount = nursingServiceAmount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _psychologistAmount">
    @Column(name = "sscPsychologistAmount")
    @Documentation(name = "Kosten Psychologen", rank = 180)
    private int _psychologistAmount;

    public int getPsychologistAmount() {
        return _psychologistAmount;
    }

    public void setPsychologistAmount(int psychologistAmount) {
        this._psychologistAmount = psychologistAmount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _socialWorkerAmount">
    @Column(name = "sscSocialWorkerAmount")
    @Documentation(name = "Kosten Sozialarbeiter", rank = 190)
    private int _socialWorkerAmount;

    public int getSocialWorkerAmount() {
        return _socialWorkerAmount;
    }

    public void setSocialWorkerAmount(int socialWorkerAmount) {
        this._socialWorkerAmount = socialWorkerAmount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _specialTherapistAmount">
    @Column(name = "sscSpecialTherapistAmount")
    @Documentation(name = "Kosten Spezialtherapeuten", rank = 200)
    private int _specialTherapistAmount;

    public int getSpecialTherapistAmount() {
        return _specialTherapistAmount;
    }

    public void setSpecialTherapistAmount(int specialTherapistAmount) {
        this._specialTherapistAmount = specialTherapistAmount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _functionalServiceAmount">
    @Column(name = "sscFunctionalServiceAmount")
    @Documentation(name = "Kosten FD", rank = 210)
    private int _functionalServiceAmount;

    public int getFunctionalServiceAmount() {
        return _functionalServiceAmount;
    }

    public void setFunctionalServiceAmount(int functionalServiceAmount) {
        this._functionalServiceAmount = functionalServiceAmount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _medicalInfrastructureAmount">
    @Column(name = "sscMedicalInfrastructureAmount")
    @Documentation(name = "Kosten med. Infra.", rank = 220)
    private int _medicalInfrastructureAmount;

    public int getMedicalInfrastructureAmount() {
        return _medicalInfrastructureAmount;
    }

    public void setMedicalInfrastructureAmount(int medicalInfrastructureAmount) {
        this._medicalInfrastructureAmount = medicalInfrastructureAmount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _nonMedicalInfrastructureAmount">
    @Column(name = "sscNonMedicalInfrastructureAmount")
    @Documentation(name = "Kosten nicht med. Infra.", rank = 230)
    private int _nonMedicalInfrastructureAmount;

    public int getNonMedicalInfrastructureAmount() {
        return _nonMedicalInfrastructureAmount;
    }

    public void setNonMedicalInfrastructureAmount(int nonMedicalInfrastructureAmount) {
        this._nonMedicalInfrastructureAmount = nonMedicalInfrastructureAmount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "sscBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "sscBaseInformationId")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _generalMapping">
    @Column(name = "sscGeneralMapping")
    private boolean _generalMapping;

    public boolean isGeneralMapping() {
        return _generalMapping;
    }

    public void setGeneralMapping(boolean generalMapping) {
        this._generalMapping = generalMapping;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _addictionMapping">
    @Column(name = "sscAddictionMapping")
    private boolean _addictionMapping;

    public boolean isAddictionMapping() {
        return _addictionMapping;
    }

    public void setAddictionMapping(boolean addictionMapping) {
        this._addictionMapping = addictionMapping;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _gerontoPsyMapping">
    @Column(name = "sscGerontoPsyMapping")
    private boolean _gerontoPsyMapping;

    public boolean isGerontoPsyMapping() {
        return _gerontoPsyMapping;
    }

    public void setGerontoPsyMapping(boolean gerontoPsyMapping) {
        this._gerontoPsyMapping = gerontoPsyMapping;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _childYouthMapping">
    @Column(name = "sscChildYouthMapping")
    private boolean _childYouthMapping;

    public boolean isChildYouthMapping() {
        return _childYouthMapping;
    }

    public void setChildYouthMapping(boolean childYouthMapping) {
        this._childYouthMapping = childYouthMapping;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _psychosomaticMapping">
    @Column(name = "sscPsychosomaticMapping")
    private boolean _psychosomaticMapping;

    public boolean isPsychosomaticMapping() {
        return _psychosomaticMapping;
    }

    public void setPsychosomaticMapping(boolean psychosomaticMapping) {
        this._psychosomaticMapping = psychosomaticMapping;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _psyPvMapping">
    @Column(name = "sscPsyPvMapping")
    private String _psyPvMapping = "";

    public String getPsyPvMapping() {
        StringBuilder sb = new StringBuilder();

        if (_generalMapping) {
            sb.append("A, ");
        }
        if (_addictionMapping) {
            sb.append("S, ");
        }
        if (_gerontoPsyMapping) {
            sb.append("G, ");
        }
        if (_childYouthMapping) {
            sb.append("KJP, ");
        }
        if (_psychosomaticMapping) {
            sb.append("P, ");
        }

        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }

    public void setPsyPvMapping(String psyPvMapping) {
        List<String> vals = Arrays.asList(psyPvMapping.split(","));
        _generalMapping = _addictionMapping = _gerontoPsyMapping = _childYouthMapping = _psychosomaticMapping = false;
        for (String val : vals) {
            switch (val.trim().toUpperCase()) {
                case "A":
                    _generalMapping = true;
                    break;
                case "S":
                    _addictionMapping = true;
                    break;
                case "G":
                    _gerontoPsyMapping = true;
                    break;
                case "KJP":
                    _childYouthMapping = true;
                    break;
                case "P":
                    _psychosomaticMapping = true;
                    break;
                default: throw new IllegalArgumentException("unknown Psy PV value " + psyPvMapping);
            }
        }
        this._psyPvMapping = getPsyPvMapping();
    }
    //</editor-fold>
    
        //<editor-fold defaultstate="collapsed" desc="Property _nonMedicalInfrastructureAmount">
    @Column(name = "sscOccupancy")
    @Documentation(name = "Belegung", rank = 230)
    private int _occupancy;

    public int getOccupancy() {
        return _occupancy;
    }

    public void setOccupancy(int occupancy) {
        this._occupancy = occupancy;
    }
    //</editor-fold>

    public String getUtilization() {
        if (_bedCnt == 0) {
            return "";
        }
        double result = 0;
        
        if(_occupancy == 1 || _occupancy == 3) {
            result = Math.round((_regularCareDays + _intensiveCareDays) * 1000.0d / (_bedCnt * 365)) / 10d;
        }
        else if (_occupancy == 2) {
            result = Math.round((_regularCareDays + _intensiveCareDays) * 1000.0d / (_bedCnt * (365-104))) / 10d;
        }
//        double result = Math.round((_regularCareDays + _intensiveCareDays) * 1000.0d / (_bedCnt * 365)) / 10d;
        return "" + result + "%";
    }

    public KGPListStationServiceCost() {
    }

    public KGPListStationServiceCost(int baseInformationId) {
        _baseInformationId = baseInformationId;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 59 * hash + this._costCenterID;
        hash = 59 * hash + Objects.hashCode(this._station);
        hash = 59 * hash + this._bedCnt;
        hash = 59 * hash + (this._receivingStation ? 1 : 0);
        hash = 59 * hash + this._regularCareDays;
        hash = 59 * hash + this._regularWeight;
        hash = 59 * hash + this._intensiveCareDays;
        hash = 59 * hash + this._intensiveWeight;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this._medicalServiceCnt) ^ (Double.doubleToLongBits(this._medicalServiceCnt) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this._nursingServiceCnt) ^ (Double.doubleToLongBits(this._nursingServiceCnt) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this._psychologistCnt) ^ (Double.doubleToLongBits(this._psychologistCnt) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this._socialWorkerCnt) ^ (Double.doubleToLongBits(this._socialWorkerCnt) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this._specialTherapistCnt) ^ (Double.doubleToLongBits(this._specialTherapistCnt) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this._functionalServiceCnt) ^ (Double.doubleToLongBits(this._functionalServiceCnt) >>> 32));
        hash = 59 * hash + this._medicalServiceAmount;
        hash = 59 * hash + this._nursingServiceAmount;
        hash = 59 * hash + this._psychologistAmount;
        hash = 59 * hash + this._socialWorkerAmount;
        hash = 59 * hash + this._specialTherapistAmount;
        hash = 59 * hash + this._functionalServiceAmount;
        hash = 59 * hash + this._medicalInfrastructureAmount;
        hash = 59 * hash + this._nonMedicalInfrastructureAmount;
        hash = 59 * hash + this._baseInformationId;
        hash = 59 * hash + (this._generalMapping ? 1 : 0);
        hash = 59 * hash + (this._addictionMapping ? 1 : 0);
        hash = 59 * hash + (this._gerontoPsyMapping ? 1 : 0);
        hash = 59 * hash + (this._childYouthMapping ? 1 : 0);
        hash = 59 * hash + (this._psychosomaticMapping ? 1 : 0);
        return hash;
    }

    @Override
    @SuppressWarnings({"CyclomaticComplexity", "JavaNCSS"})
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListStationServiceCost)) {
            return false;
        }
        final KGPListStationServiceCost other = (KGPListStationServiceCost) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._costCenterID != other._costCenterID) {
            return false;
        }
        if (!this._costCenterNumber.equals(other._costCenterNumber)) {
            return false;
        }
        if (this._bedCnt != other._bedCnt) {
            return false;
        }
        if (this._receivingStation != other._receivingStation) {
            return false;
        }
        if (this._regularCareDays != other._regularCareDays) {
            return false;
        }
        if (this._regularWeight != other._regularWeight) {
            return false;
        }
        if (this._intensiveCareDays != other._intensiveCareDays) {
            return false;
        }
        if (this._intensiveWeight != other._intensiveWeight) {
            return false;
        }
        if (Double.doubleToLongBits(this._medicalServiceCnt) != Double.doubleToLongBits(other._medicalServiceCnt)) {
            return false;
        }
        if (Double.doubleToLongBits(this._nursingServiceCnt) != Double.doubleToLongBits(other._nursingServiceCnt)) {
            return false;
        }
        if (Double.doubleToLongBits(this._psychologistCnt) != Double.doubleToLongBits(other._psychologistCnt)) {
            return false;
        }
        if (Double.doubleToLongBits(this._socialWorkerCnt) != Double.doubleToLongBits(other._socialWorkerCnt)) {
            return false;
        }
        if (Double.doubleToLongBits(this._specialTherapistCnt) != Double.doubleToLongBits(other._specialTherapistCnt)) {
            return false;
        }
        if (Double.doubleToLongBits(this._functionalServiceCnt) != Double.doubleToLongBits(other._functionalServiceCnt)) {
            return false;
        }
        if (this._medicalServiceAmount != other._medicalServiceAmount) {
            return false;
        }
        if (this._nursingServiceAmount != other._nursingServiceAmount) {
            return false;
        }
        if (this._psychologistAmount != other._psychologistAmount) {
            return false;
        }
        if (this._socialWorkerAmount != other._socialWorkerAmount) {
            return false;
        }
        if (this._specialTherapistAmount != other._specialTherapistAmount) {
            return false;
        }
        if (this._functionalServiceAmount != other._functionalServiceAmount) {
            return false;
        }
        if (this._medicalInfrastructureAmount != other._medicalInfrastructureAmount) {
            return false;
        }
        if (this._nonMedicalInfrastructureAmount != other._nonMedicalInfrastructureAmount) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (this._generalMapping != other._generalMapping) {
            return false;
        }
        if (this._addictionMapping != other._addictionMapping) {
            return false;
        }
        if (this._gerontoPsyMapping != other._gerontoPsyMapping) {
            return false;
        }
        if (this._childYouthMapping != other._childYouthMapping) {
            return false;
        }
        if (this._psychosomaticMapping != other._psychosomaticMapping) {
            return false;
        }
        if (!Objects.equals(this._station, other._station)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListStationServiceCost[ sscID=" + _id + " ]";
    }
    //</editor-fold>

    public void copyStationServiceCost(KGPListStationServiceCost item) {
        this._costCenterID = item._costCenterID;
        this._costCenterNumber = item._costCenterNumber;
        this._station = item._station;
        this._bedCnt = item._bedCnt;
        this._receivingStation = item._receivingStation;
        this._regularCareDays = item._regularCareDays;
        this._regularWeight = item._regularWeight;
        this._intensiveCareDays = item._intensiveCareDays;
        this._intensiveWeight = item._intensiveWeight;
        this._medicalServiceCnt = item._medicalServiceCnt;
        this._nursingServiceCnt = item._nursingServiceCnt;
        this._psychologistCnt = item._psychologistCnt;
        this._socialWorkerCnt = item._socialWorkerCnt;
        this._specialTherapistCnt = item._specialTherapistCnt;
        this._functionalServiceCnt = item._functionalServiceCnt;
        this._medicalServiceAmount = item._medicalServiceAmount;
        this._nursingServiceAmount = item._nursingServiceAmount;
        this._psychologistAmount = item._psychologistAmount;
        this._socialWorkerAmount = item._socialWorkerAmount;
        this._specialTherapistAmount = item._specialTherapistAmount;
        this._functionalServiceAmount = item._functionalServiceAmount;
        this._medicalInfrastructureAmount = item._medicalInfrastructureAmount;
        this._nonMedicalInfrastructureAmount = item._nonMedicalInfrastructureAmount;
        this._baseInformationId = item._baseInformationId;
        this._generalMapping = item._generalMapping;
        this._addictionMapping = item._addictionMapping;
        this._gerontoPsyMapping = item._gerontoPsyMapping;
        this._childYouthMapping = item._childYouthMapping;
        this._psychosomaticMapping = item._psychosomaticMapping;
        this._psyPvMapping = item._psyPvMapping;
        this._occupancy = item._occupancy;
    }
}
