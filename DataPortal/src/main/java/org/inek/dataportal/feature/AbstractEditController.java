/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.inek.dataportal.common.SearchConsumer;
import org.inek.dataportal.helper.Topic;
import org.inek.dataportal.helper.Topics;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public abstract class AbstractEditController implements SearchConsumer, Serializable {
    // <editor-fold defaultstate="collapsed" desc="fields">

    private static final long serialVersionUID = 1L;
    private final Topics _topics;
    // </editor-fold>

    public AbstractEditController() {
        _topics = new Topics();
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public List<Topic> getTopics() {
        return _topics.getTopics();
    }

    public List<Topic> getVisibleTopics() {
        return _topics.getVisibleTopics();
    }

    public void setTopics(List<Topic> topics) {
        _topics.setTopics(topics);
    }

    public Topic getActiveTopic() {
        return _topics.getActiveTopic();
    }

    public void setActiveTopic(String topic) {
        _topics.setActive(topic);
        topicChanged();
    }

    public void setActiveTopic(Topic topic) {
        _topics.setActive(topic);
        topicChanged();
    }

    public String getActiveTopicKey() {
        return _topics.getActiveTopic().getKey();
    }

    public void setActiveTopicKey(String key) {
        _topics.setActive(key);
        topicChanged();
        //Utils.navigate(getActiveTopic().getOutcome());
    }

    public boolean isPriorTopic() {
        return _topics.hasPriorTopic();
    }
    
    public boolean isNextTopic() {
        return _topics.hasNextTopic();
    }
    
    public void activatePriorTopic(){
        _topics.activatePriorTopic();
        topicChanged();
    }
    
    public void activateNextTopic(){
        _topics.activateNextTopic();
        topicChanged();
    }
    
    public String refrehPage(){
        return "";
    }
    
    public Topic findTopic(String key) {
        return _topics.findTopic(key);
    }
    // </editor-fold>

    @PostConstruct
    private void initTopics() {
        addTopics();
    }

    protected abstract void addTopics();

    protected void addTopic(String key, String outcome) {
        _topics.addTopic(key, Utils.getMessage(key), outcome);
    }

    protected void addTopic(String key, String outcome, boolean isVisible) {
        _topics.addTopic(key, Utils.getMessage(key), outcome, isVisible);
    }

    public String getPart() {
        return _topics.getActiveTopic().getOutcome();
    }

    public void changeTab(String newTopic) {
        if (getActiveTopic().getKey().equals(newTopic)) {
            return;
        }
        setActiveTopic(newTopic);
    }

    public String changeTabPage(String newTopic) {
        changeTab(newTopic);
        return _topics.getActiveTopic().getOutcome();
    }

    @Override
    public void addDiagnosis(String code) {
    }

    @Override
    public void addProcedure(String code) {
    }

    @Override
    public void addDrg(String code) {
    }

    @Override
    public void addPepp(String code) {
    }

    @Override
    public void addDept(String code) {
    }

    protected void topicChanged() {
    }

}
