package org.inek.dataportal.feature.dropbox.upload;

import org.inek.dataportal.common.upload.HttpUtil;
import org.inek.dataportal.common.upload.AbstractUploadServlet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.feature.dropbox.DropBoxController;
import org.inek.dataportal.common.helper.StreamHelper;

@WebServlet(urlPatterns = {"/upload/dropbox"}, name = "FileUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class FileUploadServlet extends AbstractUploadServlet {

    @Inject private SessionController _sessionController;

    @Override
    protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException {
        File dir = ((DropBoxController) _sessionController.getFeatureController(Feature.DROPBOX)).getUploadDir();
        dir.mkdirs();
        try (FileOutputStream fos = new FileOutputStream(new File(dir, filename))) {
            new StreamHelper().copyStream(is, fos);
        }
    }

}
