/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javax.servlet.http.HttpServletResponse;
import org.inek.dataportal.common.data.account.iface.Document;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.utils.Helper;

/**
 *
 * @author muellermi
 */
public class Utils {

    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

    public static String getMessageForScript(String key) {
        return getMessage(key).replace("\r\n", "\n").replace("\n", "\\r\\n");
    }

    private static String obtainMessage(String key) {
        ResourceBundle messageBundle;
        FacesContext ctxt = FacesContext.getCurrentInstance();
        if (ctxt == null) {
            messageBundle = ResourceBundle.getBundle("org.inek.dataportal.messages");
        } else {
            messageBundle = ctxt.getApplication().getResourceBundle(ctxt, "msg");
        }
        return messageBundle.getString(key);
    }

    public static String getMessage(String key) {
        try {
            return obtainMessage(key);
        } catch (MissingResourceException e) {
            return "Unbekannter Text: " + key;
        }
    }

    public static String getMessageOrKey(String key) {
        try {
            return obtainMessage(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static String getMessageOrEmpty(String key) {
        try {
            return obtainMessage(key);
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
        LOGGER.severe(msg);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.Login.URL());

    }

    public static boolean isNullOrEmpty(String test) {
        return test == null || test.isEmpty();
    }

    public static <T> T getBean(Class<T> type) {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Application application = FacesContext.getCurrentInstance().getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        String name = type.getSimpleName().substring(0, 1).toLowerCase() + type.getSimpleName().substring(1);
        ValueExpression valueExpression = expressionFactory.createValueExpression(elContext, "#{" + name + "}", type);
        //return (T) valueExpression.getValue(elContext);
        return type.cast(valueExpression.getValue(elContext));
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

    public static UIComponent findComponent(String id) {
        UIComponent result = null;
        UIComponent root = FacesContext.getCurrentInstance().getViewRoot();
        if (root != null) {
            result = findComponent(root, id);
        }
        return result;
    }

    private static UIComponent findComponent(UIComponent root, String id) {
        UIComponent result = null;
        if (root.getId().equals(id)) {
            return root;
        }

        for (UIComponent child : root.getChildren()) {
            String containerId = child.getNamingContainer() != null ? child.getNamingContainer().getClientId() : "";
            if (!"".equals(containerId)) {
                containerId += ":";
            }
            if (child.getId().equals(containerId + id)) {
                result = child;
                break;
            }
            result = findComponent(child, id);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    public static String getClientIP() {
        FacesContext ctxt = FacesContext.getCurrentInstance();
        if (ctxt == null) {
            return "non-faces request";
        }
        HttpServletRequest request = (HttpServletRequest) ctxt.getExternalContext().getRequest();
        return getClientIp(request);
    }

    public static String getClientIp(HttpServletRequest request) {
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
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static void navigate(String URL) {
        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler nav = fc.getApplication().getNavigationHandler();
        nav.handleNavigation(fc, null, URL);
    }

    public static String downloadDocument(Document document) {
        ByteArrayInputStream is = new ByteArrayInputStream(document.getContent());
        String fileName = document.getName();
        int contentLength = document.getContent().length;
        if (!downLoadDocument(is, fileName, contentLength)) {
            return Pages.Error.URL();
        }
        return "";
    }

    public static boolean downLoadDocument(InputStream is, String fileName, int contentLength) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseHeader("Content-Type", "application/octet-stream");
        if (contentLength > 0) {
            externalContext.setResponseHeader("Content-Length", "" + contentLength);
        }
        externalContext.setResponseHeader("Content-Disposition", "attachment;filename*=UTF-8''" + encodeUrl(fileName).replace("+", "_"));
        try {
            StreamHelper.copyStream(is, externalContext.getResponseOutputStream());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return false;
        }
        facesContext.responseComplete();
        return true;
    }

    public static void downloadText(String text, String filename) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Utils.downloadText(facesContext, text, filename, "");
    }

    public static void downloadText(String text, String filename, String characterset) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Utils.downloadText(facesContext, text, filename, characterset);
    }

    public static void downloadText(FacesContext facesContext, String text, String filename, String characterset) {
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        try {
            byte[] buffer = characterset.length() == 0 ? text.getBytes() : text.getBytes(characterset);
            response.reset();
            response.setContentType(Helper.getContentType(filename));
            response.setHeader("Content-Length", "" + buffer.length);
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + encodeUrl(filename).replace("+", "_"));
            response.getOutputStream().write(buffer);
            response.flushBuffer();
            facesContext.responseComplete();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            NavigationHandler nav = facesContext.getApplication().getNavigationHandler();
            nav.handleNavigation(facesContext, null, Pages.Error.RedirectURL());
        }
    }

    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
            return url;
        }
    }

    public static String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
            return url;
        }
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public static int getTargetYear(Feature feature) {
        switch (feature) {
            case DRG_PROPOSAL:
            case PEPP_PROPOSAL:
                return LocalDateTime.now().getYear() + (LocalDateTime.now().getMonthValue() >= 6 ? 2 : 1);
            case VALUATION_RATIO:
                return LocalDateTime.now().getYear() - (LocalDateTime.now().getMonthValue() <= 6 ? 2 : 1);
            case CALCULATION_HOSPITAL:
                // here the target year is the data year which might be in the past
                return LocalDateTime.now().getYear() -1;
            case SPECIFIC_FUNCTION:
                return LocalDateTime.now().getYear();
            case NUB:
            default:
                return LocalDateTime.now().getYear() + (LocalDateTime.now().getMonthValue() >= 9 ? 1 : 0);
        }
    }

    public static String convertFromUtf8(String line) {
        return line
                .replace("\u00c3\u201e", "Ä")
                .replace("\u00c3\u2013", "Ö")
                .replace("\u00c3\u0153", "Ü")
                .replace("\u00c3\u00a4", "ä")
                .replace("\u00c3\u00b6", "ö")
                .replace("\u00c3\u00bc", "ü")
                .replace("\u00c3\u0178", "ß");
    }

    public static String convertUmlauts(String line) {
        return line
                .replace("Ä", "Ae")
                .replace("Ö", "Oe")
                .replace("Ü", "Ue")
                .replace("ä", "ae")
                .replace("ö", "oe")
                .replace("ü", "ue")
                .replace("ß", "ss")
                .replace(":", " ")
                .replace(",", " ");
    }

    public static boolean isInteger(String numString) {
        try {
            Integer.parseInt(numString);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

}
