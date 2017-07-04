package org.inek.dataportal.feature.admin.controller;

import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Topics;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public class AdminController extends AbstractFeatureController {

    public AdminController(SessionController sessionController) {
        super(sessionController);
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    // place getter and setters here
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    // place this methods here
    // </editor-fold>
    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("nameADMIN"), Pages.AdminTaskSystemStatus.URL());
    }

    @Override
    public Feature getFeature() {
        return Feature.ADMIN;
    }

}
