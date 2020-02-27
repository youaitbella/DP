package org.inek.dataportal.common.controller;

import org.inek.dataportal.common.data.adm.ReportTemplate;
import org.inek.dataportal.common.data.adm.facade.AdminFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.ApplicationTools;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 *
 * @author muellermi
 */
public class ReportController implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("ReportController");
    private static final long serialVersionUID = 1L;
    @Inject
    private AdminFacade _adminFacade;
    @Inject
    private ApplicationTools _appTools;

    public boolean reportTemplateExists(String name) {
        return _adminFacade.findReportTemplateByName(name).isPresent();
    }

    public byte[] getSingleDocument(String name, int id, String fileName) {
        String path = "" + id;
        return getSingleDocument(name, path, fileName);
    }

    public byte[] getSingleDocumentByIkAndYear(String name, int ik, int year, String fileName) {
        String path = ik + "/" + year;
        return getSingleDocument(name, path, fileName);
    }

    public byte[] getSingleDocument(String name, String path, String fileName) {
        Optional<ReportTemplate> optionalTemplate = _adminFacade.findReportTemplateByName(name);
        if (optionalTemplate.isPresent()) {
            ReportTemplate template = _adminFacade.findReportTemplateByName(name).get();
            return getSingleDocument(template, path, fileName);
        } else {
            return new byte[0];
        }
    }

    public ReportTemplate getReportTemplateByName(String name) {
        return _adminFacade.findReportTemplateByName(name).get();
    }

    private byte[] getSingleDocument(ReportTemplate template, String id, String fileName) {
        String address = template.getAddress()
                .replace("{hostNameInek}", _appTools.readConfig(ConfigKey.InekReportHostName))
                .replace("{hostNameCombit}", _appTools.readConfig(ConfigKey.CombitReportHostName))
                .replace("{0}", id);
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-ReportServer-ClientId", "portal");
            conn.setRequestProperty("X-ReportServer-ClientToken", "FG+RYOLDRuAEh0bO6OBddzcrF45aOI9C");
            if (conn.getResponseCode() != HTTP_OK) {
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

    private void createSingleDocument(ReportTemplate template, String id, String fileName) {
        String address = template.getAddress()
                .replace("{hostNameInek}", _appTools.readConfig(ConfigKey.InekReportHostName))
                .replace("{hostNameCombit}", _appTools.readConfig(ConfigKey.CombitReportHostName))
                .replace("{0}", id);
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-ReportServer-ClientId", "portal");
            conn.setRequestProperty("X-ReportServer-ClientToken", "FG+RYOLDRuAEh0bO6OBddzcrF45aOI9C");
            if (conn.getResponseCode() != HTTP_OK) {
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
    public byte[] getSingleDocument(String path) {
        try {
            String address = path
                    .replace("{hostNameInek}", _appTools.readConfig(ConfigKey.InekReportHostName))
                    .replace("{hostNameCombit}", _appTools.readConfig(ConfigKey.CombitReportHostName));

            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-ReportServer-ClientId", "portal");
            conn.setRequestProperty("X-ReportServer-ClientToken", "FG+RYOLDRuAEh0bO6OBddzcrF45aOI9C");
            if (conn.getResponseCode() != HTTP_OK) {
                throw new IOException("Report failed: HTTP error code : " + conn.getResponseCode());
            }
            byte[] file = StreamHelper.toByteArray(conn.getInputStream());
            conn.disconnect();
            return file;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return new byte[0];
    }

    public List<ReportTemplate> getReportTemplates(String group) {
        return _adminFacade.getReportTemplatesByGroup(group);
    }

}
