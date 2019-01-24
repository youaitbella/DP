/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.common.mail;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.faces.context.ExternalContext;
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
import org.inek.dataportal.api.helper.Const;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.AccountChangeMail;
import org.inek.dataportal.common.data.account.entities.AccountFeatureRequest;
import org.inek.dataportal.common.data.account.entities.AccountRequest;
import org.inek.dataportal.common.data.account.iface.Person;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.account.entities.PasswordRequest;
import org.inek.dataportal.common.helper.Utils;
import static org.inek.dataportal.common.helper.Placeholder.*;

/**
 *
 * @author muellermi
 */
@Singleton
@Startup
public class Mailer {

    protected static final Logger LOGGER = Logger.getLogger("Mailer");

    @Inject
    private MailTemplateFacade _mailTemplateFacade;
    @Inject
    private ConfigFacade _config;

    public boolean sendMail(String recipient, String subject, String body) {
        return sendMail(recipient, "", subject, body);
    }

    public boolean sendMail(String recipient, String bcc, String subject, String body) {
        return sendMailFrom("DSAnfragen@inek-drg.de", recipient, bcc, subject, body);
    }

    public boolean sendMailFrom(String from, String recipient, String bcc, String subject, String body) {
        return sendMailFrom(from, recipient, "", bcc, subject, body);
    }

    /**
     *
     * @param from
     * @param recipient one or more addresses, separated by semicolon
     * @param cc        none, one or more addresses, separated by semicolon
     * @param bcc       none, one or more addresses, separated by semicolon
     * @param subject
     * @param body
     * @param files
     *
     * @return
     */
    public boolean sendMailFrom(String from, String recipient, String cc, String bcc, String subject, String body,
            String... files) {
        if (recipient.toLowerCase().endsWith(".test")) {
            // this is just to mock a successful mail
            return true;
        }
        try {
            if (recipient.isEmpty()) {
                throw new MessagingException("missing rereipient");
            }
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", "vMailSvr01");
            properties.put("mail.smtp.connectiontimeout", 1000);
            properties.put("mail.smtp.ssl.trust", "*");
            Session session = Session.getDefaultInstance(properties);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSender(new InternetAddress("InEK-Datenportal <DSAnfragen@inek-drg.de>"));
            if (_config.readConfigBool(ConfigKey.TestMode) && !_config.getDatabaseName().equals("DataPortal")) {
                addReceipients(message, "dataportaldev@inek-drg.de", Message.RecipientType.TO);
                body = createTestBody(recipient, cc, bcc) + body;
                subject = "!!! Testserver !!!  " + subject;
            } else {
                addReceipients(message, recipient, Message.RecipientType.TO);
                if (!cc.equals(recipient)) {
                    addReceipients(message, cc, Message.RecipientType.CC);
                }
                addReceipients(message, bcc, Message.RecipientType.BCC);
            }
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
            LOGGER.log(Level.SEVERE, "Mailer failed");
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean sendMailTemplate(MailTemplate template, String rec) {
        return sendMailFrom(template.getFrom(), rec, template.getBcc(), template.getSubject(), template.getBody());
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
            String serverName = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().
                    getRequest()).getServerName();
            String msg = "Server: " + serverName + "\r\n Mail template not found: " + name + "\r\n";
            sendMail(_config.readConfig(ConfigKey.ExceptionEmail), "MailTemplate not found", msg);
        }
        return template;
    }

    public String getFormalSalutation(Person person) {
        String salutation = person.getGender() == 1 ? Utils.getMessage("formalSalutationFemale") : Utils.
                getMessage("formalSalutationMale");
        salutation = salutation.replace(TITLE, person.getTitle()).replace(LASTNAME, person.getLastName()).
                replace("  ", " ");
        return salutation;
    }

    public String getSalutation(Person person) {
        String salutation = person.getGender() == 1 ? "Frau " : "Herr ";
        salutation += person.getTitle() + " " + person.getLastName();
        return salutation;
    }

    public boolean sendMailWithTemplate(String templateName, Map<String, String> substitutions, Person receiver) {
        MailTemplate template = getMailTemplate(templateName);
        if (template == null) {
            return false;
        }
        String subject = template.getSubject();
        String body = template.getBody().replace(FORMAL_SALUTATION, getFormalSalutation(receiver));
        for (String param : substitutions.keySet()) {
            String value = substitutions.get(param);
            subject = subject.replace(param, value);
            body = body.replace(param, value);
        }
        return sendMailFrom(template.getFrom(), receiver.getEmail(), template.getBcc(), subject, body);
    }

    public boolean sendActivationMail(AccountRequest accountRequest) {
        MailTemplate template = getMailTemplate("AccountActivationMail");
        if (template == null) {
            return false;
        }
        String salutation = getFormalSalutation(accountRequest);
        String codedUser = Utils.encodeUrl(accountRequest.getUser());
        String link = buildAppUrl() + "/Login/Activate.xhtml?key="
                + accountRequest.getActivationKey() + "&user=" + codedUser;
        String body = template.getBody()
                .replace(FORMAL_SALUTATION, salutation)
                .replace(LINK, link)
                .replace(USERNAME, accountRequest.getUser())
                .replace(ACTIVATIONKEY, accountRequest.getActivationKey());
        return sendMail(accountRequest.getEmail(), template.getBcc(), template.getSubject(), body);
    }

    private String buildAppUrl() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String protocol = externalContext.getRequestScheme() + "://";
        int port = externalContext.getRequestServerPort();
        String server = externalContext.getRequestServerName();
        String contextPath = externalContext.getRequestContextPath();
        return protocol + server + (port == Const.HTTP_PORT || port == Const.HTTPS_PORT ? "" : ":" + port) + contextPath;
    }

    public boolean sendReRegisterMail(Account account) {
        MailTemplate template = getMailTemplate("AccountReRegistrationMail");
        if (template == null) {
            return false;
        }
        String salutation = getFormalSalutation(account);
        String body = template.getBody()
                .replace(FORMAL_SALUTATION, salutation);
        return sendMail(account.getEmail(), template.getBcc(), template.getSubject(), body);
    }

    public boolean sendMailActivationMail(AccountChangeMail changeMail) {
        MailTemplate template = getMailTemplate("MailActivationMail");
        if (template == null) {
            return false;
        }
        String link = buildAppUrl() + "/Login/ActivateMail.xhtml?key="
                + changeMail.getActivationKey() + "&mail=" + changeMail.getMail();
        String body = template.getBody()
                //.replace("{formalSalutation}", salutation)
                .replace(LINK, link)
                .replace(EMAIL, changeMail.getMail())
                .replace(ACTIVATIONKEY, changeMail.getActivationKey());
        return sendMail(changeMail.getMail(), template.getBcc(), template.getSubject(), body);
    }

    public boolean sendPasswordActivationMail(PasswordRequest pwdRequest, Account account) {
        LOGGER.log(Level.INFO, "Password request for {0}", account.getId());
        MailTemplate template = getMailTemplate("PasswordActivationMail");
        if (template == null) {
            return false;
        }
        String salutation = getFormalSalutation(account);
        String link = buildAppUrl() + "/Login/ActivatePassword.xhtml?key="
                + pwdRequest.getActivationKey() + "&mail=" + account.getEmail();

        String body = template.getBody()
                .replace(FORMAL_SALUTATION, salutation)
                .replace(LINK, link)
                .replace(EMAIL, account.getEmail())
                .replace(ACTIVATIONKEY, pwdRequest.getActivationKey());
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
                .replace(FORMAL_SALUTATION, salutation)
                .replace(FEATURE, featureRequest.getFeature().getDescription());
        return sendMail(account.getEmail(), template.getBcc(), subject, body);
    }

    public void sendWarning(String head, Exception exception) {
        sendException(Level.WARNING, head, exception);
    }

    public void sendError(String head, Exception exception) {
        sendException(Level.WARNING, head, exception);
    }

    public void sendException(Level level, String head, Exception exception) {
        LOGGER.log(level, head, exception);

        StringBuilder msg = new StringBuilder();
        msg.append(head).append("\r\n\r\n");
        msg.append(exception.getMessage()).append("\r\n\r\n");
        for (StackTraceElement element : exception.getStackTrace()) {
            msg.append(element.toString()).append("\r\n");
        }

        String name = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).
                getServerName();
        String subject = "Exception reported by Server " + name;
        sendMail(_config.readConfig(ConfigKey.ExceptionEmail), subject, msg.toString());
    }

    public static String buildCC(List<String> ccEmails) {
        return ccEmails.stream().collect(Collectors.joining(";"));
    }

    private String createTestBody(String recipient, String cc, String bcc) {
        String bodyHeader;

        bodyHeader = "###### Empfänger ###### \n An: " + recipient + " \n CC: " + cc + " \n BCC: " + bcc + " \n ###### Ende Empfänger ###### \n \n";

        return bodyHeader;
    }
}
