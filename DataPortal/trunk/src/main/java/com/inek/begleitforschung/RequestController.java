/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung;

import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author vohldo
 */
@Named
@RequestScoped
public class RequestController {
    
    public String renderTest() {
        return new Date().getTime()+"";
    }
}
