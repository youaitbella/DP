package org.inek.dataportal.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.feature.peppproposal.entities.PeppProposalDocument;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.enums.NubFieldKey;
import org.inek.dataportal.facades.NubRequestFacade;
import org.inek.dataportal.feature.nub.NubController;
import org.inek.dataportal.feature.nub.NubSessionTools;
import org.inek.dataportal.common.helper.Utils;

@WebServlet(urlPatterns = {"/upload/nub"}, name = "NubTemplateUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class NubTemplateUploadServlet extends AbstractUploadServlet {

    @Inject private SessionController _sessionController;
    @Inject private NubRequestFacade _nubFacade;
    @Inject private NubSessionTools _nubSessionTools;
    
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

    private PeppProposalDocument findByName(List<PeppProposalDocument> documents, String name) {
        for (PeppProposalDocument document : documents) {
            if (document.getName().equalsIgnoreCase(name)) {
                return document;
            }
        }
        return null;
    }

}
