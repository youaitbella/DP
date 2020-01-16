package org.inek.dataportal.common.controller;

import org.primefaces.PrimeFaces;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class DialogController{

    public static final String ALERT = "swal('%s', '%s', '%s');";

    private static void alert(String title, String message, String warning) {
        PrimeFaces.current().executeScript(String.format(ALERT, title, message, warning));
    }

    public static void showWarningDialog(String title, String message) {
        alert(title, message, "warning");
    }

    public static void showInfoDialog(String title, String message) {
        alert(title, message, "info");
    }

    public static void showErrorDialog(String title, String message) {
        alert(title, message, "error");
    }

    public static void showSuccessDialog(String title, String message) {
        alert(title, message, "success");
    }

    public static void showAccessDeniedDialog() {
        alert("Zugriff Verweigert", "Sie haben keine Zugriffsrechte auf diese Daten", "error");
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

    public static void openDialogByName(String dialogWidgetVarName) {
        PrimeFaces.current().executeScript("PF('" + dialogWidgetVarName + "').show();");
    }
}
