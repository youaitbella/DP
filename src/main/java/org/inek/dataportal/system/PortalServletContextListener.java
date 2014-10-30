/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.system;

import java.util.HashSet;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.SessionTrackingMode;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author muellermi
 */
@WebListener
public class PortalServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HashSet<SessionTrackingMode> modes = new HashSet<>();
        modes.add(SessionTrackingMode.COOKIE);
        //sce.getServletContext().setSessionTrackingModes(modes);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
