package org.inek.documentScanner.facade;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.account.entities.AccountDocument;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.Calendar;

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
