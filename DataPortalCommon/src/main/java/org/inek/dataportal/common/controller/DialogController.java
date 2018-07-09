package org.inek.dataportal.common.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
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

    public void showWarningDialog(String message, String title) {
        PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_WARN, title, message));
    }

    public void showInfoDialog(String message, String title) {
        PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, title, message));
    }

    public void showInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
    }

    public void showSaveDialog() {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("contentWidth", 400);
        options.put("contentHeight", 80);
        options.put("closable", false);
        PrimeFaces.current().dialog().openDynamic("/Dialog/SaveDialog", options, null);
    }
}
