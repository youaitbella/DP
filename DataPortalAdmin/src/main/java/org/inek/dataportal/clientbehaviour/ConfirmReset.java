package org.inek.dataportal.clientbehaviour;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.context.FacesContext;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@FacesBehavior(value = "confirmReset")
public class ConfirmReset extends ClientBehaviorBase {

    @Override
    public String getScript(ClientBehaviorContext behaviorContext) {
        return "return confirm('" + Utils.getMessage("msgConfirmReset") + "');";
    }

    @Override
    public void decode(FacesContext context, UIComponent component) { }

}
