package org.inek.dataportal.base.feature.ikadmin.controller;

import org.inek.dataportal.api.helper.FeatureMessageHandler;
import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;

/**
 *
 * @author muellermi
 */
public class IkAdminController extends AbstractFeatureController {

    public IkAdminController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(FeatureMessageHandler.getMessage("nameIK_ADMIN"), Pages.IkAdminSummary.URL());
    }

    @Override
    public Feature getFeature() {
        return Feature.IK_ADMIN;
    }

    @Override
    public String getMainPart() {
        return Pages.PartIkAdmin.URL();
    }

}
