package org.inek.dataportal.base.feature.maintenance;

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
public class UserMaintenanceController extends AbstractFeatureController {

    public UserMaintenanceController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(FeatureMessageHandler.getMessage("nameUSER_MAINTENANCE"), Pages.UserMaintenanceMasterData.URL());
    }

    @Override
    public Feature getFeature() {
        return Feature.USER_MAINTENANCE;
    }

}
