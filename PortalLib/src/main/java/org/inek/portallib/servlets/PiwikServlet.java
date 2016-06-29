/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.portallib.servlets;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Piwik-Servlet sends the client request to the http://services.g-drg.de/piwik server.
 * You need to use the piwik.js from the PortalResources project in your own project
 * Fix the default piwik snippet.
 * Piwik requests use this servlet, because g-drg.de doesn't have a SSL certificate 
 * (https to http connection is not secure).
 * @author Dominik Vohl
 */
@WebServlet(urlPatterns = {"/piwik/"}, name = "PiwikServlet")
public class PiwikServlet extends HttpServlet {
    private static final Logger _logger = Logger.getLogger("PiwikServlet");
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpURLConnection con = null;
        try {
            String getParam = request.getQueryString();
            response.setStatus(200);
            String ip = getIp(request);
            String userAgent = getUserAgent(request);
            if(getParam.contains("jfwid%3D") && getParam.contains("&urlref=")) {
                int jfwidStart = getParam.indexOf("jfwid%3D");
                int jfwidEnd = getParam.indexOf("&urlref=");
                String wid = getParam.substring(jfwidStart, jfwidEnd);
                getParam = getParam.replaceAll(wid, "");
            }
            URL piwik = new URL("http://services.g-drg.de/piwik/piwik.php?"+getParam);
            con = (HttpURLConnection) piwik.openConnection();
            con.setRequestProperty("User-Agent", userAgent);
            con.setRequestProperty("X-FORWARDED-FOR", ip);
            con.setRequestProperty("REMOTE-ADDR", ip);
            con.getResponseCode();
            con.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(PiwikServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PiwikServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(con != null)
                con.disconnect();
        }
    }
    
    private String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
    
    private String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }
}
