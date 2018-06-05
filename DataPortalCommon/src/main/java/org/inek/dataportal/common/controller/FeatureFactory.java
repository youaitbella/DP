package org.inek.dataportal.common.controller;

import org.inek.dataportal.api.enums.Feature;

/**
 *
 * @author muellermi
 */
public interface FeatureFactory {

    IFeatureController createController(Feature feature, SessionController sessionController);
    
}
