package org.inek.dataportal.drg.specificfunction.controller;

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
