/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.utils.timed;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import org.inek.dataportal.entities.account.AccountRequest;
import org.inek.dataportal.entities.DropBox;
import org.inek.dataportal.entities.PasswordRequest;
import org.inek.dataportal.facades.account.AccountRequestFacade;
import org.inek.dataportal.facades.DropBoxFacade;
import org.inek.dataportal.facades.PasswordRequestFacade;
import org.inek.dataportal.utils.DateUtils;

/**
 *
 * @author muellermi
 */
@Singleton
public class DatabaseCleaner {

    private static final Logger _logger = Logger.getLogger("DatabaseCleaner");
    @EJB private AccountRequestFacade _requestFacade;
    @EJB private PasswordRequestFacade _pwdRequestFacade;
    @EJB private DropBoxFacade _dropBoxFacade;

    @Schedule(hour = "*", minute = "*/15", info = "every 15 minutes")
    public void cleanDatabase() {
        cleanAccountRequests();
        cleanPasswordRequests();
        cleanDropBoxes();
        // un-comment following line to clrear cache periodically
        // _requestFacade.clearCache();
    }

    private void cleanAccountRequests() {
        List<AccountRequest> requests = _requestFacade.findRequestsOlderThan(DateUtils.getDateWithDayOffset(-3));
        for (AccountRequest request : requests) {
            _logger.log(Level.WARNING, "Clean request {0}, {1}", new Object[]{request.getAccountId(), request.getUser()});
            _requestFacade.remove(request);
        }
    }

    private void cleanPasswordRequests() {
        List<PasswordRequest> requests = _pwdRequestFacade.findRequestsOlderThan(DateUtils.getDateWithMinuteOffset(-60));
        for (PasswordRequest request : requests) {
            _logger.log(Level.WARNING, "Clean password request {0}", request.getAccountId());
            _pwdRequestFacade.remove(request);
        }
    }

    private void cleanDropBoxes() {
        List<DropBox> dropboxes = _dropBoxFacade.findInvalid();
        for (DropBox dropbox : dropboxes) {
            _logger.log(Level.WARNING, "Clean DroBox {0}", dropbox.getDropBoxId());
            _dropBoxFacade.remove(dropbox);
        }
    }
}