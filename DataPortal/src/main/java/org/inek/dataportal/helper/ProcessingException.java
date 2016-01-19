/*
 * Copyright 2005 InEK gGmbH  
 * All Rights Reserved.
 *
 * $RCSfile: ProcessingException.java,v $
 * $Revision: 1.1 $
 * $Author: sj $
 * $Date: 2005/04/11 13:42:49 $
 * 
 */
package org.inek.dataportal.helper;

/**
 * @author Stephan Jacobi
 *
 * Exception wrapper.
 */
public class ProcessingException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public ProcessingException() {
        super();
    }

    /**
     * @param message
     */
    public ProcessingException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ProcessingException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
