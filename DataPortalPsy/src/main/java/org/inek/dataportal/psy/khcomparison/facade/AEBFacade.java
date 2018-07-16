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
import org.inek.dataportal.psy.khcomparison.entity.StructureInformation;

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

    public List<AEBBaseInformation> getAllByStatusAndAccount(WorkflowStatus status, int accId) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._statusId = :status and bi._createdFrom = :accId";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("status", status.getId());
        query.setParameter("accId", accId);
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
        return query.getResultList().get(0);
    }

    public List<Integer> getAllowedIks(int accountId, int year) {
        String sql = "select distinct aaiIK from dbo.AccountAdditionalIK\n"
                + "where aaiAccountId = " + accountId + "\n"
                + "and aaiIK not in (\n"
                + "select biIk from psy.AEBBaseInformation\n"
                + "where biDataYear >= " + year + ")";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> result = query.getResultList();
        return result;
    }

    public List<Integer> getUsedDataYears(int ik) {
        String sql = "select distinct biDataYear from psy.AEBBaseInformation\n"
                + "where biIk = " + ik + "";
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
}
