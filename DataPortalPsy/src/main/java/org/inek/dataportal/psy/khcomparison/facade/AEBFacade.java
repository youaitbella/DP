/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.psy.khcomparison.entity.AEBBaseInformation;

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

    public List<AEBBaseInformation> getAllByStatus(WorkflowStatus status) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._statusId = :status";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("status", status.getId());
        return query.getResultList();
    }

    public List<Integer> getAllowedIks(int accountId, int year) {
        String sql = "select distinct aaiIK from dbo.AccountAdditionalIK\n"
                + "where aaiAccountId = " + accountId + "\n"
                + "and aaiIK not in (\n"
                + "select biIk from psy.AEBBaseInformation\n"
                + "where biDataYear >= " + year + ")";
        Query query = getEntityManager().createNativeQuery(sql);
        List<Integer> result = query.getResultList();
        return result;
    }

    public List<Integer> getUsedDataYears(int ik) {
        String sql = "select distinct biDataYear from psy.AEBBaseInformation\n"
                + "where biIk = " + ik + "";
        Query query = getEntityManager().createNativeQuery(sql);
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

    public void deleteBaseInformation(AEBBaseInformation info) {
        remove(info);
    }
}
