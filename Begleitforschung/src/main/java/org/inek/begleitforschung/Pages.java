/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.begleitforschung;

import javax.faces.context.FacesContext;

/**
 *
 * @author muellermi
 */
public enum Pages {

    Participation("/views/Participation");

    private final String _url;
    Pages(String url) {
        _url = url;
    }

    public String URL() {
        return _url + ".xhtml";
    }

    public String RedirectURL() {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + _url + ".xhtml?faces-redirect=true";
    }

}