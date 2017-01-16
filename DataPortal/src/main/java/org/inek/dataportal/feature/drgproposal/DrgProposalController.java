/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.drgproposal;

import java.util.ArrayList;
import java.util.List;
import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.drg.DrgProposalDocument;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Topics;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public class DrgProposalController extends AbstractFeatureController {
    
    private List<DrgProposalDocument> _documents;

    public DrgProposalController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("lblDrgProposal"), Pages.DrgProposalSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartDrgProposal.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.DRG_PROPOSAL;
    }

    public List<DrgProposalDocument> getDocuments() {       
        if (_documents == null) {
            _documents = new ArrayList<>();
        }
        return _documents;
    }

    public void setDocuments(List<DrgProposalDocument> documents) {
        _documents = documents;
    }

  

}
