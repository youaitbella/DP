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
import org.inek.dataportal.common.helper.Topic;
import org.inek.dataportal.common.helper.Topics;

/**
 *
 * @author muellermi
 */
@Dependent
public class FeatureHolder {

    @Inject private FeatureFactory _featureFactory;
    private final Map<String, IFeatureController> _featureControllers = new Hashtable<>();
    private final Set<PortalType> _portalTypes = new HashSet<>();
    private final Topics _topics = new Topics();
    private String _currentTopic = "";

    public void initFeatures(Account account, PortalType portaltype) {

    }

    public void clear() {
        _featureControllers.clear();
        _portalTypes.clear();
        _topics.clear();
    }

    public void add(Feature feature, SessionController sessionController) {
        IFeatureController controller = _featureFactory.createController(feature, sessionController);
        _featureControllers.put(feature.name(), controller);
        _topics.addTopics(controller.getTopics());
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

    public boolean hasNoFeatureSubscribed() {
        return _featureControllers
                .values()
                .stream()
                .filter(f -> f.getFeature() != Feature.USER_MAINTENANCE)
                .filter(f -> f.getFeature() != Feature.DOCUMENTS)
                .filter(f -> f.getFeature() != Feature.COOPERATION)
                .count() == 0;
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
        if (_portalTypes.contains(portalType)){
            return;
        }
        _portalTypes.add(portalType);
        _topics.addTopic(portalType.getDisplayName(), portalType.name());
    }

    public List<Topic> getTopics() {
        return _topics.getTopics();
    }

    public String getCurrentTopic() {
        return _topics.getActiveTopic().getKey();
    }

    public void setCurrentTopic(String currentTopic) {
        _topics.setActive(currentTopic);

    }

    public void clearCurrentTopic() {
        _topics.setAllInactive();
    }

    public void setCurrentTopicByUrl(String url) {
        Topic topic = _topics.findTopicByOutcome(url);
        if (topic.getKey() == null) {
            clearCurrentTopic();
        } else {
            setCurrentTopic(topic.getKey());
        }
    }

}
