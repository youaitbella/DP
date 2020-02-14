/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.facades;

import org.inek.dataportal.care.entities.BaseData;
import org.inek.dataportal.care.entities.SensitiveDomain;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.care.proof.PpugInfo;
import org.inek.dataportal.common.data.AbstractDataAccess;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public double obtainPatientLimit(int year, SensitiveDomain sensitiveDomain, Shift shift) {
        ensureBaseData(year);
        return _baseData.get(year).stream()
                .filter(c -> c.getSensitiveDomain().equals(sensitiveDomain))
                .filter(c -> c.getShift() == shift)
                .findAny().orElseThrow(() -> new IllegalArgumentException("Unknown sensitive domain / shift"))
                .getPpug();
    }

    public double obtainPart(int year, SensitiveDomain sensitiveDomain, Shift shift) {
        ensureBaseData(year);
        return _baseData.get(year).stream()
                .filter(c -> c.getSensitiveDomain().equals(sensitiveDomain))
                .filter(c -> c.getShift() == shift)
                .findAny().orElseThrow(() -> new IllegalArgumentException("Unknown sensitive domain / shift"))
                .getPart();
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
