/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
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
import org.inek.dataportal.entities.Document;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.feature.nub.EditNubRequest;

/**
 *
 * @author muellermi
 */
public class Utils {

    private static final Logger _logger = Logger.getLogger(Utils.class.getName());

    public static String getMessageForScript(String key) {
        return getMessage(key).replace("\r\n", "\n").replace("\n", "\\r\\n");
    }
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
     * @return 
     */
    public static boolean showMessageInBrowser(String msg) {
        return returnMessage("alert('%s');", msg);
    }

    public static boolean confirmMessage(String msg) {
        return returnMessage("var result = confirm('%s');", msg);
    }

    private static boolean returnMessage(String template, String msg) throws FacesException {
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
            writer.write(String.format(template, msg.replace("\r\n", "\n").replace("\n", "\\r\\n")));
            writer.endEval();
            writer.endDocument();
            writer.flush();
            ctx.responseComplete();
            return true;
        } catch (Exception e) {
            throw new FacesException(e);
        }
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
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String forwardInfo = request.getHeader("X-FORWARDED-FOR");
        if (forwardInfo == null) {
            return request.getRemoteAddr();
        }
        return forwardInfo.split(",")[0];
    }

    public static String getUserAgent() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            return request.getHeader("user-agent");
        } catch (Exception ex) {
            return "unknown";
        }
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
            Logger.getLogger(EditNubRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static void navigate(String URL) {
        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler nav = fc.getApplication().getNavigationHandler();
        nav.handleNavigation(fc, null, URL);
    }

    public static String downloadDocument(Document document) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseHeader("Content-Type", "application/octet-stream");
        externalContext.setResponseHeader("Content-Length", "" + document.getContent().length);
        externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + document.getName() + "\"");
        ByteArrayInputStream is = new ByteArrayInputStream(document.getContent());
        try {
            new StreamHelper().copyStream(is, externalContext.getResponseOutputStream());
        } catch (IOException ex) {
            _logger.log(Level.SEVERE, null, ex);
            return Pages.Error.URL();
        }
        facesContext.responseComplete();
        return "";
    }

    public static String downloadDocument(String document, String name) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        try {
            byte[] buffer = document.getBytes("UTF-8");
            externalContext.setResponseHeader("Content-Type", "text/plain");
            externalContext.setResponseHeader("Content-Length", "" + buffer.length);
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + name);
            ByteArrayInputStream is = new ByteArrayInputStream(buffer);
            new StreamHelper().copyStream(is, externalContext.getResponseOutputStream());
        } catch (IOException ex) {
            _logger.log(Level.SEVERE, null, ex);
            return Pages.Error.URL();
        }
        facesContext.responseComplete();
        return "";
    }

    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            _logger.log(Level.WARNING, ex.getMessage());
            return url;
        }
    }

    public static String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            _logger.log(Level.WARNING, ex.getMessage());
            return url;
        }
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            _logger.log(Level.SEVERE, null, ex);
        }
    }

    public static int getTargetYear(Feature feature) {
        switch (feature) {
            case DRG_PROPOSAL:
            case PEPP_PROPOSAL:
                return LocalDateTime.now().getYear() + (LocalDateTime.now().getMonthValue() >= 6 ? 2 : 1);
            case NUB:
            default:
                return LocalDateTime.now().getYear() + (LocalDateTime.now().getMonthValue() >= 9 ? 1 : 0);
        }
    }

}
