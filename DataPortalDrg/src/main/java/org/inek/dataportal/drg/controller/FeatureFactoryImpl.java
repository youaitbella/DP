/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.drg.controller;

import java.io.Serializable;
import javax.enterprise.context.Dependent;
import org.inek.dataportal.common.controller.FeatureFactory;
import org.inek.dataportal.common.controller.IFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.drg.additionalcost.AdditionalCostController;
import org.inek.dataportal.drg.drgproposal.DrgProposalController;
import org.inek.dataportal.drg.nub.NubController;
import org.inek.dataportal.drg.valuationratio.ValuationRatioController;

/**
 *
 * @author muellermi
 */
@Dependent
public class FeatureFactoryImpl implements FeatureFactory, Serializable{

    @Override
    public IFeatureController createController(Feature feature, SessionController sessionController) {

        switch (feature) {
            case DRG_PROPOSAL:
                return new DrgProposalController(sessionController);
            case NUB:
                return new NubController(sessionController);
            case VALUATION_RATIO:
                return new ValuationRatioController(sessionController);
            case ADDITIONAL_COST:
                return new AdditionalCostController(sessionController);
            default:
                throw new IllegalArgumentException("no such controller");
        }
    }
}
