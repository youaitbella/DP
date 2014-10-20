/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.context.PartialResponseWriter;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.feature.nub.EditNubProposal;

/**
 *
 * @author muellermi
 */
public class Utils {

    private static final Logger _logger = Logger.getLogger(Utils.class.getName());

    public static String getMessage(String key) {
        //FacesContext ctxt = FacesContext.getCurrentInstance();
        //ResourceBundle messageBundle = ctxt.getApplication().getResourceBundle(ctxt, "msg");
        ResourceBundle messageBundle = ResourceBundle.getBundle("org.inek.dataportal.messages");
        return messageBundle.getString(key);
    }

    public static String getMessageOrEmpty(String key) {
        try {
            return getMessage(key);
        } catch (MissingResourceException e) {
            return "";
        }
    }

    /**
     * Shows error message
     *
     * @param msg
     */
    public static boolean showMessageInBrowser(String msg) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (!ctx.getPartialViewContext().isAjaxRequest()) {
            ctx.getPartialViewContext().setPartialRequest(true);
        }
        try {
            extContext.setResponseContentType("text/xml");
            extContext.addResponseHeader("Cache-Control", "no-cache");
            PartialResponseWriter writer
                    = ctx.getPartialViewContext().getPartialResponseWriter();
            writer.startDocument();
            writer.startEval();
            writer.write("alert('" + msg + "');");
            writer.endEval();
            writer.endDocument();
            writer.flush();
            ctx.responseComplete();
            return true;
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }

    public static boolean confirmMessage(String msg) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        //if (ctx.getPartialViewContext().isAjaxRequest()) {
        try {
            extContext.setResponseContentType("text/xml");
            extContext.addResponseHeader("Cache-Control", "no-cache");
            PartialResponseWriter writer
                    = ctx.getPartialViewContext().getPartialResponseWriter();
            writer.startDocument();
            writer.startEval();
            writer.write("var result = confirm('" + msg + "');");
            writer.endEval();
            writer.endDocument();
            writer.flush();
            ctx.responseComplete();
            return true;
        } catch (Exception e) {
            throw new FacesException(e);
        }
        //}
        //return false;
    }

    public static String getServerName() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        return extContext.getRequestServerName();
    }

    public static void logMessageAndLogoff(String msg) {
        _logger.severe(msg);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.Login.URL());

    }

    public static boolean isNullOrEmpty(String test) {
        return test == null || test.isEmpty();
    }

    public static <T> T getBean(Class<?> type) {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Application application = FacesContext.getCurrentInstance().getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        String name = type.getSimpleName().substring(0, 1).toLowerCase() + type.getSimpleName().substring(1);
        ValueExpression valueExpression = expressionFactory.createValueExpression(elContext, "#{" + name + "}", type);
        return (T) valueExpression.getValue(elContext);
    }

    public static Flash getFlash() {
        return (FacesContext.getCurrentInstance().getExternalContext().getFlash());
    }

    public static void checkPassword(FacesContext context, UIComponent component, Object value) {
        UIViewRoot root = context.getViewRoot();
        String targetId = component.getNamingContainer().getClientId() + ":password";
        Object password = ((HtmlInputSecret) root.findComponent(targetId)).getValue();
        if (password != null && !password.equals("" + value)) {
            String msg = Utils.getMessage("msgPasswordMismatch");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public static String getClientIP() {
        String ip = "ip missing";
        Object request = FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request instanceof HttpServletRequestWrapper) {
            ip = ((HttpServletRequestWrapper) request).getRemoteAddr();
        } else if (request instanceof HttpServletRequest) {
            ip = ((HttpServletRequest) request).getRemoteAddr();
        }
        return ip;
    }

    public static String getUserAgent() {
        String userAgent = "unknown";
        try {
            Object request = FacesContext.getCurrentInstance().getExternalContext().getRequest();

            if (request instanceof HttpServletRequestWrapper) {
                userAgent = ((HttpServletRequestWrapper) request).getHeader("user-agent");
            } else if (request instanceof HttpServletRequest) {
                userAgent = ((HttpServletRequest) request).getHeader("user-agent");
            } else {
                userAgent = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("user-agent");
            }
        } catch (Exception ex) {
        }
        return userAgent;
    }

    public static String getChecksum(String text) {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] buffer = new byte[8192];
            int n;
            while ((n = is.read(buffer)) != -1) {
                md.update(buffer, 0, n);
            }

            byte[] mdbytes = md.digest();
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (IOException | NoSuchAlgorithmException ex) {
            Logger.getLogger(EditNubProposal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static void navigate(String URL) {
        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler nav = fc.getApplication().getNavigationHandler();
        nav.handleNavigation(fc, null, URL);
    }

}
