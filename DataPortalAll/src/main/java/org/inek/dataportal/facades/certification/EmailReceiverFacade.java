package org.inek.dataportal.facades.certification;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.inek.dataportal.entities.certification.EmailReceiver;
import org.inek.dataportal.common.data.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class EmailReceiverFacade extends AbstractFacade<EmailReceiver> {

    public EmailReceiverFacade() {
        super(EmailReceiver.class);
    }

    public EmailReceiver save(EmailReceiver receiver) {
        if (receiver.getEmailReceiversId() == -1) {
            persist(receiver);
            return receiver;
        }
        return merge(receiver);
    }

    
    public List<EmailReceiver> findAllEmailReceiverByListId(int id) {
        String query = "SELECT i FROM EmailReceiver i WHERE i._receiverList = :id";
        return getEntityManager().createQuery(query, EmailReceiver.class).setParameter("id", id).getResultList();
    }

    public int getHighestEmailReceiverListId() {
        List<EmailReceiver> list = findAll();
        int max = 0;
        for (EmailReceiver er : list) {
            if (er.getReceiverList() > max) {
                max = er.getReceiverList();
            }
        }
        return max;
    }

    public boolean deleteAllEmailReceiverByListId(int id) {
        String query = "DELETE FROM EmailReceiver er WHERE er._receiverList = :id";
        int deleted = getEntityManager().createQuery(query, EmailReceiver.class).setParameter("id", id).executeUpdate();
        return deleted > 0;
    }

}
