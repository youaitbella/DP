/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
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

    // <editor-fold defaultstate="collapsed" desc="SpecificFunctionRequest">
    public SpecificFunctionRequest findSpecificFunctionRequest(int id) {
        return findFresh(SpecificFunctionRequest.class, id);
    }


    public List<SpecificFunctionRequest> listSpecificFunctionRequest(int accountId) {
        String sql = "SELECT s FROM SpecificFunctionRequest sop WHERE s._accountId = :accountId ORDER BY s._id DESC";
        TypedQuery<SpecificFunctionRequest> query = getEntityManager().createQuery(sql, SpecificFunctionRequest.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public void delete(SpecificFunctionRequest statement) {
        remove(statement);
    }

    public boolean existActiveSpecificFunctionRequest(int ik) {
        String jpql = "select c from SpecificFunctionRequest c where c._ik = :ik and c._dataYear = :year and c._statusId < 10";
        TypedQuery<SpecificFunctionRequest> query = getEntityManager().createQuery(jpql, SpecificFunctionRequest.class);
        query.setParameter("ik", ik);
        query.setParameter("year", Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        return query.getResultList().size() == 1;
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
        
        return merge(request);
    }
    // </editor-fold>
  
}
