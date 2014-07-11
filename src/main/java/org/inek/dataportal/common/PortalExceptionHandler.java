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
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.mail.Mailer;
import org.inek.dataportal.utils.PropertyKey;
import org.inek.dataportal.utils.PropertyManager;

/**
 *
 * @author muellermi
 */
public class PortalExceptionHandler extends ExceptionHandlerWrapper {

    static final Logger _logger = Logger.getLogger(PortalExceptionHandler.class.getName());
    private final ExceptionHandler _wrapped;

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
            } else if (exception instanceof NonexistentConversationException) {
                FacesContext fc = FacesContext.getCurrentInstance();
                NavigationHandler nav = fc.getApplication().getNavigationHandler();
                try {
                    _logger.log(Level.SEVERE, "[PortalExceptionHandler] ", exception);
                    if (!isHandled) {
                        SessionController sc = Utils.getBean(SessionController.class);
                        if (sc != null) {
                            sc.logout();
                        }
                        String path = ((HttpServletRequest) fc.getExternalContext().getRequest()).getContextPath();
                        fc.getExternalContext().redirect(path + Pages.InvalidConversation.URL());
//                        nav.handleNavigation(fc, null, Pages.SessionTimeout.URL());
//                        fc.renderResponse();
                        isHandled = true;
                    }
                } catch (IOException ex) {
                    _logger.log(Level.SEVERE, "[PortalExceptionHandler IOException] ", exception);
                } finally {
                    i.remove();
                }
            } else if (exception instanceof ELException) {
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
                        SendExeptionMessage("PortalExceptionHandler " + exception.getClass(), exception);
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
                    SendExeptionMessage("PortalExceptionHandler FacesException", exception);
                    nav.handleNavigation(fc, null, Pages.Error.URL());
                    fc.renderResponse();
//                } catch (IOException ex) {
//                    _logger.log(Level.SEVERE, "[PortalExceptionHandler IOException] ", t);
                } finally {
                    i.remove();
                }
            } else {
                FacesContext fc = FacesContext.getCurrentInstance();
                String msg = exception.getMessage();
                if (msg == null || !msg.contains("Conversation lock timed out")) {
                    SendExeptionMessage("PortalExceptionHandler OtherException", exception);
                }
                try {
                    String path = ((HttpServletRequest) fc.getExternalContext().getRequest()).getContextPath();
                    fc.getExternalContext().redirect(path + Pages.Error.URL());
                } catch (IOException ex) {
                    _logger.log(Level.SEVERE, "[PortalExceptionHandler IOException] ", exception);
                } finally {
                    i.remove();
                }
            }
        }
        getWrapped().handle();
    }

    @Inject Mailer _mailer;
    private void SendExeptionMessage(String subject, Throwable exception) {
        String name = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
        String msg = "Server: " + name + "\r\n" + exception.getMessage() + "\r\n";
        for (StackTraceElement element : exception.getStackTrace()) {
            msg += element.toString() + "\r\n";
        }
        _mailer.sendMail(PropertyManager.INSTANCE.getProperty(PropertyKey.ExceptionEmail), subject, msg);
    }

}
