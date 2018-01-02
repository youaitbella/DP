/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.tree;

import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;
import org.inek.dataportal.entities.account.Account;

/**
 *
 * @author muellermi
 */
public final class AccountTreeNode extends TreeNode{

    private final Account _account;

    public Account getAccount() {
        return _account;
    }

    public String getDisplayName(){
        return _account.getFirstName() + " " + _account.getLastName();
    }
    
    public String getCompany(){
        return _account.getCompany();
    }

    public String getTown(){
        return _account.getTown();
    }

    public String getEmail(){
        return _account.getEmail();
    }

    public int getAccountId(){
        return _account.getId();
    }
    
    private AccountTreeNode(TreeNode parent, Account account) {
        super(parent);
        _account = account;
        setId(account.getId());
    }

    public static AccountTreeNode create(TreeNode parent, Account account, TreeNodeObserver observer) {
        AccountTreeNode node = new AccountTreeNode(parent, account);
        node.registerObserver(observer);
        return node;
    }
    
}
