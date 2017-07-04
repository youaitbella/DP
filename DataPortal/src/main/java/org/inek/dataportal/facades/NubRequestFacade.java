/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.insurance.InekMethod;
import org.inek.dataportal.entities.nub.NubFormerRequest;
import org.inek.dataportal.entities.nub.NubFormerRequestMerged;
import org.inek.dataportal.entities.nub.NubMethodInfo;
import org.inek.dataportal.entities.nub.NubRequest;
import org.inek.dataportal.entities.nub.NubRequestHistory;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.feature.admin.backingbean.AccountInfo;
import org.inek.dataportal.helper.structures.ProposalInfo;
import org.inek.dataportal.utils.DateUtils;

/**
 *
 * @author muellermi
 */
@Singleton
public class NubRequestFacade extends AbstractDataAccess {

    public List<NubRequest> findAll(int accountId, DataSet dataSet, String filter) {
        return findAll(accountId, -1, -1, dataSet, filter);
    }

    public NubRequest find(int id) {
        return super.find(NubRequest.class, id);
    }

    public NubRequest findFresh(int id) {
        return super.findFresh(NubRequest.class, id);
    }

    public List<NubRequest> findAll(int accountId, int ik, int year, DataSet dataSet, String filter) {
        return findAll(accountId, ik, false, year, dataSet, filter);
    }

    public List<NubRequest> findAll(int accountId, int ik, boolean includeProxyIks, int year, DataSet dataSet, String filter) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<NubRequest> cq = cb.createQuery(NubRequest.class);
        Root<NubRequest> request = cq.from(NubRequest.class);
        Predicate condition = null;
        Order order = null;
        if (null != dataSet) {
            switch (dataSet) {
                case All:
                    condition = cb.ge(request.get("_status"), WorkflowStatus.New.getId());
                    order = cb.asc(request.get("_id"));
                    break;
                case AllOpen:
                    condition = cb.lessThan(request.get("_status"), WorkflowStatus.Provided.getId());
                    order = cb.asc(request.get("_id"));
                    break;
                case ApprovalRequested:
                    condition = cb.or(cb.equal(request.get("_status"), WorkflowStatus.ApprovalRequested.getId()),
                            cb.equal(request.get("_status"), WorkflowStatus.CorrectionRequested.getId()));
                    order = cb.asc(request.get("_id"));
                    break;
                default:
                    // provided (sealed)
                    condition = cb.greaterThanOrEqualTo(request.get("_status"), WorkflowStatus.Provided.getId());
                    order = cb.desc(request.get("_id"));
                    break;
            }
        }
        condition = cb.and(condition, cb.equal(request.get("_accountId"), accountId));
        if (!filter.isEmpty()) {
            Predicate isFiltered = cb.or(cb.like(request.get("_name"), filter), cb.like(request.get("_displayName"), filter));
            condition = cb.and(condition, isFiltered);
        }
        if (ik > 0) {
            Predicate ikCondition = cb.equal(request.get("_ik"), ik);
            if (includeProxyIks) {
                ikCondition = cb.or(ikCondition, cb.like(request.get("_proxyIKs"), "" + ik));
            }
            condition = cb.and(condition, ikCondition);
        }
        if (year > 0) {
            condition = cb.and(condition, cb.equal(request.get("_targetYear"), year));
        }
        cq.select(request).where(condition).orderBy(order);
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<NubRequest> findAll(int accountId) {
        String sql = "SELECT p FROM NubRequest p WHERE p._accountId = :accountId ORDER BY p._id DESC";
        TypedQuery<NubRequest> query = getEntityManager().createQuery(sql, NubRequest.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public NubRequest saveNubRequest(NubRequest nubRequest) {
        if (nubRequest.getStatus() == WorkflowStatus.Unknown) {
            nubRequest.setStatus(WorkflowStatus.New);
        }

        if (nubRequest.getId() == -1) {
            persist(nubRequest);
            return nubRequest;
        }
        return merge(nubRequest);
    }

    /**
     * A list of NUB infos for display usage, e.g. lists
     *
     * @param accountId
     * @param dataSet
     * @param filter
     * @return
     */
    public List<ProposalInfo> getNubRequestInfos(int accountId, DataSet dataSet, String filter) {
        return getNubRequestInfos(accountId, -1, dataSet, filter);
    }

    public List<ProposalInfo> getNubRequestInfos(int accountId, int ik, DataSet dataSet, String filter) {
        return getNubRequestInfos(accountId, ik, -1, dataSet, filter);
    }

    public List<ProposalInfo> getNubRequestInfos(int accountId, int ik, int year, DataSet dataSet, String filter) {
        List<NubRequest> requests = findAll(accountId, ik, year, dataSet, filter);
        List<ProposalInfo> proposalInfos = new ArrayList<>();
        for (NubRequest request : requests) {
            String displayName = request.getDisplayName().trim().length() == 0
                    ? request.getName()
                    : request.getDisplayName();
            proposalInfos.add(new ProposalInfo(request.getId(), displayName, request.getTargetYear(), request.getStatus(), request.getIk()));
        }
        return proposalInfos;
    }

    public List<ProposalInfo> findForAccountAndIk(int accountId, int ik, int minStatus, int maxStatus, String filter) {
        String jql = "SELECT p FROM NubRequest p "
                + "WHERE p._accountId = :accountId and p._ik = :ik and p._status >= :minStatus and p._status <= :maxStatus "
                + (filter.isEmpty() ? "" : "and (p._displayName like :filter1 or p._name like :filter2) ")
                + "ORDER BY p._id DESC";
        TypedQuery<NubRequest> query = getEntityManager().createQuery(jql, NubRequest.class);
        query.setParameter("accountId", accountId);
        query.setParameter("ik", ik);
        query.setParameter("minStatus", minStatus);
        query.setParameter("maxStatus", maxStatus);
        if (!filter.isEmpty()) {
            query.setParameter("filter1", filter);
            query.setParameter("filter2", filter);
        }
        List<NubRequest> requests = query.getResultList();
        List<ProposalInfo> proposalInfos = new ArrayList<>();
        for (NubRequest request : requests) {
            String displayName = request.getDisplayName().trim().length() == 0
                    ? request.getName()
                    : request.getDisplayName();
            proposalInfos.add(new ProposalInfo(request.getId(), displayName, request.getTargetYear(), request.getStatus(), request.getIk()));
        }
        return proposalInfos;
    }

    public List<Integer> getNubYears(int accountId) {
        Set<Integer> accountIds = new HashSet<>();
        accountIds.add(accountId);
        return getNubYears(accountIds);
    }

    public List<Integer> getNubYears(Set<Integer> accountIds) {
        String jpql = "SELECT DISTINCT p._targetYear FROM NubRequest p "
                + "WHERE p._accountId in :accountIds and p._status >= 10 ORDER BY p._targetYear DESC";
        TypedQuery<Integer> query = getEntityManager().createQuery(jpql, Integer.class);
        query.setParameter("accountIds", accountIds);
        return query.getResultList();
    }

    public Set<Integer> checkAccountsForNubOfYear(Set<Integer> accountIds, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String jpql = "SELECT DISTINCT p._accountId FROM NubRequest p "
                + "WHERE p._accountId in :accountIds and (p._targetYear = :year or -1 = :year) and p._status between :statusLow and :statusHigh";
        TypedQuery<Integer> query = getEntityManager().createQuery(jpql, Integer.class);
        query.setParameter("accountIds", accountIds);
        query.setParameter("year", year);
        query.setParameter("statusLow", statusLow.getId());
        query.setParameter("statusHigh", statusHigh.getId());
        return new HashSet<>(query.getResultList());
    }

    public List<Integer> findAccountIdForIk(int ik) {
        String jpql = "SELECT DISTINCT p._accountId FROM NubRequest p WHERE p._ik = :ik  ";
        TypedQuery<Integer> query = getEntityManager().createQuery(jpql, Integer.class);
        query.setParameter("ik", ik);
        return query.getResultList();
    }

    public List<AccountInfo> getAccountInfos(int ik) {
        String jpql = "SELECT a, false, count(n) FROM NubRequest n JOIN Account a WHERE n._accountId = a._id and n._ik = :ik GROUP BY a";
        // sadly this is not a list of the expected type, but of object[]
//        TypedQuery<AccountInfo> query = getEntityManager().createQuery(jpql, AccountInfo.class);
//        query.setParameter("ik", ik);
//        List<AccountInfo> infos = query.getResultList();
//        return infos;

        // although the compiler tells us something else, this is what we get
        List<AccountInfo> infos = new ArrayList<>();
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("ik", ik);
        @SuppressWarnings("unchecked") List<Object[]> objects = query.getResultList();
        for (Object[] obj : objects) {
            AccountInfo info = new AccountInfo((Account) obj[0], (boolean) obj[1], (int) (long) obj[2]);
            infos.add(info);
        }
        return infos;
    }

    public Map<Integer, Integer> countOpenPerIk() {
        return NubRequestFacade.this.countOpenPerIk(1 + Calendar.getInstance().get(Calendar.YEAR));
    }

    public Map<Integer, Integer> countOpenPerIk(int targetYear) {
        String jpql = "SELECT p._accountId, COUNT(p) "
                + "FROM NubRequest p JOIN Account a "
                + "WHERE p._accountId = a._id and a._customerTypeId = 5 "
                + "    and p._status < 10 and p._targetYear = :targetYear GROUP BY p._accountId";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("targetYear", targetYear);
        List data = query.getResultList();
        Map<Integer, Integer> result = new HashMap<>();
        for (Object x : data) {
            Object[] info = (Object[]) x;
            int accountId = (int) info[0];
            int count = (int) (long) info[1];
            result.put(accountId, count);
        }
        return result;
    }

    public List<NubRequest> find(List<Integer> requestIds) {
        if (requestIds.isEmpty()) {
            return Collections.emptyList();
        }
        String jpql = "SELECT p FROM NubRequest p WHERE p._id in :requestIds  ";
        TypedQuery<NubRequest> query = getEntityManager().createQuery(jpql, NubRequest.class);
        query.setParameter("requestIds", requestIds);
        return query.getResultList();
    }

    public List<InekMethod> getInekMethods() {
        return super.findAll(InekMethod.class);
    }

    @Schedule(hour = "0", info = "once a day")
    private void check4NubOrphantCorrections() {
        Date date = DateUtils.getDateWithDayOffset(-5);
        String jpql = "SELECT p FROM NubRequest p WHERE p._dateCorrectionRequested < :date and p._status = :status ";
        TypedQuery<NubRequest> query = getEntityManager().createQuery(jpql, NubRequest.class);
        query.setParameter("date", date);
        query.setParameter("status", WorkflowStatus.CorrectionRequested.getId());
        List<NubRequest> nubRequests = query.getResultList();
        for (NubRequest nubRequest : nubRequests) {
            resetNubRequest(nubRequest);
        }
    }

    private void resetNubRequest(NubRequest nubRequest) {
        String jpql = "SELECT p FROM NubRequestHistory p WHERE p._nubId = :nubId order by p._version desc ";
        TypedQuery<NubRequestHistory> query = getEntityManager().createQuery(jpql, NubRequestHistory.class);
        query.setParameter("nubId", nubRequest.getId());
        NubRequestHistory nubRequestHistory = query.getResultList().get(0);
        copy(nubRequestHistory, nubRequest);
        saveNubRequest(nubRequest);
    }

    private void copy(NubRequestHistory nubRequestHistory, NubRequest nubRequest) {
        nubRequest.setDisplayName(nubRequestHistory.getDisplayName());
        nubRequest.setIk(nubRequestHistory.getIk());
        nubRequest.setIkName(nubRequestHistory.getIkName());
        nubRequest.setGender(nubRequestHistory.getGender());
        nubRequest.setTitle(nubRequestHistory.getTitle());
        nubRequest.setFirstName(nubRequestHistory.getFirstName());
        nubRequest.setLastName(nubRequestHistory.getLastName());
        nubRequest.setDivision(nubRequestHistory.getDivision());
        nubRequest.setRoleId(nubRequestHistory.getRoleId());
        nubRequest.setStreet(nubRequestHistory.getStreet());
        nubRequest.setPostalCode(nubRequestHistory.getPostalCode());
        nubRequest.setTown(nubRequestHistory.getTown());
        nubRequest.setPhone(nubRequestHistory.getPhone());
        nubRequest.setFax(nubRequestHistory.getFax());
        nubRequest.setEmail(nubRequestHistory.getEmail());
        nubRequest.setProxyIKs(nubRequestHistory.getProxyIKs());
        nubRequest.setFormFillHelper(nubRequestHistory.getFormFillHelper());
        nubRequest.setUserComment(nubRequestHistory.getUserComment());
        nubRequest.setName(nubRequestHistory.getName());
        nubRequest.setAltName(nubRequestHistory.getAltName());
        nubRequest.setDescription(nubRequestHistory.getDescription());
        nubRequest.setProcedures(nubRequestHistory.getProcedures());
        nubRequest.setProcs(nubRequestHistory.getProcs());
        nubRequest.setHasNoProcs(nubRequestHistory.isHasNoProcs());
        nubRequest.setIndication(nubRequestHistory.getIndication());
        nubRequest.setReplacement(nubRequestHistory.getReplacement());
        nubRequest.setWhatsNew(nubRequestHistory.getWhatsNew());
        nubRequest.setLos(nubRequestHistory.getLos());
        nubRequest.setInGermanySince(nubRequestHistory.getInGermanySince());
        nubRequest.setMedApproved(nubRequestHistory.getMedApproved());
        nubRequest.setInHouseSince(nubRequestHistory.getInHouseSince());
        nubRequest.setHospitalCount(nubRequestHistory.getHospitalCount());
        nubRequest.setPatientsLastYear(nubRequestHistory.getPatientsLastYear());
        nubRequest.setPatientsThisYear(nubRequestHistory.getPatientsThisYear());
        nubRequest.setPatientsFuture(nubRequestHistory.getPatientsFuture());
        nubRequest.setAddCosts(nubRequestHistory.getAddCosts());
        nubRequest.setDrgs(nubRequestHistory.getDrgs());
        nubRequest.setWhyNotRepresented(nubRequestHistory.getWhyNotRepresented());
        nubRequest.setLastChangedBy(nubRequestHistory.getLastChangedBy());
        nubRequest.setLastModified(nubRequestHistory.getLastModified());
        nubRequest.setStatus(WorkflowStatus.Taken);
    }

    public boolean checkFormerNubId(String formerNubId, int ik) {
        if (formerNubId.startsWith("N")) {
            return checkNewNubId(formerNubId, ik);
        }
        return checkOldNubId(formerNubId, ik);
    }

    private boolean checkOldNubId(String formerNubId, int ik) {
        TypedQuery<NubFormerRequest> query = getQueryForOldNubIds(formerNubId, ik);
        int results = query.getResultList().size();
        if (results == 0) {
            return false;
        }
        return true;
    }

    private boolean checkNewNubId(String formerNubId, int ik) {
        try {
            TypedQuery<NubRequest> query = getQueryForNewNubIds(formerNubId, ik);
            return query.getResultList().size() > 0;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getOldNubIdName(String id) {
        String jpql = "SELECT p FROM NubFormerRequest p WHERE p._externalId = :exId";
        TypedQuery<NubFormerRequest> query = getEntityManager().createQuery(jpql, NubFormerRequest.class);
        query.setParameter("exId", id);
        return query.getResultList().get(0).getName();
    }

    private TypedQuery<NubFormerRequest> getQueryForOldNubIds(String id, int ik) {
        String jpql = "SELECT p FROM NubFormerRequest p WHERE p._externalId = :exId AND p._ik = :ik";
        TypedQuery<NubFormerRequest> query = getEntityManager().createQuery(jpql, NubFormerRequest.class);
        query.setParameter("exId", id);
        query.setParameter("ik", ik);
        return query;
    }

    private TypedQuery<NubRequest> getQueryForNewNubIds(String id, int ik) {
        int targetYear = Calendar.getInstance().get(Calendar.YEAR);
        String jpql = "SELECT p FROM NubRequest p "
                + "WHERE p._id = :exId AND (p._ik = :ik or p._proxyIKs like :iks) "
                + "    AND p._status >= 20 AND p._status < 200 AND p._targetYear <= " + targetYear;
        TypedQuery<NubRequest> query = getEntityManager().createQuery(jpql, NubRequest.class);
        int nubId = 0;
        try {
            nubId = Integer.parseInt(id.replaceFirst("N", ""));
        } catch (Exception ex) {
            throw ex;
        }
        query.setParameter("exId", nubId);
        query.setParameter("ik", ik);
        query.setParameter("iks", "%" + ik + "%");
        return query;
    }

    private TypedQuery<NubFormerRequest> getQueryForOldNubIds(int ik, String filter) {
        String jpql = "SELECT p FROM NubFormerRequest p WHERE p._ik = :ik AND p._name LIKE :filter";
        TypedQuery<NubFormerRequest> query = getEntityManager().createQuery(jpql, NubFormerRequest.class);
        query.setParameter("ik", ik);
        query.setParameter("filter", "%" + filter + "%");
        return query;
    }

    private TypedQuery<NubRequest> getQueryForNewNubIds(int ik, String filter, boolean maxYearOnly) {
        int maxYear = Calendar.getInstance().get(Calendar.YEAR);
        String jpql = "SELECT p FROM NubRequest p "
                + "WHERE (p._ik = :ik or p._proxyIKs like :iks) "
                + "AND p._status >= 20 AND p._status < 200 "
                + "AND p._targetYear" + (maxYearOnly ? " = " : " <= ") + maxYear + " AND p._name LIKE :filter";
        TypedQuery<NubRequest> query = getEntityManager().createQuery(jpql, NubRequest.class);
        query.setParameter("ik", ik);
        query.setParameter("iks", "%" + ik + "%");
        query.setParameter("filter", "%" + filter + "%");
        return query;
    }

    public List<NubFormerRequestMerged> getExistingNubIds(int ik, String filter, boolean maxYearOnly) {
        if (ik < 100000000) {
            return Collections.emptyList();
        }

        List<NubFormerRequestMerged> list = new ArrayList<>();

        TypedQuery<NubRequest> newIdsQuery = getQueryForNewNubIds(ik, filter, maxYearOnly);
        newIdsQuery.getResultList().stream()
                .sorted((n1, n2) -> Integer.compare(n2.getId(), n1.getId()))
                .map((nr) -> {
                    NubFormerRequestMerged m = new NubFormerRequestMerged();
                    m.setId(nr.getExternalId());
                    m.setIk(nr.getIk());
                    m.setName(nr.getName());
                    return m;
                }).forEachOrdered((m) -> {
                    list.add(m);
                });

        if (!maxYearOnly) {
            TypedQuery<NubFormerRequest> oldIdsQuery = getQueryForOldNubIds(ik, filter);
            oldIdsQuery.getResultList().stream()
                    .sorted((n1, n2) -> ((n2.getExternalId().startsWith("1") ? "" : "0") + n2.getExternalId())
                    .compareTo((n1.getExternalId().startsWith("1") ? "" : "0") + n1.getExternalId()))
                    .map((nfr) -> {
                        NubFormerRequestMerged m = new NubFormerRequestMerged();
                        m.setId(nfr.getExternalId());
                        m.setIk(nfr.getIk());
                        m.setName(nfr.getName());
                        return m;
                    }).forEachOrdered((m) -> {
                        list.add(m);
                    });
        }
        return list;
    }

    // <editor-fold defaultstate="collapsed" desc="NubMethodInfo + Description">    
    private List<NubMethodInfo> _nubMethodInfos = Collections.emptyList();
    private final Map<Integer, String> _methodDescriptions = new ConcurrentHashMap<>();
    private ReentrantLock _lock = new ReentrantLock();

    public List<NubMethodInfo> obtainNubMethodInfos() {
        if (_nubMethodInfos.isEmpty()) {
            ensureNubMethodInfos();
        }
        return _nubMethodInfos;
    }

    private void ensureNubMethodInfos() {
        _lock.lock();
        try {
            if (_nubMethodInfos.isEmpty()) {
                loadNubMethodInfos();
            }
        } finally {
            _lock.unlock();
        }
    }

    private void loadNubMethodInfos() {
        String jpql = "select i from NubMethodInfo i where i._type = 'N' order by i._rowNum";
        TypedQuery<NubMethodInfo> query = getEntityManager().createQuery(jpql, NubMethodInfo.class);
        _nubMethodInfos = query.getResultList();
    }

    public String obtainNubMethodDescription(int methodId) {
        if (!_methodDescriptions.containsKey(methodId)) {
            loadNubMethodDescription(methodId);
        }
        return _methodDescriptions.get(methodId);
    }

    private void loadNubMethodDescription(int methodId) {
        String jpql = "select i from NubMethodInfo i where i._methodId = :methodId and i._type = 'D'";
        TypedQuery<NubMethodInfo> query = getEntityManager().createQuery(jpql, NubMethodInfo.class);
        query.setParameter("methodId", methodId);
        String description = query
                .getResultList()
                .stream()
                .map(i -> i.getText())
                .collect(Collectors.joining("\r\n\r\n---------------------------------\r\n\r\n"));
        _methodDescriptions.put(methodId, description);
    }

    public void clearNubMethodInfoCache() {
        _lock.lock();
        try {
            _nubMethodInfos.clear();
        } finally {
            _lock.unlock();
        }
        _methodDescriptions.clear();
    }
    // </editor-fold>    

    public void delete(NubRequest nubRequest) {
        remove(nubRequest);
    }

}
