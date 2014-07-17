/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common;

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
        Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        if (!i.hasNext()) {
            getWrapped().handle();
            return;
        }

        FacesContext fc = FacesContext.getCurrentInstance();
        StringBuilder messageCollector = new StringBuilder();
        String targetPage = "";

        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable exception = context.getException();
            if (exception instanceof ViewExpiredException) {
                ViewExpiredException viewExpiredExeption = (ViewExpiredException) exception;
                Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
                if (!viewExpiredExeption.getViewId().contains("Login.xhtml")) {
                    targetPage = Pages.SessionTimeout.RedirectURL();
                    requestMap.put("currentViewId", viewExpiredExeption.getViewId());
                }
            } else if (exception instanceof NonexistentConversationException) {
                String head = "[PortalExceptionHandler NonexistentConversationException] ";
                _logger.log(Level.SEVERE, head, exception);
                collectException(messageCollector, head, exception);
                if (targetPage.isEmpty()) {
                    targetPage = Pages.InvalidConversation.RedirectURL();
                }
            } else if (exception instanceof ELException) {
                String head = "[PortalExceptionHandler ELException] ";
                _logger.log(Level.SEVERE, head, exception);
                collectException(messageCollector, head, exception);
                if (targetPage.isEmpty()) {
                    targetPage = Pages.ErrorRedirector.RedirectURL();
                }
            } else if (exception instanceof FacesException) {
                String head = "[PortalExceptionHandler FacesException] ";
                _logger.log(Level.SEVERE, head, exception);
                collectException(messageCollector, head, exception);
                if (targetPage.isEmpty()) {
                    targetPage = Pages.Error.RedirectURL();
                }
            } else {
                String msg = exception.getMessage();
                if (msg == null || !msg.contains("Conversation lock timed out")) {
                    String head = "[PortalExceptionHandler OtherException] ";
                    _logger.log(Level.SEVERE, head, exception);
                    collectException(messageCollector, head, exception);
                }
            }
            i.remove();
        }
        SendExeptionMessage(messageCollector.toString());
        SessionController sc = Utils.getBean(SessionController.class);
        if (sc != null) {
            sc.logout();
        }
        NavigationHandler nav = fc.getApplication().getNavigationHandler();
        //String path = ((HttpServletRequest) fc.getExternalContext().getRequest()).getContextPath();
        //fc.getExternalContext().redirect(path + targetPage);
        //fc.getViewRoot().setViewId(targetPage);
        nav.handleNavigation(fc, null, targetPage);
        fc.renderResponse();
        getWrapped().handle();
    }

    private void collectException(StringBuilder collector, String head, Throwable exception) {
        if (collector.length() > 0) {
            collector.append("\r\n\r\n--------------------------------\r\n\r\n");
        }
        collector.append(head).append("\r\n");
        collector.append(exception.getMessage()).append("\r\n");
        for (StackTraceElement element : exception.getStackTrace()) {
            collector.append(element.toString()).append("\r\n");
        }
    }

    @Inject Mailer _mailer;
    private void SendExeptionMessage(String msg) {
        String name = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
        String subject = "Exception reported by Server " + name;
        _mailer.sendMail(PropertyManager.INSTANCE.getProperty(PropertyKey.ExceptionEmail), subject, msg);
    }

}
