package org.inek.dataportal.upload;

import org.inek.dataportal.common.upload.HttpUtil;
import org.inek.dataportal.common.upload.AbstractUploadServlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import org.inek.dataportal.entities.calc.drg.KGLDocument;
import org.inek.dataportal.feature.calculationhospital.EditCalcBasicsDrg;

@WebServlet(urlPatterns = {"/upload/calcBasics"}, name = "CalcUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class CalcUploadServlet extends AbstractUploadServlet {

    @Override
    protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException {
        HttpSession session = httpUtil.getRequest().getSession();
        Map map = (Map) session.getAttribute("com.sun.faces.application.view.activeViewMaps");
        EditCalcBasicsDrg editCalcBasics = null;
        for (Object entry : map.values()) {
            if (entry instanceof Map) {
                Map viewScopes = (Map) entry;
                if (viewScopes.containsKey("editCalcBasicsDrg")) {
                    editCalcBasics = (EditCalcBasicsDrg) viewScopes.get("editCalcBasicsDrg");
                    break;
                }
            }
        }
        if (editCalcBasics == null) {
            return;
        }
        List<KGLDocument> documents = editCalcBasics.getCalcBasics().getDocuments();
        KGLDocument document = documents.stream().filter(d -> d.getName().equalsIgnoreCase(filename)).findAny().orElse(null);
        if (document == null) {
            document = new KGLDocument();
            document.setName(filename);
            document.setBaseInformationId(editCalcBasics.getCalcBasics().getId());
            document.setSheetId(19);
            documents.add(document);
        }
        document.setContent(stream2blob(is));
    }

}
