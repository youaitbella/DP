/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.KhComparison.facade;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.util.Pair;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.KhComparison.entities.AEBBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.OccupationalCategory;
import org.inek.dataportal.common.data.KhComparison.entities.StructureInformation;
import org.inek.dataportal.common.enums.CustomerTyp;

/**
 *
 * @author lautenti
 */
@Stateless
public class AEBFacade extends AbstractDataAccess {

    public AEBBaseInformation findAEBBaseInformation(int id) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._id = :id";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public AEBBaseInformation findAEBBaseInformation(int ik, int year, int typ, WorkflowStatus status) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._year = :year "
                + "and bi._ik = :ik "
                + "and bi._typ = :typ "
                + "and bi._statusId = :status";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("ik", ik);
        query.setParameter("year", year);
        query.setParameter("typ", typ);
        query.setParameter("status", status.getId());
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public StructureInformation findStructureInformation(int id) {
        String sql = "SELECT si FROM StructureInformation si WHERE si._id = :id";
        TypedQuery<StructureInformation> query = getEntityManager().createQuery(sql, StructureInformation.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<AEBBaseInformation> getAllByStatus(WorkflowStatus status) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._statusId = :status";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("status", status.getId());
        return query.getResultList();
    }

    public List<AEBBaseInformation> getAllByStatusAndIk(WorkflowStatus status, int ik, CustomerTyp typ) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._statusId = :status "
                + "and bi._ik = :ik "
                + "and bi._typ = :typ";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("status", status.getId());
        query.setParameter("ik", ik);
        query.setParameter("typ", typ.id());
        return query.getResultList();
    }

    public boolean structureInformaionAvailable(int ik) {
        String sql = "SELECT si FROM StructureInformation si WHERE si._ik = :ik";
        TypedQuery<StructureInformation> query = getEntityManager().createQuery(sql, StructureInformation.class);
        query.setParameter("ik", ik);
        return !query.getResultList().isEmpty();
    }

    public StructureInformation getStructureInformationByIk(int ik) {
        String sql = "SELECT si FROM StructureInformation si WHERE si._ik = :ik order by si._validFrom desc";
        TypedQuery<StructureInformation> query = getEntityManager().createQuery(sql, StructureInformation.class);
        query.setParameter("ik", ik);

        return findStructureInformationFromDate(query.getResultList(), new Date());
    }

    public StructureInformation findStructureInformationFromDate(List<StructureInformation> infos, Date date) {
        if (!infos.stream()
                .filter(c -> c.getValidFrom().equals(date))
                .collect(Collectors.toList())
                .isEmpty()) {
            return infos.stream()
                    .filter(c -> c.getValidFrom().equals(date))
                    .collect(Collectors.toList())
                    .get(0);
        }
        if (infos.stream()
                .anyMatch(c -> c.getValidFrom().after(date))) {
            if (infos.stream()
                    .anyMatch(c -> c.getValidFrom().before(date))) {
                return infos.stream()
                        .filter(c -> c.getValidFrom().before(date))
                        .sorted((p1, p2) -> p2.getValidFrom().compareTo(p1.getValidFrom()))
                        .collect(Collectors.toList())
                        .get(0);
            } else {
                return infos.stream()
                        .sorted((p1, p2) -> p1.getValidFrom().compareTo(p2.getValidFrom()))
                        .collect(Collectors.toList())
                        .get(0);
            }
        } else {
            return infos.stream()
                    .sorted((p1, p2) -> p2.getValidFrom().compareTo(p1.getValidFrom()))
                    .collect(Collectors.toList())
                    .get(0);
        }
    }

    public List<StructureInformation> getAllStructureInformationByIk(int ik) {
        String sql = "SELECT si FROM StructureInformation si WHERE si._ik = :ik order by si._validFrom";
        TypedQuery<StructureInformation> query = getEntityManager().createQuery(sql, StructureInformation.class);
        query.setParameter("ik", ik);
        return query.getResultList();
    }

    private Set<Pair<Integer, Integer>> retrieveIkYearPairs(Collection<Integer> iks, CustomerTyp typ) {
        if(iks.isEmpty()){return new HashSet<>();}
        String ikList = iks.stream().map(ik -> "" + ik).collect(Collectors.joining(", "));
        String sql = "select distinct biIk, biDataYear \n"
                + "from psy.AEBBaseInformation \n"
                + "where biIk in (" + ikList + ") \n"
                + "    and biTyp = " + typ.id();
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") List<Object[]> objects = query.getResultList();

        return objects.stream().map(obj -> new Pair<>((int) obj[0], (int) obj[1])).collect(Collectors.toSet());
    }

    public Set<Integer> retrievePossibleIks(Set<Integer> allowedIks, CustomerTyp typ) {
        Set<Pair<Integer, Integer>> existingIkYearPairs = retrieveIkYearPairs(allowedIks, typ);
        List<Integer> possibleYears = getPossibleDataYears();

        Set<Integer> possibleIks = allowedIks
                .stream()
                .filter(ik -> possibleYears
                .stream()
                .anyMatch(year -> !existingIkYearPairs.contains(new Pair<>(ik, year))))
                .collect(Collectors.toSet());
        return possibleIks;
    }

    public List<Integer> getPossibleDataYears() {
        List<Integer> years = new ArrayList<>();
        IntStream.rangeClosed(2018, Year.now().getValue() + 1).
                forEach((int y) -> years.add(y));
        return years;
    }

    public List<Integer> getUsedDataYears(int ik, CustomerTyp typ) {
        String sql = "select distinct biDataYear from psy.AEBBaseInformation\n"
                + "where biIk = " + ik + "\n"
                + "and biTyp = " + typ.id();
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> result = query.getResultList();
        return result;
    }

    @Transactional
    public AEBBaseInformation save(AEBBaseInformation aebBaseInformation) {
        if (aebBaseInformation.getId() == -1) {
            persist(aebBaseInformation);
            return aebBaseInformation;
        }
        return merge(aebBaseInformation);
    }

    @Transactional
    public StructureInformation save(StructureInformation info) {
        if (info.getId() == 0) {
            persist(info);
            return info;
        }
        return merge(info);
    }

    public void deleteBaseInformation(AEBBaseInformation info) {
        remove(info);
    }

    public List<OccupationalCategory> getOccupationalCategories() {
        return findAll(OccupationalCategory.class);
    }

}
