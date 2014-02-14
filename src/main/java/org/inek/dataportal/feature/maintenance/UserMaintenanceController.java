package org.inek.dataportal.feature.maintenance;

import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Topics;

/**
 *
 * @author muellermi
 */
public class UserMaintenanceController extends AbstractFeatureController{
    public UserMaintenanceController(SessionController sessionController) {
        super(sessionController);
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    // place getter and setters here
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    // place this methods here
    // </editor-fold>

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(getMsg().getString("nameUSER_MAINTENANCE"), Pages.UserMaintenanceMasterData.URL());
    }

    @Override
    public Feature getFeature() {
        return Feature.USER_MAINTENANCE;
    }


}
