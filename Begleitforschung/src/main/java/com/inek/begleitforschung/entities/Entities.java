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
import java.util.stream.Collectors;
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
    
    // <editor-fold defaultstate="collapsed" desc="C fields">
    
    private List<C_111_211> _c_111_211; 
    private List<C_111_211> _c_111;
    private List<C_111_211> _c_211;
    
    private List<C_112_212> _c_112_212;
    private List<C_112_212> _c_112;
    private List<C_112_212> _c_212;
    
    private List<C_113_213> _c_113_213;
    private List<C_113_213> _c_113;
    private List<C_113_213> _c_213;
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="C">
    
        public List<C_111_211> getC_111(int dataYear) {
            if(_c_111 == null) {
                _c_111 = getC_111_211(dataYear).stream().filter(c -> (c.getType() == 1)).collect(Collectors.toList());
            }
            return _c_111;
        }

        public List<C_111_211> getC_211(int dataYear) {
            if(_c_211 == null) {
                _c_211 = getC_111_211(dataYear).stream().filter((c) -> (c.getType() == 2)).collect(Collectors.toList());
            }
            return _c_211;
        }

        private List<C_111_211> getC_111_211(int dataYear) {
            if(_c_111_211 == null) {
                _c_111_211 = mapC_111_211(readDataFile(dataYear, "C_111_211"));
            }
            return _c_111_211;
        }
        
        public List<C_112_212> getC_112(int dataYear) {
            if(_c_112 == null) {
                _c_112 = getC_112_212(dataYear).stream().filter((c) -> (c.getType() == 1)).collect(Collectors.toList());
            }
            return _c_112;
        }
        
        public List<C_112_212> getC_212(int dataYear) {
            if(_c_212 == null) {
                _c_212 = getC_112_212(dataYear).stream().filter((c) -> (c.getType() == 2)).collect(Collectors.toList());
            }
            return _c_212;
        }
        
        private List<C_112_212> getC_112_212(int dataYear) {
            if(_c_112_212 == null) {
                _c_112_212 = mapC_112_212(readDataFile(dataYear, "C_112_212"));
            }
            return _c_112_212;
        }
        
        public List<C_113_213> getC_113(int dataYear) {
            if(_c_113 == null) {
                _c_113 = getC_113_213(dataYear).stream().filter((c) -> (c.getType() == 1)).collect(Collectors.toList());
            }
            return _c_113;
        }
        
        public List<C_113_213> getC_213(int dataYear) {
            if(_c_213 == null) {
                _c_213 = getC_113_213(dataYear).stream().filter((c) -> (c.getType() == 2)).collect(Collectors.toList());
            }
            return _c_213;
        }
        
        private List<C_113_213> getC_113_213(int dataYear) {
            if(_c_113_213 == null) {
                _c_113_213 = mapC_113_213(readDataFile(dataYear, "C_113_213"));
            }
            return _c_113_213;
        }
    
    // <editor-fold defaultstate="collapsed" desc="sum">
    
        public List<C_111_211> getC_111_sum() {
            List<C_111_211> x = new ArrayList<>();
            C_111_211 sum = new C_111_211(1, "", "", 0, 0, 0, 0, 0.0, 0.0, 0.0);
            for(C_111_211 c : _c_111){
                sum.setSumA(sum.getSumA()+c.getSumA());
                sum.setSumAw(sum.getSumAw()+c.getSumAw());
                sum.setSumAm(sum.getSumAm()+c.getSumAm());
                sum.setSumAu(sum.getSumAu()+c.getSumAu());
            }
            sum.setFractionW((float)sum.getSumAw() / sum.getSumA());
            sum.setFractionM((float)sum.getSumAm() / sum.getSumA());
            sum.setFractionU((float)sum.getSumAu() / sum.getSumA());
            x.add(sum);
            return x;
        }

        public List<C_111_211> getC_211_sum() {
            List<C_111_211> x = new ArrayList<>();
            C_111_211 sum = new C_111_211(1, "", "", 0, 0, 0, 0, 0.0, 0.0, 0.0);
            for(C_111_211 c : _c_211){
                sum.setSumA(sum.getSumA()+c.getSumA());
                sum.setSumAw(sum.getSumAw()+c.getSumAw());
                sum.setSumAm(sum.getSumAm()+c.getSumAm());
                sum.setSumAu(sum.getSumAu()+c.getSumAu());
            }
            sum.setFractionW((float)sum.getSumAw() / sum.getSumA());
            sum.setFractionM((float)sum.getSumAm() / sum.getSumA());
            sum.setFractionU((float)sum.getSumAu() / sum.getSumA());
            x.add(sum);
            return x;
        }
        
        public List<C_112_212> getC_112_sum() {
            List<C_112_212> x = new ArrayList<>();
            C_112_212 sum = new C_112_212(1, "", 0, 0, 0, 0, 0.0, 0.0, 0.0, "");
            for(C_112_212 c : _c_112) {
                sum.setSumA(sum.getSumA()+c.getSumA());
                sum.setSumAw(sum.getSumAw()+c.getSumAw());
                sum.setSumAm(sum.getSumAm()+c.getSumAm());
                sum.setSumAu(sum.getSumAu()+c.getSumAu());
            }
            sum.setFractionW((float)sum.getSumAw() / sum.getSumA());
            sum.setFractionM((float)sum.getSumAm() / sum.getSumA());
            sum.setFractionU((float)sum.getSumAu() / sum.getSumA());
            x.add(sum);
            return x;
        }
        
        public List<C_112_212> getC_212_sum() {
            List<C_112_212> x = new ArrayList<>();
            C_112_212 sum = new C_112_212(1, "", 0, 0, 0, 0, 0.0, 0.0, 0.0, "");
            for(C_112_212 c : _c_212) {
                sum.setSumA(sum.getSumA()+c.getSumA());
                sum.setSumAw(sum.getSumAw()+c.getSumAw());
                sum.setSumAm(sum.getSumAm()+c.getSumAm());
                sum.setSumAu(sum.getSumAu()+c.getSumAu());
            }
            sum.setFractionW((float)sum.getSumAw() / sum.getSumA());
            sum.setFractionM((float)sum.getSumAm() / sum.getSumA());
            sum.setFractionU((float)sum.getSumAu() / sum.getSumA());
            x.add(sum);
            return x;
        }
        
        public List<C_113_213> getC_113_sum() {
            List<C_113_213> x = new ArrayList<>();
            C_113_213 sum = new C_113_213(1, "", "", 0, 0.0, 0.0, 0, 0.0, 0, 0.0);
            for(C_113_213 c : _c_113) {
                sum.setSumA(sum.getSumA()+c.getSumA());
                sum.setAvgLos(sum.getAvgLos() + c.getAvgLos());
                sum.setAvgStdDeviation(sum.getAvgStdDeviation() + c.getAvgStdDeviation());
                sum.setSumKla(sum.getSumKla() + c.getSumKla());
                sum.setSumLla(sum.getSumLla() + c.getSumLla());
                sum.setFractionKla(sum.getSumKla() + c.getFractionKla());
                sum.setFractionLla(sum.getSumLla() + c.getSumLla());
            }
            sum.setAvgLos(sum.getAvgLos() / _c_113.size());
            sum.setAvgStdDeviation(sum.getAvgStdDeviation() / _c_113.size());
            sum.setFractionKla((float)sum.getSumKla()/sum.getSumA());
            sum.setFractionLla((float)sum.getSumLla()/sum.getSumA());
            x.add(sum);
            return x;
        }
        
        public List<C_113_213> getC_213_sum() {
            List<C_113_213> x = new ArrayList<>();
            C_113_213 sum = new C_113_213(1, "", "", 0, 0.0, 0.0, 0, 0.0, 0, 0.0);
            for(C_113_213 c : _c_213) {
                sum.setSumA(sum.getSumA()+c.getSumA());
                sum.setAvgLos(sum.getAvgLos() + c.getAvgLos());
                sum.setAvgStdDeviation(sum.getAvgStdDeviation() + c.getAvgStdDeviation());
                sum.setSumKla(sum.getSumKla() + c.getSumKla());
                sum.setSumLla(sum.getSumLla() + c.getSumLla());
                sum.setFractionKla(sum.getSumKla() + c.getFractionKla());
                sum.setFractionLla(sum.getSumLla() + c.getSumLla());
            }
            sum.setAvgLos(sum.getAvgLos() / _c_213.size());
            sum.setAvgStdDeviation(sum.getAvgStdDeviation() / _c_213.size());
            sum.setFractionKla((float)sum.getSumKla()/sum.getSumA());
            sum.setFractionLla((float)sum.getSumLla()/sum.getSumA());
            x.add(sum);
            return x;
        }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="mapper">
    
        private List<C_111_211> mapC_111_211(List<String[]> data) {
            List<C_111_211> list = new ArrayList<>();
            for(String[] x : data) {
                C_111_211 y = new C_111_211(Integer.parseInt(x[0]), x[1], x[2], Integer.parseInt(x[3]), 
                        Integer.parseInt(x[4]), Integer.parseInt(x[5]), Integer.parseInt(x[6]), Double.parseDouble(x[7]), Double.parseDouble(x[8]), Double.parseDouble(x[9]));
                list.add(y);
            }
            return list;
        }
        
        private List<C_112_212> mapC_112_212(List<String[]> data) {
            List<C_112_212> list = new ArrayList<>();
            for(String[] x : data) {
                C_112_212 y = new C_112_212(Integer.parseInt(x[0]), x[1], Integer.parseInt(x[2]), Integer.parseInt(x[3]),
                        Integer.parseInt(x[4]), Integer.parseInt(x[5]), Double.parseDouble(x[6]), Double.parseDouble(x[7]), Double.parseDouble(x[8]), x[9]);
                list.add(y);
            }
            return list;
        }
        
        private List<C_113_213> mapC_113_213(List<String[]> data) {
            List<C_113_213> list = new ArrayList<>();
            for(String[] x : data) {
                C_113_213 y = new C_113_213(Integer.parseInt(x[0]), x[1], x[2], Integer.parseInt(x[3]), Double.parseDouble(x[4]),
                        Double.parseDouble(x[5]), Integer.parseInt(x[6]), Double.parseDouble(x[7]), Integer.parseInt(x[8]), Double.parseDouble(x[9]));
                list.add(y);
            }
            return list;
        }

    // </editor-fold>
    
    // </editor-fold>
    
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
