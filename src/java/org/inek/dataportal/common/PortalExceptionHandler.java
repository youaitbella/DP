/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELException;
import javax.enterprise.context.NonexistentConversationException;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.mail.Mailer;

/**
 *
 * @author muellermi
 */
public class PortalExceptionHandler extends ExceptionHandlerWrapper {

    static final Logger _logger = Logger.getLogger(PortalExceptionHandler.class.getName());
    private ExceptionHandler _wrapped;

    PortalExceptionHandler(ExceptionHandler wrapped) {
        _wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return _wrapped;
    }

    @Override
    public void handle() throws FacesException {
        boolean isHandled = false;
        Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) {

            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable exception = context.getException();
            if (exception instanceof ViewExpiredException) {
                ViewExpiredException vee = (ViewExpiredException) exception;
                FacesContext fc = FacesContext.getCurrentInstance();
                Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
                NavigationHandler nav = fc.getApplication().getNavigationHandler();
                try {
                    if (!vee.getViewId().contains("Login.xhtml")) {
                        requestMap.put("currentViewId", vee.getViewId());
                        fc.getViewRoot().setViewId(Pages.SessionTimeout.URL());
                        nav.handleNavigation(fc, null, Pages.SessionTimeout.URL());
                        fc.renderResponse();
                    }
                } finally {
                    i.remove();
                }
            } else if (exception instanceof ELException || exception instanceof NonexistentConversationException) {
                // this might be result of a session time out
                FacesContext fc = FacesContext.getCurrentInstance();
                NavigationHandler nav = fc.getApplication().getNavigationHandler();
                try {
                    _logger.log(Level.SEVERE, "[PortalExceptionHandler] ", exception);
                    if (!isHandled) {
                        SessionController sc = Utils.getBean(SessionController.class);
                        if (sc != null) {
                            sc.logout();
                        }
                        try {
                            Mailer.sendMail("michael.mueller@inek-drg.de", "PortalExceptionHandler " + exception.getClass() , exception.getMessage());
                        } catch (MessagingException ex) {
                        }
                        String path = ((HttpServletRequest) fc.getExternalContext().getRequest()).getContextPath();
                        fc.getExternalContext().redirect(path + Pages.ErrorRedirector.URL());
//                        nav.handleNavigation(fc, null, Pages.SessionTimeout.URL());
//                        fc.renderResponse();
                        isHandled = true;
                    }
                } catch (IOException ex) {
                    _logger.log(Level.SEVERE, "[PortalExceptionHandler IOException] ", exception);
                } finally {
                    i.remove();
                }
            } else if (exception instanceof FacesException) {
                FacesContext fc = FacesContext.getCurrentInstance();
                NavigationHandler nav = fc.getApplication().getNavigationHandler();
                try {
                    _logger.log(Level.SEVERE, "[PortalExceptionHandler] ", exception);
                    //String path = ((HttpServletRequest) fc.getExternalContext().getRequest()).getContextPath();
                    //fc.getExternalContext().redirect(path + Pages.ErrorRedirector.URL());
                    SessionController sc = Utils.getBean(SessionController.class);
                    if (sc != null) {
                        sc.logout();
                    }
                    try {
                        Mailer.sendMail("michael.mueller@inek-drg.de", "PortalExceptionHandler FacesException", exception.getMessage());
                    } catch (MessagingException ex) {
                    }
                    nav.handleNavigation(fc, null, Pages.Error.URL());
                    fc.renderResponse();
//                } catch (IOException ex) {
//                    _logger.log(Level.SEVERE, "[PortalExceptionHandler IOException] ", t);
                } finally {
                    i.remove();
                }
            } else {
                    try {
                        Mailer.sendMail("michael.mueller@inek-drg.de", "PortalExceptionHandler OtherException", exception.getMessage());
                    } catch (MessagingException ex) {
                    }
            }
        }
        getWrapped().handle();
    }
}
