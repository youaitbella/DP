/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.insurance.InsuranceNubNotice;

/**
 *
 * @author muellermi
 */
@Stateless
public class InsuranceNubNoticeFacade extends AbstractFacade<InsuranceNubNotice> {

    public InsuranceNubNoticeFacade() {
        super(InsuranceNubNotice.class);
    }
}
