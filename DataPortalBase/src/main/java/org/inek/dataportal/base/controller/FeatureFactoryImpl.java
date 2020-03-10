package org.inek.dataportal.base.controller;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.base.feature.agency.AgencyController;
import org.inek.dataportal.base.feature.approval.ApprovalController;
import org.inek.dataportal.base.feature.approval.ApprovalFacade;
import org.inek.dataportal.base.feature.cooperation.CooperationController;
import org.inek.dataportal.base.feature.documents.DocumentsController;
import org.inek.dataportal.base.feature.dropbox.DropBoxController;
import org.inek.dataportal.base.feature.ikadmin.controller.IkAdminController;
import org.inek.dataportal.base.feature.maintenance.UserMaintenanceController;
import org.inek.dataportal.base.feature.requestsystem.controller.RequestSystemController;
import org.inek.dataportal.common.controller.FeatureFactory;
import org.inek.dataportal.common.controller.IFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.AccountFeature;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class FeatureFactoryImpl implements FeatureFactory, Serializable {

    private ApprovalFacade approvalFacade;

    public FeatureFactoryImpl() {
    }

    @Inject
    public FeatureFactoryImpl(ApprovalFacade approvalFacade) {
        this.approvalFacade = approvalFacade;
    }

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

    @Override
    public List<Feature> obtainMissingRequiredFeatures(int accountId, List<AccountFeature> features) {
        List<Feature> missingFeatures = new ArrayList<>();
        if (missingApprovalIsRequired(accountId, features)) {
            missingFeatures.add(Feature.APPROVAL);
        }
        return missingFeatures;
    }

    private boolean missingApprovalIsRequired(int accountId, List<AccountFeature> features) {
        boolean featureExists = features.stream().anyMatch(f -> f.getFeature() == Feature.APPROVAL);
        if (featureExists) {
            return false;
        }
        return approvalFacade.hasData(accountId);
    }
}
