package org.inek.dataportal.drg.drgproposal.upload;

import org.inek.dataportal.common.upload.HttpUtil;
import org.inek.dataportal.common.upload.AbstractUploadServlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.drg.drgproposal.entities.DrgProposalDocument;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.drg.drgproposal.DrgProposalController;

@WebServlet(urlPatterns = {"/upload/drgproposal"}, name = "DrgProposalUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class DrgProposalUploadServlet extends AbstractUploadServlet {

    @Inject private SessionController _sessionController;

    @Override
    protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException {
        DrgProposalController controller = (DrgProposalController) _sessionController.getFeatureController(Feature.DRG_PROPOSAL);
        List<DrgProposalDocument> documents = controller.getDocuments();
        DrgProposalDocument document = findByName(documents, filename);
        if (document == null) {
            document = new DrgProposalDocument();
            document.setName(filename);
            documents.add(document);
        }
        document.setContent(stream2blob(is));
    }

    private DrgProposalDocument findByName(List<DrgProposalDocument> documents, String name) {
        for (DrgProposalDocument document : documents) {
            if (document.getName().equalsIgnoreCase(name)) {
                return document;
            }
        }
        return null;
    }

}
