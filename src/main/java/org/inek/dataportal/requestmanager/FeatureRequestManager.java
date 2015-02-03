package org.inek.dataportal.requestmanager;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.entities.ContactRole;
import org.inek.dataportal.entities.Customer;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountFeature;
import org.inek.dataportal.entities.account.AccountFeatureRequest;
import org.inek.dataportal.enums.FeatureState;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.ContactRoleFacade;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.account.AccountFeatureRequestFacade;
import org.inek.dataportal.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class FeatureRequestManager implements Serializable {

    private static final Logger _logger = Logger.getLogger("FeatureRequestManager");
    private static final long serialVersionUID = 1L;
    @Inject Mailer _mailer;
    @Inject AccountFacade _accountFacade;
    @Inject AccountFeatureRequestFacade _featureRequestFacade;
    @Inject CustomerFacade _customerFacade;
    @Inject ContactRoleFacade _roleFacade;
    private AccountFeatureRequest _request;
    private Account _account;

    @PostConstruct
    private void init() {
        String key = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("key");
        _request = _featureRequestFacade.findByApprovalKey(key);
        if (_request == null) {
            _account = null;
        } else {
            _account = _accountFacade.find(_request.getAccountId());
            if (_account.getUser().isEmpty()) {
                _account = null;
            }
        }
    }

    public Account getAccount() {
        return _account;
    }

    public String getFeature() {
        if (_request == null || _request.getFeature() == null) {
            return "<unknown or handled feature request>";
        }
        return _request.getFeature().getDescription();
    }

    public boolean isRequestFound() {
        return _request != null;
    }

    public String getRole() {
        if (_account == null) {
            return "";
        }
        ContactRole role = _roleFacade.find(_account.getRoleId());
        return role.getText();
    }

    public String approve() {
        if (_account != null) {
            setNewState(FeatureState.APPROVED);
            _mailer.sendFeatureRequestAnswer("FeatureApprovalMail", _account, _request);
            //_accountFacade.clearCache();
            return (redirect(Pages.AdminApproved.URL()));
        }
        return (redirect(Pages.AdminError.URL()));
    }

    public String redirect(String url) throws FacesException {
        FacesContext ctx = FacesContext.getCurrentInstance();

        ExternalContext extContext = ctx.getExternalContext();
        String target = extContext.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, url));
        try {
            extContext.redirect(target);
        } catch (IOException ex) {
            throw new FacesException(ex);
        }
        return "";
    }

    public String reject() {
        if (_account != null) {
            setNewState(FeatureState.REJECTED);
            _mailer.sendFeatureRequestAnswer("FeatureRejectMail", _account, _request);
            //_accountFacade.clearCache(Account.class);
            return redirect(Pages.AdminApproved.URL());
        }
        return redirect(Pages.AdminError.URL());
    }

    private void setNewState(FeatureState newState) {
        for (AccountFeature feature : _account.getFeatures()) {
            if (feature.getFeatureState() == FeatureState.REQUESTED && feature.getFeature() == _request.getFeature()) {
                feature.setFeatureState(newState);
            }
        }
        _accountFacade.merge(_account);
        _featureRequestFacade.remove(_request);
    }

    public String getIkKnown() {
        if (_account == null) {
            return "";
        }
        Integer ik = _account.getIK();
        if (ik == null) {
            return "---";
        }
        Customer cust = _customerFacade.getCustomerByIK(ik);
        if (cust == null) {
            return " (New IK)";
        }
        return " (IK well known in ICMT)";
    }

}
