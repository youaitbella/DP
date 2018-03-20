/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.insurance.facade;

import org.inek.dataportal.common.data.AbstractDataAccess;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.inek.dataportal.common.data.common.RemunerationType;
import org.inek.dataportal.insurance.entities.DosageForm;
import org.inek.dataportal.insurance.entities.InsuranceNubNotice;
import org.inek.dataportal.insurance.entities.Unit;
import org.inek.dataportal.insurance.entities.InekMethod;
import org.inek.dataportal.insurance.entities.InsuranceNubNoticeItem;
import org.inek.dataportal.insurance.entities.InsuranceNubMethodInfo;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.common.enums.WorkflowStatus;

/**
 *
 * @author muellermi
 */
@Stateless
public class InsuranceFacade extends AbstractDataAccess {

    public InsuranceNubNotice findNubNotice(int id) {
        return super.find(InsuranceNubNotice.class, id);
    }

    public InsuranceNubNotice findFreshNubNotice(int id) {
        return super.findFresh(InsuranceNubNotice.class, id);
    }

    public List<InsuranceNubNotice> getAccountNotices(int accountId, DataSet dataSet) {
        String sql = "SELECT n FROM InsuranceNubNotice n "
                + "WHERE n._accountId = :accountId and n._workflowStatusId BETWEEN :minStatus AND :maxStatus ORDER BY n._year, n._id";
        TypedQuery<InsuranceNubNotice> query = getEntityManager().createQuery(sql, InsuranceNubNotice.class);
        int minStatus = dataSet == DataSet.AllOpen ? WorkflowStatus.New.getId() : WorkflowStatus.Provided.getId();
        int maxStatus = dataSet == DataSet.AllOpen ? WorkflowStatus.Provided.getId() - 1 : WorkflowStatus.Retired.
                getId();
        query.setParameter("accountId", accountId);
        query.setParameter("minStatus", minStatus);
        query.setParameter("maxStatus", maxStatus);
        return query.getResultList();

    }

    public InsuranceNubNotice saveNubNotice(InsuranceNubNotice notice) {
        if (notice.getId() == -1) {
            persist(notice);
        } else {
            for (InsuranceNubNoticeItem item : notice.getItems()) {
                item.setInsuranceNubNoticeId(notice.getId());
                saveNubNoticeItem(item);
            }
            notice = merge(notice);
        }
        return notice;
    }

    private void saveNubNoticeItem(InsuranceNubNoticeItem item) {
        if (item.getId() == -1) {
            persist(item);
        } else {
            merge(item);
        }
    }

    public List<DosageForm> getDosageForms() {
        return findAll(DosageForm.class);
    }

    public Optional<Integer> getDosageFormId(String text) {
        text = text.trim();
        if (text.endsWith(".")) {
            text = text.substring(0, text.length() - 1);
        }
        String sql = "select distinct dfId from dbo.listDosageForm "
                + "left join dbo.listDosageFormAbbreviation on dfId = dfaDosageFormId "
                + "where dfText = ? or dfaShort = ?";
        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter(1, text);
        query.setParameter(2, text);
        try {
            return Optional.of((int) query.getSingleResult());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public List<Unit> getUnits() {
        return findAll(Unit.class);
    }

    public Optional<Unit> getUnit(String text) {
        String jql = "select u from Unit u where u._text = :text";
        TypedQuery<Unit> query = getEntityManager().createQuery(jql, Unit.class);
        query.setParameter("text", text);
        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public List<InekMethod> getInekMethods() {
        return findAll(InekMethod.class);
    }

    public Optional<RemunerationType> getRemunerationType(String charId) {
        String jql = "select r from RemunerationType r where r._charId = :charId";
        TypedQuery<RemunerationType> query = getEntityManager().createQuery(jql, RemunerationType.class);
        query.setParameter("charId", charId);
        query.setMaxResults(1);
        List<RemunerationType> remunTypes = query.getResultList();
        if (remunTypes.size() == 1) {
            return Optional.of(remunTypes.get(0));
        }
        return Optional.empty();
    }

    public List<InsuranceNubMethodInfo> getNubMethodInfos(int ik, int year) {
        // with a prepared statement sql server sometimes needs quite a long time to fetch the result
        // thus, the parameters are now built into the string - no problem with SQL injection because both parameters are int
        String sql = "select prDatenportalId, prNubName, prFkCaId, caName, ciSequence "
                + "from nub.dbo.NubRequest "
                + "join nub.dbo.category on prFkCaId = caId "
                + "join nub.dbo.categoryInfoByYear on caId = ciFKCaId and prYear = ciBaseYear "
                + "left join nub.dbo.NubRequestProxyIk on prId=nppProposalId "
                + "where (prIk = " + ik + " or nppProxyIk = " + ik + ") and prYear = " + year + " and prStatus between 20 and 21 "
                + "order by prDatenportalId";
        Query query = getEntityManager().createNativeQuery(sql, InsuranceNubMethodInfo.class);
        @SuppressWarnings("unchecked") List<InsuranceNubMethodInfo> result = query.getResultList();
        return result;
    }

    public boolean existsNubRequest(int requestId, int hospitalIk, int year) {
        String sql = "select 1 "
                + "from nub.dbo.NubRequest "
                + "join nub.dbo.category on prFkCaId = caId "
                + "left join nub.dbo.NubRequestProxyIk on prId=nppProposalId "
                + "where prDatenportalId = ? and (prIk = ? or nppProxyIk = ?) and prYear = ? and prStatus between 20 and 21 "
                + "order by prDatenportalId";
        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter(1, requestId);
        query.setParameter(2, hospitalIk);
        query.setParameter(3, hospitalIk);
        query.setParameter(4, year);
        return query.getResultList().size() > 0;
    }

    public int retrieveRequestId(int sequence, int hospitalIk, int year) {
        String sql = "select prDatenportalId "
                + "from nub.dbo.NubRequest "
                + "join nub.dbo.category on prFkCaId = caId "
                + "join nub.dbo.categoryInfoByYear on prFkCaId = ciFkCaId and prYear = ciBaseYear "
                + "left join nub.dbo.NubRequestProxyIk on prId=nppProposalId "
                + "where ciSequence = ? and (prIk = ? or nppProxyIk = ?) and prYear = ? and prStatus between 20 and 21 "
                + "order by prDatenportalId";

        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter(1, sequence);
        query.setParameter(2, hospitalIk);
        query.setParameter(3, hospitalIk);
        query.setParameter(4, year);
        List resultList = query.getResultList();
        if (resultList.size() > 0) {
            return (int) resultList.get(0);
        }
        return -1;
    }

    public void delete(InsuranceNubNotice notice) {
        remove(notice);
    }

    public String checkSignature(String signature) {
        String select = "select 'IK: ' +  cast(spmIk as varchar)  + ' ('+ cuName + ', ' + "
                + "cuCity + ')' + ', Vereinbarungsjahr: ' + cast (spmYear as varchar) + ', Anlage ";
        String from = " from psy.StaffProofMaster join CallCenterDB.dbo.ccCustomer on spmIk = cuIk ";
        String sql = select + "1' " + from + " where spmSignatureAgreement = ?\n"
                + "union\n"
                + select + "2' " + from + " where spmSignatureEffective = ?";
        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter(1, signature);
        query.setParameter(2, signature);
        List resultList = query.getResultList();
        if (resultList.size() > 0) {
            return (String) resultList.get(0);
        }
        return "unbekannte oder ungültige Signatur";
        
    }

    
}
