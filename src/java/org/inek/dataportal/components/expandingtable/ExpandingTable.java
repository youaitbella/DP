/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.components.expandingtable;

/**
 *
 * @author muellermi
 */
public class ExpandingTable {
    private String _script;

    public String getScript() {
        return _script;
    }
    public void setScript(String script) {
        _script = script;
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
    
}
