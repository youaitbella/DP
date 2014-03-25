package org.inek.dataportal.feature.peppproposal;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.PeppProposal;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.PeppProposalFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.helper.structures.Triple;



@Named
@RequestScoped
public class PeppProposalList {
    @Inject PeppProposalFacade _peppProposalFacade;
    @Inject SessionController _sessionController;
   
    public List<Triple> getPeppProposals() {
        return _peppProposalFacade.getPeppProposalInfos(_sessionController.getAccountId(), DataSet.OPEN);
    }

    public List<Triple> getSealedPeppProposals() {
        return _peppProposalFacade.getPeppProposalInfos(_sessionController.getAccountId(), DataSet.SEALED);
    }
    
    public String newPeppProposal() {
        return Pages.PeppProposalEdit.URL();
    }
    
    public String editPeppProposal(int proposalId) {
        Utils.getFlash().put("ppId", proposalId);
        return Pages.PeppProposalEdit.URL();
    }
    
    public String requestDeletePeppProposal(int proposalId) {
        Utils.getFlash().put("ppId", proposalId);
        PeppProposal proposal =_peppProposalFacade.find(proposalId);
        if(_sessionController.isMyAccount(proposal.getAccountId())) {
            String script = "if (confirm ('" + proposal.getName() + "\\r\\n" + Utils.getMessage("msgConfirmDelete") + "')) {document.getElementById('form:deletePeppProposal').click();}";
            _sessionController.setScript(script);
        }
        return null;
    }

    public String deletePeppProposal(int proposalId) {
        PeppProposal proposal = _peppProposalFacade.find(proposalId);
        if(_sessionController.isMyAccount(proposal.getAccountId())) {
            _peppProposalFacade.remove(proposal);
        }
        return null;
    }
    
    public String printPeppProposal(int proposalId) {
        PeppProposal peppProposal = _peppProposalFacade.find(proposalId);
        String headLine = Utils.getMessage("namePEPP_PROPOSAL") + " " + peppProposal.getExternalId();
        Utils.getFlash().put("headLine", headLine);
        Utils.getFlash().put("targetPage", Pages.PeppProposalSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(peppProposal));
        return Pages.PrintView.URL();
    }
    
}
