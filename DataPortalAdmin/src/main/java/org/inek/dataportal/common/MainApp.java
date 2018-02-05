/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.inek.dataportal.enums.Pages;

/**
 *
 * @author muellermi
 */

@Named
@RequestScoped
public class MainApp implements Serializable {
    private static final long serialVersionUID = 1L;

    public MainApp() {
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">

    // </editor-fold>

    public String getMainApp(){
        return Pages.MainApp.URL();
    }



}
