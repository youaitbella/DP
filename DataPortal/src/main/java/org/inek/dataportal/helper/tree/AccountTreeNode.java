/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.tree;

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
