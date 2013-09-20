package org.inek.dataportal.feature.peppproposal;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.PeppProposalDocument;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Topics;

/**
 *
 * @author muellermi
 */
@SessionScoped
public class PeppProposalController extends AbstractFeatureController {

    private static final long serialVersionUID = 1L;
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
        topics.addTopic(getMsg().getString("lblPeppProposal"), Pages.PeppProposalSummary.URL());
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
