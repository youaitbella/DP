package org.inek.dataportal.base.feature.ikadmin.backingbean;

import java.io.Serializable;
import java.util.Date;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.inek.dataportal.common.data.account.entities.Account;

@Named
@ViewScoped
public class IkAdminList implements Serializable{

    
    private boolean _showDisclaimer;

    public boolean isShowDisclaimer() {
        return _showDisclaimer;
    }

    public void setShowDisclaimer(boolean showDisclaimer) {
        _showDisclaimer = showDisclaimer;
    }
    
    public void setDisclaimerDate(Account account){
        account.setIkAdminDisclaimer(new Date());
    }
}
