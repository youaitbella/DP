package org.inek.dataportal.facades.certification;

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
}
