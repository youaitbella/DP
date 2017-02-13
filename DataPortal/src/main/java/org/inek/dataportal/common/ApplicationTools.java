package org.inek.dataportal.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.admin.ConfigFacade;

@Named @ApplicationScoped
public class ApplicationTools {

    private Properties _properties;

    private static final Logger _logger = Logger.getLogger("ApplicationTools");
    @Inject
    private ConfigFacade _config;

    /**
     *
     * @return the application version as created at compile time
     */
    public String getVersion() {
        ensureProjectProperties();
        return _properties.getProperty("version", "0");
    }

    private void ensureProjectProperties() {
        String rootPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");

        _properties = new Properties();
        try {
            _properties.load(new FileInputStream(rootPath + "WEB-INF/classes/org/inek/dataportal/project.properties"));
        } catch (IOException e) {
            _logger.log(Level.WARNING, "Unexpected IOException whilst reading properties. Will use defaults.", e);
        }
    }
    
    public String getServer() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        url = url.toLowerCase();
        if(url.contains("localhost"))
            return " (local)";
        else if(url.contains("vdataportal01"))
            return " (vdata01)";
        else if(url.contains("vdataportal02"))
            return " (vdata02)";
        return "";
    }

    public boolean isEnabled(ConfigKey key) {
        return _config.readBool(key);
    }

    public boolean isEnabled(String name) {
        ConfigKey key = ConfigKey.valueOf(name);
        return isEnabled(key);
    }

    public boolean isFeatureEnabled(Feature feature) {
        return _config.readBool(feature);
    }

    public String readConfig(ConfigKey key) {
        return _config.read(key);
    }

    public int readConfigInt(ConfigKey key) {
        return _config.readInt(key);
    }

    public boolean readConfigBool(ConfigKey key) {
        return _config.readBool(key);
    }

    // <editor-fold defaultstate="collapsed" desc="SystemRoot">
    public File getSystemRoot(RemunerationSystem system) {
        File root = new File(_config.read(ConfigKey.CertiFolderRoot), "System " + system.getYearSystem());
        File systemRoot = new File(root, system.getFileName());
        return systemRoot;
    }
    // </editor-fold>

}
