/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.insurance.controller;

import java.io.Serializable;
import javax.enterprise.context.Dependent;
import org.inek.dataportal.common.controller.FeatureFactory;
import org.inek.dataportal.common.controller.IFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.insurance.nub.NubNoticeController;
import org.inek.dataportal.insurance.khcomparison.controller.HospitalComparisonInsuranceController;
import org.inek.dataportal.insurance.psychstaff.PsyStaffInsuranceController;
import org.inek.dataportal.insurance.specificfunction.SpfInsuranceController;

/**
 *
 * @author muellermi
 */
@Dependent
public class FeatureFactoryImpl implements FeatureFactory, Serializable{

    @Override
    public IFeatureController createController(Feature feature, SessionController sessionController) {

        switch (feature) {
            case NUB_NOTICE:
                return new NubNoticeController(sessionController);
            case SPF_INSURANCE:
                return new SpfInsuranceController(sessionController);
            case PSYCH_STAFF_INSURANCE:
                return new PsyStaffInsuranceController(sessionController);
            case HC_INSURANCE:
                return new HospitalComparisonInsuranceController(sessionController);
            default:
                throw new IllegalArgumentException("no such controller");
        }
    }
}
