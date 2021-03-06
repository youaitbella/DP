/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.insurance.specificfunction.backingbean.tree.insurance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.insurance.specificfunction.entity.SpecificFunctionAgreement;
import org.inek.dataportal.insurance.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.insurance.specificfunction.backingbean.tree.SpecificFunctionAgreementTreeNode;
import org.inek.dataportal.common.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;

/**
 *
 * @author aitbellayo
 */
@Dependent
public class AccountTreeNodeObserver implements TreeNodeObserver {

    @Inject private AccessManager _accessManager;
    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private SessionController _sessionController;
    @Inject private ApplicationTools _appTools;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        return obtainAccountNodeChildren((AccountTreeNode) treeNode); //To change body of generated methods, choose Tools | Templates.
    }

    private Collection<TreeNode> obtainAccountNodeChildren(AccountTreeNode treeNode) {
        int partnerId = treeNode.getId();
        List<SpecificFunctionAgreement> infos;
        if (treeNode.getParent().getId() == 1) {
            infos = obtainAgreementsForEdit(partnerId);
        } else {
            infos = obtainAgreementsForRead(partnerId);
        }
        Collection<TreeNode> children = new ArrayList<>();
        for (SpecificFunctionAgreement info : infos) {
            children.add(SpecificFunctionAgreementTreeNode.create(treeNode, info, null));
        }
        return children;
    }

    private List<SpecificFunctionAgreement> obtainAgreementsForRead(int partnerId) {
        WorkflowStatus statusLow = WorkflowStatus.Provided;
        WorkflowStatus statusHigh = WorkflowStatus.Retired;
        if (partnerId != _sessionController.getAccountId()) {
            boolean canReadSealed = _accessManager.canRead(Feature.SPECIFIC_FUNCTION, partnerId);
            if (!canReadSealed) {
                statusLow = WorkflowStatus.Unknown;
                statusHigh = WorkflowStatus.Unknown;
            }
        }
        return _specificFunctionFacade.obtainSpecificFunctionAgreements(partnerId, statusLow, statusHigh);
    }

    private List<SpecificFunctionAgreement> obtainAgreementsForEdit(int partnerId) {
        WorkflowStatus statusLow;
        WorkflowStatus statusHigh;
        if (partnerId == _sessionController.getAccountId()) {
            statusLow = WorkflowStatus.New;
            statusHigh = WorkflowStatus.ApprovalRequested;
        } else {
            boolean canRead = _accessManager.canRead(Feature.SPECIFIC_FUNCTION, partnerId);
            statusLow = canRead ? WorkflowStatus.New : WorkflowStatus.Unknown;
            statusHigh = canRead ? WorkflowStatus.ApprovalRequested : WorkflowStatus.Unknown;
        }
        return _specificFunctionFacade.obtainSpecificFunctionAgreements(partnerId, statusLow, statusHigh);
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
        Stream<SpecificFunctionAgreementTreeNode> stream = treeNode.getChildren().stream().
                map(n -> (SpecificFunctionAgreementTreeNode) n);
        Stream<SpecificFunctionAgreementTreeNode> sorted;
        int direction = treeNode.isDescending() ? -1 : 1;
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "id":
                sorted = stream.sorted((n1, n2)
                        -> direction * Integer.compare(n1.getSpecificFunctionAgreement().getId(),
                                n2.getSpecificFunctionAgreement().getId()));
                break;
            case "hospital":
                sorted = stream.sorted((n1, n2)
                        -> direction * _appTools.retrieveHospitalInfo(n1.getSpecificFunctionAgreement().getIk())
                                .compareTo(_appTools.retrieveHospitalInfo(n2.getSpecificFunctionAgreement().getIk())));
                break;
            case "date":
                sorted = stream.sorted((n1, n2)
                        -> direction * n1.getSpecificFunctionAgreement().getLastChanged()
                                .compareTo(n2.getSpecificFunctionAgreement().getLastChanged()));
                break;
            case "status":
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }

}
