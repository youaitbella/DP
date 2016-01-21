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
public interface BegleitEntity {
    
    public final static String BASE_FILE_PATH = "//vFileserver01/company$/EDV/Projekte/InEK-Browsers/Begleitforschung";
    
    public String getFileName();
    public String getHeadLine();
}
