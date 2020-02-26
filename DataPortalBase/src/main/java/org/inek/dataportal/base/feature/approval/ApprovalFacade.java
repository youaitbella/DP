package org.inek.dataportal.base.feature.approval;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.utils.DateUtils;

import javax.enterprise.context.Dependent;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Dependent
@Transactional
public class ApprovalFacade extends AbstractDataAccess {
    public boolean hasData(int accountId) {
        String jpql = "Select count(r.accountId) from ItemRecipient r where r.accountId = :accountId";
        TypedQuery<Long> query = getEntityManager().createQuery(jpql, Long.class);
        query.setParameter("accountId", accountId);
        return query.getSingleResult() > 0;
    }

    public boolean hasUnreadData(int accountId) {
        String jpql = "Select count(r.accountId) from ItemRecipient r where r.accountId = :accountId and r.firstViewedDt = :minDate";
        TypedQuery<Long> query = getEntityManager().createQuery(jpql, Long.class);
        query.setParameter("accountId", accountId);
        query.setParameter("minDate", DateUtils.MIN_DATE);
        return query.getSingleResult() > 0;
    }

}
