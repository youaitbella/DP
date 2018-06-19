package org.inek.dataportal.psy.Aeb.Controller;

import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;

/**
 *
 * @author muellermi
 */
public class AebController extends AbstractFeatureController {

    public AebController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic("Aufstellung der Entgelte und Budgetermittlung", Pages.AebSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartAeb.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.AEB;
    }

}
