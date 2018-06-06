package org.inek.dataportal.psy.psychstaff.controller;

import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.helper.FeatureMessageHandler;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;

/**
 *
 * @author muellermi
 */
public class PsychStaffController extends AbstractFeatureController {

    public PsychStaffController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(FeatureMessageHandler.getMessage("namePSYCH_STAFF"), Pages.PsychStaffSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartPsychStaff.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.PSYCH_STAFF;
    }

}
