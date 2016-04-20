/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.controller;

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
public class InfoByHospitalSizeController implements Serializable {

    private int _dataYear = 0;
    private int _type = 0;

    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        _dataYear = Integer.parseInt(params.get("dataYear"));
        _type = Integer.parseInt(params.get("type"));
        readData();
        readSumData();
    }

    private void readData() {
        _data = _entities
                .getC_121_221_State_Size(_dataYear)
                .stream()
                .filter(c -> c.getType() == _type)
                .filter(d -> _state == d.getStateCode())
                .collect(Collectors.toList());
    }
    
    private void readSumData() {
        _sumData = _entities
                .getC_121_221_sum(_dataYear)
                .stream()
                .filter(c -> c.getType() == _type)
                .filter(d -> _state == d.getStateCode())
                .collect(Collectors.toList());
    }

    private int _state;

    public int getState() {
        return _state;
    }

    public void setState(int state) {
        _state = state;
        readData();
        readSumData();
    }
    
    @Inject private Entities _entities;
    private List<C_121_221_State_Size> _data;  // this field is needed by the ice faces data table
    private List<C_121_221_State_Size> _sumData;

    public List<C_121_221_State_Size> getData() {
        return _data;
    }
    
    public List<C_121_221_State_Size> getSumData() {
        return _sumData;
    }

}
