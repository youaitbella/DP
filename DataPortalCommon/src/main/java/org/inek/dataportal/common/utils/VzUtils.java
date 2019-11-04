package org.inek.dataportal.common.utils;

import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.helper.StreamHelper;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

@Named
@SessionScoped
public class VzUtils implements Serializable {

    private static final String PLACEHOLDER_HOST = "{vzHost}";
    private static final String PLACEHOLDER_TOKEN = "{vzToken}";

    private static final String PLACEHOLDER_IK = "{ik}";
    private static final String PLACEHOLDER_VZNUMBER = "{vzNumber}";

    private static final String ANSWER_OK = "ok";

    @Inject
    private ConfigFacade _config;

    private String _vzRestCheckVzNumberBaseUrl = "";

    @PostConstruct
    public void init() {
        buildRestCheckVzNumberBaseUrl();
    }

    private void buildRestCheckVzNumberBaseUrl() {
        String baseUrl = _config.readConfig(ConfigKey.VzRestCheckVzNumber);
        baseUrl = baseUrl.replace(PLACEHOLDER_HOST, _config.readConfig(ConfigKey.VzHost));
        baseUrl = baseUrl.replace(PLACEHOLDER_TOKEN, _config.readConfig(ConfigKey.VzToken));
        _vzRestCheckVzNumberBaseUrl = baseUrl;
    }

    public boolean locationCodeIsValidForIk(int ik, int locationCode) {
        if (_config.readConfigBool(ConfigKey.TestMode)) {
            return true;
        }  // todo: remove once the interface is ready

        String address = _vzRestCheckVzNumberBaseUrl.replace(PLACEHOLDER_IK, String.valueOf(ik));
        address = address.replace(PLACEHOLDER_VZNUMBER, String.valueOf(locationCode));
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != HTTP_OK) {
                throw new IOException("requestFailed [" + address + "] : " + conn.getResponseCode());
            } else {
                String answer = StreamHelper.toString(conn.getInputStream());
                return answer.equals(ANSWER_OK);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}
