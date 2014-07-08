/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.cert;

import org.inek.dataportal.feature.modelintention.*;
import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.modelintention.ModelIntention;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Topics;

/**
 *
 * @author muellermi
 */
public class CertController extends AbstractFeatureController {

    public CertController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(getMsg().getString("lblZerti"), Pages.CertSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartCert.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.CERT;
    }
}
