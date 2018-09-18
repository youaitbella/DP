/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.KhComparison.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.adm.ChangeLog;

/**
 *
 * @author lautenti
 */
@Stateless
public class ChangeLogFacade extends AbstractDataAccess {

    @Transactional
    public void saveChangeLogs(List<ChangeLog> actions) {
        for (ChangeLog ac : actions) {
            if (ac.getId() == 0) {
                persist(ac);
            }
            merge(ac);
        }
        actions.clear();
    }
}
