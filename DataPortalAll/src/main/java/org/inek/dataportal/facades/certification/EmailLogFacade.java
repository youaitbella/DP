package org.inek.dataportal.facades.certification;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.certification.EmailLog;
import org.inek.dataportal.common.data.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class EmailLogFacade extends AbstractFacade<EmailLog> {

    public EmailLogFacade() {
        super(EmailLog.class);
    }

    
    public List<EmailLog> findEmailLogsBySystemIdAndGrouperIdAndType(int sysId, int grpId, int type) {
        String statement = "SELECT el FROM EmailLog el WHERE el._systemId = :sysId AND el._type = :type AND el._receiverAccountId = :grpId";
        TypedQuery<EmailLog> query = getEntityManager().createQuery(statement, EmailLog.class);
        try {
            return query.setParameter("sysId", sysId).setParameter("type", type).setParameter("grpId", grpId).getResultList();
        } catch (Exception ex) {

        }
        return null;
    }

    public void save(EmailLog el) {
        persist(el);
    }

}
