/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.nub;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
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
public class NubMethodInfoControler implements Serializable{

    @Inject NubRequestFacade _nubRequestFacade;

    List<NubMethodInfo> _nubMethodInfos;
    
    public List<NubMethodInfo> getNubMethodInfosHead() {
        ensureNubMethodInfos();
        int amount = _split >= 0 ? _split + 1 : _elementsPerPart;
        int[] counter = new int[1];
        return _nubMethodInfos.stream().map(i -> {i.setRowNum(counter[0]); counter[0]++; return i;}).skip(_part * _elementsPerPart).limit(amount).collect(Collectors.toList());
    }

    public List<NubMethodInfo> getNubMethodInfosTail() {
        ensureNubMethodInfos();
        int amount = _split >= 0 ? _split + 1 : _elementsPerPart;
        int[] counter = new int[1];
        return _nubMethodInfos.stream().map(i -> {i.setRowNum(counter[0]); counter[0]++; return i;}).skip(_part * _elementsPerPart + amount).limit(_elementsPerPart-amount).collect(Collectors.toList());
    }

    private void ensureNubMethodInfos() {
        if (_nubMethodInfos == null) {
            _nubMethodInfos = _nubRequestFacade.readNubMethodInfos("N");
        }
    }

    int _elementsPerPart = 50;
    int _part = 0;
    int _split = -1;

    public int getPart() {
        return _part;
    }

    public int getSplit() {
        return _split;
    }

    public String nextPart() {
        ensureNubMethodInfos();
        if ((_nubMethodInfos.size() - 1) / _elementsPerPart > _part) {
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

    public String setSplitAndReload(NubMethodInfo info){
        _split = info.getRowNum() % _elementsPerPart;
        List<NubMethodInfo> nubMethodInfos = _nubRequestFacade.readNubMethodInfos(info.getMethodId(), "D");
        _description = nubMethodInfos.stream().map(i -> i.getText()).collect(Collectors.joining("\r\n\r\n---------------------------------\r\n\r\n"));
        return "";
    }
    
    String _description = "";
    
    public String getDescription() {
        return _description;
    }
}
