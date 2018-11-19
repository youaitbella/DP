package org.inek.documentScanner.facade;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.account.entities.WaitingDocument;
import org.inek.dataportal.common.utils.DateUtils;

import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;


@Stateless
public class WaitingDocumentFacade extends AbstractDataAccess {


    @Asynchronous
    public void deleteOldDocuments() {
        String sql = "SELECT p FROM WaitingDocument p WHERE p._timestamp < :referenceDate";
        TypedQuery<WaitingDocument> query = getEntityManager().createQuery(sql, WaitingDocument.class);
        query.setParameter("referenceDate", DateUtils.getDateWithDayOffset(-60));
        List<WaitingDocument> docs = query.getResultList();
        for (WaitingDocument doc : docs) {
            LOGGER.log(Level.INFO, "Delete old waiting document {0} of agent {1}", new Object[]{doc.getName(),
                    doc.getAgentAccountId()});
            remove(doc);
        }
    }

    public void save(WaitingDocument waitingDocument) {
        persist(waitingDocument);
    }

}
