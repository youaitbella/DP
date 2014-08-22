/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.faceletvalidators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.servlet.http.Part;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.mail.Mailer;

/**
 * Validates file
 *
 * @author muellermi
 */
@FacesValidator("FileUploadValidator")
public class FileUploadValidator implements Validator {

    @Inject private Mailer _mailer;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        Part part = (Part) value;

        String fileName = part.getSubmittedFileName();
        if (fileName.length() == 0) {
            String msg = Utils.getMessage("msgFileNameInvalid");
            throw new ValidatorException(new FacesMessage(msg));
        }

        if (fileName.length() > 250) {
            String msg = Utils.getMessage("msgFileNameLong");
            throw new ValidatorException(new FacesMessage(msg));
        }

        if (!checkFileName(component, fileName)) {
            String msg = Utils.getMessage("msgFileNameInvalid");
            throw new ValidatorException(new FacesMessage(msg));
        }

        if (!checkContentType(component, part.getContentType())) {
            String msg = Utils.getMessage("msgFileTypeInvalid");
            throw new ValidatorException(new FacesMessage(msg));
        }

        if (!checkMinFileSize(component, part.getSize())) {
            String msg = Utils.getMessage("msgFileSizeMin");
            throw new ValidatorException(new FacesMessage(msg));
        }

        if (!checkMaxFileSize(component, part.getSize())) {
            String msg = Utils.getMessage("msgFileSizeMax");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    private boolean checkFileName(UIComponent component, String fileName) {
        if (component.getAttributes().containsKey("fileNamePattern")) {
            return fileName.matches((String) component.getAttributes().get("fileNamePattern"));
        }
        return true;
    }

    private boolean checkContentType(UIComponent component, String contentType) {
        if (component.getAttributes().containsKey("contentType")) {
            return contentType.equals((String) component.getAttributes().get("contentType"));
        }
        if (component.getAttributes().containsKey("contentTypes")) {
            String[] contentTypes = ((String) component.getAttributes().get("contentTypes")).split(";");
            for (String type : contentTypes) {
                if (contentType.equals(type.trim())) {
                    return true;
                }
            }
            return false;
        }
        return contentType.equals("text/plain");
    }

    private boolean checkMinFileSize(UIComponent component, long size) {
        if (component.getAttributes().containsKey("minFileSize")) {
            String minSize = (String) component.getAttributes().get("minFileSize");
            try {
                return size <= Long.parseLong(minSize);
            } catch (NumberFormatException ex) {
                _mailer.sendWarning("FileUploadValidator:checkMinFileSize " + minSize, ex);
            }
        }
        return true;
    }

    private boolean checkMaxFileSize(UIComponent component, long size) {
        if (component.getAttributes().containsKey("maxFileSize")) {
            String maxSize = (String) component.getAttributes().get("maxFileSize");
            try {
                return size <= Long.parseLong(maxSize);
            } catch (NumberFormatException ex) {
                _mailer.sendWarning("FileUploadValidator:checkMaxFileSize " + maxSize, ex);
            }
        }
        return true;
    }

}
