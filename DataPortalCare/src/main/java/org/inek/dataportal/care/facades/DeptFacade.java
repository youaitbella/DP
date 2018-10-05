/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.facades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.util.Pair;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;
import org.inek.dataportal.common.enums.WorkflowStatus;

/**
 *
 * @author lautenti
 */
@Stateless
public class DeptFacade extends AbstractDataAccessWithActionLog {

    public DeptBaseInformation findDeptBaseInformation(int id) {
        String sql = "SELECT bi FROM DeptBaseInformation bi WHERE bi._id = :id";
        TypedQuery<DeptBaseInformation> query = getEntityManager().createQuery(sql, DeptBaseInformation.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public DeptBaseInformation findDeptBaseInformation(int ik, int year, WorkflowStatus status) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._year = :year "
                + "and bi._ik = :ik "
                + "and bi._typ = :typ "
                + "and bi._statusId = :status";
        TypedQuery<DeptBaseInformation> query = getEntityManager().createQuery(sql, DeptBaseInformation.class);
        query.setParameter("ik", ik);
        query.setParameter("year", year);
        query.setParameter("status", status.getId());
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public List<DeptBaseInformation> getAllByStatusAndIk(WorkflowStatus status, int ik) {
        String sql = "SELECT bi FROM DeptBaseInformation bi WHERE bi._statusId = :status "
                + "and bi._ik = :ik";
        TypedQuery<DeptBaseInformation> query = getEntityManager().createQuery(sql, DeptBaseInformation.class);
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
        Set<Pair<Integer, Integer>> existingIkYearPairs = retrieveIkYearPairs(allowedIks);
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
        years.add(2017);
        return years;
    }

    @Transactional
    public DeptBaseInformation save(DeptBaseInformation deptBaseInformation) {
        if (deptBaseInformation.getId() == -1) {
            persist(deptBaseInformation);
            return deptBaseInformation;
        }
        return merge(deptBaseInformation);
    }

    public void deleteBaseInformation(DeptBaseInformation info) {
        remove(info);
    }

    public void prefillDeptsForBaseInformation(DeptBaseInformation info) {
        String sql = "select bipdept, bipArea, bipRequired\n"
                + "from care.DeptInekPrefill\n"
                + "where bipIk = " + info.getIk() + " \n"
                + "and bipyear = " + info.getYear() + "";

        @SuppressWarnings("unchecked")
        List<Object[]> results = getEntityManager().createNativeQuery(sql).getResultList();

        info.getDepts().clear();

        results.stream().forEach((record) -> {
            Dept dept = new Dept();
            dept.setBaseInformation(info);
            dept.setDeptName((String) record[0]);
            dept.setDeptArea((int) record[1]);
            dept.setRequired((Boolean) record[2]);
            info.addDept(dept);
        });
    }

    public List<DeptBaseInformation> getAllByStatus(WorkflowStatus status) {
        String sql = "SELECT bi FROM DeptBaseInformation bi WHERE bi._statusId = :status ";
        TypedQuery<DeptBaseInformation> query = getEntityManager().createQuery(sql, DeptBaseInformation.class);
        query.setParameter("status", status.getId());
        return query.getResultList();
    }

}
