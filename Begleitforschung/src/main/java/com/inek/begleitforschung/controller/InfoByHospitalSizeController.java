/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class InfoByHospitalSizeController {
    private int _state = 5;

    public int getState() {
        return _state;
    }

    public void setState(int state) {
        _state = state;
    }
    
}
