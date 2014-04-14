/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.clientbehaviour;

import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@FacesBehavior(value = "confirmation")
public class ConfirmationBehavour extends ClientBehaviorBase {
    
    @Override
    public String getScript(ClientBehaviorContext behaviorContext) {
        return "return confirm('" + Utils.getMessage("msgConfirmDelete")+ "');";
    }
    
}
