/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.cert.controller;

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
public class CertController extends AbstractFeatureController {

    public CertController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        if (getSessionController().isInekUser(Feature.CERT)) {
            topics.addTopic(Utils.getMessage("lblCert"), Pages.CertSystemManagement.URL());
        } else {
            topics.addTopic(Utils.getMessage("lblCert"), Pages.CertCertification.URL());
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
