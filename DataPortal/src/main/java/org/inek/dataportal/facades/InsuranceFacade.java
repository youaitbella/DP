/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.common.RemunerationType;
import org.inek.dataportal.entities.insurance.DosageForm;
import org.inek.dataportal.entities.insurance.InsuranceNubNotice;
import org.inek.dataportal.entities.insurance.Unit;
import org.inek.dataportal.entities.insurance.InekMethod;
import org.inek.dataportal.entities.insurance.NubMethodInfo;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.WorkflowStatus;

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
        String sql = "SELECT n FROM InsuranceNubNotice n WHERE n._accountId = :accountId and n._workflowStatusId = :state ORDER BY n._year, n._id";
        TypedQuery<InsuranceNubNotice> query = getEntityManager().createQuery(sql, InsuranceNubNotice.class);
        query.setParameter("accountId", accountId);
        WorkflowStatus wfs = WorkflowStatus.New;
        switch(dataSet) {
            case AllOpen:
                wfs = WorkflowStatus.New;
                break;
            case AllSealed:
                wfs = WorkflowStatus.Provided;
                break;
        }
        query.setParameter("state", wfs.getValue());
        return query.getResultList();

    }

    public InsuranceNubNotice saveNubNotice(InsuranceNubNotice notice) {
        if (notice.getId() == -1) {
            persist(notice);
            return notice;
        }
        return merge(notice);
    }

    public List<DosageForm> getDosageForms() {
        return findAll(DosageForm.class);
    }

    public Optional<DosageForm> getDosageForm(String text) {
        String jql = "select d from DosageForm d where d._text = :text";
        TypedQuery<DosageForm> query = getEntityManager().createQuery(jql, DosageForm.class);
        query.setParameter("text", text);
        try {
            return Optional.of(query.getSingleResult());
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

    public List<NubMethodInfo> getNubMethodInfos(int ik, int year) {
        String sql = "select prDatenportalId, prNubName, prFkCaId, caName "
                + "from nub.dbo.NubRequest "
                + "join nub.dbo.category on prFkCaId = caId "
                + "left join nub.dbo.NubRequestProxyIk on prId=nppProposalId "
                + "where (prIk = ? or nppProxyIk = ?) and prYear = ? and prStatus = 20 "
                + "order by prDatenportalId";
        Query query = getEntityManager().createNativeQuery(sql, NubMethodInfo.class);
        query.setParameter(1, ik);
        query.setParameter(2, ik);
        query.setParameter(3, year);
        return query.getResultList();
    }

    public boolean existsNubRequest(int requestId, int hospitalIk, int year) {
        String sql = "select 1 "
                + "from nub.dbo.NubRequest "
                + "join nub.dbo.category on prFkCaId = caId "
                + "left join nub.dbo.NubRequestProxyIk on prId=nppProposalId "
                + "where prDatenportalId = ? and (prIk = ? or nppProxyIk = ?) and prYear = ? and prStatus = 20 "
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
                + "where ciSequence = ? and (prIk = ? or nppProxyIk = ?) and prYear = ? and prStatus = 20 "
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
}
