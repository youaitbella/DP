/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.begleitforschung.controller.structural;

import org.inek.begleitforschung.entities.Entities;
import org.inek.begleitforschung.entities.structural.CmiClass;
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
public class CmiByBedClassController implements Serializable{
    private int _dataYear = 0;

    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        _dataYear = Integer.parseInt(params.get("dataYear"));
        readData();
    }

    private void readData() {
        _data = _entities
                .getCmiClasses(_dataYear)
                .stream()
                .filter(c -> c.getBedClassId() == _bedClass)
                .collect(Collectors.toList());
    }

    // <editor-fold defaultstate="collapsed" desc="Property BedClass">
    private int _bedClass;

    public int getBedClass() {
        return _bedClass;
    }

    public void setBedClass(int bedClass) {
        _bedClass = bedClass;
        readData();
    }
    // <//editor-fold>
    
    @Inject private Entities _entities;
    private List<CmiClass> _data;  // this field is needed by the ice faces data table

    public List<CmiClass> getData() {
        return _data;
    }

    public int getTotal() {
        return _data.stream().mapToInt(c -> c.getHospitalCount()).sum();
    }
   
}
