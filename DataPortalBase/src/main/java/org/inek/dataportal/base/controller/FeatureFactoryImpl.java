package org.inek.dataportal.base.controller;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.base.feature.agency.AgencyController;
import org.inek.dataportal.base.feature.approval.ApprovalController;
import org.inek.dataportal.base.feature.cooperation.CooperationController;
import org.inek.dataportal.base.feature.documents.DocumentsController;
import org.inek.dataportal.base.feature.dropbox.DropBoxController;
import org.inek.dataportal.base.feature.ikadmin.controller.IkAdminController;
import org.inek.dataportal.base.feature.maintenance.UserMaintenanceController;
import org.inek.dataportal.base.feature.requestsystem.controller.RequestSystemController;
import org.inek.dataportal.common.controller.FeatureFactory;
import org.inek.dataportal.common.controller.IFeatureController;
import org.inek.dataportal.common.controller.SessionController;

import javax.enterprise.context.Dependent;
import java.io.Serializable;

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
            case APPROVAL:
                return new ApprovalController(sessionController);
            case REQUEST_SYSTEM:
                return new RequestSystemController(sessionController);
            default:
                throw new IllegalArgumentException("no such controller");
        }
    }
}
