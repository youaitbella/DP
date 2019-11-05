package org.inek.dataportal.care.bo;

import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.enums.SensitiveArea;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AggregatedWards {
    private Date _validFrom;
    private Date _validTo;
    private List<DeptWard> _stations = new ArrayList<>();

    private SensitiveArea _sensitiveArea;
    private String _deptNames;
    private String _fabs;
    private String _wardName;
    private int _locationCode21;
    private int _locationCodeVz;

    private int _beds;
    private boolean _hasDifferentBedCount;

    public AggregatedWards(Date validFrom, Date validTo, List<DeptWard> stations) {
        this._validFrom = validFrom;
        this._validTo = validTo;
        this._stations = stations;
        setValues(stations);
    }

    public AggregatedWards(List<DeptWard> stations) {
        this._validFrom = stations.stream()
                .min(Comparator.comparing(c -> c.getValidFrom().getTime()))
                .get().getValidFrom();
        this._validTo = stations.stream()
                .max(Comparator.comparing(c -> c.getValidTo().getTime()))
                .get().getValidTo();
        this._stations = stations;
        setValues(stations);
    }

    public AggregatedWards(DeptWard ward) {
        _locationCode21 = ward.getLocationCodeP21();
        _locationCodeVz = ward.getLocationCodeVz();
        _wardName = ward.getWardName();
        _validFrom = ward.getValidFrom();
        _validTo = ward.getValidTo();
        _beds = ward.getBedCount();
        _stations.add(ward);
    }

    public void aggregate(DeptWard ward) {
        assert _locationCode21 == ward.getLocationCodeP21();
        assert _locationCodeVz == ward.getLocationCodeVz();
        assert _wardName.toLowerCase().replace(" ", "")
                .equals(ward.getWardName().toLowerCase().replace(" ", ""));
        assert _validFrom.equals(ward.getValidFrom());
        assert _validTo.equals(ward.getValidTo());
        if (_beds != ward.getBedCount()) {
            _hasDifferentBedCount = true;
        }
        _stations.add(ward);
    }

    public Date getValidFrom() {
        return _validFrom;
    }

    public Date getValidTo() {
        return _validTo;
    }

    public List<DeptWard> getWards() {
        return _stations;
    }

    public String getSensitiveAreas() {
        return _stations.stream().map(w -> w.getDept().getSensitiveArea()).collect(Collectors.joining(", "));
    }

    public String getDeptNames() {
        return _stations.stream().map(DeptWard::getDeptName).collect(Collectors.joining(", "));
    }

    public String getFabs() {
        return _stations.stream().map(DeptWard::getFab).collect(Collectors.joining(", "));
    }

    public String getWardName() {
        return _wardName;
    }

    public int getBeds() {
        return _beds;
    }

    public boolean getDifferentBedCount() {
        return _hasDifferentBedCount;
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

    private void setValues(List<DeptWard> stations) {
        DeptWard station = stations.get(0);
        _wardName = station.getWardName();
        _beds = station.getBedCount();
        _locationCodeVz = station.getLocationCodeVz();
        _locationCode21 = station.getLocationCodeP21();
        concatenateFabs(stations);
        concatenateDeptName(stations);
    }

    private void concatenateFabs(List<DeptWard> stations) {
        _fabs = stations.stream().map(DeptWard::getFab).collect(Collectors.joining(", "));
    }

    private void concatenateDeptName(List<DeptWard> stations) {
        _deptNames = stations.stream().map(DeptWard::getDeptName).collect(Collectors.joining(", "));
    }

}
