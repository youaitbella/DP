/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.modelintention.facades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.feature.modelintention.entities.ModelIntention;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.enums.UserSet;
import org.inek.dataportal.common.data.AbstractFacade;
import org.inek.dataportal.helper.structures.EntityInfo;

/**
 *
 * @author vohldo
 */
@Stateless
public class ModelIntentionFacade extends AbstractFacade<ModelIntention> {

    @Inject private SessionController _sessionController;

    public ModelIntentionFacade() {
        super(ModelIntention.class);
    }

    
    public List<ModelIntention> findAll(Set<Integer> accountIds, DataSet dataSet, UserSet userSet) {
        if (userSet == UserSet.AllUsers) {
            if (!_sessionController.isInekUser(Feature.MODEL_INTENTION)) {
                return Collections.emptyList();
            }
        } else if (accountIds.isEmpty()) {
            return Collections.emptyList();
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ModelIntention> cq = cb.createQuery(ModelIntention.class);
        Root<ModelIntention> request = cq.from(ModelIntention.class);
        Predicate status;
        Order order;
        if (dataSet == DataSet.AllOpen) {
            status = cb.lessThan(request.get("_status"), 10);
            order = cb.asc(request.get("_id"));
        } else if (dataSet == DataSet.AllSealed) {
            status = cb.greaterThanOrEqualTo(request.get("_status"), 10);
            order = cb.desc(request.get("_id"));
        } else {
            status = cb.greaterThanOrEqualTo(request.get("_status"), 0);
            order = cb.desc(request.get("_id"));
        }
        if (userSet == UserSet.AllUsers) {
            cq.select(request).where(status).orderBy(order);
        } else if (userSet == UserSet.OtherUsers) {
            Predicate isAccount = request.get("_accountId").in(accountIds);
            cq.select(request).where(cb.and(cb.not(isAccount), status)).orderBy(order);
        } else {
            Predicate isAccount = request.get("_accountId").in(accountIds);
            cq.select(request).where(cb.and(isAccount, status)).orderBy(order);
        }
        return getEntityManager().createQuery(cq).getResultList();
    }

    
    public List<ModelIntention> findAll(int accountId) {
        String sql = "SELECT m FROM ModelIntention m WHERE m._accountId = :accountId ORDER BY m._id DESC";
        TypedQuery<ModelIntention> query = getEntityManager().createQuery(sql, ModelIntention.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    
    public int count(int accountId) {
        String sql = "SELECT COUNT(m) FROM ModelIntention m WHERE m._accountId = :accountId";
        Query query = getEntityManager().createQuery(sql, ModelIntention.class);
        query.setParameter("accountId", accountId);
        return ((Long) query.getSingleResult()).intValue();
    }

    public ModelIntention saveModelIntention(ModelIntention modelIntention) {
        if (modelIntention.getId() == null) {
            persist(modelIntention);
            return modelIntention;
        }
        return merge(modelIntention);
    }

    /**
     * A list of ModelIntentions infos for display usage, e.g. lists
     *
     * @param accountId
     * @param dataSet
     * @param userSet
     * @return
     */
    public List<EntityInfo> getModelIntentionInfos(int accountId, DataSet dataSet, UserSet userSet) {
        Set<Integer> accountIds = new HashSet<>();
        accountIds.add(accountId);
        return getModelIntentionInfos(accountIds, dataSet, userSet);
    }

    public List<EntityInfo> getModelIntentionInfos(Set<Integer> accountIds, DataSet dataSet, UserSet userSet) {
        List<ModelIntention> intentions = findAll(accountIds, dataSet, userSet);
        List<EntityInfo> intentionInfos = new ArrayList<>();
        for (ModelIntention intention : intentions) {
            intentionInfos.add(new EntityInfo(
                    intention.getId(),
                    intention.getCode(),
                    intention.getDescription(),
                    intention.getStatus(),
                    intention.getAccountId()));
        }
        return intentionInfos;
    }

}
