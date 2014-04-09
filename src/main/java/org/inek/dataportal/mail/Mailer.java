/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.mail;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.inek.dataportal.entities.account.AccountChangeMail;
import org.inek.dataportal.entities.account.AccountRequest;
import org.inek.dataportal.entities.PasswordRequest;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.PropertyKey;
import org.inek.dataportal.utils.PropertyManager;

/**
 *
 * @author muellermi
 */
public class Mailer {
    protected static final Logger _logger = Logger.getLogger("Mailer");

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    // place getter and setters here
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    // place this methods here
    // </editor-fold>
    public static void sendMail(String recipient, String subject, String body) throws MessagingException {
        sendMail(recipient, "", subject, body);
    }

    public static void sendMail(String recipient, String bcc, String subject, String body) throws MessagingException {
        if (recipient.toLowerCase().endsWith(".test")) {
            return;
        }
        //String from = "do-not-reply@inek-drg.de";
        String from = "anfragen@datenstelle.de";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "exchangeserver1.d1inek.local");
        properties.put("mail.smtp.connectiontimeout", 1000);
        properties.put("mail.smtp.ssl.trust", "*");
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        if (!bcc.isEmpty()){
            message.setRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
        }
        message.setSubject(subject);
        message.setText(body);
        Transport.send(message);
    }
    
    public static boolean sendActivationMail(AccountRequest accountRequest) {
        String link = PropertyManager.INSTANCE.getProperty(PropertyKey.ApplicationURL) + "/login/Activate.xhtml?key=" + accountRequest.getActivationKey() + "&user=" + accountRequest.getUser().replace(" ", "%20");
        String body = Utils.getMessage("msgActivate") + "\r\n" + link + "\r\n" + Utils.getMessage("msgActivateInfo");
        body = body.replace("{username}", accountRequest.getUser());
        body = body.replace("{activationkey}", accountRequest.getActivationKey());
        try {
            Mailer.sendMail(accountRequest.getEmail(), "PortalAnmeldung@datenstelle.de", Utils.getMessage("headerActivate"), body);
        } catch (MessagingException ex) {
            _logger.log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public static boolean sendMailActivationMail(AccountChangeMail changeMail) {
        String link = PropertyManager.INSTANCE.getProperty(PropertyKey.ApplicationURL) + "/login/ActivateMail.xhtml?key=" + changeMail.getActivationKey() + "&mail=" + changeMail.getMail();
        String body = Utils.getMessage("msgActivateMail") + "\r\n" + link + "\r\n" + Utils.getMessage("msgActivateMailInfo");
        body = body.replace("{mail}", changeMail.getMail());
        body = body.replace("{activationkey}", changeMail.getActivationKey());
        try {
            Mailer.sendMail(changeMail.getMail(), Utils.getMessage("headerActivateMail"), body);
        } catch (MessagingException ex) {
            _logger.log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public static boolean sendPasswordActivationMail(PasswordRequest pwdRequest, String mail) {
        String link = PropertyManager.INSTANCE.getProperty(PropertyKey.ApplicationURL) + "/login/ActivatePassword.xhtml?key=" + pwdRequest.getActivationKey() + "&mail=" + mail;
        String body = Utils.getMessage("msgActivatePwd") + "\r\n" + link + "\r\n" + Utils.getMessage("msgActivatePwdInfo");
        body = body.replace("{mail}", mail);
        body = body.replace("{activationkey}", pwdRequest.getActivationKey());
        try {
            Mailer.sendMail(mail, Utils.getMessage("headerActivatePwd"), body);
        } catch (MessagingException ex) {
            _logger.log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    
}
