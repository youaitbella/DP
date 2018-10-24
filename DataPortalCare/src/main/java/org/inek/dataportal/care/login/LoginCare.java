package org.inek.dataportal.care.login;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.PortalType;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Utils;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author lautenti
 */
@Named
@ViewScoped
public class LoginCare implements Serializable {

    @Inject
    private SessionController _sessionController;

    private String _emailOrUser;
    private String _password;

    public String getEmailOrUser() {
        return _emailOrUser;
    }

    public void setEmailOrUser(String email) {
        _emailOrUser = email;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }

    public String login() {
        if (!_sessionController.loginAndSetTopics(_emailOrUser, _password, PortalType.CARE)) {
            DialogController.showErrorDialog("Falsche Eingabe", "Name bzw. Email und / oder Kennwort sind ungültig");
            return "";
        }
        if (!_sessionController.getAccount().getFeatures().stream().anyMatch(c -> c.getFeature() == Feature.CARE)) {
            DialogController.showErrorDialog("Keine Berechtigung",
                    "Sie sind nicht für den Bereich Pflege im InEK Datenportal freigeschaltet. "
                    + "Bitte wenden Sie sich an den Funktionsbeauftragen ihrem Haus.");
        } else {
            return Pages.CareDeptSummary.RedirectURL();
        }
        return "";
    }

}
