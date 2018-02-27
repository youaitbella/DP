/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.psychstaff.backingbean;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.data.account.iface.Document;
import org.inek.dataportal.feature.psychstaff.entity.StaffProof;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author lautenti
 */
public class PsyStaffExport {
    
    private SessionController _sessionController;

    public PsyStaffExport(SessionController controller) {
        _sessionController = controller;
    }
    
    public void exportAllData(String excelDokument,StaffProof staffProof) {
        String excelFileName = excelDokument.substring(0, excelDokument.lastIndexOf(".")) + "_" + staffProof.getIk() + ".xlsx";
                
        File zipFile = new File("Export_" + staffProof.getIk() + ".zip");
        try (FileOutputStream fileOut = new FileOutputStream(zipFile);
          CheckedOutputStream checkedOut = new CheckedOutputStream(fileOut, new Adler32());
          ZipOutputStream compressedOut = new ZipOutputStream(new BufferedOutputStream(checkedOut))) {
            compressedOut.putNextEntry(new ZipEntry(excelFileName));
            ByteArrayInputStream ips = new ByteArrayInputStream(_sessionController.getSingleDocument(excelDokument, staffProof.getId(),
                    excelFileName));
            StreamHelper.copyStream(ips, compressedOut);
            compressedOut.closeEntry();
            compressedOut.flush();
            for(Document d : staffProof.getStaffProofDocuments() ) {
                compressedOut.putNextEntry(new ZipEntry(d.getName()));
                ByteArrayInputStream is = new ByteArrayInputStream(d.getContent());
                StreamHelper.copyStream(is, compressedOut);
                compressedOut.closeEntry();
                compressedOut.flush();
            }         
        } catch (IOException ex) {
            //throw new IllegalStateException(ex);
        }    
        try {
            InputStream is = new FileInputStream(zipFile);
            Utils.downLoadDocument(is, "Export_" + staffProof.getIk() + ".zip", 0);   
        } catch (IOException ex) {
            //throw new IllegalStateException(ex);
        }
    }
    
}
