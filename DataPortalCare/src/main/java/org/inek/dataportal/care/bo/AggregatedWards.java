package org.inek.dataportal.care.bo;

import org.inek.dataportal.care.entities.DeptStation;
import org.inek.dataportal.care.enums.SensitiveArea;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class AggregatedWards {
    private Date _validFrom;
    private Date _validTo;
    private List<DeptStation> _stations = new ArrayList<>();

    private SensitiveArea _sensitiveArea;
    private String _deptNames;
    private String _fabs;
    private String _wardName;
    private int _locationCode21;
    private int _locationCodeVz;

    private int _beds;

    public AggregatedWards(Date validFrom, Date validTo, List<DeptStation> stations) {
        this._validFrom = validFrom;
        this._validTo = validTo;
        this._stations = stations;
        setValues(stations);
    }

    public AggregatedWards(List<DeptStation> stations) {
        this._validFrom = stations.stream()
                .min(Comparator.comparing(c -> c.getValidFrom().getTime()))
                .get().getValidFrom();
        this._validTo = stations.stream()
                .max(Comparator.comparing(c -> c.getValidTo().getTime()))
                .get().getValidTo();
        this._stations = stations;
        setValues(stations);
    }

    public Date getValidFrom() {
        return _validFrom;
    }

    public Date getValidTo() {
        return _validTo;
    }

    public List<DeptStation> getStations() {
        return _stations;
    }

    public SensitiveArea getSensitiveArea() {
        return _sensitiveArea;
    }

    public String getDeptNames() {
        return _deptNames;
    }

    public String getFabs() {
        return _fabs;
    }

    public String getWardName() {
        return _wardName;
    }

    public int getBeds() {
        return _beds;
    }

    public int getLocationCode21() {
        return _locationCode21;
    }

    public void setLocationCode21(int locationCode21) {
        this._locationCode21 = locationCode21;
    }

    public int getLocationCodeVz() {
        return _locationCodeVz;
    }

    public void setLocationCodeVz(int locationCodeVz) {
        this._locationCodeVz = locationCodeVz;
    }

    private void setValues(List<DeptStation> stations) {

    }
}
