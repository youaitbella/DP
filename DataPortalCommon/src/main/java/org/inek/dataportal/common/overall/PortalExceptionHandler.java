package org.inek.dataportal.common.overall;

import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.EnvironmentInfo;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.Mailer;

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
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.inek.dataportal.api.helper.PortalConstants.END_PARAGRAPH;
import static org.inek.dataportal.api.helper.PortalConstants.SEPERATOR;

public class PortalExceptionHandler extends ExceptionHandlerWrapper {

    static final Logger LOGGER = Logger.getLogger(PortalExceptionHandler.class.getName());
    private final ExceptionHandler _wrapped;
    @Inject private Mailer _mailer;
    @Inject private SessionController _sessionController;
    @Inject private ApplicationTools _appTools;

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
                targetPage = handleViewExpired(exception, fc, targetPage);
            } else if (isWeldException(exception)) {
                targetPage = handleWeldException(exception, targetPage);
            } else if (exception instanceof ELException && !exception.getMessage().toLowerCase().
                    contains("not logged in")) {
                targetPage = handleElException(exception, messageCollector, targetPage);
            } else if (exception instanceof FacesException) {
                targetPage = handleFacesException(exception, messageCollector, targetPage);
            } else {
                handleOtherExeption(exception, messageCollector);
            }
            i.remove();
        }
        if (messageCollector.length() > 0) {
            messageCollector.insert(0, collectUrlInformation());
            SendExeptionMessage(messageCollector.toString());
        }
        Utils.navigate(targetPage);
        getWrapped().handle();
    }

    private String handleViewExpired(Throwable exception, FacesContext fc, String targetPage) {
        LOGGER.log(Level.SEVERE, "[View expired] " + exception.getMessage() , exception);
        ViewExpiredException viewExpiredExeption = (ViewExpiredException) exception;
        Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
        if (!viewExpiredExeption.getViewId().contains("Login.xhtml")) {
            targetPage = Pages.SessionTimeout.RedirectURL();
            requestMap.put("currentViewId", viewExpiredExeption.getViewId());
        }
        //SendExeptionMessage("[View expired] ReuqestPage: " + fc.getExternalContext().getRequestMap() + " TargetPage: " + targetPage +
          //      " / " + exception.getMessage() +  exception);
        return targetPage;
    }

    private static boolean isWeldException(Throwable exception) {
        return exception instanceof NonexistentConversationException //|| exception instanceof WeldException
                // todo: exception instanceof WeldException is fine in direct window, but does not work here.
                // thus check for exception's name as workarround
                || exception.getClass().toString().equals("class org.jboss.weld.exceptions.WeldException")
                || exception instanceof FacesException && exception.getMessage() != null && exception.getMessage().
                contains("WELD-000049:");
    }

    private String handleWeldException(Throwable exception, String targetPage) {
        String head = "[PortalExceptionHandler NonexistentConversationException] ";
        LOGGER.log(Level.SEVERE, head, exception);
        // we don't like to get this reported: collectException(messageCollector, head, exception);
        if (targetPage.isEmpty()) {
            targetPage = Pages.InvalidConversation.RedirectURL();
        }
        return targetPage;
    }

    private String handleElException(Throwable exception, StringBuilder messageCollector, String targetPage) {
        String head = "[PortalExceptionHandler ELException] ";
        LOGGER.log(Level.SEVERE, head, exception);
        messageCollector.append(ExceptionCollector.collect(head, exception));
        if (targetPage.isEmpty()) {
            targetPage = Pages.Error.RedirectURL();
        }
        return targetPage;
    }

    private String handleFacesException(Throwable exception, StringBuilder messageCollector, String targetPage) {
        String head = "[PortalExceptionHandler FacesException] ";
        LOGGER.log(Level.SEVERE, head, exception);
        messageCollector.append(ExceptionCollector.collect(head, exception));
        if (targetPage.isEmpty()) {
            if (exception.getMessage() != null && exception.getMessage().contains("javax.ejb.EJBException")) {
                targetPage = Pages.DataError.RedirectURL();
            } else {
                targetPage = Pages.Error.RedirectURL();
            }
        }
        return targetPage;
    }

    private void handleOtherExeption(Throwable exception, StringBuilder messageCollector) {
        String msg = exception.getMessage();
        if (msg == null || !msg.contains("Conversation lock timed out")
                && !msg.contains("getOutputStream() has already been called for this response")) {
            // getOutput... happens on IE, but does not affect the user
            String head = "[PortalExceptionHandler OtherException] ";
            LOGGER.log(Level.SEVERE, head, exception);
            messageCollector.append(ExceptionCollector.collect(head, exception));
        }
    }


    private StringBuilder collectUrlInformation() {
        StringBuilder collector = new StringBuilder();
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String url = request.getRequestURL().toString();
            collector.append("URL ").append(url).append(request.getQueryString()).append(END_PARAGRAPH);
            collector.append(SEPERATOR);

            String viewId = context.getViewRoot().getViewId();
            collector.append("ViewId ").append(viewId).append(END_PARAGRAPH);
            collector.append("ClientIP: ").append(Utils.getClientIP()).append("\r\n");
            if (_sessionController != null && _sessionController.isLoggedIn()) {
                collector.append("AccountId: ").append(_sessionController.getAccount()).append("\r\n");
            }
            collector.append(SEPERATOR);
        } catch (Exception ex) {
            collector.append("Exception whilst collection info ").append(ex.getMessage()).append(END_PARAGRAPH);
            collector.append(SEPERATOR);
        }
        return collector;
    }

    private void SendExeptionMessage(String msg) {
        if (_mailer == null) {
            SessionController sc = Utils.getBean(SessionController.class);
            if (sc != null) {
                _mailer = sc.getMailer();
            }
        }
        if (_mailer == null) {
            LOGGER.log(Level.SEVERE, "##### Mailer not injected #####");
            return;
        }
        String subject = "Exception reported by Server " + EnvironmentInfo.getLocalServerName();
        if (_appTools == null) {
            _mailer.sendMail(ConfigKey.ExceptionEmail.getDefault(), "[no application tools available] " + subject, msg);
        } else {
            _mailer.sendMail(_appTools.readConfig(ConfigKey.ExceptionEmail), subject, msg);
        }
    }

}
