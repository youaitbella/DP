/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.helper;

import org.inek.dataportal.common.data.adm.MailTemplate;

/**
 *
 * @author lautenti
 */
public class MailTemplateHelper {

    public static void setPlaceholderInTemplateBody(MailTemplate template, String placeholder, String newValue) {
        String body = template.getBody();
        body = body.replace(placeholder, newValue);
        template.setBody(body);
    }

    public static void setPlaceholderInTemplateSubject(MailTemplate template, String placeholder, String newValue) {
        String subject = template.getSubject();
        subject = subject.replace(placeholder, newValue);
        template.setSubject(subject);
    }

}
