package org.inek.dataportal.feature.calculationbasics;

import javax.enterprise.context.SessionScoped;
import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Topics;

/**
 *
 * @author muellermi
 */
@SessionScoped
public class CalculationBasicsController extends AbstractFeatureController {


    public CalculationBasicsController(SessionController sessionController) {
        super(sessionController);
    }

    // <editor-fold defaultstate="collapsed" desc="Override abstract methods">
    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(getMsg().getString("lblCalculationBasics"), Pages.CalculationBasicsSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartCalculationBasics.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.CALCULATION_BASICS;
    }
    // </editor-fold>

}
