/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.insurance.psychstaff;

import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;

/**
 *
 * @author muellermi
 */
public class PsyStaffInsuranceController extends AbstractFeatureController {

    public PsyStaffInsuranceController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic("Psych-Personalnachweis: Überprüfung der Signatur", Pages.InsuranceCheckSignature.URL());
    }

    @Override
    public Feature getFeature() {
        return Feature.PSYCH_STAFF_INSURANCE;
    }

}
