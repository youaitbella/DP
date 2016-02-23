/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
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

    public final static String BASE_PATH = "//vfileserver01/company$/EDV/Projekte/InEK-Browsers/Begleitforschung/";

    private List<KeyValue<Integer, String>> _states;
    public List<KeyValue<Integer, String>> getStates() {
        if (_states == null){
            _states = readDataFile("Bundesland").stream().map(a -> new KeyValue<>(Integer.parseInt(a[0]), a[1])).collect(Collectors.toList());
        }
        return _states;
    }

    public List<String[]> readDataFile(String fileName) {
        return readDataFile(new File(BASE_PATH), fileName);
    }

    public List<String[]> readDataFile(int dataYear, String fileName) {
        File folder = new File(BASE_PATH, "" + dataYear);
        return readDataFile(folder, fileName);
    }

    private List<String[]> readDataFile(File folder, String fileName) {
        File file = new File(folder, fileName + ".csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().skip(1).map(line -> line.replaceAll(",", ".").split(";")).collect(Collectors.toList());
        } catch (Exception ex) {
            Logger.getLogger(ApplicationData.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

}
