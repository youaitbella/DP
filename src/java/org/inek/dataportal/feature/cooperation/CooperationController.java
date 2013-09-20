/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.cooperation;

import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Topics;

/**
 *
 * @author muellermi
 */
public class CooperationController extends AbstractFeatureController {

    private static final long serialVersionUID = 1L;

    public CooperationController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(getMsg().getString("lblCooperation"), Pages.CooperationSummary.URL());
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
