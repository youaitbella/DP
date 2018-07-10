package org.inek.dataportal.common.controller;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author muellermi
 */
@Named
@SessionScoped
public class DialogController implements Serializable {

    private static final long serialVersionUID = 1L;

    public void showWarningDialog(String title, String message) {
        PrimeFaces.current().executeScript("swal('" + title + "', '" + message + "', 'warning');");
    }

    public void showInfoDialog(String title, String message) {
        PrimeFaces.current().executeScript("swal('" + title + "', '" + message + "', 'info');");
    }

    public void showErrorDialog(String title, String message) {
        PrimeFaces.current().executeScript("swal('" + title + "', '" + message + "', 'error');");
    }

    public void showInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
    }

    public void showSaveDialog() {
        PrimeFaces.current().executeScript("swal('Speichern erfolgreich', 'Ihre Daten wurden erfolgreich gespeichert', 'success');");
    }

    public void showSendDialog() {
        PrimeFaces.current().executeScript("swal('Daten Gesendet', 'Ihre Daten wurden erfolgreich an das InEK gesendet', 'success');");
    }
}
