package org.inek.dataportal.common.overall;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * This class decorates (wrapps) the standard exception handler factory
 *
 * @author muellermi
 */
public class PortalExceptionHandlerFactory extends ExceptionHandlerFactory {

    private final ExceptionHandlerFactory _wrapped;

    public PortalExceptionHandlerFactory(ExceptionHandlerFactory wrapped) {
        _wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new PortalExceptionHandler(_wrapped.getExceptionHandler());
    }

    @Override
    public ExceptionHandlerFactory getWrapped() {
        return _wrapped;
    }

}
