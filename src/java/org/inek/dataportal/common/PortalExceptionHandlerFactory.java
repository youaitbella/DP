/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 *
 * @author muellermi
 */
public class PortalExceptionHandlerFactory extends ExceptionHandlerFactory {

    private ExceptionHandlerFactory _parent;

    public PortalExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        _parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
     ExceptionHandler result = _parent.getExceptionHandler();
      result = new PortalExceptionHandler(result);
      return result;    }
}
