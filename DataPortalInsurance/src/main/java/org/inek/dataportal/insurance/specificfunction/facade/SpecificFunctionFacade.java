package org.inek.dataportal.insurance.specificfunction.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.util.Pair;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.insurance.specificfunction.entity.SpecificFunctionAgreement;
import org.inek.dataportal.common.specificfunction.entity.CenterName;
import org.inek.dataportal.common.specificfunction.entity.RelatedName;
import org.inek.dataportal.common.specificfunction.entity.SpecificFunction;
import org.inek.dataportal.common.specificfunction.entity.TypeExtraCharge;

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

    public List<SpecificFunctionAgreement> obtainSpecificFunctionAgreements(int accountId, 
            WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String jpql = "SELECT s FROM SpecificFunctionAgreement s "
                + "WHERE s._accountId = :accountId "
                + "   and s._statusId between :statusLow and :statusHigh ORDER BY s._id DESC";
        TypedQuery<SpecificFunctionAgreement> query = getEntityManager().
                createQuery(jpql, SpecificFunctionAgreement.class);
        query.setParameter(ACCOUNT_ID, accountId);
        query.setParameter(STATUS_LOW, statusLow.getId());
        query.setParameter(STATUS_HIGH, statusHigh.getId());
        return query.getResultList();
    }

    public List<Account> loadAgreementAccounts(Set<Integer> accountIds, WorkflowStatus statusLow,
            WorkflowStatus statusHigh) {
        String jpql = "select distinct a "
                + "from Account a "
                + "join SpecificFunctionAgreement s "
                + "where a._id = s._accountId "
                + "    and s._statusId between :statusLow and :statusHigh and s._accountId in :accountIds";
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
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
    private List<CenterName> _centerNames;

    public List<CenterName> getCenterNames(boolean includeNoneOption) {
        if (_centerNames == null) {
            _centerNames = findAll(CenterName.class)
                    .stream()
                    .sorted((n1, n2) -> obtainSortName(n1).compareTo(obtainSortName(n2)))
                    .collect(Collectors.toList());
        }
        return _centerNames
                .stream()
                .filter(n -> includeNoneOption || n.getId() != 0)
                .collect(Collectors.toList());
    }

    private String obtainSortName(CenterName center){
        if (center.getId() == -1){
            return "üüü";  // sort to end
        }
        if (center.getId() == 0){
            return " ";  // sort to begin
        }
        return center.getName(); // sort by name
    }

    public List<RelatedName> getRelatedNames() {
        return findAll(RelatedName.class)
                .stream()
                .filter(n -> n.getId() > 0)
                .sorted((n1, n2) -> (n1.getId() == -1 ? "ZZZ" : n1.getName()).compareTo((n2.getId() == -1 ? "ZZZ" : n2.
                getName())))
                .collect(Collectors.toList());
    }

    public List<SpecificFunction> getSpecificFunctionsForInsurance() {
        return findAll(SpecificFunction.class)
                .stream()
                .filter(f -> f.getId() > 0)
                .sorted((f1, f2) -> f1.getId() - f2.getId())
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

    public Pair<Integer, Integer> findIkAndYearOfSpecificFunctionRequestByCode(String code) {
        String sql = "select rmIk, rmDataYear from spf.RequestMaster where rmStatusId = 10 and rmCode = ?";
        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter(1, code);
        try {
            Object[] result = (Object[]) query.getSingleResult();
            return new Pair<>((Integer) result[0], (Integer) result[1]);
        } catch (Exception ex) {
            return new Pair<>(-1, -1);
        }
    }


}
