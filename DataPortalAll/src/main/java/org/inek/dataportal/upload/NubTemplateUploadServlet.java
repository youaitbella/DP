package org.inek.dataportal.upload;

import org.inek.dataportal.common.upload.HttpUtil;
import org.inek.dataportal.common.upload.AbstractUploadServlet;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.enums.NubFieldKey;
import org.inek.dataportal.feature.nub.facades.NubRequestFacade;
import org.inek.dataportal.feature.nub.NubController;
import org.inek.dataportal.common.helper.Utils;

@WebServlet(urlPatterns = {"/upload/nub"}, name = "NubTemplateUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class NubTemplateUploadServlet extends AbstractUploadServlet {

    @Inject private SessionController _sessionController;
    @Inject private NubRequestFacade _nubFacade;
    
    @Override
    protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException {
        try {
            NubController controller = (NubController) _sessionController.getFeatureController(Feature.NUB);
            byte[] buffer = stream2blob(is);
            String fileText = new String(buffer, "UTF-8");
            int pos = fileText.lastIndexOf(NubFieldKey.CheckSum + "=");
            if (pos < 0) {
                throw new IOException("Formatfehler");
            }
            String template = fileText.substring(0, pos);
            String checksum = fileText.substring(pos + 9).replace("\r\n", "");
            if (checksum.equals(Utils.getChecksum(template + "Length=" + template.length()))) {
                _nubFacade.saveNubRequest(controller.createNubRequest(template));
                return;
            }
            throw new IOException("Formatfehler");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

}
