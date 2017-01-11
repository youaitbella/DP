package org.inek.dataportal.clientbehaviour;

import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@FacesBehavior(value = "confirmChangeData")
public class ConfirmChangeData extends ClientBehaviorBase {
    
    @Override
    public String getScript(ClientBehaviorContext behaviorContext) {
        return "return confirm('" + Utils.getMessage("msgConfirmChangeData")+ "');";
    }
    
}
