/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author muellermi
 */
@Named
@ApplicationScoped
public class ApplicationData {

    private final static String BASE_PATH = "//vfileserver01/company$/EDV/Projekte/InEK-Browsers/Begleitforschung/";

    // </editor-fold>
    // </editor-fold>
    List<String[]> readDataFile(int dataYear, String fileName) {
        File folder = new File(BASE_PATH, "" + dataYear);
        File file = new File(folder, fileName + ".csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().skip(1).map(line -> line.replaceAll(",", ".").split(";")).collect(Collectors.toList());
        } catch (Exception ex) {
            Logger.getLogger(ApplicationData.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

}
