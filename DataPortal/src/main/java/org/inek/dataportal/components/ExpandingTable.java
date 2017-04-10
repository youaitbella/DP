/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.components;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author muellermi
 */
@FacesComponent(value = "org.inek.dataportal.ExpandingTable", createTag = false)
public class ExpandingTable extends UIComponentBase implements NamingContainer {

    private static final Logger LOGGER = Logger.getLogger("ExpandingTable");
    private String _script;

    public String getScript() {
        return _script;
    }

    public void setScript(String script) {
        _script = script;
    }

    public void sessionListener(ActionEvent event) {
        showSessionMap();
    }

    public String showSessionMap() {
        LOGGER.log(Level.WARNING, "Session map");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
        for (Map.Entry entry : map.entrySet()) {
            LOGGER.log(Level.WARNING, "\tKey: {0}", entry.getKey());
            LOGGER.log(Level.WARNING, "\t\tValue {0}", entry.getValue());
        }
        return "";
    }

    public String getFoo() {
        return "Foo";
    }

    public void setFoo(String dummy) {
    }

//    public void keyUp(AjaxBehaviorEvent event) {
//        HtmlInputText t = (HtmlInputText) event.getSource();
//        String currentId = t.getClientId();
//        List<ExpandingTableData> codes = getPeppProposal().getProcedures();
//        if (ensureEmptyEntry(procedures)) {
//            _script = "setCaretPosition('" + currentId + "', -1);";
//        } else {
//            _script = "";
//            FacesContext.getCurrentInstance().responseComplete();
//        }
//    }
//    
//    private boolean ensureEmptyEntry(List<ExpandingTableData> codes) {
//        if (codes.isEmpty() || codes.get(codes.size() - 1).getCode() != null) {
//            codes.add(new ProcedureInfo());
//            return true;
//        }
//        return false;
//    }
    @Override
    public String getFamily() {
        return UINamingContainer.COMPONENT_FAMILY;
    }
}
