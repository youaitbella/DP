/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data;

import java.util.Objects;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.CooperativeRight;
import org.inek.dataportal.api.enums.Feature;

/**
 *
 * @author muellermi
 */
public class IkSupervisorInfo {
    private Feature _feature;
    private int _ik;
    private Account _account;
    private CooperativeRight _right;

    public IkSupervisorInfo(Feature feature, int ik, Account account, CooperativeRight right) {
        _feature = feature;
        _ik = ik;
        _account = account;
        _right = right;
    }

    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        _feature = feature;
    }

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        _ik = ik;
    }

    public Account getAccount() {
        return _account;
    }

    public void setAccount(Account account) {
        _account = account;
    }

    public CooperativeRight getRight() {
        return _right;
    }

    public void setRight(CooperativeRight right) {
        _right = right;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(_feature);
        hash = 19 * hash + _ik;
        hash = 19 * hash + Objects.hashCode(_account);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IkSupervisorInfo other = (IkSupervisorInfo) obj;
        if (_ik != other._ik) {
            return false;
        }
        if (_feature != other._feature) {
            return false;
        }
        if (!Objects.equals(_account, other._account)) {
            return false;
        }
        return true;
    }

    
}
