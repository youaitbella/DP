/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.inek.portallib.structures.KeyValue;

/**
 *
 * @author muellermi
 */
@Named
@ApplicationScoped
public class ApplicationData {

    public static final String BASE_PATH = "//vfileserver01/company$/EDV/Projekte/InEK-Browsers/BegleitforschungTest/";
    private int _lastDataYear = 0;

    // <editor-fold defaultstate="collapsed" desc="States">
    private Map<Integer, String> _states;

    public Map<Integer, String> obtainStateMap() {
        if (_states == null) {
            List<String[]> data = readDataFile("Bundesland");
            _states = data.stream().collect(Collectors.toMap(a -> Integer.parseInt(a[0]), a -> a[1]));
        }
        return _states;
    }

    public List<KeyValue<Integer, String>> getStates() {
        // this getter is needed becaus JSF up to versio 2.2 cant't handle maps - eagerly awaiting JSF 2.3 ;-)
        return obtainStateMap().entrySet().stream().map(e -> new KeyValue<>(e.getKey(), e.getValue())).collect(Collectors.toList());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BedClasses">
    private Map<Integer, String> _bedClasses;

    public Map<Integer, String> obtainBedClassMap() {
        if (_bedClasses == null) {
            List<String[]> data = readDataFile("Bettenstufe");
            _bedClasses = data.stream().collect(Collectors.toMap(a -> Integer.parseInt(a[0]), a -> a[1]));
        }
        return _bedClasses;
    }

    public List<KeyValue<Integer, String>> getBedClasses() {
        // this getter is needed becaus JSF up to versio 2.2 cant't handle maps - eagerly awaiting JSF 2.3 ;-)
        return obtainBedClassMap().entrySet().stream().map(e -> new KeyValue<>(e.getKey(), e.getValue())).collect(Collectors.toList());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Responsibles">
    private Map<Integer, String> _responsibles;

    public Map<Integer, String> obtainResponsibleMap() {
        if (_responsibles == null) {
            List<String[]> data = readDataFile("Traeger");
            _responsibles = data.stream().collect(Collectors.toMap(a -> Integer.parseInt(a[0]), a -> a[1]));
        }
        return _responsibles;
    }

    public List<KeyValue<Integer, String>> getResponsibles() {
        // this getter is needed becaus JSF up to versio 2.2 cant't handle maps - eagerly awaiting JSF 2.3 ;-)
        return obtainResponsibleMap().entrySet().stream().map(e -> new KeyValue<>(e.getKey(), e.getValue())).collect(Collectors.toList());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="read data">
    public List<String[]> readDataFile(String fileName) {
        return readDataFile(new File(BASE_PATH), fileName);
    }

    public List<String[]> readDataFile(int dataYear, String fileName) {
        if (dataYear != 0) {
            _lastDataYear = dataYear;
        }
        File folder = new File(BASE_PATH, "" + _lastDataYear);
        return readDataFile(folder, fileName);
    }

    private List<String[]> readDataFile(File folder, String fileName) {
        File file = new File(folder, fileName + ".csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            return reader.lines().skip(1).map(line -> line.split(";")).collect(Collectors.toList());
        } catch (Exception ex) {
            Logger.getLogger(ApplicationData.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.emptyList();
        }
    }
    // </editor-fold>

}
