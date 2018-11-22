/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.documentScanner;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.FeatureFactory;
import org.inek.dataportal.common.controller.IFeatureController;
import org.inek.dataportal.common.controller.SessionController;

import javax.enterprise.context.Dependent;
import java.io.Serializable;

/**
 * @author muellermi
 */
@Dependent
public class FeatureFactoryImpl implements FeatureFactory, Serializable {

    @Override
    public IFeatureController createController(Feature feature, SessionController sessionController) {

        switch (feature) {
            default:
                throw new IllegalArgumentException("no such controller");
        }
    }
}
