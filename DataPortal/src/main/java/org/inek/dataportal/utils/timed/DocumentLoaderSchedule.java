/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.utils.timed;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.facades.admin.ConfigFacade;

/**
 *
 * @author vohldo
 */
@Startup
@Singleton
public class DocumentLoaderSchedule {

    private static final Logger _logger = Logger.getLogger("DocumentLoaderSchedule");

    @Inject
    private ConfigFacade _config;

    @Inject
    private DocumentLoader _docLoader;

    @Schedule(hour = "*", minute = "*/1", info = "every 1 minute")
    //    @Schedule(hour = "*", minute = "*", second = "*/5", info = "every 5 minutes") // for testing purpose
    private void monitorDocumentRoot() {
        _docLoader.monitorDocumentRoot();
    }
}
