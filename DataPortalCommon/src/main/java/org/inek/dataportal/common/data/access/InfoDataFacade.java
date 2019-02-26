/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.access;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.common.ListFeature;
import org.inek.dataportal.common.data.common.ListWorkflowStatus;
import org.inek.dataportal.common.data.common.TrashMail;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * This is a facade to various informative data
 * @author muellermi
 */
@Stateless
public class InfoDataFacade extends AbstractDataAccess {

    public boolean isTrashMailDomain(String domain) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TrashMail> cq = cb.createQuery(TrashMail.class);
        Root<TrashMail> request = cq.from(TrashMail.class);
        cq.select(request).where(cb.equal(request.get("_domain"), domain));
        return getEntityManager().createQuery(cq).getResultList().size() > 0;
    }

    public List<ListFeature> findAllListFeature(){
        return findAllFresh(ListFeature.class);
    }

    public void saveListFeature(ListFeature listFeature) {
        merge(listFeature);
    }

    public List<ListWorkflowStatus> findAllListWorkflowStatus(){
        return findAllFresh(ListWorkflowStatus.class);
    }

    public void saveListWorkflowStatus(ListWorkflowStatus listWorkflowStatus) {
        merge(listWorkflowStatus);
    }
}
