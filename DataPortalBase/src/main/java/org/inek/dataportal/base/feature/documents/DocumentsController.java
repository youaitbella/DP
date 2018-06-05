/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.base.feature.documents;

import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;
import org.inek.dataportal.common.helper.Utils;

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
        if (getSessionController().isInekUser(Feature.DOCUMENTS)) {
            topics.addTopic(Utils.getMessage("lblDocuments"), Pages.ListDocumentsInek.URL());
        } else {
            topics.addTopic(Utils.getMessage("lblDocuments"), Pages.ListDocuments.URL());
        }
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
