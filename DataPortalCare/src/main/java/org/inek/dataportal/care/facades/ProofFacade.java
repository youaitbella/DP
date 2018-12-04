/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.facades;

import javafx.util.Pair;
import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.DeptStation;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;
import org.inek.dataportal.common.enums.WorkflowStatus;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author lautenti
 */
@Stateless
public class ProofFacade extends AbstractDataAccessWithActionLog {

    public ProofRegulationBaseInformation findBaseInformation(int id) {
        String jpql = "SELECT bi FROM ProofRegulationBaseInformation bi WHERE bi._id = :id";
        TypedQuery<ProofRegulationBaseInformation> query = getEntityManager().createQuery(jpql, ProofRegulationBaseInformation.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<DeptBaseInformation> getAllByStatusAndIk(WorkflowStatus status, int ik) {
        String jpql = "SELECT bi FROM DeptBaseInformation bi WHERE bi._statusId = :status "
                + "and bi._ik = :ik";
        TypedQuery<DeptBaseInformation> query = getEntityManager().createQuery(jpql, DeptBaseInformation.class);
        query.setParameter("status", status.getId());
        query.setParameter("ik", ik);
        return query.getResultList();
    }

    private Set<Pair<Integer, Integer>> retrieveIkYearPairs(Collection<Integer> iks) {
        if (iks.isEmpty()) {
            return new HashSet<>();
        }
        String ikList = iks.stream().map(ik -> "" + ik).collect(Collectors.joining(", "));
        String sql = "select distinct dbiIk, dbiYear \n"
                + "from care.DeptBaseInformation \n"
                + "where dbiIk in (" + ikList + ") "
                + "and dbiStatusId < 200\n";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Object[]> objects = query.getResultList();

        return objects.stream().map(obj -> new Pair<>((int) obj[0], (int) obj[1])).collect(Collectors.toSet());
    }

    public Set<Integer> retrievePossibleIks(Set<Integer> allowedIks) {
        //todo machen
        /*Set<Pair<Integer, Integer>> existingIkYearPairs = retrieveIkYearPairs(allowedIks);
        List<Integer> possibleYears = getPossibleDataYears();

        Set<Integer> possibleIks = allowedIks
                .stream()
                .filter(ik -> possibleYears
                .stream()
                .anyMatch(year -> !existingIkYearPairs.contains(new Pair<>(ik, year))))
                .collect(Collectors.toSet());
        return possibleIks;*/

        return allowedIks;
    }

    public List<Integer> getPossibleDataYears() {
        List<Integer> years = new ArrayList<>();
        years.add(2017);
        return years;
    }

    public ProofRegulationBaseInformation save(ProofRegulationBaseInformation deptBaseInformation) {
        if (deptBaseInformation.getId() == -1) {
            persist(deptBaseInformation);
            return deptBaseInformation;
        }
        return merge(deptBaseInformation);
    }

    public void deleteBaseInformation(DeptBaseInformation info) {
        remove(info);
    }

    public List<DeptBaseInformation> getAllByStatus(WorkflowStatus status) {
        String jpql = "SELECT bi FROM DeptBaseInformation bi WHERE bi._statusId = :status ";
        TypedQuery<DeptBaseInformation> query = getEntityManager().createQuery(jpql, DeptBaseInformation.class);
        query.setParameter("status", status.getId());
        return query.getResultList();
    }

    public List<DeptStation> getDeptStationsForProof(int ik, int year) {
        String jpql = "SELECT ds " +
                "FROM DeptStation ds " +
                "JOIN ds._dept d " +
                "JOIN d._baseInformation bi " +
                "WHERE bi._statusId = 10 " +
                "AND bi._ik = :ik " +
                "AND bi._year = :year";
        TypedQuery<DeptStation> query = getEntityManager().createQuery(jpql, DeptStation.class);
        query.setParameter("ik", ik);
        query.setParameter("year", year);
        return query.getResultList();
    }

    public Set<Integer> getValidYears(int ik) {
        return null;
    }

    public Set<Integer> getValidQuarter(int ik, int year) {
        return null;
    }
}
