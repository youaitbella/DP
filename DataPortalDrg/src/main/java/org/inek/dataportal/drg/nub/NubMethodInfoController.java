/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.drg.nub;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.drg.nub.entities.NubMethodInfo;
import org.inek.dataportal.drg.nub.facades.NubRequestFacade;

/**
 *
 * @author muellermi
 */
@ViewScoped
@Named
public class NubMethodInfoController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject private NubRequestFacade _nubRequestFacade;

    // <editor-fold defaultstate="collapsed" desc="NubMethodInfos head + tail">    
    public List<NubMethodInfo> getNubMethodInfosHead() {
        return obtainNubMethodInfos(true);
    }

    public List<NubMethodInfo> getNubMethodInfosTail() {
        return obtainNubMethodInfos(false);
    }

    private List<NubMethodInfo> obtainNubMethodInfos(boolean isHead) {
        int amount = _split > 0 ? _split : _elementsPerPart;
        int[] counter = new int[1];
        Stream<NubMethodInfo> infoStream = getFilteredInfoStream();
        infoStream = infoStream.sorted((i1, i2) -> compare(i1, i2))
                .map(info -> {
                    counter[0]++;
                    info.setRowNum(counter[0]);
                    return info;
                })
                .skip(_part * _elementsPerPart + (isHead ? 0 : amount))
                .limit(isHead ? amount : _elementsPerPart - amount);
        return infoStream.collect(Collectors.toList());
    }

    private Stream<NubMethodInfo> getFilteredInfoStream() {
        Stream<NubMethodInfo> infoStream = _nubRequestFacade.obtainNubMethodInfos().stream()
                .filter(info -> isFiltered(info));
        if (isRestrictToOne()) {
            int[] lastSeq = new int[1];
            lastSeq[0] = -1;
            // a sequence is only unique within one year!
            infoStream = infoStream.sorted((i1, i2) -> (i1.getBaseYear() * 10000 + i1.getSequence()) - (i2.getBaseYear() * 10000 + i2.getSequence()))
                    .filter(i -> lastSeq[0] != i.getSequence())
                    .peek(i -> lastSeq[0] = i.getSequence());
        }
        return infoStream;
    }

    private int compare(NubMethodInfo i1, NubMethodInfo i2) {
        switch (_sortCriteria) {
            case "year":
                if (_isDescending) {
                    return i2.getBaseYear() - i1.getBaseYear();
                }
                return i1.getBaseYear() - i2.getBaseYear();
            case "sequence":
                if (_isDescending) {
                    return i2.getSequence() - i1.getSequence();
                }
                return i1.getSequence() - i2.getSequence();
            case "name":
                if (_isDescending) {
                    return i2.getName().compareTo(i1.getName());
                }
                return i1.getName().compareTo(i2.getName());
            case "text":
                if (_isDescending) {
                    return i2.getText().compareTo(i1.getText());
                }
                return i1.getText().compareTo(i2.getText());
            default:
                return 1;
        }
    }

    private boolean isFiltered(NubMethodInfo info) {
        if (_searchTokens.isEmpty()) {
            return true;
        }
        return checkFilter(info.getName(), false) || checkFilter(info.getText(), false) || checkFilter("" + info.getSequence(), true);
    }

    private boolean checkFilter(String text, boolean wholeWord) {
        boolean found = true;
        String textToSearch = text.toLowerCase();
        for (String searchToken : _searchTokens) {
            found = found && (wholeWord ? textToSearch.equals(searchToken) : textToSearch.contains(searchToken));
        }
        return found;
    }

    // </editor-fold>  
    public long getListSize() {
        return getFilteredInfoStream().count();
    }

    // <editor-fold defaultstate="collapsed" desc="Navigation + split handling">    
    private void clearNavigationInfo() {
        _split = 0;
        _part = 0;
        _description = "";
    }

    private int _elementsPerPart = 50;
    private int _part = 0;
    private int _split = 0;

    public int getPart() {
        return _part;
    }

    public int getSplit() {
        return _split;
    }

    public long getMaxSplit() {
        return Math.max(_elementsPerPart, 1 + (getListSize() - 1) % _elementsPerPart);
    }

    public String setSplitAndReload(NubMethodInfo info) {
        _split = 1 + (info.getRowNum() - 1) % _elementsPerPart;
        _description = _nubRequestFacade.obtainNubMethodDescription(info.getMethodId());
        return "";
    }

    private String _description = "";

    public String getDescription() {
        return _description;
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Search tokens">    
    private List<String> _searchTokens = Collections.emptyList();

    public String getFilter() {
        return obtainFilter(_searchTokens);
    }

    private String obtainFilter(List<String> searchTokens) {
        return searchTokens.stream().collect(Collectors.joining(" "));
    }

    public void setFilter(String filter) {
        List<String> searchTokens;
        if (filter == null || filter.isEmpty()) {
            searchTokens = Collections.emptyList();
        } else {
            searchTokens = Arrays.asList(filter.split(" ")).stream().filter(t -> !t.isEmpty()).map(t -> t.toLowerCase()).collect(Collectors.toList());
        }
        if (obtainFilter(searchTokens).equals(obtainFilter(_searchTokens))) {
            return;
        }
        _searchTokens = searchTokens;
        clearNavigationInfo();
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="List navigation">    
    public String getPreviousInfo() {
        return obtainRangeInfo(_part - 1);
    }

    public String getNextInfo() {
        return obtainRangeInfo(_part + 1);
    }

    public String getCurrentInfo() {
        return obtainRangeInfo(_part);
    }

    private String obtainRangeInfo(long part) {
        long start = (part) * _elementsPerPart + 1;
        if (start < 0 || start > getListSize()) {
            return "";
        }
        long end = Math.min((part + 1) * _elementsPerPart, getListSize());
        return "Einträge " + start + " bis " + end;
    }

    public String nextPart() {
        if ((getListSize() - 1) / _elementsPerPart > _part) {
            _part++;
        }
        _split = 0;
        return "";
    }

    public String previousPart() {
        if (_part > 0) {
            _part--;
        }
        _split = 0;
        return "";
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Property SortCriteria + state">    
    private String _sortCriteria = "";
    private boolean _isDescending = false;

    public boolean isDescending() {
        return _isDescending;
    }

    public void setDescending(boolean isDescending) {
        _isDescending = isDescending;
    }

    public void setSortCriteria(String sortCriteria) {
        if (_sortCriteria.equals(sortCriteria)) {
            _isDescending = !_isDescending;
        } else {
            _isDescending = false;
        }
        _sortCriteria = sortCriteria == null ? "" : sortCriteria;
        clearNavigationInfo();
    }

    public String getSortCriteria() {
        return _sortCriteria;
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Property RestrictToOne">    
    private boolean _restrictToOne = false;

    public boolean isRestrictToOne() {
        return _restrictToOne;
    }

    public void setRestrictToOne(boolean restrictToOne) {
        if (_restrictToOne != restrictToOne) {
            _restrictToOne = restrictToOne;
            clearNavigationInfo();
        }
    }
    // </editor-fold>    

}
