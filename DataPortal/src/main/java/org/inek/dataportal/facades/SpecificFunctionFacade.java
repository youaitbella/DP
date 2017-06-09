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
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.specificfunction.AgreedCenter;
import org.inek.dataportal.entities.specificfunction.AgreedRemunerationKeys;
import org.inek.dataportal.entities.specificfunction.CenterName;
import org.inek.dataportal.entities.specificfunction.RelatedName;
import org.inek.dataportal.entities.specificfunction.RequestAgreedCenter;
import org.inek.dataportal.entities.specificfunction.RequestProjectedCenter;
import org.inek.dataportal.entities.specificfunction.SpecificFunction;
import org.inek.dataportal.entities.specificfunction.SpecificFunctionAgreement;
import org.inek.dataportal.entities.specificfunction.SpecificFunctionRequest;
import org.inek.dataportal.entities.specificfunction.TypeExtraCharge;
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

    //<editor-fold defaultstate="collapsed" desc="Specific Function Request">
    public SpecificFunctionRequest findSpecificFunctionRequest(int id) {
        return findFresh(SpecificFunctionRequest.class, id);
    }
    
    public List<SpecificFunctionRequest> obtainSpecificFunctionRequests(int accountId, int year, 
            WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String jpql = "SELECT s FROM SpecificFunctionRequest s "
                + "WHERE s._accountId = :accountId and s._dataYear = :year and s._statusId between :statusLow and :statusHigh ORDER BY s._id DESC";
        TypedQuery<SpecificFunctionRequest> query = getEntityManager().createQuery(jpql, SpecificFunctionRequest.class);
        query.setParameter("accountId", accountId);
        query.setParameter("year", year);
        query.setParameter("statusLow", statusLow.getId());
        query.setParameter("statusHigh", statusHigh.getId());
        return query.getResultList();
    }
    
    public boolean existActiveSpecificFunctionRequest(int ik) {
        String jpql = "select s from SpecificFunctionRequest s where s._ik = :ik and s._dataYear = :year and s._statusId < 10";
        TypedQuery<SpecificFunctionRequest> query = getEntityManager().createQuery(jpql, SpecificFunctionRequest.class);
        query.setParameter("ik", ik);
        query.setParameter("year", Utils.getTargetYear(Feature.SPECIFIC_FUNCTION));
        return query.getResultList().size() == 1;
    }
    
    public List<Account> loadRequestAccountsForYear(Set<Integer> accountIds, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String jpql = "select distinct a "
                + "from Account a join SpecificFunctionRequest s "
                + "where a._id = s._accountId and s._dataYear = :year "
                + "    and s._statusId between :statusLow and :statusHigh and s._accountId in :accountIds";
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        query.setParameter("year", year);
        query.setParameter("statusLow", statusLow.getId());
        query.setParameter("statusHigh", statusHigh.getId());
        query.setParameter("accountIds", accountIds);
        return query.getResultList();
    }
    
    public Set<Integer> getRequestCalcYears(Set<Integer> accountIds) {
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
    
    public void deleteSpecificFunctionRequest(SpecificFunctionRequest statement) {
        remove(statement);
    }
    
    public List<Account> getInekAccounts() {
        String sql = "select distinct account.*\n"
                + "from spf.RequestMaster\n"
                + "join CallCenterDB.dbo.ccCustomer on rmIk = cuIK\n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "join CallCenterDB.dbo.ccCalcInformation on caId = ciCalcAgreementId and rmDataYear-1 = ciDataYear \n"
                + "join CallCenterDB.dbo.mapCustomerReportAgent on ciId = mcraCalcInformationId\n"
                + "join CallCenterDB.dbo.ccAgent on mcraAgentId = agId\n"
                + "left join dbo.Account on agEMail = acMail\n"
                + "where agActive = 1 and agDomainId in ('O', 'E')\n"
                + "     and rmStatusId = 10 \n"
                + "     and mcraReportTypeId in (1, 3) \n"
                + "     and rmDataYear = " + Utils.getTargetYear(Feature.SPECIFIC_FUNCTION);
        Query query = getEntityManager().createNativeQuery(sql, Account.class);
        @SuppressWarnings("unchecked") List<Account> result = query.getResultList();
        return result;
    }
    
    public List<SpecificFunctionRequest> getCalcBasicsForInek(int dataYear) {
        String jpql = "select spf from SpecificFunctionRequest spf where spf._statusId = 10 and spf._dataYear = :dataYear";
        TypedQuery<SpecificFunctionRequest> query = getEntityManager().createQuery(jpql, SpecificFunctionRequest.class);
        query.setParameter("dataYear", dataYear);
        List<SpecificFunctionRequest> result = query.getResultList();
        return result;
    }
    
    public SpecificFunctionRequest findSpecificFunctionRequestByCode(String code) {
        String jpql = "select spf from SpecificFunctionRequest spf where spf._code = :code";
        TypedQuery<SpecificFunctionRequest> query = getEntityManager().createQuery(jpql, SpecificFunctionRequest.class);
        query.setParameter("code", code);
        try {
            return query.getSingleResult();
        } catch (Exception ex) {
            return new SpecificFunctionRequest();
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Specific Function Agreement">
    public SpecificFunctionAgreement findSpecificFunctionAgreement(int id) {
        return findFresh(SpecificFunctionAgreement.class, id);
    }
    
    public List<SpecificFunctionAgreement> obtainSpecificFunctionAgreements(int accountId, int year, 
            WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String jpql = "SELECT s FROM SpecificFunctionAgreement s "
                + "WHERE s._accountId = :accountId and s._dataYear = :year and s._statusId between :statusLow and :statusHigh ORDER BY s._id DESC";
        TypedQuery<SpecificFunctionAgreement> query = getEntityManager().createQuery(jpql, SpecificFunctionAgreement.class);
        query.setParameter("accountId", accountId);
        query.setParameter("year", year);
        query.setParameter("statusLow", statusLow.getId());
        query.setParameter("statusHigh", statusHigh.getId());
        return query.getResultList();
    }
    
    public List<Account> loadAgreementAccountsForYear(Set<Integer> accountIds, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String jpql = "select distinct a "
                + "from Account a "
                + "join SpecificFunctionAgreement s "
                + "where a._id = s._accountId and s._dataYear = :year "
                + "    and s._statusId between :statusLow and :statusHigh and s._accountId in :accountIds";
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        query.setParameter("year", year);
        query.setParameter("statusLow", statusLow.getId());
        query.setParameter("statusHigh", statusHigh.getId());
        query.setParameter("accountIds", accountIds);
        return query.getResultList();
    }
    
    public Set<Integer> getAgreementCalcYears(Set<Integer> accountIds) {
        String jpql = "select s._dataYear from SpecificFunctionAgreement s where s._accountId in :accountIds and s._statusId >= 10";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("accountIds", accountIds);
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
        
        for (AgreedCenter item : agreement.getAgreedCenters()) {
            if (item.getId() == -1) {
                persist(item);
            } else {
                merge(item);
            }
        }
        
        for (AgreedRemunerationKeys item : agreement.getRemunerationKeys()) {
            if (item.getId() == -1) {
                persist(item);
            } else {
                merge(item);
            }
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
                .sorted((n1, n2) -> (n1.getId() == -1 ? "ZZZ" : n1.getName()).compareTo((n2.getId() == -1 ? "ZZZ" : n2.getName())))
                .collect(Collectors.toList());
    }
    
    public List<RelatedName> getRelatedNames() {
        return findAll(RelatedName.class)
                .stream()
                .filter(n -> n.getId() > 0)
                .sorted((n1, n2) -> (n1.getId() == -1 ? "ZZZ" : n1.getName()).compareTo((n2.getId() == -1 ? "ZZZ" : n2.getName())))
                .collect(Collectors.toList());
    }
    
    public List<SpecificFunction> getSpecificFunctions() {
        return findAll(SpecificFunction.class)
                .stream()
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
}
