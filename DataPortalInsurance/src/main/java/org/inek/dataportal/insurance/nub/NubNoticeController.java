/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.insurance.nub;

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
public class NubNoticeController extends AbstractFeatureController {

    public NubNoticeController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("lblInsuranceNub"), Pages.InsuranceNubNoticeSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartInsuranceNubNotice.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.NUB_NOTICE;
    }

}
