package org.inek.dataportal.feature.psychstaff.controller;

import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;
import org.inek.dataportal.common.helper.Utils;

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
        topics.addTopic(Utils.getMessage("namePSYCH_STAFF"), Pages.PsychStaffSummary.URL());
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
