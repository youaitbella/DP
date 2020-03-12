package org.inek.dataportal.base.feature.approval;

import org.inek.dataportal.base.feature.approval.entities.ConfState;
import org.inek.dataportal.base.feature.approval.entities.ItemBlock;
import org.inek.dataportal.base.feature.approval.entities.ItemRecipient;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.utils.DateUtils;

import javax.enterprise.context.Dependent;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

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
        String jpql = "Select count(r.accountId) from ItemRecipient r " +
                "join Item i on r.item.id = i.id " +
                "join ItemBlock b on i.id = b.item.id " +
                "where r.accountId = :accountId and (r.firstViewedDt = :minDate or b.confState.id = 'u')";
        TypedQuery<Long> query = getEntityManager().createQuery(jpql, Long.class);
        query.setParameter("accountId", accountId);
        query.setParameter("minDate", DateUtils.MIN_DATE);
        return query.getSingleResult() > 0;
    }

    public List<ItemRecipient> itemsForAccount(int accountId) {
        String jpql = "Select r from ItemRecipient r where r.accountId = :accountId";
        TypedQuery<ItemRecipient> query = getEntityManager().createQuery(jpql, ItemRecipient.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public ItemBlock save(ItemBlock block) {
        return merge(block);
    }

    public ItemRecipient save(ItemRecipient recipient) {
        return merge(recipient);
    }

    public ConfState findStatebyId(String id) {
        String jpql = "select s from ConfState s where s.id = :id";
        TypedQuery<ConfState> query = getEntityManager().createQuery(jpql, ConfState.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }
}
