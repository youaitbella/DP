/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.insurance.specificfunction;

import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;
import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author muellermi
 */
public class SpfInsuranceController extends AbstractFeatureController {

    public SpfInsuranceController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("lblInsuranceSpecificFuntions"), Pages.InsuranceSpecificFunctionSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartInsuranceSpecificFunction.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.SPF_INSURANCE;
    }

}
