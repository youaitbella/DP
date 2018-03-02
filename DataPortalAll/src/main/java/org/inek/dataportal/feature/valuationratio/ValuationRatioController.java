package org.inek.dataportal.feature.valuationratio;

import org.inek.dataportal.common.helper.Topics;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.helper.*;

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
