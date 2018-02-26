/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.certification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.feature.account.entities.Account;
import org.inek.dataportal.entities.certification.AdditionalEmail;
import org.inek.dataportal.facades.certification.GrouperFacade;
import org.inek.dataportal.helper.scope.FeatureScoped;

/**
 *
 * @author vohldo
 */
@Named
@FeatureScoped(name = "Certification")
public class CertGrouperCc implements Serializable{
    
    @Inject 
    private GrouperFacade _grouperFacade;
    
    private int _selectedAccountId = 0;
    private List<AdditionalEmail> _additionalEmails;

    public int getSelectedAccountId() {
        return _selectedAccountId;
    }

    public void setSelectedAccountId(int selectedAccountId) {
        this._selectedAccountId = selectedAccountId;
    }
    
    public List<AdditionalEmail> getAdditionalEmails() {
        if(_additionalEmails == null) {
            _additionalEmails = _grouperFacade.findGrouperEmailReceivers(_selectedAccountId);
        }
        return _additionalEmails;
    }
    
    public void addNewAdditonalEmail() {
        AdditionalEmail ae = new AdditionalEmail();
        ae.setAccountId(_selectedAccountId);
        _additionalEmails.add(ae);
    }
    
    public void selectedAccountChanged() {
        _additionalEmails = null;
    }
    
    public String saveGrouperCcs() {
        _additionalEmails.removeIf(ae -> ae.getEmail().trim().isEmpty());
        _additionalEmails.stream()
                .filter(ae -> ae.getId() <= 0)
                .forEach(ae -> _grouperFacade.persist(ae));
        return "";
    }
    
    public void deleteAdditionalCc(AdditionalEmail ae) {
        _additionalEmails.removeIf(e -> e.getId() == ae.getId());
        _grouperFacade.deleteAdditionalEmailCc(ae.getId());
    }
}
