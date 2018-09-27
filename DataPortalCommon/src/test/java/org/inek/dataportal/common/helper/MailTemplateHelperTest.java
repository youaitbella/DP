/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.helper;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.junit.jupiter.api.Test;

/**
 *
 * @author lautenti
 */
public class MailTemplateHelperTest {

    public MailTemplateHelperTest() {
    }

    @Test
    public void testReplaceBodyWithExistsPlaceholder() {
        MailTemplate template = new MailTemplate();

        String body = "{salutation} ihr da draußen";
        String placeholder = "{salutation}";
        String valueForReplace = "Anrede";

        template.setBody(body);

        MailTemplateHelper.setPlaceholderInTemplateBody(template, placeholder, valueForReplace);

        Assertions.assertThat(template.getBody()).isEqualTo("Anrede ihr da draußen");
    }

    @Test
    public void testReplaceSubjectWithExistsPlaceholder() {
        MailTemplate template = new MailTemplate();

        String subject = "{salutation} ihr da draußen";
        String placeholder = "{salutation}";
        String valueForReplace = "Anrede";

        template.setSubject(subject);

        MailTemplateHelper.setPlaceholderInTemplateSubject(template, placeholder, valueForReplace);

        Assertions.assertThat(template.getSubject()).isEqualTo("Anrede ihr da draußen");
    }

}
