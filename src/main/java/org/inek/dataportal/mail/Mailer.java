/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.mail;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.inek.dataportal.entities.PasswordRequest;
import org.inek.dataportal.entities.account.AccountChangeMail;
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
public class Mailer {

    protected static final Logger _logger = Logger.getLogger("Mailer");

    @Inject
    MailTemplateFacade _mailTemplateFacade;

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    // place getter and setters here
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    // place this methods here
    // </editor-fold>
    public boolean sendMail(String recipient, String subject, String body) {
        return sendMail(recipient, "", subject, body);
    }

    public boolean sendMail(String recipient, String bcc, String subject, String body) {
        if (recipient.toLowerCase().endsWith(".test")) {
            // this is just to mock a successful mail
            return true;
        }
        //String from = "do-not-reply@inek-drg.de";
        String from = "anfragen@datenstelle.de";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "exchangeserver1.d1inek.local");
        properties.put("mail.smtp.connectiontimeout", 1000);
        properties.put("mail.smtp.ssl.trust", "*");
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            if (!bcc.isEmpty()) {
                message.setRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
            }
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            return true;
        } catch (MessagingException ex) {
            _logger.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean sendActivationMail(AccountRequest accountRequest) {
        MailTemplate template = getMailTemplate("AccountActivationMail");
        if (template == null) {
            return false;
        }
        String salutation = getFormalSalutation(accountRequest);
        String link = PropertyManager.INSTANCE.getProperty(PropertyKey.ApplicationURL) + "/login/Activate.xhtml?key=" + accountRequest.getActivationKey() + "&user=" + accountRequest.getUser().replace(" ", "%20");
        String body = template.getBody()
                .replace("{formalSalutation}", salutation)
                .replace("{link", link)
                .replace("{username}", accountRequest.getUser())
                .replace("{activationkey}", accountRequest.getActivationKey());
        return sendMail(accountRequest.getEmail(), template.getBcc(), template.getSubject(), body);
    }

    private MailTemplate getMailTemplate(String name) {
        MailTemplate template = _mailTemplateFacade.findByName(name);
        if (template == null) {
            String serverName = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
            String msg = "Server: " + serverName + "\r\n Mail template not found: " + name + "\r\n";
            sendMail(PropertyManager.INSTANCE.getProperty(PropertyKey.ExceptionEmail), "MailTemplate not found", msg);
        }
        return template;
    }

    private String getFormalSalutation(Person person) {
        String salutation = person.getGender() == 1 ? Utils.getMessage("formalSalutationFemale") : Utils.getMessage("formalSalutationFemale");
        salutation = salutation.replace("{title}", person.getTitle()).replace("{lastname}", person.getLastName()).replace("  ", " ");
        return salutation;
    }

    public boolean sendMailActivationMail(AccountChangeMail changeMail) {
        String link = PropertyManager.INSTANCE.getProperty(PropertyKey.ApplicationURL) + "/login/ActivateMail.xhtml?key=" + changeMail.getActivationKey() + "&mail=" + changeMail.getMail();
        String body = Utils.getMessage("msgActivateMail") + "\r\n" + link + "\r\n" + Utils.getMessage("msgActivateMailInfo");
        body = body.replace("{mail}", changeMail.getMail());
        body = body.replace("{activationkey}", changeMail.getActivationKey());
        return sendMail(changeMail.getMail(), Utils.getMessage("headerActivateMail"), body);
    }

    public boolean sendPasswordActivationMail(PasswordRequest pwdRequest, String mail) {
        MailTemplate template = getMailTemplate("PasswordActivationMail");
        if (template == null) {
            return false;
        }
//        String salutation = getFormalSalutation(account);
        String link = PropertyManager.INSTANCE.getProperty(PropertyKey.ApplicationURL) + "/login/ActivatePassword.xhtml?key=" + pwdRequest.getActivationKey() + "&mail=" + mail;
        String body = Utils.getMessage("msgActivatePwd") + "\r\n" + link + "\r\n" + Utils.getMessage("msgActivatePwdInfo");
        body = body.replace("{mail}", mail);
        body = body.replace("{activationkey}", pwdRequest.getActivationKey());
        return sendMail(mail, Utils.getMessage("headerActivatePwd"), body);
    }

}
