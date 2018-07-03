/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.controller;

import java.io.Serializable;
import javax.enterprise.context.Dependent;
import org.inek.dataportal.common.controller.FeatureFactory;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.controller.IFeatureController;
import org.inek.dataportal.api.enums.Feature;
import static org.inek.dataportal.api.enums.Feature.PEPP_PROPOSAL;
import org.inek.dataportal.psy.khcomparison.Controller.KhComparisonController;
import org.inek.dataportal.psy.modelintention.ModelIntentionController;
import org.inek.dataportal.psy.peppproposal.PeppProposalController;
import org.inek.dataportal.psy.psychstaff.controller.PsychStaffController;

/**
 *
 * @author muellermi
 */
@Dependent
public class FeatureFactoryImpl implements FeatureFactory, Serializable {

    @Override
    public IFeatureController createController(Feature feature, SessionController sessionController) {

        switch (feature) {
            case PEPP_PROPOSAL:
                return new PeppProposalController(sessionController);
            case MODEL_INTENTION:
                return new ModelIntentionController(sessionController);
            case PSYCH_STAFF:
                return new PsychStaffController(sessionController);
            case AEB:
                return new KhComparisonController(sessionController);
            default:
                throw new IllegalArgumentException("no such controller");
        }
    }
}
