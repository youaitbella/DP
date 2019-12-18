package org.inek.psyEvaluationService.backingBean;

import javax.ejb.Asynchronous;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Named
public class MessageProvider {

    private final int MaxMessages = 200;
    private final LinkedList<String> _messages = new LinkedList<>();
    protected static final Logger LOGGER = Logger.getLogger("MessageProvider");

    @Asynchronous
    public void reportMemory() {
        final int MB = 1024 * 1024;
        Runtime rt = Runtime.getRuntime();
        addMessage("Processors: " + rt.availableProcessors()
                + " - Memory (MB) max: " + rt.maxMemory() / MB
                + ", total: " + rt.totalMemory() / MB
                + ", free: " + rt.freeMemory() / MB
                + ", used: " + (rt.totalMemory() - rt.freeMemory()) / MB);
    }

    public void addMessageAndReportMemory(String message) {
        addMessage(message);
        reportMemory();
    }

    public synchronized void addMessage(String message) {
        String currentTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date());
        _messages.addFirst(currentTS + " - " + message);
        if (_messages.size() > MaxMessages) {
            _messages.pollLast();
        }
        LOGGER.log(Level.INFO, message);
    }

    public synchronized List<String> getMessages() {
        return _messages;
    }


    private Properties _properties;

    /**
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
            _properties.load(new FileInputStream(rootPath + "/WEB-INF/classes/org/inek/psyEvaluationService/project.properties"));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unexpected IOException whilst reading properties. Will use defaults.", e);
        }
    }

    public String getHostInfo() {
        String hostName = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
        String home = System.getProperty("catalina.home");  // GlassFish|Payara only
        String domain = home == null ? "" : home.substring(1 + home.lastIndexOf("\\"));
        return hostName + " " + domain;
    }

    public String getJavaVersion() {
        return System.getProperty("java.version");
    }

    public String getServerVersion() {
        return System.getProperty("glassfish.version");
    }

}
