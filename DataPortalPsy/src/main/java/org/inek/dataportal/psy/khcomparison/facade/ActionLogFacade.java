/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.psy.khcomparison.entity.ActionLog;

/**
 *
 * @author lautenti
 */
@Stateless
public class ActionLogFacade extends AbstractDataAccess {

    @Transactional
    public void saveActionLogs(List<ActionLog> actions) {
        for (ActionLog ac : actions) {
            if (ac.getId() == 0) {
                persist(ac);
            }
            merge(ac);
        }
        actions.clear();
    }
}
