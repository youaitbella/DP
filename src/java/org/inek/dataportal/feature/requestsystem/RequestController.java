package org.inek.dataportal.feature.requestsystem;

import java.util.ArrayList;
import java.util.List;
import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.RequestDocument;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Topics;

/**
 *
 * @author muellermi
 */
public class RequestController extends AbstractFeatureController {

    private static final long serialVersionUID = 1L;
    private List<RequestDocument> _documents;

    public RequestController(SessionController sessionController) {
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
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    // place this methods here
    // </editor-fold>
    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(getMsg().getString("lblRequest"), Pages.RequestSummary.URL());
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
