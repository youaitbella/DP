/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.SearchController.CodeInfo;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.CodeType;
import org.inek.dataportal.enums.GlobalVars;

/**
 *
 * @author muellermi
 */
@Named
@ConversationScoped
public class SearchCode implements Serializable {

    private static final long serialVersionUID = 1L;
    private @Inject SessionController _sessionController;
    private String _searchText;
    private String _hint = "";

    public void checkSearchToken(FacesContext context, UIComponent component, Object value) {
        _hint = "";
        String tokens[] = value.toString().split("(\\s|,)");
        for (String t : tokens) {
            if (t.replace("sch", "_").replace("ch", "_").replace("ck", "_").replace("st", "_").replace("qu", "_").length() < 3) {
                FacesMessage msg = new FacesMessage(getBundle().getString("msgSearchMinLen"));
                throw new ValidatorException(msg);
            }
        }
    }

    private ResourceBundle getBundle() {
        FacesContext ctxt = FacesContext.getCurrentInstance();
        return ctxt.getApplication().getResourceBundle(ctxt, "msg");
    }

    public void search(ActionEvent e) {
        getSearchController().search(_searchText, GlobalVars.PeppProposalSystemYear.getVal() - 2, GlobalVars.PeppProposalSystemYear.getVal()-1);
        if (getCodeList().isEmpty()){
            _hint = "Keine Ergebnisse zu Ihrer Eingabe gefunden.";
        } else {
            _hint= "";
        }
    }

    public String putCode(String code) {
        return getSearchController().putCode(code);
    }

    public String getSearchText() {
        return _searchText;
    }

    public String getHint() {
        return _hint;
    }

    public void setHint(String hint) {
        this._hint = hint;
    }

    public void setSearchText(String searchText) {
        this._searchText = searchText;
        _hint = "";
    }

    public CodeType getCodeType() {
        return getSearchController().getCodeType();
    }

    public void setCodeType(CodeType codeType) {
        getSearchController().setCodeType(codeType);
        _hint = "";
    }

    public List<CodeInfo> getCodeList() {
        return getSearchController().getCodeList();
    }

    public boolean isSearchDiagnosis() {
        return getSearchController().isEnableDiag();
    }

    public boolean isSearchProcedure() {
        return getSearchController().isEnableProc();
    }

    public boolean isSearchPepp() {
        return getSearchController().isEnablePepp();
    }
    
    public boolean isSearchDrg() {
        return getSearchController().isEnableDrg();
    }
    
    public String getFirstYear(){
        return "" + (GlobalVars.PeppProposalSystemYear.getVal() - 2);
    }

    public String getLastYear(){
        return "" + (GlobalVars.PeppProposalSystemYear.getVal() - 1);
    }

// </editor-fold>
    public SearchController getSearchController() {
        return _sessionController.getSearchController();
    }

    public String getTargetPage() {
        return getSearchController().getTargetPage();
    }
}
