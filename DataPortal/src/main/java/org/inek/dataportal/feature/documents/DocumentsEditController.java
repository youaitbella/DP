/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.documents;

import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.scope.FeatureScoped;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "Documents")
public class DocumentsEditController extends AbstractEditController {
    private static final Logger _logger = Logger.getLogger("DocumentsMangager");

    @Inject private SessionController _sessionController;

    @Override
    protected void addTopics() {
        if (_sessionController.isInekUser(Feature.DOCUMENTS)) {
            addTopic(DocumentsTabs.tabDocuments.name(), Pages.DocumentsList.URL());
            addTopic(DocumentsTabs.tabUpload.name(), Pages.DocumentsUpload.URL());
        }
    }

    private enum DocumentsTabs {
        tabUpload,
        tabDocuments;
    }
    
}
