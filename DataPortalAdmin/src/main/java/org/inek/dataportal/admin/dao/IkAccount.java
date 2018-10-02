/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.admin.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.ikadmin.entity.IkAdminFeature;

/**
 *
 * @author muellermi
 */
public class IkAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    public static List<IkAccount> createFromAccounts(List<Account> accounts) {
        List<IkAccount> ikAccounts = new ArrayList<>();
        accounts.forEach(account -> {
            account.getAdminIks().forEach(ai -> {
                ikAccounts.add(new IkAccount(account,
                        ai.getIk(),
                        ai.getMailDomain(),
                        account.getFirstName() + " " + account.getLastName(),
                        ai.getIkAdminFeatures()));
            });
        });
        return ikAccounts;
    }

    public IkAccount(Account account, int ik, String mailDomain, String userName, List<IkAdminFeature> ikAdminFeatures) {
        _account = account;
        _ik = ik;
        _mailDomain = mailDomain;
        _userName = userName;
        _ikAdminFeatures = ikAdminFeatures;
    }

    private Account _account;
    private int _ik;
    private String _mailDomain;
    private String _userName;
    private List<IkAdminFeature> _ikAdminFeatures = new ArrayList<>();

    public Account getAccount() {
        return _account;
    }

    public void setAccount(Account account) {
        this._account = account;
    }

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }

    public String getMailDomain() {
        return _mailDomain;
    }

    public void setMailDomain(String mailDomain) {
        this._mailDomain = mailDomain;
    }

    public String getUserName() {
        return _userName;
    }

    public void setUserName(String userName) {
        this._userName = userName;
    }

    public List<IkAdminFeature> getIkAdminFeatures() {
        return _ikAdminFeatures;
    }

    public void setIkAdminFeatures(List<IkAdminFeature> ikAdminFeatures) {
        this._ikAdminFeatures = ikAdminFeatures;
    }
}
