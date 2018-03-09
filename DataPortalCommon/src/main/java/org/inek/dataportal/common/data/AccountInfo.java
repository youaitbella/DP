/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data;

import org.inek.dataportal.common.data.account.entities.Account;

/**
 *
 * @author muellermi
 */
public class AccountInfo {
    private Account _account;
    private boolean _isSelected;
    private int _count;
    
    public AccountInfo(Account account, boolean isSelected, int count) {
        _account = account;
        _isSelected = isSelected;
        _count = count;
    }

    public Account getAccount() {
        return _account;
    }

    public void setAccount(Account account) {
        _account = account;
    }

    public boolean isSelected() {
        return _isSelected;
    }

    public void setSelected(boolean isSelected) {
        _isSelected = isSelected;
    }
    
    public int getCount() {
        return _count;
    }

    public void setCount(int count) {
        _count = count;
    }

    @Override
    public int hashCode() {
        return _account.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return _account.equals(obj);
    }

}
