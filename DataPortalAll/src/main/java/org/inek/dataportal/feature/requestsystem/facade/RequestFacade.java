package org.inek.dataportal.feature.requestsystem.facade;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.*;
import org.inek.dataportal.common.data.AbstractFacade;
import org.inek.dataportal.feature.requestsystem.dao.RequestInfo;
import org.inek.dataportal.feature.requestsystem.entity.Request;

/**
 *
 * @author muellermi
 */
@Stateless
public class RequestFacade extends AbstractFacade<Request> {

    public RequestFacade() {
        super(Request.class);
    }

    @SuppressWarnings("unchecked")
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

    public List<RequestInfo> getRequestInfos(int accountId, boolean isSealed) {
        List<Request> requests = findAll(accountId, isSealed);
        List<RequestInfo> requestInfos = new ArrayList<>();
        for (Request request : requests) {
            requestInfos.add(new RequestInfo(request.getRequestId(), request.getName()));
        }
        return requestInfos;
    }

    public Request saveRequest(Request request) {
        if (request.getRequestId() < 1) {
            persist(request);
            return request;
        }
        return merge(request);
    }

}
