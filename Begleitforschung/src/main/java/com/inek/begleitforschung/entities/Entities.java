/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author vohldo
 */
@Named
@SessionScoped
public class Entities implements Serializable {
    
    private final static String BASE_PATH = "W:\\EDV\\Projekte\\InEK-Browsers\\Begleitforschung\\";
    
    private List<C_111_211> _c_111_211;
    
    public String test() {
        return "test";
    }
    
    public List<C_111_211> getC_111(int dataYear) {
        List<C_111_211> x = new ArrayList<>();
        getC_111_211(dataYear).stream().filter((c) -> (c.getType() == 1)).forEach((c) -> {
            x.add(c);
        });
        return x;
    }
    
    private List<C_111_211> getC_111_211(int dataYear) {
        if(_c_111_211 == null) {
            _c_111_211 = mapC_111_211(readDataFile(dataYear, "C_111_211"));
        }
        return _c_111_211;
    }
    
    private List<C_111_211> mapC_111_211(List<String[]> data) {
        List<C_111_211> list = new ArrayList<>();
        for(String[] x : data) {
            C_111_211 y = new C_111_211(Integer.parseInt(x[0]), x[1], x[2], Integer.parseInt(x[3]), 
                    Integer.parseInt(x[4]), Integer.parseInt(x[5]), Integer.parseInt(x[6]), Double.parseDouble(x[7]), Double.parseDouble(x[8]), Double.parseDouble(x[9]));
            list.add(y);
        }
        return list;
    }
    
    private List<String[]> readDataFile(int dataYear, String fileName) {
        String file = BASE_PATH + dataYear + "\\" + fileName + ".csv";
        List<String[]> data = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean firstLine = true;
            while((line = reader.readLine()) != null) {
                if(firstLine) {
                    firstLine = false;
                    continue;
                }
                line = line.replaceAll(",", ".");
                data.add(line.split(";"));
            }
        } catch(Exception ex) {
            
        }
        return data;
    }
}
