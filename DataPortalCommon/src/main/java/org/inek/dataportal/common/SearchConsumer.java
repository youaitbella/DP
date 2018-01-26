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

    void addDiagnosis(String code);
    void addProcedure(String code);
    void addDrg(String code) ;
    void addPepp(String code) ;
    void addDept(String code) ;
    
}
