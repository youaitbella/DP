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
import org.inek.dataportal.entities.nub.NubRequestDocument;
import org.inek.dataportal.feature.nub.EditNubRequest;
import org.inek.dataportal.common.scope.FeatureScopedContextHolder;

@WebServlet(urlPatterns = {"/upload/nubrequest"}, name = "NubRequestUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class NubRequestUploadServlet extends AbstractUploadServlet {

    @Inject private SessionController _sessionController;

    @Override
    protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException {
        HttpSession session = httpUtil.getRequest().getSession();
        @SuppressWarnings("unchecked") Map<String, FeatureScopedContextHolder.FeatureScopedInstance> map
                = (Map<String, FeatureScopedContextHolder.FeatureScopedInstance>) session.getAttribute("FeatureScoped");
        EditNubRequest editNub = FeatureScopedContextHolder.Instance.getBean(EditNubRequest.class, map);

        List<NubRequestDocument> documents = editNub.getNubRequest().getDocuments();
        NubRequestDocument document = editNub.findDocumentByName(filename);
        if (document == null) {
            document = new NubRequestDocument();
            document.setName(filename);
            document.setNubRequestId(editNub.getNubRequest().getId());
            documents.add(document);
        }
        document.setContent(stream2blob(is));
    }

}
