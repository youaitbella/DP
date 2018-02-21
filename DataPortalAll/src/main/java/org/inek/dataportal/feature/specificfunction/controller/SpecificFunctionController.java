package org.inek.dataportal.feature.specificfunction.controller;

import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Topics;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public class SpecificFunctionController extends AbstractFeatureController {

    public SpecificFunctionController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("lblSpecificFunction"), Pages.SpecificFunctionSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartSpecificFunction.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.SPECIFIC_FUNCTION;
    }

}