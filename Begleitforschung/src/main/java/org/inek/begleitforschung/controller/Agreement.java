package org.inek.begleitforschung.controller;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author vohldo
 */
@Named
@SessionScoped
public class Agreement implements Serializable {

    private boolean _agreementAccepted = false;
    
    @PostConstruct
    private void postConstruct() {
        FacesContext fCtx = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
        session.invalidate();
    }

    public void acceptedAgreement() {
        _agreementAccepted = true;
    }

    public boolean hasAgreement() {
        return _agreementAccepted;
    }

    public void checkAgreement() throws IOException {
        if (!_agreementAccepted) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext()
                    .redirect(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath());
        }
    }

    private boolean _checkboxAgreement;

    public boolean isCheckboxAgreement() {
        return _checkboxAgreement;
    }

    public void setCheckboxAgreement(boolean checkboxAgreement) {
        this._checkboxAgreement = checkboxAgreement;
    }

    public void startBrowser() throws IOException {
        acceptedAgreement();
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getExternalContext()
                .redirect(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath());
    }

    public String start() {
        acceptedAgreement();
        return "index.xhtml?faces-redirect=true";
    }
}
