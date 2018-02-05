/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.controller;

import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.helper.Topics;

/**
 *
 * @author muellermi
 */
public interface IFeatureController{
    Topics getTopics();
    String getMainPart();
    Feature getFeature();
    boolean isActive();
    void setActive (boolean isActive);
}
