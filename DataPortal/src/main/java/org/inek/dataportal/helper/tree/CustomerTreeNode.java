/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.tree;

import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;
import org.inek.dataportal.entities.icmt.Customer;

/**
 *
 * @author muellermi
 */
public final class CustomerTreeNode extends EntityTreeNode{


    private final String _displayName;
    @Override
    public String getDisplayName(){
        return _displayName;
    }
    
    private final String _company;
    @Override
    public String getCompany(){
        return _company;
    }

    private final String _town;
    @Override
    public String getTown(){
        return _town;
    }

    private CustomerTreeNode(TreeNode parent, Customer customer) {
        super(parent);
        setId(-customer.getIK());
        _displayName = "" + customer.getIK();
        _company = customer.getName();
        _town = customer.getTown();
    }

    public static CustomerTreeNode create(TreeNode parent, Customer customer, TreeNodeObserver observer) {
        CustomerTreeNode node = new CustomerTreeNode(parent, customer);
        node.registerObserver(observer);
        return node;
    }
    
}
