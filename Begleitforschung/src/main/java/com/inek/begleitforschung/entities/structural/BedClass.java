package com.inek.begleitforschung.entities.structural;

/**
 *
 * @author muellermi
 */
public class BedClass {

    public BedClass(String level, String levelSort, int hospitalCount, double hospitalFraction, int stateId) {
        _level = level;
        _levelSort = levelSort;
        _hospitalCount = hospitalCount;
        _hospitalFraction = hospitalFraction;
        _stateId = stateId;
    }

    // <editor-fold defaultstate="collapsed" desc="Property Level">
    private String _level;

    public String getLevel() {
        return _level;
    }

    public void setLevel(String level) {
        _level = level;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LevelSort">
    private String _levelSort;

    public String getLevelSort() {
        return _levelSort;
    }

    public void setLevelSort(String levelSort) {
        _levelSort = levelSort;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Hospital count">
    private int _hospitalCount;

    public int getHospitalCount() {
        return _hospitalCount;
    }

    public void setHospitalCount(int hospitalCount) {
        _hospitalCount = hospitalCount;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Hospital fraction">
    private double _hospitalFraction;

    public double getHospitalFraction() {
        return _hospitalFraction;
    }

    public void setHospitalFraction(double hospitalFraction) {
        _hospitalFraction = hospitalFraction;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property StateId">
    private int _stateId;

    public int getStateId() {
        return _stateId;
    }

    public void setStateId(int stateId) {
        _stateId = stateId;
    }
    // </editor-fold>

}
