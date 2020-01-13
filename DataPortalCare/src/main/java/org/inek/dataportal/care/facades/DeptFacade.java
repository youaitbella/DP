/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.facades;

import javafx.util.Pair;
import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptArea;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;
import org.inek.dataportal.common.enums.WorkflowStatus;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lautenti
 */
@Stateless
public class DeptFacade extends AbstractDataAccessWithActionLog {

    private static final String FIELD_STATUS = "status";

    public DeptBaseInformation findDeptBaseInformation(int id) {
        String jpql = "SELECT bi FROM DeptBaseInformation bi WHERE bi._id = :id";
        TypedQuery<DeptBaseInformation> query = getEntityManager().createQuery(jpql, DeptBaseInformation.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<DeptBaseInformation> getAllByStatusAndIk(WorkflowStatus status, int ik) {
        String jpql = "SELECT bi FROM DeptBaseInformation bi WHERE bi._statusId = :status "
                + "and bi._ik = :ik";
        TypedQuery<DeptBaseInformation> query = getEntityManager().createQuery(jpql, DeptBaseInformation.class);
        query.setParameter(FIELD_STATUS, status.getId());
        query.setParameter("ik", ik);
        return query.getResultList();
    }

    public DeptBaseInformation findDeptBaseInformationByIk(int ik) {
        return findDeptBaseInformationByIkAndBaseYear(ik, 2018);
    }


    public DeptBaseInformation findDeptBaseInformationByIkAndBaseYear(int ik, int year) {
        String jpql = "SELECT bi FROM DeptBaseInformation bi WHERE bi._statusId between :minStatus and :maxStatus "
                + "and bi._ik = :ik and bi._year = :year";
        TypedQuery<DeptBaseInformation> query = getEntityManager().createQuery(jpql, DeptBaseInformation.class);
        query.setParameter("ik", ik);
        query.setParameter("year", year);
        query.setParameter("minStatus", WorkflowStatus.Provided.getId());
        query.setParameter("maxStatus", WorkflowStatus.ReProvided.getId());
        try {
            return query.getSingleResult();
        } catch (Exception ex) {
            return new DeptBaseInformation();
        }
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
        return retrieveMandatoryForCare(possibleIks);
    }

    private Set<Integer> retrieveMandatoryForCare(Set<Integer> possibleIks) {
        if (possibleIks.isEmpty()) {
            return possibleIks;
        }
        String sql = "select distinct bipIk from care.DeptInekPrefill where bipIk in (" +
                possibleIks.stream().map(i -> "" + i).collect(Collectors.joining(", ")) +
                ")";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> ikList = query.getResultList();
        return new HashSet<>(ikList);
    }

    public List<Integer> getPossibleDataYears() {
        List<Integer> years = new ArrayList<>();
        years.add(2018);
        return years;
    }

    @Transactional
    public DeptBaseInformation save(DeptBaseInformation deptBaseInformation) {
        if (deptBaseInformation.getCurrentVersion().getId() <= 0) {
            persist(deptBaseInformation.getCurrentVersion());
        }
        if (deptBaseInformation.getId() == -1) {
            persist(deptBaseInformation);
            return deptBaseInformation;
        }
        // sadly from new wards only the first is stored when saving structural changes :(
        for (Dept dept : deptBaseInformation.getDepts()) {
            if (dept.getId() <= 0) {
                persist(dept);
            }
            for (DeptWard ward : dept.getDeptWards()) {
                if (ward.getId() <= 0) {
                    persist(ward);
                }
            }
        }

        return merge(deptBaseInformation);
    }

    public void deleteBaseInformation(DeptBaseInformation info) {
        remove(info);
    }

    public void prefillDeptsForBaseInformation(DeptBaseInformation info) {
        String sql = "select bipdept, bipArea, bipRequired, bipDeptNumber, bipSensitiveArea, bipLocationP21, bipSeeArea\n"
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
            dept.setDeptNumber(((String) record[3]).trim());
            dept.setSensitiveArea((String) record[4]);
            dept.setLocation((String) record[5]);
            dept.setSeeDeptAreaId((int) record[6]);
            info.addDept(dept);
        });
    }

    public List<DeptBaseInformation> getAllByStatus(WorkflowStatus status, int baseYear) {
        String jpql = "SELECT bi FROM DeptBaseInformation bi WHERE bi._statusId = :status and bi._year = :baseYear ";
        TypedQuery<DeptBaseInformation> query = getEntityManager().createQuery(jpql, DeptBaseInformation.class);
        query.setParameter(FIELD_STATUS, status.getId());
        query.setParameter("baseYear", baseYear);
        return query.getResultList();
    }

    public Set<Integer> findP21LocationCodesForIkAndYear(int ik, int year) {
        String sql = "select ilLocationCode \n" +
                "from dbo.p21IkLocation \n" +
                "where ilIk = " + ik + "\n" +
                "and ilDataYear = " + year;

        @SuppressWarnings("unchecked")
        List<Integer> result = getEntityManager().createNativeQuery(sql).getResultList();

        return new HashSet<>(result);
    }

    public List<String> findStationNamesForPrefill(int ik, int year) {
        String sql = "select distinct prsStationName\n" +
                "from care.ProofRegulationStation\n" +
                "where prsIk = " + ik;

        @SuppressWarnings("unchecked")
        List<String> result = getEntityManager().createNativeQuery(sql).getResultList();
        return result;
    }

    public Boolean isValidFab(String fab) {
        String sql = "select *\n" +
                "from dbo.listDept\n" +
                "where DeIsPsych = 0\n" +
                "and DeIsPseudo = 0\n" +
                "and deId < 9000\n" +
                "and DeCharId = '" + fab + "'";
        @SuppressWarnings("unchecked")
        List<Object> resultList = getEntityManager().createNativeQuery(sql).getResultList();
        return resultList.size() > 0;
    }

    public List<DeptArea> getAreas() {
        String jpql = "select a from DeptArea a";
        TypedQuery<DeptArea> query = getEntityManager().createQuery(jpql, DeptArea.class);
        return query.getResultList();
    }

    public List<Integer> retrievePriorIk(int ik) {
        String sql = "select isnull(prev.cuIK, 0)\n" +
                "from CallCenterDB.dbo.ccCustomer curr\n" +
                "left join CallCenterDB.dbo.CustomerHistory on curr.cuId = chCustomerId\n" +
                "left join CallCenterDB.dbo.ccCustomer prev on chPreviousCustomerId = prev.cuId\n" +
                "where curr.cuIK = " + ik;
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> priorIk = query.getResultList();
        return priorIk;
    }

}
