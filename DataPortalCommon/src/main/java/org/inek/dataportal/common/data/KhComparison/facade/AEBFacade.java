/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.KhComparison.facade;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
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
import org.inek.dataportal.common.data.KhComparison.entities.StructureBaseInformation;
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
                + "and bi._status = :status";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("ik", ik);
        query.setParameter("year", year);
        query.setParameter("typ", typ);
        query.setParameter("status", status);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public StructureBaseInformation findStructureBaseInformation(int id) {
        String sql = "SELECT si FROM StructureBaseInformation si WHERE si._id = :id";
        TypedQuery<StructureBaseInformation> query = getEntityManager().createQuery(sql, StructureBaseInformation.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<AEBBaseInformation> getAllByStatus(WorkflowStatus status) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._status = :status";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    public List<AEBBaseInformation> getAllByStatusAndIk(WorkflowStatus status, int ik, CustomerTyp typ) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._status = :status "
                + "and bi._ik = :ik "
                + "and bi._typ = :typ";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("status", status);
        query.setParameter("ik", ik);
        query.setParameter("typ", typ.id());
        return query.getResultList();
    }

    public List<AEBBaseInformation> getAllByStatusAndIk(List<WorkflowStatus> status, Set<Integer> iks, CustomerTyp typ) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._status in :status "
                + "and bi._ik in :iks "
                + "and bi._typ = :typ";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("status", status);
        query.setParameter("iks", iks);
        query.setParameter("typ", typ.id());
        return query.getResultList();
    }

    public boolean structureBaseInformaionAvailable(int ik) {
        String sql = "SELECT si FROM StructureBaseInformation si WHERE si._ik = :ik";
        TypedQuery<StructureBaseInformation> query = getEntityManager().createQuery(sql, StructureBaseInformation.class);
        query.setParameter("ik", ik);
        return !query.getResultList().isEmpty();
    }

    public boolean ikHasModelIntention(int ik) {
        String sql = "select ehComment\n" +
                "--select *\n" +
                "from psy.ExpectedHospital\n" +
                "where ehIk = " + ik + "\n" +
                "and (ehComment like '%MV%'\n" +
                "or ehComment like '%Modellvorhaben%')";
        Query query = getEntityManager().createNativeQuery(sql);
        return query.getResultList().size() > 0;
    }

    public StructureBaseInformation getStructureBaseInformationByIk(int ik) {
        String sql = "SELECT si FROM StructureBaseInformation si WHERE si._ik = :ik";
        TypedQuery<StructureBaseInformation> query = getEntityManager().createQuery(sql, StructureBaseInformation.class);
        query.setParameter("ik", ik);
        return query.getSingleResult();
    }

    private Set<Pair<Integer, Integer>> retrieveIkYearPairs(Collection<Integer> iks, CustomerTyp typ) {
        if (iks.isEmpty()) {
            return new HashSet<>();
        }
        String ikList = iks.stream().map(ik -> "" + ik).collect(Collectors.joining(", "));
        String sql = "select distinct biIk, biDataYear \n"
                + "from psy.AEBBaseInformation \n"
                + "where biIk in (" + ikList + ") \n"
                + "    and biTyp = " + typ.id();
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Object[]> objects = query.getResultList();

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
    public StructureBaseInformation save(StructureBaseInformation info) {
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
