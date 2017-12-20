/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.begleitforschung.controller;

import org.inek.begleitforschung.entities.ApplicationData;
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
public class DownloadController{
    
    
    public void downloadFile(int dataYear, int download) throws IOException {
        if(download == 1) {
            File f = new File(ApplicationData.BASE_PATH + dataYear + "/download/Begleit-Daten-"+dataYear+".zip");
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            ec.responseReset();
            ec.setResponseContentType("application/zip"); 
            ec.setResponseContentLength((int)f.length()); 
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"G-DRG-Begleitforschungsbrowser_"+dataYear+".zip\""); 

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
