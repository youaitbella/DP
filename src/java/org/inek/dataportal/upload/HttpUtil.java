package org.inek.dataportal.upload;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author muellermi
 */
public class HttpUtil {

    private static final Logger _logger = Logger.getLogger("HttpUtil");
    private final HttpServletRequest _request;
    private final HttpServletResponse _response;

    public HttpUtil(HttpServletRequest request, HttpServletResponse response) {
        _request = request;
        _response = response;
    }

    public boolean isMultipartRequest() {
        return getRequest().getMethod().toLowerCase().equals("post")
                && getRequest().getContentType() != null
                && getRequest().getContentType().toLowerCase().startsWith("multipart/");
    }

    public Map<String, String> getParams(Part part) throws UnsupportedEncodingException {
        String charset = "UTF-8";
        Map<String, String> params = new HashMap<>();
        for (String paramExpression : part.getHeader("content-disposition").split(";")) {
            if (!paramExpression.contains("=")) {
                continue;
            }
            String[] tokens = paramExpression.split("=");
            String name = tokens[0].trim();
            String value = URLDecoder.decode(tokens[1].trim().replace("\"", ""), charset);
            params.put(name, value);
        }
        return params;
    }

    public String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    public void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[8192];
        int n;
        while ((n = is.read(buffer)) != -1) {
            os.write(buffer, 0, n);
        }
    }

    public void writeStatus(String status) {
        try {
            try (PrintWriter pw = getResponse().getWriter()) {
                pw.write(status);
                pw.flush();
            }
        } catch (IOException e) {
            _logger.log(Level.SEVERE, "Could not retrieve a print writer", e);
        }
    }

    public HttpServletRequest getRequest() {
        return _request;
    }

    public HttpServletResponse getResponse() {
        return _response;
    }
}
