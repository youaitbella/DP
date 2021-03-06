package org.inek.dataportal.common.controller;

import java.io.Serializable;
import java.util.logging.Logger;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.helper.Topics;

/**
 * A feature controller is responsible for displaying parts of the overview and distributing functional topics
 * @author muellermi
 */
public abstract class AbstractFeatureController implements IFeatureController, Serializable {
    protected static final Logger LOGGER = Logger.getLogger("FeatureController");
    private static final long serialVersionUID = 1L;

    private boolean _isActive;
    private final SessionController _sessionController;

    public AbstractFeatureController(SessionController sessionController) {
        _sessionController = sessionController;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    @Override
    public boolean isActive() {
        return _isActive;
    }

    @Override
    public void setActive(boolean active) {
        _isActive = active;
    }

    public SessionController getSessionController() {
        return _sessionController;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    // place this methods here
    // </editor-fold>

    @Override
    public Topics getTopics() {
        Topics topics = new Topics();
        addTopics(topics);
        return topics;
    }

    protected void addTopics(Topics topics) {
    }

    @Override
    public String getMainPart() {
        return "";
    }

    @Override
    public abstract Feature getFeature();

}
