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
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.insurance.InsuranceController;

/**
 *
 * @author muellermi
 */
@Dependent
public class FeatureFactoryImpl implements FeatureFactory, Serializable{

    @Override
    public IFeatureController createController(Feature feature, SessionController sessionController) {

        switch (feature) {
            case INSURANCE:
                return new InsuranceController(sessionController);
            default:
                throw new IllegalArgumentException("no such controller");
        }
    }
}