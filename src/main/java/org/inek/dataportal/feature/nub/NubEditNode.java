/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.nub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.Feature;

/**
 *
 * @author muellermi
 */
public class NubEditNode extends TreeNode{
    CooperationTools _cooperationTools;
    private Map<Integer, AccountTreeNode> _children = new LinkedHashMap<>();
    
    public NubEditNode(CooperationTools cooperationTools){
                _cooperationTools = cooperationTools;
    }
            
    @Override
    public void obtainChildrenIfIsExpanded() {
        List<Account> accounts = _cooperationTools.getPartnersForEdit(Feature.NUB);
        Map<Integer, AccountTreeNode> children = new LinkedHashMap<>(accounts.size());
        for (Account account : accounts) {
            Integer id = account.getId();
            AccountTreeNode node = _children.containsKey(id) ? _children.get(id) : AccountTreeNode.createTreeNode(account);
            node.obtainChildrenIfIsExpanded();
            children.put(id, node);
        }
        _children = children;
    }

    @Override
    public Collection<AccountTreeNode> getChildren() {
        //return _children.values();  -- ok for c:forEach, but produces an error with ui:repeat
        return new ArrayList<>(_children.values());
    }
    
    @Override
    protected void expandNode() {
        throw new UnsupportedOperationException("NubEditNode does not support expand/collape."); 
    }

    @Override
    protected void collapseNode() {
        throw new UnsupportedOperationException("NubEditNode does not support expand/collape.");
    }

}
