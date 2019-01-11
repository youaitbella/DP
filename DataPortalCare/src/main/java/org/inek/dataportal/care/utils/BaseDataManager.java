package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.entities.BaseData;
import org.inek.dataportal.care.entities.Proof;
import org.inek.dataportal.care.enums.SensitiveArea;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.care.facades.BaseDataFacade;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BaseDataManager {

    private List<BaseData> _baseData = new ArrayList<>();



    public BaseDataManager(BaseDataFacade facade) {
        _baseData.addAll(facade.getAllBaseData());
    }

    public BaseDataManager(int year, BaseDataFacade facade) {
        _baseData.addAll(facade.getAllBaseDataByYear(year));
    }

    public BaseDataManager(List<BaseData> baseData) {
        _baseData.addAll(baseData);
    }

    public double getPpugBySensitivAreaAndShift(SensitiveArea sensitivArea, Shift shift) {
        List<BaseData> baseDatas = getBaseData(sensitivArea, shift);

        if (baseDatas.size() > 1 || baseDatas.size() < 1) {
            return -1;
        }

        return baseDatas.get(0).getPpug();
    }

    public double getPartBySensitivAreaAndShift(SensitiveArea sensitivArea, Shift shift) {
        List<BaseData> baseDatas = getBaseData(sensitivArea, shift);

        if (baseDatas.size() > 1 || baseDatas.size() < 1) {
            return -1;
        }

        return baseDatas.get(0).getPart();
    }

    private List<BaseData> getBaseData(SensitiveArea sensitivArea, Shift shift) {
        return _baseData.stream()
                .filter(c -> c.getSensitiveArea() == sensitivArea)
                .filter(c -> c.getShift() == shift)
                .collect(Collectors.toList());
    }

    public void fillBaseDataToProofs(List<Proof> proofs) {
        for(Proof pr : proofs) {
            fillBaseDataToProof(pr);
        }
    }

    private void fillBaseDataToProof(Proof proof) {
        proof.setPpug(getPpugBySensitivAreaAndShift(proof.getProofRegulationStation().getSensitiveArea(), proof.getShift()));
        proof.setPart(getPartBySensitivAreaAndShift(proof.getProofRegulationStation().getSensitiveArea(), proof.getShift()));
    }
}