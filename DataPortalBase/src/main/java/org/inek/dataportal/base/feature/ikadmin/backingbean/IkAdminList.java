package org.inek.dataportal.base.feature.ikadmin.backingbean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.ikadmin.entity.IkAdmin;
import org.inek.dataportal.common.scope.FeatureScoped;

@Named
@FeatureScoped
public class IkAdminList {

    @Inject
    private SessionController _sessionController;

    private List<Integer> _adminIks = new ArrayList<>();
    private Account _account;

    public Account getAccount() {
        return _account;
    }

    public void setAccount(Account account) {
        this._account = account;
    }

    public List<Integer> getAdminIks() {
        return _adminIks;
    }

    public void setAdminIks(List<Integer> adminIks) {
        this._adminIks = adminIks;
    }

    @PostConstruct
    public void init() {
        setAdminIksList();
        setAccount(_sessionController.getAccount());
    }

    public void setDisclaimerDate() {
        _sessionController.getAccount().setIkAdminDisclaimer(new Date());
        _sessionController.saveAccount();
        setAdminIksList();
        setAccount(_sessionController.getAccount());
    }

    private void setAdminIksList() {
        _adminIks.clear();
        for (IkAdmin aik : _sessionController.getAccount().getAdminIks()) {
            _adminIks.add(aik.getIk());
        }
    }

}
