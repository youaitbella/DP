/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.insurance;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.scope.FeatureScoped;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped
public class EditInsuranceMessage extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("EditInsuranceMessage");

    @Override
    protected void addTopics() {
        addTopic(NubRequestTabs.tabMessageAdress.name(), Pages.InsuranceMessageEditAddress.URL());
        addTopic(NubRequestTabs.tabMessageList.name(), Pages.InsuranceMessageEditList.URL());
    }

    enum NubRequestTabs {

        tabMessageAdress,
        tabMessageList;
    }
    // <editor-fold defaultstate="collapsed" desc="fields">
    private String _singleKhName;
    // </editor-fold>

    public EditInsuranceMessage() {
    }


    // </editor-fold>
    @PostConstruct
    private void init() {
    }

    
    public String save() {
        return "";

    }

    /**
     * provide the message to InEK
     * @return 
     */
    public String provide() {
        return "";
    }

    public boolean getProvideEnabled(){
        return true;
    }


}
