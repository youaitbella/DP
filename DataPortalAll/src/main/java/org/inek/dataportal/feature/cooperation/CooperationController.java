/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.cooperation;

import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;
import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author muellermi
 */
public class CooperationController extends AbstractFeatureController {

    public CooperationController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("lblCooperation"), Pages.CooperationSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartCooperation.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.COOPERATION;
    }

}
