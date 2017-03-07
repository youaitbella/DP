/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.entities.specificfunction.CenterName;
import org.inek.dataportal.entities.specificfunction.RequestAgreedCenter;
import org.inek.dataportal.entities.specificfunction.RequestProjectedCenter;
import org.inek.dataportal.entities.specificfunction.SpecificFunction;
import org.inek.dataportal.entities.specificfunction.SpecificFunctionRequest;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class SpecificFunctionFacade extends AbstractDataAccess {

    public SpecificFunctionRequest findSpecificFunctionRequest(int id) {
        return findFresh(SpecificFunctionRequest.class, id);
    }


    public List<SpecificFunctionRequest> obtainSpecificFunctionRequests(int accountId) {
        String jpql = "SELECT s FROM SpecificFunctionRequest s WHERE s._accountId = :accountId ORDER BY s._id DESC";
        TypedQuery<SpecificFunctionRequest> query = getEntityManager().createQuery(jpql, SpecificFunctionRequest.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public List<SpecificFunctionRequest> obtainSpecificFunctionRequests(int accountId, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String jpql = "SELECT s FROM SpecificFunctionRequest s WHERE s._accountId = :accountId and s._dataYear = :year and s._statusId between :statusLow and :statusHigh ORDER BY s._id DESC";
        TypedQuery<SpecificFunctionRequest> query = getEntityManager().createQuery(jpql, SpecificFunctionRequest.class);
        query.setParameter("accountId", accountId);
        query.setParameter("year", year);
        query.setParameter("statusLow", statusLow.getValue());
        query.setParameter("statusHigh", statusHigh.getValue());
        return query.getResultList();
    }
    
    public void deleteSpecificFunctionRequest(SpecificFunctionRequest statement) {
        remove(statement);
    }

    public boolean existActiveSpecificFunctionRequest(int ik) {
        String jpql = "select s from SpecificFunctionRequest s where s._ik = :ik and s._dataYear = :year and s._statusId < 10";
        TypedQuery<SpecificFunctionRequest> query = getEntityManager().createQuery(jpql, SpecificFunctionRequest.class);
        query.setParameter("ik", ik);
        query.setParameter("year", Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        return query.getResultList().size() == 1;
    }

    public Set<Integer> checkAccountsForYear(Set<Integer> accountIds, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String jpql = "select s._accountId from SpecificFunctionRequest s where s._dataYear = :year and s._statusId between :statusLow and :statusHigh";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("year", year);
        query.setParameter("statusLow", statusLow.getValue());
        query.setParameter("statusHigh", statusHigh.getValue());
        @SuppressWarnings("unchecked") HashSet<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }

    public Set<Integer> getCalcYears(Set<Integer> accountIds) {
        String jpql = "select s._dataYear from SpecificFunctionRequest s where s._accountId in :accountIds and s._statusId >= 10";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("accountIds", accountIds);
        @SuppressWarnings("unchecked") HashSet<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }
    
    public SpecificFunctionRequest saveSpecificFunctionRequest(SpecificFunctionRequest request) {
        if (request.getStatus() == WorkflowStatus.Unknown) {
            request.setStatus(WorkflowStatus.New);
        }

        if (request.getId() == -1) {
            persist(request);
            return request;
        }
        
        //todo: save lists
        for (RequestProjectedCenter item : request.getRequestProjectedCenters()) {
            if (item.getId() == -1) {
                persist(item);
            } else {
                merge(item);
            }
        }
        for (RequestAgreedCenter item : request.getRequestAgreedCenters()) {
            if (item.getId() == -1) {
                persist(item);
            } else {
                merge(item);
            }
        }
        
        return merge(request);
    }

    public List<CenterName> getCenterNames() {
        return findAll(CenterName.class)
                .stream()
                .filter(n -> n.getId() > 0)
                .sorted((n1, n2) -> n1.getName().compareTo(n2.getName()))
                .collect(Collectors.toList());
    }
  
    public List<SpecificFunction> getSpecificFunctions() {
        return findAll(SpecificFunction.class)
                .stream()
                .filter(n -> n.getId() > 0)
                .collect(Collectors.toList());
    }
  
}
