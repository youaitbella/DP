package org.inek.dataportal.care.controller;

import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;
import org.inek.dataportal.common.helper.Utils;

public class CareController extends AbstractFeatureController {

    public CareController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("lblCare"), Pages.CareDeptSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartCare.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.CARE;
    }

}
