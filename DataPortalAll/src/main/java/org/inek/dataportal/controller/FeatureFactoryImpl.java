/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.controller;

import java.io.Serializable;
import javax.enterprise.context.Dependent;
import org.inek.dataportal.common.controller.FeatureFactory;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.controller.IFeatureController;
import org.inek.dataportal.common.enums.Feature;
import static org.inek.dataportal.common.enums.Feature.DROPBOX;
import static org.inek.dataportal.common.enums.Feature.USER_MAINTENANCE;
import org.inek.dataportal.feature.additionalcost.AdditionalCostController;
import org.inek.dataportal.feature.agency.AgencyController;
import org.inek.dataportal.feature.calculationhospital.CalcHospitalController;
import org.inek.dataportal.feature.cooperation.CooperationController;
import org.inek.dataportal.feature.documents.DocumentsController;
import org.inek.dataportal.feature.dropbox.DropBoxController;
import org.inek.dataportal.feature.maintenance.UserMaintenanceController;
import org.inek.dataportal.feature.ikadmin.controller.IkAdminController;
import org.inek.dataportal.feature.requestsystem.controller.RequestSystemController;
import org.inek.dataportal.feature.specificfunction.controller.SpecificFunctionController;

/**
 *
 * @author muellermi
 */
@Dependent
public class FeatureFactoryImpl implements FeatureFactory, Serializable {

    @Override
    public IFeatureController createController(Feature feature, SessionController sessionController) {

        switch (feature) {
            case IK_ADMIN:
                return new IkAdminController(sessionController);
            case USER_MAINTENANCE:
                return new UserMaintenanceController(sessionController);
            case DROPBOX:
                return new DropBoxController(sessionController);
            case COOPERATION:
                return new CooperationController(sessionController);
            case DOCUMENTS:
                return new DocumentsController(sessionController);
            case AGENCY:
                return new AgencyController(sessionController);
            case CALCULATION_HOSPITAL:
                return new CalcHospitalController(sessionController);
            case SPECIFIC_FUNCTION:
                return new SpecificFunctionController(sessionController);
            case ADDITIONAL_COST:
                return new AdditionalCostController(sessionController);
            case REQUEST_SYSTEM:
                return new RequestSystemController(sessionController);
            default:
                throw new IllegalArgumentException("no such controller");
        }
    }
}
