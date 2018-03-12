package org.inek.dataportal.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.inek.dataportal.common.data.adm.ReportTemplate;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.feature.admin.facade.AdminFacade;

/**
 *
 * @author muellermi
 */
public class ReportController {
    private static final Logger LOGGER = Logger.getLogger("ReportController");

    @Inject
    private AdminFacade _adminFacade;

    public boolean reportTemplateExists(String name) {
        return _adminFacade.findReportTemplateByName(name).isPresent();
    }

    public byte[] getSingleDocument(String name, int id, String fileName) {
        Optional<ReportTemplate> optionalTemplate = _adminFacade.findReportTemplateByName(name);
        if (optionalTemplate.isPresent()) {
            ReportTemplate template = _adminFacade.findReportTemplateByName(name).get();
            return getSingleDocument(template, "" + id, fileName);
        } else {
            return new byte[0];
        }
    }

    public byte[] getSingleDocument(ReportTemplate template, String id, String fileName) {
        String address = template.getAddress().replace("{0}", id);
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-ReportServer-ClientId", "portal");
            conn.setRequestProperty("X-ReportServer-ClientToken", "FG+RYOLDRuAEh0bO6OBddzcrF45aOI9C");
            if (conn.getResponseCode() != 200) {
                throw new IOException("Report failed: HTTP error code : " + conn.getResponseCode());
            }
            byte[] file = StreamHelper.toByteArray(conn.getInputStream());
            conn.disconnect();
            return file;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            // todo? sessionController.alertClient("Bei der Reporterstellung trat ein Fehler auf");
        }
        return new byte[0];
    }

    public void createSingleDocument(String name, int id) {
        createSingleDocument(name, id, name);
    }

    public void createSingleDocument(String name, int id, String fileName) {
        _adminFacade.findReportTemplateByName(name).
                ifPresent((ReportTemplate t) -> createSingleDocument(t, "" + id, fileName));
    }

    public void createSingleDocument(ReportTemplate template, String id, String fileName) {
        String address = template.getAddress().replace("{0}", id);
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-ReportServer-ClientId", "portal");
            conn.setRequestProperty("X-ReportServer-ClientToken", "FG+RYOLDRuAEh0bO6OBddzcrF45aOI9C");
            if (conn.getResponseCode() != 200) {
                throw new IOException("Report failed: HTTP error code : " + conn.getResponseCode());
            }
            if (!Utils.downLoadDocument(conn.getInputStream(), fileName, 0)) {
                throw new IOException("Report failed: Error during download");
            }
            conn.disconnect();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            // todo? sessionController.alertClient("Bei der Reporterstellung trat ein Fehler auf");
        }
    }
    
}
