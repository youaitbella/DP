/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.insurance.specificfunction.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.insurance.specificfunction.entity.AgreedCenter;
import org.inek.dataportal.insurance.specificfunction.entity.AgreedRemunerationKeys;
import org.inek.dataportal.insurance.specificfunction.entity.SpecificFunctionAgreement;
import org.inek.dataportal.common.specificfunction.entity.CenterName;
import org.inek.dataportal.common.specificfunction.entity.RelatedName;
import org.inek.dataportal.common.specificfunction.entity.SpecificFunction;
import org.inek.dataportal.common.specificfunction.entity.SpecificFunctionRequest;
import org.inek.dataportal.common.specificfunction.entity.TypeExtraCharge;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class SpecificFunctionFacade extends AbstractDataAccess {

    private static final String YEAR = "year";
    private static final String IK = "ik";
    private static final String STATUS_HIGH = "statusHigh";
    private static final String STATUS_LOW = "statusLow";
    private static final String MANAGED_IKS = "managedIks";
    private static final String ACCOUNT_IDS = "accountIds";
    private static final String CODE = "code";
    private static final String ACCOUNT_ID = "accountId";

    //<editor-fold defaultstate="collapsed" desc="Specific Function Agreement">
    public SpecificFunctionAgreement findSpecificFunctionAgreement(int id) {
        return findFresh(SpecificFunctionAgreement.class, id);
    }

    public SpecificFunctionAgreement findSpecificFunctionAgreementByCode(String code) {
        String jpql = "SELECT s FROM SpecificFunctionAgreement s WHERE s._code = :code";
        TypedQuery<SpecificFunctionAgreement> query = getEntityManager().
                createQuery(jpql, SpecificFunctionAgreement.class);
        query.setParameter(CODE, code);
        try {
            return query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean SpecificFunctionAgreementExists(String code) {
        SpecificFunctionAgreement agreement = findSpecificFunctionAgreementByCode(code);
        return agreement != null;
    }

    public List<SpecificFunctionAgreement> obtainSpecificFunctionAgreements(int accountId, int year,
            WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String jpql = "SELECT s FROM SpecificFunctionAgreement s "
                + "WHERE s._accountId = :accountId "
                + (year > 0 ? "   and s._dataYear = :year " : "")
                + "   and s._statusId between :statusLow and :statusHigh ORDER BY s._id DESC";
        TypedQuery<SpecificFunctionAgreement> query = getEntityManager().
                createQuery(jpql, SpecificFunctionAgreement.class);
        query.setParameter(ACCOUNT_ID, accountId);
        if (year > 0) {
            query.setParameter(YEAR, year);
        }
        query.setParameter(STATUS_LOW, statusLow.getId());
        query.setParameter(STATUS_HIGH, statusHigh.getId());
        return query.getResultList();
    }

    public List<Account> loadAgreementAccountsForYear(Set<Integer> accountIds, int year, WorkflowStatus statusLow,
            WorkflowStatus statusHigh) {
        String jpql = "select distinct a "
                + "from Account a "
                + "join SpecificFunctionAgreement s "
                + "where a._id = s._accountId "
                + (year > 0 ? "    and s._dataYear = :year " : "")
                + "    and s._statusId between :statusLow and :statusHigh and s._accountId in :accountIds";
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        if (year > 0) {
            query.setParameter(YEAR, year);
        }
        query.setParameter(STATUS_LOW, statusLow.getId());
        query.setParameter(STATUS_HIGH, statusHigh.getId());
        query.setParameter(ACCOUNT_IDS, accountIds);
        return query.getResultList();
    }

    public Set<Integer> getAgreementCalcYears(Set<Integer> accountIds) {
        String jpql = "select s._dataYear from SpecificFunctionAgreement s where s._accountId in :accountIds and s._statusId >= 10";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter(ACCOUNT_IDS, accountIds);
        @SuppressWarnings("unchecked") HashSet<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }

    public SpecificFunctionAgreement saveSpecificFunctionAgreement(SpecificFunctionAgreement agreement) {
        if (agreement.getStatus() == WorkflowStatus.Unknown) {
            agreement.setStatus(WorkflowStatus.New);
        }

        if (agreement.getId() == -1) {
            persist(agreement);
            return agreement;
        }

        return merge(agreement);
    }

    public void deleteSpecificFunctionAgreement(SpecificFunctionAgreement agreement) {
        remove(agreement);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Common">
    public List<CenterName> getCenterNames() {
        return findAll(CenterName.class)
                .stream()
                .filter(n -> n.getId() > 0)
                .sorted((n1, n2) -> (n1.getId() == -1 ? "ZZZ" : n1.getName()).compareTo((n2.getId() == -1 ? "ZZZ" : n2.
                getName())))
                .collect(Collectors.toList());
    }

    public List<RelatedName> getRelatedNames() {
        return findAll(RelatedName.class)
                .stream()
                .filter(n -> n.getId() > 0)
                .sorted((n1, n2) -> (n1.getId() == -1 ? "ZZZ" : n1.getName()).compareTo((n2.getId() == -1 ? "ZZZ" : n2.
                getName())))
                .collect(Collectors.toList());
    }

    public List<SpecificFunction> getSpecificFunctions(boolean includeOther) {
        return findAll(SpecificFunction.class)
                .stream()
                .filter(f -> includeOther || f.getId() > 0)
                .sorted((f1, f2) -> (f1.getId() == -1 ? 999 : f1.getId()) - (f2.getId() == -1 ? 999 : f2.getId()))
                .collect(Collectors.toList());
    }

    public List<TypeExtraCharge> getTypeChargeExtra() {
        return findAll(TypeExtraCharge.class)
                .stream()
                .filter(n -> n.getId() > 0)
                .sorted((f1, f2) -> (f1.getId() == -1 ? 999 : f1.getId()) - (f2.getId() == -1 ? 999 : f2.getId()))
                .collect(Collectors.toList());
    }
    //</editor-fold>

    public SpecificFunctionRequest findSpecificFunctionRequestByCode(String code) {
        String jpql = "select spf from SpecificFunctionRequest spf where spf._code = :code and spf._statusId = 10";
        TypedQuery<SpecificFunctionRequest> query = getEntityManager().createQuery(jpql, SpecificFunctionRequest.class);
        query.setParameter(CODE, code);
        try {
            return query.getSingleResult();
        } catch (Exception ex) {
            return new SpecificFunctionRequest();
        }
    }

}
