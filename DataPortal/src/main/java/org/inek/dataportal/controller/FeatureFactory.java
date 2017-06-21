/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.controller;

import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.Feature;
import static org.inek.dataportal.enums.Feature.DROPBOX;
import static org.inek.dataportal.enums.Feature.NUB;
import static org.inek.dataportal.enums.Feature.PEPP_PROPOSAL;
import static org.inek.dataportal.enums.Feature.USER_MAINTENANCE;
import org.inek.dataportal.feature.additionalcost.AdditionalCostController;
import org.inek.dataportal.feature.admin.AdminController;
import org.inek.dataportal.feature.agency.AgencyController;
import org.inek.dataportal.feature.calculationhospital.CalcHospitalController;
import org.inek.dataportal.feature.cooperation.CooperationController;
import org.inek.dataportal.feature.documents.DocumentsController;
import org.inek.dataportal.feature.dropbox.DropBoxController;
import org.inek.dataportal.feature.maintenance.UserMaintenanceController;
import org.inek.dataportal.feature.modelintention.ModelIntentionController;
import org.inek.dataportal.feature.nub.NubController;
import org.inek.dataportal.feature.peppproposal.PeppProposalController;
import org.inek.dataportal.feature.certification.CertController;
import org.inek.dataportal.feature.drgproposal.DrgProposalController;
import org.inek.dataportal.feature.insurance.InsuranceController;
import org.inek.dataportal.feature.specificfunction.SpecificFunctionController;

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
            case PEPP_PROPOSAL:
                return new PeppProposalController(sessionController);
            case DRG_PROPOSAL:
                return new DrgProposalController(sessionController);
            case DROPBOX:
                return new DropBoxController(sessionController);
            case COOPERATION:
                return new CooperationController(sessionController);
            case MODEL_INTENTION:
                return new ModelIntentionController(sessionController);
            case DOCUMENTS:
                return new DocumentsController(sessionController);
            case ADMIN:
                return new AdminController(sessionController);
            case CERT:
                return new CertController(sessionController);
            case AGENCY:
                return new AgencyController(sessionController);
            case INSURANCE:
                return new InsuranceController(sessionController);
            case CALCULATION_HOSPITAL:
                return new CalcHospitalController(sessionController);
            case SPECIFIC_FUNCTION:
                return new SpecificFunctionController(sessionController);
            case ADDITIONAL_COST:
                return new AdditionalCostController(sessionController);
            default:
                throw new IllegalArgumentException("no such controller");
        }
    }
    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    // place getter and setters here
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    // place this methods here
    // </editor-fold>
}
