/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.controller;

import org.inek.dataportal.entities.Account;
import org.inek.dataportal.enums.Feature;
import static org.inek.dataportal.enums.Feature.DROPBOX;
import static org.inek.dataportal.enums.Feature.NUB;
import static org.inek.dataportal.enums.Feature.PEPP_PROPOSAL;
import static org.inek.dataportal.enums.Feature.REQUEST_SYSTEM;
import static org.inek.dataportal.enums.Feature.USER_MAINTENANCE;
import org.inek.dataportal.feature.cooperation.CooperationController;
import org.inek.dataportal.feature.documents.DocumentsController;
import org.inek.dataportal.feature.dropbox.DropBoxController;
import org.inek.dataportal.feature.maintenance.UserMaintenanceController;
import org.inek.dataportal.feature.modelintention.ModelIntentionController;
import org.inek.dataportal.feature.nub.NubController;
import org.inek.dataportal.feature.peppproposal.PeppProposalController;
import org.inek.dataportal.feature.requestsystem.RequestController;

/**
 *
 * @author muellermi
 */
public class FeatureFactory {

    public static IFeatureController createController(Feature feature, Account account, SessionController sessionController) {

        switch (feature) {
            case USER_MAINTENANCE:
                return new UserMaintenanceController(sessionController);
            case NUB:
                return new NubController(sessionController);
            case REQUEST_SYSTEM:
                return new RequestController(sessionController);
            case PEPP_PROPOSAL:
                return new PeppProposalController(sessionController);
            case DROPBOX:
                return new DropBoxController(sessionController);
            case COOPERATION:
                return new CooperationController(sessionController);
            case MODEL_INTENTION:
                return new ModelIntentionController(sessionController);
            case DOCUMENTS:
                return new DocumentsController(sessionController);
        }
        throw new IllegalArgumentException("no such controller");
    }
    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    // place getter and setters here
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    // place this methods here
    // </editor-fold>
}
