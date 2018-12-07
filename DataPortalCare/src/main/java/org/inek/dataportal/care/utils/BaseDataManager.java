package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.entities.BaseData;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.care.facades.BaseDataFacade;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BaseDataManager {

    private List<BaseData> _baseData = new ArrayList<>();

    @Inject
    private BaseDataFacade _baseDataFacade;

    public BaseDataManager() {
        _baseData.addAll(_baseDataFacade.getAllBaseData());
    }

    public BaseDataManager(int year) {
        _baseData.addAll(_baseDataFacade.getAllBaseDataByYear(year));
    }

    public BaseDataManager(List<BaseData> baseData) {
        _baseData.addAll(baseData);
    }

    public double getPpugBySensitivAreaAndShift(int sensitivAreaId, Shift shift) {
        List<BaseData> baseDatas = getBaseData(sensitivAreaId, shift);

        if (baseDatas.size() > 1 || baseDatas.size() < 1) {
            return -1;
        }

        return baseDatas.get(0).getPpug();
    }

    public double getPartBySensitivAreaAndShift(int sensitivAreaId, Shift shift) {
        List<BaseData> baseDatas = getBaseData(sensitivAreaId, shift);

        if (baseDatas.size() > 1 || baseDatas.size() < 1) {
            return -1;
        }

        return baseDatas.get(0).getPart();
    }

    private List<BaseData> getBaseData(int sensitivAreaId, Shift shift) {
        return _baseData.stream()
                .filter(c -> c.getSensitiveAreaId() == sensitivAreaId)
                .filter(c -> c.getShift() == shift)
                .collect(Collectors.toList());
    }
}
