/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common;

import java.util.ArrayList;
import java.util.List;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.common.DiagnosisInfo;
import org.inek.dataportal.entities.common.ProcedureInfo;
import org.inek.dataportal.entities.drg.DrgInfo;
import org.inek.dataportal.entities.pepp.PeppInfo;
import org.inek.dataportal.enums.CodeType;
import org.inek.dataportal.facades.DrgFacade;
import org.inek.dataportal.facades.PeppFacade;
import org.inek.dataportal.facades.common.DiagnosisFacade;
import org.inek.dataportal.facades.common.ProcedureFacade;

/**
 *
 * @author muellermi
 */
public class SearchController {

    private final SessionController _sessionController;
    private boolean _enableProc;
    private boolean _enableDiag;
    private boolean _enableDrg;
    private boolean _enablePepp;
    private boolean _enableDept;
    private CodeType _codeType;
    private List<CodeInfo> _codeList = new ArrayList<>();
    private SearchConsumer _searchConsumer;
    private String _targetPage;
    private ProcedureFacade _procedureFacade;
    private DiagnosisFacade _diagnosisFacade;
    private PeppFacade _peppFacade;
    private DrgFacade _drgFacade;

    public SearchController(SessionController sessionController,
            ProcedureFacade procedureFacade,
            DiagnosisFacade diagnosisFacade,
            PeppFacade peppFacade, DrgFacade drgFacade) {
        _sessionController = sessionController;
        _procedureFacade = procedureFacade;
        _diagnosisFacade = diagnosisFacade;
        _peppFacade = peppFacade;
        _drgFacade = drgFacade;
    }
    // <editor-fold defaultstate="collapsed" desc="getter / setter">

    public boolean isEnableProc() {
        return _enableProc;
    }

    public void setEnableProc(boolean enableProc) {
        this._enableProc = enableProc;
    }

    public boolean isEnableDiag() {
        return _enableDiag;
    }

    public void setEnableDiag(boolean enableDiag) {
        this._enableDiag = enableDiag;
    }

    public boolean isEnableDrg() {
        return _enableDrg;
    }

    public void setEnableDrg(boolean enableDrg) {
        this._enableDrg = enableDrg;
    }

    public boolean isEnablePepp() {
        return _enablePepp;
    }

    public void setEnablePepp(boolean enablePepp) {
        this._enablePepp = enablePepp;
    }

    public boolean isEnableDept() {
        return _enableDept;
    }

    public void setEnableDept(boolean enableDept) {
        this._enableDept = enableDept;
    }

    public CodeType getCodeType() {
        return _codeType;
    }

    public void setCodeType(CodeType codeType) {
        if (_codeType != codeType) {
            _codeType = codeType;
            _codeList = new ArrayList<CodeInfo>();
        }
    }

    public SearchConsumer getSearchConsumer() {
        return _searchConsumer;
    }

    public void setSearchConsumer(SearchConsumer searchConsumer) {
        this._searchConsumer = searchConsumer;
    }

    public List<CodeInfo> getCodeList() {
        return _codeList;
    }

    public String getTargetPage() {
        return _targetPage;
    }

    public void setTargetPage(String targetPage) {
        _targetPage = targetPage;
    }

    // </editor-fold>
    public SearchController bindSearchConsumer(SearchConsumer searchConsumer) {
        setSearchConsumer(searchConsumer);
        return this;
    }

    public SearchController bindTargetPage(String targetPage) {
        setTargetPage(targetPage);
        return this;
    }

    public SearchController bindCodeType(CodeType codeType) {
        setCodeType(codeType);
        return this;
    }

    public SearchController bindOneCodeType(CodeType codeType) {
        enableOneType(codeType);
        return this;
    }

    public SearchController enableCodeType(CodeType codeType) {
        switch (codeType) {
            case Diag:
                _enableDiag = true;
                break;
            case Proc:
                _enableProc = true;
                break;
            case Drg:
                _enableDrg = true;
                break;
            case Pepp:
                _enablePepp = true;
                break;
            case Dept:
                _enableDept = true;
                break;
        }

        return this;
    }

    public void enableOneType(CodeType codeType) {
        _enableProc = codeType == CodeType.Proc;
        _enableDiag = codeType.equals(CodeType.Diag);
        _enableDrg = codeType.equals(CodeType.Drg);
        _enablePepp = codeType.equals(CodeType.Pepp);
        _enableProc = codeType.equals(CodeType.Proc);
        setCodeType(codeType);
    }

    public void search(String searchText) {
        search(searchText, 0, 0);
    }

    public void search(String searchText, int firstYear, int lastYear) {
        _codeList = new ArrayList<CodeInfo>();
        switch (_codeType) {
            case Diag:
                List<DiagnosisInfo> diags = _diagnosisFacade.findAll(searchText, firstYear, lastYear);
                for (DiagnosisInfo diag : diags) {
                    getCodeList().add(new CodeInfo(diag.getCode(), diag.getFirstYear(), diag.getLastYear(), diag.getName()));
                }
                break;
            case Proc:
                List<ProcedureInfo> procs = _procedureFacade.findAll(searchText, firstYear, lastYear);
                for (ProcedureInfo proc : procs) {
                    getCodeList().add(new CodeInfo(proc.getCode(), proc.getFirstYear(), proc.getLastYear(), proc.getName()));
                }
                break;
            case Drg:

                List<DrgInfo> drgs = _drgFacade.findAll(searchText, firstYear, lastYear);
                for (DrgInfo drg : drgs) {
                    getCodeList().add(new CodeInfo(drg.getCode(), drg.getYear(), 0, drg.getText()));
                }

                break;
            case Pepp:
                List<PeppInfo> pepps = _peppFacade.findAll(searchText, firstYear, lastYear);
                for (PeppInfo pepp : pepps) {
                    getCodeList().add(new CodeInfo(pepp.getCode(), pepp.getYear(), 0, pepp.getText()));
                }
                break;
            case Dept:
                break;
        }
    }

    public String putCode(String code) {
        switch (_codeType) {
            case Diag:
                _searchConsumer.addDiagnosis(code);
                break;
            case Proc:
                _searchConsumer.addProcedure(code);
                break;
            case Drg:
                _searchConsumer.addDrg(code);
                break;
            case Pepp:
                _searchConsumer.addPepp(code);
                break;
            case Dept:
                break;

        }
        return getTargetPage();
    }

    // <editor-fold defaultstate="collapsed" desc="internal class CodeInfo">
    public class CodeInfo {

        private String _code;
        private int _firstYear;
        private int _lastYear;
        private String _description;

        public CodeInfo(String code, int firstYear, int lastYear, String description) {
            _code = code;
            _firstYear = firstYear;
            _lastYear = lastYear;
            _description = description;
        }

        public String getCode() {
            return _code;
        }

        public void setCode(String code) {
            this._code = code;
        }

        public int getFirstYear() {
            return _firstYear;
        }

        public void setFirstYear(int year) {
            this._firstYear = year;
        }

        public int getLastYear() {
            return _lastYear;
        }

        public void setLastYear(int lastYear) {
            this._lastYear = lastYear;
        }

        public String getDescription() {
            return _description;
        }

        public void setDescription(String description) {
            this._description = description;
        }

    }
    // </editor-fold>
}
