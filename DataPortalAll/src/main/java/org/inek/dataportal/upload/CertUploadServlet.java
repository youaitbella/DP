/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import org.inek.dataportal.feature.certification.CertCertification;
import org.inek.dataportal.feature.certification.EditCert;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;

/**
 *
 * @author muellermi
 */
@WebServlet(urlPatterns = {"/upload/CertUploadServlet", "/Certification/upload/CertUploadServlet"}, name = "CertUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class CertUploadServlet extends AbstractUploadServlet {

//    @Override
//    protected void showFileName(String filename, HttpUtil httpUtil) {
//        HttpSession session = httpUtil.getRequest().getSession();
//        Map<String, FeatureScopedContextHolder.FeatureScopedInstance> map
//                = (Map<String, FeatureScopedContextHolder.FeatureScopedInstance>) session.getAttribute("FeatureScoped");
//        CertCertification certBean = FeatureScopedContextHolder.Instance.getBean(CertCertification.class, map);
//        certBean.setFile(filename);
//    }
    @Override
    protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException {
        HttpSession session = httpUtil.getRequest().getSession();
        @SuppressWarnings("unchecked") Map<String, FeatureScopedContextHolder.FeatureScopedInstance> map
                = (Map<String, FeatureScopedContextHolder.FeatureScopedInstance>) session.getAttribute("FeatureScoped");
        CertCertification certBean = FeatureScopedContextHolder.Instance.getBean(CertCertification.class, map);
        EditCert editCert = FeatureScopedContextHolder.Instance.getBean(EditCert.class, map);
        String targetName = certBean.getUploadFileName(editCert);
        if (targetName.isEmpty()) {
            return;
        }
        File target = new File(targetName);
        try (FileOutputStream fos = new FileOutputStream(target)) {
            new StreamHelper().copyStream(is, fos);
        }
        certBean.setFile(filename);
        certBean.logAction("Finish Upload " + filename + " -> " + target);
    }

}
