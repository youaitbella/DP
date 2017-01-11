package org.inek.dataportal.upload;

import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import org.inek.dataportal.controller.SessionController;

@WebServlet(urlPatterns = {"/upload/calcBasics"}, name = "CalcUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class CalcUploadServlet extends AbstractUploadServlet {

    @Inject SessionController _sessionController;

    @Override
    protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException {
        HttpSession session = httpUtil.getRequest().getSession();
        
        //todo
    }

}
