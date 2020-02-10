/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.facades;

import org.inek.dataportal.care.entities.BaseData;
import org.inek.dataportal.care.enums.SensitiveArea;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.common.data.AbstractDataAccess;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lautenti
 */
@ApplicationScoped
public class BaseDataFacade extends AbstractDataAccess {

    public List<BaseData> getAllBaseData() {
        String jpql = "SELECT bd FROM BaseData bd";
        TypedQuery<BaseData> query = getEntityManager().createQuery(jpql, BaseData.class);
        return query.getResultList();
    }

    public List<BaseData> getAllBaseDataByYear(int year) {
        String jpql = "SELECT bd FROM BaseData bd \n" +
                " where bd._validFrom <= :year " +
                "and bd._validTo >= :year";
        TypedQuery<BaseData> query = getEntityManager().createQuery(jpql, BaseData.class);
        query.setParameter("year", year);
        return query.getResultList();
    }

    private Map<Integer,List<BaseData>> _baseData = new HashMap<>();

    private void ensureBaseData(int year) {
        if (_baseData.containsKey(year)) return;
        _baseData.put(year, getAllBaseDataByYear(year));
    }

    public double getPpugBySensitivAreaAndShift(int year, SensitiveArea sensitivArea, Shift shift) {
        List<BaseData> baseDatas = getBaseData(year, sensitivArea, shift);

        if (baseDatas.size() > 1 || baseDatas.size() < 1) {
            return -1;
        }

        return baseDatas.get(0).getPpug();
    }

    public double getPartBySensitivAreaAndShift(int year, SensitiveArea sensitivArea, Shift shift) {
        List<BaseData> baseDatas = getBaseData(year, sensitivArea, shift);

        if (baseDatas.size() > 1 || baseDatas.size() < 1) {
            return -1;
        }

        return baseDatas.get(0).getPart();
    }

    private List<BaseData> getBaseData(int year, SensitiveArea sensitivArea, Shift shift) {
        ensureBaseData(year);
        return _baseData.get(year).stream()
                .filter(c -> c.getSensitiveArea() == sensitivArea)
                .filter(c -> c.getShift() == shift)
                .collect(Collectors.toList());
    }

    public void fillBaseDataToProofs(List<Proof> proofs) {
        for(Proof pr : proofs) {
            // todo fillBaseDataToProof(pr);
        }
    }

    private void fillBaseDataToProof(Proof proof) {
//        proof.setPpug(getPpugBySensitivAreaAndShift(proof.getProofRegulationStation().getSensitiveArea(), proof.getShift()));
//        proof.setPart(getPartBySensitivAreaAndShift(proof.getProofRegulationStation().getSensitiveArea(), proof.getShift()));
    }
}
