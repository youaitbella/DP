/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.controller;

import com.inek.begleitforschung.entities.ApplicationData;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author vohldo
 */
@Named
@ApplicationScoped
public class DownloadController {
    
    
    public void downloadFile(int dataYear, int download) throws IOException {
        if(download == 1) {
            File f = new File(ApplicationData.BASE_PATH + dataYear + "/download/Begleit-Daten-"+dataYear+".zip");
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
            ec.setResponseContentType("application/zip"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
            ec.setResponseContentLength((int)f.length()); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"G-DRG-Begleitforschungsbrowser_"+dataYear+".zip\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

            OutputStream output = ec.getResponseOutputStream();
            InputStream input = new FileInputStream(f);
            byte[] b = new byte[(int)f.length()];
            input.read(b);
            output.write(b);
            output.close();
            input.close();
            fc.responseComplete();
        }
    }
}
