package org.inek.dataportal.care.bo;

import org.inek.dataportal.care.entities.DeptWard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.inek.dataportal.common.utils.DateUtils.getMaxDate;

public class AggregatedWards {
    private Date _validFrom;
    private Date _validTo;
    private List<DeptWard> _wards = new ArrayList<>();

    private String _wardName;
    private int _locationCode21;
    private int _locationCodeVz;

    private int _beds;
    private boolean _hasDifferentBedCount;

    public AggregatedWards(DeptWard ward) {
        _locationCode21 = ward.getLocationCodeP21();
        _locationCodeVz = ward.getLocationCodeVz();
        _wardName = ward.getWardName();
        _validFrom = ward.getValidFrom();
        _validTo = ward.getValidTo();
        _beds = ward.getBedCount();
        _wards.add(ward);
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
        _wards.add(ward);
    }

    public Date getValidFrom() {
        return _validFrom;
    }

    public Date getValidTo() {
        return getMaxDate().equals(_validTo) ? null : _validTo;
    }

    public List<DeptWard> getWards() {
        return _wards;
    }

    public String getSensitiveAreas() {
        return _wards.stream().map(w -> w.getDept().getSensitiveArea()).collect(Collectors.joining(", "));
    }

    public String getDeptNames() {
        return _wards.stream().map(DeptWard::getDeptName).collect(Collectors.joining(", "));
    }

    public String getFabs() {
        return _wards.stream().map(DeptWard::getFab).collect(Collectors.joining(", "));
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

    public Set<Integer> getDistinctBedCounts() {
        return _wards.stream().map(DeptWard::getBedCount).collect(Collectors.toSet());
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

}
