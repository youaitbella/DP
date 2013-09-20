package org.inek.dataportal.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.RequestDocument;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.feature.requestsystem.RequestController;

@WebServlet(urlPatterns = {"/upload/attachment"}, name = "AttachmentUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class AttachmentUploadServlet extends AbstractUploadServlet {

    @Inject SessionController _sessionController;

    @Override
    protected void stream2Document(String filename, InputStream is) throws IOException {
        RequestController controller = (RequestController) _sessionController.getFeatureController(Feature.REQUEST_SYSTEM);
        List<RequestDocument> documents = controller.getDocuments();
        RequestDocument document = findByName(documents, filename);
        if (document == null) {
            document = new RequestDocument();
            document.setName(filename);
            documents.add(document);
        }
        document.setContent(stream2blob(is));
    }

    private RequestDocument findByName(List<RequestDocument> documents, String name) {
        for (RequestDocument document : documents) {
            if (document.getName().equalsIgnoreCase(name)) {
                return document;
            }
        }
        return null;
    }

}
