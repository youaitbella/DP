package org.inek.dataportal.facades;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.criteria.*;
import org.inek.dataportal.entities.Request;
import org.inek.dataportal.helper.structures.Pair;

/**
 *
 * @author muellermi
 */
@Stateless
public class RequestFacade extends AbstractFacade<Request> {

    public RequestFacade() {
        super(Request.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Request> findAll(int accountId, boolean isSealed) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Request> cq = cb.createQuery(Request.class);
        Root request = cq.from(Request.class);
        Predicate sealed;
        Order order;
        if (isSealed) {
            sealed = cb.isTrue(request.get("_isComplete"));
            order = cb.desc(request.get("_requestId"));
        } else {
            sealed = cb.isFalse(request.get("_isComplete"));
            order = cb.asc(request.get("_requestId"));
        }
        Predicate isAccount = cb.equal(request.get("_accountId"), accountId);
        cq.select(request).where(cb.and(isAccount, sealed)).orderBy(order);
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<Pair> getRequestInfos(int accountId, boolean isSealed) {
        List<Request> requests = findAll(accountId, isSealed);
        List<Pair> requestInfos = new ArrayList<>();
        for (Request request : requests) {
            requestInfos.add(new Pair(request.getRequestId(), request.getName()));
        }
        return requestInfos;
    }

    public Request saveRequest(Request request) {
        if (request.getRequestId() == null) {
            persist(request);
            return request;
        }
        return merge(request);
    }

}
