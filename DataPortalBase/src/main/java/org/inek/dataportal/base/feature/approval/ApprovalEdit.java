package org.inek.dataportal.base.feature.approval;

import org.inek.dataportal.base.feature.approval.entities.ItemBlock;
import org.inek.dataportal.base.feature.approval.entities.ItemRecipient;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.utils.DateUtils;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
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
        for (ItemRecipient recipient : itemRecipients) {
            if (recipient.getFirstViewedDt().equals(DateUtils.MIN_DATE)) {
                recipient.setFirstViewedDt(new Date());
                approvalFacade.save(recipient);
            }
        }
    }

    public List<SelectItem> getActions() {
        return itemRecipients
                .stream()
                .map(r -> r.getItem())
                .map(i -> new SelectItem(i, i.getAction().getConfType().getName()))
                .collect(Collectors.toList());
    }

    public void approve(ItemBlock block) {
        block.setConfAccountId(sessionController.getAccountId());
        block.setConfDt(new Date());
        approvalFacade.save(block);
    }

    public boolean isNotApproved(ItemBlock block) {
        return block.getConfDt().equals(DateUtils.MIN_DATE);
    }
}
