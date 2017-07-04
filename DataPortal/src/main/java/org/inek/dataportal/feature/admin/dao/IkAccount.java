/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.admin.dao;

import java.util.ArrayList;
import java.util.List;
import org.inek.dataportal.entities.account.Account;

/**
 *
 * @author muellermi
 */
public class IkAccount {

    public static List<IkAccount> createFromAccounts(List<Account> accounts) {
        List<IkAccount> ikAccounts = new ArrayList<>();
        accounts.forEach(account -> {
            account.getAdminIks().forEach(ai -> {
                ikAccounts.add(new IkAccount(account, ai.getIk()));
            });
        });
        return ikAccounts;
    }

    public IkAccount(Account account, int ik) {
        _account = account;
        _ik = ik;
    }
    private final Account _account;

    public Account getAccount() {
        return _account;
    }

    private final int _ik;

    public int getIk() {
        return _ik;
    }

}
