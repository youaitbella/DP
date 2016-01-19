/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.entities;

/**
 *
 * @author vohldo
 */
public class CsvFormatException extends Exception {
    
    @Override
    public String getMessage() {
        return "Error: Headline missmatch.";
    }
}
