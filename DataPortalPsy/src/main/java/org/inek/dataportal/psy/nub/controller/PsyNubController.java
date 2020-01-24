package org.inek.dataportal.psy.nub.controller;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;

/**
 *
 * @author lautenti
 */
public class PsyNubController extends AbstractFeatureController {

    public PsyNubController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic("Neue Untersuchungs- und Behandlungsmethoden", Pages.NubPsySummary.URL());
    }


    @Override
    public Feature getFeature() {
        return Feature.NUB_PSY;
    }

}
