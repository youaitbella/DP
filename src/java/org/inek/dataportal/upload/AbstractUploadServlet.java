package org.inek.dataportal.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

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
            _logger.log(Level.WARNING, "FileUploadServlet got Exception", e);
            httpUtil.getResponse().setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpUtil.writeStatus("{\"success\": false, \"message\": \"" + e.getMessage() +  "\"}");
        }
    }

    private void doSingleUpload(HttpUtil httpUtil) throws IOException {
        String filename = httpUtil.getRequest().getHeader("X-File-Name");
        try (InputStream is = httpUtil.getRequest().getInputStream()) {
            stream2Document(decodeFilename(filename), is);
        }
    }

    private void doMultipart(HttpUtil httpUtil) throws IOException, ServletException {
        Object[] parts = httpUtil.getRequest().getParts().toArray();

        for (int i = 0; i < parts.length; i++) {
            Part part = (Part) parts[i];
            Map<String, String> params = httpUtil.getParams(part);
            String filename = new File(params.get("filename")).getName();  // get rid of absolute path (some IE versions will yield the absoltute path)
            try (InputStream is = part.getInputStream()) {
                stream2Document(decodeFilename(filename), is);
            }
        }
    }

    public String decodeFilename(String filename){
        return filename.replace("%20", " ").replace("%5B", "[").replace("%5D", "]");
    }
    
    abstract protected void stream2Document(String filename, InputStream is) throws IOException;

    protected void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[8192];
        int n;
        while ((n = is.read(buffer)) != -1) {
            os.write(buffer, 0, n);
        }
    }

    /**
     * copies an input stream into an array of bytes
     * @param is
     * @return
     * @throws IOException 
     */
    protected byte[] stream2blob(InputStream is) throws IOException {
        byte[] buffer = new byte[8192];
        byte[] tempBlob = new byte[8192];
        int n;
        int offset = 0;
        while ((n = is.read(buffer)) >= 0) {
            if (offset + n > tempBlob.length) {
                byte[] newTemp = new byte[tempBlob.length * 2];
                System.arraycopy(tempBlob, 0, newTemp, 0, offset);
                tempBlob = newTemp;
            }
            System.arraycopy(buffer, 0, tempBlob, offset, n);
            offset += n;
        }

        byte[] blob;
        if (offset == tempBlob.length) {
            blob = tempBlob;
        } else {
            blob = new byte[offset];
            System.arraycopy(tempBlob, 0, blob, 0, offset);
        }
        return blob;
    }
}
