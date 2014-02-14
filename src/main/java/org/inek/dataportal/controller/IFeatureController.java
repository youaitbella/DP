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
    public Topics getTopics();
    public String getMainPart();
    public Feature getFeature();
    public boolean isActive();
    public void setActive (boolean isActive);
}
