/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.drgproposal;

import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.DrgProposal;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Topics;

/**
 *
 * @author muellermi
 */
public class DrgProposalController extends AbstractFeatureController {

    public DrgProposalController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(getMsg().getString("lblDrgProposal"), Pages.DrgProposalSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartDrgProposal.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.DRG_PROPOSAL;
    }

    /**
     * creates a new NubProposal pre-populated with master data
     *
     * @return
     */
    public DrgProposal createDrgProposal() {
        Account account = getSessionController().getAccount();
        DrgProposal proposal = new DrgProposal();
        proposal.setAccountId(account.getAccountId());
//        proposal.setIk(account.getIK());
//        proposal.setIkName(account.getCompany());
       // populateMasterData(proposal, account);
        return proposal;
    }

  

}
