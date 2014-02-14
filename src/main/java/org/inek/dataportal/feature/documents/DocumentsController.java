/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.feature.documents;

import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Topics;

/**
 *
 * @author vohldo
 */
public class DocumentsController extends AbstractFeatureController {
    
    public DocumentsController(SessionController sessionController) {
        super(sessionController);
    }
    
    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(getMsg().getString("lblDocuments"), Pages.DocumentsSummary.URL());
    }

    @Override
    public String getMainPart() {
        return "";
    }

    @Override
    public Feature getFeature() {
        return Feature.DOCUMENTS;
    }
}
