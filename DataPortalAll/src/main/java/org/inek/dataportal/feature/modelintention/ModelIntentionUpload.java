package org.inek.dataportal.feature.modelintention;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.entities.modelintention.Cost;
import org.inek.dataportal.entities.modelintention.ModelIntentionContact;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScopedContextHolder;
import org.inek.dataportal.common.utils.StringUtil;
import org.inek.dataportal.utils.ValueLists;

@Named
@RequestScoped
public class ModelIntentionUpload {

    private EditModelIntention _modelIntention;
    @Inject private ValueLists _valueLists;
    private static final Logger LOGGER = Logger.getLogger("ModelIntentionUpload");
    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    /**
     * @return the _modelIntention
     */
    public EditModelIntention getModelIntention() {
        if (_modelIntention == null) {
            _modelIntention = (EditModelIntention) FeatureScopedContextHolder.Instance.getBean(EditModelIntention.class);
        }
        return _modelIntention;
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
                getModelIntention().getCostTable().setMessage(String.format(Utils.getMessage("msgLinesUploaded"), countSuccess, countFail));

                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Upload successfully"));
            }
        } catch (IOException | NoSuchElementException e) {
            getModelIntention().getCostTable().setMessage(Utils.getMessage("msgUploadFailed"));
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Upload failed!"));
        }
    }

    public boolean tryAddCost(String line) {
        String[] tokens = StringUtil.splitAtUnquotedSemicolon(line);
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
            return getModelIntention().tryAddCost(cost);
        }
        return false;
    }

    public void uploadContact() {
        try {
            if (_file != null) {
                Scanner scanner = new Scanner(_file.getInputStream(),
                        "UTF-8");
                int countSuccess = 0;
                int countFail = 0;
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (tryAddContact(line)) {
                        countSuccess++;
                    } else {
                        countFail++;
                    }
                }
                getModelIntention().setContactMessage(String.format(Utils.getMessage("msgLinesUploaded"), countSuccess, countFail));
            }
        } catch (IOException | NoSuchElementException e) {
            getModelIntention().setContactMessage(Utils.getMessage("msgUploadFailed"));
        }
    }

    private boolean tryAddContact(String line) {
        // todo: compose and show message to user
        String[] tokens = StringUtil.splitAtUnquotedSemicolon(line);
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
                return false;
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
                contact.setRegCare(tokens[6].equals("1"));
            } catch (NumberFormatException ex) {
            }
            contact.setContactPerson(tokens[7]);
            contact.setPhone(tokens[8]);
            contact.setEmail(tokens[9]);

            return getModelIntention().tryAddContact(contact);
        }
        return false;
    }

}
