package org.inek.dataportal.base.upload;

import org.inek.dataportal.common.upload.HttpUtil;
import org.inek.dataportal.common.upload.AbstractUploadServlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import org.inek.dataportal.api.helper.Const;
import org.inek.dataportal.common.data.account.entities.AccountDocument;
import org.inek.dataportal.base.feature.documents.DocumentUpload;
import org.inek.dataportal.common.scope.FeatureScopedContextHolder;
import org.inek.dataportal.common.utils.StreamUtils;

@WebServlet(urlPatterns = {"/upload/document"}, name = "DocumentUploadServlet")
@MultipartConfig(fileSizeThreshold = 16 * 1024 * 1024, maxFileSize = Integer.MAX_VALUE - 5)
public class DocumentUploadServlet extends AbstractUploadServlet {

    @Override
    protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException, IllegalArgumentException {
        HttpSession session = httpUtil.getRequest().getSession();
        @SuppressWarnings("unchecked") Map<String, FeatureScopedContextHolder.FeatureScopedInstance> map
                = (Map<String, FeatureScopedContextHolder.FeatureScopedInstance>) session.getAttribute("FeatureScoped");
        DocumentUpload docUpload = FeatureScopedContextHolder.Instance.getBean(DocumentUpload.class, map);

        List<AccountDocument> documents = docUpload.getDocuments();
        AccountDocument document = documents
                .stream()
                .filter(d -> d.getName().equals(filename))
                .findFirst()
                .orElse(new AccountDocument(filename));
        if (!documents.contains(document)) {
            documents.add(document);
        }
        document.setContent(StreamUtils.stream2blob(is, getInitialSize(httpUtil)));
    }

    private int getInitialSize(HttpUtil httpUtil) {
        long initialSize;
        try {
            initialSize = Long.parseLong(httpUtil.getRequest().getHeader("Content-Length"));
        } catch (NumberFormatException ex) {
            initialSize = Const.BUFFER_SIZE;
        }
        if (initialSize > Integer.MAX_VALUE - 8) {
            return Integer.MAX_VALUE - 8;
        }
        return (int) initialSize;
    }

}
