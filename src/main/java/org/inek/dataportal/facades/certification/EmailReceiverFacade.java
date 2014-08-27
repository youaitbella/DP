package org.inek.dataportal.facades.certification;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import org.inek.dataportal.entities.certification.EmailReceiver;
import org.inek.dataportal.facades.AbstractFacade;

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
        List<EmailReceiver> temp = findAll();
        List<EmailReceiver> ret = new ArrayList<>();
        temp.stream().filter((er) -> (er.getAccountId() == id)).forEach((er) -> {
            ret.add(er);
        });
        if(ret.size() > 0) {
            return ret;
        }
        return new ArrayList<>();
    }
    
    public int getHighestEmailReceiverListId() {
        List<EmailReceiver> list = findAll();
        int max = 0;
        for(EmailReceiver er : list) {
            if(er.getReceiverList() > max) {
                max = er.getReceiverList();
            }
        }
        return max;
    }
}
