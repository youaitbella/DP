package org.inek.dataportal.psy.peppproposal;

import org.inek.dataportal.common.upload.HttpUtil;
import org.inek.dataportal.common.upload.AbstractUploadServlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.psy.peppproposal.entities.PeppProposalDocument;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.psy.peppproposal.PeppProposalController;

@WebServlet(urlPatterns = {"/upload/peppproposal"}, name = "PeppProposalUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class PeppProposalUploadServlet extends AbstractUploadServlet {

    @Inject private SessionController _sessionController;

    @Override
    protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException {
        PeppProposalController controller = (PeppProposalController) _sessionController.getFeatureController(Feature.PEPP_PROPOSAL);
        List<PeppProposalDocument> documents = controller.getDocuments();
        PeppProposalDocument document = findByName(documents, filename);
        if (document == null) {
            document = new PeppProposalDocument();
            document.setName(filename);
            documents.add(document);
        }
        document.setContent(stream2blob(is));
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
