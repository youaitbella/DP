package org.inek.dataportal.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.PeppProposalDocument;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.NubFieldKey;
import org.inek.dataportal.facades.NubProposalFacade;
import org.inek.dataportal.feature.nub.NubController;
import org.inek.dataportal.helper.Utils;

@WebServlet(urlPatterns = {"/upload/nub"}, name = "NubTemplateUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class NubTemplateUploadServlet extends AbstractUploadServlet {

    @Inject SessionController _sessionController;
    @Inject NubProposalFacade _nubFacade;

    @Override
    protected void stream2Document(String filename, InputStream is) throws IOException {
        try {
            NubController controller = (NubController) _sessionController.getFeatureController(Feature.NUB);
            byte[] buffer = stream2blob(is);
            String fileText = new String(buffer, "UTF-8");
            if (fileText.startsWith("NuB Vorschlag")) {
                // old format
                _nubFacade.saveNubProposal(controller.createNubProposalFromOldFormat(filename, new String(buffer)));
                return;
            }
            int pos = fileText.lastIndexOf(NubFieldKey.CheckSum + "=");
            if (pos < 0) {
                throw new IOException("Formatfehler");
            }
            String template = fileText.substring(0, pos);
            String checksum = fileText.substring(pos + 9).replace("\r\n", "");
            if (checksum.equals(Utils.getChecksum(template + "Length=" + template.length()))) {
                _nubFacade.saveNubProposal(controller.createNubProposal(template));
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
