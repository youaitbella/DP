package org.inek.dataportal.drg.valuationratio;

import org.inek.dataportal.common.helper.Topics;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;

/**
 *
 * @author muellermi
 */
public class ValuationRatioController extends AbstractFeatureController {

    public ValuationRatioController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("nameVALUATION_RATIO"), Pages.ValuationRatioSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartValuationRatio.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.VALUATION_RATIO;
    }
}
