package org.inek.dataportal.common.helper;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import static org.inek.dataportal.common.helper.Const.HTTPS_PORT;
import static org.inek.dataportal.common.helper.Const.HTTP_PORT;

/**
 *
 * @author muellermi
 */
public class EnvironmentInfo {

    public static String getServerName() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        return externalContext.getRequestServerName();
    }

    public static String getServerUrl() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String protocol = externalContext.getRequestScheme() + "://";
        int port = externalContext.getRequestServerPort();
        String server = externalContext.getRequestServerName();
        return protocol + server + (port == HTTP_PORT || port == HTTPS_PORT ? "" : ":" + port);
    }

    public static String getServerUrlWithContextpath() {
        String path = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).
                            getContextPath();
        return getServerUrl() + path;
    }

}
