package org.inek.dataportal.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.feature.psychstaff.backingbean.EditPsyStaff;

@WebServlet(urlPatterns = {"/upload/psychstaff"}, name = "PsychStaffUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class PsychStaffUploadServlet extends AbstractUploadServlet {

    @Inject private SessionController _sessionController;

    @Override
    protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException {
        HttpSession session = httpUtil.getRequest().getSession();
        Map map = (Map) session.getAttribute("com.sun.faces.application.view.activeViewMaps");
        EditPsyStaff editPsychStaff = null;
        for (Object entry : map.values()) {
            if (entry instanceof Map) {
                Map viewScopes = (Map) entry;
                if (viewScopes.containsKey("editPsyStaff")) {
                    editPsychStaff = (EditPsyStaff) viewScopes.get("editPsyStaff");
                    editPsychStaff.putDocument(filename, stream2blob(is));
                    break;
                }
            }
        }
    }

}
