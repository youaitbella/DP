package org.inek.dataportal.backingbeans;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named @ApplicationScoped
public class ApplicationTools {
    Properties _properties;
    
    private static final Logger _logger = Logger.getLogger("ApplicationTools");

    public String getVersion() {
        ensureProjectProperties();
        return _properties.getProperty("version", "0");
    }

    private void ensureProjectProperties() {
        String rootPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");

        _properties = new Properties();
        try {
            _properties.load(new FileInputStream(rootPath + "/resources/properties/project.properties"));
        } catch (IOException e) {
            _logger.log(Level.WARNING, "Unexpected IOException whilst reading properties. Will use defaults.", e);
        }
    }

}
