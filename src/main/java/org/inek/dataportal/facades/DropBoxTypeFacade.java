/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import javax.inject.Named;
import org.inek.dataportal.entities.DropBoxType;

/**
 *
 * @author muellermi
 */
@Stateless
public class DropBoxTypeFacade extends AbstractFacade<DropBoxType> {

    public DropBoxTypeFacade (){
        super(DropBoxType.class);
    }
    
}
