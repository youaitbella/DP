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
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.scope.FeatureScoped;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "DocumentUpload")
public class DocumentsEditController extends AbstractEditController {

    private static final Logger LOGGER = Logger.getLogger("DocumentsMangager");

    @Inject private SessionController _sessionController;

    @Override
    protected void addTopics() {
        if (_sessionController.isInekUser(Feature.DOCUMENTS)) {
            addTopic(DocumentsTabs.tabDocuments.name(), Pages.ListDocumentsInek.URL());
            addTopic(DocumentsTabs.tabApproval.name(), Pages.DocumentsApproval.URL());
            if (_sessionController.isInternalClient()) {
                addTopic(DocumentsTabs.tabUploadFromInek.name(), Pages.DocumentUploadFromInek.URL());
                addTopic(DocumentsTabs.tabViewUploaded.name(), Pages.DocumentsViewer.URL());
            }
            if (_sessionController.isInekUser(Feature.ADMIN)) {
                addTopic(DocumentsTabs.tabUploadToInek.name(), Pages.DocumentUploadToInek.URL());
            }
        } else {
            addTopic(DocumentsTabs.tabDocuments.name(), Pages.ListDocuments.URL());
            //addTopic(DocumentsTabs.tabUploadToInek.name(), Pages.DocumentUploadToInek.URL());
        }
    }

    private enum DocumentsTabs {
        tabUploadToInek,
        tabUploadFromInek,
        tabApproval,
        tabViewUploaded,
        tabDocuments;
    }

}
