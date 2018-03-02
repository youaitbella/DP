/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import javax.ejb.Stateless;
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
