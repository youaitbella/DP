/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.controller.structural;

import com.inek.begleitforschung.entities.BedClass;
import com.inek.begleitforschung.entities.C_121_221_State_Size;
import com.inek.begleitforschung.entities.Entities;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class SizeByStateController implements Serializable{
    private int _dataYear = 0;

    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        _dataYear = Integer.parseInt(params.get("dataYear"));
        readData();
    }

    private void readData() {
        _data = _entities
                .getBedClasses(_dataYear)
                .stream()
                .filter(c -> c.getStateId() == _state)
                .collect(Collectors.toList());
    }

    private int _state;

    // <editor-fold defaultstate="collapsed" desc="Property State">
    public int getState() {
        return _state;
    }

    public void setState(int state) {
        _state = state;
        readData();
    }
    // </editor-fold>
    
    @Inject private Entities _entities;
    private List<BedClass> _data;  // this field is needed by the ice faces data table

    public List<BedClass> getData() {
        return _data;
    }

    public int getTotal(){
        return _data.stream().mapToInt(c -> c.getHospitalCount()).sum();
    }
}
