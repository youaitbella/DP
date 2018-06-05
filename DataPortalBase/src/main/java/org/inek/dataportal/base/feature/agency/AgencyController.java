/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.base.feature.agency;

import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Topics;

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
