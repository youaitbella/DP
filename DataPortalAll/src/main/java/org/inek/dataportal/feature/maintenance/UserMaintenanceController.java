package org.inek.dataportal.feature.maintenance;

import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;
import org.inek.dataportal.common.helper.Utils;

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
        topics.addTopic(Utils.getMessage("nameUSER_MAINTENANCE"), Pages.UserMaintenanceMasterData.URL());
    }

    @Override
    public Feature getFeature() {
        return Feature.USER_MAINTENANCE;
    }

}
