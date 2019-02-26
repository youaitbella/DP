/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.access;

import org.inek.dataportal.common.data.AbstractFacade;
import org.inek.dataportal.common.data.common.CustomerType;

import javax.ejb.Stateless;

/**
 *
 * @author muellermi
 */
@Stateless
public class CustomerTypeFacade extends AbstractFacade<CustomerType> {

    public CustomerTypeFacade (){
        super(CustomerType.class);
    }
    
}
