/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades.modelintention;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.modelintention.ModelIntention;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.AbstractFacade;
import org.inek.dataportal.helper.structures.EntityInfo;

/**
 *
 * @author vohldo
 */
@Stateless
public class ModelIntentionFacade extends AbstractFacade<ModelIntention> {

    @Inject SessionController _sessionController;

    public ModelIntentionFacade() {
        super(ModelIntention.class);
    }

    public List<ModelIntention> findAll(int accountId, DataSet dataSet, boolean forAllUsers) {
        if (forAllUsers) {
            if (!_sessionController.isInekUser(Feature.MODEL_INTENTION)) {
                return Collections.EMPTY_LIST;
            }
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ModelIntention> cq = cb.createQuery(ModelIntention.class);
        Root request = cq.from(ModelIntention.class);
        Predicate status;
        Order order;
        if (dataSet == DataSet.OpenOnly) {
            status = cb.lessThan(request.get("_status"), 10);
            order = cb.asc(request.get("_id"));
        } else if (dataSet == DataSet.SealedOnly) {
            status = cb.greaterThanOrEqualTo(request.get("_status"), 10);
            order = cb.desc(request.get("_id"));
        } else {
            status = cb.greaterThanOrEqualTo(request.get("_status"), 0);
            order = cb.desc(request.get("_id"));
        }
        if (forAllUsers) {
            cq.select(request).where(status).orderBy(order);
        } else {
            Predicate isAccount = cb.equal(request.get("_accountId"), accountId);
            cq.select(request).where(cb.and(isAccount, status)).orderBy(order);
        }
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<ModelIntention> findAll(int accountId) {
        String sql = "SELECT m FROM ModelIntention m WHERE m._accountId = :accountId ORDER BY m._id DESC";
        Query query = getEntityManager().createQuery(sql, ModelIntention.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public int count(int accountId, boolean isComplete) {
        String sql = "SELECT COUNT(m) FROM ModelIntention m WHERE m._accountId = :accountId"; // removed "AND isComplete"
        Query query = getEntityManager().createQuery(sql, ModelIntention.class);
        query.setParameter("accountId", accountId);
        // removed "AND isComplete"
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
     * @param forAllUsers
     * @return
     */
    public List<EntityInfo> getModelIntentionInfos(int accountId, DataSet dataSet, boolean forAllUsers) {
        List<ModelIntention> intentions = findAll(accountId, dataSet, forAllUsers);
        List<EntityInfo> intentionInfos = new ArrayList<>();
        for (ModelIntention intention : intentions) {
            String code = intention.getCode().isEmpty() ? "- nn - " :  intention.getCode();
            intentionInfos.add(new EntityInfo(intention.getId(), code, intention.getDescription(), intention.getStatus()));
        }
        return intentionInfos;
    }

}
