package org.inek.dataportal.insurance.care.controller;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;

public class CareSignatureCheckController extends AbstractFeatureController {

    public CareSignatureCheckController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic("PPUG Nachweisvereinbarung Signaturprüfung", Pages.InsurancePpugCheckSignature.URL());
    }

    @Override
    public Feature getFeature() {
        return Feature.CARE_INSURANCE_SIGNATURE_CHECK;
    }

}
