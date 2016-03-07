package org.inek.dataportal.facades.account;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.inek.dataportal.entities.account.AccountDocument;
import org.inek.dataportal.facades.AbstractFacade;
import org.inek.dataportal.helper.structures.DocInfo;
import org.inek.dataportal.utils.DateUtils;

@Stateless
public class AccountDocumentFacade extends AbstractFacade<AccountDocument> {

    public AccountDocumentFacade() {
        super(AccountDocument.class);
    }

    
    public List<DocInfo> getDocInfos(int accountId) {
        List<AccountDocument> docs = findAll(accountId);
        List<DocInfo> docInfos = new ArrayList<>();
        for (AccountDocument doc : docs) {
            docInfos.add(new DocInfo(doc.getId(), doc.getName(), doc.getDomain(), doc.getTimestamp(), doc.getValidUntil(), doc.isRead()));
        }
        return docInfos;
    }

    public List<String> getNewDocs(int accountId){
        String sql = "SELECT d._name FROM AccountDocument d WHERE d._accountId = :accountId and d._timestamp > :referenceDate ORDER BY d._adId DESC";
        Query query = getEntityManager().createQuery(sql, String.class);
        query.setParameter("accountId", accountId);
        query.setParameter("referenceDate", DateUtils.getDateWithDayOffset(-60));
        return query.getResultList();
    }
    
    public List<AccountDocument> findAll(int accountId) {
        String sql = "SELECT p FROM AccountDocument p WHERE p._accountId = :accountId ORDER BY p._adId DESC";
        Query query = getEntityManager().createQuery(sql, AccountDocument.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public boolean isDocRead(int docId) {
        AccountDocument doc = find(docId);
        if (doc.isRead()) {
            return true;
        }
        return false;
    }

}
