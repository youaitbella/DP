package org.inek.dataportal.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.AccountDocument;
import org.inek.dataportal.feature.documents.DocumentUpload;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.common.utils.StreamUtils;

@WebServlet(urlPatterns = {"/upload/document"}, name = "DocumentUploadServlet")
@MultipartConfig(fileSizeThreshold = 16 * 1024 * 1024, maxFileSize = Integer.MAX_VALUE - 5)
public class DocumentUploadServlet extends AbstractUploadServlet {

    @Inject private SessionController _sessionController;

    @Override
    protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException, IllegalArgumentException {
        HttpSession session = httpUtil.getRequest().getSession();
        @SuppressWarnings("unchecked") Map<String, FeatureScopedContextHolder.FeatureScopedInstance> map
                = (Map<String, FeatureScopedContextHolder.FeatureScopedInstance>) session.getAttribute("FeatureScoped");
        DocumentUpload docUpload = FeatureScopedContextHolder.Instance.getBean(DocumentUpload.class, map);

        List<AccountDocument> documents = docUpload.getDocuments();
        Optional<AccountDocument> optDoc = documents.stream().filter(d -> d.getName().equals(filename)).findFirst();
        AccountDocument document = optDoc.orElse(new AccountDocument(filename));
        if (!documents.contains(document)) {
            documents.add(document);
        }
        document.setContent(StreamUtils.stream2blob(is, getInitialSize(httpUtil)));
    }

    private int getInitialSize(HttpUtil httpUtil) {
        long initialSize;
        try{
            initialSize  = Long.parseLong(httpUtil.getRequest().getHeader("Content-Length"));
        } catch (NumberFormatException ex){
            initialSize = 8192;
        }
        if (initialSize > Integer.MAX_VALUE - 8){
            return Integer.MAX_VALUE - 8;
        }
        return (int)initialSize;
    }

}
