/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.Agency;

/**
 *
 * @author muellermi
 */
@Stateless
public class AgencyFacade extends AbstractFacade<Agency> {

    public AgencyFacade() {
        super(Agency.class);
    }

}
