package org.inek.dataportal.entities.modelintention;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.feature.modelintention.EditModelIntention;

@Named
@RequestScoped
public class ModelIntentionUpload {

    @Inject EditModelIntention _modelIntention;
    private static final Logger _logger = Logger.getLogger("ModelIntentionUpload");
    private Part _file;
    
    public Part getFile() {
        return _file;
    }
    
    public void setFile(Part file) {
        _file = file;
    }
    
    public void uploadCosts() {
        _logger.log(Level.WARNING, "uploadCosts");
        try {
            if (_file != null) {
                Scanner scanner = new Scanner(_file.getInputStream(),
                        "UTF-8");
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    _logger.log(Level.WARNING, "\t found: {0}", line);
                    addCost(line);
                }
                
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Upload successfully"));
            }
        } catch (IOException | NoSuchElementException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Upload failed!"));
        }
    }
    
    public void addCost(String line){
        // todo: this is just quick'n'dirty to evaluate the function
        String[] tokens = line.split(";");
        if (tokens.length == 5){
            Cost cost = new Cost();
            try {
                cost.setIk(Integer.parseInt(tokens[0]));
            } catch (NumberFormatException ex){}
            cost.setRemunerationCode(tokens[1]); 
            try {
                cost.setCostCenterId(Integer.parseInt(tokens[2]));
            } catch (NumberFormatException ex){}
            cost.setCostTypeId(tokens[3]);
            try {
                cost.setAmount(new BigDecimal(tokens[4].replace(",", ".")));
            } catch (NumberFormatException ex){}
            _modelIntention.addCost(cost);
        }
    }
    
}
