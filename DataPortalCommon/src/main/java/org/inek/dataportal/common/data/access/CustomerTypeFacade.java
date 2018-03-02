/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.access;

import org.inek.dataportal.common.data.AbstractFacade;
import javax.ejb.Stateless;
import javax.inject.Named;
import org.inek.dataportal.common.data.common.CustomerType;

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
