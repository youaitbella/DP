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
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.insurance.InsuranceNubNotice;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.InsuranceNubNoticeFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.scope.FeatureScoped;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "Insurance")
public class EditInsuranceNubNotice extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("EditInsuranceNubNotice");

    // <editor-fold defaultstate="collapsed" desc="override AbstractEditController">    
    @Override
    protected void addTopics() {
        addTopic(NubRequestTabs.tabMessageAdress.name(), Pages.InsuranceNubNoticeEditAddress.URL());
        addTopic(NubRequestTabs.tabMessageList.name(), Pages.InsuranceNubNoticeEditList.URL());
    }

    enum NubRequestTabs {
        tabMessageAdress,
        tabMessageList;
    }
    // </editor-fold>

    @Inject private InsuranceNubNoticeFacade _noticeFacade;
    @Inject SessionController _sessionController;

    public EditInsuranceNubNotice() {
    }

    @PostConstruct
    private void init() {
        int id = 0;  // todo: determine correct id
        _notice = findFresh(id);
    }

    private InsuranceNubNotice findFresh(int id){
        InsuranceNubNotice notice = _noticeFacade.findFresh(id);
        if (notice == null){
            Account account = _sessionController.getAccount();
            notice = new InsuranceNubNotice();
            notice.setInsuranceName(account.getCompany());
            notice.setInsuranceIk(account.getIK());
            notice.setAccountId(account.getId());
        }
        return notice;
    }
    
    private InsuranceNubNotice _notice;

    public InsuranceNubNotice getNotice() {
        return _notice;
    }

    public boolean getProvideEnabled() {
        return true;
    }

    public boolean getReadOnly() {
        return false;
    }

    public void addNewMessage() {

    }

    public String save() {
        return "";

    }

    /**
     * provide the message to InEK
     *
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
