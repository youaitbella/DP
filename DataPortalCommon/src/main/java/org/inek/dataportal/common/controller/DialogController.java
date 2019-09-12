package org.inek.dataportal.common.controller;

import org.primefaces.PrimeFaces;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author muellermi
 */
public class DialogController{

    public static void showWarningDialog(String title, String message) {
        PrimeFaces.current().executeScript("swal('" + title + "', '" + message + "', 'warning');");
    }

    public static void showInfoDialog(String title, String message) {
        PrimeFaces.current().executeScript("swal('" + title + "', '" + message + "', 'info');");
    }

    public static void showErrorDialog(String title, String message) {
        PrimeFaces.current().executeScript("swal('" + title + "', '" + message + "', 'error');");
    }

    public static void showSuccessDialog(String title, String message) {
        PrimeFaces.current().executeScript("swal('" + title + "', '" + message + "', 'success');");
    }

    public static void showAccessDeniedDialog() {
        PrimeFaces.current().executeScript("swal('Zugriff Verweigert', 'Sie haben keine Zugriffsrechte auf diese Daten', 'error');");
    }

    public static void showInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
    }

    public static void showSaveDialog() {
        showSuccessDialog("Speichern erfolgreich",
                "Ihre Daten wurden gespeichert.\\r\\n "
                        + "Eventuell fehlerhafte Felder wurden dabei nicht übernommen (behalten vorherige Werte).");
    }

    public static void showSendDialog() {
        showSuccessDialog("Daten Gesendet", "Ihre Daten wurden erfolgreich an das InEK gesendet");
    }

    public static void showOldAlertMessage(String message) {
        String script = "alert ('" + message.replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
        PrimeFaces.current().executeScript(script);
    }

    public static void openDialogByName(String dialogWidgetVarName) {
        PrimeFaces.current().executeScript("PF('" + dialogWidgetVarName + "').show();");
    }
}
