package org.inek.dataportal.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.data.common.ListFeature;
import org.inek.dataportal.common.data.common.ListWorkflowStatus;
import org.inek.dataportal.entities.icmt.Customer;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.facades.InfoDataFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.PortalType;
import org.inek.dataportal.common.enums.Stage;

@Named @ApplicationScoped
public class ApplicationTools extends AbstractDataAccess{

    private Properties _properties;

    private static final Logger LOGGER = Logger.getLogger("ApplicationTools");
    @Inject private ConfigFacade _config;
    @Inject private InfoDataFacade _info;

    @PostConstruct
    private void init(){
        initListFeature();
        initListWorkflowStatus();
    }
    
    private void initListFeature(){
        List<ListFeature> listFeatures = _info.findAllListFeature();
        for (Feature feature : Feature.values()) {
            if (listFeatures.stream().noneMatch(f -> f.getId() == feature.getId())){
                ListFeature listFeature = new ListFeature();
                listFeature.setId(feature.getId());
                listFeature.setName(feature.name());
                listFeature.setDescription(feature.getDescription());
                _info.saveListFeature(listFeature);
            }
        }
    }

    private void initListWorkflowStatus(){
        List<ListWorkflowStatus> listWorkflowStatus = _info.findAllListWorkflowStatus();
        for (WorkflowStatus workflowStatus : WorkflowStatus.values()) {
            if (listWorkflowStatus.stream().noneMatch(f -> f.getId() == workflowStatus.getId())){
                ListWorkflowStatus item = new ListWorkflowStatus();
                item.setId(workflowStatus.getId());
                item.setName(workflowStatus.name());
                item.setDescription(workflowStatus.getDescription());
                _info.saveListWorkflowStatus(item);
            }
        }
    }

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
            LOGGER.log(Level.WARNING, "Unexpected IOException whilst reading properties. Will use defaults.", e);
        }
    }
    
    public boolean isEnabled(ConfigKey key) {
        return _config.readConfigBool(key);
    }

    public boolean isEnabled(String name) {
        ConfigKey key = ConfigKey.valueOf(name);
        return isEnabled(key);
    }

    public boolean isFeatureEnabled(Feature feature) {
        return _config.readConfigBool(feature);
    }

    public String readConfig(ConfigKey key) {
        return _config.readConfig(key);
    }

    public int readConfigInt(ConfigKey key) {
        return _config.readConfigInt(key);
    }

    public boolean readConfigBool(ConfigKey key) {
        return _config.readConfigBool(key);
    }

    public String readPortalAddress(PortalType portalType, Stage stage){
        return _config.readPortalAddress(portalType, stage);
    }

    @Inject private CustomerFacade _customerFacade;
    private final Map<Integer, String> _hospitalInfo = new ConcurrentHashMap<>();
    public String retrieveHospitalInfo(int ik) {
        if (_hospitalInfo.containsKey(ik)){
            return _hospitalInfo.get(ik);
        }
        Customer c = _customerFacade.getCustomerByIK(ik);
        if (c == null || c.getName() == null) {
            return "";
        }
        String info =  c.getName() + ", " + c.getTown();
        _hospitalInfo.put(ik, info);
        return  info;
    }

}
