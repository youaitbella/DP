package org.inek.dataportal.feature.requestsystem.controller;

import java.util.ArrayList;
import java.util.List;
import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.feature.requestsystem.entity.RequestDocument;
import org.inek.dataportal.common.helper.Topics;
import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author muellermi
 */
public class RequestSystemController extends AbstractFeatureController {

    private List<RequestDocument> _documents;

    public RequestSystemController(SessionController sessionController) {
        super(sessionController);
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public List<RequestDocument> getDocuments() {
        if (_documents == null) {
            _documents = new ArrayList<>();
        }
        return _documents;
    }

    public void setDocuments(List<RequestDocument> documents) {
        _documents = documents;
    }
    // </editor-fold>
    
    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("nameREQUEST_SYSTEM"), Pages.RequestSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartRequest.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.REQUEST_SYSTEM;
    }

}
