package org.inek.dataportal.feature.i68;

import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.*;

/**
 *
 * @author muellermi
 */
public class I68Controller extends AbstractFeatureController {

    public I68Controller(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("lblI68"), Pages.I68Summary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartI68.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.I68;
    }
}
