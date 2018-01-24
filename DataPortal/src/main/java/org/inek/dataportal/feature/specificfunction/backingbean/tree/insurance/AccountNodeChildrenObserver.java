/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.specificfunction.backingbean.tree.insurance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.inek.dataportal.common.AccessManager;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.feature.specificfunction.entity.SpecificFunctionAgreement;
import org.inek.dataportal.feature.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.tree.SpecificFunctionAgreementTreeNode;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;
import org.inek.portallib.tree.YearTreeNode;

/**
 *
 * @author aitbellayo
 */
@Dependent
public class AccountNodeChildrenObserver implements TreeNodeObserver{
    @Inject private AccessManager _accessManager;
    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private SessionController _sessionController;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        return obtainAccountNodeChildren((AccountTreeNode) treeNode); //To change body of generated methods, choose Tools | Templates.
    }
    
    private Collection<TreeNode> obtainAccountNodeChildren(AccountTreeNode treeNode) {
        int partnerId = treeNode.getId();
        List<SpecificFunctionAgreement> infos;
        if (treeNode.getParent() instanceof YearTreeNode) {
            int year = treeNode.getParent().getId();
            infos = obtainAgreementsForRead(partnerId, year);
        } else {
            infos = obtainAgreementsForEdit(partnerId);
        }
        Collection<TreeNode> children = new ArrayList<>();
        for (SpecificFunctionAgreement info : infos) {
            children.add(SpecificFunctionAgreementTreeNode.create(treeNode, info, this));
        }
        return children;
    }
    
    private List<SpecificFunctionAgreement> obtainAgreementsForRead(int partnerId, int year) {
        WorkflowStatus statusLow = WorkflowStatus.Provided;
        WorkflowStatus statusHigh = WorkflowStatus.Retired;
        if (partnerId != _sessionController.getAccountId()) {
            boolean canReadSealed = _accessManager.canReadSealed(Feature.SPECIFIC_FUNCTION, partnerId);
            if (!canReadSealed) {
                statusLow = WorkflowStatus.Unknown;
                statusHigh = WorkflowStatus.Unknown;
            }
        }
        return _specificFunctionFacade.obtainSpecificFunctionAgreements(partnerId, year, statusLow, statusHigh);
    }
    
    private List<SpecificFunctionAgreement> obtainAgreementsForEdit(int partnerId) {
        WorkflowStatus statusLow;
        WorkflowStatus statusHigh;
        if (partnerId == _sessionController.getAccountId()) {
            statusLow = WorkflowStatus.New;
            statusHigh = WorkflowStatus.ApprovalRequested;
        } else {
            boolean canReadAlways = _accessManager.canReadAlways(Feature.SPECIFIC_FUNCTION, partnerId);
            boolean canReadCompleted = _accessManager.canReadCompleted(Feature.SPECIFIC_FUNCTION, partnerId);
            statusLow = canReadAlways ? WorkflowStatus.New
                    : canReadCompleted ? WorkflowStatus.ApprovalRequested : WorkflowStatus.Unknown;
            statusHigh = canReadAlways
                    || canReadCompleted ? WorkflowStatus.ApprovalRequested : WorkflowStatus.Unknown;
        }
        return _specificFunctionFacade.obtainSpecificFunctionAgreements(
                partnerId,
                Utils.getTargetYear(Feature.SPECIFIC_FUNCTION),
                statusLow,
                statusHigh);
    }
    
}
