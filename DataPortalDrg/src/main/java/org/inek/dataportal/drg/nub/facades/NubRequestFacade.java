/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.drg.nub.facades;

import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.structures.ProposalInfo;
import org.inek.dataportal.common.utils.DateUtils;
import org.inek.dataportal.drg.nub.entities.*;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 *
 * @author muellermi
 */
@Singleton
public class NubRequestFacade extends AbstractDataAccessWithActionLog {

    private static final String FIELD_ID = "_id";
    private static final String FIELD_STATUS = "_status";
    private static final String IK = "ik";
    private static final String STATUS_HIGH = "statusHigh";
    private static final String STATUS_LOW = "statusLow";
    private static final String YEAR = "year";

    public NubRequest find(int id) {
        return super.find(NubRequest.class, id);
    }

    public NubRequest findFresh(int id) {
        return super.findFresh(NubRequest.class, id);
    }

    public List<NubRequest> findAll(int accountId, DataSet dataSet, String filter) {
        return findAll(accountId, -1, -1, dataSet, filter);
    }

    public List<NubRequest> findAll(int accountId, int ik, int year, DataSet dataSet, String filter) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<NubRequest> cq = cb.createQuery(NubRequest.class);
        Root<NubRequest> request = cq.from(NubRequest.class);
        Predicate condition = null;
        Order order = null;
        if (null != dataSet) {
            switch (dataSet) {
                case All:
                    condition = cb.ge(request.get(FIELD_STATUS), WorkflowStatus.New.getId());
                    order = cb.asc(request.get(FIELD_ID));
                    break;
                case AllOpen:
                    condition = cb.lessThan(request.get(FIELD_STATUS), WorkflowStatus.Provided.getId());
                    order = cb.asc(request.get(FIELD_ID));
                    break;
                case ApprovalRequested:
                    condition = cb.or(cb.equal(request.get(FIELD_STATUS), WorkflowStatus.ApprovalRequested.getId()),
                            cb.equal(request.get(FIELD_STATUS), WorkflowStatus.CorrectionRequested.getId()));
                    order = cb.asc(request.get(FIELD_ID));
                    break;
                default:
                    // provided (sealed)
                    condition = cb.greaterThanOrEqualTo(request.get(FIELD_STATUS), WorkflowStatus.Provided.getId());
                    order = cb.desc(request.get(FIELD_ID));
                    break;
            }
        }
        condition = cb.and(condition, cb.equal(request.get("_accountId"), accountId));
        if (!filter.isEmpty()) {
            Predicate isFiltered = cb.or(cb.like(request.get("_name"), filter), cb.
                    like(request.get("_displayName"), filter));
            condition = cb.and(condition, isFiltered);
        }
        if (ik >= 0) {
            Predicate ikCondition = cb.equal(request.get("_ik"), ik);
            condition = cb.and(condition, ikCondition);
        }
        if (year > 0) {
            condition = cb.and(condition, cb.equal(request.get("_targetYear"), year));
        }
        cq.select(request).where(condition).orderBy(order);
        return getEntityManager().createQuery(cq).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<NubRequest> findForAccount(
            int accountId,
            int year,
            DataSet dataSet,
            Set<Integer> managedIks,
            String filter) {
        String sql = "SELECT nub.* \n"
                + " FROM NubProposal nub \n";
        sql += " WHERE nubAccountId = " + accountId + "\n";
        if (!managedIks.isEmpty()) {
            String denyedIks = managedIks
                    .stream()
                    .map(r -> "" + r)
                    .collect(Collectors.joining(", "));
            sql += " and nubIk not in " + denyedIks + " \n";
        }
        if (!filter.isEmpty()) {
            sql += "and (nubName like '%" + filter + "%' or nubDisplayName like '%" + filter + "%')\r\n";
        }
        if (year > 0) {
            sql += "and nubTargetYear = " + year + "\r\n";
        }
        switch (dataSet) {
            case All:
                sql += "and nubStatus >= " + WorkflowStatus.New.getId() + "\r\n";
                sql += " order by nubId";
                break;
            case AllOpen:
                sql += " and nubStatus < " + WorkflowStatus.Provided.getId() + "\r\n";
                sql += " order by nubId";
                break;
            case ApprovalRequested:
                sql += " and (nubStatus = " + WorkflowStatus.ApprovalRequested.getId() + "\r\n";
                sql += " or nubStatus = " + WorkflowStatus.CorrectionRequested.getId() + ")\r\n";
                sql += "order by nubId";
                break;
            default:
                // provided (sealed)
                sql += " and nubStatus >= " + WorkflowStatus.Provided.getId() + "\r\n";
                sql += " order by nubId desc";
                break;
        }

        Query query = getEntityManager().createNativeQuery(sql, NubRequest.class);
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

    public List<ProposalInfo> getNubRequestInfos(int accountId, int ik, int year, DataSet dataSet, String filter) {
        List<NubRequest> requests = findAll(accountId, ik, year, dataSet, filter);
        List<ProposalInfo> proposalInfos = new ArrayList<>();
        for (NubRequest request : requests) {
            String displayName = request.getDisplayName().trim().length() == 0
                    ? request.getName()
                    : request.getDisplayName();
            proposalInfos.add(new ProposalInfo(request.getId(), displayName, request.getTargetYear(), request.
                    getStatus(), request.getIk()));
        }
        return proposalInfos;
    }

    public List<ProposalInfo> getNubRequestInfos(int ik, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh,
            String filter) {
        String jql = "SELECT p FROM NubRequest p "
                + "WHERE p._ik = :ik and p._status >= :statusLow and p._status <= :statusHigh "
                + (filter.isEmpty() ? "" : "and (p._displayName like :filter1 or p._name like :filter2) ")
                + (year > 0 ? " and p._targetYear = :year " : "")
                + "ORDER BY p._id DESC";
        TypedQuery<NubRequest> query = getEntityManager().createQuery(jql, NubRequest.class);
        query.setParameter(IK, ik);
        query.setParameter(STATUS_LOW, statusLow.getId());
        query.setParameter(STATUS_HIGH, statusHigh.getId());
        if (!filter.isEmpty()) {
            query.setParameter("filter1", filter);
            query.setParameter("filter2", filter);
        }
        if (year > 0) {
            query.setParameter(YEAR, year);
        }
        List<NubRequest> requests = query.getResultList();
        List<ProposalInfo> proposalInfos = new ArrayList<>();
        for (NubRequest request : requests) {
            String displayName = request.getDisplayName().trim().length() == 0
                    ? request.getName()
                    : request.getDisplayName();
            proposalInfos.add(new ProposalInfo(request.getId(), displayName, request.getTargetYear(), request.
                    getStatus(), request.getIk()));
        }
        return proposalInfos;
    }

    public List<Integer> getNubYears(Set<Integer> accountIds, Set<Integer> managedIks) {
        String jpql = "SELECT DISTINCT p._targetYear FROM NubRequest p "
                + "WHERE p._status >= 10 and ("
                + (accountIds.isEmpty() ? "1=2" : "p._accountId in :accountIds")
                + " or "
                + (managedIks.isEmpty() ? "1=2" : "p._ik in :managedIks")
                + ")\n"
                + "ORDER BY p._targetYear DESC";
        TypedQuery<Integer> query = getEntityManager().createQuery(jpql, Integer.class);
        if (!accountIds.isEmpty()) {
            query.setParameter("accountIds", accountIds);
        }
        if (!managedIks.isEmpty()) {
            query.setParameter("managedIks", managedIks);
        }
        return query.getResultList();
    }

    public Set<Integer> checkAccountsForNubOfYear(
            Set<Integer> accountIds,
            int year,
            WorkflowStatus statusLow,
            WorkflowStatus statusHigh) {
        return new HashSet<>();
    }

    public List<Account> checkAccountsForNubOfYear(
            Set<Integer> accountIds,
            int year,
            WorkflowStatus statusLow,
            WorkflowStatus statusHigh,
            Set<Integer> managedIks) {
        String jpql = "SELECT DISTINCT a "
                + "from Account a "
                + "join NubRequest p "
                + "WHERE a._id = p._accountId "
                + " and p._accountId in :accountIds "
                + (year > 0 ? " and p._targetYear = :year " : "")
                + " and p._status between :statusLow and :statusHigh "
                + (managedIks.isEmpty() ? "" : " and p._ik not in :iks");
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        query.setParameter("accountIds", accountIds);
        if (year > 0) {
            query.setParameter(YEAR, year);
        }
        query.setParameter(STATUS_LOW, statusLow.getId());
        query.setParameter(STATUS_HIGH, statusHigh.getId());
        if (!managedIks.isEmpty()) {
            query.setParameter("iks", managedIks);
        }
        return query.getResultList();
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

    public String getOldNubIdName(String id) {
        String jpql = "SELECT p FROM NubFormerRequest p WHERE p._externalId = :exId";
        TypedQuery<NubFormerRequest> query = getEntityManager().createQuery(jpql, NubFormerRequest.class);
        query.setParameter("exId", id);
        return query.getResultList().get(0).getName();
    }

    private TypedQuery<NubFormerRequest> getQueryForOldNubIds(int ik, String filter) {
        // gets information about nub requests of the pre-portal area
        String jpql = "SELECT p FROM NubFormerRequest p WHERE p._ik = :ik AND p._name LIKE :filter";
        TypedQuery<NubFormerRequest> query = getEntityManager().createQuery(jpql, NubFormerRequest.class);
        query.setParameter(IK, ik);
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
        query.setParameter(IK, ik);
        query.setParameter("iks", "%" + ik + "%");
        query.setParameter("filter", "%" + filter + "%");
        return query;
    }

    public List<NubFormerRequestMerged> getExistingNubIds(int currentIk, String filter, boolean maxYearOnly) {
        if (currentIk < 100000000) {
            return Collections.emptyList();
        }

        List<NubFormerRequestMerged> list = new ArrayList<>();
        List<Integer> iks = determineIks(currentIk);

        for (Integer ik : iks) {
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
        }

        if (!maxYearOnly) {
            // get information about pre-portal data
            for (Integer ik : iks) {

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
        }
        return list;
    }

    /**
     *
     * @param ik
     *
     * @return a list of iks, containing the current one as well as previous iks
     */
    @SuppressWarnings("unchecked")
    private List<Integer> determineIks(int ik) {
        List<Integer> iks = new ArrayList<>();
        iks.add(ik);
        String sql = "select p.cuIk\n"
                + "from CallCenterDB.dbo.ccCustomer c\n"
                + "join CallCenterDB.dbo.CustomerHistory on c.cuId = chCustomerId\n"
                + "join CallCenterDB.dbo.ccCustomer p on chPreviousCustomerId = p.cuId\n"
                + "where c.cuIK = " + ik;
        Query query = getEntityManager().createNativeQuery(sql);
        List formerIks = query.getResultList();
        for (Object formerIk : formerIks) {
            iks.add((int) formerIk);
        }
        return iks;
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
