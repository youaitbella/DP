package org.inek.dataportal.common.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.PortalType;

/**
 *
 * @author muellermi
 */
@Dependent
public class FeatureControllers {

    @Inject private FeatureFactory _featureFactory;
    private final Map<String, IFeatureController> _featureControllers = new Hashtable<>();
    private final Set<PortalType> portalTypes = new HashSet<>();

    public void initFeatures(Account account, PortalType portaltype) {

    }

    public void clear() {
        _featureControllers.clear();
        portalTypes.clear();
    }

    public void add(Feature feature, SessionController sessionController) {
        IFeatureController controller = _featureFactory.createController(feature, sessionController);
        _featureControllers.put(feature.name(), controller);
    }

    public Iterable<IFeatureController> getFeatureControllers() {
        return _featureControllers.values();
    }

    public IFeatureController getFeatureController(Feature feature) {
        IFeatureController controller = _featureControllers.get(feature.name());
        if (controller == null) {
            throw new IllegalArgumentException("Feature " + feature + " is not registered");
        }
        return controller;
    }

    public int getFeatureCount() {
        return _featureControllers.size();
    }

    public void setFeatureActive(Feature feature) {
        for (IFeatureController featureController : _featureControllers.values()) {
            featureController.setActive(featureController.getFeature() == feature);
        }
    }

    public IFeatureController getActiveFeatureController() {
        for (IFeatureController featureController : _featureControllers.values()) {
            if (featureController.isActive()) {
                return featureController;
            }
        }
        throw new IllegalArgumentException("No active FeatureController available");
    }

    List<String> getParts() {
        List<String> parts = new ArrayList<>();
        for (IFeatureController feature : _featureControllers.values()) {
            if (feature.getMainPart().length() > 0) {
                parts.add(feature.getMainPart());
            }
        }
        return parts;
    }

    void addIfMissing(PortalType portalType) {
        portalTypes.add(portalType);
    }

}
