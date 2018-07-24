/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.KhComparison.facade;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.KhComparison.entities.AEBBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.OccupationalCategory;
import org.inek.dataportal.common.data.KhComparison.entities.StructureInformation;

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

    public List<AEBBaseInformation> getAllByStatusAndIk(WorkflowStatus status, int ik, int typ) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._statusId = :status "
                + "and bi._ik = :ik "
                + "and bi._typ = :typ";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("status", status.getId());
        query.setParameter("ik", ik);
        query.setParameter("typ", typ);
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

    public List<Integer> getAllowedIks(int accountId, int year, int typ) {
        String sql = "select distinct aaiIK from dbo.AccountAdditionalIK\n"
                + "where aaiAccountId = " + accountId + "\n"
                + "and aaiIK not in (\n"
                + "select biIk from psy.AEBBaseInformation\n"
                + "where biDataYear >= " + year + " \n"
                + "and biTyp = " + typ + ")";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> result = query.getResultList();
        return result;
    }

    public List<Integer> getUsedDataYears(int ik, int typ) {
        String sql = "select distinct biDataYear from psy.AEBBaseInformation\n"
                + "where biIk = " + ik + "\n"
                + "and biTyp = " + typ;
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
