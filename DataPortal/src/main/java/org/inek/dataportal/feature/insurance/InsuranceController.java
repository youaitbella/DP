/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.insurance;


import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Topics;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public class InsuranceController extends AbstractFeatureController {

    public InsuranceController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("lblInsuranceNub"), Pages.InsuranceSummary.URL());
        topics.addTopic(Utils.getMessage("lblInsuranceSpecificFuntions"), Pages.InsuranceSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartInsurance.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.INSURANCE;
    }

}
