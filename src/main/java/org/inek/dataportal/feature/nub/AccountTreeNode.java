/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.nub;

import java.util.Collection;
import java.util.Collections;
import org.inek.dataportal.entities.account.Account;

/**
 *
 * @author muellermi
 */
public class AccountTreeNode extends TreeNode {

    private final Account _account;

    public Account getAccount() {
        return _account;
    }

    public void setAccount(Account account) {
    }

    private AccountTreeNode(Account account) {
        _account = account;
        setId(account.getId());
    }

    public static AccountTreeNode createTreeNode(Account account) {
        return new AccountTreeNode(account);
    }


    public void updateChildrenIfIsExpanded() {
        if (isExpanded()) {
            obtainChildren();
        }
    }

    private void obtainChildren() {
        // todo
    }


    @Override
    public Collection<TreeNode> getChildren() {
        return Collections.EMPTY_LIST;
    }

    @Override
    protected void expandNode() {
        obtainChildren();
    }

    @Override
    protected void collapseNode() {
    }

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _account.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccountTreeNode)) {
            return false;
        }
        AccountTreeNode other = (AccountTreeNode) object;
        return _account.equals(other._account);
    }
    
}
