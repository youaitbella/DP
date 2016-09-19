/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.nub;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.entities.nub.NubMethodInfo;
import org.inek.dataportal.facades.NubRequestFacade;

/**
 *
 * @author muellermi
 */
@ViewScoped
@Named
public class NubMethodInfoController implements Serializable {

    @Inject NubRequestFacade _nubRequestFacade;

    List<NubMethodInfo> _nubMethodInfos = Collections.EMPTY_LIST;

    public List<NubMethodInfo> getNubMethodInfosHead() {
        return obtainNubMethodInfos(true);
    }

    public List<NubMethodInfo> getNubMethodInfosTail() {
        return obtainNubMethodInfos(false);
    }

    private List<NubMethodInfo> obtainNubMethodInfos(boolean isHead) {
        ensureNubMethodInfos();
        int amount = _split > 0 ? _split : _elementsPerPart;
        int[] counter = new int[1];
        Stream<NubMethodInfo> infoStream = _nubMethodInfos.stream()
                .filter(info -> isFiltered(info))
                .map(info -> {
                    counter[0]++;
                    info.setRowNum(counter[0]);
                    return info;
                })
                .skip(_part * _elementsPerPart + (isHead ? 0 : amount)).limit(isHead ? amount : _elementsPerPart - amount);
        return infoStream.collect(Collectors.toList());
    }

    public long getListSize(){
        ensureNubMethodInfos();
        return _nubMethodInfos.stream().filter(info -> isFiltered(info)).count();
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
        for (String searchToken : _searchTokens){
            found = found && wholeWord ? textToSearch.equals(searchToken) : textToSearch.contains(searchToken);
        }
        return found;
    }
    
    private void ensureNubMethodInfos() {
        if (_nubMethodInfos.isEmpty()) {
            _nubMethodInfos = _nubRequestFacade.readNubMethodInfos("N");
        }
    }

    int _elementsPerPart = 50;
    int _part = 0;
    int _split = 0;

    public int getPart() {
        return _part;
    }

    public int getSplit() {
        return _split;
    }
    
    public long getMaxSplit() {
        return Math.max(_elementsPerPart, 1 + (getListSize()-1) % _elementsPerPart);
    }
    

    List<String> _searchTokens = Collections.EMPTY_LIST;

    public String getFilter() {
        return obtainFilter(_searchTokens);
    }

    private String obtainFilter(List<String> searchTokens) {
        return searchTokens.stream().collect(Collectors.joining(" "));
    }

    public void setFilter(String filter) {
        List<String> searchTokens;
        if (filter == null || filter.isEmpty()) {
            searchTokens = Collections.EMPTY_LIST;
        } else {
            searchTokens = Arrays.asList(filter.split(" ")).stream().filter(t -> !t.isEmpty()).map(t -> t.toLowerCase()).collect(Collectors.toList());
        }
        if (obtainFilter(searchTokens).equals(obtainFilter(_searchTokens))){
            return;
        }
        _searchTokens = searchTokens;
        _part = 0;
        _description = "";
        _split = 0;
    }

    public String getPreviousInfo (){
        return obtainElementInfo(_part - 1);
    }

    public String getNextInfo (){
        return obtainElementInfo(_part + 1);
    }

    public String getCurrentInfo (){
        return obtainElementInfo(_part);
    }

    private String obtainElementInfo(long part) {
        long start = (part) * _elementsPerPart + 1;
        if (start < 0 || start > getListSize()){
            return "";
        }
        long end = Math.min((part + 1) * _elementsPerPart, getListSize());
        return "Zeilen " + start + " bis " + end;
    }

    public String nextPart() {
        ensureNubMethodInfos();
        if ((getListSize() - 1) / _elementsPerPart > _part) {
            _part++;
        }
        _split = -1;
        return "";
    }

    public String previousPart() {
        ensureNubMethodInfos();
        if (_part > 0) {
            _part--;
        }
        _split = -1;
        return "";
    }

    public String setSplitAndReload(NubMethodInfo info) {
        _split = 1 + (info.getRowNum()-1) % _elementsPerPart;
        List<NubMethodInfo> nubMethodInfos = _nubRequestFacade.readNubMethodInfos(info.getMethodId(), "D");
        _description = nubMethodInfos.stream().map(i -> i.getText()).collect(Collectors.joining("\r\n\r\n---------------------------------\r\n\r\n"));
        return "";
    }

    String _description = "";

    public String getDescription() {
        return _description;
    }

}
