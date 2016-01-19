/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.entities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author vohldo
 */
@RequestScoped
@Named
public class Entities {
    
    private static List<A1> A1;
    private static List<A2> A2;
    private static List<A3> A3;
    private static List<B1Bundesland> B1Bundesland;

    public List<A1> getA1() {
        if(A1 == null)
            A1 = mapA1();
        return A1;
    }
    
    public List<A2> getA2() {
        if(A2 == null)
            A2 = mapA2();
        return A2;
    }
    
    public List<A3> getA3() {
        if(A3 == null)
            A3 = mapA3();
        return A3;
    }
    
    public List<B1Bundesland> getB1Bundesland() {
        if(B1Bundesland == null)
            B1Bundesland = mapB1Bundesland();
        return B1Bundesland;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Entity Mapper">
    
    private static List<A1> mapA1() {
        List<A1> list = new ArrayList<>();
        try {
            BufferedReader reader = createCsvReader(new A1().getFileName());
            String headLine  = new A1().getHeadLine();
            if(checkHeadline(reader.readLine(), headLine)) {
                String line = "";
                while((line = reader.readLine()) != null) {
                    String[] cols = createCsvColumns(line);
                    A1 ent = new A1(cols[0], Integer.parseInt(cols[1]), Integer.parseInt(cols[2]), cols[3]);
                    list.add(ent);
                }
            } else {
                throw new CsvFormatException();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Entities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | CsvFormatException ex) {
            Logger.getLogger(Entities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    private static List<A2> mapA2() {
        List<A2> list = new ArrayList<>();
        try {
            BufferedReader reader = createCsvReader(new A2().getFileName());
            String headLine  = new A2().getHeadLine();
            if(checkHeadline(reader.readLine(), headLine)) {
                String line = "";
                while((line = reader.readLine()) != null) {
                    String[] cols = createCsvColumns(line);
                    A2 ent = new A2(cols[0], Integer.parseInt(cols[1]), Float.parseFloat(cols[2]));
                    list.add(ent);
                }
            } else {
                throw new CsvFormatException();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Entities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | CsvFormatException ex) {
            Logger.getLogger(Entities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    private static List<A3> mapA3() {
        List<A3> list = new ArrayList<>();
        try {
            BufferedReader reader = createCsvReader(new A3().getFileName());
            String headLine  = new A3().getHeadLine();
            if(checkHeadline(reader.readLine(), headLine)) {
                String line = "";
                while((line = reader.readLine()) != null) {
                    String[] cols = createCsvColumns(line);
                    A3 ent = new A3(cols[0], Integer.parseInt(cols[1]), Float.parseFloat(cols[2]), Integer.parseInt(cols[3]), Integer.parseInt(cols[4]), Float.parseFloat(cols[5]));
                    list.add(ent);
                }
            } else {
                throw new CsvFormatException();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Entities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | CsvFormatException ex) {
            Logger.getLogger(Entities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    private static List<B1Bundesland> mapB1Bundesland() {
        List<B1Bundesland> list = new ArrayList<>();
        try {
            BufferedReader reader = createCsvReader(new B1Bundesland().getFileName());
            String headLine  = new B1Bundesland().getHeadLine();
            if(checkHeadline(reader.readLine(), headLine)) {
                String line = "";
                while((line = reader.readLine()) != null) {
                    String[] cols = createCsvColumns(line);
                    B1Bundesland ent = new B1Bundesland(cols[0], Integer.parseInt(cols[1]), cols[2]);
                    list.add(ent);
                }
            } else {
                throw new CsvFormatException();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Entities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | CsvFormatException ex) {
            Logger.getLogger(Entities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Helper">
    
    private static BufferedReader createCsvReader(String file) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(BegleitEntity.BASE_FILE_PATH + "\\2014\\" + file)); // TODO: load year dynamically
        return reader;
    }
    
    private static boolean checkHeadline(String readLine, String originHeadLine) {
        if(readLine.equals(originHeadLine))
            return true;
        return false;
    }
    
    private static String[] createCsvColumns(String line) {
        return line.split(";");
    }
    
    // </editor-fold>
}