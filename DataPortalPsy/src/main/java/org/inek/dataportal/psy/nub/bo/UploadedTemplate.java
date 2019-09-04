package org.inek.dataportal.psy.nub.bo;

import org.inek.dataportal.psy.nub.entities.PsyNubRequest;

import java.util.Optional;

public class UploadedTemplate {

    private static final String NO_ERRORS = " (Ok)";
    private static final String ERRORS = " (Fehler in der Formatprüfung)";

    private PsyNubRequest _request;
    private Boolean _hasError = false;
    private String _fileName = "";

    public UploadedTemplate(String fileName, Optional<PsyNubRequest> request) {
        _fileName = fileName;
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
        return _fileName + (_hasError ? ERRORS : NO_ERRORS);
    }
}
