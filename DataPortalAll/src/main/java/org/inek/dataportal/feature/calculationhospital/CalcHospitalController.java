package org.inek.dataportal.feature.calculationhospital;

import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.helper.Topics;
import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author muellermi
 */
public class CalcHospitalController extends AbstractFeatureController {


    public CalcHospitalController(SessionController sessionController) {
        super(sessionController);
    }

    // <editor-fold defaultstate="collapsed" desc="Override abstract methods">
    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("lblCalculationParticipance"), Pages.CalculationHospitalSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartCalculationHospital.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.CALCULATION_HOSPITAL;
    }
    // </editor-fold>

}
