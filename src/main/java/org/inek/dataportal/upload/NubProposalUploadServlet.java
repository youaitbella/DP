package org.inek.dataportal.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.NubProposalDocument;
import org.inek.dataportal.feature.nub.EditNubProposal;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;

@WebServlet(urlPatterns = {"/upload/nubproposal"}, name = "NubProposalUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class NubProposalUploadServlet extends AbstractUploadServlet {

    @Inject SessionController _sessionController;

    @Override
    protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException {
        HttpSession session = httpUtil.getRequest().getSession();
        Map<String, FeatureScopedContextHolder.FeatureScopedInstance> map
                = (Map<String, FeatureScopedContextHolder.FeatureScopedInstance>) session.getAttribute("FeatureScoped");
        EditNubProposal editNub = FeatureScopedContextHolder.Instance.getBean(EditNubProposal.class, map);

        List<NubProposalDocument> documents = editNub.getNubProposal().getDocuments();
        NubProposalDocument document = editNub.findDocumentByName(filename);
        if (document == null) {
            document = new NubProposalDocument();
            document.setName(filename);
            documents.add(document);
        }
        document.setContent(stream2blob(is));
    }

}
