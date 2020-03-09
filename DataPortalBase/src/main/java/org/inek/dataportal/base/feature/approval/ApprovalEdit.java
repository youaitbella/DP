package org.inek.dataportal.base.feature.approval;

import org.inek.dataportal.base.feature.approval.entities.ItemBlock;
import org.inek.dataportal.base.feature.approval.entities.ItemRecipient;
import org.inek.dataportal.common.controller.SessionController;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ApprovalEdit implements Serializable {
    private SessionController sessionController;
    private ApprovalFacade approvalFacade;

    public ApprovalEdit() {
    }

    @Inject
    public ApprovalEdit(SessionController sessionController, ApprovalFacade approvalFacade) {
        this.sessionController = sessionController;
        this.approvalFacade = approvalFacade;
    }

    private List<ItemRecipient> itemRecipients;

    @PostConstruct
    private void init() {
        itemRecipients = approvalFacade.itemsForAccount(sessionController.getAccountId());
    }

    public List<SelectItem> getActions() {
        return itemRecipients
                .stream()
                .map(r -> r.getItem())
                .map(i -> new SelectItem(i, i.getAction().getConfType().getName()))
                .collect(Collectors.toList());
    }

    public List<ItemBlock> getItemBlocks() {
        List<ItemBlock> itemBlocks = new ArrayList<>();
        itemRecipients.stream().map(r -> r.getItem()).forEach(i -> i.getBlocks().addAll(itemBlocks));
        return itemBlocks;
    }

    public void approve(ItemBlock block) {
        block.setConfAccountId(sessionController.getAccountId());
        block.setConfDt(new Date());
        approvalFacade.save(block);
    }
}
