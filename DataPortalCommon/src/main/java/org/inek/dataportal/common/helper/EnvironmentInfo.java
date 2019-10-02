package org.inek.dataportal.common.helper;

import org.inek.dataportal.api.helper.Const;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author muellermi
 */
public class EnvironmentInfo {

    public static String getServerName() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        return externalContext.getRequestServerName();
    }

    private static String serverName = "";
    public static String getLocalServerName() {

        if ("".equals(serverName)) {
            try {
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                serverName = ((ServletRequest) externalContext.getRequest()).getLocalName();
            } catch (Exception ex) {
                // we're outside of a Faces request
            }
        }
        return serverName;
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
