package org.inek.dataportal.entities.modelintention;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;
import org.inek.dataportal.feature.modelintention.EditModelIntention;

@Model
public class UploadBean {
@Inject EditModelIntention _modelIntention;
    private static final Logger _logger = Logger.getLogger("UploadBean");
    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    public void upload() {
        _logger.log(Level.WARNING, "upload");
        try {
            if (_file != null) {
                Scanner scanner = new Scanner(_file.getInputStream(),
                        "UTF-8");
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    _logger.log(Level.WARNING, "\t found: {0}", line);
                    _modelIntention.addCost(line);
                }

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Upload successfully"));
            }
        } catch (IOException | NoSuchElementException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Upload failed!"));
        }
    }
    

}
