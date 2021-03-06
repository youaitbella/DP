package org.inek.dataportal.admin.controller;

import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
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
        topics.addTopic(Utils.getMessage("tabAdminTaskInekRoles"), Pages.AdminTaskInekRoles.URL());
        topics.addTopic(Utils.getMessage("tabAdminTaskInfoText"), Pages.AdminTaskInfoText.URL());
        topics.addTopic(Utils.getMessage("tabAdminTaskMailTemplate"), Pages.AdminTaskMailTemplate.URL());
        topics.addTopic(Utils.getMessage("tabAdminTaskChangeNub"), Pages.AdminTaskChangeNub.URL());
        topics.addTopic(Utils.getMessage("tabAdminTaskSystemStatus"), Pages.AdminTaskSystemStatus.URL());
        topics.addTopic(Utils.getMessage("tabAdminTaskIkAdmin"), Pages.AdminTaskIkAdmin.URL());

    }

    @Override
    public Feature getFeature() {
        return Feature.ADMIN;
    }

}
