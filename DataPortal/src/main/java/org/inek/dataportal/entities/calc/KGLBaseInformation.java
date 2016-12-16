/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "KGLBaseInformation", schema = "calc")
public class KGLBaseInformation implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "biID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property BasicsDrgId">
    @Column(name = "biBasicsDrgId")
    private int _basicsDrgId = -1;

    public int getBasicsDrgId() {
        return _basicsDrgId;
    }

    public void setBasicsDrgId(int _basicsDrgId) {
        this._basicsDrgId = _basicsDrgId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Year">
    @Column(name = "biYear")
    private int _year = Calendar.getInstance().get(Calendar.YEAR);

    public int getYear() {
        return _year;
    }

    public void setYear(int _year) {
        this._year = _year;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property SumCalcCost">
    @Column(name = "biSumCalcCost")
    private float _sumCalcCost;

    public float getSumCalcCost() {
        return _sumCalcCost;
    }

    public void setSumCalcCost(float _sumCalcCost) {
        this._sumCalcCost = _sumCalcCost;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CntCaseInStation">
    @Column(name = "biCntCaseInStation")
    private int _cntCaseInStation;

    public int getCntCaseInStation() {
        return _cntCaseInStation;
    }

    public void setCntCaseInStation(int _cntCaseInStation) {
        this._cntCaseInStation = _cntCaseInStation;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CntCasePartialStation">
    @Column(name = "biCntCasePartialStation")
    private int _cntCasePartialStation;

    public int getCntCasePartialStation() {
        return _cntCasePartialStation;
    }

    public void setCntCasePartialStation(int _cntCasePartialStation) {
        this._cntCasePartialStation = _cntCasePartialStation;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property DaysPartialStation">
    @Column(name = "biDaysPartialStation")
    private int _daysPartialStation;

    public int getDaysPartialStation() {
        return _daysPartialStation;
    }

    public void setDaysPartialStation(int _daysPartialStation) {
        this._daysPartialStation = _daysPartialStation;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property PatientEscort">
    @Column(name = "biPatientEscort")
    private int _patientEscort;

    public int getPatientEscort() {
        return _patientEscort;
    }

    public void setPatientEscort(int _patientEscort) {
        this._patientEscort = _patientEscort;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property PreStation">
    @Column(name = "biPreStation")
    private int _preStation = -1;
    
    public int getPreStation() {
        return _preStation;
    }

    public void setPreStation(int _preStation) {
        this._preStation = _preStation;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Beds">
    @Column(name = "biBeds")
    private int _beds = -1;
    
    public int getBeds() {
        return _beds;
    }

    public void setBeds(int _beds) {
        this._beds = _beds;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CntPartial">
    @Column(name = "biCntPartial")
    private int _cntPartial = -1;
    
    public int getCntPartial() {
        return _cntPartial;
    }

    public void setCntPartial(int _cntPartial) {
        this._cntPartial = _cntPartial;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CntLocation">
    @Column(name = "biCntLocation")
    private int _cntLocation = -1;
    
    public int getCntLocation() {
        return _cntLocation;
    }

    public void setCntLocation(int _cntLocation) {
        this._cntLocation = _cntLocation;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property DifLocationSupply">
    @Column(name = "biDifLocationSupply")
    private boolean _difLocationSupply;
    
    public boolean getDifLocationSupply() {
        return _difLocationSupply;
    }

    public void setDifLocationSupply(boolean _difLocationSupply) {
        this._difLocationSupply = _difLocationSupply;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property SpecialUnit">
    @Column(name = "biSpecialUnit")
    private boolean _specialUnit;
    
    public boolean getSpecialUnit() {
        return _specialUnit;
    }

    public void setSpecialUnit(boolean _specialUnit) {
        this._specialUnit = _specialUnit;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CentralFocus">
    @Column(name = "biCentralFocus")
    private boolean _centralFocus;
    
    public boolean getCentralFocus() {
        return _centralFocus;
    }

    public void setCentralFocus(boolean _centralFocus) {
        this._centralFocus = _centralFocus;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property NeonatLvl">
    @Column(name = "biNeonatLvl")
    private int _neonatLvl;
    
    public int getNeonatLvl() {
        return _neonatLvl;
    }

    public void setNeonatLvl(int _neonatLvl) {
        this._neonatLvl = _neonatLvl;
    }
    // </editor-fold>
}
