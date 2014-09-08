package org.inek.dataportal.facades.certification;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.certification.EmailLog;
import org.inek.dataportal.facades.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class EmailLogFacade extends AbstractFacade<EmailLog> {

    public EmailLogFacade() {
        super(EmailLog.class);
    }
}
