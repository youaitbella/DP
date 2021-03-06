/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.tree;

import org.inek.dataportal.calc.entities.CalcHospitalInfo;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;

/**
 *
 * @author muellermi
 */
public final class CalcHospitalTreeNode extends TreeNode{
    
    private final CalcHospitalInfo _info;
    
    public CalcHospitalInfo getCalcHospitalInfo (){
        return _info;
    }
    
    private CalcHospitalTreeNode(TreeNode parent, CalcHospitalInfo info) {
        super(parent);
        _info = info;
        setId(info.getId());
        setExpanded(true);
    }
    
    public static CalcHospitalTreeNode create (TreeNode parent, CalcHospitalInfo info, TreeNodeObserver observer) {
        CalcHospitalTreeNode node = new CalcHospitalTreeNode(parent, info);
        node.registerObserver(observer);
        return node;
    }
}
