/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.tree.entityTree;

import org.inek.dataportal.helper.tree.TreeNode;
import org.inek.dataportal.helper.tree.TreeNodeObserver;
import org.inek.dataportal.entities.icmt.Customer;

/**
 *
 * @author muellermi
 */
public final class CustomerTreeNode extends EntityTreeNode{

    private final Customer _customer;

    @Override
    public String getDisplayName(){
        return "" + _customer.getIK();
    }
    
    @Override
    public String getCompany(){
        return _customer.getName();
    }

    @Override
    public String getTown(){
        return _customer.getTown();
    }

    public int getIk(){
        return _customer.getIK();
    }
    
    private CustomerTreeNode(TreeNode parent, Customer customer) {
        super(parent);
        setId(-customer.getIK());
        _customer = customer;
    }

    public static CustomerTreeNode create(TreeNode parent, Customer customer, TreeNodeObserver observer) {
        CustomerTreeNode node = new CustomerTreeNode(parent, customer);
        node.registerObserver(observer);
        return node;
    }
    
}
