/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.feature.certification;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.feature.AbstractEditController;

/**
 *
 * @author vohldo
 */
@Named
@ConversationScoped
public class EditCert extends AbstractEditController {
    
    @Override
    protected void addTopics() {
        addTopic(CertTabs.tabCertSystemManagement.name(), Pages.CertSystemManagement.URL());
    }
    
    private enum CertTabs {
        tabCertSystemManagement;
    }
}
