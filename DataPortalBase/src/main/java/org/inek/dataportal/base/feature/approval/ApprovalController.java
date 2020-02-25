package org.inek.dataportal.base.feature.approval;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;

public class ApprovalController extends AbstractFeatureController {

    public ApprovalController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic("Rückmeldung", Pages.Approval.RedirectURL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartApproval.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.APPROVAL;
    }

}
