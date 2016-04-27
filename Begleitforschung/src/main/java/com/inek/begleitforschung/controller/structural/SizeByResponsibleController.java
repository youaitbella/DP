/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.controller.structural;

import com.inek.begleitforschung.entities.structural.SizeClass;
import com.inek.begleitforschung.entities.Entities;
import java.io.Serializable;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class SizeByResponsibleController implements Serializable {

    private int _dataYear = 0;

    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        _dataYear = Integer.parseInt(params.get("dataYear"));
        try {
            readData();
        } catch (ParseException ex) {
            Logger.getLogger(SizeByResponsibleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void readData() throws ParseException {
        _data = _entities
                .getSizeClasses(_dataYear)
                .stream()
                .filter(c -> c.getResponsibleId() == _responsible)
                .collect(Collectors.toList());
    }

    // <editor-fold defaultstate="collapsed" desc="Property Responsible">
    private int _responsible;

    public int getResponsible() {
        return _responsible;
    }

    public void setResponsible(int responsible) {
        _responsible = responsible;
        try {
            readData();
        } catch (ParseException ex) {
            Logger.getLogger(SizeByResponsibleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // </editor-fold>

    @Inject private Entities _entities;
    private List<SizeClass> _data;  // this field is needed by the ice faces data table

    public List<SizeClass> getData() {
        return _data;
    }

    public int getTotal() {
        return _data.stream().mapToInt(c -> c.getHospitalCount()).sum();
    }

}
