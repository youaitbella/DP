/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.controller;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.enums.CodeType;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.GlobalVars;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.psy.controller.SearchController.CodeInfo;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class SearchCode implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject private SessionHelper _sessionHelper;
    private String _searchText;
    private String _hint = "";
    private int _proposalSection = GlobalVars.ProposalSectionPepp.getVal();

    public void checkSearchToken(FacesContext context, UIComponent component, Object value) {
        _hint = "";
        String[] tokens = value.toString().split("(\\s|,)");
        for (String t : tokens) {
            if (t.replace("sch", "_").replace("ch", "_").replace("ck", "_").replace("st", "_").replace("qu", "_").
                    length() < 3) {
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
        getSearchController().search(_searchText, getProposalSectionYear() - 2, getProposalSectionYear() - 1);
        if (getCodeList().isEmpty()) {
            _hint = "Keine Ergebnisse zu Ihrer Eingabe gefunden.";
        } else {
            _hint = "";
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

    public void setProposalSection(int proposalSection) {
        this._proposalSection = proposalSection;
    }

    public int getProposalSection() {
        return _proposalSection;
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

    public boolean isSearchPeppPossible() {
        //return false;
        return getSearchController().getCodeSystem() == CodeType.Pepp;
    }

    public boolean isSearchDrgPossible() {
        return false;
    }

    public boolean isSearchDrg() {
        return false;
    }

    private int getProposalSectionYear() {
        int resultYear = 2015;
        switch (_proposalSection) {
            case 101: {
                resultYear = Utils.getTargetYear(Feature.DRG_PROPOSAL);
                break;
            }
            case 102: {
                resultYear = Utils.getTargetYear(Feature.PEPP_PROPOSAL);
                break;
            }
            case 103: {
                resultYear = Utils.getTargetYear(Feature.NUB);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown proposalSection: " + _proposalSection);
        }
        return resultYear;
    }

    public String getFirstYear() {
        return "" + (getProposalSectionYear() - 2);
    }

    public String getLastYear() {
        return "" + (getProposalSectionYear() - 1);
    }

// </editor-fold>
    public SearchController getSearchController() {
        return _sessionHelper.getSearchController();
    }

    public String getTargetPage() {
        return getSearchController().getTargetPage();
    }

}
