package org.inek.documentScanner.facade;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.AbstractFacade;
import org.inek.dataportal.common.data.account.entities.AccountDocument;
import org.inek.dataportal.common.helper.structures.DocInfo;
import org.inek.dataportal.common.utils.DateUtils;

import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

@Stateless
public class AccountDocumentFacade extends AbstractDataAccess {

    public int removeOldDocuments() {
        String sql = "DELETE FROM AccountDocument d WHERE d._validUntil < :date";
        Query query = getEntityManager().createQuery(sql, AccountDocument.class);
        query.setParameter("date", Calendar.getInstance().getTime());
        return query.executeUpdate();
    }

    public AccountDocument save(AccountDocument accountDocument) {
        persist(accountDocument);
        return accountDocument;
    }
}
