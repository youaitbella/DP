package org.inek.dataportal.insurance.khcomparison.controller;

import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;

public class HospitalComparisonInsuranceController extends AbstractFeatureController {

    public HospitalComparisonInsuranceController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic("Krankenhausvergleich Datenerfassung", Pages.InsuranceKhComparisonSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartHospitalComparisonInsurance.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.HC_INSURANCE;
    }

}
