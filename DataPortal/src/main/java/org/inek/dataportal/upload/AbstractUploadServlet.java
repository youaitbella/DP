package org.inek.dataportal.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.StreamUtils;

public abstract class AbstractUploadServlet extends HttpServlet {

    private static final Logger _logger = Logger.getLogger("UploadServlet");
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        HttpUtil httpUtil = new HttpUtil(request, response);
        try {
            if (httpUtil.isMultipartRequest()) {
                doMultipart(httpUtil);
            } else {
                doSingleUpload(httpUtil);
            }
            httpUtil.getResponse().setStatus(HttpServletResponse.SC_OK);
            httpUtil.writeStatus("{\"success\": true}");
        } catch (IOException | ServletException e) {
            _logger.log(Level.WARNING, "FileUploadServlet got Exception: {0}{1}", new Object[]{e.getMessage(), e.getStackTrace()[0].toString()});
            httpUtil.getResponse().setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpUtil.writeStatus("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    private void doSingleUpload(HttpUtil httpUtil) throws IOException {
        String filename = httpUtil.getRequest().getHeader("X-File-Name");
        try (InputStream is = httpUtil.getRequest().getInputStream()) {
            stream2Document(Utils.decodeUrl(filename), is, httpUtil);
        }
    }

    private void doMultipart(HttpUtil httpUtil) throws IOException, ServletException {
        Object[] parts = httpUtil.getRequest().getParts().toArray();

        for (int i = 0; i < parts.length; i++) {
            Part part = (Part) parts[i];
            Map<String, String> params = httpUtil.getParams(part);
            String filename = new File(params.get("filename")).getName();  // get rid of absolute path (some IE versions will yield the absoltute path)
            try (InputStream is = part.getInputStream()) {
                stream2Document(Utils.decodeUrl(filename), is, httpUtil);
            }
        }
    }

    abstract protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException;

    protected void showFileName(String filename, HttpUtil httpUtil) {
    }

    /**
     * copies an input stream into an array of bytes
     *
     * @param is
     * @return
     * @throws IOException
     */
    protected byte[] stream2blob(InputStream is) throws IOException {
        return StreamUtils.stream2blob(is);
    }

}
