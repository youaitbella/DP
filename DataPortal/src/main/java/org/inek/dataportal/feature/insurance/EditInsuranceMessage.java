/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.insurance;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.servlet.http.Part;
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

    public EditInsuranceMessage() {
    }

    // </editor-fold>
    @PostConstruct
    private void init() {
    }

    public boolean getProvideEnabled(){
        return true;
    }
    public boolean getReadOnly(){
        return false;
    }
    
    
    public void addNewMessage() {
        
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

    private Part _file;
    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    public void uploadMessages() {
        try {
            if (_file != null) {
                Scanner scanner = new Scanner(_file.getInputStream(),
                        "UTF-8");
                int countSuccess = 0;
                int countFail = 0;
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                }
            }
        } catch (IOException | NoSuchElementException e) {
        }
    }


}
