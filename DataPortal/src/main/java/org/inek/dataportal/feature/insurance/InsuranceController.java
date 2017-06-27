/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.insurance;

import java.util.ArrayList;
import java.util.List;
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
        //if (getSessionController().isInekUser(Feature.INSURANCE)) { // todo: remove when public available
        List<String> allowedUsers = new ArrayList<>();
        allowedUsers.add("kerstin.bockhorst@gkv-spitzenverband.de");
        allowedUsers.add("j.vaillant@dkgev.de");
        allowedUsers.add("max.mustermann@mueller-bruehl.de");
        if (getSessionController().isInekUser(Feature.INSURANCE) 
                || allowedUsers.contains(getSessionController().getAccount().getEmail())) { 
            // todo: remove restrictions above once public available
            topics.addTopic(Utils.getMessage("lblInsuranceSpecificFuntions"), Pages.InsuranceSpecificFunctionSummary.URL());
        }
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
