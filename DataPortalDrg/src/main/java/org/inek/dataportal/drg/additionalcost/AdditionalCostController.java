/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.drg.additionalcost;

import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.helper.FeatureMessageHandler;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;

/**
 *
 * @author muellermi
 */
public class AdditionalCostController extends AbstractFeatureController {

    public AdditionalCostController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(FeatureMessageHandler.getMessage("nameADDITIONAL_COST"), Pages.AdditionalCostSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartAdditionalCost.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.ADDITIONAL_COST;
    }
}
