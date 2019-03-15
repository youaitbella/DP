package org.inek.dataportal.base.upload;

import org.inek.dataportal.api.helper.Const;
import org.inek.dataportal.base.feature.documents.DocumentUpload;
import org.inek.dataportal.common.data.account.iface.Document;
import org.inek.dataportal.common.scope.FeatureScopedContextHolder;
import org.inek.dataportal.common.upload.AbstractUploadServlet;
import org.inek.dataportal.common.upload.HttpUtil;
import org.inek.dataportal.common.utils.StreamUtils;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@WebServlet(urlPatterns = {"/upload/document"}, name = "DocumentUploadServlet")
@MultipartConfig(fileSizeThreshold = 16 * 1024 * 1024, maxFileSize = Integer.MAX_VALUE - 5)
public class DocumentUploadServlet extends AbstractUploadServlet {

    @Override
    protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException, IllegalArgumentException {
        HttpSession session = httpUtil.getRequest().getSession();
        @SuppressWarnings("unchecked") Map<String, FeatureScopedContextHolder.FeatureScopedInstance> map
                = (Map<String, FeatureScopedContextHolder.FeatureScopedInstance>) session.getAttribute("FeatureScoped");
        DocumentUpload docUpload = FeatureScopedContextHolder.Instance.getBean(DocumentUpload.class, map);

        Document document = docUpload.findOrCreateByName(filename);
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
