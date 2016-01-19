/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.certification;

import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
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
        if (getSessionController().isInekUser(Feature.CERT)) {
            topics.addTopic(getMsg().getString("lblCert"), Pages.CertSystemManagement.URL());
        } else {
            topics.addTopic(getMsg().getString("lblCert"), Pages.CertCertification.URL());
        }
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
