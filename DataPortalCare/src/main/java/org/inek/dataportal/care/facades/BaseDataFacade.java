/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.facades;

import org.inek.dataportal.care.entities.BaseData;
import org.inek.dataportal.care.entities.SensitiveDomain;
import org.inek.dataportal.care.enums.SensitiveArea;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.care.proof.PpugInfo;
import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.common.data.AbstractDataAccess;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.*;
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

    private Map<Integer, List<BaseData>> _baseData = new HashMap<>();

    private void ensureBaseData(int year) {
        if (_baseData.containsKey(year)) return;
        _baseData.put(year, getAllBaseDataByYear(year));
    }

    public double getPpugBySensitivAreaAndShift(int year, SensitiveDomain sensitiveDomain, Shift shift) {
        List<BaseData> baseDatas = getBaseData(year, sensitiveDomain, shift);

        if (baseDatas.size() > 1 || baseDatas.size() < 1) {
            return -1;
        }

        return baseDatas.get(0).getPpug();
    }

    public double getPartBySensitivAreaAndShift(int year, SensitiveDomain sensitiveDomain, Shift shift) {
        List<BaseData> baseDatas = getBaseData(year, sensitiveDomain, shift);

        if (baseDatas.size() > 1 || baseDatas.size() < 1) {
            return -1;
        }

        return baseDatas.get(0).getPart();
    }

    private List<BaseData> getBaseData(int year, SensitiveDomain sensitivArea, Shift shift) {
        ensureBaseData(year);
        return _baseData.get(year).stream()
                .filter(c -> c.getSensitiveDomain() == sensitivArea)
                .filter(c -> c.getShift() == shift)
                .collect(Collectors.toList());
    }

    public void fillBaseDataToProofs(List<Proof> proofs) {
        for (Proof pr : proofs) {
            // todo fillBaseDataToProof(pr);
        }
    }

    private void fillBaseDataToProof(Proof proof) {
//        proof.setPpug(getPpugBySensitivAreaAndShift(proof.getProofRegulationStation().getSensitiveArea(), proof.getShift()));
//        proof.setPart(getPartBySensitivAreaAndShift(proof.getProofRegulationStation().getSensitiveArea(), proof.getShift()));
    }

    public PpugInfo determineBaseData(final int year, final List<SensitiveDomain> sensitiveDomains, final Shift shift) {
        ensureBaseData(year);
        return _baseData.get(year).stream()
                .filter(baseData -> sensitiveDomains.contains(baseData.getSensitiveDomain()))
                .filter(baseData -> baseData.getShift() == shift)
                .sorted(Comparator.comparingDouble(BaseData::getPpug).thenComparingDouble(BaseData::getPart))
                .map(baseData -> new PpugInfo(baseData.getPpug(), baseData.getPart()))
                .findFirst()
                .orElse(new PpugInfo(0d, 0d));
    }
}
