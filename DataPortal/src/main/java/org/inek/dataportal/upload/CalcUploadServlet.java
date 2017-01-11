package org.inek.dataportal.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.calc.KGLDocument;
import org.inek.dataportal.feature.calculationhospital.EditCalcBasicsDrg;

@WebServlet(urlPatterns = {"/upload/calcBasics"}, name = "CalcUploadServlet")
@MultipartConfig(fileSizeThreshold = 10 * 1024 * 1024)
public class CalcUploadServlet extends AbstractUploadServlet {

    @Inject SessionController _sessionController;

    @Override
    protected void stream2Document(String filename, InputStream is, HttpUtil httpUtil) throws IOException {
        
        EditCalcBasicsDrg editCalcBasics = _sessionController.findBean("EditCalcBasicsDrg");

        List<KGLDocument> documents = editCalcBasics.getCalcBasics().getDocuments();
        KGLDocument document = documents.stream().filter(d -> d.getName().equalsIgnoreCase(filename)).findAny().orElse(null);
        if (document == null) {
            document = new KGLDocument();
            document.setName(filename);
            document.setCalcBasicsId(editCalcBasics.getCalcBasics().getId());
            documents.add(document);
        }
        document.setContent(stream2blob(is));
    }

}
