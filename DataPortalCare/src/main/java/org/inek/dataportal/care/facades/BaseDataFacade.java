/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.facades;

import javafx.util.Pair;
import org.inek.dataportal.care.entities.BaseData;
import org.inek.dataportal.care.entities.SensitiveDomain;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.care.proof.PpugInfo;
import org.inek.dataportal.common.data.AbstractDataAccess;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.*;

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

    public SensitiveDomain determineSignificantDomain(final int year, final List<String> sensitiveDomains) {
        ensureBaseData(year);
        List<Pair<BaseData, BaseData>> pairs = new ArrayList<>();
        _baseData.get(year).stream()
                .filter(baseData -> sensitiveDomains.contains(baseData.getSensitiveDomain().getName()))
                .filter(baseData -> baseData.getShift() == Shift.DAY)
                .forEach(baseDataDay -> {
                    BaseData baseDataNight = _baseData.get(year).stream()
                            .filter(baseData -> baseData.getSensitiveDomain().equals(baseDataDay.getSensitiveDomain()))
                            .filter(baseData -> baseData.getShift() == Shift.NIGHT)
                            .findAny()
                            .get();
                    pairs.add(new Pair<>(baseDataDay, baseDataNight));
                });

        return pairs.stream().sorted(
                Comparator.<Pair<BaseData, BaseData>>comparingDouble(p -> p.getKey().getPpug())
                        .thenComparingDouble(p -> p.getValue().getPpug())
                        .thenComparingDouble(p -> p.getKey().getPart())
                        .thenComparingDouble(p -> p.getValue().getPart()))
                .findFirst().get().getKey().getSensitiveDomain();
    }
}
