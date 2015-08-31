/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.mail;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import org.inek.dataportal.entities.PasswordRequest;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountChangeMail;
import org.inek.dataportal.entities.account.AccountFeatureRequest;
import org.inek.dataportal.entities.account.AccountRequest;
import org.inek.dataportal.entities.account.Person;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.PropertyKey;
import org.inek.dataportal.utils.PropertyManager;

/**
 *
 * @author muellermi
 */
@Singleton
@Startup
public class Mailer {

    protected static final Logger _logger = Logger.getLogger("Mailer");

    @Inject
    MailTemplateFacade _mailTemplateFacade;

    public boolean sendMail(String recipient, String subject, String body) {
        return sendMail(recipient, "", subject, body);
    }

    public boolean sendMail(String recipient, String bcc, String subject, String body) {
        return sendMailFrom("anfragen@datenstelle.de", recipient, bcc, subject, body);
    }

    public boolean sendMailFrom(String from, String recipient, String bcc, String subject, String body) {
        return sendMailFrom(from, recipient, "", bcc, subject, body);
    }

    /**
     *
     * @param from
     * @param recipient one or more addresses, separated by semicolon
     * @param cc none, one or more addresses, separated by semicolon
     * @param bcc none, one or more addresses, separated by semicolon
     * @param subject
     * @param body
     * @param files
     * @return
     */
    public boolean sendMailFrom(String from, String recipient, String cc, String bcc, String subject, String body, String... files) {
        if (recipient.toLowerCase().endsWith(".test")) {
            // this is just to mock a successful mail
            return true;
        }
        try {
            if (recipient.isEmpty()) {
                throw new MessagingException("missing rereipient");
            }
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", "vMailSvr01.d1inek.local");
            properties.put("mail.smtp.connectiontimeout", 1000);
            properties.put("mail.smtp.ssl.trust", "*");
            Session session = Session.getDefaultInstance(properties);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSender(new InternetAddress("InEK-Datenportal <datenportal@inek.org>"));
            addReceipients(message, recipient, Message.RecipientType.TO);
            if (!cc.equals(recipient)) {
                addReceipients(message, cc, Message.RecipientType.CC);
            }
            addReceipients(message, bcc, Message.RecipientType.BCC);
            message.setSubject(subject);
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            for (String file : files) {
                addAttachment(multipart, file);
            }
            message.setContent(multipart);
            Transport.send(message);
            return true;
        } catch (Exception ex) { // catch all, not only MessagingException
            _logger.log(Level.SEVERE, "Mailer failed");
            _logger.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private static void addAttachment(Multipart multipart, String filename) throws MessagingException {
        if (filename.isEmpty()) {
            return;
        }
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        File file = new File(filename);
        messageBodyPart.setFileName(file.getName());
        multipart.addBodyPart(messageBodyPart);
    }

    private void addReceipients(MimeMessage message, String recipients, Message.RecipientType rType) throws MessagingException {
        if (recipients == null) {
            return;
        }
        for (String recipient : recipients.split(";")) {
            if (!recipient.trim().isEmpty()) {
                message.addRecipient(rType, new InternetAddress(recipient.trim()));
            }
        }
    }

    public MailTemplate getMailTemplate(String name) {
        MailTemplate template = _mailTemplateFacade.findByName(name);
        if (template == null) {
            String serverName = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
            String msg = "Server: " + serverName + "\r\n Mail template not found: " + name + "\r\n";
            sendMail(PropertyManager.INSTANCE.getProperty(PropertyKey.ExceptionEmail), "MailTemplate not found", msg);
        }
        return template;
    }

    public String getFormalSalutation(Person person) {
        String salutation = person.getGender() == 1 ? Utils.getMessage("formalSalutationFemale") : Utils.getMessage("formalSalutationMale");
        salutation = salutation.replace("{title}", person.getTitle()).replace("{lastname}", person.getLastName()).replace("  ", " ");
        return salutation;
    }

    public boolean sendActivationMail(AccountRequest accountRequest) {
        MailTemplate template = getMailTemplate("AccountActivationMail");
        if (template == null) {
            return false;
        }
        String salutation = getFormalSalutation(accountRequest);
        String codedUser = Utils.encodeUrl(accountRequest.getUser());
        String link = PropertyManager.INSTANCE.getProperty(PropertyKey.ApplicationURL) + "/login/Activate.xhtml?key=" + accountRequest.getActivationKey() + "&user=" + codedUser;
        String body = template.getBody()
                .replace("{formalSalutation}", salutation)
                .replace("{link}", link)
                .replace("{username}", accountRequest.getUser())
                .replace("{activationkey}", accountRequest.getActivationKey());
        return sendMail(accountRequest.getEmail(), template.getBcc(), template.getSubject(), body);
    }

    public boolean sendMailActivationMail(AccountChangeMail changeMail) {
        MailTemplate template = getMailTemplate("MailActivationMail");
        if (template == null) {
            return false;
        }
        String link = PropertyManager.INSTANCE.getProperty(PropertyKey.ApplicationURL) + "/login/ActivateMail.xhtml?key=" + changeMail.getActivationKey() + "&mail=" + changeMail.getMail();
        String body = template.getBody()
                //.replace("{formalSalutation}", salutation)
                .replace("{link}", link)
                .replace("{email}", changeMail.getMail())
                .replace("{activationkey}", changeMail.getActivationKey());
        return sendMail(changeMail.getMail(), template.getBcc(), template.getSubject(), body);
    }

    public boolean sendPasswordActivationMail(PasswordRequest pwdRequest, Account account) {
        _logger.log(Level.INFO, "Password request for {0}", account.getId());
        MailTemplate template = getMailTemplate("PasswordActivationMail");
        if (template == null) {
            return false;
        }
        String salutation = getFormalSalutation(account);
        String link = PropertyManager.INSTANCE.getProperty(PropertyKey.ApplicationURL) + "/login/ActivatePassword.xhtml?key=" + pwdRequest.getActivationKey() + "&mail=" + account.getEmail();

        String body = template.getBody()
                .replace("{formalSalutation}", salutation)
                .replace("{link}", link)
                .replace("{email}", account.getEmail())
                .replace("{activationkey}", pwdRequest.getActivationKey());
        return sendMail(account.getEmail(), template.getBcc(), template.getSubject(), body);

    }

    public boolean sendFeatureRequestAnswer(String templateName, Account account, AccountFeatureRequest featureRequest) {
        MailTemplate template = getMailTemplate(templateName);
        if (template == null) {
            return false;
        }

        String salutation = getFormalSalutation(account);
        String subject = template.getSubject().replace("{feature}", featureRequest.getFeature().getDescription());
        String body = template.getBody()
                .replace("{formalSalutation}", salutation)
                .replace("{feature}", featureRequest.getFeature().getDescription());
        return sendMail(account.getEmail(), template.getBcc(), subject, body);
    }

    public void sendWarning(String head, Exception exception) {
        sendException(Level.WARNING, head, exception);
    }

    public void sendError(String head, Exception exception) {
        sendException(Level.WARNING, head, exception);
    }

    public void sendException(Level level, String head, Exception exception) {
        _logger.log(level, head, exception);

        StringBuilder msg = new StringBuilder();
        msg.append(head).append("\r\n\r\n");
        msg.append(exception.getMessage()).append("\r\n\r\n");
        for (StackTraceElement element : exception.getStackTrace()) {
            msg.append(element.toString()).append("\r\n");
        }

        String name = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
        String subject = "Exception reported by Server " + name;
        sendMail(PropertyManager.INSTANCE.getProperty(PropertyKey.ExceptionEmail), subject, msg.toString());
    }

}
