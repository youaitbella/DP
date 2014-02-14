/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.common;

/**
 * This interface enables the SearchController to write a result of specific type
 * @author muellermi
 */
public interface SearchConsumer {

    public void addDiagnosis(String code);
    public void addProcedure(String code);
    public void addDrg(String code) ;
    public void addPepp(String code) ;
    public void addDept(String code) ;
    
}
