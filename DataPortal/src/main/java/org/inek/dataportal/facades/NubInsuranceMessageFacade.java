/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.nub.NubInsuranceMessage;

/**
 *
 * @author muellermi
 */
@Stateless
public class NubInsuranceMessageFacade extends AbstractFacade<NubInsuranceMessage> {

    public NubInsuranceMessageFacade() {
        super(NubInsuranceMessage.class);
    }

}
