package org.inek.dataportal.common.helper;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.inek.dataportal.api.helper.Const;

/**
 *
 * @author muellermi
 */
public class EnvironmentInfo {

    public static String getServerName() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        return externalContext.getRequestServerName();
    }

    public static String getLocalServerName() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        return ((ServletRequest) externalContext.getRequest()).getLocalName();
    }

    public static String getServerUrl() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String protocol = externalContext.getRequestScheme() + "://";
        int port = externalContext.getRequestServerPort();
        String server = externalContext.getRequestServerName();
        return protocol + server + (port == Const.HTTP_PORT || port == Const.HTTPS_PORT ? "" : ":" + port);
    }

    public static String getServerUrlWithContextpath() {
        String path = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).
                            getContextPath();
        return getServerUrl() + path;
    }

}
