/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.agency;

import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Topics;

/**
 *
 * @author muellermi
 */
public class AgencyController extends AbstractFeatureController {

    public AgencyController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
//        if (getSessionController().isInekUser(Feature.CERT)) {
//            topics.addTopic(Utils.getMessage("lblCert"), Pages.AgencyManagement.URL());
//        } else {
//            topics.addTopic(Utils.getMessage("lblCert"), Pages.CertCertification.URL());
//        }
    }

    @Override
    public String getMainPart() {
        return Pages.PartAgency.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.AGENCY;
    }

}