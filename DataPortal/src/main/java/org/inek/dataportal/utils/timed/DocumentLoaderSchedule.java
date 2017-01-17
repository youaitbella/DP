/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.utils.timed;

import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author vohldo
 */
@Stateless
public class DocumentLoaderSchedule {

    private static final Logger _logger = Logger.getLogger("DocumentLoaderSchedule");

    @Inject
    private DocumentLoader _docLoader;

    @Schedule(hour = "*", minute = "*/1", info = "every 1 minute")
    private void monitorDocumentRoot() {
        _docLoader.monitorDocumentRoot();
    }
}
