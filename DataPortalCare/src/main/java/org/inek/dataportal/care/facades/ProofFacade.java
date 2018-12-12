/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.facades;

import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.DeptStation;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;
import org.inek.dataportal.care.entities.ProofRegulationStation;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;
import org.inek.dataportal.common.enums.WorkflowStatus;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
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

    public List<ProofRegulationBaseInformation> getAllByStatusAndIk(WorkflowStatus status, int ik) {
        String jpql = "SELECT bi FROM ProofRegulationBaseInformation bi WHERE bi._statusId = :status "
                + "and bi._ik = :ik";
        TypedQuery<ProofRegulationBaseInformation> query = getEntityManager().createQuery(jpql, ProofRegulationBaseInformation.class);
        query.setParameter("status", status.getId());
        query.setParameter("ik", ik);
        return query.getResultList();
    }

    public Set<Integer> retrievePossibleIks(Set<Integer> allowedIks) {
        Set<Integer> possibleIks = new HashSet<>();
        Set<Integer> possibleYears = getPossibleYears();

        for (int ik : allowedIks) {
            if (newEntryPossibleForIk(ik, possibleYears)) {
                possibleIks.add(ik);
            }
        }

        return possibleIks;
    }

    public Set<Integer> retrievePossibleYears(int ik) {
        Set<Integer> possibleYearsForCreation = new HashSet<>();
        Set<Integer> possibleYears = getPossibleYears();

        for (int year : possibleYears) {
            if (newEntryPossibleForYear(ik, year)) {
                possibleYearsForCreation.add(year);
            }
        }
        return possibleYearsForCreation;
    }

    public Set<Integer> retrievePossibleQuarter(int ik, int year) {
        Set<Integer> possibleQuarters = new HashSet<>();
        possibleQuarters.add(1);
        possibleQuarters.add(2);
        possibleQuarters.add(3);
        possibleQuarters.add(4);

        List<Object[]> existsQuarters = getQuartersForValidEntrys(ik, year);

        for (Object quarter : existsQuarters) {
            if (possibleQuarters.contains(quarter)) {
                possibleQuarters.remove(quarter);
            }
        }

        return possibleQuarters;
    }

    private boolean newEntryPossibleForYear(int ik, int year) {
        List<Object[]> quartersForValidEntrys = getQuartersForValidEntrys(ik, year);
        return quartersForValidEntrys.size() < 4;
    }


    private boolean newEntryPossibleForIk(int ik, Set<Integer> possibleYears) {
        for (int year : possibleYears) {
            List<Object[]> quartersForValidEntrys = getQuartersForValidEntrys(ik, year);

            if (quartersForValidEntrys.size() < 4) {
                return true;
            }
        }
        return false;
    }

    private List<Object[]> getQuartersForValidEntrys(int ik, int year) {
        String sql = "select prbiQuarter \n" +
                "from care.ProofRegulationBaseInformation \n" +
                "where prbiIk = " + ik + " \n" +
                "and prbiYear = " + year + " \n" +
                "and prbiQuarter in (1,2,3,4) \n" +
                "and prbiStatusId in (0, 3, 10)";

        Query query = getEntityManager().createNativeQuery(sql);

        @SuppressWarnings("unchecked")
        List<Object[]> objects = query.getResultList();

        return objects;
    }

    private Set<Integer> getPossibleYears() {
        Set<Integer> years = new HashSet<>();
        years.add(2019);
        return years;
    }

    public ProofRegulationBaseInformation save(ProofRegulationBaseInformation deptBaseInformation) {
        if (deptBaseInformation.getId() == -1) {
            persist(deptBaseInformation);
            return deptBaseInformation;
        }
        return merge(deptBaseInformation);
    }

    public void deleteBaseInformation(ProofRegulationBaseInformation info) {
        remove(info);
    }

    public List<DeptBaseInformation> getAllByStatus(WorkflowStatus status) {
        String jpql = "SELECT bi FROM DeptBaseInformation bi WHERE bi._statusId = :status ";
        TypedQuery<DeptBaseInformation> query = getEntityManager().createQuery(jpql, DeptBaseInformation.class);
        query.setParameter("status", status.getId());
        return query.getResultList();
    }

    public List<ProofRegulationStation> getStationsForProof(int ik, int year) {
        String jpql = "SELECT ds " +
                "FROM ProofRegulationStation ds " +
                "WHERE ds._ik = :ik " +
                "AND ds._year = :year";
        TypedQuery<ProofRegulationStation> query = getEntityManager().createQuery(jpql, ProofRegulationStation.class);
        query.setParameter("ik", ik);
        query.setParameter("year", year);
        return query.getResultList();
    }

}
