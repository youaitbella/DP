
package org.inek.dataportal.helper;

public class NotLoggedInException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotLoggedInException() {
        super("Not logged in.");
    }

    public NotLoggedInException(String message) {
        super(message);
    }

    public NotLoggedInException(Throwable cause) {
        super(cause);
    }

    public NotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }
}
