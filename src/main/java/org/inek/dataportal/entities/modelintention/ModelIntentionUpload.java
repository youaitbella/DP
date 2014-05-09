package org.inek.dataportal.entities.modelintention;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.feature.modelintention.EditModelIntention;
import org.inek.dataportal.utils.ValueLists;

@Named
@RequestScoped
public class ModelIntentionUpload {

    @Inject EditModelIntention _modelIntention;
    @Inject ValueLists _valueLists;
    private static final Logger _logger = Logger.getLogger("ModelIntentionUpload");
    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    public void uploadCosts() {
        try {
            if (_file != null) {
                Scanner scanner = new Scanner(_file.getInputStream(),
                        "UTF-8");
                int countSuccess = 0;
                int countFail = 0;
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (tryAddCost(line)) {
                        countSuccess++;
                    } else {
                        countFail++;
                    }
                }
                _modelIntention.getCostTable().setMessage(countSuccess + " Zeilen zugefügt, " + countFail + " Zeilen (Fehler oder Duplikate) nicht zugefügt.");

                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Upload successfully"));
            }
        } catch (IOException | NoSuchElementException e) {
            _modelIntention.getCostTable().setMessage("Upload fehlgeschlagen.");
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Upload failed!"));
        }
    }

    public boolean tryAddCost(String line) {
        String[] tokens = line.split(";");
        if (tokens.length == 5) {
            Cost cost = new Cost();
            try {
                cost.setIk(Integer.parseInt(tokens[0]));
            } catch (NumberFormatException ex) {
            }
            cost.setRemunerationCode(tokens[1]);
            cost.setCostCenterId(_valueLists.getCostCenterId(tokens[2]));
            cost.setCostTypeId(_valueLists.getCostTypeId(tokens[3]));
            try {
                cost.setAmount(new BigDecimal(tokens[4].replace(",", ".")));
            } catch (NumberFormatException ex) {
            }
            return !_modelIntention.tryAddCost(cost);
        }
        return false;
    }

    public void uploadContact() {
        try {
            if (_file != null) {
                Scanner scanner = new Scanner(_file.getInputStream(),
                        "UTF-8");
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    addContact(line);
                }

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Upload successfully"));
            }
        } catch (IOException | NoSuchElementException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Upload failed!"));
        }
    }

    private void addContact(String line) {
        // todo: compose and show message to user
        String[] tokens = line.split(";");
        if (tokens.length == 10) {
            ModelIntentionContact contact = new ModelIntentionContact();
            try {
                int id = Integer.parseInt(tokens[0]);
                if (id < 1 || id > 3) {
                    throw new NumberFormatException();
                }
                contact.setContactTypeId(id);
            } catch (NumberFormatException ex) {
                // without a valid id, there is nothing to add
                return;
            }
            try {
                contact.setIk(Integer.parseInt(tokens[1]));
            } catch (NumberFormatException ex) {
            }
            contact.setName(tokens[2]);
            contact.setStreet(tokens[3]);
            contact.setZip(tokens[4]);
            contact.setTown(tokens[5]);
            try {
                contact.setRegCare(Integer.parseInt(tokens[6]));
            } catch (NumberFormatException ex) {
            }
            contact.setContactPerson(tokens[7]);
            contact.setPhone(tokens[8]);
            contact.setEmail(tokens[9]);

            _modelIntention.addContact(contact);
        }
    }

}
