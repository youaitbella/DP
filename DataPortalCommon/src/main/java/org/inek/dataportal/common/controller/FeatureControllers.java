package org.inek.dataportal.common.controller;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.inek.dataportal.common.enums.Feature;

/**
 *
 * @author muellermi
 */
public class FeatureControllers {

    @Inject private FeatureFactory _featureFactory;
    private final List<IFeatureController> _featureControllers = new ArrayList<>();

    void clear() {
        _featureControllers.clear();
    }

    void add(IFeatureController controller) {
        _featureControllers.add(controller);
    }

    void add(Feature feature, SessionController sessionController) {
        add (_featureFactory.createController(feature, sessionController));
    }

    Iterable<IFeatureController> getFeatureControllers() {
        return _featureControllers;
    }

    int getFeatureCount() {
        return _featureControllers.size();
    }
    
    

}
