package org.inek.dataportal.psy.peppproposal;

import java.util.ArrayList;
import java.util.List;
import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.psy.peppproposal.entities.PeppProposalDocument;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;
import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author muellermi
 */
public class PeppProposalController extends AbstractFeatureController {

    private List<PeppProposalDocument> _documents;

    public PeppProposalController(SessionController sessionController) {
        super(sessionController);
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public List<PeppProposalDocument> getDocuments() {
        if (_documents == null) {
            _documents = new ArrayList<>();
        }
        return _documents;
    }

    public void setDocuments(List<PeppProposalDocument> documents) {
        _documents = documents;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    // place this methods here
    // </editor-fold>
    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("lblPeppProposal"), Pages.PeppProposalSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartPeppProposal.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.PEPP_PROPOSAL;
    }

}
