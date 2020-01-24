package org.inek.dataportal.psy.nub.bo;

import org.inek.dataportal.psy.nub.entities.PsyNubRequest;

import java.util.Optional;

public class UploadedTemplate {

    private static final String NO_ERRORS = " (Ok)";
    private static final String ERRORS = " (Fehler in der Formatprüfung{0})";

    private PsyNubRequest _request;
    private Boolean _hasError = false;
    private String _fileName = "";
    private String _message;

    public UploadedTemplate(String fileName, Optional<PsyNubRequest> request, String message) {
        _fileName = fileName;
        _message = "" + message;
        if (request.isPresent()) {
            _request = request.get();
        } else {
            _hasError = true;
        }
    }

    public PsyNubRequest getRequest() {
        return _request;
    }

    public Boolean getHasError() {
        return _hasError;
    }

    public String getDisplayName() {
        String message = "".equals(_message) ? "" : ": " + _message;
        return _fileName + (_hasError ? ERRORS.replace("{0}", message) : NO_ERRORS);
    }
}
