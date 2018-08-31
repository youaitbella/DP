package org.inek.dataportal.psy.khcomparison.Controller;

import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;

/**
 *
 * @author lautenti
 */
public class KhComparisonController extends AbstractFeatureController {

    public KhComparisonController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic("Krankenhausvergleich Datenerfassung", Pages.KhComparisonSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartKhComparison.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.HC_HOSPITAL;
    }

}
