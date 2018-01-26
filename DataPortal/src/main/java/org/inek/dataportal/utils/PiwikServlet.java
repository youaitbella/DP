/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Piwik-Servlet sends the client request to the http://services.g-drg.de/piwik server. You need to use the piwik.js
 * from the PortalResources project in your own project Fix the default piwik snippet. Piwik requests use this servlet,
 * because g-drg.de doesn't have a SSL certificate (https to http connection is not secure).
 *
 * @author Dominik Vohl
 */
@WebServlet(urlPatterns = {"/piwik/"}, name = "PiwikServlet")
public class PiwikServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger("PiwikServlet");
    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";
    private static final String USER_AGENT = "User-Agent";
    private static final String REMOTE_ADDR = "REMOTE-ADDR";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpURLConnection con = null;
        try {
            String getParam = request.getQueryString();
            response.setStatus(200);
            String ip = getIp(request);
            String userAgent = getUserAgent(request);
            int jfwidStart = getParam.indexOf("jfwid%3D");
            int jfwidEnd = getParam.indexOf("&urlref=");
            if (jfwidStart > 0 && jfwidEnd > jfwidStart) {
                String wid = getParam.substring(jfwidStart, jfwidEnd);
                getParam = getParam.replaceAll(wid, "");
            }
            URL piwik = new URL("http://services.g-drg.de/piwik/piwik.php?" + getParam);
            con = (HttpURLConnection) piwik.openConnection();
            con.setRequestProperty(USER_AGENT, userAgent);
            con.setRequestProperty(X_FORWARDED_FOR, ip);
            con.setRequestProperty(REMOTE_ADDR, ip);
            con.getResponseCode();
            con.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(PiwikServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PiwikServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader(X_FORWARDED_FOR);
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    private String getUserAgent(HttpServletRequest request) {
        return request.getHeader(USER_AGENT);
    }
}
