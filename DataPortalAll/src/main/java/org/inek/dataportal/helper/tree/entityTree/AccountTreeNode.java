/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.tree.entityTree;

import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.feature.account.entities.Account;

/**
 *
 * @author muellermi
 */
public final class AccountTreeNode extends EntityTreeNode{

    private final Account _account;

    @Override
    public String getDisplayName(){
        return _account.getFirstName() + " " + _account.getLastName();
    }
    
    @Override
    public String getCompany(){
        return _account.getCompany();
    }

    @Override
    public String getTown(){
        return _account.getTown();
    }

    public String getEmail(){
        return _account.getEmail();
    }

    public int getAccountId(){
        return _account.getId();
    }
    
    public Account getAccount(){
        return _account;
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
