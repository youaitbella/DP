/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.common.helper;

import java.io.Serializable;

/**
 *
 * @author muellermi
 */
public class Topic implements Serializable {

    private final String _key;
    private final String _title;
    private final String _outcome;
    private boolean _isActive;
    private boolean _isVisible;

    public Topic(String title, String outcome) {
        this(title, title, outcome, true);
    }
    
    public Topic(String key, String title, String outcome) {
        this(key, title, outcome, true);
    }

    public Topic(String key, String title, String outcome, boolean isVisible) {
        _key = key;
        _title = title;
        _outcome = outcome;
        _isVisible = isVisible;
        _isActive = false;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public String getKey() {
        return _key;
    }

    public String getTitle() {
        return _title;
    }

    public String getOutcome() {
        return _outcome;
    }

    public boolean isActive() {
        return _isActive;
    }

    public void setActive(boolean isActive) {
        _isActive = isActive;
    }

    public boolean isVisible() {
        return _isVisible;
    }

    public void setVisible(boolean isVisible) {
        _isVisible = isVisible;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
// place this methods here
// </editor-fold>

}
