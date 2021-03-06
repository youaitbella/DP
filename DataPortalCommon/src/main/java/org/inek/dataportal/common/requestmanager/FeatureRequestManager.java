package org.inek.dataportal.common.requestmanager;

import org.inek.dataportal.common.data.account.facade.AccountFacade;
import java.io.Serializable;
import java.util.Optional;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.data.icmt.entities.ContactRole;
import org.inek.dataportal.common.data.icmt.entities.Customer;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.AccountFeature;
import org.inek.dataportal.common.data.account.entities.AccountFeatureRequest;
import org.inek.dataportal.api.enums.FeatureState;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.data.icmt.facade.ContactRoleFacade;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.common.data.account.facade.AccountFeatureRequestFacade;
import org.inek.dataportal.common.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class FeatureRequestManager implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("FeatureRequestManager");
    private static final long serialVersionUID = 1L;
    @Inject
    private Mailer _mailer;
    @Inject
    private AccountFacade _accountFacade;
    @Inject
    private AccountFeatureRequestFacade _featureRequestFacade;
    @Inject
    private CustomerFacade _customerFacade;
    @Inject
    private ContactRoleFacade _roleFacade;
    private AccountFeatureRequest _request;
    private Account _account;

    @PostConstruct
    private void init() {
        String key = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("key");
        _request = _featureRequestFacade.findByApprovalKey(key);
        if (_request == null) {
            _account = null;
        } else {
            _account = _accountFacade.findAccount(_request.getAccountId());
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
            return Pages.FeatureApprovalApproved.URL();
        }
        return Pages.FeatureApprovalError.URL();
    }

    public String reject() {
        if (_account != null) {
            setNewState(FeatureState.REJECTED);
            _mailer.sendFeatureRequestAnswer("FeatureRejectMail", _account, _request);
            return Pages.FeatureApprovalApproved.URL();
        }
        return Pages.FeatureApprovalError.URL();
    }

    /**
     * silently remove a request
     *
     * @return
     */
    public String remove() {
        if (_account != null) {
            setNewState(null);
            return Pages.FeatureApprovalApproved.URL();
        }
        return Pages.FeatureApprovalError.URL();
    }

    private void setNewState(FeatureState newState) {
        Optional<AccountFeature> optFeature = _account.getFeatures()
                .stream()
                .filter(f -> f.getFeatureState() == FeatureState.REQUESTED && f.getFeature() == _request.getFeature())
                .findFirst();
        if (optFeature.isPresent()) {
            if (newState == null) {
                _account.getFeatures().remove(optFeature.get());
            } else {
                optFeature.get().setFeatureState(newState);
            }
        }
        _accountFacade.merge(_account);
        _featureRequestFacade.remove(_request);
    }
}
