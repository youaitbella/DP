package org.inek.dataportal.admin.controller;

import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;
import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author muellermi
 */
public class AdminController extends AbstractFeatureController {

    public AdminController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("nameADMIN"), Pages.AdminTaskSystemStatus.URL());
    }

    @Override
    public Feature getFeature() {
        return Feature.ADMIN;
    }

}
