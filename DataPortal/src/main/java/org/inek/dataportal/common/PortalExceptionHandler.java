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
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.NotLoggedInException;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.mail.Mailer;
import org.jboss.weld.exceptions.WeldException;

/**
 *
 * @author muellermi
 */
public class PortalExceptionHandler extends ExceptionHandlerWrapper {

    static final Logger _logger = Logger.getLogger(PortalExceptionHandler.class.getName());
    private final ExceptionHandler _wrapped;
    @Inject private Mailer _mailer;
    @Inject private SessionController _sessionController;

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
                _logger.log(Level.SEVERE, "[View expired]", exception.getMessage());
                ViewExpiredException viewExpiredExeption = (ViewExpiredException) exception;
                Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
                if (!viewExpiredExeption.getViewId().contains("Login.xhtml")) {
                    targetPage = Pages.SessionTimeout.RedirectURL();
                    requestMap.put("currentViewId", viewExpiredExeption.getViewId());
                }
            } else if (exception instanceof NotLoggedInException) {
                _logger.log(Level.SEVERE, "[Not logged in]", exception.getMessage());
                if (targetPage.isEmpty()) {
                    targetPage = Pages.SessionTimeout.RedirectURL();
                }
            } else if (exception instanceof NonexistentConversationException || exception instanceof WeldException // todo: exception instanceof WeldException is fine in direct window, but does not work here.
                    || exception.getClass().toString().equals("class org.jboss.weld.exceptions.WeldException") // check for exception's name as workarround
                    || exception instanceof FacesException && exception.getMessage() != null && exception.getMessage().contains("WELD-000049:")) {
                String head = "[PortalExceptionHandler NonexistentConversationException] ";
                _logger.log(Level.SEVERE, head, exception);
                // we don't like to get this reported: collectException(messageCollector, head, exception);
                if (targetPage.isEmpty()) {
                    targetPage = Pages.InvalidConversation.RedirectURL();
                }
            } else if (exception instanceof ELException && !exception.getMessage().toLowerCase().contains("not logged in")) {
                String head = "[PortalExceptionHandler ELException] ";
                _logger.log(Level.SEVERE, head, exception);
                collectException(messageCollector, head, exception);
                if (targetPage.isEmpty()) {
                    targetPage = Pages.Error.RedirectURL();
                }
            } else if (exception instanceof FacesException) {
                String head = "[PortalExceptionHandler FacesException] ";
                _logger.log(Level.SEVERE, head, exception);
                collectException(messageCollector, head, exception);
                if (targetPage.isEmpty()) {
                    if (exception.getMessage().contains("javax.ejb.EJBException")) {
                        targetPage = Pages.DataError.RedirectURL();
                    } else {
                        targetPage = Pages.Error.RedirectURL();
                    }
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
        if (messageCollector.length() > 0) {
            collectUrlInformation(messageCollector);
        }
        SendExeptionMessage(messageCollector.toString());
//        SessionController sc = Utils.getBean(SessionController.class);
//        if (sc != null) {
//            sc.logout("Logout due to error");
//        }
        Utils.navigate(targetPage);
        getWrapped().handle();
    }

    private void collectException(StringBuilder collector, String head, Throwable exception) {
        collectException(collector, head, exception, 0);
    }

    private void collectException(StringBuilder collector, String head, Throwable exception, int level) {
        if (collector.length() > 0) {
            collector.append("\r\n\r\n--------------------------------\r\n");
        }
        collector.append("Level: ").append(level).append("\r\n\r\n");
        collector.append(head).append("\r\n\r\n");
        collector.append(exception.getMessage()).append("\r\n\r\n");
        for (StackTraceElement element : exception.getStackTrace()) {
            collector.append(element.toString()).append("\r\n");
        }
        Throwable cause = exception.getCause();
        if (cause != null && level < 9) {
            collectException(collector, head, cause, level + 1);
        }
    }

    private void collectUrlInformation(StringBuilder collector) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String url = request.getRequestURL().toString();
            if (collector.length() > 0) {
                collector.append("\r\n\r\n--------------------------------\r\n\r\n");
            }
            collector.append("URL ").append(url).append(request.getQueryString()).append("\r\n\r\n");

            String viewId = context.getViewRoot().getViewId();
            if (collector.length() > 0) {
                collector.append("\r\n\r\n--------------------------------\r\n\r\n");
            }
            collector.append("ViewId ").append(viewId).append("\r\n\r\n");
            collector.append("ClientIP: " + Utils.getClientIP() + "\r\n");
            if (_sessionController != null && _sessionController.isLoggedIn()) {
                collector.append("AccountId: " + _sessionController.getAccount() + "\r\n");
            }
        } catch (Exception ex) {
            if (collector.length() > 0) {
                collector.append("\r\n\r\n--------------------------------\r\n\r\n");
            }
            collector.append("Exception whilst collection info ").append(ex.getMessage()).append("\r\n\r\n");
        }
    }

    private void SendExeptionMessage(String msg) {
        if (msg.isEmpty()) {
            return;
        }
        if (_mailer == null) {
            SessionController sc = Utils.getBean(SessionController.class);
            if (sc != null) {
                _mailer = sc.getMailer();
            }
        }
        if (_mailer == null) {
            _logger.log(Level.SEVERE, "##### Mailer not injected #####");
            return;
        }
        String name = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
        String subject = "Exception reported by Server " + name;
        _mailer.sendMail(_sessionController.readConfig(ConfigKey.ExceptionEmail), subject, msg);
    }

}
