/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.cert.backingbean;

import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.cert.entities.AdditionalEmail;
import org.inek.dataportal.cert.facade.GrouperFacade;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.overall.SessionTools;
import org.inek.dataportal.common.scope.FeatureScoped;

/**
 *
 * @author vohldo
 */
@Named
@FeatureScoped(name = "Certification")
public class CertGrouperCc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private GrouperFacade _grouperFacade;
    @Inject
    private SessionController _sessionController;
    @Inject
    private SessionTools _sessionTools;

    private List<AdditionalEmail> _additionalEmails;

    public List<AdditionalEmail> getAdditionalEmails() {
        if (_additionalEmails == null) {
            _additionalEmails = _grouperFacade.findGrouperEmailReceivers(_sessionController.getAccountId());
        }
        return _additionalEmails;
    }

    public void addNewAdditonalEmail() {
        AdditionalEmail ae = new AdditionalEmail();
        ae.setAccountId(_sessionController.getAccountId());
        _additionalEmails.add(ae);
    }

    public void selectedAccountChanged() {
        _additionalEmails = null;
    }

    public void saveGrouperCcs() {
        try {
            _additionalEmails.removeIf(ae -> ae.getEmail().trim().isEmpty());
            _additionalEmails.stream()
                    .forEach(ae -> _grouperFacade.saveAdditionalEmailCc(ae));
            DialogController.showSaveDialog();
        } catch (Exception ex) {
            DialogController.showErrorDialog("Fehler beim Speichern", "Die Daten konnten nicht gespeichert werden");
        }
    }

    public void deleteAdditionalCc(AdditionalEmail ae) {
        _additionalEmails.remove(ae);
        if (ae.getId() != null) {
            _grouperFacade.deleteAdditionalEmailCc(ae.getId());
        }
    }

    public void checkEmail(FacesContext context, UIComponent component, Object value) {
        String input = "" + value;
        if (!_sessionTools.isValidNonTrashEmail(input)) {
            String msg = "Bitte eine gültige Adresse eingeben";
            throw new ValidatorException(new FacesMessage(msg));
        }
    }
}
