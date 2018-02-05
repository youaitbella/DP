package org.inek.dataportal.services;

import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.inject.Inject;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.cooperation.PortalMessage;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.cooperation.PortalMessageFacade;
import org.inek.dataportal.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Singleton
public class MessageService {

    @Inject private Mailer _mailer;
    @Inject private PortalMessageFacade _messageFacade;
    protected static final Logger LOGGER = Logger.getLogger("MessageService");

    /**
     * Sends an internal message from sender to receiver If the receiver checked
     * "email copy", then an email copy is send too. Registerd for cooperation
     * without any key.
     *
     * @param sender
     * @param receiver
     * @param subject
     * @param message
     */
    public void sendMessage(Account sender, Account receiver, String subject, String message) {
        sendMessage(sender, receiver, subject, message, Feature.COOPERATION, 0);
    }

    /**
     * Sends an internal message from sender to receiver. If the receiver
     * checked "email copy", then an email copy is send too. It will be
     * registerd for a feature and a given key
     *
     * @param sender
     * @param receiver
     * @param subject
     * @param message
     * @param feature
     * @param keyId
     */
    public void sendMessage(Account sender, Account receiver, String subject, String message, Feature feature, int keyId) {
        createPortalMessage(sender, receiver, subject, message, feature, keyId);
        if (receiver.isMessageCopy()) {
            sendEmailCopy(sender, receiver, subject, message);
        }
    }

    private void sendEmailCopy(Account sender, Account receiver, String subject, String message) {
        String extMessage = "Ihr Kooperationspartner, " + sender.getDisplayName() + ", sendet Ihnen die folgende Nachricht:"
                + "\r\n\r\n"
                + "-----"
                + "\r\n\r\n"
                + message
                + "\r\n\r\n"
                + "-----"
                + "\r\n\r\n"
                + "Dies ist eine automatisch generierte E-Mail des InEK Datenportals. "
                + "\r\n"
                + "Wenn Sie auf diese E-Mail antworten, erhält Ihr Kooperationspartner Ihre Antwort."
                ;
        _mailer.sendMailFrom(sender.getEmail(), receiver.getEmail(), "", "", subject, extMessage);
    }

    private void createPortalMessage(Account sender, Account receiver, String subject, String message, Feature feature, int keyId) {
        PortalMessage portalMessage = new PortalMessage();
        portalMessage.setFromAccountId(sender.getId());
        portalMessage.setToAccountId(receiver.getId());
        portalMessage.setFeature(feature);
        portalMessage.setKeyId(keyId);
        portalMessage.setSubject(subject);
        portalMessage.setMessage(message);
        _messageFacade.save(portalMessage);
    }

}
