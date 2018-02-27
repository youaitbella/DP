package org.inek.dataportal.feature.ikadmin.controller;

import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.helper.Topics;
import org.inek.dataportal.common.helper.Utils;

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
        topics.addTopic(Utils.getMessage("nameIK_ADMIN"), Pages.IkAdminSummary.URL());
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
