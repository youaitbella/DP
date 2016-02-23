/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.controller;

import com.inek.begleitforschung.entities.C_121_221_State_Size;
import com.inek.begleitforschung.entities.Entities;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class InfoByHospitalSizeController {
    private int _state;

    public int getState() {
        return _state;
    }

    public void setState(int state) {
        _state = state;
    }
 
    @Inject private Entities _entities;
        public List<C_121_221_State_Size> getData(int dataYear) {
            return _entities
                    .getC_121_State_Size(dataYear)
                    .stream()
                    .filter(d -> _state == 0 || _state == d.getStateCode())
                    .collect(Collectors.toList());
        }
    
}
