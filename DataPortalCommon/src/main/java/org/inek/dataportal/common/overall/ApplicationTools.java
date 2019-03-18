package org.inek.dataportal.common.overall;

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
import org.inek.dataportal.common.data.icmt.entities.Customer;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.common.data.access.InfoDataFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.api.enums.PortalType;
import org.inek.dataportal.common.enums.Stage;
import org.inek.dataportal.common.helper.EnvironmentInfo;

@Named
@ApplicationScoped
public class ApplicationTools {

    private Properties _properties;

    private static final Logger LOGGER = Logger.getLogger("ApplicationTools");
    @Inject private ConfigFacade _config;
    @Inject private InfoDataFacade _info;
    @Inject private CustomerFacade _customerFacade;
    private final Map<Integer, CustomerInfo> _customerInfo = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        initListFeature();
        initListWorkflowStatus();
    }

    private void initListFeature() {
        List<ListFeature> listFeatures = _info.findAllListFeature();
        for (Feature feature : Feature.values()) {
            if (listFeatures.stream().noneMatch(f -> f.getId() == feature.getId())) {
                ListFeature listFeature = new ListFeature();
                listFeature.setId(feature.getId());
                listFeature.setName(feature.name());
                listFeature.setDescription(feature.getDescription());
                _info.saveListFeature(listFeature);
            } else {
                listFeatures.stream()
                        .filter(f -> f.getId() == feature.getId())
                        .filter(f -> !f.getName().equals(feature.name())
                        || !f.getDescription().equals(feature.getDescription()))
                        .forEach(listFeature -> {
                            listFeature.setName(feature.name());
                            listFeature.setDescription(feature.getDescription());
                            _info.saveListFeature(listFeature);
                        });
            }
        }
    }

    private void initListWorkflowStatus() {
        List<ListWorkflowStatus> listWorkflowStatus = _info.findAllListWorkflowStatus();
        for (WorkflowStatus workflowStatus : WorkflowStatus.values()) {
            if (listWorkflowStatus.stream().noneMatch(f -> f.getId() == workflowStatus.getId())) {
                ListWorkflowStatus item = new ListWorkflowStatus();
                item.setId(workflowStatus.getId());
                item.setName(workflowStatus.name());
                item.setDescription(workflowStatus.getDescription());
                _info.saveListWorkflowStatus(item);
            }
        }
    }

    public String getServerUrl(){
        return EnvironmentInfo.getServerUrl();
    }

    public String getServerWithContextUrl(){
        return EnvironmentInfo.getServerUrlWithContextpath();
    }
    /**
     *
     * @return the application version as created at compile time
     */
    public String getVersion() {
        ensureProjectProperties();
        String version = _properties.getProperty("version", "0");
        String servername = EnvironmentInfo.getLocalServerName();
        int dot = servername.indexOf(".");
        if (dot < 0) {
            dot = servername.length();
        }
        String serverIndicator = "0:0:0:0:0:0:0:1".equals(servername) ? "L" : servername.substring(dot - 1, dot);
        return version + "." + serverIndicator;
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

    public String readPortalAddress(PortalType portalType, Stage stage) {
        return _config.readPortalAddress(portalType, stage);
    }

    public String retrieveHospitalInfo(int ik) {
        ensureCustomerInfo(ik);
        return _customerInfo.get(ik).getName() + ", " + _customerInfo.get(ik).getTown();
    }

    public String retrieveHospitalName(int ik) {
        ensureCustomerInfo(ik);
        return _customerInfo.get(ik).getName();
    }

    public String retrieveHospitalTown(int ik) {
        ensureCustomerInfo(ik);
        return _customerInfo.get(ik).getTown();
    }

    public Boolean isBwHospital(int ik) {
        Customer customerByIK = _customerFacade.getCustomerByIK(ik);
        return customerByIK.getCustomerTypeId() == 45;
    }

    private void ensureCustomerInfo(int ik) {
        if (_customerInfo.containsKey(ik)) {
            return;
        }
        Customer c = _customerFacade.getCustomerByIK(ik);
        if (c == null || c.getName() == null) {
            _customerInfo.put(ik, new CustomerInfo("???", "???"));
        } else {
            _customerInfo.put(ik, new CustomerInfo(c.getName(), c.getTown()));
        }
    }

    public void cleanHospitalInfoCache() {
        _customerInfo.clear();
    }
}
