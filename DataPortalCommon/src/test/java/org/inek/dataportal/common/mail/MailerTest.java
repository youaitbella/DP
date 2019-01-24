package org.inek.dataportal.common.mail;

import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.common.User;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MailerTest {

    private static final String SENDER = "sender@test.org";
    private static final String BCC = "blind@test.org";
    private static final String USER_EMAIL = "john.doe@test.org";
    private static final String SUBJECT = "Test {name}";
    private static final String BODY = "{formalSalutation}, Name: {name} IK: {ik}";

    public MailerTest() {
    }

    @Test
    public void sendMailWithTemplate() {
        MailTemplate template = new MailTemplate();
        template.setFrom(SENDER);
        template.setBcc(BCC);
        template.setSubject(SUBJECT);
        template.setBody(BODY);
        
        Mailer mailer = spy(Mailer.class);
        doReturn(template).when(mailer).getMailTemplate("test");
        doReturn(true).when(mailer).sendMailFrom(anyString(), anyString(), anyString(), anyString(), anyString());

        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("{name}", "NAME");
        substitutions.put("{ik}", "123456789");

        User user = new User(0, 2, "", "John", "Doe", USER_EMAIL, "");

        boolean result = mailer.sendMailWithTemplate("test", substitutions, user);
        assertThat(result).isTrue();

        ArgumentCaptor<String> sender = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> receiver = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bcc = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subject = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> body = ArgumentCaptor.forClass(String.class);
        verify(mailer).sendMailFrom(sender.capture(), receiver.capture(), bcc.capture(), subject.capture(), body.capture());

        assertThat(sender.getValue()).isEqualTo(SENDER);
        assertThat(receiver.getValue()).isEqualTo(USER_EMAIL);
        assertThat(bcc.getValue()).isEqualTo(BCC);
        assertThat(subject.getValue()).isEqualTo("Test NAME");
        assertThat(body.getValue()).isEqualTo("Sehr geehrter Herr Doe, Name: NAME IK: 123456789");
    }

}
