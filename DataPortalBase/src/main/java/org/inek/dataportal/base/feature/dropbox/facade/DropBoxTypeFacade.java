/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.base.feature.dropbox.facade;

import org.inek.dataportal.base.feature.dropbox.entities.DropBoxType;
import org.inek.dataportal.common.data.AbstractFacade;

import javax.ejb.Stateless;

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
