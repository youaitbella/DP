/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.controller;

import org.inek.dataportal.common.controller.IFeatureController;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.feature.admin.controller.AdminController;

/**
 *
 * @author muellermi
 */
public class FeatureFactory {

    public static IFeatureController createController(Feature feature, SessionController sessionController) {

        switch (feature) {
            case ADMIN:
                return new AdminController(sessionController);
            default:
                throw new IllegalArgumentException("no such controller");
        }
    }
}
