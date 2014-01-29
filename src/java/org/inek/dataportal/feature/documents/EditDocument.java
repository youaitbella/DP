/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.documents;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.entities.AccountDocument;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.AccountDocumentFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.feature.peppproposal.EditPeppProposal;

/**
 *
 * @author muellermi
 */
@Named
@ConversationScoped
public class EditDocument extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("EditNubProposal");
    
    @Inject
    private AccountDocumentFacade _accDocFacade;
    
    public String downloadDocument(int docId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        AccountDocument doc = _accDocFacade.find(docId);
        try {
            byte[] buffer = doc.getContent();
            externalContext.setResponseHeader("Content-Type", "text/plain");
            externalContext.setResponseHeader("Content-Length", "" + buffer.length);
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + doc.getName() + "\"");
            ByteArrayInputStream is = new ByteArrayInputStream(buffer);
            copyStream(is, externalContext.getResponseOutputStream());

        } catch (IOException ex) {
            Logger.getLogger(EditPeppProposal.class
                    .getName()).log(Level.SEVERE, null, ex);
            return Pages.Error.URL();
        }
        facesContext.responseComplete();
        doc.setRead(true);
        _accDocFacade.merge(doc);
        return "#";
    }

    private void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[8192];
        int n;
        while ((n = is.read(buffer)) != -1) {
            os.write(buffer);
        }
    }

    @Override
    protected void addTopics() {
        
    }
}
