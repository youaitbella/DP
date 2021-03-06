package org.inek.dataportal.care.controller;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;

public class CareController extends AbstractFeatureController {

    public CareController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic("Mitteilung gem. § 5  Abs. 3 PpUGV", Pages.CareDeptSummary.URL());
        topics.addTopic("Umbenennung oder strukturelle Veränderungen (§ 5 Abs. 4 PpUGV)", Pages.CareStructuralChangesSummary.URL());
        topics.addTopic("Nachweisvereinbarung", Pages.CareProofSummary.URL());
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
