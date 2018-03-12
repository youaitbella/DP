package org.inek.dataportal.common.clientbehaviour;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.context.FacesContext;
import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author muellermi
 */
@FacesBehavior(value = "confirmCancel")
public class ConfirmCancelBehaviour extends ClientBehaviorBase {

    @Override
    public String getScript(ClientBehaviorContext behaviorContext) {
        return "return confirm('" + Utils.getMessage("msgConfirmCancel") + "');";
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {

    }

}
