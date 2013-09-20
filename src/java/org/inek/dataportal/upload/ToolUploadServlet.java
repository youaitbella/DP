package org.inek.dataportal.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.inek.dataportal.backingbeans.SessionTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.DropBox;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.DropBoxFacade;
import org.inek.dataportal.feature.dropbox.DropBoxController;
import org.inek.dataportal.helper.ProcessingException;
import org.inek.dataportal.utils.PropertyKey;
import org.inek.dataportal.utils.PropertyManager;

@WebServlet(urlPatterns = {"/upload/tool"}, name = "ToolUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class ToolUploadServlet extends HttpServlet {

    private static final Logger _logger = Logger.getLogger("ToolUploadServlet");
    private static final long serialVersionUID = 1L;
    @Inject private SessionController _sessionController;
    @Inject private SessionTools _sessionTools;
    @Inject private DropBoxFacade _dropBoxFacade;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        HttpUtil httpUtil = new HttpUtil(request, response);
        try {
            if (httpUtil.isMultipartRequest()) {
                doMultipart(httpUtil);
            } else {
                String action = request.getParameter("action");
                switch (action) {
                    case "login":
                        doLogin(httpUtil);
                        break;
                    case "clientVersion":
                        getClientVersion(httpUtil);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException | ServletException | IllegalArgumentException | ProcessingException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void doLogin(HttpUtil httpUtil) throws IOException {
        String emailOrUser = httpUtil.getParameter("name");
        String password = httpUtil.getParameter("scribble");
        if (!loginAndResponseFailure(emailOrUser, password, httpUtil)) {
            return;
        }
        httpUtil.writeStatus("success");
    }

    private void getClientVersion(HttpUtil httpUtil) {
        httpUtil.writeStatus(PropertyManager.INSTANCE.getProperty(PropertyKey.ClientVersion));
    }

    private boolean loginAndResponseFailure(String emailOrUser, String password, HttpUtil httpUtil) throws IOException {
        if (!_sessionController.login(emailOrUser, password)) {
            httpUtil.writeStatus("fail");
            return false;
        }
        try {
            _sessionController.getFeatureController(Feature.DROPBOX);
        } catch (IllegalArgumentException e) {
            httpUtil.writeStatus("no dropbox");
            return false;
        }
        return true;
    }

    private void doMultipart(HttpUtil httpUtil) throws IOException, ServletException, ProcessingException {
        Object[] parts = httpUtil.getRequest().getParts().toArray();

        if (parts.length != 1) {
            throw new IllegalArgumentException("Part error");
        }
        Part part = (Part) parts[0];
        Map<String, String> params = httpUtil.getParams(part);

        if (!loginAndResponseFailure(params.get("name"), params.get("scribble"), httpUtil)) {
            return;
        }
        String filename = new File(params.get("filename")).getName();  // get rid of absolute path (some IE versions will yield the absoltute path)
        int ik = Integer.parseInt(params.get("ik"));
        DropBox dropBox = createDropBox(filename, ik);
        File dir = ((DropBoxController) _sessionController.getFeatureController(Feature.DROPBOX)).getUploadDir(dropBox);
        dir.mkdirs();
        try (InputStream is = part.getInputStream();
                FileOutputStream fos = new FileOutputStream(new File(dir, filename))) {
            httpUtil.copyStream(is, fos);
            fos.flush();
        }
        ((DropBoxController) _sessionController.getFeatureController(Feature.DROPBOX)).sealDropBox(_dropBoxFacade, dropBox);
        httpUtil.getResponse().setStatus(HttpServletResponse.SC_OK);
        httpUtil.writeStatus("success");
    }

    private DropBox createDropBox(String filename, int ik) {
        DropBox dropBox = new DropBox();
        dropBox.setAccountId(_sessionController.getAccount().getAccountId());
        dropBox.setDirectory("");
        dropBox.setDescription("DataTool: " + filename);
        int typeId = PropertyManager.INSTANCE.getPropertyAsInt(PropertyKey.DropBoxTypeId);
        dropBox.setDropboxType(_sessionTools.getDropBoxType(typeId));
        dropBox.setIK(ik);
        return _dropBoxFacade.createDropBox(dropBox);
    }
}
