/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.common.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author muellermi
 */
public class Topics implements Serializable {

    // usually only a handfull of topics will be managed by this class
    // thus, a simple ArrayList is used
    private List<Topic> _topics = new ArrayList<>();

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    /**
     * @return the _topics
     */
    public List<Topic> getTopics() {
        return _topics;
    }

    public List<Topic> getVisibleTopics() {
        List<Topic> visibleTopics = new ArrayList<>();
        for (Topic topic : _topics) {
            if (topic.isVisible()) {
                visibleTopics.add(topic);
            }
        }
        return visibleTopics;
    }

    /**
     * @param topics the _topics to set
     */
    public void setTopics(List<Topic> topics) {
        this._topics = topics;
    }

    // </editor-fold>
    public void clear() {
        _topics = new ArrayList<>();
    }

    public void addTopic(String title) {
        addTopic(title, title, "");
    }

    /**
     *
     * @param title
     * @param outcome
     */
    public void addTopic(String title, String outcome) {
        if (findTopic(title).getTitle() == null) {
            _topics.add(new Topic(title, outcome));
        }
    }

    public void addTopic(String key, String title, String outcome) {
        addTopic(key, title, outcome, true);
    }

    public void addTopic(String key, String title, String outcome, boolean isVisible) {
        if (findTopic(key).getTitle() == null) {
            _topics.add(new Topic(key, title, outcome, isVisible));
            if (_topics.size() == 1) {
                _topics.get(0).setActive(true);
            }
        }
    }

    public void addTopics(Topics newTopics) {
        for (Topic topic : newTopics.getTopics()) {
            _topics.add(topic);
        }
    }

    public Topic findTopic(String key) {
        for (Topic topic : _topics) {
            if (topic.getKey().equals(key)) {
                return topic;
            }
        }
        return new Topic(null, null, null);
    }

    public Topic findTopicByOutcome(String outcome) {
        for (Topic topic : _topics) {
            if (topic.getOutcome().equals(outcome)) {
                return topic;
            }
        }
        return new Topic(null, null, null);
    }

    public Topic getActiveTopic() {
        for (Topic topic : _topics) {
            if (topic.isActive()) {
                return topic;
            }
        }
        return new Topic(null, null, null);
    }

    public void setAllInactive() {
        for (Topic topic : _topics) {
            topic.setActive(false);
        }
    }

    public void setActive(String key) {
        for (Topic topic : _topics) {
            topic.setActive(topic.getKey().equals(key));
        }
    }

    public void setActive(Topic activeTopic) {
        for (Topic topic : _topics) {
            topic.setActive(topic.getKey().equals(activeTopic.getKey()));
        }
    }

    public void activatePriorTopic() {
        for (int i = 1; i < _topics.size(); i++) {
            Topic topic = _topics.get(i);
            if (topic.isActive()) {
                topic.setActive(false);
                topic = _topics.get(i - 1);
                topic.setActive(true);
                return;
            }
        }
    }

    public void activateNextTopic() {
        for (int i = 0; i < _topics.size() - 1 ; i++) {
            Topic topic = _topics.get(i);
            if (topic.isActive()) {
                topic.setActive(false);
                topic = _topics.get(i + 1);
                topic.setActive(true);
                return;
            }
        }
    }

    public boolean hasPriorTopic() {
        for (int i = 1; i < _topics.size(); i++) {
            Topic topic = _topics.get(i);
            if (topic.isActive()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNextTopic() {
        for (int i = 0; i < _topics.size() - 1 ; i++) {
            Topic topic = _topics.get(i);
            if (topic.isActive()) {
                return true;
            }
        }
        return false;
    }

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    // place this methods here
    // </editor-fold>
}
