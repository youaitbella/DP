/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.insurance;

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
public class InsuranceController extends AbstractFeatureController {

    public InsuranceController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("lblInsuranceNub"), Pages.InsuranceSummary.URL());
        topics.addTopic(Utils.getMessage("lblInsuranceSpecificFuntions"), Pages.InsuranceSpecificFunctionSummary.URL());
        topics.addTopic("Psych-Personalnachweis: Überprüfung der Signatur", Pages.InsuranceCheckSignature.URL());
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
