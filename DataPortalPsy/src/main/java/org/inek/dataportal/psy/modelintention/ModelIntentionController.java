/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.psy.modelintention;

import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.psy.modelintention.entities.ModelIntention;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;
import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author muellermi
 */
public class ModelIntentionController extends AbstractFeatureController {

    public ModelIntentionController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("lblModelIntention"), Pages.ModelIntentionSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartModelIntention.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.MODEL_INTENTION;
    }
    
    /**
     * creates a new ModelIntention pre-populated with master data
     *
     * @return
     */
    public ModelIntention createModelIntention() {
        ModelIntention modelIntention = new ModelIntention();
        return modelIntention;
    }
}
